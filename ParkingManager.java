import java.util.*;
import java.io.*;

/** Class ParkingManager: manager for a parking
  * @author IIP
  * @version 2014/2015
  */
public class ParkingManager{
    static Scanner kbd = new Scanner(System.in).useLocale(Locale.US);
    static Parking[] parking;
    static int current;
    
    //The hour input recognition is implemented in Hour.java
    
    public static void main(String [] args) throws Exception {
        //initializes the parking from a file
        parking = new Parking[] {new Parking("parkingIIP.txt")};
        current = 0;
        System.out.println("\nWelcome to the IIP Parking Manager\nA sample parking has been loaded\n");
        menu();
    }
    
    private static void menu(){
        while(true){
            System.out.print("Input an order >  ");
            switch(kbd.nextLine().toLowerCase().trim()){
                case "enter": enterCar(); break;
                case "exit": exitCar(); break;
                case "search": searchCar(); break;
                case "show": System.out.println(parking[current]); break;
                case "empty": System.out.printf("Parking is now empty, the total fee is %.2f€ total\n", 
                                parking[current].emptyParking()); break;
                case "new": newParking(); break;
                case "load": load(); break;
                case "save": save(); break;
                case "delete": delete(); break;
                case "change": change(); break;
                case "rename": rename(); break;
                case "list": list(); break;
                case "help": help(); break;
                case "quit": System.out.println("Goodbye!"); return;
                default: System.out.println("ERROR! Try inputting \"help\""); break;
            }
        }
    }
    
    private static void enterCar(){
        //ask for a valid plate, enter hour and floor
        System.out.print("Input the car plate >  ");
        String plate = kbd.nextLine().trim();
        
        Hour hour;
        do{
            System.out.print("Input the entering hour in format hh:mm >  ");
            hour = Hour.valueOf(kbd.nextLine());
        }while(hour == null);
        
        int floor = -1;
        do{
            System.out.printf("Input the floor in which you want to park [0, %d] >  ", parking[current].getFloors() - 1);
            floor = (int)scanNum(false);
        }while(floor < 0 || floor >= parking[current].getFloors());
        //search for a car with the same plate
        if(parking[current].searchCar(plate) == null){
            //not present, try to allocate the car
            if(parking[current].enterCar(plate, hour, floor)){
                //allocated > show info
                System.out.println("Success!\n" + parking[current].searchCar(plate).toString());
            }else{
                //not allocated > show error
                System.out.println("No free spaces in the parking, sorry");
            }
        }else{
            //present > show error
            System.out.println("Car was already inside parking");
        }
    }
    
    private static void exitCar(){
        //get car plate
        System.out.print("Input the car plate >  ");
        ParkingSpace place = parking[current].searchCar(kbd.nextLine().trim());
        if(place == null){
            //not present > error
            System.out.println("That car is not in the parking");
        }else{
            //present > input hour > exit and show exit data
            Hour hour;
            do{
                System.out.print("Input the hour in format hh:mm >  ");
                hour = Hour.valueOf(kbd.nextLine());
            }while(hour == null);
            System.out.println(place);
            System.out.println("Exit hour: " + hour);
            System.out.printf("Cost: %.2f€\n", parking[current].exitCar(place.getCarPlate(), hour));
        }
    }
    
    private static void searchCar(){
        //input carplate
        System.out.print("Input the car plate >  ");
        String plate = kbd.nextLine().trim();
        //look for it
        ParkingSpace sp = parking[current].searchCar(plate);
        System.out.println(sp != null ? sp.toString() : "That car is not in the parking");
    }
    
    private static void newParking(){
        int fl = -1, sp = -1;
        double f = -1;
        System.out.print("Name the parking >>  ");
        String name = kbd.nextLine();
        do{
            System.out.print("Input the number of floors >> ");
            fl = (int)scanNum(false);
        }while(fl < 1);
        do{
            System.out.print("Input the number of spaces per floor >> ");
            sp = (int)scanNum(false);
        }while(sp < 1);
        do{
            System.out.print("Input the fee per minute in euros >> ");
            f = scanNum(true);
        }while(f <= 0);
        if(parking[current] != null){
            boolean overWrite = overwrite();
            if(overWrite){
                System.out.printf("Parking is now empty, the total fee is %.2f€ total\n", parking[current].emptyParking());
                System.out.println("Parking is being demolished...");
                try{Thread.sleep(1000);}
                catch(Exception e){}
                System.out.println("Done!");   
            }
            System.out.println("Building new parking...");
            try{Thread.sleep(1000);}
            catch(Exception e){}
            if(overWrite)
                parking[current] = new Parking(fl, sp, f, name);
            else
                add(new Parking(fl, sp, f, name));
            System.out.println("Operation completed successfully!");
        }else{
            System.out.println("Building new parking...");
            try{Thread.sleep(1000);}
            catch(Exception e){}
            add(new Parking(fl, sp, f, name));
            System.out.println("Operation completed successfully!");
        }
    }
    
