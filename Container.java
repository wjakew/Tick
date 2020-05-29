/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;

/**
 *Container for storing serialized data from database
 * @author jakub
 */
public class Container {
    
    ArrayList<Tick_Brick> wall;
    String mode;
    
    
    Container(ArrayList<Tick_Brick> list_of_objects,String mode){
        wall = list_of_objects;
        this.mode = mode;
    }
    /**
     * Container.copy_list(ArrayList<String> src,ArrayList<String> to_copy)
     * @param src
     * @param to_copy 
     */
    void copy_list(ArrayList<String> src,ArrayList<String> to_copy){
        for( String line: to_copy){
            src.add(line);
        }
    }
    
    /**
     * Container.make_lines()
     * @return ArrayList<String>
     * Return lines
     */
    ArrayList<String> make_lines(){
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Tick_Brick> part = new ArrayList<>();
        lines.add("Database_Viewer (powered by Container)");
        if (this.mode.equals("address")){
            /**
             *  address_id INT AUTO_INCREMENT PRIMARY KEY,
                address_city VARCHAR(30),
                address_street VARCHAR (30),
                address_house_number INT,
                address_flat_number INT,
                address_postal VARCHAR(15),
                address_country VARCHAR(30)
             */
            for (Tick_Brick element : wall){    // looping on Tick_Brick objects
                if ( !element.STOP ){
                    part.add(element);
                }
                else{
                    Tick_Address to_add = new Tick_Address(part);
                    copy_list(lines,to_add.get_lines_to_show());
                    part.clear();
                }
            }
            
            
        }
        else if (this.mode.equals("category")){
            /**
             *  category_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                category_name VARCHAR(45),
                category_note VARCHAR(100),
             */
            for (Tick_Brick element : wall){    // looping on Tick_Brick objects
                if ( !element.STOP ){
                    part.add(element);
                }
                else{
                    Tick_Category to_add = new Tick_Category(part);
                    copy_list(lines,to_add.get_lines_to_show());
                    part.clear();
                }
            }
            
            
        }
        else if (this.mode.equals("hashtag table")){
            /**
             *  hashtag_table_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                hashtag_table_name VARCHAR(45),
                hashtag_table_note VARCHAR(100),
                * 
             */
            for (Tick_Brick element : wall){    // looping on Tick_Brick objects
                if ( !element.STOP ){
                    part.add(element);
                }
                else{
                    Tick_HashtagT to_add = new Tick_HashtagT (part);
                    copy_list(lines,to_add.get_lines_to_show());
                    part.clear();
                }
            }
            
        }
        else if (this.mode.equals("note")){
            /**
             *  note_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                note_content VARCHAR(100),
                setting1 VARCHAR(40),
                setting2 VARCHAR(40),
                setting3 VARCHAR(40),
             */
            for (Tick_Brick element : wall){    // looping on Tick_Brick objects
                if ( !element.STOP ){
                    part.add(element);
                }
                else{
                    Tick_Note to_add = new Tick_Note(part);
                    copy_list(lines,to_add.get_lines_to_show());
                    part.clear();
                }
            }
            
        }
        else if (this.mode.equals("place")){
            /**
             *  place_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                place_name VARCHAR(30),
                address_id INT,
             */
            for (Tick_Brick element : wall){    // looping on Tick_Brick objects
                if ( !element.STOP ){
                    part.add(element);
                }
                else{
                    Tick_Place to_add = new Tick_Place(part);
                    copy_list(lines,to_add.get_lines_to_show());
                    part.clear();
                }
            }
            
        }
        else if (mode.equals("tag")){
            /**
             *  tag_id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT,
                hashtag_table_id INT,
                tag_name VARCHAR(45),
                tag_note VARCHAR(100),
             */
            for (Tick_Brick element : wall){    // looping on Tick_Brick objects
                if ( !element.STOP ){
                    part.add(element);
                }
                else{
                    Tick_Tag to_add = new Tick_Tag(part);
                    copy_list(lines,to_add.get_lines_to_show());
                    part.clear();
                }
            }
        }
        return lines;
    }
    
    
    
}
