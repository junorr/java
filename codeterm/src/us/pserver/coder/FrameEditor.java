/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.coder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.Timer;


/**
 *
 * @author juno
 */
public class FrameEditor extends javax.swing.JFrame {

  public static final Color
      DEF_STATUS_COLOR = Color.WHITE,
      STATUS_ERROR_COLOR = Color.YELLOW,
      DEF_EDITOR_BG = new Color(100, 100, 100),
      DEF_EDITOR_FG = new Color(200, 255, 210),
      DEF_SELECT_COLOR = new Color(170, 170, 170);
  
  public static final int 
      STATUS_HEIGHT = 18,
      STATUS_DELAY = 3500;
  
  public static final String
      ICON_OPEN = "/us/pserver/coder/images/open-gray-24.png",
      ICON_SAVE = "/us/pserver/coder/images/save-gray-24.png",
      ICON_TERM = "/us/pserver/coder/images/codeterm-24.png";
      
  
  private Editor editor;
  
  private int buttonBarHeight;
  
  private ReplaceDialog replace;
  
  private TextCopy copy;
  
  private Timer hideStatus;
  
  private File lastFile;
  
  private Color statusColor, statusWarnColor;
  
  private LineNumberPanel lnp;
  
  private CodetermConfig conf;
  
  
  /**
   * Creates new form FrameEditor
   */
  public FrameEditor() {
    editor = new Editor();
    lnp = new LineNumberPanel(editor);
    editor.setText("codeterm\ncodeterm\ncodeterm\ncodeterm\ncodeterm\ncodeterm\n");
    replace = new ReplaceDialog(this, editor);
    copy = new TextCopy();
    lastFile = null;
    conf = new CodetermConfig();
    
    initComponents();
    statusbar.setOpaque(true);

    this.setLocation(ScreenPositioner
        .getCenterScreenPoint(this));
    
    initConfig();
    
    this.setIconImage(new ImageIcon(
        getClass().getResource(ICON_TERM)).getImage());
    buttonBarHeight = buttonBar.getPreferredSize().height;
    scroll.setViewportView(lnp);
    scroll.repaint();
    editor.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_H && e.isControlDown()) {
          toggleButtonBar();
          e.consume();
        }
        else if(e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
          find();
          e.consume();
        }
        else if(e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
          copy();
          e.consume();
        }
        else if(e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
          paste();
          e.consume();
        }
      }
    });
    hideStatus = new Timer(STATUS_DELAY, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        statusbar.setPreferredSize(
            new Dimension(statusbar.getWidth(), 1));
        content.revalidate();
      }
    });
    hideStatus.setRepeats(false);
  }
  
  
  public CodetermConfig getConfig() {
    return conf;
  }
  
  
  private void initConfig() {
    if(conf.fileExists()) {
      Exception e = conf.load();
      if(e != null)
        throw new IllegalStateException(e.getMessage(), e);
      editor.setBackground(conf.getTextBgColor());
      Color c = conf.getTextColor();
      editor.setForeground(conf.getTextColor());
      editor.setFont(conf.getTextFont());
      editor.setSelectionColor(conf.getTextSelectionColor());
      lnp.setFont(conf.getTextFont());
      lnp.setBackground(conf.getLinesBgColor());
      lnp.setForeground(conf.getLinesColor());
      statusColor = conf.getStatusColor();
      statusWarnColor = conf.getStatusWarnColor();
      c = conf.getStatusBgColor();
      statusbar.setBackground(conf.getStatusBgColor());
      statusbar.setFont(conf.getStatusFont());
      Rectangle r = conf.getPosition();
      if(r != null) {
        this.setLocation(r.x, r.y);
        this.setSize(r.width, r.height);
      }
    }
    else {
      editor.setBackground(DEF_EDITOR_BG);
      editor.setForeground(DEF_EDITOR_FG);
      editor.setSelectionColor(DEF_SELECT_COLOR);
      statusColor = DEF_STATUS_COLOR;
      statusWarnColor = STATUS_ERROR_COLOR;
      statusbar.setBackground(DEF_EDITOR_BG);
      defineConfig();
      Exception e = conf.save();
      if(e != null)
        throw new IllegalStateException(e.getMessage(), e);
    }
  }
  
  
  public void defineConfig() {
    conf.setLinesBgColor(lnp.getBackground());
    conf.setLinesColor(lnp.getForeground());
    conf.setStatusBgColor(statusbar.getBackground());
    conf.setStatusColor(statusColor);
    conf.setStatusFont(statusbar.getFont());
    conf.setStatusWarnColor(statusWarnColor);
    conf.setTextBgColor(editor.getBackground());
    conf.setTextColor(editor.getForeground());
    conf.setTextFont(editor.getFont());
    conf.setTextSelectionColor(editor.getSelectionColor());
  }
  
  
  public void saveConfig() {
    Exception e = conf.save();
    if(e != null) {
      status("Error saving configurations: "+ e.getMessage(), true);
    } else {
      status("Configurations saved", false);
    }
  }
  
  
  public void setTextColor(Color c) {
    if(c != null) {
      conf.setTextColor(c);
      editor.setForeground(c);
      editor.repaint();
      saveConfig();
    }
  }
  
  
  public void setTextBGColor(Color c) {
    if(c != null) {
      conf.setTextBgColor(c);
      editor.setBackground(c);
      editor.repaint();
      saveConfig();
    }
  }
  
  
  public void setTextSelectionColor(Color c) {
    if(c != null) {
      conf.setTextSelectionColor(c);
      editor.setSelectionColor(c);
      editor.repaint();
      saveConfig();
    }
  }
  
  
  public void setStatusColor(Color c) {
    if(c != null) {
      conf.setStatusColor(c);
      statusColor = c;
      saveConfig();
    }
  }
  
  
  public void setStatusWarnColor(Color c) {
    if(c != null) {
      conf.setStatusWarnColor(c);
      statusWarnColor = c;
      saveConfig();
    }
  }
  
  
  public void setStatusBGColor(Color c) {
    if(c != null) {
      conf.setStatusBgColor(c);
      statusbar.setBackground(c);
      saveConfig();
    }
  }
  
  
  public void setLinesColor(Color c) {
    if(c != null) {
      conf.setLinesColor(c);
      lnp.setForeground(c);
      lnp.repaint();
      saveConfig();
    }
  }
  
  
  public void setTextFont(Font f) {
    if(f != null) {
      conf.setTextFont(f);
      editor.setFont(f);
      lnp.setFont(f);
      content.repaint();
      content.revalidate();
      saveConfig();
    }
  }
  
  
  public void setStatusFont(Font f) {
    if(f != null) {
      conf.setStatusFont(f);
      statusbar.setFont(f);
      saveConfig();
    }
  }
  
  
  public void toggleButtonBar() {
    Dimension d = buttonBar.getPreferredSize();
    if(d.height == 1)
      d.height = buttonBarHeight;
    else
      d.height = 1;
    buttonBar.setSize(d);
    buttonBar.setPreferredSize(d);
    buttonBar.repaint();
    content.revalidate();
  }
  
  
  public void find() {
    replace.setLocation(ScreenPositioner
        .getCenterWindowPoint(this, replace));
    replace.setVisible(true);
  }
  
  
  public void colors() {
    ColorsDialog cd = new ColorsDialog(this, true);
    cd.setTextColor(editor.getForeground());
    cd.setTextBgColor(editor.getBackground());
    cd.setTextSelectionColor(editor.getSelectionColor());
    cd.setLinesColor(lnp.getForeground());
    cd.setLinesBgColor(lnp.getBackground());
    cd.setStatusColor(statusColor);
    cd.setStatusBgColor(statusbar.getBackground());
    cd.setStatusWarnColor(statusWarnColor);
    cd.setVisible(true);
    
    editor.setForeground(cd.getTextColor());
    editor.setBackground(cd.getTextBgColor());
    editor.setSelectionColor(cd.getTextSelectionColor());
    lnp.setBackground(cd.getLinesBgColor());
    lnp.setForeground(cd.getLinesColor());
    statusColor = cd.getStatusColor();
    statusWarnColor = cd.getStatusWarnColor();
    statusbar.setBackground(cd.getStatusBgColor());
    defineConfig();
    Exception e = conf.save();
    if(e != null)
      status("Error saving config: "+ e.getMessage(), true);
    else
      status("Configurations saved", false);
  }
  
  
  public void copy() {
    int ss = editor.getSelectionStart();
    int len = editor.getSelectionEnd() - ss;
    if(ss < 0 || len <= 0) {
      status("No text selected", true);
      return;
    }
    copy.setText(editor.getString(ss, len));
    copy.putInClipboard();
    status("Copied ("+ copy.getText()+ ")", false);
  }
  
  
  public void paste() {
    copy.setFromClipboard();
    if(copy.getText() == null 
        || copy.getText().isEmpty()) {
      status("Nothing to paste", true);
      return;
    }
    int ss = editor.getSelectionStart();
    int len = editor.getSelectionEnd() - ss;
    if(ss < 0 || len <= 0) {
      ss = editor.getCaretPosition();
      len = 0;
    }
    editor.undo(editor.getText());
    editor.replace(ss, len, copy.getText());
    editor.update();
    status("Paste ("+ copy.getText()+ ")", false);
  }
  
  
  public void selectFont() {
    Font f = editor.getFont();
    FontSelector fs = new FontSelector(this, true, f);
    fs.setLocation(ScreenPositioner
        .getCenterWindowPoint(this, fs));
    fs.setVisible(true);
    f = fs.getSelectedFont();
    editor.setFont(f);
    lnp.setFont(f);
    lnp.setLinesNumber(editor.getLinesNumber());
  }
  
  
  public void open() {
    lastFile = showFileChooser(ICON_OPEN, false);
    if(lastFile == null) {
      status("No file selected", true);
      return;
    }
    if(read(lastFile)) {
      status("File Opened ("+ lastFile+ ")", false);
    }
  }
  
  
  public File showFileChooser(String iconpath, boolean save) {
    JFileChooser ch = new JFileChooser(lastFile) {
      protected JDialog createDialog(Component parent) {
        JDialog dlg = super.createDialog(parent);
        if(iconpath != null && !iconpath.isEmpty())
          dlg.setIconImage(new ImageIcon(
              Editor.class.getClass().getResource(
              iconpath)).getImage());
        return dlg;
      }
    };
    ch.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int opt = 0;
    if(save)
      opt = ch.showSaveDialog(this);
    else
      opt = ch.showOpenDialog(this);
    if(opt != JFileChooser.APPROVE_OPTION)
      return null;
    return ch.getSelectedFile();
  }
  
  
  public void save() {
    if(lastFile == null)
      saveAs();
    else if(write(lastFile))
      status("File Saved ("+ lastFile+ ")", false);
  }
  
  
  public void saveAs() {
    lastFile = showFileChooser(ICON_SAVE, true);
    if(lastFile == null) {
      status("No file selected", true);
      return;
    }
    if(write(lastFile)) {
      status("File Saved ("+ lastFile+ ")", false);
    }
  }
  
  
  public boolean write(File f) {
    if(f == null) return false;
    try (
        BufferedWriter bw = new BufferedWriter(
            new FileWriter(f));
        ) {
      String text = editor.getText();
      String[] ls = text.split(Editor.LN);
      for(String l : ls) {
        bw.write(l);
        bw.newLine();
      }
      bw.flush();
      return true;
    } 
    catch(IOException e) {
      status("Error writing file:"+ e.getMessage(), true);
      return false;
    } 
  }
  
  
  public boolean read(File f) {
    if(f == null || !f.exists()) {
      status("File dont exists ("+ f+ ")", true);
      return false;
    }
    try (
        BufferedReader br = new BufferedReader(
            new FileReader(f));
        ) {
      StringBuffer sb = new StringBuffer();
      while(true) {
        String ln = br.readLine();
        if(ln == null) break;
        sb.append(ln).append(Editor.LN);
      }
      editor.setText(sb.toString());
      editor.setCaretPosition(0);
      editor.update();
      lnp.revalidate();
      return true;
    }
    catch(IOException e) {
      status("Error reading file: "+ e.getMessage(), true);
      return false;
    }
  }
  
  
  public void status(String text, boolean error) {
    if(error) {
      statusbar.setForeground(STATUS_ERROR_COLOR);
    } else {
      statusbar.setForeground(DEF_STATUS_COLOR);
    }
    statusbar.setText(" "+ text);
    statusbar.setPreferredSize(
        new Dimension(statusbar.getWidth(), 
            STATUS_HEIGHT));
    statusbar.repaint();
    content.revalidate();
    hideStatus.restart();
  }
  
  
  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    editor.requestFocus();
  }


  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    content = new javax.swing.JPanel();
    buttonBar = new javax.swing.JPanel();
    saveAction = new us.pserver.coder.ActionLabel();
    openAction = new us.pserver.coder.ActionLabel();
    copyAction = new us.pserver.coder.ActionLabel();
    pasteAction = new us.pserver.coder.ActionLabel();
    findAction = new us.pserver.coder.ActionLabel();
    undoAction = new us.pserver.coder.ActionLabel();
    redoAction = new us.pserver.coder.ActionLabel();
    fontAction = new us.pserver.coder.ActionLabel();
    colorsAction = new us.pserver.coder.ActionLabel();
    scroll = new javax.swing.JScrollPane();
    statusbar = new javax.swing.JLabel();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jMenu2 = new javax.swing.JMenu();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    content.setLayout(new java.awt.BorderLayout());

    buttonBar.setBackground(new java.awt.Color(80, 80, 80));

    saveAction.setForeground(new java.awt.Color(255, 255, 255));
    saveAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/circle-save-24.png"))); // NOI18N
    saveAction.setText("Save");
    saveAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        saveActionMouseClicked(evt);
      }
    });

    openAction.setForeground(new java.awt.Color(255, 255, 255));
    openAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/circle-open-24.png"))); // NOI18N
    openAction.setText("Open");
    openAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        openActionMouseClicked(evt);
      }
    });

    copyAction.setForeground(new java.awt.Color(255, 255, 255));
    copyAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/circle-copy-24.png"))); // NOI18N
    copyAction.setText("Copy");
    copyAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        copyActionMouseClicked(evt);
      }
    });

    pasteAction.setForeground(new java.awt.Color(255, 255, 255));
    pasteAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/paste-24.png"))); // NOI18N
    pasteAction.setText("Paste");
    pasteAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        pasteActionMouseClicked(evt);
      }
    });

    findAction.setForeground(new java.awt.Color(255, 255, 255));
    findAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/find-24.png"))); // NOI18N
    findAction.setText("Find");
    findAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        findActionMouseClicked(evt);
      }
    });

    undoAction.setForeground(new java.awt.Color(255, 255, 255));
    undoAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/undo-24.png"))); // NOI18N
    undoAction.setText("Undo");
    undoAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        undoActionMouseClicked(evt);
      }
    });

    redoAction.setForeground(new java.awt.Color(255, 255, 255));
    redoAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/redo-24.png"))); // NOI18N
    redoAction.setText("Redo");
    redoAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        redoActionMouseClicked(evt);
      }
    });

    fontAction.setForeground(new java.awt.Color(255, 255, 255));
    fontAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/font-24.png"))); // NOI18N
    fontAction.setText("Font");
    fontAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        fontActionMouseClicked(evt);
      }
    });

    colorsAction.setForeground(new java.awt.Color(255, 255, 255));
    colorsAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/colors-24.png"))); // NOI18N
    colorsAction.setText("Colors");
    colorsAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        colorsActionMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout buttonBarLayout = new javax.swing.GroupLayout(buttonBar);
    buttonBar.setLayout(buttonBarLayout);
    buttonBarLayout.setHorizontalGroup(
      buttonBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(buttonBarLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(buttonBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(buttonBarLayout.createSequentialGroup()
            .addComponent(saveAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(copyAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(buttonBarLayout.createSequentialGroup()
            .addComponent(openAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(pasteAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(12, 12, 12)
        .addGroup(buttonBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(buttonBarLayout.createSequentialGroup()
            .addComponent(redoAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(fontAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(buttonBarLayout.createSequentialGroup()
            .addComponent(undoAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(findAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(colorsAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(35, Short.MAX_VALUE))
    );
    buttonBarLayout.setVerticalGroup(
      buttonBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(buttonBarLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(buttonBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(saveAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(copyAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(undoAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(findAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(colorsAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(buttonBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(openAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(pasteAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(redoAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(fontAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    content.add(buttonBar, java.awt.BorderLayout.NORTH);
    content.add(scroll, java.awt.BorderLayout.CENTER);

    statusbar.setBackground(new java.awt.Color(80, 80, 80));
    statusbar.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    statusbar.setPreferredSize(new java.awt.Dimension(40, 1));
    content.add(statusbar, java.awt.BorderLayout.SOUTH);

    jMenu1.setText("File");
    jMenuBar1.add(jMenu1);

    jMenu2.setText("Edit");
    jMenuBar1.add(jMenu2);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  
  private void findActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_findActionMouseClicked
    find();
  }//GEN-LAST:event_findActionMouseClicked

  private void copyActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_copyActionMouseClicked
    copy();
  }//GEN-LAST:event_copyActionMouseClicked

  private void pasteActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pasteActionMouseClicked
    paste();
  }//GEN-LAST:event_pasteActionMouseClicked

  private void undoActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_undoActionMouseClicked
    editor.undo();
  }//GEN-LAST:event_undoActionMouseClicked

  private void redoActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redoActionMouseClicked
    editor.redo();
  }//GEN-LAST:event_redoActionMouseClicked

  private void openActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openActionMouseClicked
    open();
  }//GEN-LAST:event_openActionMouseClicked

  private void saveActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveActionMouseClicked
    save();
  }//GEN-LAST:event_saveActionMouseClicked

  private void fontActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fontActionMouseClicked
    selectFont();
  }//GEN-LAST:event_fontActionMouseClicked

  private void colorsActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorsActionMouseClicked
    colors();
  }//GEN-LAST:event_colorsActionMouseClicked


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
      java.util.logging.Logger.getLogger(FrameEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(FrameEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(FrameEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(FrameEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
        //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new FrameEditor().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel buttonBar;
  private us.pserver.coder.ActionLabel colorsAction;
  private javax.swing.JPanel content;
  private us.pserver.coder.ActionLabel copyAction;
  private us.pserver.coder.ActionLabel findAction;
  private us.pserver.coder.ActionLabel fontAction;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenuBar jMenuBar1;
  private us.pserver.coder.ActionLabel openAction;
  private us.pserver.coder.ActionLabel pasteAction;
  private us.pserver.coder.ActionLabel redoAction;
  private us.pserver.coder.ActionLabel saveAction;
  private javax.swing.JScrollPane scroll;
  private javax.swing.JLabel statusbar;
  private us.pserver.coder.ActionLabel undoAction;
  // End of variables declaration//GEN-END:variables
}
