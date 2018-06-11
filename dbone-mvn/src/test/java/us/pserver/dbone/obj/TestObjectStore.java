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
import java.util.Optional;
import org.junit.jupiter.api.Test;
import us.pserver.dbone.bean.CargoShip;
import us.pserver.dbone.bean.Container;
import us.pserver.dbone.bean.Deck;
import us.pserver.dbone.store.FileChannelStorage;
import us.pserver.dbone.region.Region;
import us.pserver.dbone.store.Storage;
import us.pserver.dbone.util.Log;
import us.pserver.tools.misc.RandomString;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public class TestObjectStore {
  
  private static final String[] shipNames = {"Short", "Heavy", "Black", "Ocean", "Blue", "Cruiser"};
  
  private static final String[] contNames = {"Fruits", "Veggies", "Tractor Parts", "Explosives", "Cars"};
  
  private static final Path path = Paths.get("d:/objectStore.bin");

  
  private static ObjectStore createStore() {
    try {
      Storage stg = FileChannelStorage.builder()
          .withBufferAllocPolicy(ByteBuffer::allocateDirect)
          .create(path, 1024);
      return new DefaultObjectStore(stg);
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  private static ObjectStore openStore() {
    try {
      Storage stg = FileChannelStorage.builder()
          .withBufferAllocPolicy(ByteBuffer::allocateDirect)
          .open(path);
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
        + " " + RandomString.of(5, RandomString.StringCase.FIRST_UPPER).generate();
  }
  
  private static CargoShip blackBobCargoShip() {
    List<Container> cts = new LinkedList<>();
    while(cts.stream().reduce(0.0, (w,c)->w + c.weight(), (w,t)->w + t) < 300) {
      Sleeper.of(100).sleep();
      cts.add(new Container(randomContainerName(), RandomString.of(5).generate(), Math.random()*150));
    }
    Deck deck = new Deck(cts);
    return new CargoShip("Black Bob", deck);
  }
  
  private static CargoShip randomCargoShip() {
    List<Container> cts = new LinkedList<>();
    int max = Double.valueOf(Math.random() * 5 + 1).intValue();
    for(int i = 0; i < max; i++) {
      Sleeper.of(100).sleep();
      cts.add(new Container(randomContainerName(), RandomString.of(5).generate(), Math.random()*100));
    }
    Deck deck = new Deck(cts);
    return new CargoShip(randomShipName(), deck);
  }
  
  @Test
  public void storeCargoShips() throws IOException, ClassNotFoundException {
    try {
      ObjectStore store = createStore();
      CargoShip ship = randomCargoShip();
      Record<CargoShip> ra = store.put(ship);
      System.out.printf("* store.put( %s ): %s%n", ship, ra);

      Sleeper.of(100).sleep();
      ship = randomCargoShip();
      Record<CargoShip> rb = store.put(ship);
      System.out.printf("* store.put( %s ): %s%n", ship, rb);

      Sleeper.of(100).sleep();
      ship = blackBobCargoShip();
      Record<CargoShip> rc = store.put(ship);
      System.out.printf("* store.put( %s ): %s%n", ship, rc);

      ship = (CargoShip) store.remove(rb.getRegion()).getValue();
      System.out.printf("* store.remove( %s ): %s%n", rb, ship);
      store.close();

      Sleeper.of(100).sleep();
      store = openStore();
      ship = store.get(ra.getRegion());
      System.out.printf("* store.get( %s ): %s%n", ra, ship);

      ship = store.get(rc.getRegion());
      System.out.printf("* store.get( %s ): %s%n", rc, ship);

      Container<String> ctn = new Container(randomContainerName(), RandomString.of(5).generate(), Math.random()*100);
      Record<Container> rbb = store.put(ctn);
      System.out.printf("* store.put( %s ): %s%n", ctn, rbb);
      store.close();

      Sleeper.of(100).sleep();
      store = openStore();
      ctn = store.get(rb.getRegion());
      System.out.printf("* store.get( %s ): %s%n", rb, ctn);
      store.close();

      Sleeper.of(100).sleep();
      store = openStore();
      Optional<Record<CargoShip>> opt = store
          .streamOf(CargoShip.class)
          .filter(r->r.getValue().getName().equals("Black Bob"))
          .findAny();
      System.out.println(opt);
      Record rec = (Record<Container>) store
          .streamAll()
          .filter(r->Container.class.isAssignableFrom(r.getValueClass()))
          .findAny().get();
      System.out.println(rec);
      System.out.println(rec.getValue());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
