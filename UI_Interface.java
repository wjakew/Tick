/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *Object for communicating with user
 * @author jakub
 */
public class UI_Interface {
    final String version = "v1.1.3";
    
    Scanner sc;                     // main object for scanning 
    ArrayList<String> history;      // main log of the input,output
    Console console;
    
    String[] not_get = new String[] {"clip","again","clip again"};
    List<String> avoid;
    
    
    final String PROMPT = ">";
    
    String tab = "";                // variable used to 'move' print
    int last_und;
    boolean int_flag = false;       // variable used to flag if number found in input
    boolean blank = true;           // variable set if input is blank
    int last_input;
    String last_string;
    String raw_input;
    
    ArrayList<Integer> numbers; // collection for found 
                                // numbers in user input
    
    /**
     * Main constructor of the object.
     */
    UI_Interface(){
        sc = new Scanner(System.in);
        history = new ArrayList<>();
        numbers = new ArrayList<>();
        last_und = -1;
        avoid = Arrays.asList(not_get);
        last_string = "";
        raw_input = "";
        
    }
    
    /**
     * UI_Interface.interface_get_w_prompt(String prompt)
     * @param prompt
     * @return String
     * Function for getting user input with prompt
     */
    String interface_get_w_prompt(String prompt){
        interface_print(prompt);
        return interface_get();
    }
    
    /**
     * UI_Interface.avoid_function()
     * @return boolean
     * 
     */
    boolean avoid_function(){
        if ( raw_input !=null) {
            List<String> words_input = Arrays.asList(raw_input.split(" "));
        
            for(String word : words_input){
                if ( avoid.contains(word) ){
                    return true;
                }
            }
            return false;
        }
        return false;
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
        if ( avoid_function() || !input.isEmpty() ){
            blank = false;
        }
        if ( !avoid.contains(input)){
            last_string = input;
        }
        raw_input = input;
        return input;
    }
    
    /**
     * UI_Interface.copy_to_clipboard()
     * @return boolean
     * Function copies last input to clipboard
     */
    boolean copy_to_clipboard(){
        StringSelection data = new StringSelection(last_string);
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(data, data);
        return true;
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
     * UI_Tick_Interface.interface_print()
     * Function for printing blank 
     */
    void interface_print(){
        System.out.println("");
    }
    
    /**
     * UI_Tick_Interface.array_print(ArrayList<String> to_show)
     * @param to_show 
     * Function for printing data from ArrayList
     */
    void array_print(ArrayList<String> to_show){
        for(String line : to_show){
            interface_print(line);
        }
    }
    
    /**
     * UI_Interface.interface_set_indent(int amount)
     * @param amount 
     * Function simplifies making indent in interface
     */
    void interface_set_indent(int amount){
        tab = "";
        for (int i = 0 ; i < amount ; i++){
            tab = tab + "   ";
        }
    }
    void interface_reset_indent(){
        tab = "";
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
    
    /**
     * UI_Interface.check_existance(List<String> user_input,String [] keys)
     * @param user_input
     * @param keys
     * @return String 
     * Function for checking if list has at least one common object with array
     */
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
     * UI_Interface.check_in_array(String to_check,String[] source)
     * @param to_check
     * @param source
     * @return boolean
     * Function check if string is in array
     */
    boolean check_in_array(String to_check,String[] source){
        List<String> ch = Arrays.asList(source);
        return ch.contains(to_check);
    }
    
    /**
     * UI_Interface.fast_check_in_array(String [] source)
     * @param source
     * @return boolean
     * Function returns if last user input is in source array
     */
    boolean fast_check_in_array(String [] source){
        return check_in_array(last_string,source);
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
