/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.wdog;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author juno
 */
public class MultiWatchdog extends AbstractWatchdog {
  
  private LinkedList<Watcher> ws;
  
  
  public MultiWatchdog() {
    super();
    ws = new LinkedList<>();
  }
  
  
  public MultiWatchdog(long interval) {
    this();
    setInterval(interval);
  }
  
  
  public MultiWatchdog addWatcher(Watcher w) {
    if(w != null) ws.add(w);
    return this;
  }
  
  
  public boolean removeWatcher(Watcher w) {
    return ws.remove(w);
  }
  
  
  public int size() {
    return ws.size();
  }
  
  
  public MultiWatchdog clearWatchers() {
    ws.clear();
    return this;
  }
  
  
  public List<Watcher> watchers() {
    return ws;
  }


  @Override
  public void run() {
    while(isRunning()) {
      Iterator<Watcher> it = ws.iterator();
      while(it.hasNext()) {
        Watcher w = it.next();
        if(w.watch()) notifyListeners(w);
        if(!isRunning()) return;
      }
      delayInterval();
    }
  }
  
}
