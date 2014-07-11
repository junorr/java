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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import us.pserver.http.HttpBuilder;
import static us.pserver.http.HttpConst.CRLF;
import us.pserver.http.HttpConst.Method;
import us.pserver.http.HttpEnclosedObject;
import us.pserver.http.RequestLine;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/07/2014
 */
public class RequestMeter {

  
  public static void main(String[] args) throws IOException {
    RequestLine rl = new RequestLine(Method.POST, "172.24.75.2", 9011);
    HttpBuilder hb = HttpBuilder.requestBuilder(rl);
    HttpEnclosedObject hob = new HttpEnclosedObject();
    hob.setObject("The Object");
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    System.out.println("* hob:");
    hob.writeContent(bos);
    System.out.println(bos.toString());
    System.out.println();
    System.out.println("* hob.length="+ bos.size());
    System.out.println();
    hb.put(hob);
    bos.reset();
    hb.writeContent(bos);
    System.out.println(bos.toString());
    System.out.println("* length="+ bos.size());
    System.out.println("* transferBetween(147+CRLF, EOF) = "
        + StreamUtils.transferBetween(
            new ByteArrayInputStream(bos.toByteArray()), 
            System.out, "145"+CRLF, "EOF"));
  }
  
}
