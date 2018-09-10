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
import java.util.Optional;
import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelStream;
import us.pserver.jpx.channel.StreamPartial;
import us.pserver.jpx.channel.impl.DefaultChannelConfiguration;
import us.pserver.jpx.channel.impl.DefaultChannelEngine;
import us.pserver.jpx.channel.impl.DefaultChannelStream;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/08/2018
 */
public class TestChannelStream {
  
  private final Channel channel = Mockito.mock(Channel.class);
  
  private final ChannelStream stream = new DefaultChannelStream(channel);
  
  private static ChannelEngine engine = new DefaultChannelEngine(new DefaultChannelConfiguration());
  
  
  @Test
  public void test() {
    try {
    Mockito.when(channel.getChannelStream()).thenReturn(stream);
    Mockito.when(channel.getChannelEngine()).thenReturn(engine);
    System.out.println(channel.getChannelStream());
    ByteBuffer buf = StandardCharsets.UTF_8.encode("Hello World!");
    BiFunction<Channel,Optional<ByteBuffer>,StreamPartial<String>> f1 = (c,o) -> {
      if(!c.getChannelStream().isInIOContext()) {
        c.getChannelStream().switchToIOContext(o);
        return StreamPartial.brokenStream();
      }
      return StreamPartial.activeStream(StandardCharsets.UTF_8.decode(o.get()).toString());
    };
    stream.append(f1);
    BiFunction<Channel,Optional<String>,StreamPartial<Integer>> f2 = (c,o) -> {
      if(!c.getChannelStream().isInSytemContext()) {
        c.getChannelStream().switchToSystemContext(o);
        return StreamPartial.brokenStream();
      }
      return StreamPartial.activeStream(o.get().length());
    };
    stream.append(f2);
    BiFunction<Channel,Optional<Integer>,StreamPartial<ByteBuffer>> f3 = (c,o) -> {
      if(!c.getChannelStream().isInIOContext()) {
        c.getChannelStream().switchToIOContext(o);
        return StreamPartial.brokenStream();
      }
      ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);
      b.putInt(o.get());
      b.flip();
      return StreamPartial.activeStream(b);
    };
    stream.append(f3);
    BiFunction<Channel,Optional<ByteBuffer>,StreamPartial<Void>> f4 = (c,o) -> {
      if(!c.getChannelStream().isInIOContext()) {
        c.getChannelStream().switchToIOContext(o);
        return StreamPartial.brokenStream();
      }
      System.out.println("* f4: " + o);
      return StreamPartial.brokenStream();
    };
    stream.append(f4);
    System.out.println(stream.runSync(buf));
    Sleeper.of(10000).sleep();
    //System.out.println(stream.runSync(buf));
    } catch(Throwable t) {
      t.printStackTrace();
    }
  }
  
}
