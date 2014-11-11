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

import java.util.Objects;
import us.pserver.sdb.Query.DataType;
import us.pserver.sdb.Query.QueryMethod;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 07/11/2014
 */
public class QueryPath {

  private String field;
  
  private QueryValue value;
  
  private QueryMethod method;
  
  private DataType type;
  
  
  public QueryPath() {
    field = null;
    value = null;
    method = null;
    type = null;
  }
  
  
  public boolean isField() {
    return field != null;
  }
  
  
  public boolean isValue() {
    return value != null;
  }
  
  
  public boolean isMethod() {
    return method != null;
  }
  
  
  public String field() {
    return field;
  }
  
  
  public QueryPath field(String fld) {
    field = fld;
    return this;
  }
  
  
  public QueryValue value() {
    return value;
  }
  
  
  public QueryPath value(QueryValue val) {
    if(val == null)
      throw new IllegalArgumentException("Invalid null value: "+ val);
    value = val;
    return this;
  }
  
  
  public QueryPath value(Object val) {
    if(val == null)
      throw new IllegalArgumentException("Invalid null value: "+ val);
    if(val instanceof QueryValue)
      value = (QueryValue) val;
    else
      value = new QueryValue(val);
    return this;
  }
  
  
  public QueryMethod method() {
    return method;
  }
  
  
  public QueryPath method(QueryMethod mth) {
    method = mth;
    return this;
  }
  
  
  public QueryPath type(DataType dt) {
    if(dt == null)
      throw new IllegalArgumentException("Invalid null DataType: "+ dt);
    type = dt;
    return this;
  }
  
  
  public DataType type() {
    return type;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 29 * hash + Objects.hashCode(this.field);
    hash = 29 * hash + Objects.hashCode(this.value);
    hash = 29 * hash + Objects.hashCode(this.method);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final QueryPath other = (QueryPath) obj;
    if (!Objects.equals(this.field, other.field)) {
      return false;
    }
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    if (this.method != other.method) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    if(field != null)
      return field;
    else if(value != null)
      return value.toString();
    else if(method != null)
      return method.toString();
    else
      return "null";
  }
  
}
