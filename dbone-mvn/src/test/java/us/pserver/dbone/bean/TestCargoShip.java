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

package us.pserver.dbone.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.dbone.obj.ObjectMapperConfig;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public class TestCargoShip {
  
  private final CargoShip ship = createCargoShip();
  
  private final String json = "{\"name\":\"LastBottle\",\"maxWeight\":163.8,\"deck\":{\"maxSize\":3,\"load\":[{\"name\":\"Fruits\",\"value\":\"Banana\",\"weight\":55.2},{\"name\":\"Fruits\",\"value\":\"Orange\",\"weight\":48.6},{\"name\":\"Veggies\",\"value\":\"Potato\",\"weight\":60.0}]}}";
  
  
  private static CargoShip createCargoShip() {
    List<Container> cts = new LinkedList<>();
    cts.add(new Container("Fruits", "Banana", 55.2));
    cts.add(new Container("Fruits", "Orange", 48.6));
    cts.add(new Container("Veggies", "Potato", 60.0));
    Deck deck = new Deck(cts);
    return new CargoShip("LastBottle", deck);
  }
  
  @Test
  public void cargoShipToJson() throws JsonProcessingException, IOException {
    String js = ObjectMapperConfig.MAPPER_INSTANCE.get().writer().writeValueAsString(ship);
    Assertions.assertEquals(json, js);
  }
  
  @Test
  public void jsonToCargoShip() throws JsonProcessingException, IOException {
    CargoShip cs = ObjectMapperConfig.MAPPER_INSTANCE.get().readerFor(CargoShip.class).readValue(json);
    Assertions.assertEquals(ship, cs);
  }
  
}
