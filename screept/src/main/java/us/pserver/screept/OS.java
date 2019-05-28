/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Optional;


/**
 *
 * @author juno
 */
public enum OS {
  
  WINDOWS, UNIX, MAC, SOLARIS;
  
  public static final String OS_NAME = System.getProperty("os.name");
  
  public static final String OS_VERSION = System.getProperty("os.version");
  
  public static final String OS_ARCH = System.getProperty("os.arch");
  
  
  public static OS getOS() {
    String name = OS_NAME.toLowerCase();
    if(name.contains("win")) {
      return WINDOWS;
    }
    else if(name.contains("nix") || name.contains("nux") || name.contains("aix")) {
      return UNIX;
    }
    else if(name.contains("mac")) {
      return MAC;
    }
    else if(name.contains("sunos")) {
      return SOLARIS;
    } 
    else {
      throw new RuntimeException("Unknown os.name = " + OS_NAME);
    }
  }
  
  
  public static boolean isWindows() {
    return getOS() == WINDOWS;
  }
  
  public static <T> Optional<T> ifWindows(T t) {
    return isWindows() ? Optional.of(t) : Optional.empty();
  }
  
  
  public static boolean isUnix() {
    return getOS() == UNIX;
  }
  
  public static <T> Optional<T> ifUnix(T t) {
    return isUnix() ? Optional.of(t) : Optional.empty();
  }
  
  
  public static boolean isMac() {
    return getOS() == MAC;
  }
  
  public static <T> Optional<T> ifMac(T t) {
    return isMac() ? Optional.of(t) : Optional.empty();
  }
  
  
  public static boolean isSolaris() {
    return getOS() == SOLARIS;
  }
  
  public static <T> Optional<T> ifSolaris(T t) {
    return isSolaris() ? Optional.of(t) : Optional.empty();
  }
  
  
  public static boolean isNumLockEnabled() {
    return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
  }
  
  public static boolean isCapsLockEnabled() {
    return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
  }
  
  public static boolean isScrollLockEnabled() {
    return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
  }
  
  public static void setNumLockEnabled(boolean enabled) {
    Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, enabled);
  }
  
  public static void setCapsLockEnabled(boolean enabled) {
    Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, enabled);
  }
  
  public static void setScrollLockEnabled(boolean enabled) {
    Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_SCROLL_LOCK, enabled);
  }
  
  public static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }
  
}
