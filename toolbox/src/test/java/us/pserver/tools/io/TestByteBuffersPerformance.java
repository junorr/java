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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/12/2018
 */
public class TestByteBuffersPerformance {
  
  public static final int MAX_LONGS = 1_000_000;

  private static final List<Long> LONGS = createLongs();
  
  private static final List<Long> outlongs1 = new ArrayList<>(MAX_LONGS);
  
  private static final List<Long> outlongs2 = new ArrayList<>(MAX_LONGS);
  
  
  private static List<Long> createLongs() {
    ByteBuffer bs = StandardCharsets.UTF_8.encode("Hello World!");
    System.out.println("* encoded string buffer: " + bs);
    List<Long> longs = new ArrayList<>(MAX_LONGS);
    Random rd = new Random(System.currentTimeMillis());
    LongStream stream = rd.longs(MAX_LONGS);
    stream.forEach(l -> longs.add(l));
    printSamples(longs);
    return longs;
  }
  
  
  private static void printSamples(List<Long> longs) {
    int mod = 100_000;
    for(int i = 0; i < longs.size(); i++) {
      if(i % mod == 0) System.out.printf("* Long Sample %d [%d]%n", i, longs.get(i));
    }
  }
  
  
  @Test
  public void putHeapDynamicByteBuffer() {
    System.out.println("========= PUT HEAP =========");
    DynamicByteBuffer buffer = new DynamicByteBuffer(ByteBuffer.allocate(80));
    System.out.println(buffer);
    Timer tm = new Timer.Nanos();
    Iterator<Long> it = LONGS.iterator();
    tm.start();
    while(it.hasNext()) {
      buffer.putLong(it.next());
    }
    tm.stop();
    System.out.println(buffer);
    System.out.println(tm);
    System.out.println("____________________________");
  }
  
  
  @Test
  public void putDirectDynamicByteBuffer() {
    System.out.println("======== PUT DIRECT ========");
    DynamicByteBuffer buffer = new DynamicByteBuffer(ByteBuffer.allocateDirect(80));
    System.out.println(buffer);
    Timer tm = new Timer.Nanos();
    Iterator<Long> it = LONGS.iterator();
    tm.start();
    while(it.hasNext()) {
      buffer.putLong(it.next());
    }
    tm.stop();
    System.out.println(buffer);
    System.out.println(tm);
    System.out.println("____________________________");
  }
  
  
  @Test
  public void getHeapDynamicByteBuffer() {
    System.out.println("========= GET HEAP =========");
    DynamicByteBuffer buffer = new DynamicByteBuffer(ByteBuffer.allocate(80));
    System.out.println(buffer);
    Iterator<Long> it = LONGS.iterator();
    while(it.hasNext()) {
      buffer.putLong(it.next());
    }
    buffer.flip();
    Timer tm = new Timer.Nanos();
    tm.start();
    while(buffer.remaining() >= 8) {
      outlongs1.add(buffer.getLong());
    }
    tm.stop();
    printSamples(outlongs1);
    System.out.println(buffer);
    System.out.println(tm);
    System.out.println("____________________________");
  }
  
  
  @Test
  public void getDirectDynamicByteBuffer() {
    System.out.println("======== GET DIRECT ========");
    DynamicByteBuffer buffer = new DynamicByteBuffer(ByteBuffer.allocateDirect(80));
    System.out.println(buffer);
    Iterator<Long> it = LONGS.iterator();
    while(it.hasNext()) {
      buffer.putLong(it.next());
    }
    buffer.flip();
    Timer tm = new Timer.Nanos();
    tm.start();
    while(buffer.remaining() >= 8) {
      outlongs2.add(buffer.getLong());
    }
    tm.stop();
    printSamples(outlongs2);
    System.out.println(buffer);
    System.out.println(tm);
    System.out.println("____________________________");
  }
  
}
