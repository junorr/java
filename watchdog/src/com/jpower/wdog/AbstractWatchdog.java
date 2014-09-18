/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package com.jpower.wdog;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/08/2012
 */
public abstract class AbstractWatchdog implements Watchdog {

  private List<ChangeListener> listeners;
  
  private long interval;
  
  private boolean run;
  
  private final Object obj = new Object();
  
  
  protected AbstractWatchdog() {
    run = false;
    interval = 0;
    listeners = new LinkedList<>();
  }
  
  
  protected AbstractWatchdog(long interval) {
    this();
    this.setInterval(interval);
  }
  
  
  @Override
  public List<ChangeListener> listeners() {
    return listeners;
  }
  
  
  @Override
  public AbstractWatchdog addListener(ChangeListener c) {
    listeners.add(c);
    return this;
  }
  
  
  @Override
  public AbstractWatchdog stopWatcher() {
    run = false;
    return this;
  }
  
  
  @Override
  public AbstractWatchdog startWatcher() {
    this.setInterval(interval);
    run = true;
    new Thread(this, "Watchdog_"+ obj.toString()).start();
    return this;
  }
  
  
  @Override
  public boolean isRunning() {
    return run;
  }
  
  
  @Override
  public AbstractWatchdog setInterval(long i) {
    if(i <= 0)
      throw new IllegalArgumentException("Invalid interval: "+ i);
    
    this.interval = i;
    return this;
  }
  
  
  @Override
  public long getInterval() {
    return interval;
  }
  
  
  public void delay(long delay) {
    synchronized(obj) {
      try {
        obj.wait(delay);
      } catch(InterruptedException ex) {}
    }
  }
  
  
  public void delayInterval() {
    this.delay(interval);
  }
  
  
  public void wake() {
    synchronized(obj) {
      obj.notifyAll();
    }
  }
  
  
  public void notifyListeners(Object data) {
    Date d = new Date();
    for(ChangeListener c : listeners)
      c.stateChanged(d, data);
  }
  
}
