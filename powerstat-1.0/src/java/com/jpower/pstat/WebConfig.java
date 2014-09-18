/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pstat;

import com.jpower.sys.Config;
import com.jpower.sys.DB;
import com.jpower.sys.SystemRun;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author juno
 */
@SessionScoped
@ManagedBean
public class WebConfig {
  
  public static final String WEBCONFIG_FILE = WebConfig.class
      .getResource("conf/rsc").toString()
      .replace("rsc", "webconfig.conf").replace("file:", "");
  
  
  public static final String WEBCONFIG_COMMENT = "Powerstat WebConfig";
  
  
  public static final String SYSCHECK_TAR = WebConfig.class
      .getResource("conf/rsc").toString()
      .replace("rsc", "syscheck.tar.gz").replace("file:", "");
  
  
  public static final String INSTALL_SH = WebConfig.class
      .getResource("conf/rsc").toString()
      .replace("rsc", "install.sh").replace("file:", "");
  
  
  public static final String 
      
      OS_NAME = "os.name",
      
      OS_ARCH = "os.arch",
      
      OS_VERSION = "os.version",
      
      JAVA_HOME = "java.home",
      
      JAVA_VENDOR = "java.vendor",
      
      JAVA_VERSION = "java.version";
  
  
  public static final int DEFAULT_DB_MAX_SIZE = 512;
  
  public static final int DEFAULT_INTERFACE_UPDATE_INTERVAL = 15;
  
  public static final int DEFAULT_SYSCHECK_INTERVAL = 1800;
  
  public static final String DEFAULT_SYSCHECK_DIR = "/opt/syscheck";
  
  
  public static final String
      
      KEY_SYSCHECK_DIR = "SYSCHECK_DIR",
      
      KEY_JAVAHOME = "JAVA_HOME",
      
      KEY_SYSCHECK_INTERVAL = "SYSCHECK_INTERVAL_(SEC)",
      
      KEY_INTERFACE_UPDATE_INTERVAL = "INTERFACE_UPDATE_INTERVAL_(SEC)",
      
      KEY_DB_MAX_SIZE = "DB_MAX_SIZE_(MB)";
      
      
  private Config conf;
  
  private int dbMaxSize;
  
  private int syscheckInterval;
  
  private int interfaceInterval;
  
  private String javaHome;
  
  private String syscheckDir;
  
  private boolean asSudo;
  
  private String sudoPassword;
  
  private String installOut;
  
  @ManagedProperty(value="#{security}")
  private SecurityController security;
  
  @ManagedProperty(value="#{loginController}")
  private LoginController login;
  
  
  public WebConfig() {
    conf = new Config(WEBCONFIG_FILE);
    conf.setComment(WEBCONFIG_COMMENT);
    javaHome = System.getProperty(JAVA_HOME);
    dbMaxSize = DEFAULT_DB_MAX_SIZE;
    syscheckInterval = DEFAULT_SYSCHECK_INTERVAL;
    interfaceInterval = DEFAULT_INTERFACE_UPDATE_INTERVAL;
    syscheckDir = DEFAULT_SYSCHECK_DIR;
    asSudo = false;
    sudoPassword = "";
    installOut = "";
    login = null;
    init();
  }
  
  
  private void init() {
    Path p = Paths.get(WEBCONFIG_FILE);
    if(Files.exists(p))
      this.load();
    else 
      this.save();
  }
  
  
  public void save() {
    conf.put(KEY_JAVAHOME, javaHome);
    conf.put(KEY_SYSCHECK_DIR, syscheckDir);
    conf.put(KEY_DB_MAX_SIZE, dbMaxSize);
    conf.put(KEY_SYSCHECK_INTERVAL, syscheckInterval);
    conf.put(KEY_INTERFACE_UPDATE_INTERVAL, interfaceInterval);
    conf.save();
  }
  
  
  public void load() {
    conf.load();
    javaHome = conf.get(KEY_JAVAHOME);
    syscheckDir = conf.get(KEY_SYSCHECK_DIR);
    dbMaxSize = conf.getInt(KEY_DB_MAX_SIZE);
    syscheckInterval = conf.getInt(KEY_SYSCHECK_INTERVAL);
    interfaceInterval = conf.getInt(KEY_INTERFACE_UPDATE_INTERVAL);
  }


