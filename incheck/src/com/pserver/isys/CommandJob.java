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

package com.pserver.isys;

import us.pserver.scron.ExecutionContext;



/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 20/08/2013
 */
public abstract class CommandJob extends BasicJob {
  
  public static final String 
      
      BASIC_COMMAND = "cmd",
      
      BASIC_ARG = "/c";
  
  
  protected SystemRun runner;
  
  
  public CommandJob() {
    runner = new SystemRun(BASIC_COMMAND);
  }
  
  
  public void preExec(ExecutionContext cont) throws Exception {
    super.execute(cont);
  }
  
  
  @Override
  public void execute(ExecutionContext cont) throws Exception {
    preExec(cont);
    runner.run();
  }
  
  
  protected void parseCodeCommand(String code) {
    if(code == null || code.trim().length() < 4)
      return;
    
    String[] args = code.substring(4).split(" ");
    String[] cmds = new String[args.length +1];
    cmds[0] = BASIC_ARG;
    for(int i = 1; i < cmds.length; i++) {
      cmds[i] = args[i-1];
    }
    runner.setArgs(cmds);
  }

}
