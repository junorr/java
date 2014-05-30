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
import com.jpower.tn3270.script.Var;
import com.jpower.tn3270.script.VarMemory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 08/08/2013
 */
public class Connect3270Executor extends Executor {


  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    if(sess == null) return false;
    if(sess.isConnected()) return true;
    
    if(cmd.getArg(0) == null || cmd.getArg(1) == null)
      return false;
    
    Var host = null;
    Var port = null;
    if(VarMemory.getInstance().contains(cmd.getArg(0))) {
      host = VarMemory.getInstance().get(cmd.getArg(0));
    } else {
      host = new Var("tn3270.host", cmd.getArg(0));
    }
    if(VarMemory.getInstance().contains(cmd.getArg(1))) {
      port = VarMemory.getInstance().get(cmd.getArg(1));
    } else {
      port = new Var("tn3270.port", cmd.getArg(1));
    }
    if(port.getInt() == Var.INVALID_NUMBER)
      return false;
    
    sess.connect(host.value(), port.getInt());
    return sess.isConnected();
  }

}
