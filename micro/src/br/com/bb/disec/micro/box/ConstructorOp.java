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

package br.com.bb.disec.micro.box;

import java.util.List;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/07/2017
 */
public class ConstructorOp extends MethodOp<Class> {
  
  public ConstructorOp(Operation<?> next, List<Class> types, List args) {
    super("constructor", next, types, args);
  }
  
  public ConstructorOp(List<Class> types, List args) {
    this(null, types, args);
  }
  
  @Override
  public Operation execute(Class cls) {
    if(cls == null) {
      throw new IllegalArgumentException("Bad null argument");
    }
    lock.lock();
    try {
      Reflector ref = new Reflector(cls);
      Object ret = null;
      if(argtypes.isEmpty()) {
        ref.selectConstructor();
        ret = ref.create();
      }
      else {
        ref.selectConstructor(argtypes.toArray(
            new Class[argtypes.size()])
        );
        ret = ref.create(args.toArray());
      }
      result = (ret == null ? OpResult.successful() : OpResult.of(ret));
    }
    catch(Throwable th) {
      result = OpResult.of(th);
    }
    finally {
      lock.unlock();
    }
    return this;
  }

}
