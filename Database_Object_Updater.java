/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;

/**
 *Object for updating object in java
 * @author jakubwawak
 */
public class Database_Object_Updater {
    
    Database database;
    String mode;
    ArrayList<Tick_Brick> updated_data;
    int object_id;
    
    
    /**
     * Constructor of the object
     * @param mode
     * @param object_id
     * @param database 
     * modes:
     * category
     * tag
     * hashtag table
     * place
     */
    Database_Object_Updater(String mode,int object_id,Database database){
        
        this.database = database;
        this.mode = mode;
        this.object_id = object_id;
        
    }
}
