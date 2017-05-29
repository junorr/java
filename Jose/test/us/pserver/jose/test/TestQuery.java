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

package us.pserver.jose.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import us.pserver.jose.driver.ByteReader;
import us.pserver.jose.query.Query;
import us.pserver.jose.query.Query.QueryImpl;
import us.pserver.tools.UTF8String;
import us.pserver.tools.timer.Timer;
import us.pserver.jose.query.QueryResolver;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2017
 */
public class TestQuery {
  
  public static void main(String[] args) throws IOException {
    String json = (
        "{"
          + "'type@': 'us.pserver.Test', "
          + "'str1': 'hello', "
          + "'str2': 'world', "
          + "'array': [1, 2, 3, 4, 5], "
          + "'num': 500, "
          + "'child': {"
            + "'hello': 'world', "
            + "'bool': false"
          + "}, "
          + "'child2': {"
            + "'world': 'hello', "
            + "'sub': [{'n': 500.1},{'n': 500.2}], "
            + "'bool': true"
          + "}"
        + "}").replace("'", "\"");
    System.out.println("* json: "+ json);
    ByteReader rdr = ByteReader.of(ByteBuffer.wrap(UTF8String.from(json).getBytes()));
    
    Query query = new QueryImpl("us.pserver.Test");
    Query sub = new QueryImpl("sub").filter("n").eq(500.2);
    Query sub2 = new QueryImpl("sub").filter("n").gt(500.1);
    query.filter("num").in(200, 300, 400, 500, 600)
        .and("str2").endsWith("ld")
        .and("array").arrayContains(2)
        .and("array").arrayContains(3)
        .descend("child").filter("bool").eq(false);
    query.descend("child2")
        .filter("bool").ne(false)
        .and("world").startsWith("h")
        .and("sub").arrayContains(sub)
        .and("sub").arrayContains(sub2)
        ;
    Timer tm = new Timer.Nanos().start();
    //boolean compare = QueryResolver.resolve(query, rdr);
    boolean compare = QueryResolver.debug().apply(query, rdr);
    tm.stop();
    //boolean compare = QueryResolver.resolve(query, any);
    System.out.println("resolve(query, reader): "+ compare);
    System.out.println(tm);
  }
  
}
