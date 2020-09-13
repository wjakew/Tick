/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package tick;

/**
 *
 * @author jakubwawak
 */
public class GUI_datainput_universal_window extends javax.swing.JDialog {

    /**
     * Creates new form GUI_datainput_universal_window
     */
    public GUI_datainput_universal_window(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textfield_name = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        textfield_note = new javax.swing.JTextArea();
        button_optional1 = new javax.swing.JButton();
        button_add = new javax.swing.JButton();
        button_optional2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        textfield_name.setText("jTextField1");

        textfield_note.setColumns(20);
        textfield_note.setRows(5);
        jScrollPane1.setViewportView(textfield_note);

        button_optional1.setText("jButton1");

        button_add.setText("Add");

        button_optional2.setText("jButton1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textfield_name)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(button_optional1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_optional2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(button_add)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(textfield_name, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_optional1)
                    .addComponent(button_add)
                    .addComponent(button_optional2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_add;
    private javax.swing.JButton button_optional1;
    private javax.swing.JButton button_optional2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField textfield_name;
    private javax.swing.JTextArea textfield_note;
    // End of variables declaration//GEN-END:variables
}
