/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *Console interface
 * @author jakub
 */
public class CUI_Tick_Inteface {
    final String version = "v0.0.6";
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
     * Main run function of the interface
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
            ui.interface_print("------------------------------------------------");
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
            // link
            else if ( word.equals("link")){
                CUI_FUN_link(words);
                break;
            }
            else if (word.equals("scene")){
                CUI_FUN_scene(words);
                break;
            }
            else if (word.equals("tick")){
                CUI_FUN_tick(words);
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
            ui.interface_print("tick");
            ui.interface_print("    - add ( add simple tick reminder )");
            ui.interface_print("tick /tick_id/");
            ui.interface_print("    - link |  /place/ | /address/ | /hashtag_table/ | /category/ | /note/ ");
            ui.interface_print("          ( links tick to the choosen object ) ");
            ui.interface_print("    - mark | /done/ |");
            ui.interface_print("          ( marks tick and gives it new atribute )");
            ui.interface_print("    - delete ( delete tick ) ");
            ui.interface_print("    - det ( shows details of the tick ) ");
            ui.interface_print("-----------------------------------------------------------");
            ui.interface_print("add ");
            ui.interface_print("    - place | address | hashtable | tag | category | note ");
            ui.interface_print("-----------------------------------------------------------");
            ui.interface_print("show ");
            ui.interface_print("    - place | address | hashtable | tag | category | note ");
            ui.interface_print("-----------------------------------------------------------");
            ui.interface_print("link ");
            ui.interface_print("    - adrplp /address_id/ /place_id/   ( address to place )");
            ui.interface_print("    - hshtag    /tag_id/ /hashtag_table_id/( hashtag table to tag )");
            ui.interface_print("-----------------------------------------------------------");
            ui.interface_print("scene ");
            ui.interface_print("    ( without parameters show active scenes )");
            ui.interface_print("    - add ( inits scene maker ) ");
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
            ui.interface_print("    - update ");
            ui.interface_print("        address | password ");
        }
        // help show
        else if (addons.size() == 2 && addons.contains("show")){
            ui.interface_print("Help for show ");
            ui.interface_print("    - place | address | hashtable | tag | category | note ");
        }
        //help link
        else if (addons.size() == 2 && addons.contains("link")){
            ui.interface_print(" Help for link ");
            ui.interface_print("    Linking is used for connecting two information");
            ui.interface_print("    - adrplp /address_id/ /place_id/   ( address to place )");
            ui.interface_print("    - hshtag    /tag_id/ /hashtag_table_id/( hashtag table to tag )");
        }
        //help scene
        else if (addons.size() == 2 && addons.contains("scene")){
            ui.interface_print("Help for scene ");
            ui.interface_print("    ( without parameters show active scenes )");
            ui.interface_print("    - add ( inits scene maker ) ");
        }
        // help tick
        else if ( addons.size() == 2 && addons.contains("tick")){
            ui.interface_print("Help for tick");
            ui.interface_print("    ( without parameters show active ticks )");
            ui.interface_print("    - add ( add simple tick reminder )");
            ui.interface_print("tick /tick_id/");
            ui.interface_print("    - link |  /place/ | /address/ | /hashtag_table/ | /category/ | /note/ ");
            ui.interface_print("          ( links tick to the choosen object ) ");
            ui.interface_print("    - mark | /done/ |");
            ui.interface_print("          ( marks tick and gives it new atribute )");
            ui.interface_print("    - delete ( delete tick ) ");  
            ui.interface_print("    - det ( shows details of the tick ) ");
        }
        else{
            ui.interface_print("Wrong option");
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
        else{
            ui.interface_print("Wrong option");
        }
    }
    /**
     * CUI_Tick_Interface.CUI_FUN_me(List<String> addons)
     * @param addons
     * @throws SQLException 
     * Function of showing and changing user stuff
     */
    void CUI_FUN_me(List<String> addons) throws SQLException{
        // me
        Database_Link dl = new Database_Link(database);
        if ( addons.size() == 1){
            ui.interface_print("Info about your account:");
            logged_user.show();
            if ( logged_user.address_id == 1){
                ui.interface_print("No address linked");
            }
            else{
                ui.interface_print("Linked address:");
                Tick_Address to_show = dl.get_object_address(logged_user.address_id);
                show_arraylist(to_show.get_lines_to_show());
            }
        }
        // me update password 'content'
        else if ( addons.size() == 4 && addons.contains("password")){
            run = !database.change_password(logged_user, addons.get(3));
            ui.interface_print("Password change, please log again.");
        }
        // me update address
        else if ( addons.size() == 3 && addons.contains("address")){
            Database_Viewer usr_addr_viewer = new Database_Viewer(database,logged_user,"address");
            show_arraylist(usr_addr_viewer.make_view());
            ui.interface_print("Choose address to link: ");
            ui.interface_get();
            if(ui.int_flag){
                logged_user.address_id = ui.last_input;
                logged_user.wall_updater();
                Tick_Address usr_adr = new Tick_Address(database.return_TB_collection(logged_user, "address"));
                Database_Link linker = new Database_Link(database);
                if ( linker.link_user_address(logged_user, usr_adr) ){
                    ui.interface_print("Address linked");
                }
            }
            else{
                ui.interface_print("Wrong option");
            }
        }
        else{
            ui.interface_print("Wrong option");
        }
    }
    
