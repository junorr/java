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

import io.helidon.config.Config;
import java.lang.reflect.Proxy;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.micron.config.UserConfigHandler;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2019
 */
public class TestConfig {
  
  private static final Config cfg = Config.create();
  
  @Test
  public void testHelidonConfig() {
    System.out.printf("* cfg.get('server.address'): %s%n", cfg.get("server.address").asString().get());
    System.out.printf("* cfg.get('server.port'): %d%n", cfg.get("server.port").asInt().get());
    Assertions.assertEquals("0.0.0.0", cfg.get("server.address").asString().get());
    Assertions.assertEquals(Integer.valueOf(6666), cfg.get("server.port").asInt().get());
  }
  
  public static void printConfig(Config c, String prefix) {
    System.out.printf("* %s: %s%n", prefix, c);
    System.out.printf("* %s.name: %s%n", prefix, c.name());
    System.out.printf("* %s.key: %s%n", prefix, c.key());
    System.out.printf("* %s.isLeaf: %s%n", prefix, c.isLeaf());
    System.out.printf("* %s.hasValue: %s%n", prefix, c.hasValue());
  }
  
  @Test
  public void testUserConfigHandler() {
    try {
      Config s = cfg.get("security");
      printConfig(s, "security");
      Config c = s.get("users");
      printConfig(c, "security.users");
      System.out.printf("* security.users.0.birth: %s%n", c.get("0.birth").as(ZonedDateTime.class).get());
      c.traverse().forEach(o -> printConfig(o, "security.users[i]"));
      User user = (User) Proxy.newProxyInstance(User.class.getClassLoader(), new Class[]{User.class}, new UserConfigHandler(cfg.get("security.users.0")));
      System.out.println(user);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
