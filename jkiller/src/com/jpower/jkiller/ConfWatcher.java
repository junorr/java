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

package com.jpower.jkiller;

import com.jpower.sys.Config;
import com.jpower.sys.LockWatcher;
import com.jpower.wdog.ChangeListener;
import com.jpower.wdog.Watchdog;
import java.io.File;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/10/2012
 */
public class ConfWatcher implements ChangeListener {
  
  private LockWatcher watcher;
  
  private Watchdog dog;
  
  private Config conf;
  
  private Killer kill;

  
  public ConfWatcher(Killer k, Config c) {
    watcher = new LockWatcher(c.getFile());
    dog = new Watchdog(watcher, 1000);
    dog.addListener(this);
    conf = c;
    kill = k;
  }

  
  public Config getConf() {
    return conf;
  }

  
  public ConfWatcher setConf(Config conf) {
    this.conf = conf;
    return this;
  }

  
  @Override
  public void stateChanged(Date time, Object data) {
    if(!conf.getFile().exists()) return;
    conf.load();
    kill.setConf(conf);
  }
  
  
  public ConfWatcher startWatching() {
    dog.startWatcher();
    return this;
  }
  
  
  public ConfWatcher stopWatching() {
    dog.stopWatcher();
    return this;
  }
  
}
