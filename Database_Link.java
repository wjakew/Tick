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

/**
 *Object for linking data
 * @author jakub
 */
public class Database_Link {
    
    final String HEADER = "DATABASE_LINK";
    
    /**
     * Tick_Own - > Tick_Address + 
     * Tick_Place - > Tick_Address + 
     * Tick_Tag - > Tick_HashtagT +
     */
    
    Database database;
    
    Database_Link(Database database){
        this.database = database;
        this.database.log.add("Linker is now ready",HEADER);
    }
    
    /**
     * Database_Link.link_user_address(Tick_User user ,Tick_Address address)
     * @param user
     * @param address
     * @return boolean
     * @throws SQLException 
     * Links user to address
     */
    boolean link_user_address(Tick_User user ,Tick_Address address) throws SQLException{
        database.log.add("Trying to link address to user",HEADER);
        String query = "update OWN SET address_id = ? WHERE owner_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, address.address_id);
        ppst.setInt(2, user.owner_id);
        try{
            ppst.execute();
            database.log.add("QUERY: "+ppst.toString(),HEADER);
            return true;
        }catch(SQLException e){
            database.log.add("Cannot link user to address", HEADER);
            return false;
        }
    }
    /**
     * Database_Link.link_place_address(Tick_Place place, Tick_Address address)
     * @param place
     * @param address
     * @return boolean
     * @throws SQLException 
     * Links place to address
     */
    boolean link_place_address(Tick_Place place, Tick_Address address) throws SQLException{
        database.log.add("Trying to link address to place",HEADER);
        String query = "update PLACE SET address_id = ? WHERE place_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, address.address_id);
        ppst.setInt(2, place.place_id);
        try{
            ppst.execute();
            database.log.add("QUERY: "+ppst.toString(),HEADER);
            return true;
        }catch(SQLException e){
            database.log.add("Cannot link place to address", HEADER);
            return false;
        }
    }
    /**
     * Database_Link.link_tag_hashtagT(Tick_Tag tag, Tick_HashtagT table)
     * @param tag
     * @param table
     * @return boolean
     * @throws SQLException 
     * Links tag to hashtag table
     */
    boolean link_tag_hashtagT(Tick_Tag tag, Tick_HashtagT table) throws SQLException{
        database.log.add("Trying to link hashtag table to tag",HEADER);
        String query = "update TAG set hashtag_table_id = ? WHERE tag_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1,table.hashtag_table_id);
        ppst.setInt(2, tag.tag_id);
        try{
            ppst.execute();
            database.log.add("QUERY: "+ppst.toString(),HEADER);
            return true;
        }catch(SQLException e){
            database.log.add("Cannot link place to address", HEADER);
            return false;
        }
    }
    //----------------------------------functions for getting objects
    Tick_Address get_object_address(int address_id) throws SQLException{
        String query = "SELECT * FROM ADDRESS WHERE address_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, address_id);
        ResultSet rs = ppst.executeQuery();
        ArrayList<Tick_Brick> lists = database.return_tick_brick(rs,"address");
        return new Tick_Address(lists);
    }
    
    Tick_Place get_object_place(int place_id) throws SQLException{
        String query = "SELECT * FROM PLACE WHERE place_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, place_id);
        
        ResultSet rs = ppst.executeQuery();
        ArrayList<Tick_Brick> lists = database.return_tick_brick(rs,"place");
        return new Tick_Place(lists);
    }
    Tick_Tag get_object_tag(int tag_id) throws SQLException{
        String query = "SELECT * FROM TAG WHERE tag_id = ?";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, tag_id);
        
        ResultSet rs = ppst.executeQuery();
        ArrayList<Tick_Brick> lists = database.return_tick_brick(rs,"tag");
        return new Tick_Tag(lists);
    }
    Tick_HashtagT get_object_hashtagT(int hashtag_table_id) throws SQLException{
        String query = "SELECT * FROM HASHTAG_TABLE WHERE hashtag_table_id = ?";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, hashtag_table_id);
        ResultSet rs = ppst.executeQuery();
        ArrayList<Tick_Brick> lists = database.return_tick_brick(rs,"hashtag table");
        return new Tick_HashtagT(lists);
    }
    // functions for checking if data is linked
    /**
     * Tick_Own - > Tick_Address + 
     * Tick_Place - > Tick_Address + 
     * Tick_Tag - > Tick_HashtagT +
     */
    /**
     * Database_Link.check_link_place(int place_id)
     * @param place_id
     * @return boolean
     * @throws SQLException
     * Returns if place by given id is linked to the address
     */
    boolean check_link_place(int place_id) throws SQLException{
        String query = "SELECT address_id from PLACE where place_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, place_id);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()){
            return rs.getInt("address_id") != 1;
        }
        return false;
    }
    /**
     * Database_Link.check_link_tag(int tag_id)
     * @param tag_id
     * @return boolean
     * @throws SQLException 
     * Returns if tag by given id is linked to the address
     */
    boolean check_link_tag(int tag_id) throws SQLException{
        String query = "SELECT hashtag_table_id from TAG where tag_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, tag_id);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()){
            return rs.getInt("hashtag_table_id") != 1;
        }
        return false;
    }
}