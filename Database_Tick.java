/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    
}
