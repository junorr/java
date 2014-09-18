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

import com.jpower.tn3270.Cursor;
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
public class GetFieldExecutor extends Executor {


  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    this.checkSession(sess);
    if(cmd.getType() != CommandType.GET_FIELD) 
      return false;
    
    Var row = VarMemory.getInstance().get(cmd.getArg(0));
    Var col = VarMemory.getInstance().get(cmd.getArg(1));
    Var len = VarMemory.getInstance().get(cmd.getArg(2));
    Var var = VarMemory.getInstance().get(cmd.getArg(3));
    if(row == null) row = new Var("row", cmd.getArg(0));
    if(col == null) col = new Var("col", cmd.getArg(1));
    if(len == null) len = new Var("len", cmd.getArg(2));
    if(var == null) var = new Var(cmd.getArg(3));
    
    int r = row.getInt();
    int c = col.getInt();
    int l = len.getInt();
    if(!Cursor.isValid(r, c) || l < 1) 
      return false;
    
    var.setValue(sess.get(new Cursor(r, c), l));
    VarMemory.getInstance().put(var);
    return true;
  }

}
