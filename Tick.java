/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.io.IOException;
import java.sql.SQLException;
import javax.mail.MessagingException;

/**
 *Main program
 * @author jakub
 */
public class Tick {

    static final String version = "v1.0.0B10";
    
    static Tick_Log session_log;
    static Database database;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException, MessagingException {
        System.out.println("TICK "+version);
        
// initialization of the modules
        // -- Tick_Log
        session_log = new Tick_Log();
        // -- Database
        database = new Database(session_log);
        
        if (database.connected){
            new CUI_Tick_Inteface(database).run();
        }
        else{
            System.out.println("Failed to connect to the database. Check log file.");
            session_log.add("Failed to connect to the database",  "TICK "+version);
        }

        session_log.save();
        
    }
    
}
