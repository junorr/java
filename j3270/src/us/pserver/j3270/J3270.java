/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.j3270;

import com.jpower.conf.Config;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import us.pserver.cdr.b64.Base64Gui;
import us.pserver.j3270.script.ScriptGenerator;
import us.pserver.j3270.script.ScriptProcessor;
import us.pserver.tn3270.Cursor;
import us.pserver.tn3270.Session;


/**
 *
 * @author juno
 */
public class J3270 extends javax.swing.JFrame {
  
  public static final String SERVER_ADDR = "3270.df.bb";
  
  public static final int SERVER_PORT = 8023;
  
  public static final int TEN_MIN = 1000 * 60 * 10;
  
  public static final String MIME_TEXT_CHARSET = "text/plain; charset=UTF-8";

  public static final String MIME_TEXT = "text/plain";
  
  public static final String FILE_CONF = "./settings.conf";
  
  public static final String FONT_NAME = "font.name";
  
  public static final String FONT_SIZE = "font.size";
  
  public static final String FONT_STYLE = "font.style";
  
  public static final String CONN_HOST = "connection.host";
  
  public static final String CONN_PORT = "connection.port";
  
  public static final String STARTUP_SCRIPT = "startup_script";
  
  public static final String COMMENT = "J3270 - Java 3270 Terminal Emulator";
  
  public static final Color STATUS_DEF_COLOR = new Color(45, 90, 180);

  public static final Color STATUS_ERROR_COLOR = new Color(170, 0, 0);

  
  private Session sess;
  
  private String address;
  
  private int port;
  
  private TextCopy copy;
  
  private JDriver driver;
  
  private List<WindowListener> lst;
  
  private ScriptGenerator gen;
  
  private ScriptProcessor proc;
  
  private CodeViewer cview;
  
  private Config config;
  
  private String script;
  
  private File prevDir;
  
