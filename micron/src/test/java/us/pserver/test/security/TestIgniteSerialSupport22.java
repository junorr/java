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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import org.apache.ignite.Ignite;
import org.junit.jupiter.api.Test;
import us.pserver.micron.IgniteSetup;
import us.pserver.micron.config.IgniteConfig;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.tools.misc.Sleeper;
import us.pserver.micron.security.User;
import us.pserver.micron.security.Security;
import us.pserver.tools.Unchecked;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/01/2019
 */
public class TestIgniteSerialSupport22 {

  public static final String SECURITY_CACHE_USER = "security_user";
  
  public static final String SECURITY_CACHE_GROUP = "security_group";
  
  public static final String SECURITY_CACHE_ROLE = "security_role";
  
  public static final String SECURITY_CACHE_RESOURCE = "security_resource";
  
  @Test
  public void testCustomBeanSerializationOnDistributedCache() {
    try {
      System.out.println("====== IGNITE SERIAL SUPPORT 222 ======");
      IgniteConfig icf = IgniteConfig.builder()
          .setIgniteServerConfig("0.0.0.0", 5555)
          .setStorage(Optional.of(Paths.get("d:/ignite/db")))
          .setJoinTimeout(10000)
          .build();
      Ignite ignite = IgniteSetup.start(icf);
      Security security = Security.create(SecurityConfig.of(ignite));

      System.out.println("====== users ======");
      System.out.println(ignite.getOrCreateCache(SECURITY_CACHE_USER).get("juno"));
      security.getConfig().getUsers().forEach(System.out::println);
      System.out.println("====== groups ======");
      security.getConfig().getGroups().forEach(System.out::println);
      System.out.println("====== roles ======");
      security.getConfig().getRoles().forEach(System.out::println);
      System.out.println("====== resources ======");
      security.getConfig().getResources().forEach(System.out::println);

      String rmanage = "/manage";
      String rlook = "/look";
      User juno = security.authenticateUser("juno", "32132155".toCharArray()).get();
      User john = security.authenticateUser("john", "131313".toCharArray()).get();
      System.out.printf("* authorize( %s, %s ): %s%n", rmanage, juno.getName(), security.authorize(rmanage, juno));
      System.out.printf("* authorize( %s, %s ): %s%n", rmanage, john.getName(), security.authorize(rmanage, john));
      System.out.printf("* authorize( %s, %s ): %s%n", rlook, juno.getName(), security.authorize(rlook, juno));
      System.out.printf("* authorize( %s, %s ): %s%n", rlook, john.getName(), security.authorize(rlook, john));

      Sleeper.of(15000).sleep();
      ignite.close();
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    finally {
      Unchecked.call(() -> {
        Files.walk(Paths.get("d:/ignite/db"))
            .sorted(Comparator.reverseOrder())
            .forEach(p -> Unchecked.call(() -> Files.delete(p)));
      });
    }
  }
  
}
