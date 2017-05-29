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

package us.pserver.jose.query;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import us.pserver.jose.driver.ByteReader;
import us.pserver.jose.driver.StringByteReader;
import us.pserver.jose.json.JsonType;
import us.pserver.jose.query.op.ObjectOperation;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/02/2017
 */
public interface QueryResolver extends BiFunction<Query, ByteReader, Boolean> {

  @Override
  public Boolean apply(Query query, ByteReader reader);
  
  
  public static QueryResolver get() {
    return new QueryResolverImpl();
  }
  
  
  public static QueryResolver debug() {
    return new QueryResolverImpl(true);
  }
  
  
  public static boolean resolve(Query query, ByteReader reader) {
    return get().apply(query, reader);
  }
  
  
  
  
  
  static final class QueryResolverImpl implements QueryResolver {
    
    private final boolean debug;
    
    public QueryResolverImpl() {
      this(false);
    }
    
    public QueryResolverImpl(boolean debug) {
      this.debug = debug;
    }
    
    
    private boolean resolveArray(Query query, StringByteReader sbr) {
      JsonType type;
      boolean res = false;
      while(!res && (type = JsonType.of(sbr.iterator().getCurrentByte())) != JsonType.END_ARRAY) {
        if(ObjectOperation.class.isAssignableFrom(query.operation().getClass())) {
          ObjectOperation op = (ObjectOperation) query.operation();
          res = res || op.apply(sbr);
          sbr.iterator().skip().nextValueType();
        } else {
          res = res || resolveType(query, sbr);
        }
      }
      return res;
    }
    
    
    private boolean resolveType(Query query, StringByteReader sbr) {
      JsonType type = sbr.iterator().nextValueType();
      Object val = null;
      boolean bool = false;
      switch(type) {
        case STRING:
          val = sbr.iterator().readString();
          bool = query.operation().apply(val);
          debug(query, val, bool);
          return bool;
        case BOOLEAN:
          val = sbr.iterator().readBoolean();
          //System.out.println("* resolveBoolean");
          bool = query.operation().apply(val);
          debug(query, val, bool);
          return bool;
        case NUMBER:
          val = sbr.iterator().readNumber();
          //System.out.println("* resolveNumber: "+ val+ " : "+ query.operation());
          bool = query.operation().apply(val);
          //System.out.println("* after resolveNumber");
          debug(query, val, bool);
          return bool;
        case START_OBJECT:
          //System.out.println("* resolveObject");
          return resolve(query, sbr);
        case START_ARRAY:
          //System.out.println("* resolveArray");
          return resolveArray(query, sbr);
        default:
          return false;
      }
    }
    
    
    private boolean resolveList(List<Query> lst, StringByteReader sbr, boolean and) {
      if(lst == null || lst.isEmpty()) return false;
      int pos = sbr.getBuffer().position();
      boolean res = and;
      for(Query q : lst) {
        boolean bool = resolve(q, sbr);
        res = and ? res && bool : res || bool;
        sbr.getBuffer().position(pos);
      }
      return res;
    }
    
    
    private boolean hasChildren(Query q) {
      return !q.ands().isEmpty()
          || !q.childs().isEmpty()
          || !q.ors().isEmpty();
    }
    
    
    private int setFieldPosition(Query q, StringByteReader sbr) {
      int pos = sbr.getBuffer().position();
      if(sbr.indexOf(q.name()) < 0) {
        sbr.getBuffer().position(pos);
      } else {
        pos = sbr.getBuffer().position();
      }
      return pos;
    }
    
    
    private boolean resolveChildren(Query q, StringByteReader sbr) {
        int pos = setFieldPosition(q, sbr);
        boolean ands = q.ands().isEmpty() 
            || resolveList(q.ands(), sbr, true);
        sbr.getBuffer().position(pos);
        boolean childs = q.childs().isEmpty() 
            || resolveList(q.childs(), sbr, true);
        sbr.getBuffer().position(pos);
        boolean ors = resolveList(q.ors(), sbr, false);
        return (ands && childs) || ors;
    }
    
    
    private boolean resolve(Query query, StringByteReader sbr) {
      boolean resolve = false;
      if(hasChildren(query)) {
        resolve = resolveChildren(query, sbr);
      }
      else if(sbr.indexOf(query.name()) < 0) {
        resolve = false;
      }
      else {
        // skip field name
        sbr.iterator().skip();
        resolve = resolveType(query, sbr);
      }
      return resolve;
    }
    

    @Override
    public Boolean apply(Query query, ByteReader rdr) {
      if(query == null || query.name() == null || rdr == null) {
        return false;
      }
      return resolve(query, StringByteReader.of(rdr));
    }
    
    
    private void debug(Query query, Object val, Object result) {
      if(debug) {
        String msg = query.name() + " -> "
            + Objects.toString(val).trim()
            + (query.operation() == null ? "" : query.operation())
            + result;
        System.err.println(msg);
      }
    }
  
  }
  
}
