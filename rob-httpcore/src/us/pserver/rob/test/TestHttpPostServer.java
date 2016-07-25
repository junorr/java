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

package us.pserver.rob.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.chk.Checker;
import us.pserver.rob.HttpConnector;
import us.pserver.rob.container.Authenticator;
import us.pserver.rob.container.Credentials;
import us.pserver.rob.container.ObjectContainer;
import us.pserver.rob.container.SingleCredentialsSource;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.rob.server.NetworkServer;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/03/2015
 */
public class TestHttpPostServer {

  static class A {
    double compute(int a, int b) {
      return a / (double)b;
    }
    
    String info() { return "Hello from "+ getClass(); }
    
    double round(double d, int decSize) {
      int dec = (int) d;
      double size = (int) Math.pow(10.0, decSize);
      double frc = (d - dec) * size;
      return dec + (Math.round(frc) / size);
    }
  }
    
  static class B {
    public InputStream readFile(String fname) throws IOException {
      Checker.nullstr(fname);
      Path p = Paths.get(fname);
      if(!Files.exists(p))
        throw new FileNotFoundException(fname);
      return Files.newInputStream(p, 
          StandardOpenOption.READ);
    }
    
    String info() { return "Hello from "+ getClass(); }
    
    public long writeFile(InputStream in, String fname) throws IOException {
      Checker.nullarg(InputStream.class, in);
      Checker.nullstr(fname);
      Path p = Paths.get(fname);
      OutputStream out = Files.newOutputStream(p, 
          StandardOpenOption.CREATE, 
          StandardOpenOption.APPEND, 
          StandardOpenOption.WRITE);
      return IO.tc(in, out);
    }
  }
    
  public static void main(String[] args) {
    HttpConnector nc = new HttpConnector("0.0.0.0", 9035);
    Credentials cr = new Credentials("juno", "32132155".getBytes());
    ObjectContainer oc = new ObjectContainer(
        new Authenticator(new SingleCredentialsSource(cr)));
    oc.put("a", new A()).put("b", new B());
    NetworkServer srv = new NetworkServer(oc, nc, 
        DefaultFactoryProvider.factory()
            .enableCryptography()
            .enableGZipCompression()
            .getHttpResponseChannelFactory());
    srv.start();
  }
  
}
