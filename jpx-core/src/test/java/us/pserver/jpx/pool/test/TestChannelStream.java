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
import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import us.pserver.jpx.channel.Channel;
import us.pserver.jpx.channel.ChannelEngine;
import us.pserver.jpx.channel.ChannelStream;
import us.pserver.jpx.channel.impl.DefaultChannelConfiguration;
import us.pserver.jpx.channel.impl.DefaultChannelEngine;
import us.pserver.jpx.channel.impl.DefaultChannelStream;

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
    BiFunction<Channel,ByteBuffer,String> f1 = (c,b) -> {
      if(!c.getChannelStream().isInIOContext()) {
        c.getChannelStream().switchToIOContext(b);
        return null;
      }
      return StandardCharsets.UTF_8.decode(b).toString();
    };
    stream.append(f1);
    BiFunction<Channel,String,Integer> f2 = (c,s) -> {
      if(!c.getChannelStream().isInSytemContext()) {
        c.getChannelStream().switchToSystemContext(s);
        return null;
      }
      return s.length();
    };
    stream.append(f2);
    BiFunction<Channel,Integer,ByteBuffer> f3 = (c,i) -> {
      if(!c.getChannelStream().isInIOContext()) {
        c.getChannelStream().switchToIOContext(i);
        return null;
      }
      ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);
      b.putInt(i);
      b.flip();
      return b;
    };
    stream.append(f3);
    System.out.println(stream.apply(buf));
    } catch(Throwable t) {
      t.printStackTrace();
    }
  }
  
}
