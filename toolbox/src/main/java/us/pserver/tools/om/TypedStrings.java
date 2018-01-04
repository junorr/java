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

package us.pserver.tools.om;

import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import us.pserver.tools.ForEach;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/01/2018
 */
public class TypedStrings {

  public static final String BOOLEAN_PATTERN = "(?i)(true|false)";
  
  public static final String CLASS_PATTERN = "\\w+\\..*\\.\\w+";
  
  public static final String DOUBLE_PATTERN = "(-|\\+)?\\d+\\.?\\d*";
  
  public static final String INSTANT_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?Z";
  
  public static final String IPV4_PATTERN = "(\\d+|\\*{1})\\.(\\d+|\\*{1})\\.(\\d+|\\*{1})\\.(\\d+|\\*{1})";
  
  public static final String LOCAL_DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
  
  public static final String LOCAL_DATE_TIME_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?";
  
  public static final String LOCAL_TIME_PATTERN = "\\d{2}:\\d{2}(:\\d{2})?(\\.\\d+)?";
  
  public static final String LONG_PATTERN = "(-|\\+)?\\d+";
  
  public static final String OFFSET_TIME_PATTERN = "\\d{2}:\\d{2}(:\\d{2})?(\\.\\d+)?(-|\\+)\\d{2}:\\d{2}";
  
  public static final String ZONED_DATE_TIME_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(-|\\+)\\d{2}:\\d{2}(\\[[\\w|\\/|-]+\\])?";
  
  
  private final List<TypedString<?>> types;
  
  
  public TypedStrings(ClassLoader ldr) {
    this.types = new ArrayList<>();
    types.add(new BooleanString());
    types.add(new ByteString());
    types.add(new CharacterString());
    types.add(new ClassString(ldr != null ? ldr : ClassLoader.getSystemClassLoader()));
    types.add(new DoubleString());
    types.add(new FloatString());
    types.add(new InetAddressString());
    types.add(new InstantString());
    types.add(new IntegerString());
    types.add(new LocalDateString());
    types.add(new LocalDateTimeString());
    types.add(new LocalTimeString());
    types.add(new LongString());
    types.add(new OffsetTimeString());
    types.add(new PathString());
    types.add(new ShortString());
    types.add(new StringString());
    types.add(new ZonedDateTimeString());
  }
  
  public TypedStrings() {
    this(null);
  }
  
  public List<TypedString<?>> getTypesList() {
    return types;
  }
  
  public boolean isTypedAvailable(Class cls) {
    return types.stream().anyMatch(t->t.isTypeOf(cls));
  }
  
  public <T> Optional<TypedString<T>> getTypedString(Class<T> cls) {
    if(cls == null) return Optional.empty();
    return types.stream()
        .filter(t->t.isTypeOf(cls))
        .map(t->(TypedString<T>)t)
        .findAny();
  }
  
  public <T> T asType(String str, Class<T> cls) throws TypedStringException {
    Optional<TypedString<T>> opt = this.getTypedString(cls);
    if(opt.isPresent()) {
      return opt.get().apply(str);
    }
    throw new TypedStringException("Unsupported type "+ cls);
  }
  
  public TypedStrings put(Class cls, TypedString<?> typed) {
    int idx = ForEach.of(types).indexOf(t->t.isTypeOf(cls));
    if(idx >= 0) types.remove(idx);
    types.add(typed);
    return this;
  }
  
  public Class<?> guessTypeFromPattern(String str) {
    Class<?> cls = String.class;
    if(str.matches(BOOLEAN_PATTERN)) {
      cls = Boolean.class;
    }
    else if(str.matches(CLASS_PATTERN)) {
      cls = Class.class;
    }
    else if(str.matches(DOUBLE_PATTERN)) {
      cls = Double.class;
    }
    else if(str.matches(INSTANT_PATTERN)) {
      cls = Instant.class;
    }
    else if(str.matches(IPV4_PATTERN)) {
      cls = InetAddress.class;
    }
    else if(str.matches(LOCAL_DATE_PATTERN)) {
      cls = LocalDate.class;
    }
    else if(str.matches(LOCAL_DATE_TIME_PATTERN)) {
      cls = LocalDateTime.class;
    }
    else if(str.matches(LOCAL_TIME_PATTERN)) {
      cls = LocalTime.class;
    }
    else if(str.matches(LONG_PATTERN)) {
      cls = Long.class;
    }
    else if(str.matches(OFFSET_TIME_PATTERN)) {
      cls = OffsetTime.class;
    }
    else if(str.matches(ZONED_DATE_TIME_PATTERN)) {
      cls = ZonedDateTime.class;
    }
    return cls;
  }
  
}
