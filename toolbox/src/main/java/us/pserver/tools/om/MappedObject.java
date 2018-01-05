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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import us.pserver.tools.NotMatch;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 */
public class MappedObject implements InvocationHandler {
  
  public static final Function<Method,String> GETTER_AS_DOTTED_KEY = new KeyMethodName('.', 3);
  
  public static final Function<Method,String> GETTER_AS_KEY = new KeyMethodName((char)0, 3);
  
  public static final Function<Method,String> GETTER_AS_UNDERSCORED_KEY = new KeyMethodName('_', 3);
  
  public static final Function<Method,String> GETTER_AS_ENVIRONMENT_KEY = m->GETTER_AS_UNDERSCORED_KEY.apply(m).toUpperCase();
  
  public static final Function<Method,String> NAME_AS_DOTTED_KEY = new KeyMethodName('.', 0);
  
  public static final Function<Method,String> NAME_AS_KEY = new KeyMethodName((char)0, 0);
  
  public static final Function<Method,String> NAME_AS_UNDERSCORED_KEY = new KeyMethodName('_', 0);
  
  public static final Function<Method,String> NAME_AS_ENVIRONMENT_KEY = m->NAME_AS_UNDERSCORED_KEY.apply(m).toUpperCase();
  
  
  private final Map<String,Object> map;
  
  private final Function<Method,String> methodToKey;
  
  private final TypedStrings typeds;
  
  
  
  public MappedObject(Map<String,Object> map, TypedStrings typeds, Function<Method,String> methodToKey) {
    this.map = NotMatch.notNull(map).getOrFail("Bad null StringMap");
    this.typeds = NotMatch.notNull(typeds).getOrFail("Bad null TypedStrings");
    this.methodToKey = NotMatch.notNull(methodToKey).getOrFail("Bad null method name Function");
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if(isToString(method)) return toString();
    String key = methodToKey.apply(method);
    return args == null || args.length == 0
        ? getter(key, proxy, method)
        : setter(key, proxy, method, args);
  }
  
  private boolean isToString(Method meth) {
    return "toString".equals(meth.getName()) && meth.getParameterCount() == 0;
  }
  
  public Object getter(String key, Object proxy, Method method) throws Throwable {
    Object val = map.get(key);
    if(val != null
        && (CharSequence.class.isAssignableFrom(val.getClass()) 
        || !method.getReturnType().isAssignableFrom(val.getClass()))) {
      val = typeds.asType(Objects.toString(val), method.getReturnType());
    }
    return val;
  }
  
  public Object setter(String key, Object proxy, Method method, Object[] args) throws Throwable {
    Object val = args.length == 1 ? args[0] : args;
    map.put(key, val);
    return method.getReturnType()
        .isAssignableFrom(proxy.getClass()) ? proxy : null;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    map.entrySet().forEach(e->sb.append(e).append("\n"));
    return sb.toString();
    //return map.toString();
  }
  
  
  public static <T> Builder<T> builder(Class<T> cls, Map<String,Object> map) {
    return new Builder(cls, map);
  }
  
  
  public static <T> T fromEnvironment(Class<T> cls) {
    return new Builder<>(cls)
        .withMap(new HashMap<>(System.getenv()))
        .withMethodKeyFunction(GETTER_AS_ENVIRONMENT_KEY)
        .build();
  }
  
  
  public static <T> T fromProperties(Class<T> cls, Path propsFile) throws IOException {
    Properties props = new Properties();
    props.load(Files.newInputStream(propsFile, StandardOpenOption.READ));
    return fromProperties(cls, props);
  }
  
  
  public static <T> T fromProperties(Class<T> cls, Properties props) {
    HashMap<String,Object> map = new HashMap<>();
    props.entrySet().forEach(e->map.put(e.getKey().toString(), e.getValue()));
    return new Builder<T>(cls, map).build();
  }
  
  
  
  
  
  public static class Builder<T> {
    
    private final Map<String,Object> map;
    
    private final TypedStrings typeds;
    
    private final Function<Method,String> methodKey;
    
    private final Class<T> type;
    
    public Builder(Class<T> type) {
      this(type, null, new TypedStrings(type.getClassLoader()), GETTER_AS_DOTTED_KEY);
    }
    
    public Builder(Class type, Map<String,Object> map) {
      this(type, map, new TypedStrings(type.getClassLoader()), GETTER_AS_DOTTED_KEY);
    }
    
    public Builder(Class type, Map<String,Object> map, TypedStrings typeds, Function<Method,String> methodKey) {
      this.type = type;
      this.map = map;
      this.typeds = typeds;
      this.methodKey = methodKey;
    }
    
    public Map<String,Object> getMap() {
      return map;
    }
    
    public TypedStrings getTypedStrings() {
      return typeds;
    }
    
    public Function<Method,String> getMethodKeyFunction() {
      return methodKey;
    }
    
    public Builder<T> withMap(Map<String,Object> map) {
      return new Builder(type, map, typeds, methodKey);
    }
    
    public Builder<T> withTypedStrings(TypedStrings typeds) {
      return new Builder(type, map, typeds, methodKey);
    }
    
    public Builder<T> withMethodKeyFunction(Function<Method,String> methodKey) {
      return new Builder(type, map, typeds, methodKey);
    }
    
    public T build() {
      MappedObject mo = new MappedObject(map, typeds, methodKey);
      return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, mo);
    }
    
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
      for(int i = 1; i < cs.length; i++) {
        if(Character.isUpperCase(cs[i]) && separator != 0) {
          sb.append(separator);
        }
        sb.append(Character.toLowerCase(cs[i]));
      }
      return sb.toString();
    }
    
  }

}
