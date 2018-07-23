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

package us.pserver.ignite.test.test;

import java.io.IOException;
import java.nio.file.Files;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.jupiter.api.Test;
import us.pserver.ignite.test.AccessRule;
import us.pserver.ignite.test.Group;
import us.pserver.ignite.test.RuledResource;
import us.pserver.ignite.test.User;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2018
 */
public class TestIgnite {

  public static IgniteCache<String, RuledResource> getCache() throws IOException {
    IgniteConfiguration ic = new IgniteConfiguration();
    DataStorageConfiguration dc = new DataStorageConfiguration();
    dc.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
    dc.setStoragePath(Files.createTempDirectory(
        String.format("ignite-test_%d", System.currentTimeMillis())
    ).toAbsolutePath().toString());
    ic.setDataStorageConfiguration(dc);
    Ignite ig = Ignition.getOrStart(ic);
    ig.cluster().active(true);
    return ig.getOrCreateCache("access");
  }
  
  @Test
  public void testIgniteCache() throws IOException {
    IgniteCache<String, RuledResource> cache1 = getCache();
    IgniteCache<String, RuledResource> cache2 = getCache();
    try {
      User juno = new User("juno", "secret");
      User john = new User("john", "1234");
      Group admin = new Group("admin", juno);
      Group readOnly = new Group("readOnly", juno, john);
      AccessRule acsAdmin = new AccessRule(admin, "rwx");
      AccessRule acsRead = new AccessRule(readOnly, "r--");
      cache1.put("system", new RuledResource("system", acsAdmin, acsRead));

      System.out.println(cache2.get("system"));
    }
    finally {
      cache1.close();
      cache2.close();
    }
  }
  
}
