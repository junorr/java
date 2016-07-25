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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import us.pserver.cdr.StringByteConverter;
import us.pserver.http.HttpBuilder;
import static us.pserver.http.HttpConst.HD_CONTENT_TYPE;
import us.pserver.http.PlainContent;
import us.pserver.http.ResponseLine;
import us.pserver.streams.IO;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/03/2015
 */
public class TestDumpServer {

  
  public static void main(String[] args) throws IOException, InterruptedException {
    //http://localhost:36000/?obj=a&mth=compute&types=int%3Bint&args=5%3B3
    System.out.println("* Listening on localhost:45000");
    ServerSocket srv = new ServerSocket();
    srv.bind(new InetSocketAddress("localhost", 45000));
    
    while(true) {
      Socket sock = srv.accept();
      Thread.sleep(500);
      System.out.println("* Connection Received: "+ sock);
      StreamUtils.transfer(sock.getInputStream(), System.out);
      System.out.println("---------------------------------------");
      
      StringByteConverter scv = new StringByteConverter();
      InputStream in = TestDumpServer.class.getClass().getResourceAsStream("/us/pserver/rob/channel/rob-post.html");
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      IO.tc(in, bos);
    
      HttpBuilder builder = HttpBuilder.responseBuilder(new ResponseLine(200, "OK"));
      builder.remove(HD_CONTENT_TYPE);
      builder.put(HD_CONTENT_TYPE, "text/html");
      PlainContent pc = new PlainContent(scv.reverse(bos.toByteArray()));
      builder.put(new PlainContent(scv.reverse(bos.toByteArray())));
      //builder.writeContent(System.out);
      builder.writeContent(sock.getOutputStream());
      sock.getOutputStream().flush();
      sock.shutdownOutput();
    }
    //srv.close();
  }
  
}
