/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *CUI Viewer for options
 * @author jakubwawak
 */
public class Options_Viewer {
    
    final String version = "v0.0.1";
    Options options;
    UI_Interface ui;
    boolean run = true;
    
    // menus to show
    
    ArrayList<String> main_menu_list = new ArrayList<String>() {{
            add("1. Status");
            add("2. Me");
            add("3. General view options");
            add("4. Information");
            add("exit.");
            }};
    
    ArrayList<String> main_menu_me = new ArrayList<String>(){{
            add("1. Change password");
            add("2. Link address");
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
    
    void CUI_OPT_logic(String user_input, String mode){
        
        List<String> words = Arrays.asList(user_input.split(" ")); 
        // main menu set
        if ( mode.equals ( "main_menu" )){
            
            // status
            if ( user_input.equals("1")){
                CUI_OPT_FUN_status(words);
            }
            else if ( user_input.equals("2")){
                CUI_OPT_FUN_me(words);
            }
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
    void run(){
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
    void CUI_OPT_mainloop(){
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
    void CUI_OPT_FUN_status(List<String> addons){
        if ( options.internal_fail ){
            ui.interface_print("Internal fail of options");
        }
        else{
            _viewer(options.show_data(),5);
        } 
    }
    
    void CUI_OPT_FUN_me(List<String> addons){
        if ( !options.internal_fail ){
            
            _viewer(main_menu_me,5);
            String choose = ui.interface_get();
            
            switch(ui.last_input){
                case 1:
                    // update password
                    ui.interface_print("Updating password");
                    break;
                case 2:
                    // link address
                    ui.interface_print("Link address");
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
    
}
