/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;

/**
 *Object for storing category data.
 * @author jakub
 */
public class Tick_Category extends Tick_Element{
    /**
     * CREATE TABLE CATEGORY
        (
        category_id INT AUTO_INCREMENT PRIMARY KEY,
        owner_id INT,
        category_name VARCHAR(45),
        category_note VARCHAR(100),
        CONSTRAINT fk_category FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
        );
     */
    int category_id;
    int owner_id;
    String category_name;
    String category_note;
    
    // main constructor
    Tick_Category(){
        super("Tick_Category");
        category_id = -1;
        owner_id = -1;
        category_name = "";
        category_note = "";
        super.put_elements(wall_updater());
    }
    
    // one argument constructor
    Tick_Category(ArrayList<Tick_Brick> to_add){
        super("Tick_Category");
        category_id = to_add.get(0).i_get();
        owner_id = to_add.get(1).i_get();
        category_name = to_add.get(2).s_get();
        category_note = to_add.get(3).s_get();
        super.put_elements(wall_updater());
    }
    
    /**
     * Tick_Category.wall_updater()
     * @return ArrayList
     * Returns 'brick wall'
     */
    ArrayList<Tick_Brick> wall_updater(){
        ArrayList<Tick_Brick> to_ret = new ArrayList<>();
        
        to_ret.add(new Tick_Brick(category_id));
        to_ret.add(new Tick_Brick(owner_id));
        to_ret.add(new Tick_Brick(category_name));
        to_ret.add(new Tick_Brick(category_note));
        return to_ret;
    }
    /**
     * Tick_Category.init_CUI()
     * Interface for CUI
     */
    void init_CUI(){
        super.inter.interface_print("Category name:");
        category_name = super.inter.interface_get();
        super.inter.interface_print("Category note:");
        category_note = super.inter.interface_get();
        super.put_elements(wall_updater());
    }
    
}
