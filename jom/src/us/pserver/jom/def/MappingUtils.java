/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.jom.def;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import us.pserver.jom.MappedValue;
import static us.pserver.jom.MappedValue.Type.ARRAY;
import static us.pserver.jom.MappedValue.Type.BOOLEAN;
import static us.pserver.jom.MappedValue.Type.MAP;
import static us.pserver.jom.MappedValue.Type.NUMBER;
import static us.pserver.jom.MappedValue.Type.STRING;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/08/2017
 */
public abstract class MappingUtils {

  public static final String KEY_TYPE = "@type";
  
  public static final String KEY_VALUE = "@value";
  
  public static final List<Predicate<Class>> NATIVE_SUPPORT = createNatives();
  
  
  private static List<Predicate<Class>> createNatives() {
    ObjectMapper root = new ObjectMapper();
    List<Predicate<Class>> maps = new ArrayList<>();
    maps.add(c->CharSequence.class.isAssignableFrom(c) 
        || Character.class.isAssignableFrom(c) 
        || char.class == c || char[].class == c
    );
    maps.add(c->Number.class.isAssignableFrom(c) 
        || byte.class == c
        || short.class == c
        || int.class == c
        || long.class == c
        || float.class == c
        || double.class == c
    );
    maps.add(c->Boolean.class.isAssignableFrom(c) || boolean.class == c);
    maps.add(Date.class::isAssignableFrom);
    maps.add(Instant.class::isAssignableFrom);
    maps.add(LocalDateTime.class::isAssignableFrom);
    maps.add(ZonedDateTime.class::isAssignableFrom);
    maps.add(byte[].class::isAssignableFrom);
    maps.add(Class.class::isAssignableFrom);
    maps.add(ByteBuffer.class::isAssignableFrom);
    maps.add(List.class::isAssignableFrom);
    maps.add(Set.class::isAssignableFrom);
    maps.add(Map.class::isAssignableFrom);
    maps.add(MappedValue.class::isAssignableFrom);
    maps.add(Class::isArray);
    return maps;
  }
  
  
  public static boolean isNativeSupported(Class cls) {
    return cls != null && NATIVE_SUPPORT.stream()
        .anyMatch(p->p.test(cls));
  }
  
  public static Class getMapperType(MappedValue.Type type) {
    switch(type) {
      case ARRAY:
        return Object[].class;
      case BOOLEAN:
        return Boolean.class;
      case MAP:
        return SerializableHashMap.class;
      case NUMBER:
        return Number.class;
      case STRING:
        return String.class;
      default:
        throw new UnsupportedOperationException("Unknown Type: "+ type);
    }
  }
  
}
