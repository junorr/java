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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.ignite.test.AccessRule;
import us.pserver.ignite.test.Group;
import us.pserver.ignite.test.Permission;
import us.pserver.ignite.test.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2018
 */
public class TestAccessRule {

  @Test
  public void test1() {
    User u = new User("juno","secret");
    Group g = new Group("group_juno", u);
    AccessRule r = new AccessRule(g, Permission.toPosixBin(Permission.fromPosixString("r-x")));
    System.out.println(r);
    Assertions.assertTrue(u.authenticate("secret"));
    Assertions.assertTrue(r.hasPermission("juno", Permission.EXEC));
    Assertions.assertTrue(r.hasPermission("juno", Permission.READ));
    Assertions.assertFalse(r.hasPermission("john", Permission.READ));
    Assertions.assertTrue(r.hasPermission("juno", Permission.NOPERM));
    Assertions.assertFalse(r.hasPermission("juno", Permission.WRITE));
  }
  
}
