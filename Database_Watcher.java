/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

/**
 *Object for watching database data
 * @author jakubwawak
 */
public class Database_Watcher {
    
    /**
     * Constructor
     * @param mode
     * modes:
     * "lists" - checks lists activity
     */
    String mode;
    Database database;
    
    Database_Watcher(String mode,Database database){
        
        this.mode = mode;
        this.database = database;
        
    }
    
    /**
     * Main mode selector for object
     */
    void mode(){
        
        if (mode.equals("lists")){
            //list_watcher();
        }
    }
    
    
    /**
     * Method for checking activity on lists
     * @return boolean
     */

    
}
