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

package us.pserver.remote.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.remote.DefaultFactoryProvider;
import us.pserver.remote.NetworkServer;
import us.pserver.remote.container.Authenticator;
import us.pserver.remote.container.Credentials;
import us.pserver.remote.container.ObjectContainer;
import us.pserver.remote.container.SingleCredentialsSource;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/07/2014
 */
public class TestRServer {

  
  static class StreamHandler {
    public boolean save(InputStream is, String path) throws IOException {
      if(is == null || path == null) return false;
      OutputStream os = Files.newOutputStream(Paths.get(path), 
          StandardOpenOption.CREATE, 
          StandardOpenOption.WRITE);
      StreamUtils.transfer(is, os);
      os.flush();
      os.close();
      return true;
    }
    public InputStream read(String path) throws IOException {
      if(path == null) return null;
      Path p = Paths.get(path);
      if(!Files.exists(p)) return null;
      return Files.newInputStream(p, StandardOpenOption.READ);
    }
  }
  
  
  public static void main(String[] args) {
    NetworkServer sv = new NetworkServer(new ObjectContainer());
    sv.setChannelFactory(DefaultFactoryProvider
        .getHttpResponseChannelFactory());
    
    sv.container().put("StreamHandler", new StreamHandler());
    SingleCredentialsSource src = new SingleCredentialsSource(
        new Credentials("juno", new StringBuffer("32132155")));
    sv.container().setAuthenticator(
        new Authenticator(src));
    sv.start();
  }
  
}
