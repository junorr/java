/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.wdog;

import java.io.File;


/**
 *
 * @author juno
 */
public class FileChangeWatcher implements Watcher {
  
  private File file;
  
  private boolean changed;
  
  private long lastModified;
  
  
  public FileChangeWatcher() {
    file = null;
    changed = false;
    lastModified = 0;
  }
  
  
  public FileChangeWatcher(String file) {
    this();
    setFile(file);
  }
  
  
  public FileChangeWatcher(File f) {
    this();
    setFile(f);
  }
  
  
  public File getFile() {
    return file;
  }
  
  
  public long lasModified() {
    return lastModified;
  }
  
  
  public FileChangeWatcher setFile(String f) {
    if(f == null || f.trim().isEmpty())
      throw new IllegalArgumentException("Invalid file name: "+ f);
    return setFile(new File(f));
  }
  
  
  public FileChangeWatcher setFile(File f) {
    if(!f.exists())
      throw new IllegalArgumentException("File does not exists: "+ f);
    this.file = f;
    lastModified = file.lastModified();
    return this;
  }


  @Override
  public boolean watch() {
    long mod = file.lastModified();
    changed = lastModified != mod;
    lastModified = mod;
    return changed;
  }


  @Override
  public boolean isChanged() {
    return changed;
  }


  @Override
  public Object getData() {
    return file;
  }
  
}
