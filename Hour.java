/** Class Hour.
 *  @author (Carlos Galindo)
 *  @mail (cargaji@inf.upv.es)
 *  @version (Year 2014-15)
 */
public class Hour {
    // ATTRIBUTES:
    private int hh, mm;
    private static int UTC = 0;
    // CONSTRUCTORS:
   /** Hour corresponding to hh hours and mm minutes.
     * Precondition: 0<=hh<24, 0<=mm<60
     * @param int Hour of the hour object
     * @param int Minutes of the hour object
     */
    public Hour(int hh, int mm){
        if(hh>=0 && hh<24 && mm>=0 && mm<60){
            this.hh = hh;
            this.mm = mm;
        }
    }
 
   
   /** Current UTC Hour (hours and minutes) 
     */
     public Hour() {
         int s = (int) ((System.currentTimeMillis()/1000) % (3600*24));
         this.hh = s / 3600 + UTC;
         if(this.hh >= 24){ this.hh -= 24; }
         this.mm = s % 3600 / 60;
     }
    // CONSULTORS AND MODIFYERS:
   /** Returns hour from current Hour object
      * @return int Hour of the current hour
      */
    public int getHour(){ return this.hh; }
  

   /** Returns minutes from current Hour object
    * @return int Minutes of the current hour
    */
   public int getMinutes(){ return this.mm; }
  
   
   /** Modifies hour of current Hour object 
     * @param int Hour of the hour object, limited to 0<=h<24
     */
   public void setHour(int hh){ if(hh>=0 && hh<24){ this.hh = hh; } }
  
   
   /** Modifies minutes of current Hour object 
     * @param int Minutes of the hour object, limited to 0<=m<60 
     */
   public void setMinutes(int mm){ if(mm>=0 && mm<60){ this.mm = mm; } } 
   
   // OTHER METHODS:
   /** Returns current Hour object in format "hh:mm"
    * @return String Hour in format "hh:mm"
      */
    
    public String toString(){
        return hh/10 + "" + hh%10 + ":" + mm/10 + "" + mm%10;
    }
    
   
  /** Returns amount of minutes from 00:00 to current Hour object
   * @return int Minutes from last midnight
    */
    public int toMinutes(){
       return hh*60 + mm;
    }
  

  /** Returns true only if o and current Hour object represent the same hour
   * @param Object An hour to be compared with the first one
   * @return boolean True if the object is an Hour and both the hour and the minutes are equal in both
    */
   public boolean equals(Object o){
       return o instanceof Hour
        && this.hh==((Hour)o).hh
        && this.mm==((Hour)o).mm;
    }


  /** Compares chronologically current Hour object and hour; result is:
    *      - negative when current Hour is previous to hour
    *      - zero if they are equal
    *      - positive when current Hour is posterior to hour
    *      @param Hour An hour to be compared
    *      @return int Returns the difference in minutes between the hours (0 if equal).
    */
   public int compare(Hour hour){
       return this.toMinutes() - hour.toMinutes();
    }

  // EXTRA ACTIVITY:      
  /** Returns Hour from textual data
   *  in format "hh:mm".
   *  @param String Format must be "hh:mm"
   *  @return Hour The object created with the data from the parameter.
   */ 
  public static Hour valueOf (String s){
      if(!s.matches("\\d{2}[:]\\d{2}"))return null;
      Hour hour = new Hour(Integer.valueOf(s.substring(0, 2)), Integer.valueOf(s.substring(3)));
      return hour;
  }
}
