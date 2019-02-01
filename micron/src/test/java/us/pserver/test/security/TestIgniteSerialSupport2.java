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
import org.junit.jupiter.api.Test;
import us.pserver.micron.IgniteSetup;
import us.pserver.tools.Unchecked;
import us.pserver.tools.misc.Sleeper;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/01/2019
 */
public class TestIgniteSerialSupport2 {

  @Test
  public void testCustomBeanSerializationOnDistributedCache() {
    try {
      System.out.println("====== IGNITE SERIAL SUPPORT 222 ======");
      IgniteSetup setup = IgniteSetup.create();
      Sleeper.of(5000).sleep();
      System.out.println("=== users ===");
      setup.security().getUserCache().forEach(System.out::println);
      System.out.println("=== groups ===");
      setup.security().getGroupCache().forEach(System.out::println);
      System.out.println("=== roles ===");
      setup.security().getRoleCache().forEach(System.out::println);
      System.out.println("=== resources ===");
      setup.security().getResourceCache().forEach(System.out::println);
      String rmanage = "manage";
      String rlook = "look";
      User juno = setup.security().authenticateUser("juno", "32132155".toCharArray()).get();
      User john = setup.security().authenticateUser("john", "131313".toCharArray()).get();
      System.out.printf("* authorize( %s, %s ): %s%n", rmanage, juno.getName(), setup.security().authorize(rmanage, juno));
      System.out.printf("* authorize( %s, %s ): %s%n", rmanage, john.getName(), setup.security().authorize(rmanage, john));
      System.out.printf("* authorize( %s, %s ): %s%n", rlook, juno.getName(), setup.security().authorize(rlook, juno));
      System.out.printf("* authorize( %s, %s ): %s%n", rlook, john.getName(), setup.security().authorize(rlook, john));
      Sleeper.of(5000).sleep();
      setup.ignite().close();
      Unchecked.call(() -> {
        Files.walk(Paths.get("d:/ignite/db"))
            .sorted(Comparator.reverseOrder())
            .forEach(p -> Unchecked.call(() -> Files.delete(p)));
      });
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
  
}
