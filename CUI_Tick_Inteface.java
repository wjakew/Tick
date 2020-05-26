/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.SQLException;
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
        while ( welcome_menu());

        // login loop
        while ( !login_prompt() );  // in login_prompt method is closing the procedure
        
        // if we are here login was succesful
    
        while ( run ){  // main loop of the program
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
            
            if (word.equals("exit")){
                close();
            }
            else{
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
                    return true;
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
    
}
