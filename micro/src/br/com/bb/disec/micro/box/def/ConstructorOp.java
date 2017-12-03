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

package br.com.bb.disec.micro.box.def;

import br.com.bb.disec.micro.box.OpResult;
import br.com.bb.disec.micro.box.Operation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import us.pserver.tools.rfl.Reflector;
import us.pserver.tools.rfl.ReflectorException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/07/2017
 */
public class ConstructorOp extends MethodOp {
  
  public ConstructorOp(Operation next, List<Class> types, List args) {
    super("constructor", next, types, args);
  }
  
  public ConstructorOp(List<Class> types, List args) {
    this(null, types, args);
  }
  
  public ConstructorOp(Operation next) {
    this(next, null, null);
  }
  
  public ConstructorOp() {
    this(null, null, null);
  }

  
  @Override
  Class[] getTypes(Object obj) {
    if(argtypes.isEmpty() && !args.isEmpty()) {
      Optional<Method> opt = Arrays.asList(Reflector.of(obj).methods())
          .stream()
          .filter(m->m.getParameterCount() == args.size())
          .findAny();
      if(opt.isPresent()) {
        return opt.get().getParameterTypes();
      }
      else {
        throw new ReflectorException("Method not found: "+ name);
      }
    }
    else {
      return argtypes.toArray(new Class[argtypes.size()]);
    }
  }
  

  @Override
  public OpResult execute(Object obj) {
    try {
      return argtypes.isEmpty() 
        ? OpResult.of(Reflector.of(obj).create())
        : OpResult.of(Reflector.of(obj).selectConstructor(getTypes(obj)).create(args.toArray()));
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
}
