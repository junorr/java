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

package us.pserver.sdb.query;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import us.pserver.sdb.Document;
import us.pserver.sdb.SDBException;
import us.pserver.sdb.util.ObjectUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/09/2014
 */
public class Query1 {
  
  private List<QueryPath> path;
  
  private List<Query1> and;
  
  private List<Query1> or;
  
  
  private boolean result;
  
  private int limit;
  
  private String label;
  
  
  protected Query1(List<QueryPath> path, String label) {
    if(path == null || path.isEmpty())
      throw new IllegalArgumentException(
          "Invalid empty query path: "
          + (path == null ? "null" : "path.size()=0"));
    
    this.path = path;
    this.label = label;
    and = new LinkedList<>();
    or = new LinkedList<>();
    result = false;
    limit = -1;
  }
  
  
  public List<Query1> and() {
    return and;
  }
  
  
  public List<Query1> or() {
    return or;
  }
  
  
  public Query1 and(Query1 qry) {
    if(qry != null 
        && (!qry.isEmpty() 
        ||  qry.label() != null)) {
      and.add(qry);
    }
    return this;
  }
  
  
  public Query1 or(Query1 qry) {
    if(qry != null 
        && (!qry.isEmpty() 
        ||  qry.label() != null)) {
      or.add(qry);
    }
    return this;
  }
  
  
  public Query1 limit(int lmt) {
    limit = lmt;
    return this;
  }
  
  
  public int limit() {
    return limit;
  }
  
  
  public String label() {
    return label;
  }
  
  
  protected boolean getResult() {
    return result;
  }
  
  
  public boolean isEmpty() {
    return path.isEmpty();
  }
  
  
  private boolean xcontarray(QueryValue val, Object arg) {
    if(arg == null 
        || (!arg.getClass().isArray() 
        &&  !List.class.isAssignableFrom(arg.getClass()))) 
        return false;
    
    Object[] vals = toArray(val);
    Object[] args = null;
    if(ObjectUtils.isArray(arg)) {
      args = ObjectUtils.toArray(arg);
    }
    else {
      args = ((List) arg).toArray();
    }
    return xcontarray(vals, args);
  }
  
  
  private Object[] toArray(QueryValue val) {
    Object value = val.value();
    Object[] vals = null;
    if(ObjectUtils.isArray(value)) {
      vals = ObjectUtils.toArray(value);
    }
    else if(ObjectUtils.isList(value)) {
      vals = ((List) value).toArray();
    }
    else {
      vals = new Object[] { value };
    }
    return vals;
  }
  
  
  private boolean xcontarray(Object[] val, Object[] arg) {
    if(val == null 
        || arg == null 
        || val.length < 1 
        || arg.length < 1)
      return false;
    
    boolean[] bools = new boolean[val.length];
    for(int i = 0; i < val.length; i++) {
      bools[i] = false;
      for(int j = 0; j < arg.length; j++) {
        if(Objects.equals(val[i], arg[j])) {
          bools[i] = true;
          break;
        }
      }
    }
    boolean res = true;
    for(int i = 0; i < bools.length; i++) {
      res = res && bools[i];
    }
    return res;
  }
  
  
  private boolean xcontains(QueryValue val, Object arg) {
    if(ObjectUtils.isArray(arg) 
        || ObjectUtils.isList(arg)) {
      return xcontarray(val, arg);
    }
    return arg.toString().contains(val.asString());
  }
  
  
  private boolean xcontics(QueryValue val, Object arg) {
    return arg.toString().toLowerCase()
        .contains(val.asString().toLowerCase());
  }
  
  
  private boolean xendsw(QueryValue val, Object arg) {
    return arg.toString().toLowerCase()
        .endsWith(val.asString().toLowerCase());
  }
  
  
  private boolean xstartsw(QueryValue val, Object arg) {
    return arg.toString().toLowerCase()
        .startsWith(val.asString().toLowerCase());
  }
  
  
  private boolean xeq(QueryValue val, Object arg) {
    if(val == null || arg == null)
      return false;
    if(ObjectUtils.isArray(arg)) {
      return Arrays.deepEquals(toArray(val), ObjectUtils.toArray(arg));
    }
    
    QueryValue varg = new QueryValue(arg);
    Object o1 = val.asString();
    Object o2 = varg.asString();
    if(val.type() == DataType.NUMBER) {
      o1 = val.asNumber();
      o2 = varg.asNumber();
    } else if(val.type() == DataType.DATE) {
      o1 = val.asDate();
      o2 = varg.asDate();
    } else if(val.type() == DataType.BOOLEAN) {
      o1 = val.asBoolean();
      o2 = varg.asBoolean();
    } 
    return Objects.equals(o1, o2);
  }
  
  
  private boolean xeqics(QueryValue val, Object arg) {
    return arg.toString().toLowerCase()
        .endsWith(val.asString().toLowerCase());
  }
  
  
  private boolean xgt(QueryValue val, Object arg) {
    QueryValue arv = new QueryValue(arg);
    if(val.type() == DataType.NUMBER) {
      Number n1 = val.asNumber();
      Number n2 = arv.asNumber();
      return n2.doubleValue() > n1.doubleValue();
    } else if(val.type() == DataType.DATE) {
      Date d1 = val.asDate();
      Date d2 = arv.asDate();
      return d2.after(d1);
    } else if(val.type() == DataType.BOOLEAN) {
      boolean b1 = val.asBoolean();
      boolean b2 = arv.asBoolean();
      return b2 && !b1;
    } else {
      return arg.toString().length() > val.asString().length();
    }
  }
  
  
  private boolean xge(QueryValue val, Object arg) {
    QueryValue arv = new QueryValue(arg);
    if(val.type() == DataType.NUMBER) {
      Number n1 = val.asNumber();
      Number n2 = arv.asNumber();
      return n2.doubleValue() >= n1.doubleValue();
    } else if(val.type() == DataType.DATE) {
      Date d1 = val.asDate();
      Date d2 = arv.asDate();
      return d2.equals(d1) || d2.after(d1);
    } else if(val.type() == DataType.BOOLEAN) {
      boolean b1 = val.asBoolean();
      boolean b2 = arv.asBoolean();
      return (b2 && !b1) || Objects.equals(b1, b1);
    } else {
      return arg.toString().length() >= val.asString().length();
    }
  }
  
  
  private boolean xlt(QueryValue val, Object arg) {
    QueryValue arv = new QueryValue(arg);
    if(val.type() == DataType.NUMBER) {
      Number n1 = val.asNumber();
      Number n2 = arv.asNumber();
      return n2.doubleValue() < n1.doubleValue();
    } else if(val.type() == DataType.DATE) {
      Date d1 = val.asDate();
      Date d2 = arv.asDate();
      return d2.before(d1);
    } else if(val.type() == DataType.BOOLEAN) {
      boolean b1 = val.asBoolean();
      boolean b2 = arv.asBoolean();
      return !b2 && b1;
    } else {
      return arg.toString().length() < val.asString().length();
    }
  }
  
  
  private boolean xle(QueryValue val, Object arg) {
    QueryValue arv = new QueryValue(arg);
    if(val.type() == DataType.NUMBER) {
      Number n1 = val.asNumber();
      Number n2 = arv.asNumber();
      return n2.doubleValue() <= n1.doubleValue();
    } else if(val.type() == DataType.DATE) {
      Date d1 = val.asDate();
      Date d2 = arv.asDate();
      return d2.equals(d1) || d2.before(d1);
    } else if(val.type() == DataType.BOOLEAN) {
      boolean b1 = val.asBoolean();
      boolean b2 = arv.asBoolean();
      return (!b2 && b1) || Objects.equals(b1, b1);
    } else {
      return arg.toString().length() <= val.asString().length();
    }
  }
  
  
  private boolean xempty(Object arg) {
    return arg == null || arg.toString().equalsIgnoreCase("null");
  }
  
  
  protected boolean exec(QueryMethod mth, QueryValue val, Object arg) throws SDBException {
    if(mth == null) return false;
    boolean bool = false;
    switch(mth) {
      case CONTAINS:
        bool = xcontains(val, arg);
        break;
      case CONTAINS_ICS:
        bool = xcontics(val, arg);
        break;
      case EMPTY:
        bool = xempty(arg);
        break;
      case ENDS_WITH:
        bool = xendsw(val, arg);
        break;
      case EQUALS:
        bool = xeq(val, arg);
        break;
      case EQUALS_ICS:
        bool = xeqics(val, arg);
        break;
      case GREATER:
        bool = xgt(val, arg);
        break;
      case GREATER_EQ:
        bool = xge(val, arg);
        break;
      case LESSER:
        bool = xlt(val, arg);
        break;
      case LESSER_EQ:
        bool = xle(val, arg);
        break;
      case STARTS_WITH:
        bool = xstartsw(val, arg);
        break;
      default:
        bool = false;
        break;
    }
    System.out.println("-> query: "+ arg+ " "+ mth+ " "+ val+ " : "+ bool);
    return bool;
  }
  
  
  protected boolean exec(Document doc, Iterator<QueryPath> iterator, boolean eval) {
    if(doc == null 
        || iterator == null 
        || !iterator.hasNext())
      return false;
    
    Object arg = null;
    QueryValue value = null;
    String field = null;
    boolean not = false;
    
    while(iterator.hasNext()) {
      QueryPath p = iterator.next();
      if(p.isField()) {
        field = p.field();
        if(!doc.map().containsKey(field)) {
          result = false;
          eval = false;
          continue;
        }
        arg = doc.get(field);
      }
      else if(p.isValue()) {
        value = p.value();
      }
      else if(p.isMethod()) {
        if(field == null) {
          result = false;
          eval = false;
        }
        if(p.method() == QueryMethod.DESCEND) {
          if(arg == null || !(arg instanceof Document)) {
            result = false;
            eval = false;
          }
          System.out.println("-> query: descend --> "+ field);
          result = exec((Document) arg, iterator, eval);
        }
        else if(p.method() == QueryMethod.NOT) {
          not = !not;
          System.out.println("-> query: not="+ not);
        }
        else if(eval) {
          result = exec(p.method(), value, arg);
        }
      }
    }
    return (not ? !result : result);
  }
  
  
  public boolean exec(Document doc) throws SDBException {
    if(doc == null) return false;
    if(doc.label() != null && label != null && !doc.label().equals(label))
      return false;
    
    Iterator<QueryPath> it = path.iterator();
    boolean exec = exec(doc, it, true);
    for(int i = 0; i < or.size(); i++) {
      exec = exec || or.get(i).exec(doc);
    }
    for(int i = 0; i < and.size(); i++) {
      exec = exec && and.get(i).exec(doc);
    }
    return exec;
  }
  
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if(label != null) {
      sb.append(label);
    }
    sb.append("( ");
    
    QueryPath value = null;
    for(int i = 0; i < path.size(); i++) {
      QueryPath p = path.get(i);
      if(p.isValue())
        value = p;
      else if(p.isMethod()) {
        sb.append(p).append(" ");
        if(value != null) {
          sb.append(value.toString()).append(" ");
          value = null;
        }
      }
      else
        sb.append(path.get(i).toString()).append(" ");
    }
    sb.append(")");
    
    for(int i = 0; i < or.size(); i++) {
      sb.append(" || ").append(or.get(i).toString());
    }
    for(int i = 0; i < and.size(); i++) {
      sb.append(" && ").append(and.get(i).toString());
    }
    
    return sb.toString();
  }
  
}
