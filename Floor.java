/** Class Floor represents a set of parking spaces in a floor of the parking
  * @author IIP
  * @version 2014/2015
  */

import java.util.*;
import java.io.*;

public class Floor{
   
    // COMPLETE ATTRIBUTES
    private int floor;
    private ParkingSpace spaces[];
  

    /** Constructor that receives number of floor and number of spaces of the floor
     * Floor is empty at the beginning
     * @param fl floor number, fl>=0
     * @param sp number of spaces by floor, sp>0
     */
    public Floor(int fl, int sp){
        if(sp>0 && fl>=0){
            floor = fl;
            spaces = new ParkingSpace[sp];
            for(int i = 0; i < sp; i++){
                spaces[i] = new ParkingSpace(fl, i);
            }
        }
    }
    
    /** Get floor number
     * @return int floor number of the parking
     */
    public int getFloor(){return floor;}
    
    /** Search for free space in the floor
     * @return int index of first free parking space found in the floor, -1 when no one is free
     */
    public int freeInFloor(){
        for(int i = 0; i < spaces.length; i++){
            if(spaces[i].isEmpty()){return i;}
        }
        return -1;
    }
    
    /** Enter car in the floor and returns true, false when no free spaces or repeated car
     * @param c plate of car to be entered
     * @param h hour when the car enters
     * @return boolean true when car entered, false when not
     */
    public boolean enterCar(String c, Hour h){
        int place = freeInFloor();
        if(place >= 0 && searchCar(c) == null){
            spaces[place].enterCar(c, h);
            return true;
        }else{return false;}
    }

    /** Searches if the car is in the parking from car object
     * @param c plate of the car to be searched
     * @return ParkingSpace where car is, null when not found
     */
    public ParkingSpace searchCar(String c){
        for(int i = 0; i < spaces.length; i++){
            if(c.equals(spaces[i].getCarPlate())) return spaces[i];
        }
        return null;
    }

    /** Exits car from the parking, returning how many minutes it was
     * @param c plate of the car to exit, precondition: always present
     * @param h hour when exit is produced, precondition: later than hour when car entered
     * @return int number of minutes that was present
     */
    public int exitCar(String c, Hour h){
        ParkingSpace ps = searchCar(c);
        int dif = h.compare(ps.getEnterHour());
        if(ps != null && dif > 0){
            ps.exitCar();
            return dif;
        }
        return -1;
    }

    /** Returns String with parking occupation, with 'X' occupied, ' ' free
     * Format: floor number (occupying 3 positions), space, occupation ("  X" or "   "), space, ..., occupation ("  X" or "   "), space, '\n'
     * Format example (5 spaces): "  2   X       X   X      "
     * Shows floor and space number indicator
     */
    public String toString(){
        String res = String.format("%3d", floor);
        for(int i = 0; i < spaces.length; i++){
            if(spaces[i].isEmpty()) res += String.format("%4s", " ");
            else res += String.format("%4s", "X");
        }
        return res + " \n";
    }

    /** Empties all floor, returns total number of minutes of the cars for the given hour
     * @param h Hour when all cars must exit; precondition: posterior to car enter hour
     * @return int total number of minutes that was present
     */
    public int emptyAll(Hour h){
        int min = 0;
        for(int i = 0; i < spaces.length; i++){
            if(!spaces[i].isEmpty()){
                min += h.compare(spaces[i].getEnterHour());
                spaces[i].exitCar();
            }
        }
        return min;
    }
    
    public String printFile(){
        String res = "";
        for(int i = 0; i < spaces.length; i++){
            res += spaces[i].printFile();
        }
        return res;
    }
}
