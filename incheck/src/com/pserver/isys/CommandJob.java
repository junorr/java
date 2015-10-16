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

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import us.pserver.scron.ExecutionContext;



/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 20/08/2013
 */
public abstract class CommandJob extends BasicJob {
  
  protected SystemRun runner;
  
  
  public CommandJob() {
    runner = new SystemRun();
  }
  
  
  public void preExec(ExecutionContext cont) throws Exception {
    super.execute(cont);
  }
  
  
  @Override
  public void execute(ExecutionContext cont) throws Exception {
    preExec(cont);
    runner.run();
  }
  
  
  protected void parseCommand(String code) {
    if(code == null || code.trim().length() < 4)
      return;
    
    char[] cs = code.toCharArray();
    if(code.startsWith("cmd")) {
      cs = code.substring(4).toCharArray();
    }
    System.out.println(new String(cs));
    List<String> list = new LinkedList<>();
    StringBuffer sb = new StringBuffer();
    boolean quote = false;
    for(int i = 0; i < cs.length; i++) {
      if(quote) {
        if(cs[i] == '"' || cs[i] == '\'')
          quote = false;
        else 
          sb.append(cs[i]);
      }
      else if(cs[i] == ' ') {
        if(sb.length() > 0)
          list.add(sb.toString());
        sb = new StringBuffer();
      }
      else {
        if(cs[i] == '"' || cs[i] == '\'')
          quote = true;
        else 
          sb.append(cs[i]);
      }
    }
    if(sb.length() > 0)
      list.add(sb.toString());
    String[] args = new String[list.size()];
    System.out.println("* args="+ Arrays.toString(args));
    runner.setCommandArgs(list.toArray(args));
  }
  
  
  public static void main(String[] args) {
    CommandJob cj = new CommandJob(){};
    cj.parseCommand("/home/juno/viewports");
    System.out.println(cj.runner.buildCommand());
    cj.runner.run();
    System.out.println(cj.runner.getOutput());
  }
  
}
