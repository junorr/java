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
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.remote.DefaultFactoryProvider;
import us.pserver.remote.MethodInvocationException;
import us.pserver.remote.NetConnector;
import us.pserver.remote.RemoteMethod;
import us.pserver.remote.RemoteObject;
import us.pserver.remote.container.Credentials;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/07/2014
 */
public class TestRClient {

  public static void main(String[] args) throws MethodInvocationException, IOException {
    RemoteObject rem = new RemoteObject(
        new NetConnector("172.24.77.6", 
            NetConnector.DEFAULT_PORT), 
        DefaultFactoryProvider
            .getHttpRequestChannelFactory());
    
    InputStream is = null;
    Files.newInputStream(
        Paths.get("c:/.local/splash.png"), 
        StandardOpenOption.READ);
    
    RemoteMethod mth = new RemoteMethod()
        .setCredentials(new Credentials("juno", 
            new StringBuffer("32132155")))
        .forObject("StreamHandler")
        .method("save")
        .setArgTypes(InputStream.class, String.class);
        //.setReturnType(boolean.class);
    
    is = Files.newInputStream(
      Paths.get("c:/.local/splash.png"), 
      StandardOpenOption.READ);
    mth.arguments(is, "c:/.local/remote.png");
    System.out.println("* invoking remote...");
    System.out.println("* "+ mth+ " = "+ rem.invoke(mth));

    mth.method("read")
        .arguments("c:/.local/remote.png")
        .setArgTypes(String.class);
        //.setReturnType(InputStream.class);
    System.out.println("* invoking remote...");
    System.out.println("* "+ mth);
    
    is = (InputStream) rem.invoke(mth);
    OutputStream os = Files.newOutputStream(
        Paths.get("c:/.local/client.png"), 
        StandardOpenOption.CREATE, 
        StandardOpenOption.WRITE);
    StreamUtils.transfer(is, os);
    os.flush();
    os.close();
    is.close();
    rem.getChannel().close();
    System.out.println("* success!");
  }
  
}
