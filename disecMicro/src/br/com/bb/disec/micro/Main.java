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

package br.com.bb.disec.micro;

import br.com.bb.disec.micro.conf.ServerConfig;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.jboss.logging.Logger;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/07/2016
 */
public class Main {
  
  public static final String SERVER_CONF_FILE = "/resources/serverconf.json";
  
  
  public static void main(String[] args) throws IOException, URISyntaxException {
    ServerConfig conf = ServerConfig.builder().load(
        Paths.get(Main.class.getResource(SERVER_CONF_FILE).toURI())
    ).build();
    Logger.getLogger(Main.class).info(conf);
    new Server(conf).start();
  }
  
}
