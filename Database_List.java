/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *Object for list database link implementation
 * @author jakubwawak
 */
public class Database_List {
    
    Database database;
    
    
    // main constructor
    Database_List(Database database){
        this.database = database;
    }
    
    
    /**
     * Database_List.add_list(Tick_List to_add)
     * @param to_add
     * @return boolean
     * @throws SQLException
     * Function for adding list 
     */
    boolean add_list(Tick_List to_add) throws SQLException{
        
        String query =  "INSERT INTO LISTS\n" +
                        "(owner_id,list_name,tick_list_id,list_date)\n" +
                        "VALUES\n" +
                        "(?,?,?,?);";
        
        PreparedStatement ppst = database.con.prepareStatement(query);
        
        ppst.setInt(1, database.logged.owner_id);
        ppst.setString(2,to_add.list_name);
        ppst.setString(3,to_add.tick_list_id);
        ppst.setString(4,to_add.list_date);
        
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            database.log.add("Failed to add list ( "+e.toString(),"DATABASE LIST E!!!");
            return false;
        }
    }
    
    /**
     * Database_List.get_tick_list(int list_id)
     * @param list_id
     * @return String
     * @throws SQLException 
     * Function for getting list of ticks
     */
    String get_tick_list(int list_id) throws SQLException{
        String query = "SELECT tick_list_id FROM LISTS where list_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1,list_id);
        
        try{
            ResultSet rs = ppst.executeQuery();
            
            if(rs.next()){
                return rs.getString("tick_list_id");
            }
            return null;
            
        }catch(SQLException e ){
            database.log.add("Getting tick list occured problem ("+e.toString()+")","DATABASE LIST E!!!");
            return null;
        }
    }
    /**
     * Database_List.add_tick_to_list(int tick_id,int list_id)
     * @param tick_id
     * @param list_id
     * @return boolean
     * @throws SQLException 
     * Function for adding tick_id to list of ticks in list object
     */
    boolean add_tick_to_list(int tick_id,int list_id) throws SQLException{
        
        if ( database.check_if_record_exists(tick_id, "tick")){
            String ticks = get_tick_list(list_id);
            
            if ( ticks != null){
                ticks = ticks + "," + Integer.toString(tick_id);
                String query = "UPDATE LISTS SET tick_list_id = ? where list_id = ?;";
                
                PreparedStatement ppst = database.con.prepareStatement(query);
                ppst.setString(1,ticks);
                ppst.setInt(2,list_id);
                
                try{
                    ppst.execute();
                    return true;
                }catch(SQLException e){
                    database.log.add("Failed adding tick to tick_list_id ("+e.toString()+")","DATABASE LIST E!!!");
                    return false;
                }
                
            }
            return false;
        }
        else{
            return false;
        }
    }
    
    /**
     * Database_List.delete_list(int list_id)
     * @param list_id
     * @return boolean
     * @throws SQLException 
     * Function for deleting list
     */
    boolean delete_list(int list_id) throws SQLException{
        if ( database.check_if_record_exists(list_id, "list") ){
            Database_Garbage_Collector dgc = new Database_Garbage_Collector(database);
            return dgc.delete(list_id,"list_id","LISTS");
        }
        return false;
    }
}
