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

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import us.pserver.micron.IgniteSetup;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.micron.security.User;
import us.pserver.tools.misc.Sleeper;
import us.pserver.micron.security.User;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/01/2019
 */
public class TestIgniteSerialSupport {

  @Test
  public void testCustomBeanSerializationOnDistributedCache() {
    System.out.println("====== IGNITE SERIAL SUPPORT 111 ======");
    IgniteSetup setup = IgniteSetup.create();
    System.out.printf("- getStoragePath: %s%n", setup.ignite().configuration().getDataStorageConfiguration().getStoragePath());
    System.out.printf("- getWalArchivePath: %s%n", setup.ignite().configuration().getDataStorageConfiguration().getWalArchivePath());
    System.out.printf("- getWalPath: %s%n", setup.ignite().configuration().getDataStorageConfiguration().getWalPath());
    User juno = User.builder()
        .setName("juno")
        .setFullName("Juno Roesler")
        .setBirth(LocalDate.of(1980, 7, 7))
        .setEmail("juno.rr@gmail.com")
        .setPassword("32132155".toCharArray())
        .build();
    User john = User.builder()
        .setName("john")
        .setFullName("John Doe")
        .setBirth(LocalDate.of(1982, 4, 13))
        .setEmail("john.doe.13@gmail.com")
        .setPassword("131313".toCharArray())
        .build();
    Group root = Group.builder()
        .setName("root")
        .addUser(juno)
        .build();
    Group other = Group.builder()
        .setName("other")
        .addUser(juno)
        .addUser(john)
        .build();
    Group johns = Group.builder()
        .setName("johns")
        .addUser(john)
        .build();
    Role manage = Role.builder()
        .setName("manage")
        .setAllowed(true)
        .addGroup(root)
        .build();
    Role noJohns = Role.builder()
        .setName("noJohns")
        .setAllowed(false)
        .addGroup(johns)
        .build();
    Role look = Role.builder()
        .setName("look")
        .setAllowed(true)
        .addGroup(other)
        .build();
    Resource rmanage = Resource.builder()
        .setName("manage")
        .addRole(manage)
        .addRole(look)
        .addRole(noJohns)
        .build();
    Resource rlook = Resource.builder()
        .setName("look")
        .addRole(look)
        .build();
    setup.security().getUserCache().put(juno.getName(), juno);
    setup.security().getUserCache().put(john.getName(), john);
    setup.security().getGroupCache().put(root.getName(), root);
    setup.security().getGroupCache().put(other.getName(), other);
    setup.security().getGroupCache().put(johns.getName(), johns);
    setup.security().getRoleCache().put(noJohns.getName(), noJohns);
    setup.security().getRoleCache().put(manage.getName(), manage);
    setup.security().getRoleCache().put(look.getName(), look);
    setup.security().getResourceCache().put(rmanage.getName(), rmanage);
    setup.security().getResourceCache().put(rlook.getName(), rlook);
    System.out.println("====== users ======");
    System.out.println(juno);
    System.out.println(john);
    System.out.println("====== groups ======");
    System.out.println(root);
    System.out.println(other);
    System.out.println(johns);
    System.out.println("====== roles ======");
    System.out.println(manage);
    System.out.println(look);
    System.out.println(noJohns);
    System.out.println("====== resources ======");
    System.out.println(rmanage);
    System.out.println(rlook);
    System.out.printf("* authorize( %s, %s ): %s%n", rmanage.getName(), juno.getName(), setup.security().authorize(rmanage, juno));
    System.out.printf("* authorize( %s, %s ): %s%n", rmanage.getName(), john.getName(), setup.security().authorize(rmanage, john));
    System.out.printf("* authorize( %s, %s ): %s%n", rlook.getName(), juno.getName(), setup.security().authorize(rlook, juno));
    System.out.printf("* authorize( %s, %s ): %s%n", rlook.getName(), john.getName(), setup.security().authorize(rlook, john));
    Sleeper.of(5000).sleep();
    setup.ignite().close();
  }
  
}
