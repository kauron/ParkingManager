/** Class Parking represents a set of parking spaces along with the fee per minute
  * @author IIP
  * @version 2014/2015
  */

import java.util.*;
import java.io.*;

public class Parking {
    
    // COMPLETE ATTRIBUTES
    private Floor[] floors;
    private int numSpaces;
    private double fee;
    private boolean saved, namefromfile;
    private String fileName, lastFileName, name;
    
    /** Constructor that receives number of floors, number of spaces by floor, and fee in euros by minute
    * Parking is empty at the beginning
    * @param fl number of floors, fl>0
    * @param sp number of spaces by floor, sp>0
    * @param f fee in euros by minute, f>0
    */
    public Parking(int fl, int sp, double f, String name){
        numSpaces = sp;
        floors = new Floor[fl];
        for(int i = 0; i < fl; i++) floors[i] = new Floor(i, sp);
        fee = f;
        this.name = name;
        notSaved();
        namefromfile = false;
    }
    
    /** Constructor that creates parking from data in a file, whose name is passed as parameter
    *
    * File format:
    *
    * floors spaces
    * fee
    * floor plate hour minute
    * ...
    * floor plate hour minute
    *
    * Correct data (no repeated cars or spaces, correct and not full floor, and hour) are guaranteed
    * @param fn file name with data
    */
    public Parking(String fn) throws Exception {
        File file = new File(fn);
        name = fn;
        Scanner in=new Scanner(file).useLocale(Locale.US);
        int fl, sp, h, m;
        double f;
        String pl;
    
        fl=in.nextInt(); sp=in.nextInt(); f=in.nextDouble();
    
        floors=new Floor[fl];
        for (int i=0;i<fl;i++)
            floors[i]=new Floor(i,sp);
          numSpaces=sp;
        fee=f;
        
        while (in.hasNext()) {
            fl=in.nextInt(); pl=in.next(); h=in.nextInt(); m=in.nextInt();
            floors[fl].enterCar(pl,new Hour(h,m));
        }
    
        in.close();
        saved(file.getAbsolutePath());
        namefromfile = true;
    }
    
    /** Get number of floors
    * @return int number of floors in the parking
    */
    public int getFloors(){return floors.length;}
    
    /** Get number of spaces by floor
    * @return int number of spaces by floors in the parking
    */
    public int getSpacesByFloor(){return numSpaces;}
    
    /** Get fee
    * @return double fee of the parking (euros/minute)
    */
    public double getFee(){return fee;}
    
    /**Check if the parking has already been saved
     * @return boolean Has it been saved?
     */
    public boolean isSaved(){return saved;}
    
    public String getFileName(){return fileName;}
    public String getLastFileName(){return lastFileName;}
    
    public void setName(String name){this.name = name;}
    public String getName(){return name;}
    
    private void notSaved(){
        if(saved && namefromfile) name += "*";
        saved = false;
        if(fileName != null)
            lastFileName = new String(fileName);
        fileName = null;
    }
    
    private void saved(String newFileName){
        if(!saved && namefromfile) name = name.substring(0, name.length() - 1);
        saved = true;
        if(fileName != null)
            lastFileName = new String(fileName);
        fileName = newFileName;
    }
    
    /** Set fee
    * @param f new fee (euros/minute) of the parking, f>0
    */
    public void setFee(double f){ if(f>0) fee = f; }
    
    /** Enter car in a given floor preference, returns true when car enters, false otherwise
    * If floor preference is not available, search for free spaces in the nearest floors
    * @param c plate of the car to be entered
    * @param h hour when the car enters
    * @param f floor preference
    * @return boolean with true when car entered, false otherwise
    */
    public boolean enterCar(String c, Hour h, int f){
        int floor = -1;
        int space = floors[f].freeInFloor();
        int deviation = Math.max(f + 1, floors.length - f);
        for(int i = 0; i < deviation && floor == -1; i++){
            if(f + i < floors.length && floors[f + i].freeInFloor() != -1){
                floor = f + i;
                break;
            }
            if(f - i >= 0 && floors[f - i].freeInFloor() != -1){
                floor = f - i;
                break;
            }
        }
        if(floor==-1){return false;}
        floors[floor].enterCar(c, h);
        notSaved();
        return true;
    }
    
    /** Searches if the car is in the parking from car plate
    * @param c plate of the car to be searched
    * @return ParkingSpace where car is, null when not found
    */
    public ParkingSpace searchCar(String c){
        for(int i = 0; i < floors.length; i++){
            ParkingSpace ps = floors[i].searchCar(c);
            if(ps != null) return ps;
        }
        return null;
    }
    
    /** Exits car from the parking and returns its fee
    * @param c car to exit, precondition: always present
    * @param h hour when exit is produced, precondition: later than hour when car entered
    * @return double fee in euros to be paid
    */
    public double exitCar(String c, Hour h){
        int f = -1;
        for(int i = 0; i < floors.length; i++){
            if(floors[i].searchCar(c) != null) f = i;
        }
        notSaved();
        return fee * floors[f].exitCar(c, h);
    }
    
    /** String representation to show parking occupation, with 'X' occupied, ' ' free
    * Must place a header with the corresponding spaces numbers
    * Example: "      0   1   2   3   4 
    *             0   X   X       X    
    *             1       X   X       X
    *             2   X   X             "
    * @return String with parking representation
    */
    public String toString(){
        String s = String.format("%s\n%3s", name, "");
        for(int i = 0; i < numSpaces; i++){
            s += String.format("%4s", i);
        }
        s += "\n";
        for(int j = 0; j < floors.length; j++){
            s += floors[j].toString();
        }
        return s;
    }
    
    /** Empties all parking, supposing we are at 23:59, and calculates and returns the global fee
    * @return double total fee in euros to be paid for the whole set of cars
    */
    public double emptyParking(){
        Hour h = new Hour(23, 59);
        int minutes = 0;
        for(int i = 0; i < floors.length; i++){
            minutes += floors[i].emptyAll(h);
        }
        notSaved();
        return fee * minutes;
    }
    
    public boolean save(File file){
        //store the formatted data in a String and then write it
        String saveThis = floors.length + " " + numSpaces + "\n" + fee + "\n";
        for(int i = 0; i < floors.length; i++)
            saveThis += floors[i].printFile();
        BufferedWriter bWriter;
        try{
            bWriter = new BufferedWriter(new FileWriter(file));
            bWriter.write(saveThis);
            bWriter.close();
            saved(file.getAbsolutePath());
            return true;
        }catch(Exception e){
            System.out.println("There was an error");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
