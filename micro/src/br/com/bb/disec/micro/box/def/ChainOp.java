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
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/07/2017
 */
public class ChainOp extends BaseOp {
  
  private final Operation op;

  
  public ChainOp(String name, Operation op) {
    super(name, null);
    this.op = op;
  }
  
  
  public ChainOp(Operation op) {
    this(op.getName(), op);
  }
  
  
  @Override
  public OpResult execute(Object obj) {
    OpResult res = OpResult.of(obj);
    Optional<Operation> opr = Optional.of(op);
    while(opr.isPresent() && res.getReturnValue().isPresent()) {
      res = opr.get().execute(res.getReturnValue().get());
      opr = opr.get().next();
    }
    return res;
  } 

}
