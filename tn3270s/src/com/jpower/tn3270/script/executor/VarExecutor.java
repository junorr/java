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
import com.jpower.tn3270.script.parser.CommandParser;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public class VarExecutor extends Executor {

  
  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    if(cmd.getType() != CommandType.VAR)
      return false;
    
    String vn = cmd.getArg(0);
    String vv = cmd.getArg(1);
    if(vn == null || vv == null) return false;
    
    if(vv.startsWith(Command.STR_CONTAINS)) {
      CommandParser cp = CommandParser.getInstanceFor(vv);
      ContainsExecutor ce = new ContainsExecutor();
      if(cp == null) vv = "false";
      else {
        ce.exec(cp.parse(vv), sess);
        vv = Var.TEMP.value();
      }
    }
    else if(vv.startsWith(Command.STR_EQUALS)) {
      CommandParser cp = CommandParser.getInstanceFor(vv);
      EqualsExecutor ce = new EqualsExecutor();
      if(cp == null) vv = "false";
      else {
        ce.exec(cp.parse(vv), sess);
        vv = Var.TEMP.value();
      }
    }
    else if(vv.startsWith(Command.STR_NOT)) {
      CommandParser cp = CommandParser.getInstanceFor(vv);
      NotExecutor ce = new NotExecutor();
      if(cp == null) vv = "false";
      else {
        ce.exec(cp.parse(vv), sess);
        vv = Var.TEMP.value();
      }
    }
    
    VarMemory.getInstance().put(new Var(vn, vv));
    return true;
  }

  
  public static void main(String[] args) {
    String var = "var vc = contains vr va";
    VarMemory.getInstance()
        .put(new Var("vr", "rato"))
        .put(new Var("va", "at"));
    System.out.println("* "+ var);
    CommandParser cp = CommandParser.getInstanceFor(var);
    Command c = cp.parse(var);
    Executor ce = Executor.getInstanceFor(c.getType());
    System.out.println("* ce.exec(): "+ ce.exec(c, null));
    System.out.println("* var: "+ VarMemory.getInstance().get("vc"));
  }
  
}
