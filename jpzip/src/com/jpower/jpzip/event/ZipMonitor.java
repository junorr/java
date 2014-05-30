package com.jpower.jpzip.event;


/**
 *
 * @author juno
 */
public interface ZipMonitor {
  
  public void addListener(ZipUpdateListener l);
  
  public boolean removeListener(ZipUpdateListener l);
  
  public void clearListeners();
  
  public void notifyListeners(ZipUpdateEvent event);
  
}
