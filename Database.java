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
    
    final String version = "v0.0.2B";
    
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
    /**
     * Database.make_first_configuration(Tick_User user)
     * @param user
     * @throws SQLException 
     * Setting the first configuration
     */
    void make_first_configuration(Tick_User user) throws SQLException{
        log.add("Making first configuration for user: "+user.owner_login,HEADER);
        String query = "INSERT INTO CONFIGURATION\n" +
                       "(owner_id,sum_entries,debug,conf2,conf3,conf4,conf5,conf6,conf7)\n" +
                       "VALUES\n" +
                       "(?,1,1,'','','','','','');";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1, user.owner_id);
        ppst.execute();
    }
    /**
     * Database.ret_owner_id(String owner_login)
     * @param owner_login
     * @return int
     * @throws SQLException
     * Returns id of user with that login
     */
    int ret_owner_id (String owner_login) throws SQLException{
        log.add("Returning id for user : " + owner_login, HEADER);
        String query = " SELECT owner_id from OWN WHERE owner_login = ?;";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1, owner_login);
        ResultSet rs = ppst.executeQuery();
        
        if ( rs.next() ){
            return rs.getInt("owner_id");
        }
        
        return -1;
    }
    /**
     * Database.get_last_address_id()
     * @return int
     * @throws SQLException 
     * 
     */
    int get_last_address_id() throws SQLException{
        String query = "select address_id from ADDRESS ORDER BY id DESC LIMIT 1;";
        PreparedStatement ppst = con.prepareStatement(query);
        ResultSet rs = ppst.executeQuery();
        if ( rs.next() ){
            return rs.getInt("address_id");
        }
        return -1;
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
    //----------------------------LINK FUNCTIONS
    void link_own_address(int owner_id,int address_id) throws SQLException{
        String query = "UPDATE OWN SET address_id = ? WHERE owner_id = ?";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1,address_id);
        ppst.setInt(2,owner_id);
        ppst.execute();
    }
    //----------------------------FUNCTIONS FOR USER
    /**
     * Database.register_user(Tick_User to_add)
     * @param to_add
     * @throws SQLException 
     * Function adds user records to database
     */
    void register_user(Tick_User to_add) throws SQLException{
        log.add("Registering new user...",HEADER);
        String query = "INSERT INTO OWN\n" +
                       "(owner_login,address_id,owner_password,owner_name,owner_surname,owner_email_address,\n" +
                       "owner_age,owner_status)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?,?,?,?);";
        log.add("QUERY: "+query, HEADER);
        PreparedStatement ppst = con.prepareStatement(query);
        
        for ( int i = 1,j=1 ; i < to_add.tick_Element_size ; i++,j++){
            Tick_Brick act = to_add.tick_Element_Elements.get(j);
            
            if ( act.category == 1){
                ppst.setInt(i, act.data_int);
            }
            else if (act.category == 2){
                ppst.setString(i, act.data_string);
            }
        }
        log.add("QUERY: "+ppst.toString(), HEADER);
        ppst.execute();
        
        to_add.owner_id = ret_owner_id(to_add.owner_login);
        make_first_configuration(to_add);
    }
    
    /**
     * Database.change_password(Tick_User logged, String new_password)
     * @param logged
     * @param new_password
     * @return
     * @throws SQLException 
     * Function changes user password
     */
    boolean change_password(Tick_User logged, String new_password) throws SQLException{
        log.add("Changing user password",HEADER);
        String query = "UPDATE OWN SET owner_password = ? WHERE owner_id = ?;";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1,new_password);
        ppst.setInt(2,logged.owner_id);
        log.add("QUERY: "+ppst.toString(), HEADER);
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            log.add("Failed to change user password",HEADER);
            return false;
        }
        
    }
    //----------------------------FUNCTIONS FOR PLACE
    /**
     * Database.add_place(Tick_Place to_add)
     * @param to_add
     * @return boolean
     * @throws SQLException
     * 
     */
    boolean add_place(Tick_Place to_add) throws SQLException{
        String query = "INSERT INTO PLACE\n" + 
                       "(owner_id,place_name,address_id)\n" +
                       "VALUES\n" +
                       "(?,?,?);";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1, to_add.owner_id);
        ppst.setString(2, to_add.place_name);
        ppst.setInt(3, to_add.address_id);
        try{
            ppst.execute();
            log.add("Added place",HEADER);
            return true;
        }catch(SQLException e){
            log.add("Failed to add place",HEADER);
            log.add("QUERY FAILED: "+e.getMessage(),HEADER+ "E!!");
            return false;
        }      
    }
    //----------------------------FUNCTIONS FOR ADDRESS
    /**
     * Database add_address(Tick_Address to_add)
     * @param to_add
     * @throws SQLException 
     * Adding address to the database
     */
    boolean add_address(Tick_Address to_add) throws SQLException{
        log.add("Adding new address",HEADER);
        String query = "INSERT INTO ADDRESS\n" +
                       "(address_city,address_street,address_house_number,address_flat_number,address_postal,\n" +
                       "address_country)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?,?);";
        PreparedStatement ppst = con.prepareStatement(query);

        for ( int i = 1,j=1 ; i < to_add.tick_Element_size ; i++,j++){
            Tick_Brick act = to_add.tick_Element_Elements.get(j);
            
            if (act.category == 1){
                ppst.setInt(i,act.i_get());
            }
            else if (act.category == 2){
                ppst.setString(i, act.s_get());
            }
        }
        try{
            log.add("QUERY: "+ppst.toString(),HEADER);
            ppst.execute();
            return true;
        }catch (Exception e){
            log.add("Failed to add address",HEADER);
            log.add("QUERY FAILED: "+e.getMessage(),HEADER+ "E!!");
            return false;
        }
    }
    //---------------------------------FUNCTIONS FOR HASHTAG TABLE
    /**
     * Database.add_hashtagT(Tick_HashtagT to_add)
     * @param to_add
     * @return boolean
     * @throws SQLException
     * Function adds hashtag table
     */
    boolean add_hashtagT(Tick_HashtagT to_add) throws SQLException{
        log.add("Adding new Hashtag Table",HEADER);
        String query = "INSERT INTO HASHTAG_TABLE\n" +
                       "(owner_id,hashtag_table_name,hashtag_table_note)\n" +
                       "VALUES\n" +
                       "(?,?,?);";
        PreparedStatement ppst = con.prepareStatement(query);
        
        for ( int i = 1,j=1 ; i < to_add.tick_Element_size ; i++,j++){
            Tick_Brick act = to_add.tick_Element_Elements.get(j);
            
            if (act.category == 1){
                ppst.setInt(i,act.i_get());
            }
            else if (act.category == 2){
                ppst.setString(i, act.s_get());
            }
        }
        try{
            log.add("QUERY: "+ppst.toString(),HEADER);
            ppst.execute();
            return true;
        }catch (Exception e){
            log.add("Failed to add Hashtag Table",HEADER);
            log.add("QUERY FAILED: "+e.getMessage(),HEADER+ "E!!");
            return false;
        }
    }
    //---------------------------------FUNCTIONS FOR TAGS
    /**
     * Database.add_tag(Tick_Tag to_add)
     * @param to_add
     * @return
     * @throws SQLException 
     * Function thats add tag
     */
    boolean add_tag(Tick_Tag to_add) throws SQLException{
        log.add("Adding new tag",HEADER);
        String query = "INSERT INTO TAG\n" +
                       "(owner_id,hashtag_table_id,tag_name,tag_note)\n" +
                       "VALUES\n" +
                       "(?,?,?,?);";
        PreparedStatement ppst = con.prepareStatement(query);
        
        for ( int i = 1,j=1 ; i < to_add.tick_Element_size ; i++,j++){
            Tick_Brick act = to_add.tick_Element_Elements.get(j);
            
            if (act.category == 1){
                ppst.setInt(i,act.i_get());
            }
            else if (act.category == 2){
                ppst.setString(i, act.s_get());
            }
        }
        try{
            log.add("QUERY: "+ppst.toString(),HEADER);
            ppst.execute();
            return true;
        }catch (Exception e){
            log.add("Failed to add tag",HEADER);
            log.add("QUERY FAILED: "+e.getMessage(),HEADER+ "E!!");
            return false;
        }
    }
    //----------------------------------FUNCTIONS FOR CATEGORY
    /**
     * Database.add_category(Tick_Category to_add)
     * @param to_add
     * @return
     * @throws SQLException 
     */
    boolean add_category(Tick_Category to_add) throws SQLException{
        log.add("Adding new category",HEADER);
        String query = "INSERT INTO CATEGORY\n" +
                       "(owner_id,category_name,category_note)\n" +
                       "VALUES\n" +
                       "(?,?,?);";
        PreparedStatement ppst = con.prepareStatement(query);
        
        for ( int i = 1,j=1 ; i < to_add.tick_Element_size ; i++,j++){
            Tick_Brick act = to_add.tick_Element_Elements.get(j);
            
            if (act.category == 1){
                ppst.setInt(i,act.i_get());
            }
            else if (act.category == 2){
                ppst.setString(i, act.s_get());
            }
        }
        try{
            log.add("QUERY: "+ppst.toString(),HEADER);
            ppst.execute();
            return true;
        }catch (Exception e){
            log.add("Failed to add category",HEADER);
            log.add("QUERY FAILED: "+e.getMessage(),HEADER+ "E!!");
            return false;
        }
        
    }
}
