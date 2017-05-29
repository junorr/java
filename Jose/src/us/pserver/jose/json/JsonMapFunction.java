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

package us.pserver.jose.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import us.pserver.jose.json.iterator.ByteIterator;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/03/2017
 */
public interface JsonMapFunction extends Function<ByteIterator,Map<String,Object>> {

  @Override 
  public Map<String,Object> apply(ByteIterator biter);
  
  
  
  public static JsonMapFunction get() {
    return new JsonMapReaderImpl();
  }
  
  
  
  
  
  static class JsonMapReaderImpl implements JsonMapFunction {

    
    private List readArray(ByteIterator biter) {
      List array = new ArrayList();
      JsonType type;
      while((type = biter.nextValueType()) != JsonType.UNKNOWN 
          && type != JsonType.END_ARRAY) {
        array.add(readValue(type, biter));
      }
      return array;
    }
    
    
    private Object readValue(JsonType type, ByteIterator biter) {
      switch(type) {
          case BOOLEAN:
            return biter.readBoolean();
          case NULL:
            return null;
          case NUMBER:
            return biter.readNumber();
          case START_ARRAY:
            return readArray(biter);
          case START_OBJECT:
            return apply(biter);
          case STRING:
            return biter.readString();
          default:
            return null;
      }
    }
    
    
    @Override
    public Map<String,Object> apply(ByteIterator biter) {
      Map<String,Object> map = new HashMap<>();
      String fld = null;
      while(biter.hasNext()) {
        JsonType type = biter.next();
        if(fld == null && type == JsonType.STRING) {
          fld = biter.readField();
          type = biter.next();
        }
        if(type == JsonType.UNKNOWN 
            || type == JsonType.END_OBJECT) {
          break;
        }
        switch(type) {
          case FIELD:
            fld = biter.readField();
            break;
          case BOOLEAN:
          case NULL:
          case NUMBER:
          case START_ARRAY:
          case START_OBJECT:
          case STRING:
            if(fld != null) {
              map.put(fld, readValue(type, biter));
            }
            break;
        }
      }
      return map;
    }
    
  }
  
}
