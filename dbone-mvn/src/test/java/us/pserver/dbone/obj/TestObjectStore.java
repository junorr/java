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

package us.pserver.dbone.obj;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import us.pserver.dbone.bean.CargoShip;
import us.pserver.dbone.bean.Container;
import us.pserver.dbone.bean.Deck;
import us.pserver.dbone.store.FileChannelStorage;
import us.pserver.dbone.store.Region;
import us.pserver.dbone.store.Storage;
import us.pserver.tools.misc.RandomString;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public class TestObjectStore {
  
  private static final String[] shipNames = {"Short", "Heavy", "Black"};
  
  private static final String[] contNames = {"Fruits", "Veggies", "Car Parts"};
  
  private static final Path path = Paths.get("d:/objectStore.bin");

  
  private static ObjectStore createStore() {
    try {
      Storage stg = FileChannelStorage.builder().withBufferAllocPolicy(ByteBuffer::allocate).create(path, 1024);
      return new DefaultObjectStore(stg);
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  private static ObjectStore openStore() {
    try {
      Storage stg = FileChannelStorage.builder().open(path);
      return new DefaultObjectStore(stg);
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  private static String randomContainerName() {
    return contNames[Double.valueOf(Math.random()*3).intValue()];
  }
  
  private static String randomShipName() {
    return shipNames[Double.valueOf(Math.random()*3).intValue()] 
        + RandomString.of(5, RandomString.StringCase.FIRST_UPPER).generate();
  }
  
  private static CargoShip randomCargoShip() {
    List<Container> cts = new LinkedList<>();
    int max = Double.valueOf(Math.random() * 5 + 1).intValue();
    for(int i = 0; i < max; i++) {
      cts.add(new Container(randomContainerName(), RandomString.of(5).generate(), Math.random()*100));
    }
    Deck deck = new Deck(cts);
    return new CargoShip(randomShipName(), deck);
  }
  
  @Test
  public void storeCargoShips() throws IOException, ClassNotFoundException {
    ObjectStore store = createStore();
    CargoShip ship = randomCargoShip();
    Region ra = store.put(ship);
    System.out.printf("* store.put( %s ): %s%n", ship, ra);
    
    ship = randomCargoShip();
    Region rb = store.put(ship);
    System.out.printf("* store.put( %s ): %s%n", ship, rb);
    
    ship = randomCargoShip();
    Region rc = store.put(ship);
    System.out.printf("* store.put( %s ): %s%n", ship, rc);
    
    ship = store.remove(rb);
    System.out.printf("* store.remove( %s ): %s%n", rb, ship);
    store.close();
    
    store = openStore();
    ship = store.get(ra);
    System.out.printf("* store.get( %s ): %s%n", ra, ship);
    
    ship = store.get(rc);
    System.out.printf("* store.get( %s ): %s%n", rc, ship);
    
    ship = randomCargoShip();
    Region r = store.put(ship);
    System.out.printf("* store.put( %s ): %s%n", ship, r);
    store.close();
    
    store = openStore();
    ship = store.get(rb);
    System.out.printf("* store.get( %s ): %s%n", rb, ship);
    store.close();
  }
  
}
