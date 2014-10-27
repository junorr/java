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

package us.pserver.sdb;

import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import us.pserver.date.SimpleDate;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/09/2014
 */
public class Query {
  
  
  public static enum QueryMethod {
    EQUALS, 
    EQUALS_ICS, 
    CONTAINS, 
    CONTAINS_ICS, 
    STARTS_WITH, 
    ENDS_WITH, 
    GREATER, 
    LESSER, 
    GREATER_EQ, 
    LESSER_EQ,
    EMPTY;
    @Override
    public String toString() {
      switch(this) {
        case EQUALS:
          return "==";
        case EQUALS_ICS:
          return "==(ics)";
        case CONTAINS:
          return "CONTAINS";
        case CONTAINS_ICS:
          return "CONTAINS(ics)";
        case STARTS_WITH:
          return "[STARTS]...";
        case ENDS_WITH:
          return "...[ENDS]";
        case GREATER:
          return ">";
        case GREATER_EQ:
          return ">=";
        case LESSER:
          return "<";
        case LESSER_EQ:
          return "<=";
        default:
          return "IS_EMPTY{}";
      }
    }
  }
  
  
  public static enum DataType {
    STRING,
    DATE,
    BOOLEAN,
    NUMBER;
  }

  
  protected Query next, prev, head, other;
  
  private QueryMethod meth;
  
  private DataType type;
  
  private boolean not;
  
  private boolean and;
  
  private boolean or;
  
  private boolean result;
  
  private boolean exec;
  
  private boolean descend;
  
  private Object value;
  
  private String key;
  
  private String label;
  
