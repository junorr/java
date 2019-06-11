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

package us.pserver.screept.impl;

import java.util.Objects;
import java.util.Optional;
import us.pserver.screept.Memory;
import us.pserver.screept.Stack;
import us.pserver.screept.Value;
import us.pserver.screept.parse.Chars;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11 de jun de 2019
 */
public class ValueImpl implements Value {
  
  private final String svalue;
  
  private volatile int priority;
  
  public ValueImpl(String svalue) {
    this.svalue = Objects.requireNonNull(svalue);
    this.priority = 0;
  }
  
  public ValueImpl(String svalue, int priority) {
    this.svalue = Objects.requireNonNull(svalue);
    this.priority = priority;
  }
  
  @Override
  public ValueType getValueType() {
    if(Chars.isNumber(svalue)) {
      return ValueType.NUMBER;
    }
    else if(Chars.isBoolean(svalue)) {
      return ValueType.BOOLEAN;
    }
    else return ValueType.STRING;
  }
  
  @Override
  public String getAsString() {
    return svalue;
  }
  
  @Override
  public Boolean getAsBoolean() {
    return Boolean.parseBoolean(svalue);
  }
  
  @Override
  public Number getAsNumber() {
    if(svalue.contains(".")) {
      return Double.parseDouble(svalue);
    }
    else try {
      return Integer.parseInt(svalue);
    }
    catch(NumberFormatException e) {
      return Long.parseLong(svalue);
    }
  }
  
  @Override
  public Optional resolve(Memory m, Stack s) {
    switch(getValueType()) {
      case BOOLEAN:
        return Optional.of(getAsBoolean());
      case NUMBER:
        return Optional.of(getAsNumber());
      default:
        return Optional.of(svalue);
    }
  }
  
  @Override
  public int priority() {
    return priority;
  }
  
  @Override
  public void priority(int p) {
    this.priority = p;
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.svalue);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ValueImpl other = (ValueImpl) obj;
    return Objects.equals(this.svalue, other.svalue);
  }
  
  @Override
  public String toString() {
    return "Value(" + svalue + ')';
  }
  
}
