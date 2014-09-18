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
import us.pserver.cdr.b64.Base64StringCoder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 09/08/2013
 */
public class PassExecutor extends Executor {

  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    if(cmd.getArg(0) == null
        || cmd.getArg(1) == null)
      return false;
    
    try {
      Base64StringCoder sc = new Base64StringCoder();
      VarMemory.getInstance().put(
          new Var(cmd.getArg(0), 
          sc.decode(cmd.getArg(1))));
      return true;
    } catch(Exception e) {
      return false;
    }
  }
  
  
  public static void main(String[] args) {
    String s = "OTg3NjU0OTg=";
    System.out.println(s);
    Base64StringCoder sc = new Base64StringCoder();
    System.out.println(sc.decode(s));
  }
  
}
