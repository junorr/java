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

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import us.pserver.jpx.channel.ChannelAttribute;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelEvent;
import us.pserver.jpx.channel.SwitchableChannel;
import us.pserver.jpx.event.Attribute;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2018
 */
public class ClientChannelGroup extends AbstractChannelGroup<SocketChannel> {
  
  
  public ClientChannelGroup(Selector select, ChannelConfiguration cfg, ChannelEngine eng, int maxSize) {
    super(select, cfg, eng, maxSize);
  }
  
  @Override
  public boolean add(SocketChannel socket) throws IOException {
    Objects.requireNonNull(socket);
    boolean success = count <= maxSize;
    if(success) {
      count++;
      SwitchableChannel channel = new ClientChannel_old(socket, selector, config, engine);
      if(running) {
        functions.forEach(channel::appendFunction);
        listeners.forEach(channel::addListener);
      }
      socket.configureBlocking(false);
      sockets.put(socket, channel);
      socket.register(selector, SelectionKey.OP_CONNECT 
          | SelectionKey.OP_READ 
          | SelectionKey.OP_WRITE, channel
      );
    }
    return success;
  }
  
  
  @Override
  public void run() {
    try {
      functions.forEach(f -> sockets.values().forEach(c -> c.appendFunction(f)));
      listeners.forEach(l -> sockets.values().forEach(c -> c.addListener(l)));
      while(running) {
        Optional<Iterator<SelectionKey>> opt = selectKeys();
        if(opt.isPresent()) {
          iterate(opt.get());
        }
      }//while
      doClose();
      fireEvent(createEvent(ChannelEvent.Type.CONNECTION_CLOSED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
      ));
    }
    catch(IOException e) {
      fireEvent(createEvent(ChannelEvent.Type.EXCEPTION_THROWED, Attribute.mapBuilder()
          .add(ChannelAttribute.UPTIME, getUptime())
          .add(ChannelAttribute.CHANNEL, this)
          .add(ChannelAttribute.EXCEPTION, e)
      ));
    }
  }
  
  
  private Optional<Iterator<SelectionKey>> selectKeys() throws IOException {
    return selector.select(100) > 0
        ? Optional.of(selector.selectedKeys().iterator())
        : Optional.empty();
  }
  
  
  private void iterate(Iterator<SelectionKey> it) throws IOException {
    while(it.hasNext()) {
      SelectionKey key = it.next();
      it.remove();
      switchKey(key);
    }
  }
  
}
