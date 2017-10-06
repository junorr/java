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

package us.pserver.jom;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;
import us.pserver.jom.def.ArrayValue;
import us.pserver.jom.def.BooleanValue;
import us.pserver.jom.def.ByteArrayValue;
import us.pserver.jom.def.MapValue;
import us.pserver.jom.def.NumberValue;
import us.pserver.jom.def.SerializableHashMap;
import us.pserver.jom.def.StringValue;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/09/2017
 */
public interface MappedValue<T> extends Serializable {
  
  public static enum Type {
    NUMBER, STRING, ARRAY, BOOLEAN, MAP, BYTE_ARRAY
  }
  
  public T get();
  
  public Type getType();
  
  public String asString();
  
  public MappedValue[] asArray();
  
  public Boolean asBoolean();
  
  public Number asNumber();
  
  public byte[] asByteArray();
  
  public Map<String,MappedValue> asMap();
  
  public void ifString(Consumer<String> exec);
  
  public void ifArray(Consumer<MappedValue[]> exec);
  
  public void ifBoolean(Consumer<Boolean> exec);
  
  public void ifNumber(Consumer<Number> exec);
  
  public void ifMap(Consumer<Map<String,MappedValue>> exec);
  
  public void ifByteArray(Consumer<byte[]> exec);
  
  
  public static MappedValue of(Object obj) {
    NotNull.of(obj).failIfNull();
    if(obj.getClass().isArray()) {
      return new ArrayValue((MappedValue[]) obj);
    }
    else if(Boolean.class.isAssignableFrom(obj.getClass())) {
      return new BooleanValue((Boolean) obj);
    }
    else if(SerializableHashMap.class.isAssignableFrom(obj.getClass())) {
      return new MapValue((Map<String,MappedValue>) obj);
    }
    else if(Number.class.isAssignableFrom(obj.getClass())) {
      return new NumberValue((Number) obj);
    }
    else if(String.class.isAssignableFrom(obj.getClass())) {
      return new StringValue((String) obj);
    }
    else if(byte[].class.isAssignableFrom(obj.getClass())) {
      return new ByteArrayValue((byte[]) obj);
    }
    else {
      throw new UnsupportedOperationException("Not supported object type");
    }
  }
  
}
