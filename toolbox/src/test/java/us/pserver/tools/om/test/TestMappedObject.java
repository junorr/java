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

package us.pserver.tools.om.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.om.MappedObject;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/01/2018
 */
public class TestMappedObject {

  @Test
  public void mappedServerConfig() throws UnknownHostException {
    ServerConfig cfg = MappedObject.builder(ServerConfig.class, new HashMap<>())
        .withMethodKeyFunction(MappedObject.GETTER_AS_ENVIRONMENT_KEY)
        .build();
    cfg.setServerAddress("127.0.0.1")
        .setServerPort("8080")
        .setUserName("juno")
        .setUserKey("mykey");
    System.out.println(cfg);
    Assertions.assertEquals(InetAddress.getByName("127.0.0.1"), cfg.getServerAddress());
    Assertions.assertEquals(8080, cfg.getServerPort());
    Assertions.assertEquals("juno", cfg.getUserName());
  }
  
  @Test
  public void propertiesServerConfig() throws UnknownHostException, IOException {
    ServerConfig cfg = MappedObject.fromProperties(ServerConfig.class, Paths.get("./test.properties"));
    System.out.println(cfg);
    Assertions.assertEquals(InetAddress.getByName("127.0.0.1"), cfg.getServerAddress());
    Assertions.assertEquals(8080, cfg.getServerPort());
    Assertions.assertEquals("juno", cfg.getUserName());
  }
  
}
