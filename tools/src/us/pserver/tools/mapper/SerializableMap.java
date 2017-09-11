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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/09/2017
 */
public class SerializableMap<K,V> implements Map<K,V>, Serializable {
  
  private final HashMap<K,V> map;
  
  public SerializableMap() {
    this.map = new HashMap<>();
  }

  @Override
  public int size() {
    return map.size();
  }


  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }


  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }


  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }


  @Override
  public V get(Object key) {
    return map.get(key);
  }


  @Override
  public V put(K key, V value) {
    if(value != null && !Serializable.class.isAssignableFrom(value.getClass())) {
      throw new IllegalArgumentException("Value object should implement Serializable");
    }
    if(key != null && !Serializable.class.isAssignableFrom(key.getClass())) {
      throw new IllegalArgumentException("Key object should implement Serializable");
    }
    return map.put(key, value);
  }


  @Override
  public V remove(Object key) {
    return map.remove(key);
  }


  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    map.putAll(m);
  }


  @Override
  public void clear() {
    map.clear();
  }


  @Override
  public Set<K> keySet() {
    return map.keySet();
  }


  @Override
  public Collection<V> values() {
    return map.values();
  }


  @Override
  public Set<Entry<K, V>> entrySet() {
    return map.entrySet();
  }

}
