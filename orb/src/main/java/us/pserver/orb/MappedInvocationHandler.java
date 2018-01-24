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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 */
public class MappedInvocationHandler implements InvocationHandler {
  
  private final Map<String,Object> map;
  
  private final Function<Method,String> methodToKey;
  
  private final TypedStrings types;
  
  
  
  public MappedInvocationHandler(Map<String,Object> map, TypedStrings typeds, Function<Method,String> methodToKey) {
    this.map = Match.notNull(map).getOrFail("Bad null StringMap");
    this.types = Match.notNull(typeds).getOrFail("Bad null TypedStrings");
    this.methodToKey = Match.notNull(methodToKey).getOrFail("Bad null method name Function");
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if(isToString(method)) return toString();
    String key = methodToKey.apply(method);
    Object ret = args == null || args.length == 0
        ? getter(key, proxy, method)
        : setter(key, proxy, method, args);
    //System.out.printf("* MappedInvocationHandler.invoke( %s ): '%s' = %s%n", method.getName(), key, ret);
    return ret;
  }
  
  private boolean isToString(Method meth) {
    return "toString".equals(meth.getName()) && meth.getParameterCount() == 0;
  }
  
  public Object getter(String key, Object proxy, Method method) throws Throwable {
    Object val = map.get(key);
    //System.out.printf("* MappedInvocationHandler.getter[1]( %s => %s ): %s%n", method.getName(), key, val);
    //System.out.println(map);
    return getReturnValue(method.getReturnType(), val, proxy);
  }
  
  public Object setter(String key, Object proxy, Method method, Object[] args) throws Throwable {
    Object val = args.length == 1 ? args[0] : args;
    map.put(key, val);
    return getReturnValue(method.getReturnType(), val, proxy);
  }
  
  private Object getReturnValue(Class rtype, Object val, Object proxy) {
    if(val == null) return val;
    Object rval = val;
    if(rtype.isInterface() && Map.class.isAssignableFrom(val.getClass())) {
      rval = valueAsMappedObject(val, rtype);
    }
    else if(rtype.isAssignableFrom(proxy.getClass())) {
      rval = proxy;
    }
    else if(CharSequence.class.isAssignableFrom(val.getClass()) 
        || !rtype.isAssignableFrom(val.getClass())) {
      rval = types.asType(Objects.toString(val), rtype);
    }
    return rval;
  }
  
  private Object valueAsMappedObject(Object val, Class rtype) {
    return Orb.get()
        .withMap((Map)val)
        .withMethodToKeyFunction(methodToKey)
        .withTypedStrings(types)
        .create(rtype);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("MappedObject {\n");
    map.entrySet().forEach(e->sb.append(" -> ").append(e).append("\n"));
    return sb.append("}").toString();
    //return map.toString();
  }
  
}
