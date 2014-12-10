/** Class ParkingSpace defines the parking space occupancy
  * @author IIP
  * @version Year 2014/2015
  */

public class ParkingSpace {

    // COMPLETE ATTRIBUTES
    private int number, floor;
    private String carPlate;
    private Hour enterHour;

    /** Constructor that initialises an empty parking space
     * with a given floor and number
     * @param f Floor number of the parking space
     * @param n Parking space number
     */
    public ParkingSpace(int f, int n){
        floor = f;
        number = n;
    }

    /** Constructor that initialises a filled parking space
     * @param c String with the plate of the car that is inside the parking space
     * @param h Hour when the car entered
     * @param f Floor number of the parking space
     * @param n Parking space number
     */
    public ParkingSpace(String c, Hour h, int f, int n){
        carPlate = c;
        enterHour = h;
        floor = f;
        number = n;
    }

    /** Get car plate
     * @return plate of the car that is inside the parking space, null when empty 
     */
    public String getCarPlate(){return carPlate;}

    /** Get enter hour
     * @return hour when the car entered the parking space, null when empty 
     */
    public Hour getEnterHour(){return enterHour;}

    /** Get floor
     * @return floor of the parking space
     */
    public int getFloor(){return floor;}

    /** Get number
     * @return number of the parking space
     */
    public int getNumber(){return number;}

    /** Set car plate
     * @param String with car plate to put inside the parking space
     */
    public void setCarPlate(String c){carPlate = c;}

    /** Set enter hour
     * @param Hour ref to the hour when the car entered the parking space
     */
    public void setEnterHour(Hour h){enterHour = h;}

    /** Enter car in the parking space
     * @param String with car plate to put inside the parking space
     * @param Hour ref to the hour when the car entered the parking space
     */
    public void enterCar(String c, Hour h){setEnterHour(h); setCarPlate(c);}

    /** Exit car in the parking space
     */
    public void exitCar(){carPlate = null; enterHour = null;}

    /** Empty parking space
     * @return boolean true when empty
     */
    public boolean isEmpty(){return carPlate == null && enterHour == null;}

    /** String representation for the parking space
     * Format: "Car with plate CARPLATE in place FLOOR-NUMBER, enter in ENTERHOUR"
     * @return String representation
     */
    public String toString(){
        return String.format("Car with carplate %s in place %d-%d, enter in %s", carPlate, floor, number, enterHour.toString());
    }
    
    public String printFile(){
        if(enterHour != null && carPlate != null)
            return String.format("%s %s %s %s\n", floor, carPlate, enterHour.getHour(), enterHour.getMinutes());
        else return "";
    }
}
