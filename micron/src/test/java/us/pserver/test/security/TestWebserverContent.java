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

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.StaticContentSupport;
import io.helidon.webserver.WebServer;
import java.net.InetAddress;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import us.pserver.micron.config.MicronConfig;
import us.pserver.micron.handler.JsonHandler;
import us.pserver.micron.handler.SecurityHandler;
import us.pserver.micron.security.Security;
import us.pserver.tools.Unchecked;
import us.pserver.tools.misc.Sleeper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/02/2019
 */
public class TestWebserverContent {

  @Test
  public void stringContent() {
    try {
      MicronConfig micron = MicronConfig.builder().buildFromAppYaml();
      Routing rt = Routing.builder()
          .register("/videos", StaticContentSupport.builder(Paths.get("D:/videos/porn")))
          .any("/auth", new JsonHandler()::handle, 
              new SecurityHandler(Security.create(micron.getSecurityConfig()))::authenticate)
          .build();
      ServerConfiguration cfg = ServerConfiguration.builder()
          .bindAddress(Unchecked.call(() -> InetAddress.getByName(micron.getServerConfig().getAddress())))
          .port(micron.getServerConfig().getPort())
          .build();
      WebServer server = Unchecked.call(() -> WebServer.builder(rt).config(cfg).build().start().toCompletableFuture().get());
      System.out.printf("* Server started on %s:%d%n", micron.getServerConfig().getAddress(), micron.getServerConfig().getPort());
      //Sleeper.of(30000).sleep();
      //System.out.println("* Terminating server...");
      Unchecked.call(() ->server.whenShutdown().toCompletableFuture().get());
      //Unchecked.call(() ->server.shutdown().toCompletableFuture().get());
    }
    catch(Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  
}
