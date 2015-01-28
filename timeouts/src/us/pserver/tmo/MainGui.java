/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.tmo;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import murlen.util.fscript.FSException;
import us.pserver.coder.ui.FrameEditor;
import us.pserver.jcs.BufferStream;
import us.pserver.log.SimpleLog;


/**
 *
 * @author juno
 */
public class MainGui extends javax.swing.JFrame {

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
    jlist.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() >= 2) {
          editSchedule();
        }
      }
    });
    
    this.setIconImage(new ImageIcon(
        getClass().getResource(
        "/us/pserver/tmo/imgs/hourglass-icon-32.png"))
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
        .setPackageOverIcon("view_white-20.png")
        .setClickAction(()->viewScript());
    newScriptButton.setPackageIcon("doc-green-20.png")
        .setPackageOverIcon("doc-white-20.png")
        .setClickAction(()->newScript());
  }
  
  
  private void reloadList() {
    DefaultListModel model = 
        new DefaultListModel();
    if(!tmo.list().isEmpty()) {
      tmo.update();
      tmo.list().forEach(model::addElement);
    }
    jlist.setModel(model);
    jlist.repaint();
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
    log.info("Schedule Added");
  }
  
  
  private boolean checkSelected() {
    if(tmo.list().isEmpty()
        || selected < 0 || selected
        > tmo.list().size()) {
      log.error("No Schedule Selected");
      return false;
    }
    return true;
  }
  
  
  private void editSchedule() {
    if(!checkSelected()) return;
    ScriptPair p = tmo.get(selected);
    NewJobDialog jd = new NewJobDialog();
    p = jd.showDialog(p, this);
    if(p == null) {
      log.error("Edit Canceled");
      return;
    }
    tmo.edit(selected, p);
    this.reloadList();
    log.info("Schedule Edited");
  }
  
  
  private void removeSchedule() {
    if(!checkSelected()) return;
    tmo.remove(selected);
    this.reloadList();
    log.info("Schedule Removed");
  }
  
  
  private void runScript() {
    if(!checkSelected()) return;
    int opt = JOptionPane.showConfirmDialog(
        this, 
        "Are you sure do you want to RUN this script NOW?", 
        "Run Script", 
        JOptionPane.YES_NO_CANCEL_OPTION);
    if(opt != JOptionPane.YES_OPTION) {
      log.info("Script run canceled!");
      return;
    }
    log.info("Running Script...");
    ScriptPair p = tmo.get(selected);
    try {
      tmo.getScriptExecutor().exec(
          p.job().getScriptPath().toString());
    } catch(IOException | FSException e) {
      log.error(e.toString());
    } finally {
      log.info("Execution Finished");
    }
  }
  
  
  private void viewScript() {
    this.checkSelected();
    FrameEditor fe = new FrameEditor();
    Point p = this.getLocationOnScreen();
    fe.setLocation(p.x + this.getWidth() + 2, p.y);
    ScriptPair sp = tmo.get(selected);
    fe.read(sp.job().getScriptPath().toFile());
    fe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    fe.setVisible(true);
  }
  
  
  public void newScript() {
    FrameEditor fe = new FrameEditor();
    Point p = this.getLocationOnScreen();
    fe.setLocation(p.x + this.getWidth() + 2, p.y);
    fe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    fe.setVisible(true);
  }
  
  
  public void stop() {
    tmo.stop();
    tmo.save();
  }
  
  
  public void start() {
    tmo.start();
    reloadList();
  }


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
    newScriptButton = new us.pserver.tmo.LabelButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    jScrollPane2 = new javax.swing.JScrollPane();
    console = new us.pserver.jcs.JConsole();
    jScrollPane1 = new javax.swing.JScrollPane();
    jlist = new javax.swing.JList();
    jMenuBar1 = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    newScheduleMenu = new javax.swing.JMenuItem();
    newScriptMenu = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    quitMenu = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    editScheduleMenu = new javax.swing.JMenuItem();
    deleteMenu = new javax.swing.JMenuItem();
    runMenu = new javax.swing.JMenuItem();
    viewMenu = new javax.swing.JMenuItem();
    clearMenu = new javax.swing.JMenuItem();

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
    runLButton.setToolTipText("Run Schedule");

    viewLButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/tmo/imgs/view-20.png"))); // NOI18N
    viewLButton.setToolTipText("View Script");

    newScriptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/tmo/imgs/doc-green-20.png"))); // NOI18N
    newScriptButton.setToolTipText("New Script");
    newScriptButton.setPreferredSize(new java.awt.Dimension(24, 30));

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addComponent(newLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(newScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(removeLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(editLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(runLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(viewLButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
        .addComponent(jclock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(newScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    fileMenu.setText("File");

    newScheduleMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
    newScheduleMenu.setText("New Schedule");
    newScheduleMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newScheduleMenuActionPerformed(evt);
      }
    });
    fileMenu.add(newScheduleMenu);

    newScriptMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    newScriptMenu.setText("New Script");
    newScriptMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newScriptMenuActionPerformed(evt);
      }
    });
    fileMenu.add(newScriptMenu);
    fileMenu.add(jSeparator1);

    quitMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
    quitMenu.setText("Quit");
    quitMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        quitMenuActionPerformed(evt);
      }
    });
    fileMenu.add(quitMenu);

    jMenuBar1.add(fileMenu);

    editMenu.setText("Edit");

    editScheduleMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
    editScheduleMenu.setText("Edit Schedule");
    editScheduleMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editScheduleMenuActionPerformed(evt);
      }
    });
    editMenu.add(editScheduleMenu);

    deleteMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
    deleteMenu.setText("Delete Schedule");
    deleteMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteMenuActionPerformed(evt);
      }
    });
    editMenu.add(deleteMenu);

    runMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
    runMenu.setText("Run Schedule");
    runMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runMenuActionPerformed(evt);
      }
    });
    editMenu.add(runMenu);

    viewMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
    viewMenu.setText("View Script");
    viewMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        viewMenuActionPerformed(evt);
      }
    });
    editMenu.add(viewMenu);

    clearMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
    clearMenu.setText("Clear Console");
    clearMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clearMenuActionPerformed(evt);
      }
    });
    editMenu.add(clearMenu);

    jMenuBar1.add(editMenu);

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

  private void deleteMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuActionPerformed
    removeSchedule();
  }//GEN-LAST:event_deleteMenuActionPerformed

  private void quitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuActionPerformed
    this.stop();
    System.exit(0);
  }//GEN-LAST:event_quitMenuActionPerformed

  private void newScheduleMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newScheduleMenuActionPerformed
    newSchedule();
  }//GEN-LAST:event_newScheduleMenuActionPerformed

  private void newScriptMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newScriptMenuActionPerformed
    newScript();
  }//GEN-LAST:event_newScriptMenuActionPerformed

  private void editScheduleMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editScheduleMenuActionPerformed
    editSchedule();
  }//GEN-LAST:event_editScheduleMenuActionPerformed

  private void runMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runMenuActionPerformed
    runScript();
  }//GEN-LAST:event_runMenuActionPerformed

  private void viewMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewMenuActionPerformed
    viewScript();
  }//GEN-LAST:event_viewMenuActionPerformed

  private void clearMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearMenuActionPerformed
    console.setText("");
    console.repaint();
  }//GEN-LAST:event_clearMenuActionPerformed


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
  private javax.swing.JMenuItem clearMenu;
  private us.pserver.jcs.JConsole console;
  private javax.swing.JMenuItem deleteMenu;
  private us.pserver.tmo.LabelButton editLButton;
  private javax.swing.JMenu editMenu;
  private javax.swing.JMenuItem editScheduleMenu;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JSplitPane jSplitPane1;
  private us.pserver.tmo.JClock jclock;
  private javax.swing.JList jlist;
  private us.pserver.tmo.LabelButton newLButton;
  private javax.swing.JMenuItem newScheduleMenu;
  private us.pserver.tmo.LabelButton newScriptButton;
  private javax.swing.JMenuItem newScriptMenu;
  private javax.swing.JMenuItem quitMenu;
  private us.pserver.tmo.LabelButton removeLButton;
  private us.pserver.tmo.LabelButton runLButton;
  private javax.swing.JMenuItem runMenu;
  private us.pserver.tmo.LabelButton viewLButton;
  private javax.swing.JMenuItem viewMenu;
  // End of variables declaration//GEN-END:variables
}
