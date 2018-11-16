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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import org.junit.jupiter.api.Test;
import us.pserver.jpx.channel.impl.SSLSocketChannel;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/11/2018
 */
public class TestSSLSocketChannel {

  @Test
  public void testSSLSocketChannel() throws Exception {
    try {
      String host = "www.bb.com.br";
      int port = 443;
      SocketChannel sch = SocketChannel.open(new InetSocketAddress(host, port));
      sch.configureBlocking(false);
      SSLContext ctx = SSLContext.getDefault();
      System.out.printf("* SSLContext.protocol = '%s'%n", ctx.getProtocol());
      SSLEngine eng = ctx.createSSLEngine(host, port);
      eng.setUseClientMode(true);
      Selector selector = Selector.open();
      SelectionKey key = sch.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
      SSLSocketChannel channel = new SSLSocketChannel(sch, eng, Executors.newFixedThreadPool(2), key);
      ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
      boolean writed = false;
      for(int i = 0; i < 100; i++) {
        if(selector.select() == 0) continue;
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while(it.hasNext()) {
          SelectionKey k = it.next();
          it.remove();
          if(k.isConnectable()) {
            sch.finishConnect();
          }
          else if(k.isReadable()) {
            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            System.out.println("* CHANNEL READING...");
            System.out.println(StandardCharsets.UTF_8.decode(buffer).toString());
          }
          else if(k.isWritable() && !writed) {
            channel.read(buffer);
            System.out.println("* CHANNEL WRITING...");
            StringBuilder sreq = new StringBuilder();
            sreq.append("GET https://").append(host).append("/pbb/pagina-inicial#/").append("/ HTTP/1.0\r\n")
                .append("Host: ").append(host).append("\r\n")
                .append("User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:62.0) Gecko/20100101 Firefox/62.0\r\n")
                .append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n")
                .append("Accept-Language: en-US,en;q=0.5\r\n")
                .append("Accept-Encoding: gzip, deflate, br\r\n")
                .append("Referer: http://").append(host).append("\r\n")
                .append("Proxy-Authorization: Basic ZjYwMzY0Nzc6OTYzMjU4OTY=\r\n")
                .append("Connection: keep-alive\r\n")
                .append("Upgrade-Insecure-Requests: 0\r\n")
                .append("\r\n\r\n");
            ByteBuffer req = StandardCharsets.UTF_8.encode(sreq.toString());
            channel.write(req);
          }
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
