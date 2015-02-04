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

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import murlen.util.fscript.FSException;
import us.pserver.psf.ScriptProcessor;
import us.pserver.scron.SimpleCron;
import us.pserver.scronv6.SCronV6;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/04/2014
 */
public class ScriptExecutor {

  private SimpleCron cron;
  
  private ScriptProcessor proc;
  
  private final ReentrantLock lock;
  
  private Runnable doneAction;
  
  
  public ScriptExecutor() {
    cron = new SimpleCron();
    proc = new ScriptProcessor(cron);
    lock = new ReentrantLock();
    doneAction = null;
  }
  
  
  public ScriptExecutor(SimpleCron cron) {
    if(cron == null)
      throw new IllegalArgumentException(
          "Invalid SCronV6: "+ cron);
    this.cron = cron;
    proc = new ScriptProcessor(cron);
    lock = new ReentrantLock();
    doneAction = null;
  }
  
  
  public ScriptProcessor getProcessor() {
    return proc;
  }
  
  
  public ScriptExecutor setDoneAction(Runnable r) {
    doneAction = r;
    return this;
  }
  
  
  public Object exec(String scriptfile) throws IOException, FSException {
    try {
      lock.lock();
      proc.executor().reset();
      return proc.execFile(scriptfile);
    } finally {
      lock.unlock();
    }
  }
  
  
  public void execDone() {
    if(doneAction != null)
      doneAction.run();
  }
  
}
