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

package us.pserver.test.security;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.junit.jupiter.api.Test;
import us.pserver.micron.IgniteSetup;
import us.pserver.micron.config.MicronConfig;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.tools.misc.Sleeper;
import us.pserver.micron.security.User;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.micron.security.Security;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/01/2019
 */
public class TestIgniteSerialSupport {

  public static final String SECURITY_CACHE_USER = "security_user";
  
  public static final String SECURITY_CACHE_GROUP = "security_group";
  
  public static final String SECURITY_CACHE_ROLE = "security_role";
  
  public static final String SECURITY_CACHE_RESOURCE = "security_resource";
  
  @Test
  public void testCustomBeanSerializationOnDistributedCache() {
    MicronConfig mcf = MicronConfig.builder().buildFromAppYaml();
    try (Ignite ignite = IgniteSetup.start(mcf.getIgniteConfig())) {
      System.out.println("====== IGNITE SERIAL SUPPORT 111 ======");
      System.out.printf("- getStoragePath: %s%n", ignite.configuration().getDataStorageConfiguration().getStoragePath());
      System.out.printf("- getWalArchivePath: %s%n", ignite.configuration().getDataStorageConfiguration().getWalArchivePath());
      System.out.printf("- getWalPath: %s%n", ignite.configuration().getDataStorageConfiguration().getWalPath());

      IgniteCache<String,User> users = ignite.getOrCreateCache(SECURITY_CACHE_USER);
      mcf.getSecurityConfig().getUsers().forEach(u -> users.put(u.getName(), u.edit().build()));
      IgniteCache<String,Group> groups = ignite.getOrCreateCache(SECURITY_CACHE_GROUP);
      mcf.getSecurityConfig().getGroups().forEach(g -> groups.put(g.getName(), g.edit().build()));
      IgniteCache<String,Role> roles = ignite.getOrCreateCache(SECURITY_CACHE_ROLE);
      mcf.getSecurityConfig().getRoles().forEach(r -> roles.put(r.getName(), r.edit().build()));
      IgniteCache<String,Resource> resources = ignite.getOrCreateCache(SECURITY_CACHE_RESOURCE);
      mcf.getSecurityConfig().getResources().forEach(r -> resources.put(r.getName(), r.edit().build()));

      Security security = Security.create(SecurityConfig.of(ignite));

      System.out.println("====== users ======");
      System.out.println(security.getConfig().getUser("juno"));
      System.out.println(security.getConfig().getUser("john"));
      System.out.println("====== groups ======");
      System.out.println(security.getConfig().getGroup("root"));
      System.out.println(security.getConfig().getGroup("other"));
      System.out.println(security.getConfig().getGroup("johns"));
      System.out.println("====== roles ======");
      System.out.println(security.getConfig().getRole("admin"));
      System.out.println(security.getConfig().getRole("look"));
      System.out.println(security.getConfig().getRole("noJohns"));
      System.out.println("====== resources ======");
      System.out.println(security.getConfig().getResource("/manage"));
      System.out.println(security.getConfig().getResource("/look"));
      System.out.printf("* authorize( %s, %s ): %s%n", "/manage", "juno", security.authorize(
          security.getConfig().getResource("/manage").get(), 
          security.getConfig().getUser("juno").get())
      );
      System.out.printf("* authorize( %s, %s ): %s%n", "/manage", "john", security.authorize(
          security.getConfig().getResource("/manage").get(), 
          security.getConfig().getUser("john").get())
      );
      System.out.printf("* authorize( %s, %s ): %s%n", "/look", "juno", security.authorize(
          security.getConfig().getResource("/look").get(), 
          security.getConfig().getUser("juno").get())
      );
      System.out.printf("* authorize( %s, %s ): %s%n", "/look", "john", security.authorize(
          security.getConfig().getResource("/look").get(), 
          security.getConfig().getUser("john").get())
      );
      Sleeper.of(10000).sleep();
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
  
}
