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

package us.pserver.job.json.adapter;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import us.pserver.job.json.JsonReader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/02/2017
 */
public class JsoniterReader implements JsonReader {

  private byte[] getBytes(ByteBuffer buf) {
    if(buf.hasArray()) {
      return buf.array();
    }
    byte[] bs = new byte[buf.remaining()];
    buf.get(bs);
    return bs;
  }
  
  
  private List toList(Any any) {
    if(any == null || any.valueType() != ValueType.ARRAY) {
      return Collections.EMPTY_LIST;
    }
    List<Any> lst = any.asList();
    ArrayList array = new ArrayList(lst.size());
    for(Any a : lst) {
      Object val;
      switch (a.valueType()) {
        case OBJECT:
          val = toMapObject(a);
          break;
        case ARRAY:
          val = toList(a);
          break;
        default:
          val = a.object();
          break;
      }
      array.add(val);
    }
    return array;
  }
  
  
  private Map<String,Object> toMapObject(Any any) {
    if(any == null || any.valueType() != ValueType.OBJECT) {
      return Collections.emptyMap();
    }
    Map<String,Object> map = new TreeMap<>();
    Map<String,Any> anymap = any.asMap();
    Set<Entry<String,Any>> ents = anymap.entrySet();
    for(Entry<String,Any> e : ents) {
      Object val;
      switch (e.getValue().valueType()) {
        case OBJECT:
          val = toMapObject(e.getValue());
          break;
        case ARRAY:
          val = toList(e.getValue());
          break;
        default:
          val = e.getValue().object();
          break;
      }
      map.put(e.getKey(), val);
    }
    return map;
  }
  
  
  @Override
  public Map<String, Object> read(ByteBuffer buf) {
    Any any = JsonIterator.deserialize(this.getBytes(buf));
    return toMapObject(any);
  }
  
}
