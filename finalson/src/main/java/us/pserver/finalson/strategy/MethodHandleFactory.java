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

package us.pserver.finalson.strategy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import us.pserver.tools.Match;
import us.pserver.tools.function.Rethrow;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/02/2018
 */
public interface MethodHandleFactory extends Supplier<MethodHandle> {
  
  public static MethodHandleFactory of(Constructor cct) {
    return new ConstructorFactory(cct);
  }
  
  public static MethodHandleFactory of(Method meth) {
    return new MethodFactory(meth);
  }
  
  

  static final class ConstructorFactory implements MethodHandleFactory {
    
    private final Constructor cct;
    
    public ConstructorFactory(Constructor cct) {
      this.cct = Match.notNull(cct).getOrFail("Bad null Constructor");
    }
    
    @Override
    public MethodHandle get() {
      return Rethrow.illegalState().apply(()->
          MethodHandles.publicLookup().unreflectConstructor(cct));
    }

  }
  
  
  
  static final class MethodFactory implements MethodHandleFactory {
    
    private final Method meth;
    
    public MethodFactory(Method meth) {
      this.meth = Match.notNull(meth).getOrFail("Bad null Method");
    }
    
    @Override
    public MethodHandle get() {
      return Rethrow.illegalState().apply(()->
          MethodHandles.publicLookup().unreflect(meth));
    }

  }
  
}
