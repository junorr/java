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

package us.pserver.dbone.index;

import java.lang.reflect.Method;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Test;
import us.pserver.date.SimpleDate;
import us.pserver.dbone.bean.AObj;
import us.pserver.dbone.store.Region;
import us.pserver.dbone.volume.Record;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2017
 */
public class ReflectIndexBuilderTest {

  private final IndexBuilder ageBuilder = new ReflectIndexBuilder("age");
  
  private final IndexBuilder dateBuilder = new ReflectIndexBuilder("date");
  
  private final Date date = SimpleDate.parseDate("07/12/2017 14:00:00");
  
  private final AObj a = new AObj("Aobj", 37, new int[]{3,5,7}, new char[]{'w','a','r'}, date);
  
  
  @Test
  public void ageIndexBuilding() {
    Index idx = ageBuilder.build(a, Record.of("A1", Region.of(0, 512)));
    Assert.assertEquals(37, idx.value());
    System.out.println("ageIndexBuilding : "+ idx);
    Assert.assertTrue(idx.test(37));
  }
  
  
  @Test
  public void dateIndexBuilding() {
    Index idx = dateBuilder.build(a, Record.of("A1", Region.of(512, 512)));
    Assert.assertEquals(date, idx.value());
    System.out.println("dateIndexBuilding: "+ idx);
    Assert.assertTrue(idx.test(date));
  }
  
}
