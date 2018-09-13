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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.stream.ChannelStream;
import us.pserver.jpx.channel.stream.ChannelStreamAttribute;
import us.pserver.jpx.channel.stream.ChannelStreamEvent;
import static us.pserver.jpx.channel.stream.ChannelStreamEvent.Type.EXCEPTION_THROWED;
import static us.pserver.jpx.channel.stream.ChannelStreamEvent.Type.STREAM_FINISHED;
import static us.pserver.jpx.channel.stream.ChannelStreamEvent.Type.STREAM_FUNCTION_APPENDED;
import static us.pserver.jpx.channel.stream.ChannelStreamEvent.Type.STREAM_FUNCTION_EXECUTED;
import static us.pserver.jpx.channel.stream.ChannelStreamEvent.Type.STREAM_FUNCTION_REMOVED;
import us.pserver.jpx.channel.stream.StreamFunction;
import us.pserver.jpx.channel.stream.StreamPartial;
import us.pserver.jpx.event.Attribute;
import us.pserver.jpx.event.Attribute.AttributeMapBuilder;
import us.pserver.jpx.event.Event;
import us.pserver.jpx.event.EventListener;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/08/2018
 */
public class DefaultChannelStream implements ChannelStream, Runnable {
  
  private final LinkedBlockingDeque<StreamPartial> input;
  
  private final List<StreamFunction> stream;
  
  private final List<EventListener<ChannelStream,ChannelStreamEvent>> listeners;
  
  private final Channel channel;
  
  private final AtomicBoolean inioctx;
  
  private volatile int index;
  
  
  public DefaultChannelStream(Channel ch) {
    this.channel = Objects.requireNonNull(ch);
    input = new LinkedBlockingDeque();
    stream = new CopyOnWriteArrayList<>();
    listeners = new CopyOnWriteArrayList<>();
    index = 0;
    inioctx = new AtomicBoolean(false);
  }
  
  
  @Override
  public ChannelStream addListener(EventListener<ChannelStream,ChannelStreamEvent> lst) {
    if(lst != null) {
      listeners.add(lst);
    }
    return this;
  }
  
  
  @Override
  public boolean removeListener(EventListener<ChannelStream,ChannelStreamEvent> lst) {
    return listeners.remove(lst);
  }
  
  
  private void fireEvent(ChannelStreamEvent evt) {
    listeners.stream()
        .filter(l -> l.getInterests().contains(evt.getType()))
        .forEach(l -> l.accept(this, evt));
  }
  
  
  @Override
  public <I,O> ChannelStream appendFunction(StreamFunction<I,O> fn) {
    if(fn != null) {
      stream.add(fn); 
      fireEvent(createEvent(STREAM_FUNCTION_APPENDED, Attribute.mapBuilder()
          .add(ChannelStreamAttribute.STREAM_FUNCTION, fn))
      );
    }
    return this;
  }


  @Override
  public <I,O> boolean removeFunction(StreamFunction<I,O> fn) {
    fireEvent(createEvent(STREAM_FUNCTION_REMOVED, Attribute.mapBuilder()
        .add(ChannelStreamAttribute.STREAM_FUNCTION, fn))
    );
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
    addListener(new SyncListener(lock, sync));
    run(buf);
    if(!isStreamFinished()) {
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
    boolean apply = !isStreamFinished();
    if(apply) {
      StreamFunction fn = stream.get(index++);
      if(isStreamFinished()) fireEvent(createEvent(STREAM_FINISHED));
      StreamPartial partial;
      try {
        partial = fn.apply(channel, take());
        fireEvent(createEvent(STREAM_FUNCTION_EXECUTED, Attribute.mapBuilder()
            .add(ChannelStreamAttribute.STREAM_FUNCTION, fn)
            .add(ChannelStreamAttribute.STREAM_PARTIAL, partial))
        );
      }
      catch(Exception e) {
        partial = StreamPartial.brokenStream();
        fireEvent(createEvent(EXCEPTION_THROWED, Attribute.mapBuilder()
            .add(ChannelStreamAttribute.STREAM_FUNCTION, fn)
            .add(ChannelStreamAttribute.EXCEPTION, e))
        );
      }
      if(!partial.isActive()) return false;
      Logger.debug("%s", partial);
      input.addLast(partial);
    }
    return apply;
  }
  
  
  public void resume() {
    Logger.debug("Execution context=%s", (isInIOContext() ? "IOContext" : "SYSContext"));
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
    Logger.debug("%s", opt);
    index--;
    inioctx.set(true);
    input.addLast(StreamPartial.activeStream(opt));
    channel.getChannelEngine().executeIO(channel, this);
    fireEvent(createEvent(ChannelStreamEvent.Type.SWITCH_TO_IO_CONTEXT));
  }
  
  
  private ChannelStreamEvent createEvent(ChannelStreamEvent.Type type) {
    return createEvent(type, Attribute.mapBuilder());
  }
  
  
  private ChannelStreamEvent createEvent(ChannelStreamEvent.Type type, AttributeMapBuilder bld) {
    bld.add(ChannelStreamAttribute.CHANNEL, channel)
        .add(ChannelStreamAttribute.IS_STREAM_FINISHED, isStreamFinished())
        .add(ChannelStreamAttribute.STREAM_FILTER_SIZE, stream.size())
        .add(ChannelStreamAttribute.STREAM_INDEX, index);
    return new ChannelStreamEvent(type, bld.create());
  }
  
  
  @Override
  public boolean isInSytemContext() {
    return !inioctx.get();
  }


  @Override
  public <I> void switchToSystemContext(Optional<I> opt) {
    Logger.debug("%s", opt);
    index--;
    inioctx.set(false);
    input.addLast(StreamPartial.activeStream(opt));
    channel.getChannelEngine().execute(channel, this);
    fireEvent(createEvent(ChannelStreamEvent.Type.SWITCH_TO_SYSTEM_CONTEXT));
  }
  
  
  @Override
  public boolean isStreamFinished() {
    return index >= stream.size();
  }
  
  
  
  
  
  private static class SyncListener implements EventListener<ChannelStream,ChannelStreamEvent> {
    
    private final List<Event.Type> interests;
    
    private final Lock lock;
    
    private final Condition sync;
    
    public SyncListener(Lock l, Condition c) {
      lock = l;
      sync = c;
      interests = Arrays.asList(EXCEPTION_THROWED, STREAM_FINISHED);
    }
    
    @Override
    public Collection<Event.Type> getInterests() {
      return interests;
    }
    
    @Override
    public void accept(ChannelStream t, ChannelStreamEvent u) {
      lock.lock();
      try {
        sync.signalAll();
      }
      finally {
        lock.unlock();
      }
    }
    
  }
  
}
