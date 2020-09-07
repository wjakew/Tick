/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jakubwawak
 */
public class GUI_markdone_window extends javax.swing.JDialog {

    /**
     * Creates new form GUI_markdone_window
     */
    Database database;
    int choosen_tick;
    
    
    public GUI_markdone_window(java.awt.Frame parent, boolean modal,Database database,int tick_id) {
        super(parent, modal);
        this.database = database;
        choosen_tick = tick_id;
        initComponents();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        textarea_notes = new javax.swing.JTextArea();
        button_markdone = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        textarea_notes.setColumns(20);
        textarea_notes.setRows(5);
        jScrollPane1.setViewportView(textarea_notes);

        button_markdone.setText("Mark done");
        button_markdone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_markdoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                    .addComponent(button_markdone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_markdone)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_markdoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_markdoneActionPerformed
        Database_Tick dt = new Database_Tick(database);
        try {
            if (dt.mark_done(textarea_notes.getText(), choosen_tick)){
                button_markdone.setText("Marked done");
                button_markdone.setEnabled(false);
                textarea_notes.setEditable(false);
            }
            else{
                button_markdone.setText("Failed to mark done");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUI_markdone_window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_button_markdoneActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_markdone;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textarea_notes;
    // End of variables declaration//GEN-END:variables
}
