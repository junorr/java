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

package us.pserver.test.mapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.LocalTime;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.mapping.ArrayMapping;
import us.pserver.finalson.mapping.LocalTimeMapping;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/02/2018
 */
public class TestArrayMapping {

  private final String[] sarray = {"a", "b", "c"};
  
  private final int[] iarray = {1, 2, 3};
  
  private final LocalTime[] tarray = {LocalTime.of(17, 16), LocalTime.of(16, 15), LocalTime.of(13, 12)};
  
  private final ArrayMapping map = new ArrayMapping(new FinalsonConfig());
  
  
  private JsonElement getStringJsonArray() {
    JsonObject job = new JsonObject();
    job.addProperty("@class", sarray.getClass().getName());
    JsonArray array = new JsonArray(3);
    array.add("a");
    array.add("b");
    array.add("c");
    job.add("array", array);
    return job;
  }
  
  private JsonElement getIntJsonArray() {
    JsonObject job = new JsonObject();
    job.addProperty("@class", iarray.getClass().getName());
    JsonArray array = new JsonArray(3);
    array.add(1);
    array.add(2);
    array.add(3);
    job.add("array", array);
    return job;
  }
  
  private JsonElement getLocalTimeJsonArray() {
    JsonObject job = new JsonObject();
    job.addProperty("@class", tarray.getClass().getName());
    JsonArray array = new JsonArray(3);
    LocalTimeMapping lmap = new LocalTimeMapping();
    array.add(lmap.toJson(LocalTime.of(17, 16)));
    array.add(lmap.toJson(LocalTime.of(16, 15)));
    array.add(lmap.toJson(LocalTime.of(13, 12)));
    job.add("array", array);
    return job;
  }
  
  
  @Test
  public void stringArrayToJson() {
    JsonElement elt = map.toJson(sarray);
    Assertions.assertEquals(getStringJsonArray(), elt);
  }
  
  @Test
  public void jsonToStringArray() {
    String[] array = (String[]) map.fromJson(getStringJsonArray());
    Assertions.assertTrue(Arrays.deepEquals(sarray, array));
  }
  
  
  @Test
  public void intArrayToJson() {
    JsonElement elt = map.toJson(iarray);
    Assertions.assertEquals(getIntJsonArray(), elt);
  }
  
  @Test
  public void jsonToIntArray() {
    int[] array = (int[]) map.fromJson(getIntJsonArray());
    Assertions.assertTrue(Arrays.equals(iarray, array));
  }
  
  
  @Test
  public void localTimeArrayToJson() {
    JsonElement elt = map.toJson(tarray);
    Assertions.assertEquals(getLocalTimeJsonArray(), elt);
  }
  
  @Test
  public void jsonToLocalTimeArray() {
    LocalTime[] array = (LocalTime[]) map.fromJson(getLocalTimeJsonArray());
    Assertions.assertTrue(Arrays.deepEquals(tarray, array));
  }
  
}
