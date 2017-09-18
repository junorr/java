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

package us.pserver.tools.mapper;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;
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
