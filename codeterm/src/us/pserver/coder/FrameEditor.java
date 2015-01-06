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
      DEF_EDITOR_BG = new Color(61, 61, 61),
      DEF_EDITOR_FG = new Color(102, 255, 51),
      DEF_SELECT_COLOR = new Color(170, 170, 170);
  
  public static final int 
      STATUS_HEIGHT = 18,
      STATUS_DELAY = 3500;
  
  
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
    super("Codeterm");
    editor = new Editor();
    editor.setBackground(DEF_EDITOR_BG);
    editor.setForeground(DEF_EDITOR_FG);
    editor.setSelectionColor(DEF_SELECT_COLOR);
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
        else if(e.getKeyCode() == KeyEvent.VK_B && e.isControlDown()) {
          base64();
          e.consume();
        }
      }
    });
    hideStatus = new Timer(STATUS_DELAY, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setStatusBarVisible(false);
      }
    });
    hideStatus.setRepeats(false);
  }
  
  
  public CodetermConfig getConfig() {
    return conf;
  }
  
  
  public Editor getEditor() {
    return editor;
  }
  
  
  private void initConfig() {
    if(conf.fileExists()) {
      Exception e = conf.load();
      if(e != null)
        throw new IllegalStateException(e.getMessage(), e);
      editor.setBackground(conf.getTextBgColor());
      editor.setForeground(conf.getTextColor());
      editor.setFont(conf.getTextFont());
      editor.setSelectionColor(conf.getTextSelectionColor());
      
      lnp.setFont(conf.getTextFont());
      lnp.setBackground(conf.getLinesBgColor());
      lnp.setForeground(conf.getLinesColor());
      
      statusColor = conf.getStatusColor();
      statusWarnColor = conf.getStatusWarnColor();
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
    //Lines
    conf.setLinesColor(lnp.getForeground());
    conf.setLinesBgColor(lnp.getBackground());
    //Status
    conf.setStatusColor(statusColor);
    conf.setStatusWarnColor(statusWarnColor);
    conf.setStatusBgColor(statusbar.getBackground());
    conf.setStatusFont(statusbar.getFont());
    //Text
    conf.setTextColor(editor.getForeground());
    conf.setTextSelectionColor(editor.getSelectionColor());
    conf.setTextBgColor(editor.getBackground());
    conf.setTextFont(editor.getFont());
  }
  
  
  public void base64() {
    Base64Converter cv = new Base64Converter(this, true);
    cv.setTitle("Base64 Converter");
    cv.setLocationRelativeTo(this);
    cv.setVisible(true);
    editor.replace(editor.getCaretPosition(), 0, cv.getConverted());
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
  
  
  public void setLinesBGColor(Color c) {
    if(c != null) {
      conf.setLinesBgColor(c);
      lnp.setBackground(c);
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
  
  
  public void configDialog() {
    JDialog dlg = new JDialog(this, "Configurations");
    dlg.setIconImage(IconProvider.getIconGearGray());
    dlg.add(new PanelConfig(this));
    dlg.pack();
    dlg.setLocation(ScreenPositioner
        .getCenterWindowPoint(this, dlg));
    dlg.setVisible(true);
  }
  
  
  public void highlightsDialog() {
    JDialog dlg = new JDialog(this, "Syntax Highlight");
    dlg.setIconImage(IconProvider.getIconFontGray());
    dlg.add(new HighlightConfigPanel(this));
    dlg.pack();
    dlg.setLocation(ScreenPositioner
        .getCenterWindowPoint(this, dlg));
    dlg.setVisible(true);
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
    lastFile = showFileChooser(false);
    if(lastFile == null) {
      status("No file selected", true);
      return;
    }
    if(read(lastFile)) {
      status("File Opened ("+ lastFile+ ")", false);
    }
  }
  
  
  public File showFileChooser(boolean save) {
    JFileChooser ch = new JFileChooser(lastFile) {
      protected JDialog createDialog(Component parent) {
        JDialog dlg = super.createDialog(parent);
        if(save) {
          dlg.setIconImage(IconProvider.getIconSaveGray());
        } else {
          dlg.setIconImage(IconProvider.getIconOpenGray());
        }
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
    lastFile = showFileChooser(true);
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
      this.setTitle("Codeterm - "+ f.getName());
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
      this.setTitle("Codeterm - "+ f.getName());
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
    setStatusBarVisible(true);
    hideStatus.restart();
  }
  
  
  public void setStatusBarVisible(boolean visible) {
    statusbar.setPreferredSize(
        new Dimension(statusbar.getWidth(), 
            (visible ? STATUS_HEIGHT : 1)));
    statusbar.repaint();
    content.revalidate();
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
    highlightsAction = new us.pserver.coder.ActionLabel();
    colorsAction = new us.pserver.coder.ActionLabel();
    hideAction = new us.pserver.coder.ActionLabel();
    scroll = new javax.swing.JScrollPane();
    statusbar = new javax.swing.JLabel();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    menuNew = new javax.swing.JMenuItem();
    menuOpen = new javax.swing.JMenuItem();
    menuSave = new javax.swing.JMenuItem();
    menuSaveAs = new javax.swing.JMenuItem();
    jMenu2 = new javax.swing.JMenu();
    menuUndo = new javax.swing.JMenuItem();
    menuRedo = new javax.swing.JMenuItem();
    menuCopy = new javax.swing.JMenuItem();
    menuPaste = new javax.swing.JMenuItem();
    menuFind = new javax.swing.JMenuItem();
    menuToBase64 = new javax.swing.JMenuItem();
    jMenu3 = new javax.swing.JMenu();
    menuFont = new javax.swing.JMenuItem();
    menuConfig = new javax.swing.JMenuItem();
    menuHideButtons = new javax.swing.JCheckBoxMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setIconImage(us.pserver.coder.IconProvider.getIconCodeterm());

    content.setLayout(new java.awt.BorderLayout());

    buttonBar.setBackground(new java.awt.Color(80, 80, 80));

    saveAction.setForeground(new java.awt.Color(255, 255, 255));
    saveAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/save-white-20.png"))); // NOI18N
    saveAction.setText("Save");
    saveAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        saveActionMouseClicked(evt);
      }
    });

    openAction.setForeground(new java.awt.Color(255, 255, 255));
    openAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/open-white-20.png"))); // NOI18N
    openAction.setText("Open");
    openAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        openActionMouseClicked(evt);
      }
    });

    copyAction.setForeground(new java.awt.Color(255, 255, 255));
    copyAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/copy-white-20.png"))); // NOI18N
    copyAction.setText("Copy");
    copyAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        copyActionMouseClicked(evt);
      }
    });

    pasteAction.setForeground(new java.awt.Color(255, 255, 255));
    pasteAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/paste-white-20.png"))); // NOI18N
    pasteAction.setText("Paste");
    pasteAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        pasteActionMouseClicked(evt);
      }
    });

    findAction.setForeground(new java.awt.Color(255, 255, 255));
    findAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/search-white-20.png"))); // NOI18N
    findAction.setText("Find");
    findAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        findActionMouseClicked(evt);
      }
    });

    undoAction.setForeground(new java.awt.Color(255, 255, 255));
    undoAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/undo-white-20.png"))); // NOI18N
    undoAction.setText("Undo");
    undoAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        undoActionMouseClicked(evt);
      }
    });

    redoAction.setForeground(new java.awt.Color(255, 255, 255));
    redoAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/redo-white-20.png"))); // NOI18N
    redoAction.setText("Redo");
    redoAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        redoActionMouseClicked(evt);
      }
    });

    highlightsAction.setForeground(new java.awt.Color(255, 255, 255));
    highlightsAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/highlight-white-20.png"))); // NOI18N
    highlightsAction.setText("Highlights");
    highlightsAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        highlightsActionMouseClicked(evt);
      }
    });

    colorsAction.setForeground(new java.awt.Color(255, 255, 255));
    colorsAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/gear-white-20.png"))); // NOI18N
    colorsAction.setText("Config");
    colorsAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        colorsActionMouseClicked(evt);
      }
    });

    hideAction.setForeground(new java.awt.Color(255, 255, 255));
    hideAction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/coder/images/hide-white-20.png"))); // NOI18N
    hideAction.setText("Hide");
    hideAction.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        hideActionMouseClicked(evt);
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
            .addComponent(undoAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(findAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(buttonBarLayout.createSequentialGroup()
            .addComponent(redoAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(hideAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(buttonBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(highlightsAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(colorsAction, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(15, Short.MAX_VALUE))
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
          .addComponent(highlightsAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(hideAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    content.add(buttonBar, java.awt.BorderLayout.NORTH);
    content.add(scroll, java.awt.BorderLayout.CENTER);

    statusbar.setBackground(new java.awt.Color(80, 80, 80));
    statusbar.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    statusbar.setPreferredSize(new java.awt.Dimension(40, 1));
    statusbar.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        statusbarMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        statusbarMouseEntered(evt);
      }
    });
    content.add(statusbar, java.awt.BorderLayout.SOUTH);

    jMenu1.setText("File");

    menuNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
    menuNew.setText("New");
    menuNew.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuNewActionPerformed(evt);
      }
    });
    jMenu1.add(menuNew);

    menuOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    menuOpen.setText("Open");
    menuOpen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuOpenActionPerformed(evt);
      }
    });
    jMenu1.add(menuOpen);

    menuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    menuSave.setText("Save");
    menuSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuSaveActionPerformed(evt);
      }
    });
    jMenu1.add(menuSave);

    menuSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    menuSaveAs.setText("Save As");
    menuSaveAs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuSaveAsActionPerformed(evt);
      }
    });
    jMenu1.add(menuSaveAs);

    jMenuBar1.add(jMenu1);

    jMenu2.setText("Edit");

    menuUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
    menuUndo.setText("Undo");
    menuUndo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuUndoActionPerformed(evt);
      }
    });
    jMenu2.add(menuUndo);

    menuRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
    menuRedo.setText("Redo");
    menuRedo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuRedoActionPerformed(evt);
      }
    });
    jMenu2.add(menuRedo);

    menuCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
    menuCopy.setText("Copy");
    menuCopy.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuCopyActionPerformed(evt);
      }
    });
    jMenu2.add(menuCopy);

    menuPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
    menuPaste.setText("Paste");
    menuPaste.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuPasteActionPerformed(evt);
      }
    });
    jMenu2.add(menuPaste);

    menuFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
    menuFind.setText("Find");
    menuFind.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuFindActionPerformed(evt);
      }
    });
    jMenu2.add(menuFind);

    menuToBase64.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
    menuToBase64.setText("Text to Base64");
    menuToBase64.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuToBase64ActionPerformed(evt);
      }
    });
    jMenu2.add(menuToBase64);

    jMenuBar1.add(jMenu2);

    jMenu3.setText("Settings");

    menuFont.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    menuFont.setText("Font Selector");
    menuFont.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuFontActionPerformed(evt);
      }
    });
    jMenu3.add(menuFont);

    menuConfig.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    menuConfig.setText("Configurations");
    menuConfig.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuConfigActionPerformed(evt);
      }
    });
    jMenu3.add(menuConfig);

    menuHideButtons.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
    menuHideButtons.setSelected(true);
    menuHideButtons.setText("Show/Hide Buttons Bar");
    menuHideButtons.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuHideButtonsActionPerformed(evt);
      }
    });
    jMenu3.add(menuHideButtons);

    jMenuBar1.add(jMenu3);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
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

  private void highlightsActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_highlightsActionMouseClicked
    highlightsDialog();
  }//GEN-LAST:event_highlightsActionMouseClicked

  private void colorsActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorsActionMouseClicked
    configDialog();
  }//GEN-LAST:event_colorsActionMouseClicked

  private void menuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewActionPerformed
    lastFile = null;
    editor.setText("");
    this.setTitle("Codeterm - new");
  }//GEN-LAST:event_menuNewActionPerformed

  private void menuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenActionPerformed
    this.open();
  }//GEN-LAST:event_menuOpenActionPerformed

  private void menuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveActionPerformed
    this.save();
  }//GEN-LAST:event_menuSaveActionPerformed

  private void menuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveAsActionPerformed
    this.saveAs();
  }//GEN-LAST:event_menuSaveAsActionPerformed

  private void menuUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUndoActionPerformed
    editor.undo();
  }//GEN-LAST:event_menuUndoActionPerformed

  private void menuRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRedoActionPerformed
    editor.redo();
  }//GEN-LAST:event_menuRedoActionPerformed

  private void menuCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCopyActionPerformed
    this.copy();
  }//GEN-LAST:event_menuCopyActionPerformed

  private void menuPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPasteActionPerformed
    this.paste();
  }//GEN-LAST:event_menuPasteActionPerformed

  private void menuFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFindActionPerformed
    this.find();
  }//GEN-LAST:event_menuFindActionPerformed

  private void menuFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFontActionPerformed
    this.highlightsActionMouseClicked(null);
  }//GEN-LAST:event_menuFontActionPerformed

  private void menuConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConfigActionPerformed
    this.configDialog();
  }//GEN-LAST:event_menuConfigActionPerformed

  private void menuHideButtonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHideButtonsActionPerformed
    this.toggleButtonBar();
  }//GEN-LAST:event_menuHideButtonsActionPerformed

  private void statusbarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statusbarMouseEntered
    setStatusBarVisible(true);
    this.hideStatus.stop();
  }//GEN-LAST:event_statusbarMouseEntered

  private void statusbarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statusbarMouseExited
    this.hideStatus.start();
  }//GEN-LAST:event_statusbarMouseExited

  private void menuToBase64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuToBase64ActionPerformed
    base64();
  }//GEN-LAST:event_menuToBase64ActionPerformed

  private void hideActionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hideActionMouseClicked
    toggleButtonBar();
  }//GEN-LAST:event_hideActionMouseClicked


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
  private us.pserver.coder.ActionLabel hideAction;
  private us.pserver.coder.ActionLabel highlightsAction;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenu jMenu3;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenuItem menuConfig;
  private javax.swing.JMenuItem menuCopy;
  private javax.swing.JMenuItem menuFind;
  private javax.swing.JMenuItem menuFont;
  private javax.swing.JCheckBoxMenuItem menuHideButtons;
  private javax.swing.JMenuItem menuNew;
  private javax.swing.JMenuItem menuOpen;
  private javax.swing.JMenuItem menuPaste;
  private javax.swing.JMenuItem menuRedo;
  private javax.swing.JMenuItem menuSave;
  private javax.swing.JMenuItem menuSaveAs;
  private javax.swing.JMenuItem menuToBase64;
  private javax.swing.JMenuItem menuUndo;
  private us.pserver.coder.ActionLabel openAction;
  private us.pserver.coder.ActionLabel pasteAction;
  private us.pserver.coder.ActionLabel redoAction;
  private us.pserver.coder.ActionLabel saveAction;
  private javax.swing.JScrollPane scroll;
  private javax.swing.JLabel statusbar;
  private us.pserver.coder.ActionLabel undoAction;
  // End of variables declaration//GEN-END:variables
}