  public SecurityController getSecurity() {
    return security;
  }


  public void setSecurity(SecurityController security) {
    this.security = security;
  }


  public int getDbMaxSize() {
    return dbMaxSize;
  }


  public void setDbMaxSize(int dbMaxSize) {
    this.dbMaxSize = dbMaxSize;
  }


  public String getJavaHome() {
    return javaHome;
  }


  public void setJavaHome(String javaHome) {
    this.javaHome = javaHome;
  }


  public String getSyscheckDir() {
    return syscheckDir;
  }


  public void setSyscheckDir(String syscheckDir) {
    this.syscheckDir = syscheckDir;
  }


  public int getSyscheckInterval() {
    return syscheckInterval;
  }


  public void setSyscheckInterval(int interval) {
    this.syscheckInterval = interval;
  }


  public int getInterfaceInterval() {
    return interfaceInterval;
  }


  public void setInterfaceInterval(int interfaceInterval) {
    this.interfaceInterval = interfaceInterval;
  }


  public String getInstallOut() {
    return installOut.replace("\n", "<br/>").replaceAll("\n", "<br/>");
  }
  
  
  public boolean isSyscheckRunning() {
    try {
      DB.openSocketClient().close();
      return true;
    } catch(Exception ex) {
      return false;
    }
  }


  public String getSudoPassword() {
    return "";
  }


  public void setSudoPassword(String pass) {
    if(pass == null || pass.isEmpty()
        || pass.equals("password"))
      return;
    pass = security.decrypt(pass);
    if(pass == null || pass.isEmpty())
      return;
    sudoPassword = pass;
  }


  public boolean isAsSudo() {
    return asSudo;
  }


  public void setAsSudo(boolean asSudo) {
    this.asSudo = asSudo;
  }


  public LoginController getLogin() {
    return login;
  }


  public void setLogin(LoginController login) {
    this.login = login;
  }
  
  
  public String getOsName() {
    return System.getProperty(OS_NAME);
  }
  
  
  public boolean isDefaultAdminUser() {
    return login.isLogged() && login.getUser() != null
        && login.getUser().getEmail().equals(
        LoginController.DEFAULT_USER.getEmail());
  }
  
  
  public String getOsArch() {
    return System.getProperty(OS_ARCH);
  }
  
  
  public String getOsVersion() {
    return System.getProperty(OS_VERSION);
  }
  
  
  public String getJavaVersion() {
    return System.getProperty(JAVA_VERSION);
  }
  
  
  public String getJavaVendor() {
    return System.getProperty(JAVA_VENDOR);
  }
  
  
  public boolean isServiceInstalled() {
    String local = syscheckDir;
    if(local.endsWith("/"))
      local += "service.sh";
    else
      local += "/service.sh";
    
    return Files.exists(Paths.get(local));
  }
  
  
  public void restartSyscheck() {
    if(!isServiceInstalled())
      return;
    
    String cmd = syscheckDir;
    if(cmd.endsWith("/"))
      cmd += "service.sh";
    else
      cmd += "/service.sh";
    
    String[] args;
    
    if(asSudo && sudoPassword != null 
        && !sudoPassword.isEmpty()) {
      
      if(isSyscheckRunning()) {
        args = new String[] {cmd, "sudo", sudoPassword, "--kill"};
      } 
      else {
        args = new String[] {cmd, "sudo", sudoPassword, "--start"};
      }
    }
    else {
      if(isSyscheckRunning()) {
        args = new String[] {cmd, "--kill"};
      } else {
        args = new String[] {cmd, "--start"};
      }
    }
    
    try { Runtime.getRuntime().exec(args); }
    catch(IOException ex) {}
  }
  
  
  public void installSyscheck() {
    if(isServiceInstalled() || syscheckDir == null
        || syscheckDir.isEmpty())
      return;
    
    String cmd = INSTALL_SH;
    String[] args;
    
    if(asSudo && sudoPassword != null 
        && !sudoPassword.isEmpty()) {
      args = new String[] {"sudo", sudoPassword, "-f", syscheckDir};
    }
    else {
      args = new String[] {"-f", syscheckDir};
    }
    
    SystemRun run = new SystemRun(cmd, args);
    run.run();
    installOut = run.getOutput();
  }
  
}
