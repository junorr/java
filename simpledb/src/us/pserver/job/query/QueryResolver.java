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

package us.pserver.job.query;

import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import java.util.List;
import java.util.Set;
import us.pserver.job.query.Query;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2017
 */
public interface QueryResolver {

  public boolean resolve(Query query, Any any);
  
  
  public static QueryResolver instance() {
    return new DefQueryJson();
  }
  
  
  public static QueryResolver debug() {
    return new DefQueryJson(true);
  }
  
  
  public static boolean resolved(Query query, Any any) {
    return new DefQueryJson().resolve(query, any);
  }
  
  
  
  
  
  static final class DefQueryJson implements QueryResolver {
    
    private final boolean debug;
    
    public DefQueryJson() {
      this(false);
    }
    
    public DefQueryJson(boolean debug) {
      this.debug = debug;
    }
    
    private boolean isDouble(Any any) {
      return any.valueType() == ValueType.NUMBER
          && any.toString().contains(".");
    }

    private boolean resolveArray(Query query, Any any) {
      boolean result = any.asList().stream().anyMatch(e->resolve(query, e));
      if(debug) {
        System.err.println("resolveArray( "+ any+ " ): "+ result);
      }
      return result;
    }

    private boolean resolveBoolean(Query query, Any any) {
      boolean result = query.operation().apply(any.toBoolean());
      if(debug) {
        System.err.println(query.operation()+ ".( "+ any+ " ): "+ result);
      }
      return result;
    }

    private boolean resolveNumber(Query query, Any any) {
      boolean result = query.operation().apply((isDouble(any) 
          ? any.toDouble() : any.toLong()));
      if(debug) {
        System.err.println(query.operation()+ ".( "+ any+ " ): "+ result);
      }
      return result;
    }

    private boolean resolveString(Query query, Any any) {
      boolean result = query.operation().apply(any.toString());
      if(debug) {
        System.err.println(query.operation()+ ".( "+ any+ " ): "+ result);
      }
      return result;
    }

    private boolean resolveList(List<Query> qs, Any any, boolean and) {
      Set<String> keys = any.keys();
      boolean bool = and;
      for(Query q : qs) {
        if(!keys.stream().anyMatch(k->k.equals(q.name())))
          continue;
        boolean resolve = resolveOne(q, any.get(q.name()));
        bool = and ? bool && resolve : bool || resolve;
      }//for
      return bool;
    }

    private boolean resolveOne(Query query, Any any) {
      boolean bool = false;
      switch(any.valueType()) {
        case ARRAY:
          bool = resolveArray(query, any);
          break;
        case BOOLEAN:
          bool = resolveBoolean(query, any);
          break;
        case NUMBER:
          bool = resolveNumber(query, any);
          break;
        case STRING:
          bool = resolveString(query, any);
          break;
        case OBJECT:
          bool = resolve(query, any);
          break;
      }
      return bool;
    }

    @Override
    public boolean resolve(Query query, Any any) {
      boolean result;
      if(any.keys().isEmpty()) {
        result = query.operation().apply(any.object());
        if(debug) {
          System.err.println(query.operation()+ ".( "+ any+ " ): "+ result);
        }
      } 
      else {
        result = resolveList(query.ands(), any, true)
            && resolveList(query.childs(), any, true)
            || resolveList(query.ors(), any, false);
      }
      return result;
    }
  
  }
  
}
