/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *Main program
 * @author jakub
 */
public class Tick {

    static final String version = "v0.0.1";
    
    static Tick_Log session_log;
    static Database database;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("TICK "+version);
        
// initialization of the modules
        // -- Tick_Log
        session_log = new Tick_Log();
        // -- Database
        database = new Database(session_log);
        
        new CUI_Tick_Inteface(database).run();
   
        session_log.save();
    }
    
}
