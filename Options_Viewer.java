/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *CUI Viewer for options
 * @author jakubwawak
 */
public class Options_Viewer {
    
    final String version = "v1.0.0";
    Options options;
    UI_Interface ui;
    boolean run = true;
    
    // menus to show
    
    ArrayList<String> main_menu_list = new ArrayList<String>() {{
            add("1. Status");
            add("2. Me");
            add("3. General view options");
            add("4. Information");
            add("5. Debug options");
            add("exit.");
            }};
    
    ArrayList<String> main_menu_me = new ArrayList<String>(){{
            add("1. Change password");
            add("2. Link address");
            }};
    
    ArrayList<String> main_menu_gvo = new ArrayList<String>(){{
            add("1.Tick view");
            add("2.Scenes view");
            add("3.List view");
            add("4.blank");
            }};

    // main constructor
    Options_Viewer(Options options){
        
        this.options = options;
        ui = new UI_Interface();
    
    }
    
    /**
     * Options_Viewer.user_input(String message)
     * @param message
     * @return String
     * Function for 
     */
    String user_input(String message){
        ui.interface_print(message);
        return ui.interface_get();
    }
    
    /**
     * Options_Viewer.menu_viewer(ArrayList<String> menu)
     * @param menu 
     * @param indent_mark
     * Function for showing menu items in console
     */
    void _viewer(ArrayList<String> menu,int indent_mark){
        for(int i = 0 ; i < indent_mark ; i++){
            ui.tab = ui.tab + "   ";
        }
        ui.interface_print("----");
        for (String line : menu){
            ui.interface_print(line);
        }
        ui.interface_print("----");
        ui.tab = "";
    }
    
    void CUI_OPT_logic(String user_input, String mode) throws SQLException{
        
        List<String> words = Arrays.asList(user_input.split(" ")); 
        // main menu set
        if ( mode.equals ( "main_menu" )){
            
            // status
            if ( user_input.equals("1")){
                CUI_OPT_FUN_status(words);
            }
            // me
            else if ( user_input.equals("2")){
                CUI_OPT_FUN_me(words);
            }
            // general view options
            else if ( user_input.equals("3")){
                CUI_OPT_FUN_gvo(words);
            }
            // exit
            else if (user_input.equals("exit")){
                run = false;
            }
            else{
                ui.interface_print("Wrong main menu option");
            } 
        } 
    }
    
    /**
     * Options_Viewer.run()
     * Function for running main code and show menus
     */
    void run() throws SQLException{
        CUI_OPT_welcomescreen();
        while(run){
            CUI_OPT_mainloop();
        }
    }
    
    /**
     * Options_Viewer.CUI_OPT_welcomescreen()
     * Function for showing welcome screen
     */
    void CUI_OPT_welcomescreen(){
        ui.interface_print("Options");
        ui.interface_print("Powered by Options Viewer "+version + " and Options "+options.version);
    }

    /**
     * Options_Viewer.CUI_OPT_mainloop()
     * Function for showing main menu
     */
    void CUI_OPT_mainloop() throws SQLException{
        _viewer(main_menu_list,0);
        String ans = user_input("Your option:");
        CUI_OPT_logic(ans,"main_menu");
    }

    //------------------------option viewer functions
    
    /**
     * Options_Viewer.CUI_OPT_FUN_status(List<String> addons)
     * @param addons 
     * Function for showing status of the options
     */
    void CUI_OPT_FUN_status(List<String> addons) throws SQLException{
        if ( options.internal_fail ){
            ui.interface_print("Internal fail of options");
        }
        else{
            _viewer(options.show_data(),5);
        } 
    }
    
    /**
     * Options_Viewer.approval_window(String item_name)
     * @param item_name
     * @return boolean
     * Function returns prompt to approve action
     */
    boolean approval_window(String item_name){
        ui.interface_print("Are you sure to change data in "+item_name+"? ( y/n )");
        String choose = ui.interface_get();
        return choose.equals("y");
    }
    
    /**
     * Options_Viewer.CUI_OPT_FUN_me(List<String> addons)
     * @param addons
     * @throws SQLException 
     * Function implements me functionality
     */
    void CUI_OPT_FUN_me(List<String> addons) throws SQLException{
        if ( !options.internal_fail ){
            
            _viewer(main_menu_me,5);
            String choose = ui.interface_get();
            
            switch(ui.last_input){
                case 1:
                    // update password
                    ui.interface_print("Updating password");
                    ui.interface_print("Type actual password: ");
                    String old_password = ui.get_password();
                    ui.interface_print("Type new password:");
                    String new_password = ui.get_password();
                    ui.interface_print("Type new password again:");
                    String new_password2 = ui.get_password();
                    if ( new_password2.equals(new_password)){
                        if ( approval_window("password")){
                            int ret_code = options.update_password(old_password, new_password2);
                            switch (ret_code) {
                                case 1:
                                    ui.interface_print("Password updated");
                                    break;
                                case -1:
                                    ui.interface_print("Database faliure");
                                    break;
                                default:
                                    ui.interface_print("Wrong actual password");
                                    break;
                            }
                        }
                        else{
                            ui.interface_print("Cancelled");
                        }
                    }
                    else{
                        ui.interface_print("Passwords doesn't match");
                    }
                    break;
                case 2:
                    // link address
                    ui.interface_print("Link address");
                    Database_Link dl = new Database_Link(options.database);
                    Database_Viewer dv = new Database_Viewer(options.database,options.database.logged,"address");
                    _viewer(dv.make_view(),2);
                    ui.interface_print("Choose address to link: ");
                    ui.interface_get();
                    if ( options.database.check_if_record_exists(ui.last_input, "address")){
                        Tick_Address ta = new Tick_Address(options.database.return_TB_collection(options.database.logged, "address", ui.last_input));
                        if ( dl.link_user_address(options.database.logged, ta)){
                            ui.interface_print("Address linked");
                        }
                        else{
                            ui.interface_print("Failed to link address");
                        }
                    }
                    else{
                        ui.interface_print("Address record with that id doesn't exist");
                    }

                    break;
                default:
                    ui.interface_print("Wrong option");
                    break;
            }
        }
        else{
            ui.interface_print("Failed to run option 'me'");
        }
    }
    
    /**
     * Options_Viewer.CUI_OPT_FUN_gvo(List<String> words)
     * @param words 
     * Function implements general view options
     */
    void CUI_OPT_FUN_gvo(List<String> addons) throws SQLException{
        
        _viewer(main_menu_gvo,2);
        
        ui.interface_print("Actual view: "+options.get_view_option());
        
        ui.interface_get();
        
        if ( ui.last_input >= 1 && ui.last_input <= 4 ){
            switch(ui.last_input){
                case 1:
                    options.update_view_option("tick");
                    break;
                case 2:
                    options.update_view_option("scene");
                    break;
                case 3:
                    options.update_view_option("list");
                    break;
                case 4:
                    options.update_view_option("blank");
                    break;
                default:
                    ui.interface_print("Error.");
                    break;
            }
        }
        else{
            ui.interface_print("Wrong input");
        }
        
    }
    
}
