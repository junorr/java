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

package us.pserver.tools.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public class TestByteBufferOutputStream {
  
  private static final int max = 10_000_000;
  
  private static final List<ByteBuffer> bufs = createBufferList();
  
  private static List<ByteBuffer> createBufferList() {
    Random rdm = new Random(System.currentTimeMillis());
    List<ByteBuffer> bufs = new ArrayList<>(max);
    rdm.longs(max).forEach(l -> {
      ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
      buf.putLong(l);
      buf.flip();
      bufs.add(buf);
    });
    return bufs;
  }

  @Test
  public void testByteBufferOutputStreamPerformance() {
    ByteBufferOutputStream bos = new ByteBufferOutputStream();
    Timer tm = new Timer.Nanos().start();
    bufs.forEach(b -> bos.write(b));
    tm.stop();
    System.out.printf("* testByteBufferOutputStreamPerformance: %s%n", tm);
  }
  
  @Test
  public void testByteArrayOutputStreamPerformance() {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Timer tm = new Timer.Nanos().start();
    bufs.forEach(b -> {
      try { bos.write(b.array()); }
      catch(IOException e) { e.printStackTrace(); }
    });
    tm.stop();
    System.out.printf("* testByteArrayOutputStreamPerformance: %s%n", tm);
  }
  
}
