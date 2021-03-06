/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TransparentInterfaceBuilder.java
 *
 * Created on 09/01/2010, 00:14:44
 */

package com.power.visual;

/**
 *
 * @author f6036477
 */
public class TransparentInterfaceBuilder extends javax.swing.JFrame {

    /** Creates new form TransparentInterfaceBuilder */
    public TransparentInterfaceBuilder() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jDesktopPane1 = new javax.swing.JDesktopPane();
    backgroundPanel = new javax.swing.JPanel();
    componentFrame = new javax.swing.JInternalFrame();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jMenu2 = new javax.swing.JMenu();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    backgroundPanel.setFocusable(false);

    componentFrame.setIconifiable(true);
    componentFrame.setMaximizable(true);
    componentFrame.setResizable(true);
    componentFrame.setTitle("Paleta de Componentes");
    componentFrame.setVisible(true);

    javax.swing.GroupLayout componentFrameLayout = new javax.swing.GroupLayout(componentFrame.getContentPane());
    componentFrame.getContentPane().setLayout(componentFrameLayout);
    componentFrameLayout.setHorizontalGroup(
      componentFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 209, Short.MAX_VALUE)
    );
    componentFrameLayout.setVerticalGroup(
      componentFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 543, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
    backgroundPanel.setLayout(backgroundPanelLayout);
    backgroundPanelLayout.setHorizontalGroup(
      backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(backgroundPanelLayout.createSequentialGroup()
        .addContainerGap(545, Short.MAX_VALUE)
        .addComponent(componentFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    backgroundPanelLayout.setVerticalGroup(
      backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(componentFrame)
    );

    backgroundPanel.setBounds(0, 0, 770, 570);
    jDesktopPane1.add(backgroundPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

    jMenu1.setText("Arquivo");
    jMenuBar1.add(jMenu1);

    jMenu2.setText("Editar");
    jMenuBar1.add(jMenu2);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransparentInterfaceBuilder().setVisible(true);
            }
        });
    }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel backgroundPanel;
  private javax.swing.JInternalFrame componentFrame;
  private javax.swing.JDesktopPane jDesktopPane1;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenuBar jMenuBar1;
  // End of variables declaration//GEN-END:variables

}
