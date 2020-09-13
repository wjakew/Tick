/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *Object for deleting data from the database
 * @author jakub
 */
public class Database_Garbage_Collector {
    
    final String version = "v0.0.4";
    Database database;
    
    // main constructor
    Database_Garbage_Collector(Database database){
        this.database = database;
        System.out.println("Database Garbage Collector "+version+" is ready.");
    }
    
    /**
     * Database_Garbage_Collector.sec_check(int data_id, String data_name,String table)
     * @param data_id
     * @param data_name
     * @param table
     * @return int
     * @throws SQLException
     * Checks if table has data with given id
     * return codes:
     * 1 - has data
     * 0 - no data
     * -1 - fail
     */
    int sec_check(int data_id, String data_name, String table) throws SQLException{
        String query = "SELECT * from "+table+" where "+data_name+"= ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1,data_id);
        try{
            ResultSet rs = ppst.executeQuery();
            if (rs.next()){
                System.out.println("sec_check : found data in "+table);
                return 1;
            }
            return 0;
        }catch(SQLException e ){
            database.log.add("Failed to check given id. ("+e.toString()+")","DATABASE GARBAGE COLLECTOR E!!!");
            return -1;
        }
    }
    
    /**
     * Database_Garbage_Collector.update_data(int new_id,int lookup_id,String data_name,String table_name)
     * @param new_id
     * @param lookup_id
     * @param data_name
     * @param table_name
     * @return boolean
     * @throws SQLException 
     * Function updates tables
     */
    boolean update_data(int new_id,int lookup_id,String data_name,String table_name) throws SQLException{
        String query = "UPDATE "+table_name+" SET "+data_name+ " = ? where "+data_name+"=?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1,new_id);
        ppst.setInt(2,lookup_id);
        
        try{
            ppst.execute();
            System.out.println("Updated data in "+table_name);
            return true;
        }catch(SQLException e){
            System.out.println("Update failed: "+e.toString());
            database.log.add("Failed to update data. ("+e.toString()+")","DATABASE GARBAGE COLLECTOR E!!!");
            return false;
        }
    }
    
