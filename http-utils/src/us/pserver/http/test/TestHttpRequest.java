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

package us.pserver.http.test;

import java.io.IOException;
import java.net.Socket;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.chk.Invoke;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.HttpInputStream;
import us.pserver.http.RequestLine;
import us.pserver.http.ResponseParser;
import us.pserver.streams.IO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/06/2014
 */
public class TestHttpRequest implements HttpConst {

  
  public static void main(String[] args) throws IOException {
    RequestLine req = new RequestLine(Method.POST, "172.24.77.60", 9099);
    HttpBuilder build = HttpBuilder.requestBuilder(req);
    
    Object obj = "A plain text content object.";
    CryptKey key = CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_PKCS5);
    build.put(new HttpEnclosedObject(obj)
        .setCryptKey(key));
    
    build.put(new HttpInputStream(IO.is(IO.p("c:/.local/file.txt"))));
    
    //Socket sock = new Socket("172.24.75.19", 6060);
    Socket sock = new Socket("172.24.77.60", 9099);
    //Socket sock = new Socket("10.100.0.105", 6060);
    build.writeContent(sock.getOutputStream());
    //build.writeContent(System.out);
    
    ResponseParser rp = new ResponseParser();
    rp.parseInput(sock.getInputStream());
    rp.headers().forEach(System.out::print);
    
    System.out.println("-------------------------");
    //StreamUtils.transfer(sock.getInputStream(), System.out);
    
    int count = 0;
    
    while(true) {
      String so = "The Object ("+ count++ + ")";
      HttpBuilder hb = HttpBuilder.requestBuilder(req);
      System.out.println("* writing ( "+ so+ " )...");
      hb.put(new HttpEnclosedObject(so).setCryptKey(key));
      hb.writeContent(sock.getOutputStream());
      System.out.println();
      //StreamUtils.transfer(sock.getInputStream(), System.out);
      Invoke.unchecked(()->Thread.sleep(2500));
    }
  }
  
}
