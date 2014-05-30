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

import com.jpower.inet.INotesMail;
import java.nio.file.Files;
import java.nio.file.Path;
import us.pserver.scron.ExecutionContext;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 14/08/2013
 */
public class CustomCommandJob extends CommandJob {

  private String cmd;
  
  private INotesMail mail;
  
  
  public CustomCommandJob(String command, INotesMail email) {
    super();
    cmd = command;
    mail = email;
  }
  
  
  @Override
  public void execute(ExecutionContext cont) throws Exception {
    preExec(cont);
    if(cmd == null || cmd.length() < 4)
      throw new IllegalArgumentException(
          "Invalid Command: "+ cmd);
    
    log.info("Executing command: "+ cmd);
    parseCodeCommand(cmd);
    runner.run();
    String out = runner.getOutput();
    
    if(out == null || out.trim().isEmpty())
      return;
    log.info(out);
    
    if(parser == null)
      throw new IllegalArgumentException(
          "Invalid EmailParser instance: "+ parser);
    if(mail == null)
      throw new IllegalArgumentException(
          "Invalid E-mail: "+ mail);
    
    Path encout = parser.encode(out);
    parser.sendResponse(mail, encout);
    Files.delete(encout);
  }
  
}
