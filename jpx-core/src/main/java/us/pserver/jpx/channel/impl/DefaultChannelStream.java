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

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.stream.ChannelStream;
import us.pserver.jpx.channel.stream.StreamPartial;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/08/2018
 */
public class DefaultChannelStream implements ChannelStream, Runnable {
  
  private final LinkedBlockingDeque<StreamPartial> input;
  
  private final List<BiFunction> stream;
  
  private final Channel channel;
  
  private final AtomicBoolean inioctx;
  
  private volatile int index;
  
  
  public DefaultChannelStream(Channel ch) {
    this.channel = Objects.requireNonNull(ch);
    input = new LinkedBlockingDeque();
    stream = new CopyOnWriteArrayList<>();
    index = 0;
    inioctx = new AtomicBoolean(false);
  }

  
  @Override
  public <I,O> ChannelStream appendFunction(BiFunction<Channel,Optional<I>,StreamPartial<O>> fn) {
    if(fn != null) {
      stream.add(fn); 
    }
    return this;
  }


  @Override
  public <I,O> boolean removeFunction(BiFunction<Channel,Optional<I>,StreamPartial<O>> fn) {
    return stream.remove(fn);
  }


  @Override
  public ChannelStream run(ByteBuffer buf) {
    input.addLast(StreamPartial.activeStream(buf));
    channel.getChannelEngine().execute(channel, this);
    return this;
  }
  
  
  @Override
  public ChannelStream runSync(ByteBuffer buf) {
    Lock lock = new ReentrantLock();
    Condition sync = lock.newCondition();
    BiFunction<Channel,Optional,StreamPartial> last = stream.get(stream.size() -1);
    BiFunction<Channel,Optional,StreamPartial> finish = (c,o)->{
      StreamPartial part = last.apply(c, o);
      sync.signalAll();
      //lock.lock();
      //try {
        //sync.signalAll();
      //}
      //finally {
        //lock.unlock();
      //}
      return part;
    };
    stream.remove(last);
    stream.add(finish);
    resume();
    lock.lock();
    try {
      sync.await();
    }
    catch(InterruptedException e) {
      throw new RuntimeException(e.toString(), e);
    }
    finally {
      lock.unlock();
    }
    return this;
  }
  
  
  private Optional take() {
    try {
      StreamPartial part = input.take();
      return part.get();
    } 
    catch(InterruptedException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }


  public boolean applyCurrent() {
    boolean apply = index < stream.size();
    if(apply) {
      BiFunction<Channel,Optional,StreamPartial> fn = stream.get(index++);
      StreamPartial partial = fn.apply(channel, take());
      if(!partial.isActive()) return false;
      Logger.info("%s", partial);
      input.addLast(partial);
    }
    return apply;
  }
  
  
  public void resume() {
    Logger.info("Execution context=%s", (isInIOContext() ? "IOContext" : "SYSContext"));
    while(applyCurrent());
  }


  @Override
  public void run() {
    resume();
  }


  @Override
  public boolean isInIOContext() {
    return inioctx.get();
  }


  @Override
  public <I> void switchToIOContext(Optional<I> opt) {
    Logger.info("%s", opt);
    index--;
    inioctx.set(true);
    input.addLast(StreamPartial.activeStream(opt));
    channel.getChannelEngine().executeIO(channel, this);
  }


  @Override
  public boolean isInSytemContext() {
    return !inioctx.get();
  }


  @Override
  public <I> void switchToSystemContext(Optional<I> opt) {
    Logger.info("%s", opt);
    index--;
    inioctx.set(false);
    input.addLast(StreamPartial.activeStream(opt));
    channel.getChannelEngine().execute(channel, this);
  }

}
