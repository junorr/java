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
  
  private final ReentrantLock lock;
  
  private final Dropper dropper;
  
  
  public DropEngine(DMap<K,V> map) {
    if(map == null) {
      throw new IllegalArgumentException("Bad Null DMap");
    }
    this.map = map;
    consumers = new TreeMap<>();
    drops = new TreeSet<>();
    lock = new ReentrantLock();
    dropper = new Dropper();
  }
  
  
  @Override
  public boolean isRunning() {
    return dropper.isRunning();
  }
  
  
  @Override
  public DEngine<K,V> reset() {
    lock.lock();
    try {
      dropper.disable();
      consumers.clear();
      drops.clear();
    } finally {
      lock.unlock();
    }
    return this;
  }
  

  @Override
  public DEngine<K,V> stop() {
    lock.lock();
    try {
      dropper.stop();
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
      dropper.disable();
      drops.add(e);
      if(c != null) consumers.put(e.getKey(), c);
      dropper.enable();
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
        dropper.disable();
        drops.remove(e);
        consumers.remove(e.getKey());
      } finally {
        lock.unlock();
        dropper.enable();
      }
    }
    return this;
  }
  
  
  private DropEngine<K,V> rmDrop(DEntry<K,V> entry) {
    if(entry != null && drops.contains(entry)) {
      lock.lock();
      try {
        drops.remove(entry);
      } finally {
        lock.unlock();
      }
    }
    return this;
  }
  
  
  private Consumer<DEntry<K,V>> rmConsumer(K k) {
    Consumer<DEntry<K,V>> cs = c->{};
    if(k != null && consumers.containsKey(k)) {
      lock.lock();
      try {
        cs = consumers.remove(k);
      } finally {
        lock.unlock();
      }
    }
    return cs;
  }
  
  
  
  
  
  class Dropper implements Runnable {
    
    private final AtomicBoolean running;
    
    private final AtomicBoolean disabled;
    
    private final ReentrantLock waitLock;

    private final Condition waitCond;
  
    public Dropper() {
      disabled = new AtomicBoolean(false);
      running = new AtomicBoolean(false);
      waitLock = new ReentrantLock();
      waitCond = waitLock.newCondition();
    }
    
    public boolean isRunning() {
      return running.get();
    }
    
    public Dropper disable() {
      disabled.compareAndSet(false, true);
      return this;
    }
    
    public Dropper enable() {
      disabled.compareAndSet(true, false);
      if(!this.isRunning()) {
        this.start();
      } else {
        this.signal();
      }
      return this;
    }
    
    public Dropper signal() {
      waitLock.lock();
      try {
        waitCond.signalAll();
      } finally {
        waitLock.unlock();
      }
      return this;
    }
    
    private void await(long ms) {
      waitLock.lock();
      try {
        if(ms > 0) {
          waitCond.await(ms, TimeUnit.MILLISECONDS);
        } else {
          waitCond.await();
        }
      } catch(InterruptedException ex) {
      } finally {
        waitLock.unlock();
      }
    }
    
    @Override
    public void run() {
      while(isRunning()) {
        if(drops.isEmpty() || disabled.get()) {
          await(0);
        }
        DEntry<K,V> entry = drops.first();
        await(entry.getTTL().toMillis());
        if(drops.contains(entry) && entry.getTTL().toMillis() <= 0) {
          map.remove(entry.getKey());
          rmDrop(entry);
          rmConsumer(entry.getKey()).accept(entry);
        }
      }//while
      System.out.println("!!! Dropper.Thread Exited !!!");
    }
    
    public Dropper start() {
      running.set(true);
      Thread thread = new Thread(this, "Dropper.Thread");
      thread.setDaemon(true);
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
      return this;
    }
    
    public Dropper stop() {
      running.set(false);
      return signal();
    }
    
  }
  
  
}
