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

package us.pserver.dbone;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import us.pserver.dbone.serial.GsonSerializationService;
import us.pserver.tools.Hash;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/11/2017
 */
public class ObjectUIDFactory {
  
  public static final List<Class> PRIMITIVE_TYPES = Arrays.asList(
      byte.class,
      char.class,
      short.class,
      int.class,
      long.class,
      float.class,
      double.class,
      boolean.class,
      Class.class,
      Number.class,
      Character.class,
      Boolean.class,
      String.class,
      StringBuffer.class,
      StringBuilder.class,
      Charset.class,
      Date.class,
      java.sql.Date.class,
      java.sql.Time.class,
      java.sql.Timestamp.class,
      Temporal.class,
      TemporalAmount.class,
      Calendar.class,
      TimeZone.class,
      Enum.class,
      Path.class,
      Currency.class,
      Locale.class
  );
  
  
  private static final GsonSerializationService serial = new GsonSerializationService();
  
  
  public static String create(Object obj) {
    byte[] bs = serial.serialize(obj);
    System.out.printf("* OUIDFactory.create.gson.bytes: %s%n", Arrays.toString(bs));
    System.out.printf("* OUIDFactory.create.gson.equals: %s%n", arrayString.equals(Arrays.toString(bs)));
    String str = UTF8String.from(bs).toString();
    System.out.printf("* OUIDFactory.create.gson: %s%n", str);
    return Hash.sha1().of(str);
    /*
    Class cls = obj.getClass();
    Field[] fls = cls.getDeclaredFields();
    Hash hash = Hash.sha1();
    hash.put(cls.getName());
    Arrays.asList(fls).stream()
        .filter(ObjectUIDFactory::take)
        .forEach(f->calcHash(obj, f, hash));
    return hash.get();
    */
  }
  
  
  private static void calcHash(Object o, Field f, Hash h) {
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    h.put(get(f, o));
  }
  
  
  private static boolean take(Field f) {
    return !Modifier.isStatic(f.getModifiers()) 
        && !Modifier.isTransient(f.getModifiers());
  }
  
  
  private static String get(Field f, Object owner) {
    try {
      return toString(f.get(owner));
    } catch(IllegalAccessException | IllegalArgumentException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  private static String toString(Object obj) {
    if(PRIMITIVE_TYPES.stream().anyMatch(c->c.isAssignableFrom(obj.getClass()))) {
      return Objects.toString(obj);
    }
    else if(obj.getClass().isArray()) {
      return arrayToString(obj);
    }
    else if(Collection.class.isAssignableFrom(obj.getClass())) {
      return collectionToString((Collection)obj);
    }
    else if(Map.class.isAssignableFrom(obj.getClass())) {
      return mapToString((Map)obj);
    }
    else {
      return create(obj);
    }
  }
  
  
  private static String arrayToString(Object array) {
    int len = Array.getLength(array);
    StringBuilder sb = new StringBuilder("[");
    for(int i = 0; i < len; i++) {
      sb.append(toString(Array.get(array, i))).append(",");
    }
    sb.deleteCharAt(sb.length() -1);
    return sb.append("]").toString();
  }
  
  
  private static String collectionToString(Collection list) {
    StringBuilder sb = new StringBuilder("[");
    for(Object o : list) {
      sb.append(toString(o)).append(",");
    }
    sb.deleteCharAt(sb.length() -1);
    return sb.append("]").toString();
  }

  
  private static String mapToString(Map map) {
    StringBuilder sb = new StringBuilder("[");
    Set<Entry> ets = map.entrySet();
    for(Entry e : ets) {
      sb.append(toString(e.getKey())).append("=").append(toString(e.getValue())).append(",");
    }
    sb.deleteCharAt(sb.length() -1);
    return sb.append("]").toString();
  }
  
}
