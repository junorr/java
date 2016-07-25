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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.NetConnector;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.RemoteObject;
import us.pserver.rob.container.Credentials;
import us.pserver.rob.factory.DefaultFactoryProvider;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/03/2015
 */
public class TestHttpPostClient {

  public static void main(String[] args) throws MethodInvocationException, UnsupportedEncodingException, IOException {
    NetConnector nc = new NetConnector("localhost", 9035)
        //.setProxyAddress("cache.bb.com.br")
        //.setProxyPort(80)
        .setProxyAuthorization("f6036477:65498788");
    RemoteObject rob = new RemoteObject(nc, DefaultFactoryProvider.factory()
        .enableCryptography()
        .enableGZipCompression()
        .getHttpRequestChannelFactory());
    Credentials cr = new Credentials("juno", "32132155".getBytes());
    
    //----- a.compute() -----
    RemoteMethod mth = new RemoteMethod()
        .forObject("a")
        .method("compute")
        .types(int.class, int.class)
        .params(5, 3)
        .credentials(cr);
    
    double res = (double) rob.invoke(mth);
    System.out.println("* invoking: "+ mth+ " = "+ res);

    //----- a.round() -----
    mth = new RemoteMethod()
        .forObject("a")
        .method("round")
        .types(double.class, int.class)
        .params(res, 3)
        .credentials(cr);
    System.out.println("* invoking: "+ mth+ " = "+ rob.invoke(mth));
    
    //----- b.readFile() -----
    mth = new RemoteMethod()
        .forObject("b")
        .method("readFile")
        .types(String.class)
        .params("/home/juno/freemem")
        .credentials(cr);
    
    InputStream in = (InputStream) rob.invoke(mth);
    System.out.println("* invoking: "+ mth+ " = "+ in);
    System.out.println("IO.tc(): "+ IO.tc(in, Files.newOutputStream(
        Paths.get("/home/juno/rob-freemem"), 
        StandardOpenOption.WRITE, 
        StandardOpenOption.CREATE)));
    
    //----- b.writeFile() -----
    mth = new RemoteMethod()
        .forObject("b")
        .method("writeFile")
        .types(InputStream.class, String.class)
        .addParam(Files.newInputStream(
            Paths.get("/home/juno/freemem"), 
            StandardOpenOption.READ))
        .addParam("/home/juno/rob-freemem2")
        .credentials(cr);
    
    System.out.println("* invoking: "+ mth+ " = "+ rob.invoke(mth));
    
    rob.close();
  }
  
  
  public static double round(double d, int decSize) {
    int dec = (int) d;
    double size = (int) Math.pow(10.0, decSize);
    double frc = (d - dec) * size;
    return dec + (Math.round(frc) / size);
  }
  
}
