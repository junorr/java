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

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.finalson.test.bean.AObj;
import us.pserver.finalson.test.bean.BObj;
import us.pserver.tools.date.SimpleDate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/03/2018
 */
public class TestGson {

  private final Date date = SimpleDate.parseDate("30/03/2018 21:20:19");
  
  private final AObj A = new AObj("juno", 38, new int[] {1,2,3}, new char[] {'a', 'b', 'c'}, date);
  
  private final BObj B = new BObj("b", A, Arrays.asList(4,3,2));
  
  private final String Bjson = "{\"name\":\"b\",\"a\":{\"name\":\"juno\",\"age\":38,\"magic\":[1,2,3],\"chars\":[\"a\",\"b\",\"c\"],\"date\":\"Mar 30, 2018 9:20:19 PM\"},\"list\":[4,3,2]}";
  
  private final Gson gson = new Gson();
  
  
  @Test
  public void toJson() {
    String json = gson.toJson(B);
    System.out.println(json);
    Assertions.assertEquals(Bjson, json);
  }
  
  @Test
  public void fromJson() {
    BObj b = gson.fromJson(Bjson, BObj.class);
    Assertions.assertEquals(B, b);
  }
  
}
