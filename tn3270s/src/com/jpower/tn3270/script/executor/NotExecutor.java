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

import com.jpower.expr.Expression;
import com.jpower.expr.ExpressionParser;
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
public class NotExecutor extends Executor {


  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    if(cmd.getType() != CommandType.NOT) 
      return false;
    
    if(cmd.getArg(0) != null) {
      
      String val = cmd.getArg(0);
      if(val.startsWith(Command.STR_CONTAINS)) {
        CommandParser cp = CommandParser.getInstanceFor(val);
        ContainsExecutor ce = new ContainsExecutor();
        if(cp == null) return true;
        else {
          return !ce.exec(cp.parse(val), sess);
        }
      } 
      else if(val.startsWith(Command.STR_EQUALS)) {
        CommandParser cp = CommandParser.getInstanceFor(val);
        EqualsExecutor ce = new EqualsExecutor();
        if(cp == null) return true;
        else {
          return !ce.exec(cp.parse(val), sess);
        }
      } 
    
      Var.TEMP.setValue(val);
      if(val.equalsIgnoreCase("true")
          || val.equalsIgnoreCase("false")) {
        return !Var.TEMP.getBoolean();
        
      } else if(Var.TEMP.getInt() != Var.INVALID_NUMBER) {
        return (Var.TEMP.getInt() == 0);
      }
    }
    throw new IllegalArgumentException(
        "If argument not valid: "+ cmd.getArg(0));
  }
  
  
  public static void main(String[] args) {
    NotExecutor ife = new NotExecutor();
    
    VarMemory.getInstance().put(new Var("verdade", "true"));
    String cmd = "not contains att at";
    CommandParser c = CommandParser.getInstanceFor(cmd);
    System.out.println(c);
    
    System.out.println(ife.exec(c.parse(cmd), null));
  }
  
}
