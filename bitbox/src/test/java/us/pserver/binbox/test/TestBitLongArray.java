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

package us.pserver.binbox.test;

import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.bitbox.array.BitLongArray;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2018
 */
public class TestBitLongArray {

  @Test
  public void testCreateFromByteBuffer() {
    long[] longs = new long[100_000];
    int idx = 0;
    Random rdm = new Random();
    Timer tm = new Timer.Nanos().start();
    PrimitiveIterator.OfLong it = rdm.longs(100_000).iterator();
    while(it.hasNext()) {
      longs[idx++] = it.nextLong();
    }
    tm.stop();
    System.out.printf("* Create random long array: %s%n", tm);
    longs[99] = 99;
    longs[107] = 107;
    tm.clear().start();
    BitLongArray array = BitLongArray.factory().createFrom(longs);
    tm.stop();
    System.out.printf("* Create BitLongArray: %s%n", tm);
    Assertions.assertEquals(Integer.BYTES * 3 + Long.BYTES * 100_000, array.toByteBuffer().limit());
    Assertions.assertEquals(Integer.BYTES * 3 + Long.BYTES * 100_000, array.boxSize());
    Assertions.assertEquals(100_000, array.length());
    Assertions.assertEquals(BitLongArray.ID, array.boxID());
    Assertions.assertArrayEquals(longs, array.toArray());
    Assertions.assertEquals(99, array.get(99));
    Assertions.assertEquals(107, array.get(107));
    Assertions.assertEquals(107, array.indexOf(107));
    AtomicInteger ix = new AtomicInteger(0);
    array.stream().forEach(i -> Assertions.assertEquals(longs[ix.getAndIncrement()], i));
  }
  
}