    /**
     * Database_Garbage_Collector.delete(int object_id,String field_name,String table_name)
     * @param object_id
     * @param field_name
     * @param table_name
     * @return boolean
     * @throws SQLException 
     * Delete object by given id in table name
     * WARNING:
     * Not save to use alone.
     */
    boolean delete(int object_id,String field_name,String table_name) throws SQLException{
        String query = "DELETE FROM "+table_name+" where "+field_name+" = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1,object_id);
        
        if( table_name.equals("CATEGORY") && object_id == 1){
            database.log.add("Cannot delete default data","DATABASE GARBAGE COLLECTOR W!!!");
            return false;
        }
        else if ( table_name.equals("PLACE") && object_id == 1){
           database.log.add("Cannot delete default data","DATABASE GARBAGE COLLECTOR W!!!");
            return false;
        }
        
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            System.out.println("Delete failed ("+e.toString()+")");
            database.log.add("Failed to delete object. ("+e.toString()+")","DATABASE GARBAGE COLLECTOR E!!!");
            database.log.add("Failed query : "+ppst.toString(),"DATABASE GARBAGE COLLECTOR E!!!");
            return false;
        }
    }
    
    /**
     * Database_Garbage_Collector.delete_address(int address_id)
     * @param address_id
     * @return boolean
     * @throws SQLException
     * Safely delete addreses
     */
    boolean delete_address(int address_id) throws SQLException{
        System.out.println("Prepare to delete address object with id: "+Integer.toString(address_id));
        String query_upd = "UPDATE OWN SET address_id = 1 where address_id = ?;";
        
        PreparedStatement ppst = database.con.prepareStatement(query_upd);
        ppst.setInt(1,address_id);
        ppst.execute();
        
        query_upd = "UPDATE PLACE SET address_id = 1 where address_id = ?;";
        ppst = database.con.prepareStatement(query_upd);
        ppst.setInt(1,address_id);
        ppst.execute();
        System.out.println("Safely deleting address...");
        query_upd = "DELETE FROM ADDRESS where address_id = ?;";
        
        ppst = database.con.prepareStatement(query_upd);
        
        ppst.setInt(1,address_id);
        
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            System.out.println("Failed: "+e.toString());
            database.log.add("Failed to delete address. ("+e.toString()+")","DATABASE GARBAGE COLLECTOR E!!!");
            return false;
        }
    }
    
    /**
     * Database_Garbage_Collector.delete_place(int place_id)
     * @param place_id
     * @return boolean
     * @throws SQLException 
     * Safely deletes place
     */
    boolean delete_place(int place_id) throws SQLException{
        if ( sec_check(place_id,"place_id","TICK") == 1){
            // we have place in tick
            update_data(1,place_id,"place_id","TICK");
        }
        else if ( sec_check(place_id,"place_id","SCENE") == 1){
            // we have place in scene
            update_data(1,place_id,"place_id","SCENE");
        }
        return delete(place_id,"place_id","PLACE");
    }
    
    /**
     * Database_Garbage_Collector.delete_category(int category_id)
     * @param category_id
     * @return boolean
     * @throws SQLException 
     * Function delete category by given id
     */
    boolean delete_category(int category_id) throws SQLException{
        if (sec_check(category_id,"category_id","SCENE") == 1){
            // we have category in scene
            update_data(1,category_id,"category_id","SCENE");
        }
        else if (sec_check(category_id,"category_id","TICK") == 1){
            // we have category in tick
            update_data(1,category_id,"category_id","TICK");
        }
        return delete(category_id,"category_id","CATEGORY");
    }
    
    /**
     * Database_Garbage_Collector.delete_tag(int tag_id)
     * @param tag_id
     * @return boolean
     * @throws SQLException
     * Function delete tag
     */
    boolean delete_tag(int tag_id) throws SQLException{
        return delete(tag_id,"tag_id","TAG");
    }
    
    /**
     * Database_Garbage_Collector.delete_hashtag_table(int hashtag_table_id)
     * @param hashtag_table_id
     * @return boolean
     * @throws SQLException 
     * Function delete hashtag table
     */
    boolean delete_hashtag_table(int hashtag_table_id) throws SQLException{
        if ( sec_check(hashtag_table_id,"hashtag_table_id","TAG") == 1){
            // we have hashtag table in tag
            update_data(1,hashtag_table_id,"hashtag_table_id","HASHTAG_TABLE");
        }
        else if( sec_check(hashtag_table_id,"hashtag_table_id","TICK") == 1){
            // we have hashtag table in tick
            update_data(1,hashtag_table_id,"hashtag_table_id","TICK");
        }
        else if( sec_check(hashtag_table_id,"hashtag_table_id","SCENE") == 1){
            // we have hashtag table in scene
            update_data(1,hashtag_table_id,"hashtag_table_id","SCENE");
        }
        
        return delete(hashtag_table_id,"hashtag_table_id","HASHTAG_TABLE");
    }
    
    /**
     * Database_Garbage_Collector.delete_note(int note_id)
     * @param note_id
     * @return boolean
     * @throws SQLException 
     * Function for deleting note
     */
    boolean delete_note(int note_id) throws SQLException{
        if ( sec_check(note_id,"note_id","TICK") == 1){
            // we have note in tick
            update_data(1,note_id,"note_id","TICK");
        }
        return delete(note_id,"note_id","NOTE");
    }
    
    /**
     * Database_Garbage_Collector.delete_scene(int scene_id)
     * @param scene_id
     * @return int
     * @throws SQLException
     * Function for deleting scene
     */
    boolean delete_scene(int scene_id) throws SQLException{
        return delete(scene_id,"scene_id","SCENE");
    }
    
    /**
     * Database_Garbage_Collector.delete_tick(int tick_id)
     * @param tick_id
     * @return boolean
     * @throws SQLException 
     * Deleting tick
     */
    boolean delete_tick(int tick_id) throws SQLException{
        return delete(tick_id,"tick_id","TICK");
    }
}
