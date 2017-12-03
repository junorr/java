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

package us.pserver.coreone.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import us.pserver.coreone.Pipe;
import us.pserver.coreone.ex.PipeOperationException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public class DefaultPipe<T> implements Pipe<T> {
  
  public static final int DEFAULT_PULL_TIMEOUT = 100;
  
  public static final int DEFAULT_PIPE_SIZE = 50;
  

  private final LinkedBlockingDeque<T> queue;
  
  private final AtomicBoolean closed;
  
  private final AtomicBoolean closeOnEmpty;
  
  private final List<Consumer<Throwable>> errors;
  
  private final List<Consumer<T>> consumers;
  
  
  public DefaultPipe(int pipeSize) {
    if(pipeSize < 1) {
      throw new IllegalArgumentException("Bad pipe size (< 1)");
    }
    queue = new LinkedBlockingDeque<>(pipeSize);
    closed = new AtomicBoolean(false);
    closeOnEmpty = new AtomicBoolean(false);
    this.errors = new CopyOnWriteArrayList<>();
    this.consumers = new CopyOnWriteArrayList<>();
  }
  
  
  public DefaultPipe() {
    this(DEFAULT_PIPE_SIZE);
  }


  @Override
  public void onAvailable(Consumer<T> cs) {
    if(cs != null) consumers.add(cs);
  }


  @Override
  public void onError(Consumer<Throwable> cs) {
    if(cs != null) errors.add(cs);
  }


  @Override
  public T pull(long timeout) {
    this.checkClosed();
    try {
      return queue.pollFirst(timeout, TimeUnit.MILLISECONDS);
    } 
    catch(InterruptedException e) {
      throw new PipeOperationException(e.toString(), e);
    }
  }
  
  
  private void checkClosed() {
    if(closed.get()) {
      throw new PipeOperationException("Pipe is closed");
    }
  }
  
  
  private boolean canClose() {
    return closeOnEmpty.get() && !available();
  }


  @Override
  public T pull() {
    checkClosed();
    T polled = null;
    try {
      while(polled == null && !isClosed() && !canClose()) {
        polled = queue.pollFirst(
            DEFAULT_PULL_TIMEOUT, 
            TimeUnit.MILLISECONDS
        );
      }
      if(canClose()) close();
      return polled;
    } 
    catch(InterruptedException e) {
      throw new PipeOperationException(e.toString(), e);
    }
  }


  @Override
  public boolean push(T val) {
    if(isClosed()) {
      throw new PipeOperationException("Pipe is closed");
    }
    try {
      boolean offerOk = !consumers.isEmpty();
      if(offerOk) consumers.forEach(c->c.accept(val));
      while(!closed.get() && !offerOk) {
        offerOk = queue.offerLast(val, 
            DEFAULT_PULL_TIMEOUT, 
            TimeUnit.MILLISECONDS
        );
      }
      return offerOk;
    } 
    catch(InterruptedException e) {
      throw new PipeOperationException(e.toString(), e);
    }
  }
  
  
  @Override
  public void error(Throwable th) {
    errors.forEach(c->c.accept(th));
  }
  
  
  @Override
  public boolean available() {
    return !queue.isEmpty();
  }
  
  
  @Override
  public void close() {
    closed.set(true);
  }
  
  
  @Override
  public void closeOnEmpty() {
    closeOnEmpty.set(true);
  }
  
  
  @Override
  public boolean isClosed() {
    return closed.get();
  }
  
}
