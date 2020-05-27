/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.util.ArrayList;

/**
 *Object for storing addresses
 * @author jakub
 */
public class Tick_Address extends Tick_Element{
    /**
     * CREATE TABLE ADDRESS
        (
        address_id INT AUTO_INCREMENT PRIMARY KEY,
        address_city VARCHAR(30),
        address_street VARCHAR (30),
        address_house_number INT,
        address_flat_number INT,
        address_postal VARCHAR(15),
        address_country VARCHAR(30)
        );
     */
    int address_id;
    String address_city;
    String address_street;
    int address_house_number;
    int address_flat_number;
    String address_postal;
    String address_country;
 
    // main constructor
    Tick_Address(){
        super("Tick_Address");
        address_id = -1;
        address_city = "";
        address_street = "";
        address_house_number = -1;
        address_flat_number = -1;
        address_postal = "";
        address_country = "";
        super.put_elements(wall_updater());
    }
    // constructor with one given argument
    Tick_Address(ArrayList<Tick_Brick> to_add){
        super("Tick_Address");
        address_id = to_add.get(0).i_get();
        address_city = to_add.get(1).s_get();
        address_street = to_add.get(2).s_get();
        address_house_number = to_add.get(3).i_get();
        address_flat_number = to_add.get(4).i_get();
        address_postal = to_add.get(5).s_get();
        address_country = to_add.get(6).s_get();
        super.put_elements(wall_updater());
    }
    
    /**
     * Tick_Address.wall_updater()
     * @return ArrayList
     * Returns collection of Tick_Brick object
     */
    ArrayList<Tick_Brick> wall_updater(){
        ArrayList<Tick_Brick> to_ret = new ArrayList<>();
        to_ret.add(new Tick_Brick(address_id));
        to_ret.add(new Tick_Brick(address_city));
        to_ret.add(new Tick_Brick(address_street));
        to_ret.add(new Tick_Brick(address_house_number));
        to_ret.add(new Tick_Brick(address_flat_number));
        to_ret.add(new Tick_Brick(address_postal));
        to_ret.add(new Tick_Brick(address_country));
        return to_ret;
    }
    
    void init_CUI(){
        super.inter.interface_print("Adding new address:");
        super.inter.interface_print("City name:");
        address_city = super.inter.interface_get();
        super.inter.interface_print("Street name:");
        address_street = super.inter.interface_get();
        super.inter.interface_print("House number:");
        address_house_number = Integer.parseInt(super.inter.interface_get());
        super.inter.interface_print("Flat number:");
        address_flat_number = Integer.parseInt(super.inter.interface_get());
        super.inter.interface_print("Postal code:");
        address_postal = super.inter.interface_get();
        super.inter.interface_print("Country:");
        address_country = super.inter.interface_get();
        super.put_elements(wall_updater());
    }
}
