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
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/07/2017
 */
public class SetFieldOp extends BaseOp {
  
  private final Object arg;

  
  public SetFieldOp(String name, Operation next, Object arg) {
    super(name, next);
    if(arg == null) {
      throw new IllegalArgumentException("Bad null argument");
    }
    this.arg = arg;
  }

  public SetFieldOp(String name, Object arg) {
    this(name, null, arg);
  }


  @Override
  public OpResult execute(Object obj) {
    try {
      return OpResult.of(Reflector.of(obj).selectField(name).set(arg).getTarget());
    }
    catch(Exception e) {
      return OpResult.of(e);
    }
  }
  
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{\n"
        + "  name="+ name+ ",\n"
        + "  arg="+ arg+ "\n}"
        + (next().isPresent() ? next.toString() + "\n" : "");
  }

}
