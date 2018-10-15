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

package us.pserver.jpx.pool.impl;

import java.time.Instant;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import us.pserver.jpx.event.Attribute;
import us.pserver.jpx.event.EventListener;
import us.pserver.jpx.pool.Pool;
import us.pserver.jpx.pool.PoolAttribute;
import us.pserver.jpx.pool.PoolConfiguration;
import us.pserver.jpx.pool.PoolEvent;
import us.pserver.jpx.pool.Pooled;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/08/2018
 */
public class DefaultPool<T> implements Pool<T> {
  
  private final PoolConfiguration cfg;
  
  private final Supplier<T> supplier;
  
  private final AtomicInteger refcount;
  
  private final AtomicInteger avlcount;
  
  private final AtomicBoolean empty;
  
  private final Deque<Pooled<T>> available;
  
  private final Deque<Consumer<Pooled<T>>> awaiters;
  
  private final List<EventListener<Pool,PoolEvent>> listeners;
  
  private final ReentrantLock lock;
  
  private final Condition avlcond;
  
  
  public DefaultPool(PoolConfiguration cfg, Supplier<T> sup) {
    this.cfg = Objects.requireNonNull(cfg);
    this.supplier = Objects.requireNonNull(sup);
    refcount = new AtomicInteger(0);
    avlcount = new AtomicInteger(0);
    empty = new AtomicBoolean(false);
    available = new ConcurrentLinkedDeque<>();
    awaiters = new ConcurrentLinkedDeque<>();
    listeners = new CopyOnWriteArrayList<>();
    lock = new ReentrantLock();
    avlcond = lock.newCondition();
    for(int i = 0; i < cfg.getInitialSize(); i++) {
      addNew(false);
    }
    addListener(new SignalPoolEventListener(lock, avlcond));
  }
  
  
  private void fireEvent(PoolEvent evt) {
    if(evt != null) {
      listeners.forEach(l -> l.accept(this, evt));
    }
  }
  
  
  @Override
  public PoolConfiguration getConfiguration() {
    return cfg;
  }
  
  
  @Override
  public Pool<T> addListener(EventListener<Pool,PoolEvent> lst) {
    if(lst != null) {
      listeners.add(lst);
    }
    return this;
  }
  
  
  @Override
  public boolean removeListener(EventListener<Pool,PoolEvent> lst) {
    return listeners.remove(lst);
  }
  
  
  @Override
  public Pooled<T> alloc() {
    Optional<Pooled<T>> opt = tryAlloc();
    if(!opt.isPresent()) {
      throw new PoolUnderflowException();
    }
    return opt.get();
  }


  @Override
  public Pooled<T> allocAwait() {
    Optional<Pooled<T>> opt = tryAlloc();
    if(!opt.isPresent()) {
      lock.lock();
      try {
        avlcond.await();
        return allocAwait();
      }
      catch(InterruptedException e) {
        throw new PoolException(e.toString(), e);
      }
      finally {
        lock.unlock();
      }
    }
    else {
      return opt.get();
    }
  }
  
  
  @Override
  public void onAvailable(Consumer<Pooled<T>> cs) {
    Optional<Pooled<T>> opt = tryAlloc();
    opt.ifPresent(cs);
    if(!opt.isPresent()) {
      awaiters.addLast(cs);
    }
  }


