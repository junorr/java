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
import us.pserver.bitbox.BitDoubleArray;
import us.pserver.bitbox.BitLongArray;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2018
 */
public class TestBitDoubleArray {

  @Test
  public void testCreateFromByteBuffer() {
    double[] doubles = new double[100_000];
    int idx = 0;
    Random rdm = new Random();
    Timer tm = new Timer.Nanos().start();
    PrimitiveIterator.OfDouble it = rdm.doubles(100_000).iterator();
    while(it.hasNext()) {
      doubles[idx++] = it.nextDouble();
    }
    tm.stop();
    System.out.printf("* Create random double array: %s%n", tm);
    doubles[99] = 99.1;
    doubles[107] = 107.1;
    tm.clear().start();
    BitDoubleArray array = BitDoubleArray.factory().createFrom(doubles);
    tm.stop();
    System.out.printf("* Create BitDoubleArray: %s%n", tm);
    Assertions.assertEquals(Integer.BYTES * 3 + Double.BYTES * 100_000, array.toByteBuffer().limit());
    Assertions.assertEquals(Integer.BYTES * 3 + Double.BYTES * 100_000, array.boxSize());
    Assertions.assertEquals(100_000, array.length());
    Assertions.assertEquals(BitDoubleArray.ID, array.boxID());
    Assertions.assertArrayEquals(doubles, array.toArray());
    Assertions.assertEquals(99.1, array.get(99));
    Assertions.assertEquals(107.1, array.get(107));
    Assertions.assertEquals(107, array.indexOf(107.1));
    AtomicInteger ix = new AtomicInteger(0);
    array.stream().forEach(i -> Assertions.assertEquals(doubles[ix.getAndIncrement()], i));
  }
  
}
