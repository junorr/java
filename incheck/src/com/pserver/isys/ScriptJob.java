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
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import us.pserver.psf.ScriptProcessor;
import us.pserver.scron.ExecutionContext;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 14/08/2013
 */
public class ScriptJob extends BasicJob {

  private final Path script;
  
  private final INotesMail mail;
  
  private final ScriptProcessor proc;
  
  
  public ScriptJob(Path script, INotesMail email) {
    super();
    this.script = script;
    mail = email;
    proc = new ScriptProcessor();
  }
  
  
  @Override
  public void execute(ExecutionContext cont) throws Exception {
    super.execute(cont);
    log.info("Executing script: "+ script);
      
    if(script == null || !Files.exists(script))
      throw new IllegalArgumentException(
          "Invalid script path: "+ script);
    
    Path sclog = parser.createTempFile(
        script.getFileName().toString(), 
        EmailParser.LOG_FILE_EXTENTION);
    if(sclog == null)
      throw new IllegalArgumentException(
          "Can not create log file: "+ sclog);
      
    PrintStream out = new PrintStream(
        Files.newOutputStream(sclog, 
        StandardOpenOption.WRITE));
    
    proc.setStdOut(out);
    proc.execFile(script.toString());
    
    out.flush();
    out.close();
    proc.finish();
      
    Path enclog = parser.encode(sclog);
    if(mail == null)
      throw new IllegalArgumentException(
          "Invalid E-mail: "+ mail);
    
    parser.sendResponse(mail, enclog);
    Files.delete(sclog);
    Files.delete(script);
  }
  
}
