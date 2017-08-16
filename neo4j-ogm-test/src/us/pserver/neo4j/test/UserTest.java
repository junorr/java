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

package us.pserver.neo4j.test;

import us.pserver.neo4j.test.bean.Role;
import us.pserver.neo4j.test.bean.User;
import us.pserver.neo4j.test.bean.UserBuilder;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class UserTest {

  
  public static void main(String[] args) throws IllegalAccessException {
    User usr = new UserBuilder()
        .withName("Juno")
        .withPassword("32132155")
        .newAccess("testObjectBox.jar")
        .authorize(Role.JAR)
        .insert()
        .newAccess("testobjectbox.Message")
        .authorize(Role.CLASS)
        .insert()
        .newAccess()
        .deny(Role.METHOD)
        .insert()
        .create();
    System.out.println(usr);
    User request = new UserBuilder()
        .withName("Juno")
        .withPassword("32132155")
        .newAccess("testObjectBox.jar")
        .authorize(Role.JAR)
        .insert()
        .newAccess("testobjectbox.Message")
        .authorize(Role.CLASS)
        .insert()
        .newAccess()
        .authorize(Role.METHOD)
        .insert()
        .create();
    System.out.println(request);
    System.out.println("user.validate( request )");
    usr.validate(request);
  }
  
}
