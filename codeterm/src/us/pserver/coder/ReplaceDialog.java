/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.coder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;


/**
 *
 * @author juno
 */
public class ReplaceDialog extends javax.swing.JDialog {

  private Editor editor;
  
  private int pos;
  
  /**
   * Creates new form ReplaceDialog
   */
  public ReplaceDialog(java.awt.Frame parent, Editor edit) {
    super(parent, false);
    pos = -1;
    editor = edit;
    KeyListener kl = new KeyAdapter() {
      @Override public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
          if(e.getSource() == findInput 
              || e.getSource() == findButton)
            find();
          else if(e.getSource() == replaceInput
              || e.getSource() == replaceButton) {
            replace();
            find();
          }
          else if(e.getSource() == repAllButton)
            replaceAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
          find();
        }
        else if(e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()) {
          replace();
          find();
        }
        else if(e.getKeyCode() == KeyEvent.VK_R 
            && e.isControlDown() && e.isShiftDown()) {
          replaceAll();
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          ReplaceDialog.this.setVisible(false);
          editor.requestFocus();
        }
      }
    };
    initComponents();
    findInput.addKeyListener(kl);
    replaceInput.addKeyListener(kl);
    findButton.addKeyListener(kl);
    replaceButton.addKeyListener(kl);
    repAllButton.addKeyListener(kl);
  }
  
  
  public void find() {
    String sf = findInput.getText();
    if(sf == null || sf.isEmpty()) return;
    int len = editor.getDocument().getLength();
    if(pos >= len || pos < 0) pos = editor.getCaretPosition();
    pos = editor.find(sf, pos);
    System.out.println("find="+ pos);
    if(pos < 0) {
      editor.setSelectionStart(0);
      editor.setSelectionEnd(0);
    }
    else {
      editor.setSelectionStart(pos);
      pos += sf.length();
      editor.setSelectionEnd(pos);
    }
  }
  
  
  public void replace() {
    int ss = editor.getSelectionStart();
    int se = editor.getSelectionEnd();
    if(ss <= 0 && (se < 0 || se == ss)
        || replaceInput.getText() == null)
      return;
    editor.replace(ss, se - ss, replaceInput.getText());
  }
  
  
  public void replaceAll() {
    while(pos >= 0) {
      replace();
      find();
    }
    System.out.println("* finish replaceAll");
  }


  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    findLabel = new javax.swing.JLabel();
    findInput = new javax.swing.JTextField();
    replaceLabel = new javax.swing.JLabel();
    replaceInput = new javax.swing.JTextField();
    jPanel1 = new javax.swing.JPanel();
    findButton = new javax.swing.JButton();
    replaceButton = new javax.swing.JButton();
    repAllButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Find/Replace");
    setIconImage(IconGetter.getIconSearchGray());

    findLabel.setText("Find");

    replaceLabel.setText("Replace");

    findButton.setText("Find");
    findButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        findButtonActionPerformed(evt);
      }
    });

    replaceButton.setText("Replace");
    replaceButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        replaceButtonActionPerformed(evt);
      }
    });

    repAllButton.setText("Replace All");
    repAllButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        repAllButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(findButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(replaceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(repAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(findButton)
          .addComponent(replaceButton)
          .addComponent(repAllButton))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(replaceLabel)
          .addComponent(findLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(replaceInput)
          .addComponent(findInput))
        .addContainerGap())
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(17, 17, 17)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(findLabel)
          .addComponent(findInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(replaceLabel)
          .addComponent(replaceInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findButtonActionPerformed
    find();
  }//GEN-LAST:event_findButtonActionPerformed

  private void replaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceButtonActionPerformed
    replace();
    find();
  }//GEN-LAST:event_replaceButtonActionPerformed

  private void repAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repAllButtonActionPerformed
    replaceAll();
  }//GEN-LAST:event_repAllButtonActionPerformed


  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(ReplaceDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(ReplaceDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(ReplaceDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(ReplaceDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
        //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        ReplaceDialog dialog = new ReplaceDialog(new javax.swing.JFrame(), null);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosing(java.awt.event.WindowEvent e) {
            System.exit(0);
          }
        });
        dialog.setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton findButton;
  private javax.swing.JTextField findInput;
  private javax.swing.JLabel findLabel;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JButton repAllButton;
  private javax.swing.JButton replaceButton;
  private javax.swing.JTextField replaceInput;
  private javax.swing.JLabel replaceLabel;
  // End of variables declaration//GEN-END:variables
}
