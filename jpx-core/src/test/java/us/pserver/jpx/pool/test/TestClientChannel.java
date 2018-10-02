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
import org.junit.jupiter.api.Test;
import us.pserver.jpx.channel.ChannelConfiguration;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.impl.ClientChannel;
import us.pserver.jpx.channel.impl.DefaultChannelConfiguration;
import us.pserver.jpx.channel.impl.DefaultChannelEngine;
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
      //String host = "disec3.intranet.bb.com.br";
      //String host = "www.google.com";
      String host = "dzone.com";
      //String host = "www.terra.com.br";
      //socket.connect(new InetSocketAddress(host, 80));
      socket.connect(new InetSocketAddress("localhost", 40080));
      Selector selector = Selector.open();
      ClientChannel channel = new ClientChannel(selector, config, engine, socket);
      Pooled<ByteBuffer> buf = engine.getByteBufferPool().alloc();
      StringBuilder sreq = new StringBuilder();
      sreq.append("GET http://").append(host).append("/ HTTP/1.0\r\n")
          .append("Host: ").append(host).append("\r\n")
          .append("User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:62.0) Gecko/20100101 Firefox/62.0\r\n")
          .append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n")
          .append("Accept-Language: en-US,en;q=0.5\r\n")
          .append("Accept-Encoding: gzip, deflate, br\r\n")
          .append("Referer: http://").append(host).append("\r\n")
          .append("Proxy-Authorization: Basic ZjYwMzY0Nzc6OTYzMjU4OTY=\r\n")
          .append("Connection: keep-alive\r\n")
          .append("Upgrade-Insecure-Requests: 1\r\n")
          .append("\r\n\r\n");
      ByteBuffer req = StandardCharsets.UTF_8.encode(sreq.toString());
      buf.get().put(req);
      buf.get().flip();
      channel.addListener((c,e) -> {
        Logger.info("%s", e);
      });
      StreamFunction<Pooled<ByteBuffer>,Void> fn = (s,o) -> {
        Logger.info("o.isPresent(): %s", o.isPresent());
        if(o.isPresent()) {
          System.err.println(StandardCharsets.UTF_8.decode(o.get().get()).toString());
        }
        return StreamPartial.brokenStream();
      };
      Logger.info("function: %s", fn);
      channel.getChannelStream().appendFunction(fn);
      channel.write(buf);
      channel.start();
      Sleeper.of(5000).sleep();
      channel.closeAwait();
      //channel.close();
    }
    catch(Exception e) {
      Logger.error(e);
    }
  }
  
}
