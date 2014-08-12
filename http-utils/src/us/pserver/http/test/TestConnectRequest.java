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
import java.io.OutputStream;
import java.net.Socket;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;
import us.pserver.chk.Invoke;
import us.pserver.http.HeaderProxyAuth;
import us.pserver.http.HeaderXCKey;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpConst.Method;
import us.pserver.http.HttpParser;
import us.pserver.http.RequestLine;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2014
 */
public class TestConnectRequest {

  
  public static void main(String[] args) throws IOException {
    RequestLine rl = new RequestLine(Method.CONNECT, "172.24.75.2", 8000);
    Socket sock = new Socket("172.24.75.19", 6060);
    //Socket sock = new Socket("172.24.75.2", 8000);
    //Socket sock = new Socket("10.100.0.105", 6060);
    
    HeaderProxyAuth hp = new HeaderProxyAuth("f6036477:12345678");
    OutputStream os = sock.getOutputStream();
    
    HttpBuilder hb = new HttpBuilder();
    hb.put(rl).put(hp);
    hb.writeContent(os);
    
    
    HttpParser hps = new HttpParser();
    hps.parseInput(sock.getInputStream());
    hps.headers().forEach(System.out::print);
    System.out.println("--------------------");
    
    HeaderXCKey hx = (HeaderXCKey) hps.getHeader(HttpConst.HD_X_CRYPT_KEY);
    CryptKey key = hx.getCryptKey();
    OutputStream secout = CryptUtils.createCipherOutputStream(sock.getOutputStream(), key);
    int count = 0;
    
    System.out.println("* writing to secure channel...");
    while(true) {
      String s = "Secured Channel ["+ count++ +"]\n";
      System.out.print("* write: "+ s);
      StreamUtils.write(s, secout);
      Invoke.unchecked(()->Thread.sleep(3000));
    }
  }
  
}
