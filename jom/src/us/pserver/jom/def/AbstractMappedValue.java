/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.jom.def;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import us.pserver.jom.MappedValue;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/09/2017
 */
public abstract class AbstractMappedValue<T> implements MappedValue<T> {

  private final T value;
  
  private final Type type;
  
  public AbstractMappedValue() {
    value = null;
    type = null;
  }
  
  protected AbstractMappedValue(T value, Type type) {
    this.value = NotNull.of(value).getOrFail();
    this.type = NotNull.of(type).getOrFail();
  }

  @Override
  public T get() {
    return this.value;
  }
  
  @Override
  public Type getType() {
    return this.type;
  }
  
  @Override
  public String asString() {
    throw new UnsupportedOperationException("Not a String");
  }

  @Override
  public MappedValue[] asArray() {
    throw new UnsupportedOperationException("Not an Array");
  }

  @Override
  public Boolean asBoolean() {
    throw new UnsupportedOperationException("Not a Boolean");
  }

  @Override
  public Number asNumber() {
    throw new UnsupportedOperationException("Not a Number");
  }
  
  @Override
  public byte[] asByteArray() {
    throw new UnsupportedOperationException("Not a byte[] array");
  }

  @Override
  public Map<String, MappedValue> asMap() {
    throw new UnsupportedOperationException("Not a Map");
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + Objects.hashCode(this.value);
    hash = 71 * hash + Objects.hashCode(this.type);
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
    final AbstractMappedValue<?> other = (AbstractMappedValue<?>) obj;
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return this.type == other.type;
  }


  @Override
  public String toString() {
    return Objects.toString(value);
  }

  @Override public void ifString(Consumer<String> exec) {}

  @Override public void ifArray(Consumer<MappedValue[]> exec) {}

  @Override public void ifBoolean(Consumer<Boolean> exec) {}

  @Override public void ifNumber(Consumer<Number> exec) {}
  
  @Override public void ifByteArray(Consumer<byte[]> exec) {}
  
  @Override public void ifMap(Consumer<Map<String, MappedValue>> exec) {}
  
}
