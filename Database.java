/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *Object for maintaining connection to the database.
 * @author jakub
 */
public class Database {
    
    final String version = "v0.0.1";
    
    final String HEADER = "DATABASE ("+version+")";
    
    Connection con = null;          // connection to the database
    ResultSet rs = null;            // result set of the query
    boolean connected = false;
    
    Tick_Log log;                   // program log saver
    
    /**
     * Main Database maintaining connection object
     * @param log 
     */
    Database(Tick_Log log){
        
        this.log = log;
        
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost/tick_database?" +
                                   "user=root&password=password");
            this.log.add("Connected: "+con.toString(),"DATABASE");
            connected = true;
        
        }catch(SQLException ex){
            this.log.add("SQLException: " + ex.getMessage(),HEADER);
            this.log.add("SQLState: " + ex.getSQLState(),HEADER);
            this.log.add("VendorError: " + ex.getErrorCode(),HEADER); 
            connected = false;
        }
    }
    //----------------------------ADDING TO THE DATABASE
}
