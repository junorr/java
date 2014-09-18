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

import com.jpower.tn3270.script.CommandType;
import com.jpower.tn3270.script.Command;
import com.jpower.tn3270.script.VarMemory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/08/2013
 */
public class IfParser extends CommandParser {


  @Override
  public boolean canParse(String str) {
    return canParseBasic(str, Command.STR_IF);
  }


  @Override
  public Command parse(String str) {
    if(!this.canParse(str)) return null;
    str = this.removeInitSpaces(str);
    
    String val = str.substring(Command.STR_IF.length()+1);
    
    if(VarMemory.getInstance().contains(val)) {
      val = VarMemory.getInstance().get(val).value();
    }
    else {
      String[] vals = val.split(" ");
      val = "";
      for(int i = 0; i < vals.length; i++) {
        if(!val.isEmpty()) val += " ";
        if(VarMemory.getInstance().contains(vals[i]))
          val += VarMemory.getInstance().get(vals[i]).value();
        else
          val += vals[i];
      }
    }

    return new Command(CommandType.IF).setArg(val, 0);
  }
  
}
