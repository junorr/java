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
public class MappedObjectFactory {

  public static final Function<Method,String> GETTER_AS_DOTTED_KEY = new KeyMethodName('.', 3);
  
  public static final Function<Method,String> GETTER_AS_KEY = new KeyMethodName((char)0, 3);
  
  public static final Function<Method,String> GETTER_AS_UNDERSCORED_KEY = new KeyMethodName('_', 3);
  
  public static final Function<Method,String> GETTER_AS_ENVIRONMENT_KEY = m->GETTER_AS_UNDERSCORED_KEY.apply(m).toUpperCase();
  
  public static final Function<Method,String> NAME_AS_DOTTED_KEY = new KeyMethodName('.', 0);
  
  public static final Function<Method,String> NAME_AS_KEY = new KeyMethodName((char)0, 0);
  
  public static final Function<Method,String> NAME_AS_UNDERSCORED_KEY = new KeyMethodName('_', 0);
  
  public static final Function<Method,String> NAME_AS_ENVIRONMENT_KEY = m->NAME_AS_UNDERSCORED_KEY.apply(m).toUpperCase();
  
  
  private final TypedStrings types;
  
  private final Map<String,Object> map;
  
  private final Function<Method,String> methodToKey;
  
  
  
  public MappedObjectFactory(Map<String,Object> map, TypedStrings types, Function<Method,String> methodToKey) {
    this.map = Match.notNull(map).getOrFail("Bad null Map");
    this.types = Match.notNull(types)
        .and(t->!t.getTypesList().isEmpty())
        .getOrFail("Bad empty types list");
    this.methodToKey = Match.notNull(methodToKey)
        .getOrFail("Bad null methodToKey Function");
  }
  
  
  public static MappedObjectFactory factory() {
    return new MappedObjectFactory(new TreeMap<>(), new TypedStrings(), GETTER_AS_DOTTED_KEY);
  }
  
  
  public MappedObjectFactory withMap(Map<String,Object> map) {
    return new MappedObjectFactory(map, types, methodToKey);
  }
  
  
  public MappedObjectFactory withTypedStrings(TypedStrings types) {
    return new MappedObjectFactory(map, types, methodToKey);
  }
  
  
  public MappedObjectFactory withMethodToKeyFunction(Function<Method,String> methodToKey) {
    return new MappedObjectFactory(map, types, methodToKey);
  }
  
  
  public MappedObject create() {
    return new MappedObject(map, types, methodToKey);
  }
  
  public <T> T newInstance(Class<T> cls) {
    Match.notNull(cls).failIfNotMatch("Bad null proxied Class");
    return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, create());
  }
  
  public Object newInstance(Class ... cls) {
    Match.notEmpty(cls).failIfNotMatch("Bad null proxied Class array");
    return Proxy.newProxyInstance(cls[0].getClassLoader(), cls, create());
  }
  
  
  public <T> T fromEnvironment(Class<T> cls) {
    Match.notNull(cls).failIfNotMatch("Bad null proxied Class");
    Map<String,Object> map = new TreeMap<>();
    System.getenv().entrySet().forEach(e->map.put(e.getKey(), e.getValue()));
    MappedObject mapped = this.withMap(map)
        .withMethodToKeyFunction(GETTER_AS_ENVIRONMENT_KEY)
        .create();
    return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, mapped);
  }
  
  
  public Object fromEnvironment(Class ... cls) {
    Match.notEmpty(cls).failIfNotMatch("Bad null proxied Class array");
    Map<String,Object> map = new TreeMap<>();
    System.getenv().entrySet().forEach(e->map.put(e.getKey(), e.getValue()));
    MappedObject mapped = this.withMap(map)
        .withMethodToKeyFunction(GETTER_AS_ENVIRONMENT_KEY)
        .create();
    return Proxy.newProxyInstance(cls[0].getClassLoader(), cls, mapped);
  }
  
  
  public <T> T fromProperties(Properties props, Class<T> cls) {
    Match.notNull(cls).failIfNotMatch("Bad null proxied Class");
    Map<String,Object> map = new TreeMap<>();
    props.entrySet().forEach(e->map.put(e.getKey().toString(), e.getValue()));
    return (T) Proxy.newProxyInstance(cls.getClassLoader(), 
        new Class[]{cls}, this.withMap(map).create()
    );
  }
  
  
  public Object fromProperties(Properties props, Class ... cls) {
    Match.notEmpty(cls).failIfNotMatch("Bad null proxied Class array");
    Map<String,Object> map = new TreeMap<>();
    props.entrySet().forEach(e->map.put(e.getKey().toString(), e.getValue()));
    return Proxy.newProxyInstance(cls[0].getClassLoader(), cls, 
        this.withMap(map).create()
    );
  }
  
  
  public <T> T fromProperties(Path propsFile, Class<T> cls) throws IOException {
    Match.notNull(cls).failIfNotMatch("Bad null proxied Class");
    Match.exists(propsFile).failIfNotMatch();
    Properties props = new Properties();
    props.load(Files.newBufferedReader(propsFile));
    Map<String,Object> map = new TreeMap<>();
    props.entrySet().forEach(e->map.put(e.getKey().toString(), e.getValue()));
    return (T) Proxy.newProxyInstance(cls.getClassLoader(), 
        new Class[]{cls}, this.withMap(map).create()
    );
  }
  
  
  public Object fromProperties(Path propsFile, Class ... cls) throws IOException {
    Match.notEmpty(cls).failIfNotMatch("Bad null proxied Class array");
    Match.exists(propsFile).failIfNotMatch();
    Properties props = new Properties();
    props.load(Files.newBufferedReader(propsFile));
    Map<String,Object> map = new TreeMap<>();
    props.entrySet().forEach(e->map.put(e.getKey().toString(), e.getValue()));
    return Proxy.newProxyInstance(cls[0].getClassLoader(), cls, 
        this.withMap(map).create()
    );
  }
  
  
  
  
  
  public static class KeyMethodName implements Function<Method,String> {
    
    private final char separator;
    
    private final int startAt;
    
    public KeyMethodName(char camelCaseSeparator, int startNameAt) {
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
