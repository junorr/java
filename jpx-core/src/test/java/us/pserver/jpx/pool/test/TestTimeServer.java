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

package us.pserver.jpx.pool.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelEvent;
import us.pserver.jpx.channel.impl.DefaultChannelConfiguration;
import static us.pserver.jpx.channel.impl.DefaultChannelConfiguration.DEFAULT_IO_THREAD_POOL_SIZE;
import static us.pserver.jpx.channel.impl.DefaultChannelConfiguration.DEFAULT_SYSTEM_THREAD_POOL_SIZE;
import us.pserver.jpx.channel.impl.DefaultChannelEngine;
import us.pserver.jpx.channel.impl.DefaultSocketOptions;
import us.pserver.jpx.channel.impl.ServerChannelGroup;
import us.pserver.jpx.event.EventListener;
import us.pserver.jpx.log.Logger;
import us.pserver.jpx.pool.Pooled;
import us.pserver.jpx.pool.impl.BufferPoolConfiguration;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/10/2018
 */
public class TestTimeServer {

  private final ChannelConfiguration config = new DefaultChannelConfiguration(
      new BufferPoolConfiguration(), 
      new DefaultSocketOptions(),
      DEFAULT_IO_THREAD_POOL_SIZE, 
      DEFAULT_SYSTEM_THREAD_POOL_SIZE, 
      new InetSocketAddress("127.0.0.1", 20202),
      true,
      true
  );

  private final ChannelEngine engine = new DefaultChannelEngine(config);
  
  @Test
  public void timeServer() {
    try {
      ServerSocketChannel socket = ServerSocketChannel.open();
      socket.bind(config.getSocketAddress());
      ServerChannelGroup server = new ServerChannelGroup(Selector.open(), config, engine, 1);
      server.add(socket);
      server.addListener((c,e) -> Logger.info("%s", e));
      server.addListener(EventListener.create((c,e) -> {
        Pooled<ByteBuffer> buf = c.getChannelEngine().getBufferPool().allocAwait();
        buf.get().put(StandardCharsets.UTF_8.encode(Instant.now().toString()));
        buf.get().flip();
        c.write(buf);
      }, ChannelEvent.Type.CONNECTION_STABLISHED));
      server.start();
      //new TestClientTimeServer().clientConnect();
      Sleeper.of(10000).sleep();
      server.closeAwait();
    }
    catch(Exception e) {
      Logger.error(e);
    }
  }
}
