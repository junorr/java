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

import com.google.gson.JsonElement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import us.pserver.finalson.mapping.JavaPrimitive;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/12/2017
 */
public class TestJavaPrimitive {

  @Test
  public void doubleMapping() {
    Double d = 5.551;
    JsonElement e = JavaPrimitive.javaToJson(d);
    System.out.println(e);
    Number read = (Number) JavaPrimitive.jsonToJava(e.getAsJsonPrimitive());
    assertEquals(d, read);
  }

  @Test
  public void intMapping() {
    Integer d = 1001;
    JavaPrimitive java = JavaPrimitive.INT;
    System.out.println(java);
    JsonElement e = java.toJson(d);
    System.out.println(e);
    Number read = (Number) JavaPrimitive.jsonToJava(e.getAsJsonPrimitive());
    assertEquals(d, read);
  }

  @Test
  public void booleanMapping() {
    Boolean d = true;
    JsonElement e = JavaPrimitive.javaToJson(d);
    System.out.println(e);
    Boolean read = (Boolean) JavaPrimitive.jsonToJava(e.getAsJsonPrimitive());
    assertEquals(d, read);
  }
  
}
