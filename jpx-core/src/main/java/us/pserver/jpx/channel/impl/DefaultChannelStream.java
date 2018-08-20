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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelAsync;
import us.pserver.jpx.channel.ChannelStream;
import us.pserver.jpx.log.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/08/2018
 */
public class DefaultChannelStream implements ChannelStream, Runnable {
  
  private final LinkedBlockingDeque input;
  
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
  public <I, O> ChannelStream append(BiFunction<Channel, I, O> fn) {
    if(fn != null) {
      stream.add(fn);
    }
    return this;
  }


  @Override
  public <I, O> boolean remove(BiFunction<Channel, I, O> fn) {
    return stream.remove(fn);
  }


  @Override
  public ChannelStream applyStart(ByteBuffer buf) {
    if(buf != null) {
      input.addLast(buf);
    }
    return this;
  }
  
  
  private Object take() {
    try {
      Object obj = input.take();
      Logger.info("input.size=%d, %s", input.size(), obj);
      return obj;
    } 
    catch(InterruptedException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }


  @Override
  public ByteBuffer getFinal() {
    return (ByteBuffer) take();
  }


  @Override
  public boolean applyCurrent() {
    boolean apply = index < stream.size();
    if(apply) {
      Object obj = stream.get(index++).apply(channel, take());
      if(obj == null) return false;
      System.out.println("* applyCurrent(): "+ obj);
      input.addLast(obj);
    }
    return apply;
  }
  
  
  public void resume() {
    while(applyCurrent());
  }


  @Override
  public ByteBuffer apply(ByteBuffer buf) {
    ChannelAsync[] asyncs = new ChannelAsync[stream.size() + 1];
    asyncs[0] = channel.getChannelEngine().execute(channel, () -> buf);
    for(int i = 1; i < asyncs.length; i++) {
      BiFunction bc = stream.get(i);
      final int idx = i;
      asyncs[i-1].appendSuccessListener(c -> {
        asyncs[idx] = channel.getChannelEngine().execute(channel, asyncs[idx-1].get().get(), g -> {
          return bc.apply(c, g);
        });
      });
    }
  }
  
  
  @Override
  public void run() {
    applyCurrent();
    resume();
  }


  @Override
  public boolean isInIOContext() {
    return inioctx.get();
  }


  @Override
  public <I> void switchToIOContext(I in) {
    if(in != null) {
      input.addLast(in);
      index--;
      inioctx.set(true);
      channel.getChannelEngine().getIOExecutorService().execute(this);
    }
  }


  @Override
  public boolean isInSytemContext() {
    return !inioctx.get();
  }


  @Override
  public <I> void switchToSystemContext(I in) {
    if(in != null) {
      input.addLast(in);
      index--;
      inioctx.set(false);
      channel.getChannelEngine().getSystemExecutorService().execute(this);
    }
  }

}
