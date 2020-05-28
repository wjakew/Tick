/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;
import com.mysql.jdbc.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 *Database viewer for main components
 * @author jakub
 */
public class Database_Viewer {
    
    final String version = "v1.0.0";
    final String HEADER = "DATABASE_VIEWER ("+version+")"; 
    /**
     * modes:
     *      1 - address
     *      2 - category
     *      3 - hashtag table
     *      4 - note
     *      5 - place
     *      6 - tag
     */
    
    // storage for main object info
    Tick_User logged;
    Database database;
    UI_Tick_Interface i;
    
    //
    String mode;
    
    // main constructor
    Database_Viewer(Database database,Tick_User logged,String mode){
        this.database = database;
        this.logged = logged;
        this.mode = mode;
        i = new UI_Tick_Interface();
    }
    /**
     * Database_Viewer.make_view()
     * @return ArrayList
     * @throws SQLException 
     * Returns lines to show
     */
    ArrayList<String> make_view() throws SQLException{
        ResultSet actual= prepare_query();      // getting data from the base
        // based on the mode variable
        ArrayList<Tick_Brick> wall = prepare_tick_brick(actual);    // prepared universal storage object
        return get_lines(wall); // getting object from Tick_Brick collection
    }
    /**
     * Database_Viewer.get_lines(ArrayList<Tick_Brick> to_get)
     * @param to_get
     * @return ArrayList
     * Returns lines to show
     */
    ArrayList<String> get_lines(ArrayList<Tick_Brick> to_get){
        if (this.mode.equals("address")){
            Tick_Address act = new Tick_Address(to_get);
            return act.get_lines_to_show();
        }
        else if (this.mode.equals("category")){
            Tick_Category act = new Tick_Category(to_get);
            return act.get_lines_to_show();
        }
        else if (this.mode.equals("hashtag table")){
            Tick_HashtagT act = new Tick_HashtagT(to_get);
            return act.get_lines_to_show();
        }
        else if (this.mode.equals("note")){
            Tick_Note act = new Tick_Note(to_get);
            return act.get_lines_to_show();
        }
        else if (this.mode.equals("place")){
            Tick_Place act = new Tick_Place(to_get);
            return act.get_lines_to_show();
        }
        else if (mode.equals("tag")){
            Tick_Tag act = new Tick_Tag(to_get);
            return act.get_lines_to_show();
        }
        return null;
    }
    /**
     * Database_Viewer.prepare_query(String mode)
     * @return ResultSet
     * @throws SQLException 
     * Function prepares query for view
     */
    ResultSet prepare_query() throws SQLException{
        String query = "";
        if (this.mode.equals("address")){
            query = "SELECT * FROM ADDRESS;";
        }
        else if (this.mode.equals("category")){
            query = "SELECT * FROM CATEGORY;";
        }
        else if (this.mode.equals("hashtag table")){
            query = "SELECT * FROM HASHTAG_TABLE;";
        }
        else if (this.mode.equals("note")){
            query = "SELECT * FROM NOTE;";
        }
        else if (this.mode.equals("place")){
            query = "SELECT * FROM PLACE;";
        }
        else if (mode.equals("tag")){
            query = "SELECT * FROM TAG;";
        }
        PreparedStatement ppst = database.con.prepareStatement(query);
        try{
            return ppst.executeQuery();
        }catch(SQLException e){
            database.log.add("Cant return ResultSet",HEADER);
            return null;
        }
    }
    /**
     * Database_Viewer.prepare_tick_brick(ResultSet rs)
     * @param rs
     * @return ArrayList
     * Function returns collection of Tick_Brick
     */
    ArrayList<Tick_Brick> prepare_tick_brick(ResultSet rs) throws SQLException{
        // metadata for number of columns
        ResultSetMetaData meta   = (ResultSetMetaData) rs.getMetaData();
        
        ArrayList<Tick_Brick> to_ret = new ArrayList<>();  // tick_brick to ret
        
        int index[] = {};                // array of int indexes
        
        if (this.mode.equals("address")){
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
        else if (this.mode.equals("category")){
            /**
             *  category_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                category_name VARCHAR(45),
                category_note VARCHAR(100),
             */
            index = new int[] {1,2};
            
        }
        else if (this.mode.equals("hashtag table")){
            /**
             *  hashtag_table_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                hashtag_table_name VARCHAR(45),
                hashtag_table_note VARCHAR(100),
                * 
             */
            index = new int[] {1,2};
        }
        else if (this.mode.equals("note")){
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
        else if (this.mode.equals("place")){
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
            index = new int[] {1,4,3};
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
            database.log.add("Tick_Brick Collection returns succesfully", HEADER);
                
            } 
        }
        else{
            database.log.add("Tick_Brick Collection failed", HEADER);
        }
        return to_ret;
    }
}
