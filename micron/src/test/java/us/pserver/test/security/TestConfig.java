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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.micron.config.IgniteConfig;
import us.pserver.micron.config.SecurityConfig;
import us.pserver.micron.config.proxy.IgniteConfigHandler;
import us.pserver.micron.config.proxy.GroupConfigHandler;
import us.pserver.micron.config.proxy.ResourceConfigHandler;
import us.pserver.micron.config.proxy.RoleConfigHandler;
import us.pserver.micron.config.proxy.SecurityConfigHandler;
import us.pserver.micron.config.proxy.UserConfigHandler;
import us.pserver.micron.security.Group;
import us.pserver.micron.security.Resource;
import us.pserver.micron.security.Role;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2019
 */
public class TestConfig {
  
  private static final Config cfg = Config.builder()
      .addMapper(LocalDate.class, TestConfig::toLocalDate)
      .addMapper(Instant.class, TestConfig::toInstant)
      .build();
      
  private static LocalDate toLocalDate(Config c) {
    System.out.println("..toLocalDate( " + c + " )");
    try {
      DateTimeFormatter fmt = DateTimeFormatter.ofPattern( "EEE MMM d HH:mm:ss zzz uuuu" , Locale.US);
      ZonedDateTime zdt = ZonedDateTime.parse(c.asString().get(), fmt);
      return LocalDateTime.ofInstant(zdt.toInstant(), ZoneId.of("GMT")).toLocalDate();
    } catch(Exception e) {
      System.out.println("# ERROR toLocalDate: " + e.toString());
      e.printStackTrace();
      throw e;
    }
  }
  
  private static Instant toInstant(Config c) {
    System.out.println("..toInstant( " + c + " )");
    try {
      DateTimeFormatter fmt = DateTimeFormatter.ofPattern( "EEE MMM d HH:mm:ss zzz uuuu" , Locale.US);
      ZonedDateTime zdt = ZonedDateTime.parse(c.asString().get(), fmt);
      return zdt.toInstant();
    } catch(Exception e) {
      System.out.println("# ERROR toInstant: " + e.toString());
      e.printStackTrace();
      throw e;
    }
  }
  
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
      User user = (User) Proxy.newProxyInstance(User.class.getClassLoader(), new Class[]{User.class}, new UserConfigHandler(cfg.get("security.users.0")));
      System.out.println(user);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testGroupConfigHandler() {
    try {
      Group group = (Group) Proxy.newProxyInstance(Group.class.getClassLoader(), new Class[]{Group.class}, new GroupConfigHandler(cfg.get("security.groups.0")));
      System.out.println(group);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testRoleConfigHandler() {
    try {
      Role role = (Role) Proxy.newProxyInstance(Role.class.getClassLoader(), new Class[]{Role.class}, new RoleConfigHandler(cfg.get("security.roles.0")));
      System.out.println(role);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testResourceConfigHandler() {
    try {
      Resource resource = (Resource) Proxy.newProxyInstance(Resource.class.getClassLoader(), new Class[]{Resource.class}, new ResourceConfigHandler(cfg.get("security.resources.0")));
      System.out.println(resource);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testIgniteConfigHandler() {
    try {
      IgniteConfig config = (IgniteConfig) Proxy.newProxyInstance(IgniteConfig.class.getClassLoader(), new Class[]{IgniteConfig.class}, new IgniteConfigHandler(cfg.get("ignite")));
      System.out.println(config);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
  @Test
  public void testSecurityConfigHandler() {
    try {
      System.out.println("SECURITY CONFIG");
      SecurityConfig config = (SecurityConfig) Proxy.newProxyInstance(SecurityConfig.class.getClassLoader(), new Class[]{SecurityConfig.class}, new SecurityConfigHandler(cfg.get("security")));
      System.out.println(config);
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
