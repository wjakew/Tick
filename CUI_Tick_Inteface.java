/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *Console interface
 * @author jakub
 */
public class CUI_Tick_Inteface {
    final String version = "v0.0.1";
    final String HEADER  = "CUI";
    boolean logged = false;
    Tick_User logged_user = null;
    
    boolean run = true;
    Date actual_date = null;
    
    UI_Tick_Interface ui;
    Database database;
    
    //  main constructor
    CUI_Tick_Inteface(Database database) throws SQLException{
        this.database = database;
        ui = new UI_Tick_Interface();
        actual_date = new Date();
    }
    
    /**
     * CUI_Tick_Interface.run()
     * @throws SQLException 
     * Main run function of the inteface
     */
    void run() throws SQLException{
        welcome_screen();
        
        // welcome prompt
        while ( welcome_menu());    // setting the login or register option

        // login loop
        while ( !login_prompt() );  // in login_prompt method is closing the procedure
        
        // if we are here login was succesful
    
        while ( run ){  // main loop of the program
            // showing main menu
            String user_input = ui.interface_get();
            CUI_logic(user_input);   
        }
    }
    /**
     * CUI_Tick_Interface.CUI_logic(String user_input)
     * @param user_input
     * @throws SQLException 
     * Main logic of the program
     */
    void CUI_logic(String user_input) throws SQLException{
        List<String> words = Arrays.asList(user_input.split(" ")); 
        
        for (String word : words){
            // exit
            if (word.equals("exit")){
                close();
            }
            // help
            else if ( word.equals("help")){
                CUI_FUN_help(words);
                break;
            }
            // add
            else if ( word.equals("add")){
                CUI_FUN_add(words);
                break;
            }
            // me
            else if ( word.equals("me")){
                CUI_FUN_me(words);
                break;
            }
            // show
            else if ( word.equals("show")){
                CUI_FUN_show(words);
                break;
            }
            // not supported command
            else{
                ui.interface_print("Wrong command");
                echo_CUI_interface(words.toString());
            }
        }
    }
    
    /**
     * CUI_Tick_Interface.login_prompt()
     * @return boolean
     * @throws SQLException 
     * Shows login prompt
     */
    boolean login_prompt() throws SQLException{
        ui.interface_print("Login into program...");
        ui.interface_print("User Login: ");
        String user_login = ui.interface_get();
        ui.interface_print("Password: ");
        String user_password = ui.get_password();
        
        // ending login procedure
        if (user_login.equals("exit") || user_password.equals("exit")){
            ui.interface_print("Login procedure suspended");
            close();
            System.exit(0);
        }
        
        logged_user = database.user_login(user_login, user_password);
        
        if ( logged_user != null){
            ui.interface_print("Logged!");
            return true;
        }
        ui.interface_print("Wrong password or login.");
        return false;
    }
    /**
     * CUI_Tick_Interface.echo_module(String input)
     * @param input 
     * Function for showing echo 
     */
    void echo_CUI_interface(String input){
        ui.interface_print("echo: "+input);
        ui.interface_print("len: "+ Integer.toString(input.split(" ").length));
    }
    /**
     * CUI_Tick_Interface.close()
     * @throws SQLException 
     * Function closing program
     */
    void close() throws SQLException{
        if (database != null){
            database.close();
            database.log.add(ui.history);
        }
        database.log.add("CUI Closed", HEADER);
        ui.interface_print("Program exited");
        run = false;
    }
    
    /**
     * CUI_Tick_Interface.welcome_screen()
     * Shows welcome screen
     */
    void welcome_screen(){
        ui.interface_print("Tick Console User Interface (TCUI version "+version+")");
    }
    
