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
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import us.pserver.job.query.Operations;
import us.pserver.job.query.Query;
import us.pserver.job.query.Query.DefQuery;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2017
 */
public class TestQuery {
  
  
  public static boolean isDouble(Any any) {
    return any.valueType() == ValueType.NUMBER
        && any.toString().contains(".");
  }
  
  
  public static boolean resolve(Query query, Any any) {
    System.out.println("__resolve( "+ any+ " )__");
    Set<String> keys = any.keys();
    Boolean comp = null;
    for(String k : keys) {
      Any a = any.get(k);
      ValueType valt = a.valueType();
      boolean and = true;
      Query qf = query.andFields().stream().filter(q->q.name().equals(k)).findAny().orElse(null);
      if(qf == null) {
        and = false;
        qf = query.orFields().stream().filter(q->q.name().equals(k)).findAny().orElse(null);
      }
      if(qf == null) {
        and = true;
        qf = query.childFields().stream().filter(q->q.name().equals(k)).findAny().orElse(null);
      }
      if(qf == null) continue;
      switch(valt) {
        case ARRAY:
          List<Any> als = a.asList();
          List lst = new ArrayList(als.size());
          als.forEach(aa->lst.add(aa.object()));
          if(and) {
            comp = (comp == null ? true : comp) && qf.operation().apply(a.object());
          } else {
            comp = (comp == null ? false : comp) || qf.operation().apply(a.object());
          }
          System.out.println(lst+ " - "+ lst.get(0).getClass());
          System.out.println("\""+ k+ "\""+ ".operation().apply( "+ a+ " ): "+ comp);
          break;
        case BOOLEAN:
          if(and) {
            comp = (comp == null ? true : comp) && qf.operation().apply(a.toBoolean());
          } else {
            comp = (comp == null ? false : comp) || qf.operation().apply(a.toBoolean());
          }
          System.out.println("\""+ k+ "\""+ ".operation().apply( "+ a+ " ): "+ comp);
          break;
        case NUMBER:
          if(and) {
            comp = (comp == null ? true : comp) && qf.operation().apply((isDouble(a) ? a.toDouble() : a.toLong()));
          } else {
            comp = (comp == null ? false : comp) && qf.operation().apply((isDouble(a) ? a.toDouble() : a.toLong()));
          }
          System.out.println("\""+ k+ "\""+ ".operation().apply( "+ a+ " ): "+ comp);
          break;
        case STRING:
          if(and) {
            comp = (comp == null ? true : comp) && qf.operation().apply(a.toString());
          } else {
            comp = (comp == null ? false : comp) || qf.operation().apply(a.toString());
          }
          System.out.println("\""+ k+ "\""+ ".operation().apply( "+ a+ " ): "+ comp);
          break;
        case OBJECT:
          boolean b = resolve(qf, a);
          if(and) {
            comp = (comp == null ? true : comp) && b;
          } else {
            comp = (comp == null ? false : comp) || b;
          }
          System.out.println("\""+ k+ "\""+ ".operation().apply( "+ a+ " ): "+ comp);
          break;
        default:
          System.out.println("default for: "+ a);
          comp = false;
          break;
      }
    }
    return comp;
  }

  
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
    System.out.println("* any : "+ any);
    Set<String> keys = any.keys();
    for(String k : keys) {
      Any a = any.get(k);
      System.out.println(" - "+ k+ ": "+ a+ " - "+ a.valueType());
    }
    System.out.println();
    
    Query query = new DefQuery("us.pserver.Test");
    query.field("num", Operations.between(200, 600));
    query.and("str2", Operations.endsWith("ld"));
    query.and("array", Operations.arrayContains(2));
    query.descend("child").field("bool", Operations.eq(false));
    query.descend("child2").field("bool", Operations.eq(false));
    query.descend("child2").field("world", Operations.startsWith("h"));
    
    boolean compare = resolve(query, any);
    System.out.println();
    System.out.println("resolve(query, any): "+ compare);
  }
  
}