    private static void load(){
        System.out.print("Name of the file to import > ");
        try{
            if(overwrite())
                add(new Parking(kbd.nextLine()));
            else
                parking[current] = new Parking(kbd.nextLine());
        }catch(Exception e){
            System.out.println("There was an error, that's all we know");
            System.out.println(e.getMessage());
            System.out.println("The good news are that the previous data hasn't been lost");
        }
    }
    
    public static boolean overwrite(){
        System.out.println("Do you want to overwrite the data? [Y/N] >  ");
        return kbd.nextLine().trim().toLowerCase().equals("y");
    }
    
    private static void save(){
        if(parking[current].isSaved())
            System.out.println("Remember that your file is already saved as " + parking[current].getFileName());
        System.out.print("Input the filename where this parking is going to be saved >  ");
        File file = new File(kbd.nextLine());
        //first, warning if the file exists
        if(file.exists() && overwrite()) return;
        //save and confirm if ok
        if(parking[current].save(file))
            System.out.println("Success!");
    }
    
    private static void add(Parking p){
        System.out.println("The index of the new parking is " + parking.length);
        Parking[] aux = new Parking[parking.length + 1];
        for(int i = 0; i < parking.length; i++)
            aux[i] = parking[i];
        parking = aux;
        current = parking.length - 1;
        parking[parking.length - 1] = p;
    }
    
    private static void delete(){
        if(parking.length == 1){
            System.out.println("This is your last parking. If you want to get rid of it, \"quit\" or \"add\" another parking");
            return;
        }else if(!overwrite()){ return;
        }else{
            Parking[] aux = new Parking[parking.length - 1];
            for(int i = 0; i < aux.length; i++)
                aux[i] = parking[i];
            parking = aux;
            change();
        }
    }
    
    private static void change(){
        if(parking.length == 1){ System.out.println("There are no other parkings."); return;}
        int i;
        list();
        do{
            System.out.printf("Input the index/name of parking you want to manage [0, %d] > ", parking.length - 1);
            String name = kbd.nextLine().trim();
            try{
                i = Integer.parseInt(name);
            }catch(Exception e){
                i = searchParking(name);
            }
        }while(i < 0 || i >= parking.length);
        current = i;
    }
    
    private static void rename(){
        boolean ok = true;
        System.out.println("Input the new name >>  ");
        String name = kbd.nextLine().trim();
        if(searchParking(name) == -1)
            parking[current].setName(name);
        else
            System.out.println("This name is repeated, please try again");
    }
    
    private static int searchParking(String name){
        for(int i = 0; i < parking.length; i++)
            if(parking[i].getName().equals(name)) return i;
        return -1;    
    }
    
    private static void list(){
        for(int i = 0; i < parking.length; i++)
            System.out.printf("[%d]\t%s\n", i, parking[i].getName());
    }
    
    private static double scanNum(boolean decimal){
        while(true)
            try{
                if(decimal)
                    return Double.parseDouble(kbd.nextLine().trim());
                else
                    return Integer.parseInt(kbd.nextLine().trim());
            }catch(Exception e){
                System.out.println("Please, input an integer");
            }
    }
    
    private static void help(){
        System.out.println("Parking manager v1.0\n\n" +
                            "enter\tTake a car inside the parking\n" +
                            "exit\tTake a car outside the parking\n" +
                            "show\tShow the occupation level of the parking\n" +
                            "search\tGet the location of a car if it is inside the parking\n" +
                            "empty\tGet all the cars out at 23:59\n\n" +
                            "load\tLoad a parking setting from a given file\n" +
                            "save\tSave the current parking setting to a file\n\n" +
                            "new\tAdd a new parking (without cars) to manage various parkings\n" +
                            "change\tChange to manage a different parking from the ones you have active\n" +
                            "list\tList the names of the parkings you currently have\n" +
                            "rename\tChange the name of the parking you are currently working with\n" + 
                            "delete\tErase completely a parking from the list\n\n" +
                            "quit\tExit the parking manager");
    }
}