  private Timer discTimer;

  
  /**
   * Creates new form J3270
   */
  public J3270() {
    initComponents();
    address = SERVER_ADDR;
    port = SERVER_PORT;
    sess = new Session();
    this.setLocationRelativeTo(null);
    this.addKeyListener(grid);
    copy = new TextCopy();
    driver = new JDriver(this, grid);
    this.setLookAndFeel();
    script = null;
    prevDir = null;
    
    discTimer = new Timer(TEN_MIN, 
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            J3270.this.disconnect();
          }
        }
    );
    discTimer.setRepeats(true);
    
    lst = new LinkedList<WindowListener>();
    proc = new ScriptProcessor(driver);
    cview = new CodeViewer(this, false);
    gen = new ScriptGenerator(cview);
    
    KeyboardFocusManager
        .getCurrentKeyboardFocusManager()
        .setDefaultFocusTraversalKeys(
        KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, 
        Collections.EMPTY_SET);
    KeyboardFocusManager
        .getCurrentKeyboardFocusManager()
        .setDefaultFocusTraversalKeys(
        KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, 
        Collections.EMPTY_SET);
    this.addWindowListener(new java.awt.event.WindowListener() {
      @Override
      public void windowClosing(WindowEvent e) {
        sess.close();
      }
      @Override
      public void windowIconified(WindowEvent e) {
        cview.setVisible(false);
      }
      @Override
      public void windowDeiconified(WindowEvent e) {
        cview.setVisible(scriptButton.isSelected());
      }
      @Override public void windowOpened(WindowEvent e) {}
      @Override public void windowClosed(WindowEvent e) {}
      @Override public void windowActivated(WindowEvent e) {
        grid.requestFocus();
        grid.requestFocusInWindow();
      }
      @Override public void windowDeactivated(WindowEvent e) {}
    });
    this.addComponentListener(new ComponentListener() {
      @Override
      public void componentResized(ComponentEvent e) {
        cview.setLocation(getLocationOnScreen().x 
            + getWidth() + 2, getLocationOnScreen().y);
      }
      @Override
      public void componentMoved(ComponentEvent e) {
        cview.setLocation(getLocationOnScreen().x 
            + getWidth() + 2, getLocationOnScreen().y);
      }
      @Override
      public void componentShown(ComponentEvent e) {
        cview.setVisible(scriptButton.isSelected());
        if(script != null && !script.trim().isEmpty()) {
          status("Executing Startup Script: "+ script);
          cview.open(new File(script));
          cview.exec();
        }
      }
      @Override
      public void componentHidden(ComponentEvent e) {
        cview.setVisible(false);
      }
    });
    
    config = new Config(FILE_CONF);
    if(config.isEmpty())
      setDefaultConfig();
    else
      readConfig();
  }
  
  
  public void startRecord() {
    this.addListener(gen);
    grid.addListener(gen);
  }
  
  
  public void stopRecord() {
    this.lst.clear();
    grid.listeners().clear();
  }
  
  
  public ScriptGenerator scriptGen() {
    return gen;
  }
  
  
  public ScriptProcessor scriptProc() {
    return proc;
  }
  
  
  private void setDefaultConfig() {
    config.setComment(COMMENT);
    putGridFont(grid.getViewFont());
    putConnection(address, port);
    putStartupScript(" ");
  }
  
  
  private void readConfig() {
    grid.setViewFont(readGridFont());
    readConnection();
    script = readStartupScript();
  }
  
  
  private void putGridFont(Font f) {
    if(f == null) return;
    config.put(FONT_NAME, f.getFamily())
        .put(FONT_SIZE, f.getSize())
        .put(FONT_STYLE, f.getStyle())
        .save();
  }
  
  
  private Font readGridFont() {
    return new Font(
        config.get(FONT_NAME),
        config.getInt(FONT_STYLE),
        config.getInt(FONT_SIZE));
  }
  
  
  private void readConnection() {
    address = config.get(CONN_HOST);
    port = config.getInt(CONN_PORT);
  }
  
  
  private void putConnection(String host, int port) {
    if(host == null || port <= 0) return;
    config.put(CONN_HOST, host)
        .put(CONN_PORT, port)
        .save();
  }
  
  
  private void putStartupScript(String path) {
    if(path == null) return;
    config.put(STARTUP_SCRIPT, path).save();
  }
  
  
  private String readStartupScript() {
    return config.get(STARTUP_SCRIPT);
  }
  
  
  public void addListener(WindowListener wl) {
    if(wl != null) lst.add(wl);
  }
  
  
  public void connect() {
    this.connect(address, port);
  }
  
  
  public void connect(String host, int port) {
    if(host == null || host.isEmpty()) {
      String s = "Invalid host to connect ["+ host+ "]";
      error(s);
      throw new IllegalArgumentException(s);
    }
    if(port <= 1 && port > Short.MAX_VALUE) {
      String s = "Invalid port to connect ["+ port+ "]";
      error(s);
      throw new IllegalArgumentException(s);
    }
    this.address = host;
    this.port = port;
    sess.connect(host, port, grid);
    grid.requestFocus();
    
    status("OK. Connected to ["+ host+ ":"+ port+ "]");
    for(WindowListener wl : lst)
      wl.connected(host, port);
    
    if(!discTimer.isRunning()) {
      discTimer.start();
    }
  }
  
  
  public void disconnect() {
    sess.close();
    sess = new Session();
    grid.clearScreen();
    status("Disconnected");
    for(WindowListener wl : lst)
      wl.disconnected();
    grid.requestFocus();
    
    if(discTimer.isRunning())
      discTimer.stop();
  }
  
  
  public void notifyAction() {
    if(discTimer.isRunning())
      discTimer.restart();
  }
  
  
  private void setLookAndFeel() {
    try {
      UIManager.setLookAndFeel(new NimbusLookAndFeel());
    } catch (UnsupportedLookAndFeelException ex) {}
    SwingUtilities.updateComponentTreeUI(this);
  }
  
  
  public JGrid getDisplay() {
    return grid;
  }
  
  
  public Session session() {
    return sess;
  }
  
  
  public JDriver driver() {
    return driver;
  }
  
  
  public CodeViewer getCodeViewer() {
    return cview;
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
    connButton = new javax.swing.JButton();
    discButton = new javax.swing.JButton();
    pasteButton = new javax.swing.JButton();
    appendButton = new javax.swing.JButton();
    copyButton = new javax.swing.JButton();
    networkButton = new javax.swing.JButton();
    scriptButton = new javax.swing.JToggleButton();
    status = new javax.swing.JLabel();
    cursorLabel = new javax.swing.JLabel();
    grid = new us.pserver.j3270.JGrid(this);
    menuBar = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    saveImgMenu = new javax.swing.JMenuItem();
    saveTxtMenu = new javax.swing.JMenuItem();
    appendImgMenu = new javax.swing.JMenuItem();
    quitMenu = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    copyMenu = new javax.swing.JMenuItem();
    copyAppendMenu = new javax.swing.JMenuItem();
    cutMenu = new javax.swing.JMenuItem();
    pasteMenu = new javax.swing.JMenuItem();
    findMenu = new javax.swing.JMenuItem();
    jSeparator2 = new javax.swing.JPopupMenu.Separator();
    insertwaitMenu = new javax.swing.JMenuItem();
    insertgetMenu = new javax.swing.JMenuItem();
    jMenu1 = new javax.swing.JMenu();
    networkMenu = new javax.swing.JMenuItem();
    startupScriptMenu = new javax.swing.JMenuItem();
    fontMenu = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    b64Menu = new javax.swing.JMenuItem();
    jMenu2 = new javax.swing.JMenu();
    connectMenu = new javax.swing.JMenuItem();
    diconnectMenu = new javax.swing.JMenuItem();
    aboutMenu = new javax.swing.JMenu();
    aboutButton = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("J3270");
    setIconImage(new ImageIcon(getClass().getResource("/us/pserver/j3270/images/icon24.png")).getImage());

    jPanel1.setBackground(new java.awt.Color(238, 238, 238));

    connButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/connect_24.png"))); // NOI18N
    connButton.setText("Connect");
    connButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        connButtonActionPerformed(evt);
      }
    });

    discButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/disconnect_24.png"))); // NOI18N
    discButton.setText("Disconnect");
    discButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        discButtonActionPerformed(evt);
      }
    });

    pasteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/paste_24.png"))); // NOI18N
    pasteButton.setText("Paste");
    pasteButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        pasteButtonActionPerformed(evt);
      }
    });

    appendButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/append_24.png"))); // NOI18N
    appendButton.setText("Append");
    appendButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        appendButtonActionPerformed(evt);
      }
    });

    copyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/copy_24.png"))); // NOI18N
    copyButton.setText("Copy");
    copyButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        copyButtonActionPerformed(evt);
      }
    });

    networkButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/network_24.png"))); // NOI18N
    networkButton.setText("Network");
    networkButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        networkButtonActionPerformed(evt);
      }
    });

    scriptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/j3270/images/gears_24.png"))); // NOI18N
    scriptButton.setText("Script");
    scriptButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        scriptButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(copyButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(appendButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(pasteButton)
        .addGap(18, 18, 18)
        .addComponent(scriptButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(connButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(discButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(networkButton)
        .addGap(12, 12, 12))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(connButton)
          .addComponent(discButton)
          .addComponent(pasteButton)
          .addComponent(appendButton)
          .addComponent(copyButton)
          .addComponent(networkButton)
          .addComponent(scriptButton))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    status.setBackground(new java.awt.Color(238, 238, 238));
    status.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
    status.setForeground(new java.awt.Color(45, 90, 180));
    status.setText("Disconnected");
    status.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    status.setOpaque(true);

    cursorLabel.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
    cursorLabel.setForeground(new java.awt.Color(30, 30, 130));
    cursorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    cursorLabel.setText("[ 1 x 1 ]");

    fileMenu.setText("File");

    saveImgMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
    saveImgMenu.setText("Save Image");
    saveImgMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveImgMenuActionPerformed(evt);
      }
    });
    fileMenu.add(saveImgMenu);

    saveTxtMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    saveTxtMenu.setText("Save Text");
    saveTxtMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveTxtMenuActionPerformed(evt);
      }
    });
    fileMenu.add(saveTxtMenu);

    appendImgMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
    appendImgMenu.setText("Append Text");
    appendImgMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        appendTextMenuActionPerformed(evt);
      }
    });
    fileMenu.add(appendImgMenu);

    quitMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
    quitMenu.setText("Quit");
    quitMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        quitMenuActionPerformed(evt);
      }
    });
    fileMenu.add(quitMenu);

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

    copyAppendMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
    copyAppendMenu.setText("Copy Append");
    copyAppendMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        copyAppendMenuActionPerformed(evt);
      }
    });
    editMenu.add(copyAppendMenu);

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

    findMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
    findMenu.setText("Find Text");
    findMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        findMenuActionPerformed(evt);
      }
    });
    editMenu.add(findMenu);
    editMenu.add(jSeparator2);

    insertwaitMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
    insertwaitMenu.setText("Insert Script WaitFor");
    insertwaitMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        insertwaitMenuActionPerformed(evt);
      }
    });
    editMenu.add(insertwaitMenu);

    insertgetMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
    insertgetMenu.setText("Insert Script GetText");
    insertgetMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        insertgetMenuActionPerformed(evt);
      }
    });
    editMenu.add(insertgetMenu);

    menuBar.add(editMenu);

    jMenu1.setText("Settings");

    networkMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
    networkMenu.setText("Network");
    networkMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        networkMenuActionPerformed(evt);
      }
    });
    jMenu1.add(networkMenu);

    startupScriptMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
    startupScriptMenu.setText("Startup Script");
    startupScriptMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        startupScriptMenuActionPerformed(evt);
      }
    });
    jMenu1.add(startupScriptMenu);

    fontMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK));
    fontMenu.setText("Font Select");
    fontMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fontMenuActionPerformed(evt);
      }
    });
    jMenu1.add(fontMenu);
    jMenu1.add(jSeparator1);

    b64Menu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
    b64Menu.setText("Base64 Coder");
    b64Menu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        b64MenuActionPerformed(evt);
      }
    });
    jMenu1.add(b64Menu);

    menuBar.add(jMenu1);

    jMenu2.setText("Network");

    connectMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
    connectMenu.setText("Connect");
    connectMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        connectMenuActionPerformed(evt);
      }
    });
    jMenu2.add(connectMenu);

    diconnectMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
    diconnectMenu.setText("Disconnect");
    diconnectMenu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        diconnectMenuActionPerformed(evt);
      }
    });
    jMenu2.add(diconnectMenu);

    menuBar.add(jMenu2);

    aboutMenu.setText("About");
    aboutMenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
    aboutMenu.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        aboutMenuMouseClicked(evt);
      }
    });

    aboutButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    aboutButton.setText("About");
    aboutButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        aboutButtonActionPerformed(evt);
      }
    });
    aboutMenu.add(aboutButton);

    menuBar.add(aboutMenu);

    setJMenuBar(menuBar);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addComponent(cursorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
      .addComponent(grid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(grid, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(cursorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  
  public boolean checkLockedScreen() {
    if(grid.isLocked()) {
      error("Screen is Locked");
      return true;
    }
    return false;
  }
  
  
  private void connButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connButtonActionPerformed
    status("Connecting ...");
    if(address == null || address.isEmpty() 
        || port < 1 || port > Short.MAX_VALUE) {
      error("Invalid Server Address ["
          + (address == null || address.isEmpty() 
          ? "null" : address)
          + ":"+ port+ "]");
    }
    this.connect(address, port);
  }//GEN-LAST:event_connButtonActionPerformed

  
  private void discButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discButtonActionPerformed
    disconnect();
  }//GEN-LAST:event_discButtonActionPerformed

  
  private void saveImgMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveImgMenuActionPerformed
    if(checkLockedScreen()) return;
    File f = selectFile("Save Screen Image", "jpg", false);
    if(f != null) this.saveImage(f);
  }//GEN-LAST:event_saveImgMenuActionPerformed

  
  private void saveTxtMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTxtMenuActionPerformed
    if(checkLockedScreen()) return;
    String text = grid.getSelected();
    if(text == null) text = getScreenln();
    File f = selectFile("Save Screen Text", "txt", false);
    if(f != null) this.saveText(f, text, false);
  }//GEN-LAST:event_saveTxtMenuActionPerformed

  
  private void appendTextMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appendTextMenuActionPerformed
    if(checkLockedScreen()) return;
    String text = grid.getSelected();
    if(text == null) text = getScreenln();
    File f = selectFile("Append Screen Text", "txt", false);
    if(f != null) this.saveText(f, text, true);
  }//GEN-LAST:event_appendTextMenuActionPerformed

  
  private void quitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuActionPerformed
    System.exit(0);
  }//GEN-LAST:event_quitMenuActionPerformed

  
  private void copyAppendMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyAppendMenuActionPerformed
    if(checkLockedScreen()) return;
    String sel = grid.getSelected();
    if(sel == null) sel = getScreenln();
    this.copyAppend(sel);
  }//GEN-LAST:event_copyAppendMenuActionPerformed

  
  private void copyMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuActionPerformed
    if(checkLockedScreen()) return;
    String sel = grid.getSelected();
    if(sel == null) sel = getScreenln();
    this.copy(sel);
  }//GEN-LAST:event_copyMenuActionPerformed

  
  private void cutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutMenuActionPerformed
    if(checkLockedScreen()) return;
    String str = grid.clearSelected();
    if(str != null && !str.isEmpty()) {
      this.copy(str);
      status("cut");
      for(WindowListener wl : lst)
        wl.cut(str);
    }
  }//GEN-LAST:event_cutMenuActionPerformed

  
  private void pasteMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenuActionPerformed
    if(checkLockedScreen()) return;
    this.paste();
  }//GEN-LAST:event_pasteMenuActionPerformed

  
  private void findMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findMenuActionPerformed
    if(checkLockedScreen()) return;
    String str = JOptionPane.showInputDialog(this, 
        "Inform the text to search:", 
        "Find", JOptionPane.PLAIN_MESSAGE);
    if(str == null) {
      error("No text informed");
      return;
    }
    Cursor cs = grid.search(str);
    if(cs == null) {
      error("Text not found");
      return;
    }
    status("Found on ["+ cs.row()+ "x"+ cs.column()+"]");
    grid.clearCursor();
    grid.setCursorPosition(cs);
  }//GEN-LAST:event_findMenuActionPerformed

  
  private void fontMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontMenuActionPerformed
    if(checkLockedScreen()) return;
    FontSelector fs = new FontSelector(this, true, grid.getViewFont());
    fs.setVisible(true);
    grid.setViewFont(fs.getSelectedFont());
    putGridFont(fs.getSelectedFont());
  }//GEN-LAST:event_fontMenuActionPerformed

  
  private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
    copyMenuActionPerformed(evt);
    grid.requestFocus();
  }//GEN-LAST:event_copyButtonActionPerformed

  
  private void appendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appendButtonActionPerformed
    copyAppendMenuActionPerformed(evt);
    grid.requestFocus();
  }//GEN-LAST:event_appendButtonActionPerformed

  
  private void pasteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteButtonActionPerformed
    pasteMenuActionPerformed(evt);
    grid.requestFocus();
  }//GEN-LAST:event_pasteButtonActionPerformed

  
  private void networkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_networkButtonActionPerformed
    networkMenuActionPerformed(evt);
    grid.requestFocus();
  }//GEN-LAST:event_networkButtonActionPerformed

  
  private void networkMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_networkMenuActionPerformed
    NetworkDialog dlg = new NetworkDialog(this, true);
    dlg.setAddress(address);
    dlg.setPort(port);
    dlg.setVisible(true);
    address = dlg.getAddress();
    port = dlg.getPort();
    if(address == null || address.isEmpty() 
        || port < 1 || port > Short.MAX_VALUE) {
      error("Invalid Server Address ["
          + (address == null || address.isEmpty() 
          ? "null" : address)
          + ":"+ port+ "]");
    }
    else {
      putConnection(address, port);
      status("Server Address [ "+ address+ ":"
          + String.valueOf(port)+ " ]");
    }
  }//GEN-LAST:event_networkMenuActionPerformed

  
  private void connectMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectMenuActionPerformed
    connButtonActionPerformed(evt);
  }//GEN-LAST:event_connectMenuActionPerformed

  
  private void diconnectMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diconnectMenuActionPerformed
    discButtonActionPerformed(evt);
  }//GEN-LAST:event_diconnectMenuActionPerformed

  
  private void b64MenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b64MenuActionPerformed
    Base64Gui gui = new Base64Gui(this);
    gui.setVisible(true);
  }//GEN-LAST:event_b64MenuActionPerformed

  
  private void scriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scriptButtonActionPerformed
    cview.setVisible(scriptButton.isSelected());
  }//GEN-LAST:event_scriptButtonActionPerformed

  
  private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed
    new AboutDialog(this, true).setVisible(true);
  }//GEN-LAST:event_aboutButtonActionPerformed

  
  private void aboutMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutMenuMouseClicked
    new AboutDialog(this, true).setVisible(true);
  }//GEN-LAST:event_aboutMenuMouseClicked

  
  private void insertwaitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertwaitMenuActionPerformed
    cview.insertWaitFor();
  }//GEN-LAST:event_insertwaitMenuActionPerformed

  
  private void insertgetMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertgetMenuActionPerformed
    cview.insertGetText();
  }//GEN-LAST:event_insertgetMenuActionPerformed

  
  private void startupScriptMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startupScriptMenuActionPerformed
    if(script != null && !script.isEmpty())
      prevDir = new File(script).getParentFile();
    File f = selectFile("Select Startup Script", "txt", false);
    if(f != null) {
      status("Startup Script defined: "+ f.toString());
      script = f.toString();
    }
    else script = " ";
    putStartupScript(script);
  }//GEN-LAST:event_startupScriptMenuActionPerformed

  
  
  public void copy(String str) {
    if(str == null) {
      error("No text selected");
      return;
    }
    status("copy");
    copy.setText(str).putInClipboard();
    for(WindowListener wl : lst)
      wl.copy(str);
  }
  
  
  public void copyAppend(String str) {
    if(str == null) {
      error("No text selected");
      return;
    }
    status("copy");
    copy.append(str).putInClipboard();
    for(WindowListener wl : lst)
      wl.copy(str);
  }
  
  
  public void paste() {
    copy.setFromClipboard();
    String cont = copy.getText();
    grid.setTextAtCursor(cont);
    for(WindowListener wl : lst)
      wl.paste(cont);
  }
  
  
  public File selectFile(String title, String extension, boolean directories) {
    if(title == null || extension == null)
      return null;
    JFileChooser chooser = new JFileChooser(prevDir);
    chooser.setDialogTitle(title);
    if(directories)
      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    else
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    
    FileNameExtensionFilter filter = 
        new FileNameExtensionFilter(extension, extension);
    chooser.setFileFilter(filter);
    if(chooser.showSaveDialog(this) 
        != JFileChooser.APPROVE_OPTION) {
      error("Invalid File Selected");
      return null;
    }
    File f = chooser.getSelectedFile();
    if(f.isFile())
      prevDir = f.getParentFile();
    else prevDir = f;
    return f;
  }
  
  
  public void saveImage(File f) {
    if(f == null) error("Invalid file ["+ f+ "]");
    BufferedImage img = new BufferedImage(
        grid.getWidth(), grid.getHeight(), 
        BufferedImage.TYPE_INT_RGB);
    grid.paint(img.createGraphics());
    try {
      ImageIO.write(img, "jpg", f);
      status("Screen image saved ["+ f+ "]");
    } catch(IOException e) {
      error("Error saving image ["+ e.getMessage()+ "]");
    }
  }
  
  
  public String getScreenln() {
    return getScreen(true);
  }  
  
  
  public String getScreen() {
    return getScreen(false);
  }
  
  
  private String getScreen(boolean doln) {
    StringBuilder sb = new StringBuilder();
    String ln = (File.separatorChar == '/' ? "\n" : "\r\n");
    Component[] cs = grid.getComponents();
    if(cs == null || cs.length == 0) {
      error("There is no text in screen");
      return null;
    }
    
    for(int i = 0; i < cs.length; i++) {
      JChar jc = (JChar) cs[i];
      sb.append(jc.getText());
      if(i % 80 == 0 && doln) sb.append(ln);
    }
    return sb.toString();
  }  
  
  
  public void saveText(File f, String text, boolean append) {
    if(f == null) {
      error("Invalid file ["+ f+ "]");
      return;
    }
    if(text == null) {
      error("No text to save");
      return;
    }
    
    try {
      BufferedWriter bw = new BufferedWriter(
        new FileWriter(f, append));
      if(append) bw.newLine();
      bw.write(text);
      bw.flush();
      status("Text saved ["+ f+ "]");
      bw.close();
    }
    catch(IOException e) {
      error("Error saving text ["+ e.getMessage()+ "]");
    }
  }  
  
  
  public void error(final String error) {
    new Thread(new Runnable() {
      public void run() {
        status.setForeground(STATUS_ERROR_COLOR);
        status.setText(error);
        blinkStatus(4);
      }
    }).start();
  }
  
  
  public void status(final String sts) {
    new Thread(new Runnable() {
      public void run() {
        status.setForeground(STATUS_DEF_COLOR);
        status.setBackground(new Color(238, 238, 238));
        status.setText(sts);
        status.repaint();
      }
    }).start();
  }
  
  
  public void blinkStatus(int times) {
    times = (times < 1 ? 1 : times);
    Color obg = status.getBackground();
    Color ofg = status.getForeground();
    boolean ordered = false;
    status.setOpaque(true);
    for(int i = 0; i < times; i++) {
      status.setBackground((ordered ? obg : ofg));
      status.setForeground((ordered ? ofg : obg));
      status.repaint();
      ordered = !ordered;
      try { Thread.sleep(200); }
      catch(InterruptedException e) {
        e.printStackTrace();
      }
    }
    status.setBackground(obg);
    status.setForeground(ofg);
    status.repaint();
  }
  
  
  public void showCursor(Cursor cur) {
    if(cur == null) return;
    String lbl = "[ "+ String.valueOf(cur.row())
        + " x " + String.valueOf(cur.column())+ " ]";
    cursorLabel.setText(lbl);
    cursorLabel.repaint();
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
      java.util.logging.Logger.getLogger(J3270.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(J3270.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(J3270.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(J3270.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
        //</editor-fold>

    final SplashScreen splash = SplashScreen.getSplashScreen();
    new Thread(new Runnable() {
      public void run() {
        try { Thread.sleep(3000); }
        catch(InterruptedException e) {}
        if(splash != null) splash.close();
      }
    }).start();
    
    J3270 j3270 = new J3270();
    j3270.setVisible(true);
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem aboutButton;
  private javax.swing.JMenu aboutMenu;
  private javax.swing.JButton appendButton;
  private javax.swing.JMenuItem appendImgMenu;
  private javax.swing.JMenuItem b64Menu;
  private javax.swing.JButton connButton;
  private javax.swing.JMenuItem connectMenu;
  private javax.swing.JMenuItem copyAppendMenu;
  private javax.swing.JButton copyButton;
  private javax.swing.JMenuItem copyMenu;
  private javax.swing.JLabel cursorLabel;
  private javax.swing.JMenuItem cutMenu;
  private javax.swing.JMenuItem diconnectMenu;
  private javax.swing.JButton discButton;
  private javax.swing.JMenu editMenu;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JMenuItem findMenu;
  private javax.swing.JMenuItem fontMenu;
  private us.pserver.j3270.JGrid grid;
  private javax.swing.JMenuItem insertgetMenu;
  private javax.swing.JMenuItem insertwaitMenu;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JPopupMenu.Separator jSeparator2;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JButton networkButton;
  private javax.swing.JMenuItem networkMenu;
  private javax.swing.JButton pasteButton;
  private javax.swing.JMenuItem pasteMenu;
  private javax.swing.JMenuItem quitMenu;
  private javax.swing.JMenuItem saveImgMenu;
  private javax.swing.JMenuItem saveTxtMenu;
  private javax.swing.JToggleButton scriptButton;
  private javax.swing.JMenuItem startupScriptMenu;
  private javax.swing.JLabel status;
  // End of variables declaration//GEN-END:variables
}
