/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *Object for communicating with user
 * @author jakub
 */
public class UI_Tick_Interface {
    final String version = "v1.0.4";
    
    Scanner sc;                     // main object for scanning 
    ArrayList<String> history;      // main log of the input,output
    Console console;
    
    
    final String PROMPT = ">";
    
    String tab = "";                // variable used to 'move' print
    int last_und;
    boolean int_flag = false;
    int last_input;
    
    ArrayList<Integer> numbers; // collection for found 
                                // numbers in user input
    
    /**
     * Main constructor of the object.
     */
    UI_Tick_Interface(){
        sc = new Scanner(System.in);
        history = new ArrayList<>();
        numbers = new ArrayList<>();
        last_und = -1;
    }
    
    /**
     * UI_Tick_Interface.interface_get()
     * @return String 
     */
    String interface_get(){
        numbers.clear();
        System.out.print(tab+PROMPT);
        String input = sc.nextLine();
        history.add("UI - > USER INPUT : "+input+"\n");
        last_und = understand(input);
        get_numbers(input);
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
        System.out.println(tab+text);
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
            last_input = a;
            int_flag = true;
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
    
    void get_numbers(String input){
        // looping on words
        for (String word : input.split(" ")){
            try{
                int number = Integer.parseInt(word);
                numbers.add(number);
                int_flag = true;
            }catch(NumberFormatException e){
                int_flag = false;
            }
        }
        if ( !numbers.isEmpty() ){
            last_input = numbers.get(0);
        }
    }
    
    /**
     * UI_Tick_Interface.check_existance(String line, String [] keys)
     * @param line
     * @param keys
     * @return String
     * Method checks if in line is key word given in the array
     */
    String check_existance(String line, String [] keys){
        List<String> user_input = new ArrayList<String>(Arrays.asList(line.split(" ")));
        List<String> given_keys = new ArrayList<String>(Arrays.asList(keys));
        
        for ( String word : user_input ){
            if ( given_keys.contains(word) ){
                return word;
            }
        }
        return null;
    }
    
    String check_existance(List<String> user_input, String [] keys){
        List<String> given_keys = new ArrayList<String>(Arrays.asList(keys));
        for ( String word : user_input ){
            if ( given_keys.contains(word) ){
                return word;
            }
        }
        return null;
    }
    
    /**
     * UI_Tick_Interface.check_existance_int( List<String> user_input )
     * @param user_input
     * @return int
     * Returns first found integer, if not found returns -1
     */
    int check_existance_int ( List<String> user_input ){
        for ( String word : user_input ){
            try{
                Integer i = Integer.parseInt(word);
                return i; 
            }catch(NumberFormatException e){
            }
        }
        return -1;
    }
}
