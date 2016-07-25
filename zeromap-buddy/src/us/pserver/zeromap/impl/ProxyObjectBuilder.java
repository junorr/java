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

package us.pserver.zeromap.impl;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.NegatingMatcher;
import us.pserver.zeromap.MapperFactory;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.ObjectBuilder;
import us.pserver.zeromap.proxy.ObjectProxy;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/03/2016
 */
public class ProxyObjectBuilder<T> implements ObjectBuilder<T> {
  
  private final MapperFactory factory;
  
  
  public ProxyObjectBuilder() {
    this(null);
  }
  
  
  public ProxyObjectBuilder(MapperFactory mfac) {
    this.factory = (mfac != null ? mfac : MapperFactory.factory());
  }
  

  @Override
  public T create(Class<T> cls, Node nod) throws ReflectiveOperationException {
    try {
      Class<T> runtype = new ByteBuddy()
          .subclass(cls)
          .method(new NegatingMatcher(ElementMatchers.isStatic()))
          .intercept(MethodDelegation.to(new ObjectProxy(cls, nod, factory)))
          .make()
          .load(Node.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
          .getLoaded();
      return this.factory.mapper(runtype).unmap(nod, runtype);
    }
    catch(Exception e) {
      throw new ReflectiveOperationException(e);
    }
  }

}
