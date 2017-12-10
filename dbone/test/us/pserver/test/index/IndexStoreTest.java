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

package us.pserver.test.index;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import junit.framework.Assert;
import org.junit.Test;
import us.pserver.date.SimpleDate;
import us.pserver.dbone.index.Index;
import us.pserver.dbone.index.IndexStore;
import us.pserver.test.bean.AObj;
import us.pserver.dbone.index.IndexStore.DefIndexStore;
import us.pserver.dbone.index.Indexed;
import us.pserver.dbone.index.MethodHandleUtils;
import us.pserver.dbone.store.Region;
import us.pserver.dbone.volume.Record;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2017
 */
public class IndexStoreTest {

  private final IndexStore store = new DefIndexStore();
  
  private final Date date = SimpleDate.parseDate("07/12/2017 14:00:00");
  
  private final AObj a = new AObj("Aobj", 37, new int[]{3,5,7}, new char[]{'w','a','r'}, date);
  
  private final AObj aa = new AObj("AAobj", 27, new int[]{5,5,6}, new char[]{'c','a','r'}, date);
  
  
  @Test
  public void reflectiveAgeIndex() {
    store.appendReflectiveIndexBuilder(AObj.class, "age");
    store.putIndex(a, Record.of("A1", Region.of(0, 512)));
    Optional<Index<Integer>> opt = store.getIndex(AObj.class, "age", 37).findAny();
    Assert.assertTrue(opt.isPresent());
    Assert.assertTrue(opt.get().test(37));
    Assert.assertEquals(Region.of(0, 512), opt.get().record().region());
    List<Index<Integer>> ls = store.removeIndex(AObj.class, "age", 37);
    Assert.assertEquals(1, ls.size());
    Assert.assertEquals(opt.get(), ls.get(0));
  }
  
  
  @Test
  public void acessorAgeIndex() {
    store.appendIndexBuilder(AObj.class, "age", AObj::age);
    store.putIndex(a, Record.of("A1", Region.of(0, 512)));
    Optional<Index<Integer>> opt = store.getIndex(AObj.class, "age", 37).findAny();
    Assert.assertTrue(opt.isPresent());
    Assert.assertTrue(opt.get().test(37));
    Assert.assertEquals(Region.of(0, 512), opt.get().record().region());
    List<Index<Integer>> ls = store.removeIndex(AObj.class, "age", 37);
    Assert.assertEquals(1, ls.size());
    Assert.assertEquals(opt.get(), ls.get(0));
  }
  
  
  @Test
  public void annotatedNameIndex() {
    MethodHandleUtils.getAnnotatedMethodHandlesWithName(
        AObj.class, Indexed.class, MethodHandles.lookup()
    ).forEach(System.out::println);
    store.appendAnnotatedIndexBuilder(AObj.class, MethodHandles.lookup());
    store.putIndex(a, Record.of("A1", Region.of(0, 512)));
    Optional<Index<String>> opt = store.getIndex(AObj.class, "name", "Aobj").findAny();
    Assert.assertTrue(opt.isPresent());
    Assert.assertTrue(opt.get().test("Aobj"));
    Assert.assertEquals(Region.of(0, 512), opt.get().record().region());
    List<Index<String>> ls = store.removeIndex(AObj.class, "name", "Aobj");
    Assert.assertEquals(1, ls.size());
    Assert.assertEquals(opt.get(), ls.get(0));
  }
  
}
