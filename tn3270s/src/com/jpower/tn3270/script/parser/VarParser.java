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

package com.jpower.tn3270.script.parser;

import com.jpower.expr.Expression;
import com.jpower.expr.ExpressionParser;
import com.jpower.tn3270.script.CommandType;
import com.jpower.tn3270.script.Command;
import com.jpower.tn3270.script.Var;
import com.jpower.tn3270.script.VarMemory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/08/2013
 */
public class VarParser extends CommandParser {
  
  @Override
  public boolean canParse(String str) {
    return canParseBasic(str, Command.STR_VAR);
  }
  
  
  @Override
  public Command parse(String str) {
    if(!this.canParse(str)) return null;
    str = this.removeInitSpaces(str);
    
    int ieq = str.indexOf("=");
    if(ieq < 0) return null;
    
    String name = str.substring(3, ieq).trim();
    String val = str.substring(ieq+1);
    if(name == null || val == null) return null;
    
    String[] vals = val.split(" ");
    val = "";
    for(int i = 0; i < vals.length; i++) {
      if(!val.isEmpty()) val += " ";
      if(VarMemory.getInstance().contains(vals[i]))
        val += VarMemory.getInstance().get(vals[i]).value();
      else
        val += vals[i];
    }
      
    if(this.isExpression(val)) {
      ExpressionParser p = new ExpressionParser(VarMemory.getInstance());
      Expression e = p.parse(val);
      if(e == null) return null;
      val = e.resolve().toString();
    }
    return new Command(CommandType.VAR)
        .setArg(name, 0).setArg(val, 1);
  }
  
  
  public static void main(String[] args) {
    String var = "var v1 = 500";
    CommandParser vp = CommandParser.getInstanceFor(var);
    System.out.println("* vp.parse( "+ var+ " ): "+ vp.parse(var));
    
    var = "var v1 = 500 + 600";
    System.out.println("* vp.parse( "+ var+ " ): "+ vp.parse(var));
    
    VarMemory mem = VarMemory.getInstance();
    mem.put(new Var("abc", 400));
    var = "var v1 = abc + 500";
    System.out.println("* vp.parse( "+ var+ " ): "+ vp.parse(var));
    var = "var v1 = abc";
    System.out.println("* vp.parse( "+ var+ " ): "+ vp.parse(var));
  }
  
}