    /**
     * CUI_Tick_Interface.welcome_menu()
     * @return
     * @throws SQLException 
     * Menu for login and registration
     */
    boolean welcome_menu() throws SQLException{
        ui.interface_print("1 - Login | 2 - Register | 3 - Exit | 4 - Info");
            String dec = ui.interface_get();
            if ( ui.last_und == 1){ // it's an int
                
                if (ui.last_input == 1){    // go next to login
                     return false;
                }
                else if (ui.last_input == 2){   // go to register
                    ui.interface_print("Welcome in Tick registration!");
                    Tick_User to_register = new Tick_User();
                    to_register.init_CUI();
                    database.register_user(to_register);
                    return false;
                }
                else if ( ui.last_input == 3){
                    close();
                    System.exit(0);
                }
                else if ( ui.last_input == 4){
                    info_screen();
                    return true;
                }
                else{
                    ui.interface_print("Wrong option");
                    return true;
                }
            }
            ui.interface_print("You have to enter the number of option");
            return true;
    }
    /**
     * CUI_Tick_Interface.info_screen()
     * Screen for showing info about the creators of the program
     */
    void info_screen(){
        ui.interface_print("Tick info: ");
        ui.interface_print("Made by JAKUB WAWAK");
        ui.interface_print("May 2020 - all rights reserved");
        ui.interface_print("Program for getting shit done");
        ui.interface_print("kubawawak@gmail.com");
    }
    
    //----------------------------------------FUNCTIONS OF THE INTERFACE
    /**
     * CUI_Tick_Inteface.help_screen(List<String> addons)
     * @param addons 
     * Shows help for the user
     */
    void CUI_FUN_help(List<String> addons){
        // help
        if ( addons.size() == 1){
            ui.interface_print("Help for the program: ");
            ui.interface_print("-----------------------------------------------------------");
            ui.interface_print("add ");
            ui.interface_print("    - place | address | hashtable | tag | category | note ");
            ui.interface_print("-----------------------------------------------------------");
            ui.interface_print("show ");
            ui.interface_print("    - place | address | hashtable | tag | category | note ");
            ui.interface_print("-----------------------------------------------------------");
            ui.interface_print("me ");
            ui.interface_print(" ( without parameters shows account )"); 
            ui.interface_print("    - update ");
            ui.interface_print("        address | password ");
            ui.interface_print("-----------------------------------------------------------");
        }
        // help add
        else if (addons.size() == 2 && addons.contains("add")){
            ui.interface_print("Help for add ");
            ui.interface_print("    - place | address | hashtable | tag | category | note ");
        }
        // help me
        else if (addons.size() == 2 && addons.contains("me")){
            ui.interface_print("Help for me ");
            ui.interface_print(" ( without parameters shows account )"); 
            ui.interface_print("    - password ( changes password ) ");
        }
        // help show
        else if (addons.size() == 2 && addons.contains("show")){
            ui.interface_print("Help for show ");
            ui.interface_print("    - place | address | hashtable | tag | category | note ");
        }
    }
    /**
     * CUI_Tick_Interface.CUI_FUN_add(List<String> addons)
     * @param addons
     * @throws SQLException 
     * Function for adding data to the database
     */
    void CUI_FUN_add(List<String> addons) throws SQLException{
        //   +         +         +         +       +        +
        //  place | address | hashtable | tag | category | note 
        // add
        if ( addons.size() == 1 ){
            ui.interface_print("No additional arguments. See help ( help add ) ");
        }
        // add address
        else if ( addons.size() == 2 && addons.contains("address")){
            Tick_Address to_add = new Tick_Address();
            to_add.init_CUI();
            if ( database.add_address(to_add) ) {
                ui.interface_print("Address added");
            }
            else{
                ui.interface_print("Something goes wrong");
            }
        }
        // add place
        else if ( addons.size() == 2 && addons.contains("place")){
            Tick_Place to_add = new Tick_Place();
            to_add.init_CUI();
            to_add.owner_id = logged_user.owner_id;
            if ( database.add_place(to_add)){
                ui.interface_print("Place added");
            }
            else{
                ui.interface_print("Something goes wrong");
            }
        }
        // add hashtable
        else if ( addons.size() == 2 && addons.contains("hashtable")){
            Tick_HashtagT  to_add = new Tick_HashtagT();
            to_add.init_CUI();
            to_add.owner_id = logged_user.owner_id;
            to_add.put_elements(to_add.wall_updater());
            if (database.add_hashtagT(to_add)){
                ui.interface_print("Hashtag Table added");
            }
            else{
                ui.interface_print("Something goes wrong");
            }
        }
        // add tag
        else if ( addons.size() == 2 && addons.contains("tag")){
            Tick_Tag to_add = new Tick_Tag();
            to_add.init_CUI();
            to_add.owner_id = logged_user.owner_id;
            to_add.put_elements(to_add.wall_updater());
            if (database.add_tag(to_add)){
                ui.interface_print("Tag added");
            }
            else{
                ui.interface_print("Something goes wrong");
            }
        }
        // add category
        else if ( addons.size() == 2 && addons.contains("category")){
            Tick_Category to_add = new Tick_Category();
            to_add.init_CUI();
            to_add.owner_id = logged_user.owner_id;
            to_add.put_elements(to_add.wall_updater());
            if (database.add_category(to_add)){
                ui.interface_print("Category added");
            }
            else{
                ui.interface_print("Something goes wrong");
            }
        }
        // add note
        else if ( addons.size() == 2 && addons.contains("note")){
            Tick_Note to_add = new Tick_Note();
            to_add.init_CUI();
            to_add.owner_id = logged_user.owner_id;
            to_add.put_elements(to_add.wall_updater());
            if (database.add_note(to_add)){
                ui.interface_print("Note added");
            }
            else{
                ui.interface_print("Something goes wrong");
            }
        }
    }
    /**
     * CUI_Tick_Inteface.CUI_FUN_me(List<String> addons)
     * @param addons
     * @throws SQLException 
     * Function of showing and changing user stuff
     */
    void CUI_FUN_me(List<String> addons) throws SQLException{
        // me
        if ( addons.size() == 1){
            ui.interface_print("Info about your account:");
            logged_user.show();
        }
        // me password 'content'
        else if ( addons.size() == 3 && addons.contains("password")){
            run = !database.change_password(logged_user, addons.get(2));
            ui.interface_print("Password change, please log again.");
        }
    }
    
