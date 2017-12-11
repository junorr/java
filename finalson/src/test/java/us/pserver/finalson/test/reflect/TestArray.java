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

package us.pserver.finalson.test.reflect;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.finalson.json.ArrayType;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/12/2017
 */
public class TestArray {

  @Test
  public void isPrimitiveArray() {
    Class c = int[].class;
    Assertions.assertTrue(c.isArray());
    Assertions.assertTrue(c.getComponentType().isPrimitive());
  }
  
  @Test
  public void primitiveArray() {
    int[] array = {1,2,3,4,5};
    ArrayType atp = new ArrayType(this.getClass().getClassLoader());
    JsonElement elt = atp.toJson(array);
    System.out.println(elt);
    Assertions.assertTrue(JsonObject.class.isAssignableFrom(elt.getClass()));
    Assertions.assertEquals(5, ((JsonObject)elt).getAsJsonArray("array").size());
    Object o = atp.fromJson(elt);
    System.out.println(o);
  }
  
}
