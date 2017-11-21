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

import java.util.Date;
import junit.framework.Assert;
import org.junit.Test;
import us.pserver.dbone.OUID;
import us.pserver.dbone.OUIDFactory;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/11/2017
 */
public class OUIDFactoryTest {

  private final AObj a = new AObj("hello", 37, new int[]{1, 2, 3}, new char[]{'a', 'b', 'c'}, new Date());
  
  private final OUID auid = OUIDFactory.create(a);
  
  private final int times = 1_000_000;
  
  
  @Test
  public void sameOUIDcalculation() {
    Timer tm = new Timer.Nanos().start();
    for(int i = 0; i < times; i++) {
      Assert.assertEquals(auid, OUIDFactory.create(a));
      tm.lap();
    }
    System.out.printf("sameOUIDcalculation( %s ): %s%n", auid, tm.stop());
  }
  
}
