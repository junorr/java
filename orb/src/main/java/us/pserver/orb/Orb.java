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

import us.pserver.orb.invoke.MappedInvocable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.function.Function;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/01/2018
 */
public class Orb {

  public static final Function<Method,String> GETTER_AS_DOTTED_KEY = new MethodNameTransform('.', 3);
  
  public static final Function<Method,String> GETTER_AS_DASH_KEY = new MethodNameTransform('-', 3);
  
  public static final Function<Method,String> GETTER_AS_KEY = new MethodNameTransform((char)0, 3);
  
  public static final Function<Method,String> GETTER_AS_UNDERSCORED_KEY = new MethodNameTransform('_', 3);
  
  public static final Function<Method,String> GETTER_AS_ENVIRONMENT_KEY = m->GETTER_AS_UNDERSCORED_KEY.apply(m).toUpperCase();
  
  public static final Function<Method,String> NAME_AS_DOTTED_KEY = new MethodNameTransform('.', 0);
  
  public static final Function<Method,String> NAME_AS_DASH_KEY = new MethodNameTransform('-', 0);
  
  public static final Function<Method,String> NAME_AS_KEY = new MethodNameTransform((char)0, 0);
  
  public static final Function<Method,String> NAME_AS_UNDERSCORED_KEY = new MethodNameTransform('_', 0);
  
  public static final Function<Method,String> NAME_AS_ENVIRONMENT_KEY = m->NAME_AS_UNDERSCORED_KEY.apply(m).toUpperCase();
  
  
  private final TypedStrings types;
  
  private final Map<String,Object> map;
  
  private final Function<Method,String> methodToKey;
  
  
  
  public Orb(Map<String,Object> map, TypedStrings types, Function<Method,String> methodToKey) {
    this.map = Match.notNull(map).getOrFail("Bad null Map");
    this.types = Match.notNull(types)
        .and(t->!t.getTypesList().isEmpty())
        .getOrFail("Bad empty types list");
    this.methodToKey = Match.notNull(methodToKey)
        .getOrFail("Bad null methodToKey Function");
  }
  
  
  public static Orb get() {
    return new Orb(new TreeMap<>(), new TypedStrings(), GETTER_AS_DOTTED_KEY);
  }
  
  
  public Orb withMap(Map<String,Object> map) {
    return new Orb(map, types, methodToKey);
  }
  
  
  public Orb withTypedStrings(TypedStrings types) {
    return new Orb(map, types, methodToKey);
  }
  
  
  public Orb withMethodToKeyFunction(Function<Method,String> methodToKey) {
    return new Orb(map, types, methodToKey);
  }
  
  
  public MappedInvocable invocationHandler() {
    return new MappedInvocable(map, types, methodToKey);
  }
  
  public <T> T create(Class<T> cls) {
    Match.notNull(cls).failIfNotMatch("Bad null proxied Class");
    return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, invocationHandler());
  }
  
  public Object create(Class ... cls) {
    Match.notEmpty(cls).failIfNotMatch("Bad null proxied Class array");
    return Proxy.newProxyInstance(cls[0].getClassLoader(), cls, invocationHandler());
  }
  
  
  public Orb fromEnvironment() {
    Map<String,Object> map = new TreeMap<>();
    System.getenv().entrySet().forEach(e->map.put(e.getKey(), e.getValue()));
    return this.withMap(map)
        .withMethodToKeyFunction(GETTER_AS_ENVIRONMENT_KEY);
  }
  
  
  public Orb fromConfiguration(OrbConfiguration sm) {
    Map<String,Object> map = (sm.getValuesMap() != null 
        ? sm.getValuesMap() : this.map);
    TypedStrings types = (sm.getSupportedTypes() != null 
        ? sm.getSupportedTypes() : this.types);
    Function<Method,String> func = (sm.getMethodToKeyFunction() != null 
        ? sm.getMethodToKeyFunction() 
        : this.methodToKey
    );
    return new Orb(map, types, func);
  }
  
  
  public Orb fromProperties(Properties props) {
    Map<String,Object> map = new TreeMap<>();
    props.entrySet().forEach(e->map.put(e.getKey().toString(), e.getValue()));
    return  this.withMap(map);
  }
  
  
  public Orb fromProperties(Path propsFile) throws IOException {
    Match.exists(propsFile).failIfNotMatch();
    Properties props = new Properties();
    props.load(Files.newBufferedReader(propsFile));
    Map<String,Object> map = new TreeMap<>();
    props.entrySet().forEach(e->map.put(e.getKey().toString(), e.getValue()));
    return this.withMap(map);
  }
  
  
  
  
  
  public static class MethodNameTransform implements Function<Method,String> {
    
    private final char separator;
    
    private final int startAt;
    
    public MethodNameTransform(char camelCaseSeparator, int startNameAt) {
      this.separator = camelCaseSeparator;
      this.startAt = startNameAt;
    }
    
    @Override
    public String apply(Method m) {
      String name = m.getName().substring(startAt);
      StringBuilder sb = new StringBuilder(name.substring(0, 1).toLowerCase());
      char[] cs = name.toCharArray();
      boolean lastLowerCase = false;
      for(int i = 1; i < cs.length; i++) {
        if(Character.isUpperCase(cs[i]) && separator != 0 && lastLowerCase) {
          sb.append(separator);
        }
        sb.append(Character.toLowerCase(cs[i]));
        lastLowerCase = Character.isLowerCase(cs[i]);
      }
      return sb.toString();
    }
    
  }

}
