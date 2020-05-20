/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;

/**
 *Main object of the program, all other objects extends it
 * @author jakub
 */
public class Tick_Element {
    
    String tick_Element_Name;                       // name of the overriding element ( name of the class )
    ArrayList<Tick_Brick> tick_Element_Elements;    // collection of the elements to add
    int tick_Element_size;
    UI_Tick_Interface inter;
    
    
    Tick_Element(String object_name){
        inter = new UI_Tick_Interface();
        tick_Element_Name = object_name;
        tick_Element_Elements = new ArrayList<>();
        tick_Element_size = 0;
    }
    
    void put_elements(ArrayList<Tick_Brick> object_elements){
        tick_Element_Elements = object_elements;
        tick_Element_size = object_elements.size();
    }
}
