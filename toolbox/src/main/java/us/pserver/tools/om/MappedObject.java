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
public class MappedObject implements InvocationHandler {
  
  private final Map<String,Object> map;
  
  private final Function<Method,String> methodToKey;
  
  private final TypedStrings typeds;
  
  
  
  public MappedObject(Map<String,Object> map, TypedStrings typeds, Function<Method,String> methodToKey) {
    this.map = Match.notNull(map).getOrFail("Bad null StringMap");
    this.typeds = Match.notNull(typeds).getOrFail("Bad null TypedStrings");
    this.methodToKey = Match.notNull(methodToKey).getOrFail("Bad null method name Function");
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
  
}
