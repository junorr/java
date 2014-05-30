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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/08/2013
 */
public class WaitParser extends CommandParser {


  @Override
  public boolean canParse(String str) {
    return canParseBasic(str, Command.STR_WAIT);
  }


  @Override
  public Command parse(String str) {
    if(!this.canParse(str)) return null;
    str = this.removeInitSpaces(str);
    
    String sr[] = str.split(" ");
    if(sr == null || sr.length 
        < CommandType.WAIT.getArgsSize() + 1)
      return null;
    
    Command sc = new Command(CommandType.WAIT);
    sc.setArg(sr[1], 0)
        .setArg(sr[2], 1);
    
    //wait 1 3 IBBM
    int isp = 0;
    int esp = -1;
    char sp = ' ';
    for(int i = 0; i < 3; i++) {
      isp = str.indexOf(sp, isp);
      esp = str.indexOf(sp, ++isp);
      if(isp < 0) return null;
      if(i == 2) 
        sc.setArg(str.substring(isp), i);
      else if(esp < 0)
        return null;
      else
        sc.setArg(str.substring(isp, esp), i);
    }
    return sc;
  }

}
