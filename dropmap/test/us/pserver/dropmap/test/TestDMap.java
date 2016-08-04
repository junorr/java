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
import us.pserver.dropmap.DMap;
import us.pserver.dropmap.DMap.DEntry;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2016
 */
public class TestDMap {
  
  
  public static void removed(DEntry<Integer,String> entry) {
    System.out.println("* Removed: "+ entry);
  }

  
  public static void main(String[] args) throws InterruptedException {
    DMap<Integer,String> map = DMap.newMap();
    map.put(0, "zero", Duration.ofSeconds(2), TestDMap::removed);
    map.put(1, "one", Duration.ofMillis(2250), TestDMap::removed);
    map.put(2, "two", Duration.ofMillis(2750), TestDMap::removed);
    map.put(3, "three", Duration.ofSeconds(3), TestDMap::removed);
    System.out.println("* map.size: "+ map.size());
    Thread.sleep(3100);
    System.out.println("* map.size: "+ map.size());
  }
  
}
