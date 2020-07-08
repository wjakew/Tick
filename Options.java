/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author jakub
 */
public class Options {
    
    Database database;
    
    boolean internal_fail;
    
    
    Options(Database database){
        
        this.database = database;
        internal_fail = false;
        
    }
    
    
    /**
     * Options.options_show(String to_show)
     * @param to_show 
     * Function for showing text data
     */
    void options_show(String to_show){
        System.out.println("OPTIONS INFO: "+to_show);
    }
    
    /**
     * Options.make_config_record()
     * Function for making first record
     */
    boolean make_config_record() throws SQLException{
        int seed = random_generator();
        String query = "INSERT INTO CONFIGURATION\n" +
                       "(owner_id,sum_entries,debug,conf2,conf3,conf4,conf5,conf6,conf7)\n" +
                       "VALUES\n" +
                       "(?,1,0,\"NO\",?,\"\",\"\",\"\",\"\",\"\");";
        PreparedStatement ppst = database.con.prepareStatement(query);
        if ( database.logged != null){
            ppst.setInt(1,database.logged.owner_id);
            ppst.setInt(2,seed);
        }
        else{
            ppst.setInt(1,1);
            ppst.setInt(2,seed);
        }
        
        try{
            ppst.execute();
            options_show("Made new configuration record");
            return true;
        }catch(SQLException e){
            options_show("Fail! "+e.toString());
            return false;
        }
    }
    
    /**
     * Options.update_entries()
     * @return boolean
     * @throws SQLException 
     * Updates sum of entries
     */
    boolean update_entries() throws SQLException{
        String query = "UPDATE CONFIGURATION SET sum_entries = sum_entries + 1 where owner_id = ?;";
        PreparedStatement ppst = database.con.prepareStatement(query);
        ppst.setInt(1, database.logged.owner_id);
        
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    
    /**
     * Options.random_generator()
     * @return int
     * Returns random generated int
     */
    int random_generator(){
        return 0 + (int)(Math.random() * 10000);
    }
    
    /**
     * Options.get_rs_config(int seed)
     * @param seed
     * @return ResultSet
     * @throws SQLException
     * Function for returning ResultSet data
     */
    ResultSet get_rs_config(int seed) throws SQLException{
        if ( database.logged == null){
            String query = "SELECT * from CONFIGURATION WHERE conf3 = ?";
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,seed);
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return rs;
            }
            else{
                return null;
            }
        }
        else{
            String query = "SELECT * from CONFIGURATION WHERE owner_id = ?";
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,database.logged.owner_id);
            
            return ppst.executeQuery();
        }
    }
    
    /**
     * Options.startup()
     * @throws SQLException
     * All instructions for options startup
     */
    void startup() throws SQLException, IOException{

        
    }
    
}
