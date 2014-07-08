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
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpEncodedObject;
import us.pserver.http.RequestLine;
import us.pserver.http.ResponseParser;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/06/2014
 */
public class TestHttpRequest implements HttpConst {

  
  
  public static void main(String[] args) throws IOException {
    Object obj = "A plain text content object.";
    //Path p = Paths.get("d:/file.exe");
    RequestLine req = new RequestLine(Method.POST, "172.24.75.2", 8000);
    //RequestLine req = new RequestLine(Method.POST, "10.100.0.104", 8000);
    HttpBuilder build = HttpBuilder.requestBuilder(req)
        .put(new HttpEncodedObject(obj));
        //.add(new HttpInputStream(Files.newInputStream(p, StandardOpenOption.READ)));
    
    //Socket sock = new Socket("172.24.75.19", 6060);
    Socket sock = new Socket("172.24.75.2", 8000);
    //Socket sock = new Socket("10.100.0.105", 6060);
    build.writeContent(sock.getOutputStream());
    build.writeContent(System.out);
    
    ResponseParser rp = new ResponseParser();
    rp.readFrom(sock.getInputStream());
    rp.headers().forEach(System.out::print);
    
    System.out.println("-------------------------");
    StreamUtils.transfer(sock.getInputStream(), System.out);
    
    sock.close();
  }
  
  
}