    void show_arraylist(ArrayList<String> to_show){
        for(String line: to_show){
            ui.interface_print(line);
        }
    }
    
    void CUI_FUN_show(List<String> addons) throws SQLException{
        /**
         *     +        +          +        +       + 
         * - place | address | hashtable | tag | category | note 
         */
        // show
        if ( addons.size() == 1){
            ui.interface_print("No additional arguments. See help ( help show )");
        }
        // show place
        else if ( addons.size() == 2 && addons.contains("place")){
            Database_Viewer view = new Database_Viewer(database,logged_user,"place");
            show_arraylist(view.make_view());
        }
        // show address
        else if ( addons.size() == 2 && addons.contains("address")){
            Database_Viewer view = new Database_Viewer(database,logged_user,"address");
            show_arraylist(view.make_view());
        }
        // show hashtable
        else if ( addons.size() == 2 && addons.contains("hashtable")){
            Database_Viewer view = new Database_Viewer(database,logged_user,"hashtag table");
            show_arraylist(view.make_view());
        }
        // show tag
        else if ( addons.size() == 2 && addons.contains("tag")){
            Database_Viewer view = new Database_Viewer(database,logged_user,"tag");
            show_arraylist(view.make_view());
        }
        // show category
        else if ( addons.size() == 2 && addons.contains("category")){
            Database_Viewer view = new Database_Viewer(database,logged_user,"category");
            show_arraylist(view.make_view());
        }
        // show note
        else if ( addons.size() == 2 && addons.contains("note")){
            Database_Viewer view = new Database_Viewer(database,logged_user,"note");
            show_arraylist(view.make_view());
        }
    }
    
    
}
