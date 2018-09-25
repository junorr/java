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
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.impl.ClientChannel;
import us.pserver.jpx.channel.impl.DefaultChannelConfiguration;
import us.pserver.jpx.channel.impl.DefaultChannelEngine;
import us.pserver.jpx.channel.stream.ChannelStream;
import us.pserver.jpx.channel.stream.StreamFunction;
import us.pserver.jpx.channel.stream.StreamPartial;
import us.pserver.jpx.log.Logger;
import us.pserver.jpx.pool.Pooled;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/09/2018
 */
public class TestClientChannel {
  
  private final ChannelConfiguration config = new DefaultChannelConfiguration();
  
  private final ChannelEngine engine = new DefaultChannelEngine(config);

  @Test
  public void connectToGoogle() {
    try {
      SocketChannel socket = SocketChannel.open();
      socket.connect(new InetSocketAddress("www.google.com", 80));
      Selector selector = Selector.open();
      ClientChannel channel = new ClientChannel(selector, config, engine, socket);
      Pooled<ByteBuffer> buf = engine.getByteBufferPool().alloc();
      ByteBuffer req = StandardCharsets.UTF_8.encode("GET / HTTP/1.1\n\r\n\r");
      buf.get().put(req);
      buf.get().flip();
      channel.addListener((c,e) -> {
        Logger.info("%s", e);
      });
      StreamFunction<Pooled<ByteBuffer>,Void> fn = (s,o) -> {
        Logger.info("o.isPresent(): %s", o.isPresent());
        if(o.isPresent()) {
          System.out.println(StandardCharsets.UTF_8.decode(o.get().get()).toString());
        }
        return StreamPartial.brokenStream();
      };
      channel.getChannelStream().appendFunction(fn);
      channel.write(buf);
      channel.start();
      Sleeper.of(10000).sleep();
    }
    catch(Exception e) {
      Logger.error(e);
    }
  }
  
}
