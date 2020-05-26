/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 *Object for storing log of the program
 * @author jakub
 */
public class Tick_Log {
    
    final String version = "v1.0.1";
    
    ArrayList<String> log_lines;
    String LOG_SRC = "LOG_TICKPROGRAM_";
    Date actual_date;
    File log_file;
    FileWriter writer;
    
    /**
     * Tic_Log() Constructor
     */
    Tick_Log(){
        log_lines = new ArrayList<>();
        actual_date = new Date();
        LOG_SRC = LOG_SRC + "log from "+actual_date.toString()+".txt";
        init();
    }
    
    /**
     * Tic_Log.init()
     * Initialization of the log content
     */
    void init(){
        log_lines.add("Log file from program TICK\n");
        log_lines.add("Log made by Tick_Log version "+version);
        log_lines.add("TIME OF START: "+ actual_date.toString()+"\n");
        log_lines.add("---------------------------------------\n");
    }
    
    /**
     * Tic_Log.add(String text,String header)
     * @param text
     * @param header 
     * Function for adding data to the log
     */
    void add(String text,String header){
        String to_add = header + " ----> " + text+"\n";
        log_lines.add(to_add);
    }
    /**
     * Tic_Log.add(String text)
     * @param lines 
     * Function for adding data to the log
     */
    void add(ArrayList<String> lines){
        log_lines.addAll(lines);
    }
    
    /**
     * Tic_Log.close()
     * Closing the log content.
     */
    void close(){
        actual_date = new Date();
        log_lines.add("---------------------------------------\n");
        log_lines.add("TIME OF END: "+actual_date.toString()+"\n");
    }
    /**
     * Tic_Log.save()
     * @throws IOException
     * Saving log content to the file
     */
    void save() throws IOException{
        close();
        log_file = new File(LOG_SRC);
        writer = new FileWriter(LOG_SRC);
        
        for ( String line : log_lines ){
            writer.write(line);
        }
        writer.close();
    }
    
    
}
