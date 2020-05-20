/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *Object for storing user info
 * @author jakub
 */
public class Tick_User extends Tick_Element{
    /**
     * CREATE TABLE OWN
        (
        owner_id INT AUTO_INCREMENT PRIMARY KEY,
        owner_login VARCHAR(30),
        address_id INT,
        owner_password VARCHAR(72),
        owner_name VARCHAR(70),
        owner_surname VARCHAR(70),
        owner_email_address VARCHAR(60),
        owner_age INT,
        owner_status INT,
        CONSTRAINT fk_own FOREIGN KEY (address_id) REFERENCES ADDRESS(address_id)
        );
     */
    
    int owner_id;                           // id from the database
    int address_id;                         // id of the address
    String owner_login;                     // owner login from database
    String owner_name;                      // owner name
    private String owner_surname;           // --/--
    String owner_email_address;
    int owner_age;
    int owner_status;
    
    Tick_User(){
        super("Tick_User");
        owner_id = 0;                           // id from the database
        address_id = 0;                         // id of the address
        owner_login = "";                     // owner login from database
        owner_name = "";                      // owner name
        owner_surname = "";           // --/--
        owner_email_address = "";
        owner_age = 0;
        owner_status = 0;
        super.put_elements(wall_updater());
    }
    
    ArrayList<Tick_Brick> wall_updater(){
          ArrayList<Tick_Brick> to_ret = new ArrayList<>();
          to_ret.add(new Tick_Brick(owner_id));
          to_ret.add(new Tick_Brick(address_id));
          to_ret.add(new Tick_Brick(owner_login));
          to_ret.add(new Tick_Brick(owner_name));
          to_ret.add(new Tick_Brick(owner_surname));
          to_ret.add(new Tick_Brick(owner_email_address));
          to_ret.add(new Tick_Brick(owner_age));
          to_ret.add(new Tick_Brick(owner_status));
          return to_ret;
        }
    
    /**
     * Tick_User.init_CUI()
     * Inits input to 
     */
    void init_CUI(){
        
        System.out.println("Welcome in the Tick User interface!");
        owner_id = -1;
        
        
        
    }
    
    void show(){
        System.out.println("Tick User info: ");
        System.out.println("    Login: "+owner_login);
        System.out.println("    unique id: "+Integer.toString(owner_id));
        System.out.println("    Name: "+owner_name+" Surname: "+owner_surname);
        System.out.println("    E-mail address: "+owner_email_address);
        System.out.println("    Age: "+Integer.toString(owner_age) + "Status: "+Integer.toString(owner_status));
    }

}