    void show_arraylist(ArrayList<String> to_show){
        if (to_show.size() == 1){
            ui.interface_print("Empty");
        }
        else{
            for(String line: to_show){
                ui.interface_print(line);
            }
        }
    }
    /**
     * CUI_Tick_Interface.CUI_FUN_show(List<String> addons)
     * @param addons
     * @throws SQLException 
     * Function for showing data from database
     */
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
        // show scene
        else if ( addons.size() == 2 && addons.contains("scene")){
            Database_Viewer view = new Database_Viewer(database,logged_user,"scene");
            show_arraylist(view.make_view());
        }
        else{
            ui.interface_print("Wrong option");
        }
    }
    /**
     * CUI_Tick_Interface.CUI_FUN_link(List<String> addons)
     * @param addons 
     * Function for linking two objects of data
     */
    void CUI_FUN_link(List<String> addons) throws SQLException{
        /**
         * - adrplp /address_id/ /place_id/              ( address to place )
           - hshtag    /tag_id/ /hashtag_table_id/       ( hashtag table to tag )
         */
        Database_Link linker = new Database_Link(database);
        if ( addons.size() == 1){
            ui.interface_print("No additional arguments. See help (help link)");
        }
        // link adrplp /address_id/ /place_id/
        else if ( addons.size() == 4 && addons.contains("adrplp")){
            // check if records exists
            if ( database.check_if_record_exists(ui.numbers.get(0), "address") 
                    && database.check_if_record_exists(ui.numbers.get(1), "place")){
                int address_id = ui.numbers.get(0);
                int place_id = ui.numbers.get(1);
                // preparing objects
                
                Tick_Address to_link_address = linker.get_object_address(address_id);
                Tick_Place to_link_place = linker.get_object_place(place_id);
                
                if ( linker.link_place_address(to_link_place, to_link_address) ){
                    ui.interface_print("Link succesfull");
                }
                else{
                    ui.interface_print("Link occured a problem");
                }
            }
            else{
                ui.interface_print("One of the objects not exist");
            }
        }
        // hshtag    /tag_id/ /hashtag_table_id/
        else if ( addons.size() == 4 && addons.contains("hshtag")){
            if ( database.check_if_record_exists(ui.numbers.get(0), "tag") 
                    && database.check_if_record_exists(ui.numbers.get(1), "hashtag table")){
                int tag_id = ui.numbers.get(0);
                int hashtag_table_id = ui.numbers.get(1);
                
                // preparing objects
                Tick_Tag to_link_tag = linker.get_object_tag(tag_id);
                Tick_HashtagT to_link_hashtagT = linker.get_object_hashtagT(hashtag_table_id);         
                
                if ( linker.link_tag_hashtagT(to_link_tag, to_link_hashtagT) ){
                    ui.interface_print("Link succesfull");
                }
                else{
                    ui.interface_print("Link occured a problem");
                }
            }
            else{
                ui.interface_print("One of the objects not exist");
            }
        }
        
        else{
            ui.interface_print("Wrong option");
        }
    }
    /**
     * CUI_Tick_Interface.CUI_FUN_scene(List<String> addons)
     * @param addons 
     */
    void CUI_FUN_scene(List<String> addons) throws SQLException{
        // scene
        if (addons.size() == 1){
            ui.interface_print("Currently active scenes:");
            Database_Viewer view = new Database_Viewer(database,logged_user,"scene");
            show_arraylist(view.make_view());
        }
        // scene add
        else if (addons.size() == 2 && addons.contains("add")){
            ui.interface_print("Welcome in the scene creator: ");
            System.out.printf("%30s %30s %30s", "CATEGORIES", "PLACES", "HASHTAG TABLES\n");
            
            Database_Viewer scene_v = new Database_Viewer(database,logged_user,"scene view");
            ArrayList<String> lines_to_show = scene_v.make_view();
            lines_to_show.remove(0);
            lines_to_show.remove(0);
            lines_to_show.remove(0);
            // showing lines from database viewer
            for (int i = 0; i < lines_to_show.size(); i = i+3){
                System.out.printf("%30s",lines_to_show.get(i));
                if ( i + 3 < lines_to_show.size() ){
                    System.out.printf(" %30s",lines_to_show.get(i+1));
                }
                if ( i + 6 < lines_to_show.size() ){
                    System.out.printf(" %30s",lines_to_show.get(i+2));
                }
                System.out.printf("\n");
            }
            // adding scene
            Tick_Scene to_add = new Tick_Scene();
            to_add.init_CUI();
            
            try{
                if ( database.add_scene(to_add) ){
                    ui.interface_print("Scene added");
                }
                else{
                    ui.interface_print("Scene adding occured a problem");
                }
            }catch(SQLException e){
                ui.interface_print("");
            }
        }

        else{
            ui.interface_print("Wrong option");
        }
    }
    /**
     * CUI_Tick_Interface.CUI_Tick_tick(List<String> addons)
     * @param addons 
     * Function for using functionality tick
     */
    void CUI_FUN_tick(List<String> addons) throws SQLException{
        /**
         * ( without parameters show active ticks )
                - add ( add simple tick reminder )
            tick /tick_id/
                 - link |  /place/ | /address/ | /hashtag_table/ | /category/ | /note/ 
                    ( links tick to the choosen object ) 
                - mark | /done/ |
                    ( marks tick and gives it new atribute )
                - delete ( delete tick )
                - det ( shows details of the tick ) 
         */
        // tick
        if ( addons.size() == 1 ){
            ui.interface_print("Showing active ticks: ");
            Database_Viewer dv = new Database_Viewer(database,database.logged,"tick");
            show_arraylist(dv.make_view());
        }
        // tick add
        else if ( addons.size() == 2 && addons.contains("add")){
            Tick_Tick to_add = new Tick_Tick();
            to_add.init_CUI();
            to_add.owner_id = database.logged.owner_id;
            to_add.wall_updater();
            Database_Tick adder = new Database_Tick(database);
            
            if ( adder.add_tick(to_add) ){
                ui.interface_print("Tick added");
            }
            else{
                ui.interface_print("Error adding tick");
            }
        }
        // tick /tick_id/ link
        else if ( addons.size() == 4 && addons.contains("link") && ui.check_existance_int(addons)!= -1){
            int tick_id = ui.last_input;
            Database_Tick linker = new Database_Tick(database);
            if ( linker.check_if_exists(ui.last_input)){
                // here we have checked if tick exists
                String [] keys = new String[] {"place","address","hashtag_table","category","note"};
                String mode = ui.check_existance(addons, keys);
                // showing viewer for database
                if (  mode != null ){
                    Database_Viewer view = new Database_Viewer(database,database.logged,mode);
                    show_arraylist(view.make_view());
                    ui.interface_get();
                    int index = ui.last_input;
                    
                    if ( database.check_if_record_exists(index, mode) ){
                        // we have correct id
                        ui.interface_print("Tick id found: "+tick_id);
                        ui.interface_print("Mode: "+ mode);
                        ui.interface_print("Object id: "+index);
                        if ( linker.update_data(index,tick_id,mode)){
                            ui.interface_print("Data linked ("+mode+")");
                        }
                        else{
                            ui.interface_print("Unable to link data. Error");
                        }
                    }
                    else{
                        ui.interface_print("Wrong id for the object");
                    }
                }
                else{
                    ui.interface_print("Wrong object to link");
                }
                
            }
            else{
                ui.interface_print("No tick with given id");
            }
        }
        // tick /tick_id/ det
        else if ( addons.size() == 3 && addons.contains("det") && ui.check_existance_int(addons)!= -1){
            Database_Tick shower = new Database_Tick(database);
            
            if ( database.check_if_record_exists(ui.last_input, "tick") ){
                show_arraylist(shower.view_tick(ui.last_input));
            }
            else{
                ui.interface_print("Wrong tick id");
            }
        }
        else{
            ui.interface_print("Wrong arguments for tick. See: help tick ");
        }
    }
}
