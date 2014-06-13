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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.HttpHexObject;
import us.pserver.http.HttpInputStream;
import us.pserver.http.RequestLine;
import us.pserver.http.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/06/2014
 */
public class TestHttpRequest implements HttpConst {

  
  
  public static void main(String[] args) throws IOException {
    HttpBuilder build = new HttpBuilder();
    Object obj = "A plain text content object.";
    Path p = Paths.get("d:/file.exe");
    RequestLine req = new RequestLine(Method.POST, "172.24.75.2", 8000);
    build.add(req)
        .add(req.getHost())
        .add(HD_USER_AGENT, VALUE_USER_AGENT)
        .add(HD_ACCEPT, VALUE_ACCEPT)
        .add(HD_ACCEPT_ENCODING, VALUE_ENCODING)
        .add(HD_CONTENT_TYPE, VALUE_CONTENT_MULTIPART + HD_BOUNDARY + BOUNDARY)
        .add(new HttpHexObject(obj))
        .add(new HttpInputStream(Files.newInputStream(p, StandardOpenOption.READ)));
    
    Socket sock = new Socket("172.24.75.19", 6060);
    //Socket sock = new Socket("172.24.75.2", 8000);
    build.writeTo(sock.getOutputStream());
    
    StreamUtils.transfer(sock.getInputStream(), System.out);
    sock.close();
  }
  
  
}
