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

package com.jpower.tn3270.script.executor;

import com.jpower.tn3270.Session;
import com.jpower.tn3270.script.Command;
import com.jpower.tn3270.script.CommandType;
import com.jpower.tn3270.script.Var;
import com.jpower.tn3270.script.VarMemory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public class EqualsExecutor extends Executor {


  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    if(cmd.getType() != CommandType.EQUALS) return false;
    
    Var v1 = VarMemory.getInstance().get(cmd.getArg(0));
    if(v1 == null) v1 = Var.TEMP.setValue(cmd.getArg(0));
    Var v2 = VarMemory.getInstance().get(cmd.getArg(1));
    if(v2 == null) v2 = new Var("v2", cmd.getArg(1));
    
    boolean b = v1.value().equalsIgnoreCase(v2.value());
    Var.TEMP.setValue(b);
    return b;
  }

}
