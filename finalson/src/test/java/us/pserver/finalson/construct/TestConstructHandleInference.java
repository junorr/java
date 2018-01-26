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

package us.pserver.finalson.construct;

import com.google.gson.JsonObject;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.finalson.FinalsonConfig;
import us.pserver.finalson.test.bean.AObj;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/01/2018
 */
public class TestConstructHandleInference {
  
  private final FinalsonConfig config = new FinalsonConfig()
      .setUseGetters(true)
      .withClassLoader(AObj.class.getClassLoader());

  @Test
  public void defaultConstructInference() {
    JsonObject job = new JsonObject();
    job.addProperty("name", "Juno");
    job.addProperty("date", "2018-01-26T10:29:00");
    DefaultConstructInference inf = new DefaultConstructInference(config, AObj.class, job);
    List<ConstructParam> pars = inf.infer().getParameters();
    System.out.printf("* infer(): %s( %s )%n", AObj.class.getSimpleName(), pars);
    Assertions.assertEquals(2, pars.size());
    Assertions.assertEquals("name", pars.get(0).getJsonProperty().getName());
    Assertions.assertEquals("date", pars.get(1).getJsonProperty().getName());
  }
  
}
