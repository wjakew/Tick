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
    
    final String version = "v1.0.0";
    
    final String HEADER = "DATABASE ("+version+")";
    
    Connection con = null;          // connection to the database
    ResultSet rs = null;            // result set of the query
    boolean connected = false;
    Tick_User logged;               // actual logged user on the database
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
        else if (mode.equals("tick")){
            query = "SELECT * FROM TICK where tick_id = ?;";
        }
        else if (mode.equals("tick_done")){
            query = "SELECT * FROM TICK_DONE where tick_done_id = ?;";
        }
        else if (mode.equals("scene")){
            query = "SELECT * FROM SCENE where scene_id =?;";
        }
        else if (mode.equals("list")){
            query = "SELECT * FROM LISTS where list_id =?;";
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
        logged = null;
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
     * Database.ret_owner_name(int owner_id)
     * @param owner_id
     * @return String
     * @throws SQLException 
     * Returns owner login by owner_id
     */
    String ret_owner_name(int owner_id) throws SQLException{
        String query = "SELECT owner_login FROM OWN where owner_id = ?;";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1, owner_id);
        
        try{
            ResultSet rs = ppst.executeQuery();
            log.add("RET NAME: QUERY "+ ppst.toString(),HEADER);
            if ( rs.next() ){
            return rs.getString("owner_login");
            }
            else{
                return null;
            }
        }catch(SQLException e){
            log.add("Failed to get owner name by id ("+e.toString()+")",HEADER+" E!!!");
            return null;
        }
        
        
    }
    /**
     * Database.tick_name(int tick_id)
     * @param tick_id
     * @return String
     * @throws SQLException
     * Returns name of the tick by given tick id
     */
    String ret_tick_name(int tick_id) throws SQLException{
        String query = "SELECT * from TICK where tick_id = ?;";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1, tick_id);
        
        try{
            ResultSet rs = ppst.executeQuery();
            if ( rs.next()){
                return rs.getString("tick_name");
            }
            return null;
            
        }catch(SQLException e){
            log.add("Failed to find tick name ("+e.toString()+")",HEADER+" E!!!");
            return null;
        }
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
    
    /**
     * Database.get_last_id(String table_name)
     * @param table_name
     * @return int
     * @throws SQLException 
     * Returns last id of given table
     */
    int get_last_id(String table_name) throws SQLException{
        int index = 0;
        String field = table_name.toLowerCase();
        field = field + "_id";
        String query = "SELECT * from "+table_name+" where owner_id = ?;";
        PreparedStatement ppst = con.prepareStatement(query);
        
        ppst.setInt(1,logged.owner_id);
        
        try{
            ResultSet rs = ppst.executeQuery();
            
            while ( rs.next() ){
                index = rs.getInt(field);
            }
        }catch(SQLException e){}
        return index;
    }
    
    /**
     * Database.return_resultset(String query)
     * @param query
     * @return ResultSet
     * @throws SQLException
     * Returns resultset of given string
     */
    ResultSet return_resultset(String query) throws SQLException{
         PreparedStatement ppst = con.prepareStatement(query);
         return ppst.executeQuery();
    }
    
    /**
     * Database.make_query(String query_head, int [] numbers)
     * @param query_head
     * @param numbers
     * @return String
     * Function returns string for query
     */
    String make_query(String query_head, int [] numbers){
        String [] parts = query_head.split("?");
        String result = "";
        int i = 0;
        for( String part : parts ){
            result = result + part.substring(0, part.length()-2) + Integer.toString(numbers[i]);
            i++;
        }
        return result;
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
        else if (mode.equals("tag")){
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
        else if (mode.equals("tick")){
            /**
             *  tick_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                place_id INT,
                category_id INT,
                note_id INT,
                hashtag_table_id INT,
                tick_done_id INT,
                tick_done_start VARCHAR(60),
                tick_date_end VARCHAR(60),
                tick_name VARCHAR(60),
             */
            index = new int[] {1,2,3,4,5,6,7};
        }
        else if (mode.equals("scene")){
            /**
             *  scene_id INT AUTO_INCREMENT PRIMARY KEY,
                hashtag_table_id INT,
                place_id INT,
                owner_id INT,
                category_id INT,
                scene_name VARCHAR(30),
                scene_note VARCHAR(100),
             */
            index = new int[] {1,2,3,4,5};
        }
        // coping array to collection
        List<Integer> int_index = Arrays.stream(index).boxed().collect(Collectors.toList());
        int colmax = meta.getColumnCount();
        while(rs.next()){
                    // looping on all database records returned by ResultSet
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
        if (to_ret.size()>0){
            log.add("Tick_Brick Collection returns succesfully", HEADER); 
            log.add("RS DATA: "+rs.toString(), HEADER);
        }
        else{
            log.add("RS size is probably 0.",HEADER+"E!!!");
            log.add("Failed to reach RS",HEADER+"E!!!");
        }

        return to_ret;
    }
    
    ArrayList<Tick_Brick> return_one_tick_brick(ResultSet rs,String mode) throws SQLException{
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
        else if (mode.equals("tick")){
            /**
             *  tick_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                place_id INT,
                category_id INT,
                note_id INT,
                hashtag_table_id INT,
                tick_done_id INT,
                tick_done_start VARCHAR(60),
                tick_date_end VARCHAR(60),
                tick_name VARCHAR(60),
             */
            index = new int[] {1,2,3,4,5,6,7};
        }
        else if (mode.equals("scene")){
            /**
             *  scene_id INT AUTO_INCREMENT PRIMARY KEY,
                hashtag_table_id INT,
                place_id INT,
                owner_id INT,
                category_id INT,
                scene_name VARCHAR(30),
                scene_note VARCHAR(100),
             */
            index = new int[] {1,2,3,4,5};
        }
        else if (mode.equals("list")){
            /**
             *  list_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                tick_list_id VARCHAR(100),
                list_name VARCHAR(50),
                list_date VARCHAR(50),
             */
            index = new int[] {1,2};
        }
        // coping array to collection
        List<Integer> int_index = Arrays.stream(index).boxed().collect(Collectors.toList());
        int colmax = meta.getColumnCount();
        
        // looping on all database records returned by ResultSet
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
        
        if (to_ret.size()>0){
            log.add("Tick_Brick Collection returns succesfully", HEADER); 
            log.add("RS DATA: "+rs.toString(), HEADER);
        }
        else{
            log.add("RS size is probably 0.",HEADER+"E!!!");
            log.add("Failed to reach RS",HEADER+"E!!!");
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
    ArrayList<Tick_Brick> return_TB_collection(Tick_User logged_user, String mode, int object_id) throws SQLException{
        log.add("Started return_TB_collection function : tick_user, mode and object",HEADER);
        String query = "";
        if (mode.equals("address")){
            query = "SELECT * FROM ADDRESS where address_id = ?;";
        }
        else if (mode.equals("category")){
            query = "SELECT * FROM CATEGORY where owner_id = ? and category_id = ?;";
        }
        else if (mode.equals("hashtag table")){
            query = "SELECT * FROM HASHTAG_TABLE where owner_id = ? and hashtag_table_id = ?;";
        }
        else if (mode.equals("note")){
            query = "SELECT * FROM NOTE where owner_id = ? and note_id = ?;";
        }
        else if (mode.equals("place")){
            query = "SELECT * FROM PLACE where owner_id = ? and place_id = ?;";
        }
        else if (mode.equals("tag")){
            query = "SELECT * FROM TAG where owner_id = ? and tag_id = ?;";
        }
        else if (mode.equals("scene")){
            query = "SELECT * FROM SCENE where owner_id = ? and scene_id = ?;";
        }
        else if (mode.equals("tick")){
            query = "SELECT * FROM TICK where owner_id = ? and tick_id = ?;";
        }
        else if (mode.equals("list")){
            query = "SELECT * FROM LISTS where owner_id = ? and list_id =?;";
        }
        
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1,logged_user.owner_id);
        ppst.setInt(2,object_id);
        
        ResultSet result = ppst.executeQuery();
        
        log.add("RS QUERY : "+ppst.toString(),HEADER);
        
        if (result.next()){
            log.add("RS OWNER ID: "+Integer.toString(result.getInt("owner_id")),HEADER);
            return return_one_tick_brick(result,mode);
        }
        return null; 
    }
    ArrayList<Tick_Brick> return_TB_collection(String mode,int object_id) throws SQLException{
        log.add("Started return_TB_collection function : object and mode",HEADER);
        String query = "";
        if (mode.equals("address")){
            query = "SELECT * FROM ADDRESS where address_id = ?;";
        }
        else if (mode.equals("category")){
            query = "SELECT * FROM CATEGORY where category_id = ?;";
        }
        else if (mode.equals("hashtag table")){
            query = "SELECT * FROM HASHTAG_TABLE where and hashtag_table_id = ?;";
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
        else if (mode.equals("scene")){
            query = "SELECT * FROM SCENE where scene_id = ?;";
        }
        else if (mode.equals("tick")){
            query = "SELECT * FROM TICK where tick_id = ?;";
        }
        else if (mode.equals("list")){
            query = "SELECT * FROM LISTS where list_id =?;";
        }
        
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1,object_id);
        
        ResultSet result = ppst.executeQuery();
        
        log.add("RS QUERY : "+ppst.toString(),HEADER);
        
        if (result.next()){
            log.add("RS OWNER ID: "+Integer.toString(result.getInt("owner_id")),HEADER);
            return return_one_tick_brick(result,mode);
        }
        return null; 
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
        logged = null;
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
        }
        return logged;
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
    /**
     * Database.check_password(Tick_User logged,String password)
     * @param logged
     * @param password
     * @return boolean
     * @throws SQLException
     * Function returns true if password is correct.
     */
    boolean check_password(Tick_User logged,String password) throws SQLException{
        String query = "SELECT owner_id from OWN where owner_id = ? and owner_password = ?; ";
        PreparedStatement ppst = con.prepareStatement(query);
        
        ppst.setInt(1,logged.owner_id);
        ppst.setString(2,password);
        try{
            return ppst.executeQuery().next();
        }catch(SQLException e){
            log.add("Failed to check password ("+e.toString()+")",HEADER);
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
    //--------------------------------------FUNCTIONS FOR SCENE
    /**
     * Database.add_scene(Tick_Scene to_add)
     * @param to_add
     * @return boolean
     * @throws SQLException
     * Function for adding scene
     */
    boolean add_scene(Tick_Scene to_add) throws SQLException{
        log.add("Adding new scene",HEADER);
        String query = "INSERT INTO SCENE\n"
                + "(hashtag_table_id,place_id,owner_id,category_id,scene_name,scene_note)\n"
                + "VALUES\n"
                + "(?,?,?,?,?,?);";
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
            log.add("Failed to add scene",HEADER);
            log.add("QUERY FAILED: "+e.getMessage(),HEADER+ "E!!");
            return false;
        }
    }
    
    /**
     * Database.stats()
     * @return ArrayList
     * @throws SQLException
     * Function returns lines to show of stats from database
     * like number of items etc.
     */
    ArrayList<String> stats() throws SQLException{
        ArrayList<String> to_ret = new ArrayList<>();
        
        to_ret.add("Logged user: "+ logged.owner_login);
        to_ret.add("User id: "+ Integer.toString(logged.owner_id));
        
        String[] names = new String[] {"TICK","PLACE","ADDRESS","CATEGORY","HASHTAG_TABLE","TAG","SCENE"};
        
        for(String name : names){
            String query = "SELECT COUNT(*) FROM ";
            query = query + name + ";";
            PreparedStatement ppst = con.prepareStatement(query);
            
            try{
                ResultSet act_rs = ppst.executeQuery();
                
                if ( act_rs.next() ){
                    to_ret.add(name + " -> "+Integer.toString(act_rs.getInt("COUNT(*)")));
                }

            }catch(SQLException e){
                log.add("Failed to get stats ("+e.toString(),HEADER+"E!!!");
                System.out.println(ppst.toString());
                System.out.println(e.toString());
                return null;
            }
        }
        return to_ret;
    }
}
