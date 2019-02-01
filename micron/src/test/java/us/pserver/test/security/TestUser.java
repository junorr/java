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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.Hash;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public class TestUser {

  @Test
  public void testUserBuilder() {
    try {
      String name = "juno";
      String email = "juno.rr@gmail.com";
      String fullName = "Juno Roesler";
      LocalDate birth = LocalDate.of(1980, 7, 7);
      char[] password = "32132155".toCharArray();
      String hash = Hash.sha256().of(new String(password));
      Instant created = Instant.now();
      
      User user = User.builder()
          .setName(name)
          .setFullName(fullName)
          .setEmail(email)
          .setPassword(password)
          .setCreated(created)
          .setBirth(birth)
          .build();
      System.out.println(user);
      
      Assertions.assertEquals(name, user.getName());
      Assertions.assertEquals(email, user.getEmail());
      Assertions.assertEquals(birth, user.getBirth());
      Assertions.assertEquals(created, user.getCreated());
      Assertions.assertEquals(hash, user.getHash());
      Assertions.assertTrue(user.authenticate(name, hash));
      Assertions.assertTrue(user.authenticate(name, password));
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
  
  @Test
  public void testEditUser() {
    try {
      User user = User.builder()
          .setName("juno")
          .setEmail("juno@pserver.us")
          .setFullName("Juno Dani")
          .setPassword("32132155".toCharArray())
          .setBirth(LocalDate.of(2000, 7, 7))
          .build();
      
      String fullName = "Juno Roesler";
      String email = "juno.rr@gmail.com";
      LocalDate birth = LocalDate.of(1980, 7, 7);
      
      user = user.edit()
          .setFullName(fullName)
          .setEmail(email)
          .setBirth(birth)
          .build();
      
      Assertions.assertEquals(email, user.getEmail());
      Assertions.assertEquals(birth, user.getBirth());
      Assertions.assertEquals(fullName, user.getFullName());
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
  
}
