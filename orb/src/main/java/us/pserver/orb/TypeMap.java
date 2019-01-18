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

package us.pserver.orb;

import java.net.InetAddress;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/03/2018
 */
public class TypeMap implements Map<String,Object> {

  private final Map<String,Object> map;
  
  private final TypeStrings types;
  
  
  public TypeMap(Map<String,Object> map, TypeStrings types) {
    this.map = Match.notNull(map).getOrFail("Bad null Map");
    this.types = Match.notNull(types).getOrFail("Bad null TypedStrings");
  }
  
  
  public <T> T getAs(String key, Class<T> valueType) {
    return types.asType(Objects.toString(map.get(key)), valueType);
  }
  
  public Number getNumber(String key) {
    return types.asType(Objects.toString(map.get(key)), Double.class);
  }
  
  public boolean isNumber(String key) {
    Class<?> cls = types.guessTypeFromPattern(Objects.toString(map.get(key)));
    return cls == Double.class || cls == Long.class;
  }
  
  public int getInt(String key) {
    return types.asType(Objects.toString(map.get(key)), Integer.class);
  }
  
  public short getShort(String key) {
    return types.asType(Objects.toString(map.get(key)), Short.class);
  }
  
  public short getByte(String key) {
    return types.asType(Objects.toString(map.get(key)), Byte.class);
  }
  
  public boolean getBoolean(String key) {
    return types.asType(Objects.toString(map.get(key)), Boolean.class);
  }
  
  public boolean isBoolean(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == Boolean.class;
  }
  
  public char getChar(String key) {
    return getCharacter(key);
  }
  
  public Character getCharacter(String key) {
    return types.asType(Objects.toString(map.get(key)), Character.class);
  }
  
  public boolean isCharacter(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == Character.class;
  }
  
  public long getLong(String key) {
    return types.asType(Objects.toString(map.get(key)), Long.class);
  }
  
  public boolean isLong(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == Long.class;
  }
  
  public double getDouble(String key) {
    return types.asType(Objects.toString(map.get(key)), Double.class);
  }
  
  public boolean isDouble(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == Double.class;
  }
  
  public float getFloat(String key) {
    return types.asType(Objects.toString(map.get(key)), Float.class);
  }
  
  public Class getClass(String key) {
    return types.asType(Objects.toString(map.get(key)), Class.class);
  }
  
  public boolean isClass(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == Class.class;
  }
  
  public ZonedDateTime getZonedDateTime(String key) {
    return types.asType(Objects.toString(map.get(key)), ZonedDateTime.class);
  }
  
  public boolean isZonedDateTime(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == ZonedDateTime.class;
  }
  
  public Path getPath(String key) {
    return types.asType(Objects.toString(map.get(key)), Path.class);
  }
  
  public OffsetTime getOffsetTime(String key) {
    return types.asType(Objects.toString(map.get(key)), OffsetTime.class);
  }
  
  public boolean isOffsetTime(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == OffsetTime.class;
  }
  
  public LocalTime getLocalTime(String key) {
    return types.asType(Objects.toString(map.get(key)), LocalTime.class);
  }
  
  public boolean isLocalTime(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == LocalTime.class;
  }
  
  public LocalDate getLocalDate(String key) {
    return types.asType(Objects.toString(map.get(key)), LocalDate.class);
  }
  
  public boolean isLocalDate(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == LocalDate.class;
  }
  
  public LocalDateTime getLocalDateTime(String key) {
    return types.asType(Objects.toString(map.get(key)), LocalDateTime.class);
  }
  
  public boolean isLocalDateTime(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == LocalDateTime.class;
  }
  
  public Instant getInstant(String key) {
    return types.asType(Objects.toString(map.get(key)), Instant.class);
  }
  
  public boolean isInstant(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == Instant.class;
  }
  
  public ZoneId getZoneId(String key) {
    return types.asType(Objects.toString(map.get(key)), ZoneId.class);
  }
  
  public boolean isZoneId(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == ZoneId.class;
  }
  
  public ZoneOffset getZoneOffset(String key) {
    return types.asType(Objects.toString(map.get(key)), ZoneOffset.class);
  }
  
  public boolean isZoneOffset(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == ZoneOffset.class;
  }
  
  public InetAddress getInetAddress(String key) {
    return types.asType(Objects.toString(map.get(key)), InetAddress.class);
  }
  
  public boolean isInetAddress(String key) {
    return types.guessTypeFromPattern(Objects.toString(map.get(key))) == InetAddress.class;
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
  public Object get(Object key) {
    return map.get(key);
  }


  @Override
  public Object put(String key, Object value) {
    return map.put(key, value);
  }


  @Override
  public Object remove(Object key) {
    return map.remove(key);
  }


  @Override
  public void putAll(Map<? extends String, ? extends Object> m) {
    map.putAll(m);
  }


  @Override
  public void clear() {
    map.clear();
  }


  @Override
  public Set<String> keySet() {
    return map.keySet();
  }


  @Override
  public Collection<Object> values() {
    return map.values();
  }


  @Override
  public Set<Entry<String, Object>> entrySet() {
    return map.entrySet();
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.map);
    hash = 79 * hash + Objects.hashCode(this.types);
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
    final TypeMap other = (TypeMap) obj;
    if (!Objects.equals(this.map, other.map)) {
      return false;
    }
    if (!Objects.equals(this.types, other.types)) {
      return false;
    }
    return true;
  }
  
  
  @Override
  public String toString() {
    return map.toString();
  }
  
}
