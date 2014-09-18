package com.jpower.jpzip.event;

import java.util.LinkedList;


/**
 *
 * @author juno
 */
public class AbstractZipMonitor implements ZipMonitor {

  protected LinkedList<ZipUpdateListener> listeners = 
      new LinkedList<ZipUpdateListener>();
  

  @Override
  public void addListener(ZipUpdateListener l) {
    if(l != null) listeners.add(l);
  }


  @Override
  public boolean removeListener(ZipUpdateListener l) {
    return listeners.remove(l);
  }


  @Override
  public void clearListeners() {
    listeners.clear();
  }


  @Override
  public void notifyListeners(ZipUpdateEvent event) {
    for(ZipUpdateListener z : listeners)
      z.update(event);
  }
  
}
