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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.mapping.ObjectJsonMapping;
import us.pserver.finalson.test.bean.AObj;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/12/2017
 */
public class TestObjectJsonMapping {
  
  private final FinalsonConfig config = new FinalsonConfig().setUseGetters(true).setUseMethodAnnotation(true);
  
  private final ObjectJsonMapping omap = new ObjectJsonMapping(config);
  
  private final AObj a = new AObj("aobj", 55, new int[]{5,5}, new char[]{'a','b'}, new Date());
  
  private List<String> meths = Arrays.asList(
      "name", "age", "magic", "chars", "date", "class"
  );
  
  
  @Test
  public void mapAtoJson() {
    System.out.println(omap.toJson(a));
  }

}
