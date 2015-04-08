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
import java.net.Socket;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.http.HeaderProxyAuth;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst.Method;
import us.pserver.http.HttpCryptKey;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.RequestLine;
import us.pserver.rob.MethodInvocationException;
import us.pserver.rob.RemoteMethod;
import us.pserver.rob.channel.Transport;
import us.pserver.rob.container.Credentials;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/07/2014
 */
public class TestRClient1 {

  public static void main(String[] args) throws MethodInvocationException, IOException {
    HttpBuilder hb = HttpBuilder.requestBuilder(
        //new RequestLine(Method.POST, "pserver.us", 9099));
        new RequestLine(Method.POST, "172.24.77.60", 9099));
    hb.put(new HeaderProxyAuth("f6036477:32132155"));
    
    CryptKey key = CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_PKCS5);
    HttpEnclosedObject hob = new HttpEnclosedObject();
    hob.setCryptEnabled(true, key);
    RemoteMethod rm = new RemoteMethod()
        .forObject("NetworkServer")
        .method("getAvailableThreads")
        .credentials(new Credentials("juno", new StringBuffer("32132155")));
    Transport t = new Transport(rm);
    hob.setObject(t);
    hb.put(new HttpCryptKey(key)).put(hob);
    
    hb.writeContent(System.out);
    
    //Socket s = new Socket("cache.bb.com.br", 80);
    Socket s = new Socket("172.24.75.19", 6060);
    hb.writeContent(s.getOutputStream());
    
    System.out.println();
    System.out.println("-------------------------------");
    System.out.println();
    
    IO.tc(s.getInputStream(), System.out);
    s.close();
  }
  
}
