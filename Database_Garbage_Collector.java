/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

/**
 *Object for deleting data from the database
 * @author jakub
 */
public class Database_Garbage_Collector {
    
    Database database;
    String mode;
    
    // main constructor
    Database_Garbage_Collector(Database database,String mode){
        this.database = database;
        this.mode = mode;
    }
    
}
