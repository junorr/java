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

package us.pserver.dropmap.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import us.pserver.dropmap.DMap;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2016
 */
public class DropMap<K,V> implements DMap<K,V> {
  
  private final Map<K,DEntry<K,V>> map;
  
  private final DEngine<K,V> engine;
  
  
  public DropMap() {
    map = Collections.synchronizedMap(new HashMap<K,DEntry<K,V>>());
    engine = new DropEngine(this);
  }
  
  
  private Duration duration(long time, TimeUnit unit) {
    Duration dur = null;
    switch(unit) {
      case DAYS:
        dur = Duration.of(time, ChronoUnit.DAYS);
        break;
      case HOURS:
        dur = Duration.of(time, ChronoUnit.HOURS);
        break;
      case MICROSECONDS:
        dur = Duration.of(time, ChronoUnit.MICROS);
        break;
      case MILLISECONDS:
        dur = Duration.of(time, ChronoUnit.MILLIS);
        break;
      case MINUTES:
        dur = Duration.of(time, ChronoUnit.MINUTES);
        break;
      case NANOSECONDS:
        dur = Duration.of(time, ChronoUnit.NANOS);
        break;
      case SECONDS:
        dur = Duration.of(time, ChronoUnit.SECONDS);
        break;
      default:
        dur = Duration.ZERO;
        break;
    }
    return dur;
  }
  

  @Override
  public DMap<K, V> put(K k, V v, long timeout, TimeUnit unit) {
    return this.put(k, v, duration(timeout, unit), null);
  }


  @Override
  public DMap<K, V> put(K k, V v, Duration dur, Consumer<DEntry<K, V>> cs) {
    if(k != null && v != null) {
      DEntry<K,V> entry = new DropEntry(k, v, this, Instant.now(), dur);
      map.put(k, entry);
      engine.add(entry, cs);
    }
    return this;
  }


  @Override
  public DMap<K, V> drop(K k, long timeout, TimeUnit unit) {
    return drop(k, duration(timeout, unit), null);
  }


  @Override
  public DMap<K, V> drop(K k, Duration dur, Consumer<DEntry<K, V>> cs) {
    if(k != null && map.containsKey(k)) {
      DEntry<K,V> orig = map.get(k);
      DEntry<K,V> entry = new DropEntry(
          orig.getKey(), 
          orig.getValue(), 
          this, 
          orig.getStoredInstant(), 
          dur
      );
      engine.add(entry, cs);
    }
    return this;
  }


  @Override
  public DMap<K, V> drop(K k) {
    engine.remove(map.remove(k));
    return this;
  }


  @Override
  public DEntry<K, V> entry(K k) {
    return map.get(k);
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
    if(map.containsKey(key)) {
      return map.get(key).getValue();
    }
    return null;
  }


  @Override
  public V put(K key, V value) {
    map.put(key, new DropEntry(key, value, this));
    return value;
  }


  @Override
  public V remove(Object key) {
    if(map.containsKey(key)) {
      DEntry<K,V> entry = map.remove(key);
      return entry.getValue();
    }
    return null;
  }


  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    if(m != null) {
      m.forEach((k,v)->{
        map.put(k, new DropEntry(k, v, this));
      });
    }
  }


  @Override
  public void clear() {
    map.clear();
    engine.reset();
  }


  @Override
  public Set<K> keySet() {
    return map.keySet();
  }


  @Override
  public Collection<V> values() {
    if(this.isEmpty()) {
      return Collections.EMPTY_LIST;
    }
    List<V> vs = new ArrayList<>(this.size());
    map.forEach((k,e)->vs.add(e.getValue()));
    return vs;
  }


  @Override
  public Set<Entry<K, V>> entrySet() {
    Set<Entry<K,V>> set = new HashSet<>();
    map.forEach((k,e)->{
      set.add(new SimpleEntry(k, e.getValue()));
    });
    return set;
  }

}
