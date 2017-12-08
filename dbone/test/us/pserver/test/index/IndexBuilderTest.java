/************************************************************************
 * Copyright (C) 2017 Juno Roelser - juno.rr@gmail.com                  *                            
 * This program is free software: you can redistribute it and/or modify *
 * it under the terms of the GNU General Public License as published by *
 * the Free Software Foundation, either version 3 of the License, or    *
 * (at your option) any later version.                                  *
 * This program is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of       *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the        *
 * GNU General Public License for more details.                         *
 * You should have received a copy of the GNU General Public License    *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.*
 ************************************************************************/

package us.pserver.test.index;

import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;
import junit.framework.Assert;
import org.junit.Test;
import us.pserver.dbone.index.IndexStore;
import us.pserver.test.bean.AObj;
import us.pserver.test.bean.BObj;
import us.pserver.dbone.index.IndexStore.DefIndexStore;
import us.pserver.dbone.store.Region;
import us.pserver.dbone.volume.Record;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 05/12/2017
 */
public class IndexBuilderTest {
  
  private final AObj a = new AObj("Aobj", 37, new int[]{3,5,7}, new char[]{'w','a','r'}, new Date());
  
  private final BObj b = new BObj("Bobj", a, Arrays.asList(3,5,7));

  private final AObj aa = new AObj("AAobj", 27, new int[]{5,5,6}, new char[]{'l','o','v','e'}, new Date());
  
  private final BObj bb = new BObj("BBobj", aa, Arrays.asList(5,5,6));

  private final IndexStore store = new DefIndexStore();

  @Test
  public void storeTestAObj() {
    Function<AObj,Integer> fn = AObj::age;
    store.appendIndexBuilder(AObj.class, "age", fn);
    store.putIndex(a, Record.of("A1", Region.of(0, 512)));
    store.putIndex(aa, Record.of("A2", Region.of(512, 512)));
    Assert.assertEquals(1, store.getIndex(AObj.class, Region.of(0, 512)).count());
    Assert.assertEquals(1, store.getIndex(AObj.class, Region.of(512, 512)).count());
    Assert.assertEquals(0, store.getIndex(AObj.class, Region.of(256, 512)).count());
    Assert.assertEquals(2, store.getIndex(AObj.class, "age").count());
    Assert.assertEquals(1, store.getIndex(AObj.class, "age", 37).count());
    Assert.assertEquals(1, store.getIndex(AObj.class, "age", 27).count());
    Assert.assertEquals(0, store.getIndex(AObj.class, "age", 17).count());
    Assert.assertEquals(1, store.removeIndex(AObj.class, "age", 27).size());
    Assert.assertEquals(1, store.getIndex(AObj.class, "age").count());
    Assert.assertNotNull(store.removeIndexBuilder(AObj.class, "age"));
  }
  
  @Test
  public void storeTestBObj() {
    Function<BObj,String> fn = BObj::getName;
    store.appendIndexBuilder(BObj.class, "name", fn);
    store.putIndex(b, Record.of("B1", Region.of(0, 512)));
    store.putIndex(bb, Record.of("B2", Region.of(512, 512)));
    Assert.assertEquals(1, store.getIndex(BObj.class, Region.of(0, 512)).count());
    Assert.assertEquals(1, store.getIndex(BObj.class, Region.of(512, 512)).count());
    Assert.assertEquals(0, store.getIndex(BObj.class, Region.of(256, 512)).count());
    Assert.assertEquals(2, store.getIndex(BObj.class, "name").count());
    Assert.assertEquals(1, store.getIndex(BObj.class, "name", "Bobj").count());
    Assert.assertEquals(1, store.getIndex(BObj.class, "name", "BBobj").count());
    Assert.assertEquals(0, store.getIndex(BObj.class, "name", "hello").count());
    Assert.assertEquals(1, store.removeIndex(BObj.class, "name", "Bobj").size());
    Assert.assertEquals(1, store.getIndex(BObj.class, "name").count());
    Assert.assertNotNull(store.removeIndexBuilder(BObj.class, "name"));
  }
  
}
