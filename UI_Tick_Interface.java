/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *Object for communicating with user
 * @author jakub
 */
public class UI_Tick_Interface {
    
    Scanner sc;                 // main object for scanning 
    ArrayList<String> history;      // main log of the input,output
    Console console;
    
    final String PROMPT = ">";
    
    int last_und;
    
    /**
     * Main constructor of the object.
     */
    UI_Tick_Interface(){
        sc = new Scanner(System.in);
        history = new ArrayList<>();
        last_und = -1;
    }
    
    /**
     * UI_Tick_Interface.interface_get()
     * @return String 
     */
    String interface_get(){
        System.out.print(PROMPT);
        String input = sc.nextLine();
        history.add("UI - > USER INPUT : "+input+"\n");
        last_und = understand(input);
        return input;
    }
    /**
     * UI_Tick_Interface.get_password()
     * @return String
     * Returns password typed by user
     */
    String get_password(){
        Console console = System.console();
        if ( console == null){
            return interface_get();
        }
        else{
            char[] password = console.readPassword("Password: ");
            return String.copyValueOf(password);
        }
        
    }
    /**
     * UI_Tick_Interface.interface_print(String text)
     * @param text 
     * Function for printing
     */
    void interface_print(String text){
        history.add("UI - > USER OUTPUT: "+text+"\n");
        System.out.println(text);
    }
    /**
     * UI_Tick_Interface.understand(String input)
     * @param input
     * @return Integer
     * Function for checking if input is integer (ret 1),
     * float (ret 2) or double (ret 3)
     */
    int understand(String input){
        try{
            int a = Integer.parseInt(input);
            return 1;
        }catch( NumberFormatException e){
            // it's not an int
            try{
                float a = Float.parseFloat(input);
                return 2;
            }catch( NumberFormatException f){
            // it's not a float
                try{
                    double a = Double.parseDouble(input);
                    return 3;
                }catch( NumberFormatException g){
                    //it's not a double
                        return 0;
                }
             }
        }
    }
}