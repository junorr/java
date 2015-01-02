/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.tmo;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import murlen.util.fscript.FSException;
import us.pserver.jcs.BufferStream;
import us.pserver.log.SimpleLog;


/**
 *
 * @author juno
 */
public class MainGui extends javax.swing.JFrame
    implements WindowListener {

  private final Timeouts tmo;
  
  private final PrintStream output;
  
  private final SimpleLog log;
  
  private int selected;
  
  
  /**
   * Creates new form MainGui
   */
  public MainGui() {
    initComponents();
    output = new PrintStream(new BufferStream(console));
    System.setOut(output);
    System.setErr(output);
    
    jclock.start();
    tmo = new Timeouts();
    tmo.getScriptExecutor()
        .setDoneAction(()->reloadList());
    log = tmo.getLogger();
    tmo.getScriptExecutor()
        .getProcessor().setStdOut(output);
    
    jlist.addListSelectionListener(new ListSelectionListener() {
      @Override public void valueChanged(ListSelectionEvent e) {
        selected = e.getFirstIndex();
      }
    });
    
    this.setIconImage(new ImageIcon(
        getClass().getResource(
        "/us/pserver/tmo/imgs/timer-blue-32.png"))
        .getImage());
    newLButton.setPackageIcon("plus-20.png")
        .setPackageOverIcon("plus_white-20.png")
        .setClickAction(()->newSchedule());
    editLButton.setPackageIcon("edit-20.png")
        .setPackageOverIcon("edit_white-20.png")
        .setClickAction(()->editSchedule());
    removeLButton.setPackageIcon("minus-20.png")
        .setPackageOverIcon("minus_white-20.png")
        .setClickAction(()->removeSchedule());
    runLButton.setPackageIcon("gear-20.png")
        .setPackageOverIcon("gear_white-20.png")
        .setClickAction(()->runScript());
    viewLButton.setPackageIcon("view-20.png")
        .setPackageOverIcon("view_white-20.png");
  }
  
  
  private void reloadList() {
    DefaultListModel model = 
        new DefaultListModel();
    if(!tmo.getCron().jobs().isEmpty()) {
      tmo.getCron().jobs().forEach(model::addElement);
    }
    jlist.setModel(model);
    jlist.paint(jlist.getGraphics());
  }
  
  
  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    if(b) {
      tmo.start();
      reloadList();
    }
    else tmo.stop();
  }
  
  
  private void newSchedule() {
    NewJobDialog jd = new NewJobDialog();
    ScriptPair p = jd.showDialog(this);
    if(p == null) {
      log.error("Invalid Schedule");
      return;
    }
    tmo.add(p);
    this.reloadList();
    tmo.save();
    log.info("Schedule Added");
  }
  
  
  private boolean checkSelected() {
    if(tmo.getCron().jobs().isEmpty()
        || selected < 0 || selected
        > tmo.getCron().jobs().size()) {
      log.error("No Schedule Selected");
      return false;
    }
    return true;
  }
  
  
  private void editSchedule() {
    if(!checkSelected()) return;
    tmo.getCron().stop();
    ScriptPair p = new ScriptPair(
        tmo.getCron().jobs().get(selected));
    NewJobDialog jd = new NewJobDialog();
    p = jd.showDialog(p, this);
    if(p == null) {
      tmo.getCron().start();
      log.error("Edit Canceled");
      return;
    }
    tmo.getCron().jobs().set(selected, p);
    this.reloadList();
    tmo.save();
    tmo.getCron().start();
    log.info("Schedule Edited");
  }
  
  
  private void removeSchedule() {
    if(!checkSelected()) return;
    tmo.getCron().stop();
    tmo.getCron().jobs().remove(selected);
    this.reloadList();
    tmo.save();
    tmo.getCron().start();
    log.info("Schedule Removed");
  }
  
  
  private void runScript() {
    if(!checkSelected()) return;
    log.info("Running Script...");
    tmo.getCron().stop();
    ScriptPair p = new ScriptPair(
        tmo.getCron().jobs().get(selected));
    try {
      tmo.getScriptExecutor().exec(
          p.job().getScriptPath().toString());
    } catch(IOException | FSException e) {
      log.error(e.toString());
    } finally {
      tmo.getCron().start();
      log.info("Execution Finished");
    }
  }


  @Override
  public void windowClosing(WindowEvent e) {
    tmo.stop();
    tmo.save();
  }


  @Override public void windowOpened(WindowEvent e) {}
  @Override public void windowClosed(WindowEvent e) {}
  @Override public void windowIconified(WindowEvent e) {}
  @Override public void windowDeiconified(WindowEvent e) {}
  @Override public void windowActivated(WindowEvent e) {}
  @Override public void windowDeactivated(WindowEvent e) {}
  
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    jclock = new us.pserver.tmo.JClock();
    newLButton = new us.pserver.tmo.LabelButton();
    editLButton = new us.pserver.tmo.LabelButton();
    removeLButton = new us.pserver.tmo.LabelButton();
    runLButton = new us.pserver.tmo.LabelButton();
    viewLButton = new us.pserver.tmo.LabelButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    jScrollPane2 = new javax.swing.JScrollPane();
    console = new us.pserver.jcs.JConsole();
    jScrollPane1 = new javax.swing.JScrollPane();
    jlist = new javax.swing.JList();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jMenu2 = new javax.swing.JMenu();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("TimeoutS");

    jPanel1.setBackground(new java.awt.Color(70, 80, 80));

    newLButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/tmo/imgs/plus-20.png"))); // NOI18N
    newLButton.setToolTipText("New Schedule");
    newLButton.setPreferredSize(new java.awt.Dimension(24, 30));

    editLButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/tmo/imgs/edit-20.png"))); // NOI18N
    editLButton.setToolTipText("Edit Schedule");

    removeLButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/tmo/imgs/minus-20.png"))); // NOI18N
    removeLButton.setToolTipText("Remove Schedule");

    runLButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/tmo/imgs/gear-20.png"))); // NOI18N
    runLButton.setToolTipText("Run Script Job");

    viewLButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/tmo/imgs/view-20.png"))); // NOI18N
    viewLButton.setToolTipText("View Script");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addComponent(newLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(removeLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(editLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(runLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(viewLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
        .addComponent(jclock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jclock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(newLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(editLButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(removeLButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(runLButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(viewLButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(12, Short.MAX_VALUE))
    );

    jSplitPane1.setDividerLocation(175);
    jSplitPane1.setDividerSize(5);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    console.setColumns(20);
    console.setForeground(new java.awt.Color(180, 245, 180));
    console.setRows(5);
    jScrollPane2.setViewportView(console);

    jSplitPane1.setBottomComponent(jScrollPane2);

    jlist.setModel(new DefaultListModel());
    jScrollPane1.setViewportView(jlist);

    jSplitPane1.setLeftComponent(jScrollPane1);

    jMenu1.setText("File");
    jMenuBar1.add(jMenu1);

    jMenu2.setText("Edit");
    jMenuBar1.add(jMenu2);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jSplitPane1)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents


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
      java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
        //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MainGui().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private us.pserver.jcs.JConsole console;
  private us.pserver.tmo.LabelButton editLButton;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane jSplitPane1;
  private us.pserver.tmo.JClock jclock;
  private javax.swing.JList jlist;
  private us.pserver.tmo.LabelButton newLButton;
  private us.pserver.tmo.LabelButton removeLButton;
  private us.pserver.tmo.LabelButton runLButton;
  private us.pserver.tmo.LabelButton viewLButton;
  // End of variables declaration//GEN-END:variables
}
