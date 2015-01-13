/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.j3270;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import murlen.util.fscript.ParserListener;
import us.pserver.j3270.script.CodeListener;
import us.pserver.j3270.script.ScriptProcessor;
import us.pserver.tn3270.Cursor;


/**
 *
 * @author juno
 */
public class CodeViewerOld extends javax.swing.JDialog 
implements CodeListener, ParserListener {

  public static final int UNDO_MAX = 80;
  
  private static String LN = (File.separatorChar == '/' ? "\n" : "\r\n");
  
  private TextCopy tcp;
  
  private File f, dir;
  
  private J3270 j3270;
  
  private List<String> undo;
  
  private List<String> redo;
  
  private int uidx, ridx;
  
  private boolean debug;
  
  
  /**
   * Creates new form CodeViewer
   */
  public CodeViewerOld(J3270 parent, boolean modal) {
    super(parent, modal);
    initComponents();
    f = dir = null;
    debug = true;
    j3270 = parent;
    j3270.scriptProc().processor()
        .addParserListener(this);
    
    PrintStream ps = new PrintStream(new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        console.append(String.valueOf((char) b));
        console.setCaretPosition(console.getText().length());
        console.paint(console.getGraphics());
      }
    });
    System.setOut(ps);
    j3270.scriptProc().setStdOut(ps);
    
    undo = new LinkedList<String>();
    redo = new LinkedList<String>();
    uidx = ridx = 0;
    codeArea.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        if(e.isControlDown()) return;
        if(undo.size() > UNDO_MAX)
          undo.remove(0);
        undo.add(codeArea.getText());
        uidx = undo.size() -1;
        redo.clear();
      }
      @Override public void keyPressed(KeyEvent e) {}
      @Override public void keyReleased(KeyEvent e) {}
    });
  }
  
  
  public void lineUpdate(String line) {
    if(debug) println(line);
  }
  
  
  @Override
  public void codeAppended(String str) {
    if(str == null || str.isEmpty())
      return;
    codeArea.append(str);
    codeArea.append(LN);
    codeArea.setCaretPosition(getCode().length());
  }
  
  
  @Override
  public void codeChanged(String str) {
    if(str == null || str.isEmpty())
      return;
    codeArea.setText(str);
    codeArea.setCaretPosition(getCode().length());
  }
  
  
  public void setCode(String code) {
    codeArea.setText(code);
    codeArea.setCaretPosition(code.length());
  }
  
  
  public String getCode() {
    return codeArea.getText();
  }
  
  
  public File getFile() {
    return f;
  }
  
  
  public void selectAll() {
    if(getCode() == null || getCode().isEmpty())
      return;
    codeArea.setSelectionStart(0);
    codeArea.setSelectionEnd(getCode().length());
  }
  
  
  public void unselect() {
    if(getCode() == null || getCode().isEmpty())
      return;
    codeArea.setCaretPosition(getCode().length());
  }
  
  
  public void copy() {
    if(getCode() == null || getCode().isEmpty()
        || codeArea.getSelectedText() == null 
        || codeArea.getSelectedText().isEmpty())
      return;
    tcp.setText(codeArea.getSelectedText()).putInClipboard();
  }


  public void cut() {
    if(getCode() == null || getCode().isEmpty()
        || codeArea.getSelectedText() == null 
        || codeArea.getSelectedText().isEmpty())
      return;
    tcp.setText(codeArea.getSelectedText()).putInClipboard();
    String code = getCode().substring(0, 
        codeArea.getSelectionStart());
    if(codeArea.getSelectionEnd() < getCode().length())
      code += getCode().substring(
          codeArea.getSelectionEnd(), getCode().length());
    codeArea.setText(code);
  }
  
  
  public void paste() {
    tcp.setFromClipboard();
    String code = getCode();
    int caret = codeArea.getCaretPosition();
    if(code != null && !code.isEmpty()
        && caret < code.length()) {
      code = code.substring(0, caret)
          + tcp.getText() 
          + code.substring(caret, code.length());
    }
    codeArea.setText(code);
  }
  
  
  public File selectFile(String title, String extension, boolean open) {
    if(title == null || extension == null)
      return null;
    JFileChooser chooser = new JFileChooser(dir);
    chooser.setDialogTitle(title);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    
    FileNameExtensionFilter filter = 
        new FileNameExtensionFilter(extension, extension);
    chooser.setFileFilter(filter);
    if(open) {
      if(chooser.showOpenDialog(this) 
          != JFileChooser.APPROVE_OPTION) {
        return null;
      }
    } else {
      if(chooser.showSaveDialog(this) 
          != JFileChooser.APPROVE_OPTION) {
        return null;
      }
    }
    File file = chooser.getSelectedFile();
    if(file.isDirectory())
      dir = file;
    else if(file.getParentFile() != null)
      dir = file.getParentFile();
    return file;
  }
  
  
  public void save() {
    if(f == null)
      f = this.selectFile("Save Code", "txt", false);
    if(f == null) return;
    String code = getCode().replaceAll("\n", LN);
    try {
      FileWriter fw = new FileWriter(f, false);
      fw.write(code);
      this.setTitle("Script: "+ f.toString());
      fw.close();
    } catch(IOException ex) {}
  }
  
  
  public void saveas() {
    File f = this.selectFile("Save Code", "txt", false);
    if(f == null) return;
    String code = getCode().replaceAll("\n", LN);
    try {
      FileWriter fw = new FileWriter(f, false);
      fw.write(code);
      this.f = f;
      this.setTitle("Script: "+ f.toString());
      fw.close();
    } catch(IOException ex) {}
  }
  
  
  public void newScript() {
    codeArea.setText("");
    console.setText("");
    f = null;
    this.setTitle("Code Viewer");
  }
  
  
  public void open() {
    open(selectFile(
        "Open Code", "txt", true));
    this.setTitle("Script: "+ f);
  }
  
  
  public void open(File f) {
    if(f == null || !f.exists())
      return;
    
    this.f = f;
    StringBuffer sb = new StringBuffer();
    try {
      FileReader fr = new FileReader(f);
      int ch = -1;
      while((ch = fr.read()) != -1) {
        if(((char)ch) == '\r') continue;
        sb.append((char) ch);
      }
      codeArea.setText(sb.toString());
      fr.close();
    } catch(IOException ex) {}
  }
  
  
  public void clearFile() {
    f = null;
  }
  
  
  public void println(String str) {
    System.out.println("  ["+ str+ "]");
  }
  
  
  public void record() {
    if(recButton.isSelected()) {
      j3270.startRecord();
      println("Recording...");
    } else {
      j3270.stopRecord();
      println("Record stopped!");
    }
  }
  
  
  public void play() {
    if(playButton.isSelected()) {
      if(f == null && (getCode() == null 
          || getCode().isEmpty()))
        this.open();
      
      this.exec();
    }
    
    else {
      try {
        println("Iterrupted: "+ j3270.scriptProc()
            .processor().getCurrentLine());
        if(debug)
          println("Context>> "+ j3270.scriptProc()
              .processor().getContext());
        j3270.scriptProc()
          .processor().reset(); 
        j3270.scriptProc().closeIO();
      }
      catch(Exception e) {}
      playButton.setText("Play");
      println("Execution stopped!");
    }
  }
  
  
  public void exec() {
    console.setText("");
    if(getCode() == null || getCode().isEmpty()) {
      println("### No code for execution");
      return;
    }
    playButton.setText("Stop");
    println("Executing script...");
    
    Thread th = new Thread() {
      public void run() { 
        processCode(); 
      }
    };
    th.setPriority(Thread.MAX_PRIORITY);
    th.start();
  }
  
  
  public void processCode() {
    ScriptProcessor proc = j3270.scriptProc();
    proc.processor().reset();
    
    String[] lns = getCode().split("\n");
    try {
      for(String l : lns) {
        if(l.startsWith("import")) {
          proc.execLine(l);
        }
        else {
          proc.loadLine(l);
        }
      }
      
      proc.exec();
      
      playButton.setSelected(false);
      play();
      
    } catch(Exception e) {
      println("### "+ e.getMessage());
      if(playButton.isSelected()) {
        playButton.setSelected(false);
        play();
      }
    }
  }
  
  
  public void delay(int millis) {
    try { Thread.sleep(millis); }
    catch(InterruptedException e) {}
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
    recButton = new javax.swing.JToggleButton();
    playButton = new javax.swing.JToggleButton();
    openButton = new javax.swing.JButton();
    saveButton = new javax.swing.JButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    scrollPane = new javax.swing.JScrollPane();
    codeArea = new javax.swing.JTextArea();
    jScrollPane1 = new javax.swing.JScrollPane();
    console = new javax.swing.JTextArea();
    menuBar = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    newMenu = new javax.swing.JMenuItem();
    saveMenu = new javax.swing.JMenuItem();
    saveasMenu = new javax.swing.JMenuItem();
    openMenu = new javax.swing.JMenuItem();
    separator2Menu = new javax.swing.JPopupMenu.Separator();
    closeMenu = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    copyMenu = new javax.swing.JMenuItem();
    cutMenu = new javax.swing.JMenuItem();
    pasteMenu = new javax.swing.JMenuItem();
    separatorMenu = new javax.swing.JPopupMenu.Separator();
    selectMenu = new javax.swing.JMenuItem();
    unselectMenu = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    undoMenu = new javax.swing.JMenuItem();
    redoMenu = new javax.swing.JMenuItem();
    jMenu1 = new javax.swing.JMenu();
    recMenu = new javax.swing.JMenuItem();
    playMenu = new javax.swing.JMenuItem();
    clearMenu = new javax.swing.JMenuItem();
    debugMenu = new javax.swing.JCheckBoxMenuItem();
    jSeparator2 = new javax.swing.JPopupMenu.Separator();
    autowaitMenu = new javax.swing.JCheckBoxMenuItem();
    autoselectMenu = new javax.swing.JCheckBoxMenuItem();

    setTitle("Code Viewer");
    setIconImage(new ImageIcon(getClass().getResource("/us/pserver/j3270/images/icon24.png")).getImage());

    jPanel1.setBackground(new java.awt.Color(238, 238, 238));

    recButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/rec_24.png"))); // NOI18N
    recButton.setText("Record");
    recButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        recButtonActionPerformed(evt);
      }
    });

    playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/play_24.png"))); // NOI18N
    playButton.setText("Play");
    playButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        playButtonActionPerformed(evt);
      }
    });

    openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/open_24.png"))); // NOI18N
    openButton.setText("Open");
    openButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openButtonActionPerformed(evt);
      }
    });

    saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/save_24.png"))); // NOI18N
    saveButton.setText("Save");
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(recButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(playButton)
        .addGap(18, 18, 18)
        .addComponent(openButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(saveButton)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(recButton)
          .addComponent(playButton)
          .addComponent(openButton)
          .addComponent(saveButton))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jSplitPane1.setDividerLocation(280);
    jSplitPane1.setDividerSize(4);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    codeArea.setColumns(20);
    codeArea.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
    codeArea.setRows(5);
    codeArea.setTabSize(2);
    scrollPane.setViewportView(codeArea);

    jSplitPane1.setTopComponent(scrollPane);

    console.setEditable(false);
    console.setBackground(new java.awt.Color(51, 51, 51));
    console.setColumns(20);
    console.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
    console.setForeground(new java.awt.Color(204, 255, 0));
    console.setRows(5);
    jScrollPane1.setViewportView(console);

    jSplitPane1.setRightComponent(jScrollPane1);

    fileMenu.setText("File");

    newMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
    newMenu.setText("New");
    newMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newMenuActionPerformed(evt);
      }
    });
    fileMenu.add(newMenu);

    saveMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    saveMenu.setText("Save");
    saveMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveMenuActionPerformed(evt);
      }
    });
    fileMenu.add(saveMenu);

    saveasMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    saveasMenu.setText("Save As");
    saveasMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveasMenuActionPerformed(evt);
      }
    });
    fileMenu.add(saveasMenu);

    openMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    openMenu.setText("Open");
    openMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openMenuActionPerformed(evt);
      }
    });
    fileMenu.add(openMenu);
    fileMenu.add(separator2Menu);

    closeMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
    closeMenu.setText("Close");
    closeMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        closeMenuActionPerformed(evt);
      }
    });
    fileMenu.add(closeMenu);

    menuBar.add(fileMenu);

    editMenu.setText("Edit");

    copyMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
    copyMenu.setText("Copy");
    copyMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        copyMenuActionPerformed(evt);
      }
    });
    editMenu.add(copyMenu);

    cutMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
    cutMenu.setText("Cut");
    cutMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cutMenuActionPerformed(evt);
      }
    });
    editMenu.add(cutMenu);

    pasteMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
    pasteMenu.setText("Paste");
    pasteMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        pasteMenuActionPerformed(evt);
      }
    });
    editMenu.add(pasteMenu);
    editMenu.add(separatorMenu);

    selectMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
    selectMenu.setText("Select All");
    selectMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectMenuActionPerformed(evt);
      }
    });
    editMenu.add(selectMenu);

    unselectMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    unselectMenu.setText("Unselect");
    unselectMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        unselectMenuActionPerformed(evt);
      }
    });
    editMenu.add(unselectMenu);
    editMenu.add(jSeparator1);

    undoMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
    undoMenu.setText("Undo");
    undoMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        undoMenuActionPerformed(evt);
      }
    });
    editMenu.add(undoMenu);

    redoMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
    redoMenu.setText("Redo");
    redoMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        redoMenuActionPerformed(evt);
      }
    });
    editMenu.add(redoMenu);

    menuBar.add(editMenu);

    jMenu1.setText("Script");

    recMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
    recMenu.setText("Record");
    recMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        recMenuActionPerformed(evt);
      }
    });
    jMenu1.add(recMenu);

    playMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
    playMenu.setText("Play");
    playMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        playMenuActionPerformed(evt);
      }
    });
    jMenu1.add(playMenu);

    clearMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
    clearMenu.setText("Clear Console");
    clearMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clearMenuActionPerformed(evt);
      }
    });
    jMenu1.add(clearMenu);

    debugMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    debugMenu.setSelected(true);
    debugMenu.setText("Debug");
    debugMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        debugMenuActionPerformed(evt);
      }
    });
    jMenu1.add(debugMenu);
    jMenu1.add(jSeparator2);

    autowaitMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    autowaitMenu.setSelected(true);
    autowaitMenu.setText("Auto Gen WaitFor");
    autowaitMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        autowaitMenuActionPerformed(evt);
      }
    });
    jMenu1.add(autowaitMenu);

    autoselectMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    autoselectMenu.setSelected(true);
    autoselectMenu.setText("Auto Gen Select");
    autoselectMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        autoselectMenuActionPerformed(evt);
      }
    });
    jMenu1.add(autoselectMenu);

    menuBar.add(jMenu1);

    setJMenuBar(menuBar);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jSplitPane1)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
    save();
  }//GEN-LAST:event_saveMenuActionPerformed

  private void saveasMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveasMenuActionPerformed
    saveas();
  }//GEN-LAST:event_saveasMenuActionPerformed

  private void closeMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuActionPerformed
    this.setVisible(false);
  }//GEN-LAST:event_closeMenuActionPerformed

  private void copyMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuActionPerformed
    copy();
  }//GEN-LAST:event_copyMenuActionPerformed

  private void cutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutMenuActionPerformed
    cut();
  }//GEN-LAST:event_cutMenuActionPerformed

  private void pasteMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenuActionPerformed
    paste();
  }//GEN-LAST:event_pasteMenuActionPerformed

  private void selectMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectMenuActionPerformed
    selectAll();
  }//GEN-LAST:event_selectMenuActionPerformed

  private void unselectMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unselectMenuActionPerformed
    unselect();
  }//GEN-LAST:event_unselectMenuActionPerformed

  private void openMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuActionPerformed
    open();
  }//GEN-LAST:event_openMenuActionPerformed

  private void recButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recButtonActionPerformed
    record();
  }//GEN-LAST:event_recButtonActionPerformed

  private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
    play();
  }//GEN-LAST:event_playButtonActionPerformed

  private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
    open();
  }//GEN-LAST:event_openButtonActionPerformed

  private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
    save();
  }//GEN-LAST:event_saveButtonActionPerformed

  private void recMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recMenuActionPerformed
    record();
  }//GEN-LAST:event_recMenuActionPerformed

  private void playMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playMenuActionPerformed
    play();
  }//GEN-LAST:event_playMenuActionPerformed

  private void clearMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearMenuActionPerformed
    console.setText("");
  }//GEN-LAST:event_clearMenuActionPerformed

  private void newMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuActionPerformed
    newScript();
  }//GEN-LAST:event_newMenuActionPerformed

  private void undoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoMenuActionPerformed
    if(undo.isEmpty()) return;
    redo.add(getCode());
    ridx = redo.size() -1;
    codeArea.setText(undo.get(uidx--));
    if(uidx < 0) uidx = undo.size() -1;
  }//GEN-LAST:event_undoMenuActionPerformed

  private void redoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoMenuActionPerformed
    if(redo.isEmpty()) return;
    codeArea.setText(redo.get(ridx--));
    if(ridx < 0) ridx = redo.size() -1;
  }//GEN-LAST:event_redoMenuActionPerformed

  private void debugMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugMenuActionPerformed
    debug = debugMenu.isSelected();
  }//GEN-LAST:event_debugMenuActionPerformed

  private void autowaitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autowaitMenuActionPerformed
    j3270.scriptGen().setAutoWaitFor(autowaitMenu.isSelected());
    autoselectMenu.setSelected(autowaitMenu.isSelected());
    j3270.scriptGen().setAutoGenSelect(autowaitMenu.isSelected());
  }//GEN-LAST:event_autowaitMenuActionPerformed

  private void autoselectMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoselectMenuActionPerformed
    j3270.scriptGen().setAutoGenSelect(autoselectMenu.isSelected());
  }//GEN-LAST:event_autoselectMenuActionPerformed

  
  public void insertWaitFor() {
    if(!recButton.isSelected()) return;
    String sel = j3270.getDisplay().getSelected();
    Cursor cs = j3270.getDisplay().getSelectionStart();
    if(sel == null || sel.isEmpty() || cs == null) return;
    this.codeAppended("waitfor("+ cs.row()
        + ", "+ cs.column()+ ", \""+ sel+ "\")");
  }
  
  
  public void insertGetText() {
    if(!recButton.isSelected()) return;
    String sel = j3270.getDisplay().getSelected();
    Cursor cs = j3270.getDisplay().getSelectionStart();
    if(sel == null || sel.isEmpty() || cs == null) return;
    int rd = (int) (Math.random() * 100000);
    this.codeAppended("string str"+ String.valueOf(rd)
        + " = gettext("+ cs.row()+ ", "
        + cs.column()+ ", "+ sel.length()+ ")");
  }
  
  
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
      java.util.logging.Logger.getLogger(CodeViewerOld.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(CodeViewerOld.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(CodeViewerOld.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(CodeViewerOld.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
        //</editor-fold>
        //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        CodeViewerOld dialog = new CodeViewerOld(null, true);
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
  private javax.swing.JCheckBoxMenuItem autoselectMenu;
  private javax.swing.JCheckBoxMenuItem autowaitMenu;
  private javax.swing.JMenuItem clearMenu;
  private javax.swing.JMenuItem closeMenu;
  private javax.swing.JTextArea codeArea;
  private javax.swing.JTextArea console;
  private javax.swing.JMenuItem copyMenu;
  private javax.swing.JMenuItem cutMenu;
  private javax.swing.JCheckBoxMenuItem debugMenu;
  private javax.swing.JMenu editMenu;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JPopupMenu.Separator jSeparator2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenuItem newMenu;
  private javax.swing.JButton openButton;
  private javax.swing.JMenuItem openMenu;
  private javax.swing.JMenuItem pasteMenu;
  private javax.swing.JToggleButton playButton;
  private javax.swing.JMenuItem playMenu;
  private javax.swing.JToggleButton recButton;
  private javax.swing.JMenuItem recMenu;
  private javax.swing.JMenuItem redoMenu;
  private javax.swing.JButton saveButton;
  private javax.swing.JMenuItem saveMenu;
  private javax.swing.JMenuItem saveasMenu;
  private javax.swing.JScrollPane scrollPane;
  private javax.swing.JMenuItem selectMenu;
  private javax.swing.JPopupMenu.Separator separator2Menu;
  private javax.swing.JPopupMenu.Separator separatorMenu;
  private javax.swing.JMenuItem undoMenu;
  private javax.swing.JMenuItem unselectMenu;
  // End of variables declaration//GEN-END:variables
}
