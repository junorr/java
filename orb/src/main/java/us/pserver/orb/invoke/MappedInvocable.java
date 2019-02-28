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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import us.pserver.orb.OrbConfiguration;
import us.pserver.orb.OrbSource;
import us.pserver.orb.annotation.Annotations;
import us.pserver.orb.annotation.DefaultValue;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/12/2017
 */
public class MappedInvocable implements InvocationHandler {
  
  private final List<String> prefix;
  
  private final Map<String,String> map;
  
  private final OrbConfiguration config;
  
  private final Set<MethodTransform> trans;
  
  
  
  public MappedInvocable(List<String> prefix, Map<String,String> map, OrbConfiguration config) {
    this.prefix = new ArrayList<>(prefix);
    this.map = Match.notNull(map).getOrFail("Bad null StringMap");
    this.config = Match.notNull(config).getOrFail("Bad null OrbConfiguration");
    this.trans = new HashSet<>(config.getMethodTransforms());
    this.initTransforms();
  }
  
  private void initTransforms() {
    Predicate<Method> prd = m->"hashCode".equals(m.getName()) && m.getParameterCount() == 0;
    trans.add(new InterceptSupplierTransform(prd, map::hashCode));
    prd = m->"equals".equals(m.getName()) && m.getParameterCount() == 1;
    trans.add(new InterceptFunctionTransform(prd, map::equals));
    prd = m->"toString".equals(m.getName()) && m.getParameterCount() == 0;
    trans.add(new InterceptSupplierTransform(prd, this::toString));
    trans.add(new MappedObjectTransform(config));
  }
  
  public Set<MethodTransform> getMethodTransforms() {
    return Collections.unmodifiableSet(trans);
  }
  
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Optional<String> key = map.keySet().stream()
        .filter(k -> config.getOrbSources().stream()
            .sorted(Collections.reverseOrder(OrbSource::compareTo))
            .map(OrbSource::methodBind)
            .anyMatch(b -> b.apply(prefix, method).equals(k)))
        .findFirst();
    Object value = key.isPresent() ? map.get(key.get()) : null;
    if(value == null) {
      Optional<String> defval = Annotations.getAnnotationValue(DefaultValue.class, method);
      if(defval.isPresent()) value = defval.get();
    }
    List list = args != null ? Arrays.asList(args) : Collections.EMPTY_LIST;
    return transform(InvocationContext.of(proxy, method, list, value));
  }
  
  private Object transform(InvocationContext ctx) {
    Object value = ctx.getValue();
    Iterator<MethodTransform> it = trans.iterator();
    while(it.hasNext()) {
      MethodTransform t = it.next();
      if(t.canHandle(ctx)) {
        value = t.apply(ctx);
        ctx = ctx.withValue(value);
      }
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
