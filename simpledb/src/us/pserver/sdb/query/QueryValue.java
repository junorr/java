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
import java.util.List;
import us.pserver.sdb.util.ObjectUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/11/2014
 */
public class QueryValue {

  private Object value;
  
  private DataType type;
  
  
  public QueryValue() {
    value = null;
    type = null;
  }
  
  
  public QueryValue(Object val) {
    value(val);
  }
  
  
  public QueryValue value(Object val) {
    if(val == null)
      throw new IllegalArgumentException(
          "Invalid null value: "+ val);
    value = val;
    if(Number.class.isAssignableFrom(val.getClass())) {
      type = DataType.NUMBER;
    }
    else if(Boolean.class.isAssignableFrom(val.getClass())) {
      type = DataType.BOOLEAN;
    }
    else if(Date.class.isAssignableFrom(val.getClass())) {
      type = DataType.DATE;
    }
    else if(CharSequence.class.isAssignableFrom(val.getClass())) {
      type = DataType.STRING;
    }
    else if(val.getClass().isArray() 
        || List.class.isAssignableFrom(val.getClass())) {
      type = DataType.ARRAY;
    }
    else {
      type = DataType.UNDEFINED;
    }
    return this;
  }
  
  
  public Object value() {
    return value;
  }
  
  
  public DataType type() {
    return type;
  }
  
  
  public QueryValue type(DataType dt) {
    if(dt == null)
      throw new IllegalArgumentException("Invalid DataType: "+ dt);
    type = dt;
    return this;
  }
  
  
  public Number asNumber() {
    if(value == null)
      throw new IllegalStateException("Invalid null value: "+ value);
    return Double.parseDouble(value.toString());
  }
  
  
  public Boolean asBoolean() {
    if(value == null)
      throw new IllegalStateException("Invalid null value: "+ value);
    return Boolean.parseBoolean(value.toString());
  }
  
  
  public Date asDate() {
    if(value == null)
      throw new IllegalStateException("Invalid null value: "+ value);
    return (Date) value;
  }
  
  
  public String asString() {
    if(value == null)
      throw new IllegalStateException("Invalid null value: "+ value);
    return value.toString();
  }
  
  
  @Override
  public String toString() {
    return (value != null 
        ? (ObjectUtils.isArray(value) 
          ? Arrays.toString(ObjectUtils.toArray(value)) 
          : value.toString()) 
        : null);
  }
  
}
