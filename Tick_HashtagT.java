/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;

/**
 *Object for storing tables of tags
 * @author jakub
 */
public class Tick_HashtagT extends Tick_Element{
    
    /**
     * CREATE TABLE HASHTAG_TABLE
        (
        hashtag_table_id INT AUTO_INCREMENT PRIMARY KEY,
        owner_id INT,
        hashtag_table_name VARCHAR(45),
        hashtag_table_note VARCHAR(100),
        CONSTRAINT fk_hshtable2 FOREIGN KEY (owner_id) REFERENCES OWN(owner_id)
        );
     */
    int hashtag_table_id;
    int owner_id;
    String hashtag_table_name;
    String hashtag_table_note;
    
    // main constructor
    Tick_HashtagT(){
        super("Tick_HashtagT");
        hashtag_table_id = -1;
        owner_id = -1;
        hashtag_table_name = "";
        hashtag_table_note = "";
        super.put_elements(wall_updater());
    }
    
    // constructor with one argument
    Tick_HashtagT(ArrayList<Tick_Brick> to_add){
        super("Tick_HashtagT");
        hashtag_table_id = to_add.get(0).i_get();
        owner_id = to_add.get(1).i_get();
        hashtag_table_name = to_add.get(2).s_get();
        hashtag_table_note = to_add.get(3).s_get();
        super.put_elements(wall_updater());
    }
    
    /**
     * Tick_HashtagT.wall_updater()
     * @return ArrayList
     * Returns 'brick wall'
     */
    ArrayList<Tick_Brick> wall_updater(){
        ArrayList<Tick_Brick> to_ret = new ArrayList<>();
        to_ret.add(new Tick_Brick(hashtag_table_id));
        to_ret.add(new Tick_Brick(owner_id));
        to_ret.add(new Tick_Brick(hashtag_table_name));
        to_ret.add(new Tick_Brick(hashtag_table_note));
        return to_ret;
    }
    /**
     * Tick_HashtagT.init_CUI()
     * Input for CUI
     */
    void init_CUI(){
        super.inter.interface_print("Hashtag Table Name:");
        hashtag_table_name = super.inter.interface_get();
        super.inter.interface_print("Hashtag Table Note:");
        hashtag_table_note = super.inter.interface_get();
        super.put_elements(wall_updater());
    }
    
}
