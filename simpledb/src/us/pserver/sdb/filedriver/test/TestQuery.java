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

package us.pserver.sdb.filedriver.test;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import java.io.IOException;
import us.pserver.job.query.Operations;
import us.pserver.job.query.Query;
import us.pserver.job.query.Query.DefQuery;
import us.pserver.job.query.QueryJson;

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
            + "'bool': true"
          + "}"
        + "}").replace("'", "\"");
    System.out.println("* json: "+ json);
    Any any = JsonIterator.deserialize(json);
    
    Query query = new DefQuery("us.pserver.Test");
    query.filter("num", Operations.in(200, 300, 400, 500, 600));
    query.and("str2", Operations.endsWith("ld"));
    query.and("array", Operations.arrayContains(2));
    query.and("array", Operations.arrayContains(3));
    
    query.descend("child").filter("bool", Operations.eq(false));
    query.descend("child2").filter("bool", Operations.ne(false));
    query.descend("child2").filter("world", Operations.startsWith("h"));
    
    //boolean compare = resolve(query, any);
    boolean compare = QueryJson.debug().resolve(query, any);
    //boolean compare = QueryJson.resolved(query, any);
    System.out.println("resolve(query, any): "+ compare);
  }
  
}
