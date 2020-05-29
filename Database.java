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
import java.util.stream.Collectors;

/**
 *Object for maintaining connection to the database.
 * @author jakub
 */
public class Database {
    
    final String version = "v0.0.6B";
    
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
     * Database.check_if_record_exists(int id,String mode)
     * @param id
     * @param mode
     * @return
     * @throws SQLException 
     * Function checks if record exists
     */
    boolean check_if_record_exists(int id,String mode) throws SQLException{
        String query = "";
        if (mode.equals("address")){
            query = "SELECT * FROM ADDRESS where address_id = ?;";
        }
        else if (mode.equals("category")){
            query = "SELECT * FROM CATEGORY where category_id = ?;";
        }
        else if (mode.equals("hashtag table")){
            query = "SELECT * FROM HASHTAG_TABLE where hashtag_table_id = ?;";
        }
        else if (mode.equals("note")){
            query = "SELECT * FROM NOTE where note_id = ?;";
        }
        else if (mode.equals("place")){
            query = "SELECT * FROM PLACE where place_id = ?;";
        }
        else if (mode.equals("tag")){
            query = "SELECT * FROM TAG where tag_id = ?;";
        }
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1, id);
        ResultSet rs = ppst.executeQuery();
        return rs.next();
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
     * Returning last address id
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
    ResultSet return_resultset(String query) throws SQLException{
         PreparedStatement ppst = con.prepareStatement(query);
         return ppst.executeQuery();
    }
    /**
     * Database.return_resultset(String mode,Tick_User logged_user)
     * @param mode
     * @param logged_user
     * @return
     * @throws SQLException 
     * Returns ResultSet for choosen object
     */
    ResultSet return_resultset(String mode,Tick_User logged_user) throws SQLException{
        String query = "";
        if (mode.equals("address")){
            query = "SELECT * FROM ADDRESS;";
        }
        else if (mode.equals("category")){
            query = "SELECT * FROM CATEGORY where owner_id = ?;";
        }
        else if (mode.equals("hashtag table")){
            query = "SELECT * FROM HASHTAG_TABLE where owner_id = ?;";
        }
        else if (mode.equals("note")){
            query = "SELECT * FROM NOTE where owner_id = ?;";
        }
        else if (mode.equals("place")){
            query = "SELECT * FROM PLACE where owner_id = ?;";
        }
        else if (equals("tag")){
            query = "SELECT * FROM TAG where owner_id = ?;";
        }
        PreparedStatement ppst = con.prepareStatement(query);
        if ( !mode.equals("address") ){
            ppst.setInt(1,logged_user.owner_id);
        }
        try{
            return ppst.executeQuery();
        }catch(SQLException e){
            log.add("Cant return ResultSet",HEADER);
            return null;
        }
    }
    /**
     * Database.prepare_tick_brick(ResultSet rs,String mode)
     * @param rs
     * @param mode
     * @return ArrayList
     * @throws SQLException
     * Returns collection of Tick_Brick to make object
     */
    ArrayList<Tick_Brick> return_tick_brick(ResultSet rs,String mode) throws SQLException{
        // metadata for number of columns
        ResultSetMetaData meta   = (ResultSetMetaData) rs.getMetaData();
        
        ArrayList<Tick_Brick> to_ret = new ArrayList<>();  // tick_brick to ret
        
        int index[] = {};                // array of int indexes
        
        if (mode.equals("address")){
            /**
             *  address_id INT AUTO_INCREMENT PRIMARY KEY,
                address_city VARCHAR(30),
                address_street VARCHAR (30),
                address_house_number INT,
                address_flat_number INT,
                address_postal VARCHAR(15),
                address_country VARCHAR(30)
             */
            index = new int[] {1,4,5};
            
        }
        else if (mode.equals("category")){
            /**
             *  category_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                category_name VARCHAR(45),
                category_note VARCHAR(100),
             */
            index = new int[] {1,2};
            
        }
        else if (mode.equals("hashtag table")){
            /**
             *  hashtag_table_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                hashtag_table_name VARCHAR(45),
                hashtag_table_note VARCHAR(100),
                * 
             */
            index = new int[] {1,2};
        }
        else if (mode.equals("note")){
            /**
             *  note_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                note_content VARCHAR(100),
                setting1 VARCHAR(40),
                setting2 VARCHAR(40),
                setting3 VARCHAR(40),
             */
            index = new int[] {1,2};
        }
        else if (mode.equals("place")){
            /**
             *  place_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                place_name VARCHAR(30),
                address_id INT,
             */
            index = new int[] {1,2,4};
            
        }
        else if (mode.equals("tag")){
            /**
             *  tag_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                hashtag_table_id INT,
                tag_name VARCHAR(45),
                tag_note VARCHAR(100),
             */
            index = new int[] {1,2,3};
        }
        // coping array to collection
        List<Integer> int_index = Arrays.stream(index).boxed().collect(Collectors.toList());
        int colmax = meta.getColumnCount();
        
        if ( rs != null ){
            // looping on all database records returned by ResultSet
            while( rs.next() ){
                // looping on one record
                
                for ( int i = 1 ; i <= colmax; i++){
                    if ( int_index.contains(i)){
                        to_ret.add(new Tick_Brick(rs.getInt(meta.getColumnName(i))));
                    }
                    else{
                        to_ret.add(new Tick_Brick(rs.getString(meta.getColumnName(i))));
                    }
                }
                // flagging end of the object
                Tick_Brick brake = new Tick_Brick();
                brake.STOP = true;
                to_ret.add(brake);
            } 
            log.add("Tick_Brick Collection returns succesfully", HEADER); 
        }
        else{
            log.add("Tick_Brick Collection failed", HEADER);
        }
        return to_ret;
    }
    //----------------------------tick brick function
    /**
     * Database.return_collection(Tick_User logged_user, String mode)
     * @param logged_user
     * @param mode
     * @return ArrayList
     * @throws SQLException
     * Returns collection of Tick_Brick objects to make Tick_Element object
     */
    ArrayList<Tick_Brick> return_TB_collection(Tick_User logged_user, String mode) throws SQLException{
        ResultSet actual = return_resultset(mode,logged_user);
        return return_tick_brick(actual,mode);
    }
    ArrayList<Tick_Brick> return_TB_collection(Tick_User logged_user, String mode,String query) throws SQLException{
        ResultSet actual = return_resultset(query);
        return return_tick_brick(actual,mode);
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
    //------------------------------FUNCTIONS FOR NOTE
    /**
     * Database.add_note(Tick_Note to_add)
     * @param to_add
     * @return
     * @throws SQLException 
     * Functions for adding data to database
     */
    boolean add_note(Tick_Note to_add) throws SQLException{
        log.add("Adding new note",HEADER);
        String query = "INSERT INTO NOTE\n" +
                       "(owner_id,note_content,setting1,setting2,setting3)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?);";
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
            log.add("Failed to add note",HEADER);
            log.add("QUERY FAILED: "+e.getMessage(),HEADER+ "E!!");
            return false;
        }
    }
}
