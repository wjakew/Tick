/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        String query = "update OWN SET address_id = ? WHERE owner_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, address.address_id);
        ppst.setInt(2, user.owner_id);
        try{
            ppst.execute();
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
        String query = "update PLACE SET address_id = ? WHERE place_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, address.address_id);
        ppst.setInt(2, place.place_id);
        try{
            ppst.execute();
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
        String query = "update TAG set hashtag_table_id = ? WHERE tag_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1,table.hashtag_table_id);
        ppst.setInt(2, tag.tag_id);
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            database.log.add("Cannot link place to address", HEADER);
            return false;
        }
    }
    
}
