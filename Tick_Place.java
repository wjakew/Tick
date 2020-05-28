/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;

/**
 *Object for storing Place info
 * @author jakub
 */
public class Tick_Place extends Tick_Element{
    /**
     * CREATE TABLE PLACE
        (
        place_id INT AUTO_INCREMENT PRIMARY KEY,
        owner_id INT,
        place_name VARCHAR(30),
        address_id INT,
        CONSTRAINT fk_place FOREIGN KEY (address_id) REFERENCES ADDRESS(address_id),
        CONSTRAINT fk_place2 FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
        );
     */
    int place_id;
    int owner_id;
    String place_name;
    int address_id;
    
    // main constructor
    Tick_Place(){
        super("Tick_Place");
        place_id = -1;
        owner_id = -1;
        place_name = "";
        address_id = 1;
        
        super.put_elements(wall_updater());
    }
    
    // constructor with 1 parameter
    Tick_Place(ArrayList<Tick_Brick> to_add){
        super("Tick_Place");
        place_id = to_add.get(0).i_get();
        owner_id = to_add.get(1).i_get();
        place_name = to_add.get(2).s_get();
        address_id = to_add.get(3).i_get();
        super.put_elements(wall_updater());
    }
    
    
    /**
     * Tick_Place.wall_updater()
     * @return ArrayList
     * Updates Tick_Brick collection
     */
    ArrayList<Tick_Brick> wall_updater(){
        ArrayList<Tick_Brick> to_ret = new ArrayList<>();
        
        to_ret.add(new Tick_Brick(place_id));
        to_ret.add(new Tick_Brick(place_name));
        to_ret.add(new Tick_Brick(address_id));
        return to_ret;
    }
    /**
     * Tick_Place.init_CUI()
     * Prints prompts to enter the place
     */
    void init_CUI(){
        super.inter.interface_print("Enter the place name: ");
        place_name = super.inter.interface_get();
    }
    /**
     * Tick_Place.get_lines_to_show()
     * @return ArrayList
     * Returns lines of object content
     */
    ArrayList<String> get_lines_to_show(){
        /**
         * id: /place_id/
         * Place name: /place_name/
         */
        ArrayList<String> to_ret = new ArrayList<>();
        to_ret.add("id: "+Integer.toString(place_id));
        to_ret.add("Place name: "+place_name);
        return to_ret;
    }

    
}
