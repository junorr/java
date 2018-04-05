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

package us.pserver.dropmap.test;

import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.dropmap.DMap;
import us.pserver.dropmap.DMap.DEntry;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2016
 */
public class TestDMap {
  
  private static final double trash = 10.0;
  
  private static double lastlap;
  
  private static double elapsedStart;
  
  
  public static void removed(DEntry<Integer,String> entry) {
    double end = System.nanoTime();
    double ms = ((end - lastlap) / 1_000_000.0);
    if(ms > 1002) {
      System.out.println("\n* Removed: "+ entry+ " (elapsed: "+ ms+ " ms)");
    } else {
      System.out.print(entry.getKey()+ ".");
    }
    lastlap = end;
  }

  
  public static void removedInfo(DEntry<Integer,String> entry) {
    double end = System.nanoTime();
    double elapsed = ((end - elapsedStart) / 1_000_000.0);
    double ms = ((end - lastlap) / 1_000_000.0);
    System.out.println("\n* Removed: "+ entry+ " (last-lap: "+ ms+ " ms, elapsed: "+ elapsed+ " ms)");
    lastlap = end;
    Assertions.assertTrue(elapsed < (entry.getDuration().toMillis() + trash));
  }

  @Test
  public void testTTL() throws InterruptedException {
    DMap<Integer,String> map = DMap.newMap();
    long ttl = 250;
    for(int i = 0; i < 50; i++) {
      map.put(i, "string-"+String.valueOf(i), Duration.ofMillis(ttl), TestDMap::removedInfo);
      ttl += 250;
      if(i == 0) {
        elapsedStart = System.nanoTime();
        lastlap = System.nanoTime();
      }
    }
    System.out.println("* map.size: "+ map.size());
    Thread.sleep(12600);
    System.out.println();
    System.out.println("* map.size: "+ map.size());
  }
  
}