  @Override
  public Optional<Pooled<T>> tryAlloc() {
    Optional<Pooled<T>> opt = Optional.empty();
    if(isAvailable()) {
      opt = Optional.of(pooled());
      if(isMinAvailableCountReached()) {
        PoolEvent evt = new PoolEvent(PoolEvent.Type.MIN_AVAILABLE_COUNT_REACHED, Attribute.mapBuilder()
            .add(PoolAttribute.AVAILABLE_COUNT, avlcount.get())
            .attrs()
        );
        fireEvent(evt);
        allocateOptimum();
      }
    }
    if(!isAvailable()) {
      if(!empty.get()) {
        PoolEvent evt = new PoolEvent(PoolEvent.Type.EMPTY_POOL, Attribute.mapBuilder()
            .add(PoolAttribute.REFERENCE_COUNT, refcount.get())
            .add(PoolAttribute.AVAILABLE_COUNT, avlcount.get())
            .attrs()
        );
        fireEvent(evt);
        empty.set(true);
      }
      if(!isMaxRefCountReached()) {
        addNew(true);
        opt = Optional.of(pooled());
        allocateOptimum();
      }
    }
    return opt;
  }
  
  
  private void allocateOptimum() {
    int rfc = refcount.get();
    int avl = avlcount.get();
    while(!isMaxRefCountReached() && avlcount.get() < getOptimumAllocationCount()) {
      addNew(false);
    }
    if(rfc < refcount.get()) {
      PoolEvent evt = new PoolEvent(PoolEvent.Type.REFERENCE_COUNT_INCREASE, Attribute.mapBuilder()
          .add(PoolAttribute.REFERENCE_COUNT, refcount.get())
          .attrs()
      );
      fireEvent(evt);
    }
    if(avl < avlcount.get()) {
      PoolEvent evt = new PoolEvent(PoolEvent.Type.AVAILABLE_COUNT_INCREASE, Attribute.mapBuilder()
          .add(PoolAttribute.AVAILABLE_COUNT, avlcount.get())
          .attrs()
      );
      fireEvent(evt);
    }
    if(isMaxRefCountReached()) {
      PoolEvent evt = new PoolEvent(PoolEvent.Type.MAX_REF_COUNT_REACHED, Attribute.mapBuilder()
          .add(PoolAttribute.REFERENCE_COUNT, refcount.get())
          .attrs()
      );
      fireEvent(evt);
    }
  }
  
  
  private void deallocateOptimum() {
    int rfc = refcount.get();
    int avl = avlcount.get();
    while(canDeallocate()) {
      avlcount.decrementAndGet();
      refcount.decrementAndGet();
      available.pollFirst();
    }
    if(rfc > refcount.get()) {
      PoolEvent evt = new PoolEvent(PoolEvent.Type.REFERENCE_COUNT_DECREASE, Attribute.mapBuilder()
          .add(PoolAttribute.REFERENCE_COUNT, refcount.get())
          .attrs()
      );
      fireEvent(evt);
    }
    if(avl > avlcount.get()) {
      PoolEvent evt = new PoolEvent(PoolEvent.Type.AVAILABLE_COUNT_DECREASE, Attribute.mapBuilder()
          .add(PoolAttribute.AVAILABLE_COUNT, avlcount.get())
          .attrs()
      );
      fireEvent(evt);
    }
  }
  
  
  private boolean canDeallocate() {
    return avlcount.get() > cfg.getMinAvailableCount() 
        && refcount.get() > getOptimumAllocationCount()
        && refcount.get() > 0;
  }
  
  
  private void addNew(boolean fireEvent) {
    refcount.incrementAndGet();
    avlcount.incrementAndGet();
    Instant inst = Instant.now();
    available.addLast(new DefaultPooled<>(supplier.get(), this));
    empty.set(false);
    if(fireEvent) {
      PoolEvent evt = new PoolEvent(PoolEvent.Type.AVAILABLE_COUNT_INCREASE, Attribute.mapBuilder()
          .add(PoolAttribute.AVAILABLE_COUNT, avlcount.get())
          .attrs()
      );
      fireEvent(evt);
      evt = new PoolEvent(PoolEvent.Type.REFERENCE_COUNT_INCREASE, Attribute.mapBuilder()
          .add(PoolAttribute.REFERENCE_COUNT, refcount.get())
          .attrs()
      );
      fireEvent(evt);
    }
  }
  
  
  private Pooled<T> pooled() {
    avlcount.decrementAndGet();
    PoolEvent evt = new PoolEvent(PoolEvent.Type.AVAILABLE_COUNT_DECREASE, Attribute.mapBuilder()
        .add(PoolAttribute.AVAILABLE_COUNT, avlcount.get())
        .attrs()
    );
    fireEvent(evt);
    return available.pollFirst();
  }
  
  
  private boolean isDeallocationPercentageReached() {
    return refcount.get() >= getDeallocationPercentageCount();
  }
  
  
  private int getOptimumAllocationCount() {
    return (int) Math.round((cfg.getMaxReferenceCount() - cfg.getMinAvailableCount()) / 2.0);
  }
  
  
  private int getDeallocationPercentageCount() {
    return Long.valueOf(Math.round(
        cfg.getMaxReferenceCount() * cfg.getDeallocationPercentage()
    )).intValue();
  }
  
  
  private boolean isMaxRefCountReached() {
    return refcount.get() >= cfg.getMaxReferenceCount();
  }
  
  
  private boolean isMinAvailableCountReached() {
    return avlcount.get() == cfg.getMinAvailableCount();
  }
  
  
  @Override
  public void release(Pooled<T> pld) {
    Objects.requireNonNull(pld);
    if(!awaiters.isEmpty()) {
      awaiters.pollFirst().accept(pld);
    }
    else {
      available.addLast(pld);
      avlcount.incrementAndGet();
      empty.set(false);
      PoolEvent evt = new PoolEvent(PoolEvent.Type.AVAILABLE_COUNT_INCREASE, Attribute.mapBuilder()
          .add(PoolAttribute.AVAILABLE_COUNT, avlcount.get())
          .attrs()
      );
      fireEvent(evt);
      if(isDeallocationPercentageReached()) {
        evt = new PoolEvent(PoolEvent.Type.DEALLOCATION_PERCENTAGE_REACHED, Attribute.mapBuilder()
            .add(PoolAttribute.REFERENCE_COUNT, refcount.get())
            .attrs()
        );
        fireEvent(evt);
        deallocateOptimum();
      }
    }
  }


  @Override
  public int allocatedCount() {
    return refcount.get();
  }


  @Override
  public boolean isAvailable() {
    return !available.isEmpty();
  }
  
}
