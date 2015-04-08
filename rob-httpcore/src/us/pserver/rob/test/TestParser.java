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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import us.pserver.http.GetRequest;
import us.pserver.http.HttpBuilder;
import us.pserver.http.HttpConst;
import us.pserver.http.RequestParser;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/03/2015
 */
public class TestParser {

  
  public static void main(String[] args) throws IOException {
    GetRequest get = new GetRequest("localhost", 25000);
    get.query("obj", "a")
        .query("mth", "compute")
        .query("types", int.class, int.class)
        .query("args", 5, 3);
    HttpBuilder build = HttpBuilder.requestBuilder(get);
    build.remove(HttpConst.HD_CONTENT_TYPE);
    build.writeContent(System.out);
    
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    build.writeContent(bos);
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    
    RequestParser rp = new RequestParser()
        .parseInput(bis);
    System.out.println(rp.getRequestLine());
    get = GetRequest.parse(rp.getRequestLine().toString());
    System.out.println(get);
    System.out.println(get.queryMap());
  }
  
}
