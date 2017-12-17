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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.mapping.ObjectJsonMapping;
import us.pserver.finalson.strategy.JavaMappingStrategy;
import us.pserver.finalson.strategy.MethodHandleInfo;
import us.pserver.finalson.test.bean.AObj;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/12/2017
 */
public class TestJavaMappingStrategy {
  
  private final FinalsonConfig config = new FinalsonConfig().setUseGetters(true).setUseMethodAnnotation(true);
  
  private final ObjectJsonMapping omap = new ObjectJsonMapping(config);
  
  private final JavaMappingStrategy jas = new JavaMappingStrategy(config);
  
  private final AObj a = new AObj("aobj", 55, new int[]{5,5}, new char[]{'a','b'}, new Date());
  
  
  @Test
  public void mapAtoJson() {
    JsonElement elt = omap.toJson(a);
    System.out.println(elt);
    List<MethodHandleInfo> infos = jas.apply(elt);
    System.out.println("--------------------");
    infos.forEach(System.out::println);
    System.out.println("--------------------");
    assertEquals(3, infos.size());
    Optional<MethodHandleInfo> allParams = infos.stream().max(
        (x,y)->Integer.compare(x.getParameters().size(), y.getParameters().size())
    );
    assertTrue(allParams.isPresent());
    assertEquals(5, allParams.get().getParameters().size());
  }

}