  private int limit;
  
  
  protected Query() {
    next = prev = null;
    head = null;
    meth = null;
    key = null;
    result = false;
    exec = false;
    descend = false;
    type = null;
    and = or = not = false;
  }
  
  
  protected Query(Query hd, Query pv) {
    head = hd;
    prev = pv;
    next = null;
    meth = null;
    key = null;
    type = null;
    and = or = not = false;
  }
  
  
  public Query(String label) {
    this();
    label(label);
  }
  
  
  public Query(String label, String key) {
    this();
    label(label).key(key);
  }
  
  
  public Query(Class cls) {
    this(cls.getName());
  }
  
  
  public Query label(String lbl) {
    if(lbl == null)
      throw new SDBException("Invalid label: "+ lbl+ " - [Query.label]");
    label = lbl;
    return this;
  }
  
  
  public String label() {
    return label;
  }
  
  
  public Query className(String lbl) {
    if(lbl == null)
      throw new SDBException("Invalid label: "+ lbl+ " - [Query.className]");
    label = lbl;
    return this;
  }
  
  
  public String className() {
    return label;
  }
  
  
  public Query limit(int lmt) {
    if(head == null)
      limit = lmt;
    else
      head.limit = lmt;
    return this;
  }
  
  
  public int limit() {
    if(head == null)
      return limit;
    else
      return head.limit;
  }
  
  
  private Query setNext() {
    if(next == null) {
      if(head != null)
        next = new Query(head, this);
      else
        next = new Query(this, this);
    }
    return next;
  }
  
  
  private boolean executed() {
    return exec;
  }
  
  
  protected boolean isDescend() {
    return descend;
  }
  
  
  private boolean computeResult() {
    if(next != null && next.executed()) {
      boolean nr = next.computeResult();
      if(and) result = result && nr;
      else if(or) result = result || nr;
    }
    return result;
  }
  
  
  protected boolean getResult() {
    return result;
  }
  
  
  public boolean result() {
    if(head != null)
      return head.result();
    return computeResult();
  }
  
  
  public String key() {
    return key;
  }
  
  
  public Object value() {
    return value;
  }
  
  
  public Query head() {
    return (head == null ? this : head);
  }
  
  
  public Query key(String k) {
    if(k == null)
      throw new SDBException("Invalid key: "+ k+ " - [Query.key]");
    key = k;
    return this;
  }
  
  
  public Query field(String key) {
    return key(key);
  }
  
  
  public String field() {
    return key();
  }
  
  
  protected Query next() {
    return setNext();
  }
  
  
  protected Query prev() {
    return prev;
  }
  
  
  protected Query other() {
    return other;
  }
  
  
  protected QueryMethod method() {
    return meth;
  }
  
  
  protected DataType type() {
    return type;
  }
  
  
  public Query asDate() {
    type = DataType.DATE;
    return this;
  }
  
  
  public Query asString() {
    type = DataType.STRING;
    return this;
  }
  
  
  public Query asNumber() {
    type = DataType.NUMBER;
    return this;
  }
  
  
  public Query asBoolean() {
    type = DataType.NUMBER;
    return this;
  }
  
  
  public Query asDate(Object val) {
    type = DataType.DATE;
    value = val;
    return this;
  }
  
  
  public Query asString(Object val) {
    type = DataType.STRING;
    value = val;
    return this;
  }
  
  
  public Query asNumber(Object val) {
    type = DataType.NUMBER;
    value = val;
    return this;
  }
  
  
  public Query asBoolean(Object val) {
    type = DataType.BOOLEAN;
    value = val;
    return this;
  }
  
  
  public boolean isAnd() {
    return and;
  }
  
  
  public boolean isOr() {
    return or;
  }
  
  
  public boolean isNot() {
    return not;
  }
  
  
  public Query or() {
    if(prev == null)
      throw new SDBException(
          "Query Error: Link condition before value - [Query.or]");
    prev.or = true;
    prev.and = false;
    key(prev.key());
    return this;
  }
  
  
  public Query and() {
    if(prev == null)
      throw new SDBException(
          "Query Error: Link condition before value - [Query.and]");
    prev.and = true;
    prev.or = false;
    key(prev.key());
    return this;
  }
  
  
  public Query descend(String field) {
    if(field == null)
      throw new SDBException(
          "Query Error: Invalid field ("+ field+ ") - [Query.descend]");
    descend = true;
    return this.field(field).next();
  }
  
  
  public Query not() {
    not = !not;
    return this;
  }
  
  
  public Query isEmpty() {
    meth = QueryMethod.EMPTY;
    return this;
  }
  
  
  public Query or(String key) {
    if(prev == null)
      throw new SDBException(
          "Query Error: Link condition before value - [Query.or]");
    prev.or = true;
    prev.and = false;
    return key(key);
  }
  
  
  public Query and(String key) {
    if(prev == null)
      throw new SDBException(
          "Query Error: Link condition before value - [Query.and]");
    prev.and = true;
    prev.or = false;
    return key(key);
  }
  
  
  public Query or(Query other) {
    if(other == null)
      throw new SDBException(
          "Query Error: Invalid nested Query - [Query.or]");
    if(prev == null)
      throw new SDBException(
          "Query Error: Link condition before value - [Query.or]");
    prev.or = true;
    prev.and = false;
    this.other = other;
    return next();
  }
  
  
  public Query and(Query other) {
    if(other == null)
      throw new SDBException(
          "Query Error: Invalid nested Query - [Query.and]");
    if(prev == null)
      throw new SDBException(
          "Query Error: Link condition before value - [Query.and]");
    prev.and = true;
    prev.or = false;
    this.other = other;
    return next();
  }
  
  
  private void setType(Object o) {
    if(o != null) {
      if(Number.class.isAssignableFrom(o.getClass()))
        type = DataType.NUMBER;
      else if(Boolean.class.isAssignableFrom(o.getClass()))
        type = DataType.STRING;
      else if(Date.class.isAssignableFrom(o.getClass()))
        type = DataType.DATE;
      else {
        try { 
          getDate(o.toString());
          type = DataType.DATE;
        } catch(SDBException e) {
          type = DataType.STRING;
        }
      }
    }
  }
  
  
  public Query equal(Object val) {
    if(val == null) return this;
    meth = QueryMethod.EQUALS;
    value = val;
    return next();
  }
  
  
  public Query equalIgnoreCase(Object val) {
    if(val == null) return this;
    meth = QueryMethod.EQUALS_ICS;
    type = DataType.STRING;
    value = val;
    return next();
  }
  
  
  public Query contains(Object val) {
    if(val == null) return this;
    meth = QueryMethod.CONTAINS;
    type = DataType.STRING;
    value = val;
    return next();
  }
  
  
  public Query containsIgnoreCase(Object val) {
    if(val == null) return this;
    meth = QueryMethod.CONTAINS_ICS;
    type = DataType.STRING;
    value = val;
    return next();
  }
  
  
  public Query endsWith(Object val) {
    if(val == null) return this;
    meth = QueryMethod.ENDS_WITH;
    type = DataType.STRING;
    value = val;
    return next();
  }
  
  
  public Query greater(Object val) {
    if(val == null) return this;
    meth = QueryMethod.GREATER;
    value = val;
    return next();
  }
  
  
  public Query greaterEquals(Object val) {
    if(val == null) return this;
    meth = QueryMethod.GREATER_EQ;
    value = val;
    return next();
  }
  
  
  public Query lesser(Object val) {
    if(val == null) return this;
    meth = QueryMethod.LESSER;
    value = val;
    return next();
  }
  
  
  public Query lesserEquals(Object val) {
    if(val == null) return this;
    meth = QueryMethod.LESSER_EQ;
    value = val;
    return next();
  }
  
  
  public Query startsWith(Object val) {
    if(val == null) return this;
    meth = QueryMethod.STARTS_WITH;
    type = DataType.STRING;
    value = val;
    return next();
  }
  
  
  public Query equal() {
    meth = QueryMethod.EQUALS;
    return next();
  }
  
  
  public Query equalIgnoreCase() {
    meth = QueryMethod.EQUALS_ICS;
    type = DataType.STRING;
    return next();
  }
  
  
  public Query contains() {
    meth = QueryMethod.CONTAINS;
    return next();
  }
  
  
  public Query containsIgnoreCase() {
    meth = QueryMethod.CONTAINS_ICS;
    type = DataType.STRING;
    return next();
  }
  
  
  public Query endsWith() {
    meth = QueryMethod.ENDS_WITH;
    type = DataType.STRING;
    return next();
  }
  
  
  public Query greater() {
    meth = QueryMethod.GREATER;
    return next();
  }
  
  
  public Query greaterEquals() {
    meth = QueryMethod.GREATER_EQ;
    return next();
  }
  
  
  public Query lesser() {
    meth = QueryMethod.LESSER;
    return next();
  }
  
  
  public Query lesserEquals() {
    meth = QueryMethod.LESSER_EQ;
    return next();
  }
  
  
  public Query startsWith() {
    meth = QueryMethod.STARTS_WITH;
    type = DataType.STRING;
    return next();
  }
  
  
  private Number getNumber(Object o) {
    try {
      return Double.parseDouble(o.toString());
    } catch(Exception e) {
      throw new SDBException("Invalid number value: "+ o+ " - [Query.getNumber]");
    }
  }
  
  
  public Date getDate(Object o) {
    String msg = "Invalid Date value: "+ o;
    if(o == null) 
      throw new SDBException(msg);
    SimpleDate d = null;
    try {
      d = SimpleDate.parseDate(o.toString());
    } catch(Exception e) {
      throw new SDBException(msg);
    }
    if(d == null)
      throw new SDBException(msg);
    return d;
  }
  
  
  public boolean getBoolean(Object o) {
    if(o == null 
        || (!o.toString().equalsIgnoreCase("true") 
        && !o.toString().equalsIgnoreCase("false")))
      throw new SDBException("Invalid boolean value: "+ o+ " - [Query.getBoolean]");
    return Boolean.parseBoolean(o.toString());
  }
  
  
  private boolean xcontains(Object o) {
    return o.toString().contains(value.toString());
  }
  
  
  private boolean xcontics(Object o) {
    return o.toString().toLowerCase()
        .contains(value.toString().toLowerCase());
  }
  
  
  private boolean xendsw(Object o) {
    return o.toString().toLowerCase()
        .endsWith(value.toString().toLowerCase());
  }
  
  
  private boolean xstartsw(Object o) {
    return o.toString().toLowerCase()
        .startsWith(value.toString().toLowerCase());
  }
  
  
  private boolean xeq(Object o) {
    Object o1 = value.toString();
    Object o2 = o.toString();
    if(type == DataType.NUMBER) {
      o1 = getNumber(value);
      o2 = getNumber(o);
    } else if(type == DataType.DATE) {
      o1 = getDate(value);
      o2 = getDate(o);
    } else if(type == DataType.BOOLEAN) {
      o1 = getBoolean(value);
      o2 = getBoolean(o);
    }
    return Objects.equals(o1, o2);
  }
  
  
  private boolean xeqics(Object o) {
    return o.toString().toLowerCase()
        .endsWith(value.toString().toLowerCase());
  }
  
  
  private boolean xgt(Object o) {
    if(type == DataType.NUMBER) {
      Number n1 = getNumber(value);
      Number n2 = getNumber(o);
      return n2.doubleValue() > n1.doubleValue();
    } else if(type == DataType.DATE) {
      Date d1 = getDate(value);
      Date d2 = getDate(o);
      return d2.after(d1);
    } else if(type == DataType.BOOLEAN) {
      boolean b1 = getBoolean(value);
      boolean b2 = getBoolean(o);
      return b2 && !b1;
    } else {
      return o.toString().length() > value.toString().length();
    }
  }
  
  
  private boolean xge(Object o) {
    if(type == DataType.NUMBER) {
      Number n1 = getNumber(value);
      Number n2 = getNumber(o);
      return n2.doubleValue() >= n1.doubleValue();
    } else if(type == DataType.DATE) {
      Date d1 = getDate(value);
      Date d2 = getDate(o);
      return d2.equals(d1) || d2.after(d1);
    } else if(type == DataType.BOOLEAN) {
      boolean b1 = getBoolean(value);
      boolean b2 = getBoolean(o);
      return (b2 && !b1) || Objects.equals(b1, b1);
    } else {
      return o.toString().length() >= value.toString().length();
    }
  }
  
  
  private boolean xlt(Object o) {
    if(type == DataType.NUMBER) {
      Number n1 = getNumber(value);
      Number n2 = getNumber(o);
      return n2.doubleValue() < n1.doubleValue();
    } else if(type == DataType.DATE) {
      Date d1 = getDate(value);
      Date d2 = getDate(o);
      return d2.before(d1);
    } else if(type == DataType.BOOLEAN) {
      boolean b1 = getBoolean(value);
      boolean b2 = getBoolean(o);
      return !b2 && b1;
    } else {
      return o.toString().length() < value.toString().length();
    }
  }
  
  
  private boolean xle(Object o) {
    if(type == DataType.NUMBER) {
      Number n1 = getNumber(value);
      Number n2 = getNumber(o);
      return n2.doubleValue() <= n1.doubleValue();
    } else if(type == DataType.DATE) {
      Date d1 = getDate(value);
      Date d2 = getDate(o);
      return d2.equals(d1) || d2.before(d1);
    } else if(type == DataType.BOOLEAN) {
      boolean b1 = getBoolean(value);
      boolean b2 = getBoolean(o);
      return (!b2 && b1) || Objects.equals(b1, b1);
    } else {
      return o.toString().length() <= value.toString().length();
    }
  }
  
  
  private boolean xempty(Object o) {
    return o == null || o.toString().equalsIgnoreCase("null");
  }
  
  
  protected Query exec(Object obj) throws SDBException {
    if(meth != null && meth != QueryMethod.EMPTY && obj == null)
      throw new SDBException("Invalid null argument - [Query.exec]");
    if(value == null && meth != null && meth != QueryMethod.EMPTY) {
      throw new SDBException("Invalid value: "+ value+ " - [Query.exec]");
    }
    
    if(type == null)
      setType(value);
    
    boolean bool = false;
    switch(meth) {
      case CONTAINS:
        bool = xcontains(obj);
        break;
      case CONTAINS_ICS:
        bool = xcontics(obj);
        break;
      case EMPTY:
        bool = xempty(obj);
        break;
      case ENDS_WITH:
        bool = xendsw(obj);
        break;
      case EQUALS:
        bool = xeq(obj);
        break;
      case EQUALS_ICS:
        bool = xeqics(obj);
        break;
      case GREATER:
        bool = xgt(obj);
        break;
      case GREATER_EQ:
        bool = xge(obj);
        break;
      case LESSER:
        bool = xlt(obj);
        break;
      case LESSER_EQ:
        bool = xle(obj);
        break;
      case STARTS_WITH:
        bool = xstartsw(obj);
        break;
      default:
        throw new SDBException("Unknown condition");
    }
    exec = true;
    result = (not ? !bool : bool);
    return this;
  }
  
  
  protected Query execute(Object obj) throws SDBException {
    if(head != null) {
      return head.exec(obj);
    }
    return exec(obj);
  }
  
  
  public static Query fromExample(Document doc) {
    Query q = new Query(doc.label());
    Iterator<String> it = doc.map().keySet().iterator();
    if(!it.hasNext()) return q;
    String key = it.next();
    q.key(key);
    q = q.equal(doc.map().get(key));
    while(it.hasNext()) {
      key = it.next();
      q = q.and(key).equal(doc.map().get(key));
    }
    System.out.println("* Query.fromExample: "+ q);
    return q;
  }
  
  
  @Override
  public String toString() {
    if(head != null) {
      return "Query"+head.toString();
    } else {
      return this.to_string();
    }
  }
  
  
  public String to_string() {
    StringBuffer sb = new StringBuffer();
    if(other != null)
      sb.append("(").append(other.toString()).append(")");
    
    if(label != null)
      sb.append(":").append(label);
    sb.append("( ");
    
    if(key != null) {
      if(value == null && meth == null)
        sb.append("-").append(key).append("->");
      else
        sb.append(key);
    }
    
    if(meth != null) {
      sb.append(" ");
      if(not) sb.append("!");
      sb.append(meth.toString());
      if(value != null)
        sb.append(" ").append(value);
    }
    
    if(next != null && (next.key != null || next.meth != null || next.other != null)) {
      if(or) sb.append(" || ");
      else if(and) sb.append(" && ");
      else sb.append(" ");
      sb.append(next.to_string());
    }
    sb.append(" )");
    if(limit > 0)
      sb.append(".limit(")
          .append(limit).append(")");
    return sb.toString();
  }
  
  
  public static void main(String[] args) {
    Query q = new Query("name")
        .contains("1")
        .and().not().contains("3");
    //q.execute("102").execute(true);
    System.out.println("* "+ q+ ": "+ q.execute("102").next().exec("102").result());
  }
  
}
