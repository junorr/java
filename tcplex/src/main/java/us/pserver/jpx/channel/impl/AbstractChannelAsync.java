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

package us.pserver.jpx.channel.impl;

import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAsync;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/08/2018
 */
public abstract class AbstractChannelAsync<T> implements ChannelAsync<T> {
  
  protected final Channel channel;
  
  protected final Deque<Consumer<Channel>> oncomplete;
  
  protected final Deque<Consumer<Channel>> onsuccess;
  
  protected final Deque<BiConsumer<Channel,Throwable>> onerror;
  
  protected final Lock lock;
  
  protected final Condition sync;
  
  
  public AbstractChannelAsync(Channel channel) {
    this.channel = Objects.requireNonNull(channel);
    oncomplete = new ConcurrentLinkedDeque<>();
    onsuccess = new ConcurrentLinkedDeque<>();
    onerror = new ConcurrentLinkedDeque<>();
    this.lock = new ReentrantLock();
    this.sync = lock.newCondition();
  }


  @Override
  public ChannelAsync<T> appendCompleteListener(Consumer<Channel> cs) {
    oncomplete.add(cs);
    return this;
  }


  @Override
  public ChannelAsync<T> appendSuccessListener(Consumer<Channel> cs) {
    onsuccess.add(cs);
    return this;
  }


  @Override
  public ChannelAsync<T> appendErrorListener(BiConsumer<Channel,Throwable> cs) {
    onerror.add(cs);
    return this;
  }


  @Override
  public ChannelAsync<T> sync() {
    lock.lock();
    try {
      sync.await();
      return this;
    }
    catch(InterruptedException e) {
      throw new RuntimeException(e.toString(), e);
    }
    finally {
      lock.unlock();
    }
  }
  
  
  @Override
  public ChannelAsync<T> sync(long time, TimeUnit unit) {
    lock.lock();
    try {
      sync.await(time, unit);
      return this;
    }
    catch(InterruptedException e) {
      throw new RuntimeException(e.toString(), e);
    }
    finally {
      lock.unlock();
    }
  }
  
  
  protected void signalAll() {
    lock.lock();
    try {
      sync.signalAll();
    }
    finally {
      lock.unlock();
    }
  }
  
  
  protected void onError(Throwable th) {
    while(!onerror.isEmpty()) {
      BiConsumer<Channel,Throwable> cs = onerror.poll();
      if(cs != null) cs.accept(channel, th);
    }
  }
  
  
  protected void onComplete() {
    while(!oncomplete.isEmpty()) {
      Consumer<Channel> cs = oncomplete.poll();
      if(cs != null) cs.accept(channel);
    }
  }
  
  
  protected void onSuccess() {
    while(!onsuccess.isEmpty()) {
      Consumer<Channel> cs = onsuccess.poll();
      if(cs != null) cs.accept(channel);
    }
  }
  
}
