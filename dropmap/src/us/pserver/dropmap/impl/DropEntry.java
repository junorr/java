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
import java.util.Objects;
import us.pserver.dropmap.DMap;
import us.pserver.dropmap.DMap.DEntry;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/08/2016
 */
public class DropEntry<K,V> implements DEntry<K,V> {
  
  private final K key;
  
  private final V value;
  
  private final DMap<K,V> map;
  
  private final Instant stored;
  
  private Instant access;
  
  private final Instant update;
  
  private final Duration duration;
  
  
  public DropEntry(K k, V v, DMap<K,V> map, Instant stored, Duration dur) {
    if(k == null) {
      throw new IllegalArgumentException("Bad Null Key");
    }
    if(v == null) {
      throw new IllegalArgumentException("Bad Null Value");
    }
    if(map == null) {
      throw new IllegalArgumentException("Bad Null DMap");
    }
    if(stored == null) {
      throw new IllegalArgumentException("Bad Null Instant");
    }
    if(dur == null) {
      throw new IllegalArgumentException("Bad Null Duration");
    }
    this.key = k;
    this.value = v;
    this.map = map;
    this.stored = stored;
    this.duration = dur;
    this.update = Instant.now();
  }
  
  
  public DropEntry(K k, V v, DMap<K,V> map, Instant stored) {
    this(k, v, map, stored, Duration.ZERO);
  }
  
  
  public DropEntry(K k, V v, DMap<K,V> map) {
    this(k, v, map, Instant.now(), Duration.ZERO);
  }
  
  
  public DropEntry<K,V> update(K k, V v) {
    return new DropEntry(k, v, map, stored, duration);
  }
  
  
  @Override
  public K getKey() {
    return key;
  }
  

  @Override
  public V getValue() {
    return value;
  }
  
  
  @Override
  public DMap<K,V> getMap() {
    return map;
  }


  @Override
  public Duration getDuration() {
    return duration;
  }


  @Override
  public Duration getTTL() {
    Instant fut = stored.plus(duration);
    Instant now = Instant.now();
    return (fut.isBefore(now) 
        ? Duration.ZERO 
        : Duration.between(now, fut)
    );
  }


  @Override
  public Instant getStoredInstant() {
    return stored;
  }
  
  
  @Override
  public Instant getLastAccess() {
    return access;
  }
  
  
  public DropEntry<K,V> updateLastAccess() {
    this.access = Instant.now();
    return this;
  }


  @Override
  public Instant getLastUpdate() {
    return update;
  }


  @Override
  public int compareTo(DEntry<K, V> o) {
    if(o == null || o.getDuration() == null) {
      throw new IllegalArgumentException("Bad Null DEntry");
    }
    return duration.compareTo(o.getDuration());
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.key);
    hash = 97 * hash + Objects.hashCode(this.stored);
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
    final DropEntry<?, ?> other = (DropEntry<?, ?>) obj;
    if (!Objects.equals(this.key, other.key)) {
      return false;
    }
    if (!Objects.equals(this.stored, other.stored)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "DropEntry" 
        + "{\n   key.....: " + key 
        + ",\n   value...: " + value 
        + ",\n   stored..: " + stored 
        + ",\n   update..: " + update 
        + ",\n   duration: " + duration.toMillis() + " ms" 
        + ",\n   ttl.....: " + this.getTTL().toMillis() + " ms"
        + "\n}";
  }

}
