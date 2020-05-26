/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import com.mysql.jdbc.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *Object for maintaining connection to the database.
 * @author jakub
 */
public class Database {
    
    final String version = "v0.0.1B";
    
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
    //----------------------------maintanance and optional methods
    /**
     * Database.update_user_logins(Tick_User user)
     * @param user
     * @throws SQLException 
     */
    void update_user_logins(Tick_User user) throws SQLException{
        String query = "update CONFIGURATION SET sum_entries = sum_entries + 1 WHERE owner_id = ?;";
        
        this.log.add("Updating sum_entries value on database...", HEADER);
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1, user.owner_id);
        ppst.execute();
    }
    /**
     * Database.get_debug_info(int owner_id)
     * @param user
     * @return int
     * @throws SQLException
     * Returns debug info number from database
     */
    int get_debug_info(Tick_User user) throws SQLException{
        String query = "SELECT debug FROM CONFIGURATION where owner_id = ?";
        this.log.add("Getting debug info from database", HEADER);
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1, user.owner_id);
        ResultSet rs = ppst.executeQuery();
        
        if ( rs.next() ){
            return rs.getInt("debug");
        }
        return -1;
    }
    /**
     * Database.array_has_it(int array[],int a)
     * @param array
     * @param a
     * @return boolean
     * Function for checking if array has a number
     */
    boolean array_has_it(int array[],int a){
        for (int number : array){
            if (number == a){
                return true;
            }
        }
        return false;
    }
    
    void close() throws SQLException{
        con.close();
        connected = false;
        this.log.add("Database connection ended", HEADER);
    }
    //----------------------------USER LOGIN TO THE DATABASE
    /**
     * Database.user_login(String user_login, String user_password)
     * @param user_login
     * @param user_password
     * @return
     * @throws SQLException 
     * Logging into database
     */
    Tick_User user_login(String user_login, String user_password) throws SQLException{
        String query = "SELECT * FROM OWN WHERE owner_login = ? and owner_password = ?;";
        this.log.add("Trying to log, user credentials: "+user_login+"/"+user_password, HEADER);
        Tick_User logged = null;
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1, user_login);
        ppst.setString(2, user_password);
        
        ResultSet rs = ppst.executeQuery();
        
        ResultSetMetaData meta   = (ResultSetMetaData) rs.getMetaData();
        int colmax = meta.getColumnCount();
        this.log.add("COLMAX "+Integer.toString(colmax), HEADER);
        int a[] = {1,3,8,9};
        
        ArrayList<Tick_Brick> us_part = new ArrayList<>();
        if ( rs.next() ){
            for ( int i = 1 ; i <= colmax; i++){
                if ( array_has_it(a,i)){
                    us_part.add(new Tick_Brick(rs.getInt(meta.getColumnName(i))));
                }
                else{
                    us_part.add(new Tick_Brick(rs.getString(meta.getColumnName(i))));
                }
            }
            this.log.add("Logged user correctly", HEADER);
            logged = new Tick_User(us_part);
            update_user_logins(logged);
        }
        return logged;
    }
    
    //----------------------------ADDING TO THE DATABASE
}
