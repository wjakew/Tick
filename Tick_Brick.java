/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;

/**
 *Object for storing data of the field with info
 * @author jakub
 */
public class Tick_Brick {
    final String version = "v1.0.0";
    
    int MAX = -2147483648;
    int index_in_collection = -1;
    
    // fields for storing data
    int data_int;
    String data_string;
    ArrayList<String> data_array;
    int category;
    
    Tick_Brick(){
        data_int = MAX;
        data_string = null;
        category = 0;
        data_array = null;
    }
    
    Tick_Brick(int data){
        data_int = data;
        data_string = null;
        get_category();
    }
    
    Tick_Brick(String data){
        data_int = MAX;
        data_string = null;
        get_category();
    }
    
    Tick_Brick(ArrayList<String> data){
        data_int = MAX;
        data_string = null;
        data_array = data;
        get_category();
    }
    
    /**
     * Tick_Brick.get_category()
     * Function for categorise data input
     */
    void get_category(){
        if ( data_int != MAX ){ // category int
            category = 1;
        }
        else if ( data_string != null ){ // category string
            category = 2;
        }
        else if ( data_array != null ){ // category array
            category = 3;
        }
        else{
            category = -1;      // category error
        }
    }
    /**
     * Tick_Brick.set_index_collection(int index)
     * @param index 
     * Function for passing index from collection where Tick_Brick is
     */
    void set_index_collection(int index){
        index_in_collection = index;
    }
    
    void show(){
        System.out.println("Tick_Brick version ("+version+"):");
        System.out.println("Category : "+Integer.toString(category));
        System.out.println("data_int : "+Integer.toString(data_int));
        System.out.println("data_string : "+data_string);
        System.out.println("data_array:"+data_array.toString());
    }
}
