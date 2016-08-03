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

package us.pserver.dropmap.impl;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import us.pserver.dropmap.DMap;
import us.pserver.dropmap.DMap.DEngine;
import us.pserver.dropmap.DMap.DEntry;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2016
 */
public class DropEngine<K,V> implements DEngine<K,V> {
  
  private final DMap<K,V> map;
  
  private final SortedSet<DEntry<K,V>> drops;
  
  private final Map<K,Consumer<DEntry<K,V>>> consumers;
  
  private final AtomicBoolean running;
  
  private final ReentrantLock waitLock;
  
  private final Condition waitCond;
  
  private final ReentrantLock lock;
  
  private final Dropper dropper;
  
  
  public DropEngine(DMap<K,V> map) {
    if(map == null) {
      throw new IllegalArgumentException("Bad Null DMap");
    }
    this.map = map;
    consumers = new TreeMap<>();
    running = new AtomicBoolean();
    drops = new TreeSet<>();
    lock = new ReentrantLock();
    waitLock = new ReentrantLock();
    waitCond = waitLock.newCondition();
    dropper = new Dropper();
  }
  
  
  @Override
  public boolean isRunning() {
    return running.get();
  }
  
  
  @Override
  public DEngine<K,V> reset() {
    this.stop();
    consumers.clear();
    drops.clear();
    return this;
  }
  

  @Override
  public DEngine<K,V> start() {
    if(!map.isEmpty()) {
      lock.lock();
      try {
        running.compareAndSet(false, true);
        System.out.println("engine.start.running: "+ running.get());
        dropper.start();
      } finally {
        lock.unlock();
      }
    }
    return this;
  }


  @Override
  public DEngine<K,V> stop() {
    lock.lock();
    try {
      running.compareAndSet(true, false);
      dropper.disable();
      waitLock.lock();
      try { 
        waitCond.signalAll(); 
      } finally { 
        waitLock.unlock(); 
      }
    } finally {
      lock.unlock();
    }
    return this;
  }


  @Override
  public DEngine<K,V> add(DEntry<K,V> e, Consumer<DEntry<K,V>> c) {
    if(e == null) {
      throw new IllegalArgumentException("Bad Null DEntry");
    }
    lock.lock();
    try {
      this.stop();
      drops.add(e);
      consumers.put(e.getKey(), (c != null ? c : x->{}));
      this.start();
    } finally {
      lock.unlock();
    }
    return this;
  }
  
  
  @Override
  public boolean isTracking(DEntry<K,V> e) {
    return drops.contains(e);
  }


  @Override
  public DEngine<K,V> remove(DEntry<K, V> e) {
    if(e != null && drops.contains(e)) {
      lock.lock();
      try {
        this.stop();
        drops.remove(e);
        consumers.remove(e.getKey());
        this.start();
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  
  
  class Dropper extends Thread {
    
    private final AtomicBoolean disabled;
    
    public Dropper() {
      this.setDaemon(true);
      this.setPriority(MIN_PRIORITY);
      disabled = new AtomicBoolean(false);
    }
    
    public Dropper disable() {
      disabled.compareAndSet(false, true);
      return this;
    }
    
    @Override
    public void run() {
      while(!drops.isEmpty()) {
        DEntry<K,V> entry = drops.first();
        waitLock.lock();
        try {
          waitCond.await(entry.getTTL().toMillis(), TimeUnit.MILLISECONDS);
          if(running.get() && !disabled.get()) {
            map.remove(entry.getKey());
            drops.remove(entry);
            consumers.remove(entry.getKey()).accept(entry);
          }
        } 
        catch(InterruptedException e) {}
        finally {
          waitLock.unlock();
          disabled.compareAndSet(true, false);
        }
      }
    }
    
    public Dropper startDropping() {
      super.start();
      return this;
    }
    
  }
  
  
}
