/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jakubwawak
 */
public class GUI_addtick_window extends javax.swing.JDialog {

    /**
     * Creates new form GUI_addtick_window
     */
    Database database;
    Tick_Tick to_add;
    
    public GUI_addtick_window(java.awt.Frame parent, boolean modal,Database database) {
        super(parent, modal);
        this.database = database;
        to_add = new Tick_Tick();
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textfield_tickdata = new javax.swing.JTextField();
        button_add = new javax.swing.JButton();
        button_moreoptions = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        textfield_tickdata.setText("Name your task here");
        textfield_tickdata.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textfield_tickdataFocusGained(evt);
            }
        });

        button_add.setText("Add");
        button_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addActionPerformed(evt);
            }
        });

        button_moreoptions.setText("More options..");
        button_moreoptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_moreoptionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textfield_tickdata)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(button_add, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(button_moreoptions)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(textfield_tickdata, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_add)
                    .addComponent(button_moreoptions))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addActionPerformed
        if(!textfield_tickdata.getText().equals("") || !textfield_tickdata.getText().equals("Name your task here") ){
            Database_Tick dt = new Database_Tick(database);
            to_add.owner_id = database.logged.owner_id;
            to_add.tick_name = textfield_tickdata.getText();
            to_add.tick_done_start = new Date().toString();
            to_add.wall_updater();
            
            try {
                if ( dt.add_tick(to_add) ){
                    textfield_tickdata.setText("Tick added");
                    textfield_tickdata.setEditable(false);
                    button_add.setEnabled(false);
                    button_moreoptions.setEnabled(false);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GUI_addtick_window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            textfield_tickdata.setText("Wrong input data, try again");
        }
    }//GEN-LAST:event_button_addActionPerformed

    private void textfield_tickdataFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textfield_tickdataFocusGained
        textfield_tickdata.setText("");
    }//GEN-LAST:event_textfield_tickdataFocusGained

    private void button_moreoptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_moreoptionsActionPerformed
        if (!textfield_tickdata.getText().equals("") || !textfield_tickdata.getText().equals("Name your task here") ){
            Database_Tick dt = new Database_Tick(database);
            to_add.owner_id = database.logged.owner_id;
            to_add.tick_name = textfield_tickdata.getText();
            to_add.tick_done_start = new Date().toString();
            to_add.wall_updater();
            try {
                new GUI_moreoptions_addtick_window(this,true,to_add,database);
            } catch (SQLException ex) {
                Logger.getLogger(GUI_addtick_window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            textfield_tickdata.setText("Name tick first");
        }
           
    }//GEN-LAST:event_button_moreoptionsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_add;
    private javax.swing.JButton button_moreoptions;
    private javax.swing.JTextField textfield_tickdata;
    // End of variables declaration//GEN-END:variables
}
