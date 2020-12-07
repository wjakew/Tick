/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

/**
 *
 * @author jakubwawak
 */
public class GUI_addaddress_window extends javax.swing.JDialog {

    /**
     * Creates new form GUI_addaddress_window
     */
    Database database;
    int program;
    
    /**
     * Constructor for the window
     * @param parent
     * @param modal
     * @param database
     * @param program 
     */
    public GUI_addaddress_window(java.awt.Frame parent, boolean modal,Database database,int program) throws SQLException, SQLException {
        super(parent, modal);
        this.database = database;
        this.program = program;
        initComponents();
        load_components();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    
    /**
     * Function for loading components
     */
    void load_components() throws SQLException{
        if ( program != 1){
            String query = "SELECT * from ADDRESS where address_id = "+program+";";
            Tick_Address ta = new Tick_Address(database.return_resultset(query));
            
            field_streetname.setText(ta.address_street + " " + Integer.toString(ta.address_house_number)+" "+Integer.toString(ta.address_flat_number));
            field_city.setText(ta.address_city);
            field_country.setText(ta.address_country);
            field_postal.setText(ta.address_postal);
        }
    }
    /**
     * Function for checking one field
     * @param to_check
     * @return boolean
     */
    boolean check_field(JTextField to_check){
        return to_check.getText().equals("");
    }
    
    /**
     * Function for checking fields in object
     */
    boolean check_fields(){
        ArrayList<JTextField> fields = new ArrayList();
        fields.add(field_city);
        fields.add(field_country);
        fields.add(field_postal);
        fields.add(field_streetname);

        for(JTextField field : fields){
            if ( check_field(field) ){
                return false;
            }
        }
        
        return field_streetname.getText().contains("/");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        field_streetname = new javax.swing.JTextField();
        field_postal = new javax.swing.JTextField();
        field_city = new javax.swing.JTextField();
        field_country = new javax.swing.JTextField();
        button_address = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        field_streetname.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        field_streetname.setText("Street name/number");
        field_streetname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field_streetnameFocusGained(evt);
            }
        });

        field_postal.setText("Postal code");

        field_city.setText("City name");

        field_country.setText("Country");

        button_address.setText("Add address");
        button_address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addressActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(field_streetname)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(field_postal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(field_city))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(button_address, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(field_country, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(field_streetname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(field_postal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(field_city, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(field_country)
                    .addComponent(button_address, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_addressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addressActionPerformed
        if ( check_fields() ){
            Tick_Address ta = new Tick_Address();
            ta.address_city = field_city.getText();
            ta.address_country = field_country.getText();
            ta.address_flat_number = 0;
            if ( field_streetname.getText().split("/").length == 1 || field_streetname.getText().split("/").length == 0){
                ta.address_house_number = 0;
                ta.address_street = field_streetname.getText();
            }
            else{
                ta.address_house_number = Integer.parseInt(field_streetname.getText().split("/")[1]);
                ta.address_street = field_streetname.getText().split("/")[0];
            }
            ta.address_postal = field_postal.getText();
            ta.wall_updater();
            
            try {
                if ( database.add_address(ta) ){
                    button_address.setText("Done");
                    field_city.setEnabled(false);
                    field_country.setEnabled(false);
                    field_postal.setEnabled(false);
                    field_streetname.setEnabled(false);      
                }
                else{
                    button_address.setText("Failed");
                }
            } catch (SQLException ex) {
                Logger.getLogger(GUI_addaddress_window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            button_address.setText("Wrong data");
        }
    }//GEN-LAST:event_button_addressActionPerformed

    private void field_streetnameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_field_streetnameFocusGained
         button_address.setText("Add address");
    }//GEN-LAST:event_field_streetnameFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_address;
    private javax.swing.JTextField field_city;
    private javax.swing.JTextField field_country;
    private javax.swing.JTextField field_postal;
    private javax.swing.JTextField field_streetname;
    // End of variables declaration//GEN-END:variables
}
