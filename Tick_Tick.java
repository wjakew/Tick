/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jakub
 */
public class Tick_Tick extends Tick_Element{
    /**
     * CREATE TABLE TICK
        (
        tick_id INT AUTO_INCREMENT PRIMARY KEY,
        owner_id INT,
        place_id INT,
        category_id INT,
        note_id INT,
        hashtag_table_id INT,
        tick_done_id INT,
        tick_done_start VARCHAR(60),
        tick_date_end VARCHAR(60),
        tick_name VARCHAR(60),
        CONSTRAINT fk_tick FOREIGN KEY (owner_id) REFERENCES OWN(owner_id),
        CONSTRAINT fk_tick1 FOREIGN KEY (place_id) REFERENCES PLACE(place_id),
        CONSTRAINT fk_tick2 FOREIGN KEY (category_id) REFERENCES CATEGORY(category_id),
        CONSTRAINT fk_tick3 FOREIGN KEY (note_id) REFERENCES NOTE(note_id),
        CONSTRAINT fk_tick4 FOREIGN KEY (hashtag_table_id) REFERENCES HASHTAG_TABLE(hashtag_table_id),
        CONSTRAINT fk_tick5 FOREIGN KEY (tick_done_id) REFERENCES TICK_DONE(tick_done_id)
        );
     */
    
    int tick_id;
    int owner_id;
    int place_id;
    int category_id;
    int note_id;
    int hashtag_table_id;
    int tick_done_id;
    String tick_done_start;
    String tick_done_end;
    String tick_name;
    
    
    // main constructor
    Tick_Tick(){
        super("Tick_Tick");
        tick_id = -1;
        owner_id = -1;
        place_id = 1;
        category_id = 1;
        note_id = 1;
        hashtag_table_id = 1;
        tick_done_id = 1;
        tick_done_start = "";
        tick_done_end = "";
        tick_name = "";
        super.put_elements(wall_updater());
    }
    
    // constructor with tick brick functionality
    Tick_Tick(ArrayList<Tick_Brick> to_add){
        super("Tick_Tick");
        tick_id = to_add.get(0).i_get();
        owner_id = to_add.get(1).i_get();
        place_id = to_add.get(2).i_get();
        category_id = to_add.get(3).i_get();
        note_id = to_add.get(4).i_get();
        hashtag_table_id = to_add.get(5).i_get();
        tick_done_id = to_add.get(6).i_get();
        tick_done_start = to_add.get(7).s_get();
        tick_done_end = to_add.get(8).s_get();
        super.put_elements(wall_updater());
    }
    
    /**
     * Tick_Tick.wall_updater()
     * @return ArrayList
     * Function for updating 'the wall'
     */
    ArrayList<Tick_Brick> wall_updater(){
        ArrayList<Tick_Brick> to_ret = new ArrayList<>();
        
        to_ret.add(new Tick_Brick(tick_id));
        to_ret.add(new Tick_Brick(owner_id));
        to_ret.add(new Tick_Brick(place_id));
        to_ret.add(new Tick_Brick(category_id));
        to_ret.add(new Tick_Brick(note_id));
        to_ret.add(new Tick_Brick(hashtag_table_id));
        to_ret.add(new Tick_Brick(tick_done_id));
        to_ret.add(new Tick_Brick(tick_done_start));
        to_ret.add(new Tick_Brick(tick_done_end));
        to_ret.add(new Tick_Brick(tick_name));
        return to_ret;
    }
    
    void init_CUI(){
        super.inter.interface_print("Welcome in the Tick Creator");
        super.inter.interface_print("Tick name: ");
        tick_name = super.inter.interface_get();
        tick_done_start = new Date().toString();
        super.put_elements(wall_updater());
    }
    
    String simple_show(){
        return Integer.toString(tick_id)+ ": "+tick_name+" ("+tick_done_start+")";
    }
    /**
     * Tick_Tick.get_lines_to_show()
     * @return ArrayList
     * Function returns lines of info from object
     */
    ArrayList<String> get_lines_to_show(){
        ArrayList<String> to_ret = new ArrayList<>();
        to_ret.add(simple_show());
        return to_ret;
    }
}