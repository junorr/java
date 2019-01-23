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

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import static java.util.Objects.hash;
import java.util.Set;
import static org.apache.ignite.internal.processors.authentication.User.password;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class TestGroup {

  @Test
  public void testGroupBuilder() {
    try {
      String name = "root";
      Instant created = Instant.now();
      Set<String> usernames = new HashSet<>();
      usernames.add("juno");
      usernames.add("foo");
      usernames.add("bar");
      
      Group group = Group.builder()
          .setName(name)
          .setCreated(created)
          .addUsername("juno")
          .addUsername("foo")
          .addUsername("bar")
          .build();
      System.out.println(group);
      
      Assertions.assertEquals(name, group.getName());
      Assertions.assertEquals(usernames, group.getUsernames());
      Assertions.assertEquals(created, group.getCreated());

      User user = User.builder()
          .setName("juno")
          .setEmail("juno.rr@gmail.com")
          .setPassword("32132155".toCharArray())
          .setBirth(LocalDate.of(1980, 7, 7))
          .build();

      Assertions.assertTrue(group.contains(user));
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
  
  @Test
  public void testEditGroup() {
    try {
      Group group = Group.builder()
          .setName("admin")
          .addUsername("john")
          .addUsername("fo")
          .addUsername("ba")
          .build();
      System.out.println(group);
      
      String name = "root";
      Set<String> usernames = new HashSet<>();
      usernames.add("juno");
      usernames.add("foo");
      usernames.add("bar");
      
      group = group.edit()
          .setName(name)
          .clearUsernames()
          .addUsername("juno")
          .addUsername("foo")
          .addUsername("bar")
          .build();
      
      Assertions.assertEquals(name, group.getName());
      Assertions.assertEquals(usernames, group.getUsernames());

      User user = User.builder()
          .setName("juno")
          .setEmail("juno.rr@gmail.com")
          .setPassword("32132155".toCharArray())
          .setBirth(LocalDate.of(1980, 7, 7))
          .build();

      Assertions.assertTrue(group.contains(user));
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
  
}
