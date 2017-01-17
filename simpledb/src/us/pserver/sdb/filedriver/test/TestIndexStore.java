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

package us.pserver.sdb.filedriver.test;

import us.pserver.sdb.filedriver.IndexStore;
import us.pserver.sdb.filedriver.Region;
import us.pserver.tools.RoundDouble;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/12/2016
 */
public class TestIndexStore {

  
  
  public static void main(String[] args) {
    IndexStore<Double> store = IndexStore.newStore();
    String name = "measurement";
    double val = 52.7;
    long start = 0;
    long len = 512;
    for(int i = 0; i < 5; i++) {
      val = RoundDouble.round(val + Math.random() * 10, 4);
      store.newIndex()
          .withName(name)
          .withValue(val)
          .addRegion(Region.of(start, len))
          .addRegion(Region.of(start+i*10, len+i*10))
          .insert();
      start += len;
    }
    name = "capacity";
    for(int i = 0; i < 5; i++) {
      val = RoundDouble.round(val + Math.random() * 10, 4);
      store.newIndex()
          .withName(name)
          .withValue(val)
          .addRegion(Region.of(start, len))
          .addRegion(Region.of(start+i*10, len+i*10))
          .insert();
      start += len;
    }
    name = "accuracy";
    for(int i = 0; i < 5; i++) {
      val = RoundDouble.round(val + Math.random() * 10, 4);
      store.newIndex()
          .withName(name)
          .withValue(val)
          .addRegion(Region.of(start, len))
          .addRegion(Region.of(start+i*10, len+i*10))
          .insert();
      start += len;
    }
    System.out.println(store);
    System.out.println();
    
    System.out.println("* store.find(\"measurement\", v->v > 50.0 && v < 65.0):");
    System.out.println("   "+ store.query().find("measurement", v->v > 50.0 && v < 65.0));
    System.out.println("* store.remove(\"measurement\", v->v > 50.0 && v < 65.0):");
    System.out.println("   "+ store.remove("measurement", v->v > 50.0 && v < 65.0));
    System.out.println(store);
  }
  
  
}
