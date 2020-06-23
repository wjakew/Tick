/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *Object for maintaining tick objects on the database
 * @author jakub
 */
public class Database_Tick {
    final String HEADER = "DATABASE_TICK";
    Database database;
    
    // main constructor
    Database_Tick(Database database){
        this.database = database;
    }
    
    
    /**
     * Database_Tick.add_tick(Tick_Tick to_add)
     * @param to_add
     * @return boolean
     * @throws SQLException
     * Function for adding tick to the database
     */
    boolean add_tick(Tick_Tick to_add) throws SQLException{
        String query = "INSERT INTO TICK\n" +
                       "(owner_id,place_id,category_id,note_id,hashtag_table_id,tick_done_id,tick_done_start,tick_date_end,tick_name)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?,?,?,?,?);";
        
        PreparedStatement ppst = database.con.prepareStatement(query);
        
        ppst.setInt(1,database.logged.owner_id);
        ppst.setInt(2, to_add.place_id);
        ppst.setInt(3, to_add.category_id);
        ppst.setInt(4, to_add.note_id);
        ppst.setInt(5, to_add.hashtag_table_id);
        ppst.setInt(6, to_add.tick_done_id);
        ppst.setString(7, to_add.tick_done_start);
        ppst.setString(8, to_add.tick_done_end);
        ppst.setString(9, to_add.tick_name);
        
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            database.log.add("Failed to add tick ( "+e.toString()+")", HEADER);
            return false;
        }
    }
    
    /**
     * Database_Tick.check_if_exists(int tick_id)
     * @param tick_id
     * @return boolean
     * @throws SQLException
     * Function returns true 
     */
    boolean check_if_exists(int tick_id) throws SQLException{
        String query = " SELECT * FROM TICK WHERE owner_id = ? and tick_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        
        ppst.setInt(1,database.logged.owner_id);
        ppst.setInt(2,tick_id);
        
        ResultSet rs = ppst.executeQuery();
        
        return rs.next();
    }
    /**
     * Database_Tick.update_data(int data_id,String mode)
     * @param data_id
     * @param mode
     * @return boolean
     * Modes:
     * place
     * address
     * hashtag_table
     * category
     * note
     */
    boolean update_data(int data_id,int tick_id,String mode) throws SQLException{
        String query = "UPDATE TICK SET "+mode+"_id = ? where tick_id = ? and owner_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
      
        ppst.setInt(1,data_id);
        ppst.setInt(2,tick_id);
        ppst.setInt(3,database.logged.owner_id);
        System.out.println(ppst.toString());
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            database.log.add("FAILED TO UPDATE TICK ( "+e.toString()+")", HEADER);
            return false;
        }
    }
    
    /**
     * Database_Tick.view_tick(int tick_id)
     * @param tick_id
     * @return ArrayList<String>
     * @throws SQLException 
     * Returns lines to show 
     */
    ArrayList<String> view_tick(int tick_id) throws SQLException{
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
        String tab = "  ";
        ArrayList<String> to_ret_lines = new ArrayList<>();
        String query = "SELECT * FROM TICK where tick_id = ? and owner_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        
        
        ppst.setInt(1,tick_id);
        ppst.setInt(2,database.logged.owner_id);
        
        try{
            ResultSet rs = ppst.executeQuery();
            
            Tick_Tick to_show = new Tick_Tick(database.return_tick_brick(rs, "tick"));
            to_ret_lines.add(to_show.simple_show());
            if ( to_show.tick_done_id == 1){
                to_ret_lines.add(tab + "TICK NOT DONE");
            }
            else{
                to_ret_lines.add(tab + "TICK DONE");
            }
            // setting categories
            if ( to_show.category_id != 1){
                String clear_query = "SELECT * FROM CATEGORY WHERE category_id = ?;";
                ppst = database.con.prepareStatement(clear_query);
                
                ppst.setInt(1, to_show.category_id );
                
                rs = ppst.executeQuery();
                
                Tick_Category tc = new Tick_Category ( database.return_tick_brick(rs, "category"));
                to_ret_lines.addAll(tc.get_lines_to_show());
            }
            else {
                to_ret_lines.add(tab + "NO CATEGORIES");
            }
            // setting hashtag table
            if ( to_show.hashtag_table_id != 1){
                String clear_query = "SELECT * FROM HASHTAG_TABLE WHERE hashtag_table_id = ?;";
                ppst = database.con.prepareStatement(clear_query);
                
                ppst.setInt(1, to_show.hashtag_table_id);
                
                rs = ppst.executeQuery();
                
                Tick_HashtagT tht = new Tick_HashtagT(database.return_tick_brick(rs,"hashtag table"));
                
                to_ret_lines.addAll(tht.get_lines_to_show());
            }
            else{
                to_ret_lines.add(tab + "NO HASHTAG TABLES");
            }
            // setting places
            
            
            // setting addresses
            
            
            // setting note
            
            return to_ret_lines;
            
        }catch (SQLException e){
            return null;
        }
    }
    
}
