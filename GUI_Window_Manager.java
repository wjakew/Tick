/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

/**
 *Object for managing windows in the program
 * @author jakubwawak
 */
public class GUI_Window_Manager {
    final String version = "v.0.0.2";
    
    GUI_main_window window;
    DefaultListModel model_obj; // jList model object
    
    
    GUI_Window_Manager(GUI_main_window window){
        this.window = window;
        
    }
    
    /**
     * Function for reloading data in jList ( TICK_list_ticklist )
     * @throws SQLException 
     * modes:
     * 0 - not done ticks
     * 1 - done ticks
     */
    void reload_ticklist(int mode) throws SQLException{
        // setting view handler
        Database_Tick dt = new Database_Tick(window.database);
        
        model_obj = new DefaultListModel(); // setting model
        
        for(String element : dt.view_simpleviews(mode)){
            model_obj.addElement(element);
        }
        
        window.TICK_list_ticklist.setModel(model_obj);
        log("Model for Tick list updated");
    }
    //------------------------- functions for implementing button presses
    /**
     * Reloads gui and init action for adding tick
     * @throws SQLException 
     * 
     */
    void buttonaction_addtick() throws SQLException{
        new GUI_addtick_window(window,true,window.database);
        reload_default_scene_tick();
    }
    /**
     * Sets behaviour to mark done tick
     * @throws SQLException 
     */
    void buttonaction_markdone() throws SQLException{
        new GUI_markdone_window(window,true,window.database,window.actual_choosen_element_index);
        reload_default_scene_tick();
    }
    /**
     * Implement delete of Tick element
     * @throws SQLException 
     */
    void button_action_delete() throws SQLException{
        Database_Garbage_Collector dgc = new Database_Garbage_Collector(window.database);
        
        if(dgc.delete_tick(window.actual_choosen_element_index)){
            window.TICK_button_delete.setText("Deleted");
            window.TICK_button_delete.setEnabled(false);
            reload_default_scene_tick();
        }
    }
    //--------------------------------------------------------------------

    /**
     * Function for reloading to default scene (TICK)
     * @throws SQLException 
     */
    void reload_default_scene_tick() throws SQLException{
        reload_ticklist(0);
        window.TICK_button_delete.setText("Delete");
        window.TICK_button_delete.setEnabled(false);
        window.TICK_button_active_ticks.setText("Active Ticks");
        window.TICK_list_ticklist.clearSelection();
        window.actual_choosen_element_index = -1;
        window.TICK_button_addnewtick.setEnabled(true);
        window.TICK_button_edittick.setEnabled(false);
        window.TICK_button_sharetick.setEnabled(false);
        window.TICK_textarea_tickdetails.setEditable(false);
        window.TICK_button_unarchive.setVisible(false);
        window.TICK_button_unarchive.setEnabled(false);
        window.TICK_textarea_tickdetails.setText("");
        window.TICK_button_markdone.setEnabled(false);
        window.TICK_button_link.setEnabled(false);
        log("Tick scene reloaded");
    }
    /**
     * Function for reloading archved scene
     * @throws SQLException 
     */
    void reload_archived_scene_tick() throws SQLException{
        reload_ticklist(1);
        window.TICK_button_delete.setEnabled(false);
        window.TICK_button_active_ticks.setText("Archived Ticks");
        window.TICK_button_addnewtick.setEnabled(false);
        window.TICK_list_ticklist.clearSelection();
        window.actual_choosen_element_index = -1;
        window.TICK_button_edittick.setEnabled(false);
        window.TICK_button_sharetick.setEnabled(false);
        window.TICK_textarea_tickdetails.setEditable(false);
        window.TICK_button_unarchive.setVisible(true);
        window.TICK_button_unarchive.setEnabled(false);
        window.TICK_textarea_tickdetails.setText("");
        window.TICK_button_markdone.setEnabled(false);
        window.TICK_button_link.setEnabled(false);
    }
    /**
     * Function for loading textarea to database
     * @param to_load 
     */
    void load_textarea(ArrayList<String> to_load,JTextArea to_fill){
        String data = "";
        
        for(String line : to_load){
            data = data + line + "\n";
        }
        
        to_fill.setText(data);
    }
    /**
     * Function for loading choosen tick from database
     * @throws SQLException 
     */
    void tick_list_clicked() throws SQLException{
        if( window.actual_choosen_element_index != -1){
            if ( window.TICK_button_active_ticks.getText().equals("Active Ticks")){
                int tick_id = window.actual_choosen_element_index;
                Database_Tick dt = new Database_Tick(window.database);

                if ( dt.check_if_exists(tick_id) ){

                    ArrayList<String> to_view = dt.view_tick(tick_id);
                    load_textarea(to_view,window.TICK_textarea_tickdetails);
                    window.TICK_button_delete.setEnabled(true);
                    window.TICK_button_edittick.setEnabled(true);
                    window.TICK_button_sharetick.setEnabled(true);
                    window.TICK_button_markdone.setEnabled(true);
                    window.TICK_button_link.setEnabled(true);
                }
            }
            else{
                int tick_id = window.actual_choosen_element_index;
                Database_Tick dt = new Database_Tick(window.database);

                if ( dt.check_if_exists(tick_id) ){

                    ArrayList<String> to_view = dt.view_tick(tick_id);
                    load_textarea(to_view,window.TICK_textarea_tickdetails);
                    window.TICK_button_edittick.setEnabled(false);
                    window.TICK_button_sharetick.setEnabled(false);
                    window.TICK_button_markdone.setEnabled(false);
                    window.TICK_button_unarchive.setEnabled(true);
                    window.TICK_button_unarchive.setVisible(true);
                    window.TICK_button_link.setEnabled(false);
                    window.TICK_button_delete.setEnabled(false);
                }
            }
            
        }
    }
    
    /**
     * Function for logging data
     * @param data 
     */
    void log(String data){
        window.database.log.add(data, "GUI WINDOW MANAGER "+version);
    }
    
}
