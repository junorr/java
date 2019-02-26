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

package us.pserver.orb.invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import us.pserver.orb.TypeStrings;
import us.pserver.orb.bind.MethodBind;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 */
public class MappedInvocable implements InvocationHandler {
  
  private final Map<String,String> map;
  
  private final MethodBind bind;
  
  private final TypeStrings types;
  
  private final Set<MethodTransform> trans;
  
  
  
  public MappedInvocable(Map<String,String> map, TypeStrings types, MethodBind bind, Collection<MethodTransform> transforms) {
    this.map = Match.notNull(map).getOrFail("Bad null StringMap");
    this.types = Match.notNull(types).getOrFail("Bad null TypedStrings");
    this.bind = Match.notNull(bind).getOrFail("Bad null MethodBind");
    this.trans = new HashSet<>(transforms);
    this.initTransforms();
  }
  
  public MappedInvocable(Map<String,String> map, TypeStrings types, MethodBind bind) {
    this(map, types, bind, Collections.EMPTY_SET);
  }
  
  private void initTransforms() {
    Predicate<Method> prd = m->"hashCode".equals(m.getName()) && m.getParameterCount() == 0;
    trans.add(new InterceptSupplierTransform(prd, map::hashCode));
    prd = m->"equals".equals(m.getName()) && m.getParameterCount() == 1;
    trans.add(new InterceptFunctionTransform(prd, map::equals));
    prd = m->"toString".equals(m.getName()) && m.getParameterCount() == 0;
    trans.add(new InterceptSupplierTransform(prd, this::toString));
    trans.add(new DefaultMethodTransform());
    trans.add(new MappedObjectTransform(types, bind));
    trans.add(new ProxyReturnTransform());
    trans.add(new EnumStringTransform());
    trans.add(new TypeStringTransform(types));
  }
  
  public Set<MethodTransform> getMethodTransforms() {
    return Collections.unmodifiableSet(trans);
  }
  
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String key = bind.apply(method);
    String value = map.get(key);
    List list = args != null ? Arrays.asList(args) : Collections.EMPTY_LIST;
    InvocationContext ctx = InvocationContext.of(proxy, method, list, value);
    if(!list.isEmpty()) {
      map.put(key, list.size() == 1 ? list.get(0) : list);
    }
    Optional<MethodTransform> opt = trans.stream().filter(m->m.canHandle(ctx)).findFirst();
    if(opt.isPresent()) {
      return opt.get().apply(ctx);
    }
    return value;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("{\n");
    map.entrySet().forEach(e->sb.append(" -> ").append(e).append("\n"));
    return sb.append("}").toString();
  }
  
}
