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
import java.io.PrintStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public class PrintExecutor extends Executor {

  private PrintStream out;
  
  
  public PrintExecutor() {
    out = System.out;
  }
  
  
  public PrintExecutor(PrintStream ps) {
    this();
    if(ps != null) out = ps;
  }


  public PrintStream getStdout() {
    return out;
  }


  public PrintExecutor setStdout(PrintStream out) {
    if(out != null)
      this.out = out;
    return this;
  }
  

  @Override
  public boolean exec(Command cmd, Session sess) {
    this.checkCommand(cmd);
    if(cmd.getType() != CommandType.PRINT) 
      return false;
    
    out.println(cmd.getArg(0));
    return true;
  }

}
