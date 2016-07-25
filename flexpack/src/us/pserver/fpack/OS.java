/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fpack;

/**
 *
 * @author juno
 */
public enum OS {
  
  WINDOWS(), 
  UNIX(),
  MAC();
  
  
  public static final String NAME = System.getProperty("os.name");
  
  public static final OS CURRENT = (
      NAME.toLowerCase().contains("win") ? WINDOWS : (
      NAME.toLowerCase().contains("mac") ? MAC : UNIX)
  );
  
  
  public static boolean isWindows() {
    return CURRENT == WINDOWS;
  }
  
  
  public static boolean isMacOS() {
    return CURRENT == MAC;
  }
  
  
  public static boolean isUnix() {
    return CURRENT == UNIX;
  }
  
}
