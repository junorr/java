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
import us.pserver.http.HeaderProxyAuth;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst.Method;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.RequestLine;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/07/2014
 */
public class SimpleHttpRequest {

  
  public static void main(String[] args) throws IOException {
    HttpBuilder build = HttpBuilder.requestBuilder(
        new RequestLine(Method.POST, "172.24.77.60", 9099));
    
    build.put(new HeaderProxyAuth("f6036477:00000000"));
    build.put(new HttpEnclosedObject("Some String object"));
    build.writeContent(System.out);
    /*
    System.out.println("-------------------------------");
    Socket sock = new Socket("172.24.75.19", 6060);
    build.writeContent(sock.getOutputStream());
    StreamUtils.transfer(sock.getInputStream(), System.out);*/
  }
  
}
