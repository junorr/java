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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.stream.ChannelStream;
import us.pserver.jpx.channel.stream.StreamPartial;
import us.pserver.jpx.channel.impl.DefaultChannelConfiguration;
import us.pserver.jpx.channel.impl.DefaultChannelEngine;
import us.pserver.jpx.channel.impl.DefaultChannelStream;
import us.pserver.jpx.channel.stream.StreamFunction;
import us.pserver.jpx.log.Log;
import us.pserver.jpx.log.Logger;
import us.pserver.jpx.pool.Pooled;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/08/2018
 */
public class TestChannelStream {
  
  private final Channel channel = Mockito.mock(Channel.class);
  
  private final ChannelStream stream = new DefaultChannelStream(channel);
  
  private final ChannelEngine engine = new DefaultChannelEngine(new DefaultChannelConfiguration());
  
  
  @Test
  public void test() {
    try {
      Mockito.when(channel.getChannelEngine()).thenReturn(engine);
      Logger.LEVELS.remove(Log.Level.DEBUG);
      stream.addListener((s,e) -> {
        //String[] ss = e.toString().split(",");
        //Logger.info("%s: %s", Thread.currentThread().getName(), ss[0]);
        Logger.info("%s", e.toString());
        //Arrays.asList(ss).stream().skip(1).forEach(o -> System.out.printf("    - %s%n%n", o));
      });
      Pooled<ByteBuffer> pbuf = engine.getByteBufferPool().allocAwait();
      ByteBuffer buf = StandardCharsets.UTF_8.encode("Hello World!");
      pbuf.get().put(buf);
      pbuf.get().flip();
      StreamFunction<Pooled<ByteBuffer>,String> f1 = (c,o) -> {
        if(!c.isInIOContext()) {
          c.switchToIOContext(o);
          return StreamPartial.brokenStream();
        }
        try {
          return StreamPartial.activeStream(StandardCharsets.UTF_8.decode(o.get().get()).toString());
        }
        finally {
          o.get().release();
        }
      };
      stream.appendFunction(f1);
      StreamFunction<String,Integer>  f2 = (c,o) -> {
        if(!c.isInSytemContext()) {
          c.switchToSystemContext(o);
          return StreamPartial.brokenStream();
        }
        return StreamPartial.activeStream(o.get().length());
      };
      stream.appendFunction(f2);
      StreamFunction<Integer,ByteBuffer>  f3 = (c,o) -> {
        if(!c.isInIOContext()) {
          c.switchToIOContext(o);
          return StreamPartial.brokenStream();
        }
        ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);
        b.putInt(o.get());
        b.flip();
        return StreamPartial.activeStream(b);
      };
      stream.appendFunction(f3);
      StreamFunction<ByteBuffer,Void>  f4 = (c,o) -> {
        if(!c.isInIOContext()) {
          c.switchToIOContext(o);
          return StreamPartial.brokenStream();
        }
        System.out.println("* f4: " + o);
        return StreamPartial.brokenStream();
      };
      stream.appendFunction(f4);
      //stream.run(buf);
      stream.runSync(pbuf);
      System.out.println("-- runSync exited --");
      //Sleeper.of(1000).sleep();
    } catch(Throwable t) {
      t.printStackTrace();
    }
  }
  
}
