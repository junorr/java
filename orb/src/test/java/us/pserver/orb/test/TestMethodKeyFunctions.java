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

package us.pserver.orb.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.orb.Orb;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/01/2018
 */
public class TestMethodKeyFunctions {

  @Test
  public void getterAsDottedKey() throws NoSuchMethodException {
    String key = "user.name";
    Assertions.assertEquals(key, Orb.GETTER_AS_DOTTED_KEY.apply(ServerConfig.class.getMethod("getUserName")));
  }
  
  @Test
  public void getterAsKey() throws NoSuchMethodException {
    String key = "username";
    Assertions.assertEquals(key, Orb.GETTER_AS_KEY.apply(ServerConfig.class.getMethod("getUserName")));
  }
  
  @Test
  public void getterAsUnderscoredKey() throws NoSuchMethodException {
    String key = "user_name";
    Assertions.assertEquals(key, Orb.GETTER_AS_UNDERSCORED_KEY.apply(ServerConfig.class.getMethod("getUserName")));
  }
  
  @Test
  public void getterAsEnvironmentKey() throws NoSuchMethodException {
    String key = "USER_NAME";
    Assertions.assertEquals(key, Orb.GETTER_AS_ENVIRONMENT_KEY.apply(ServerConfig.class.getMethod("getUserName")));
  }
  
  @Test
  public void getterAsEnvironmentKeyAllUpperCase() throws NoSuchMethodException {
    String key = "OS";
    Assertions.assertEquals(key, Orb.GETTER_AS_ENVIRONMENT_KEY.apply(WindowsEnvConfig.class.getMethod("getOS")));
  }
  
  @Test
  public void nameAsDottedKey() throws NoSuchMethodException {
    String key = "get.user.name";
    Assertions.assertEquals(key, Orb.NAME_AS_DOTTED_KEY.apply(ServerConfig.class.getMethod("getUserName")));
  }
  
  @Test
  public void nameAsKey() throws NoSuchMethodException {
    String key = "getusername";
    Assertions.assertEquals(key, Orb.NAME_AS_KEY.apply(ServerConfig.class.getMethod("getUserName")));
  }
  
  @Test
  public void nameAsUnderscoredKey() throws NoSuchMethodException {
    String key = "get_user_name";
    Assertions.assertEquals(key, Orb.NAME_AS_UNDERSCORED_KEY.apply(ServerConfig.class.getMethod("getUserName")));
  }
  
  @Test
  public void nameAsEnvironmentKey() throws NoSuchMethodException {
    String key = "GET_USER_NAME";
    Assertions.assertEquals(key, Orb.NAME_AS_ENVIRONMENT_KEY.apply(ServerConfig.class.getMethod("getUserName")));
  }
  
}
