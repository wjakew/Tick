/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jakubwawak
 */
public class GUI_share_window extends javax.swing.JDialog {

    /**
     * Creates new form GUI_share_window
     */
    Database database;
    int tick_id;
    public GUI_share_window(java.awt.Frame parent, boolean modal,Database database,int tick_id) throws SQLException {
        super(parent, modal);
        this.tick_id = tick_id;
        this.database = database;
        initComponents();
        setLocationRelativeTo(null);
        
        load_components();
        
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
        textarea_tick_details = new javax.swing.JTextArea();
        textfield_user_login = new javax.swing.JTextField();
        button_share = new javax.swing.JButton();
        label_confirm_data = new javax.swing.JLabel();
        label_readytoshare = new javax.swing.JLabel();
        button_confirm = new javax.swing.JButton();
        button_history = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        textarea_tick_details.setColumns(20);
        textarea_tick_details.setRows(5);
        jScrollPane1.setViewportView(textarea_tick_details);

        textfield_user_login.setText("jTextField1");

        button_share.setText("Share");
        button_share.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_shareActionPerformed(evt);
            }
        });

        label_confirm_data.setText("jLabel1");

        label_readytoshare.setText("Ready to share");

        button_confirm.setText("Confirm");

        button_history.setText("History");
        button_history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_historyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(textfield_user_login)
                            .addComponent(button_share, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label_confirm_data)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(label_readytoshare)
                        .addGap(0, 144, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(button_confirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_history)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button_history)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textfield_user_login, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_share)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label_confirm_data)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label_readytoshare)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_confirm, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_shareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_shareActionPerformed
        if(!textfield_user_login.getText().equals("User login") || !textfield_user_login.getText().equals("")){
            
        }
        else{
            textfield_user_login.setText("Wrong user login");
        }
    }//GEN-LAST:event_button_shareActionPerformed

    private void button_historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_historyActionPerformed

    }//GEN-LAST:event_button_historyActionPerformed
    
    /**
     * Function for loading components
     */
    void load_components() throws SQLException{
        String filler = "";
        Tick_Tick tt = new Tick_Tick(database.return_TB_collection(database.logged, "tick", tick_id));
        for(String line : tt.get_detailed_data(database)){
            filler = filler + line + "\n";
        }
        textarea_tick_details.setText(filler);
        textfield_user_login.setText("User login");
        
        // setting initial window view
        label_readytoshare.setVisible(false);
        label_confirm_data.setVisible(false);
        button_confirm.setVisible(false);
        button_history.setText("History");
        
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_confirm;
    private javax.swing.JButton button_history;
    private javax.swing.JButton button_share;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_confirm_data;
    private javax.swing.JLabel label_readytoshare;
    private javax.swing.JTextArea textarea_tick_details;
    private javax.swing.JTextField textfield_user_login;
    // End of variables declaration//GEN-END:variables
}