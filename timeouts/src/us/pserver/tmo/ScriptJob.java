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

package us.pserver.tmo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import murlen.util.fscript.ParserListener;
import us.pserver.log.SimpleLog;
import us.pserver.scron.ExecutionContext;
import us.pserver.scron.Job;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/04/2014
 */
public class ScriptJob implements Job, ParserListener {

  private String scpath;
  
  private transient SimpleLog log;
  
  private transient ScriptExecutor exec;
  
  private boolean enabled;
  
  
  public ScriptJob(String file) {
    if(file == null)
      throw new IllegalArgumentException(
          "Invalid Script File: "+ file);
    scpath = file;
    exec = null;
    this.createLog();
    enabled = true;
  }
  
  
  public ScriptJob(ScriptExecutor se) {
    if(se == null)
      throw new IllegalArgumentException(
          "Invalid ScriptExecutor: "+ se);
    scpath = null;
    exec = se;
    this.createLog();
    enabled = true;
  }
  
  
  public ScriptJob(String file, ScriptExecutor se) {
    this(se);
    if(file == null)
      throw new IllegalArgumentException(
          "Invalid Script File: "+ file);
    scpath = file;
  }
  
  
  public ScriptJob enable() {
    enabled = true;
    return this;
  }
  
  
  public ScriptJob disable() {
    enabled = false;
    return this;
  }
  
  
  public boolean isEnabled() {
    return enabled;
  }
  
  
  public ScriptJob setEnabled(boolean en) {
    enabled = en;
    return this;
  }
  
  
  public ScriptJob setExecutor(ScriptExecutor e) {
    if(e != null) {
      exec = e;
    }
    return this;
  }
  
  
  private void createLog() {
    if(scpath != null) {
      int pt = scpath.lastIndexOf(".");
      if(pt <= 0) pt = scpath.length() -1;
      String logfile = scpath.substring(0, pt)+ ".log";
      log = new SimpleLog(logfile);
    }
  }
  
  
  public Path getScriptPath() {
    if(scpath == null) return null;
    return Paths.get(scpath);
  }


  @Override
  public void execute(ExecutionContext context) throws Exception {
    if(enabled) {
      if(log == null) createLog();
      Object ret = exec.exec(scpath);
      if(ret != null) 
        log.debug("Exution Finished [Return: "+ ret+ "]!");
      else
        log.debug("Exution Finished!");
    }
    exec.execDone();
  }


  @Override
  public void error(Throwable th) {
    if(log == null) createLog();
    log.error(th.getClass().getSimpleName()
            + ": '"+ th.getMessage()+ "'");
    th.printStackTrace();
  }


  @Override
  public void lineUpdate(String line) {
    if(log == null) createLog();
    log.info("> "+ line);
  }
  
  
  @Override
  public String toString() {
    Path p = this.getScriptPath();
    if(p == null) return scpath;
    return (enabled ? "" : "! ") + p.getFileName().toString();
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.scpath);
    hash = 17 * hash + (this.enabled ? 1 : 0);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ScriptJob other = (ScriptJob) obj;
    if (!Objects.equals(this.scpath, other.scpath)) {
      return false;
    }
    if (this.enabled != other.enabled) {
      return false;
    }
    return true;
  }
  
}
