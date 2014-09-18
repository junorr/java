/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.sys;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author juno
 */
public class SysConfig {
  
  public static final String FILENAME = "./sys.conf";
  
  public static final String COMMENT = "Syscheck Config";
  
  public static final int DEFAULT_DB_MAX_SIZE = 512;
  
  public static final int DEFAULT_INTERFACE_UPDATE_INTERVAL = 25;
  
  public static final int DEFAULT_SYSCHECK_INTERVAL = 1800;
  
  
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
  
  private String configFile;
  
  
  public SysConfig() {
    configFile = FILENAME;
    dbMaxSize = DEFAULT_DB_MAX_SIZE;
    syscheckInterval = DEFAULT_SYSCHECK_INTERVAL;
    interfaceInterval = DEFAULT_INTERFACE_UPDATE_INTERVAL;
    conf = new Config(configFile);
    conf.setComment(COMMENT);
    init();
  }
  
  
  private void init() {
    Path p = Paths.get(FILENAME);
    if(Files.exists(p))
      this.load();
    else 
      this.save();
  }
  
  
  public void save() {
    conf.put(KEY_DB_MAX_SIZE, dbMaxSize);
    conf.put(KEY_SYSCHECK_INTERVAL, syscheckInterval);
    conf.put(KEY_INTERFACE_UPDATE_INTERVAL, interfaceInterval);
    conf.save();
  }
  
  
  public void load() {
    conf.load();
    dbMaxSize = conf.getInt(KEY_DB_MAX_SIZE);
    syscheckInterval = conf.getInt(KEY_SYSCHECK_INTERVAL);
    interfaceInterval = conf.getInt(KEY_INTERFACE_UPDATE_INTERVAL);
  }


  public int getDbMaxSize() {
    return dbMaxSize;
  }


  public void setDbMaxSize(int dbMaxSize) {
    this.dbMaxSize = dbMaxSize;
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


  public String getConfigFile() {
    return configFile;
  }


  public void setConfigFile(String configFile) {
    this.configFile = configFile;
    conf.setFile(configFile);
    this.init();
  }
  
  
  public boolean isSyscheckRunning() {
    try {
      DB.openSocketClient().close();
      return true;
    } catch(Exception ex) {
      return false;
    }
  }
  
}
