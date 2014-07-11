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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.RequestParser;
import us.pserver.http.ResponseLine;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/06/2014
 */
public class TestSimpleServer implements HttpConst {

  
  public static void main(String[] args) throws IOException {
    InetSocketAddress addr = new InetSocketAddress("0.0.0.0", 8000);
    ServerSocket server = new ServerSocket();
    server.bind(addr);
    System.out.println("* Server listening on: "+ addr.toString());
    Socket sock = server.accept();
    System.out.println("* Connected: "+ sock.getRemoteSocketAddress());
    
    RequestParser rp = new RequestParser();
    rp.readFrom(sock.getInputStream());
    System.out.println("-----------------------");
    System.out.println("* headers: "+ rp.headers().size());
    rp.headers().forEach(System.out::print);
    System.out.println("-----------------------");
    
    String hob = "HttpEnclosedObject";
    if(rp.containsHeader(hob)) {
      System.out.println("  - obj: "+ ((HttpEnclosedObject)rp.getHeader(hob)).getObject());
    }
    
    ResponseLine rl = new ResponseLine(200, "OK");
    HttpBuilder hb = HttpBuilder.responseBuilder(rl);
    hb.put(rp.getHeader(hob));
    hb.writeContent(sock.getOutputStream());
  }
  
}
