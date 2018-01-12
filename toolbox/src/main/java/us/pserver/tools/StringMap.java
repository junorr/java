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

package us.pserver.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 */
public class StringMap implements Map<String,String> {
  
  public static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
  
  public static final String DATE_TIME_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*";
  
  public static final String CLASS_PATTERN = "\\w+\\..*\\.\\w+";
  
  public static final String IPV4_PATTERN = "(\\d+|\\*{1})\\.(\\d+|\\*{1})\\.(\\d+|\\*{1})\\.(\\d+|\\*{1})";
  
  public static final String DOUBLE_PATTERN = "-?\\d+\\.?\\d*";
  
  public static final String LONG_PATTERN = "-?\\d+";
  
  public static final String BOOLEAN_PATTERN = "(?i)(true|false)";
  

  private final Map<String,String> map;
  
  public StringMap(Map<String,String> map) {
    this.map = Match.notNull(map).getOrFail("Bad null Map");
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
  public String get(Object key) {
    return map.get(key);
  }
  
  public boolean isNumberValue(String key) {
    return isDoubleValue(key);
  }
  
  public boolean isDoubleValue(String key) {
    return this.get(key).matches(DOUBLE_PATTERN);
  }
  
  public boolean isLongValue(String key) {
    return this.get(key).matches(LONG_PATTERN);
  }
  
  public boolean isDateTimeValue(String key) {
    return this.get(key).matches(DATE_TIME_PATTERN);
  }
  
  public boolean isDateValue(String key) {
    return this.get(key).matches(DATE_PATTERN);
  }
  
  public boolean isClassValue(String key) {
    return this.get(key).matches(CLASS_PATTERN);
  }
  
  public boolean isIPv4AddressValue(String key) {
    return this.get(key).matches(IPV4_PATTERN);
  }
  
  public boolean isBooleanValue(String key) {
    return this.get(key).matches(BOOLEAN_PATTERN);
  }
  
  public boolean getAsBoolean(String key) {
    return Boolean.parseBoolean(this.get(key));
  }
  
  public double getAsDouble(String key) {
    return Double.parseDouble(this.get(key));
  }
  
  public long getAsLong(String key) {
    return Long.parseLong(this.get(key));
  }
  
  public long getAsInt(String key) {
    return Integer.parseInt(this.get(key));
  }
  
  public Path getAsPath(String key) {
    return Paths.get(this.get(key));
  }
  
  public LocalDateTime getAsLocalDateTime(String key) {
    try {
      return LocalDateTime.from(
          DateTimeFormatter.ISO_DATE_TIME.parse(this.get(key))
      );
    } catch(Exception e) {
      return null;
    }
  }
  
  public LocalDate getAsLocalDate(String key) {
    try {
      return LocalDate.from(
          DateTimeFormatter.ISO_DATE.parse(this.get(key))
      );
    } catch(Exception e) {
      return null;
    }
  }
  
  public LocalTime getAsLocalTime(String key) {
    try {
      return LocalTime.from(
          DateTimeFormatter.ISO_TIME.parse(this.get(key))
      );
    } catch(Exception e) {
      return null;
    }
  }
  
  public ZonedDateTime getAsZonedDateTime(String key) {
    try {
      return ZonedDateTime.from(
          DateTimeFormatter.ISO_DATE_TIME.parse(this.get(key))
      );
    } catch(Exception e) {
      return null;
    }
  }
  
  public OffsetTime getAsOffsetTime(String key) {
    try {
      return OffsetTime.from(
          DateTimeFormatter.ISO_TIME.parse(this.get(key))
      );
    } catch(Exception e) {
      return null;
    }
  }
  
  public Instant getAsInstant(String key) {
    try {
      return Instant.parse(this.get(key));
    } catch(Exception e) {
      return null;
    }
  }
  
  public Date getAsDate(String key) {
    LocalDateTime ldt = this.getAsLocalDateTime(key);
    if(ldt == null) return null;
    return Date.from(ldt.toInstant(ZoneOffset.from(Instant.now())));
  }
  
  public java.sql.Date getAsSqlDate(String key) {
    Date date = this.getAsDate(key);
    if(date == null) return null;
    return new java.sql.Date(date.getTime());
  }
  
  public Timestamp getAsTimestamp(String key) {
    Date date = this.getAsDate(key);
    if(date == null) return null;
    return new Timestamp(date.getTime());
  }
  
  public InetAddress getAsIPv4Address(String key) {
    try {
      return InetAddress.getByName(this.get(key));
    } catch(UnknownHostException e) {
      return null;
    }
  }
  
  public Class getAsClass(String key) {
    try {
      return Class.forName(this.get(key));
    } catch(ClassNotFoundException e) {
      return null;
    }
  }
  
  public Class getAsClass(String key, ClassLoader ldr) {
    try {
      return ldr.loadClass(this.get(key));
    } catch(ClassNotFoundException e) {
      return this.getAsClass(key);
    }
  }
  
  public <V> V getAs(String key, Function<String,V> transform) {
    return transform.apply(this.get(key));
  }
  
  @Override
  public String put(String key, String value) {
    return map.put(key, value);
  }
  
  public String put(String key, Path path) {
    return map.put(key, path.toAbsolutePath().toString());
  }
  
  public String put(String key, boolean bool) {
    return this.put(key, String.valueOf(bool));
  }
  
  public String put(String key, Number n) {
    return this.put(key, n.toString());
  }
  
  public String put(String key, Class c) {
    return this.put(key, c.getName());
  }
  
  public String put(String key, Date d) {
    LocalDateTime ldt = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
    return put(key, ldt);
  }
  
  public String put(String key, Instant it) {
    return this.put(key, it.toString());
  }
  
  public String put(String key, ZonedDateTime zdt) {
    return this.put(key, DateTimeFormatter.ISO_DATE_TIME.format(zdt));
  }
  
  public String put(String key, LocalDateTime ldt) {
    return this.put(key, DateTimeFormatter.ISO_DATE_TIME.format(ldt));
  }
  
  public String put(String key, LocalTime lt) {
    return this.put(key, DateTimeFormatter.ISO_TIME.format(lt));
  }
  
  public String put(String key, OffsetTime ot) {
    return this.put(key, DateTimeFormatter.ISO_TIME.format(ot));
  }
  
  public String put(String key, LocalDate ld) {
    return this.put(key, DateTimeFormatter.ISO_DATE.format(ld));
  }
  
  public String put(String key, InetAddress addr) {
    return this.put(key, addr.getHostAddress());
  }
  
  @Override
  public String remove(Object key) {
    return map.remove(key);
  }
  
  @Override
  public void putAll(Map<? extends String, ? extends String> m) {
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
  public Collection<String> values() {
    return map.values();
  }
  
  @Override
  public Set<Entry<String,String>> entrySet() {
    return map.entrySet();
  }
  
}
