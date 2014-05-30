/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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


package com.jpower.sys;

import com.jpower.wdog.ChangeListener;
import com.jpower.wdog.FileChangeWatcher;
import com.jpower.wdog.SimpleWatchdog;
import com.jpower.wdog.Watchdog;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @version 0.0 - 17/01/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class ProcessKiller implements ChangeListener {
  
  public static final int SECONDS_INTERVAL = 1000;
  
  public static final File file = new File("./proc-kill.conf");
  
  public static final String CMD = "kill";
  
  public static final String KEY_PID = "pid_to_kill";
  
  public static final String COMMENT = 
      "ProcessKiller.";
  
  
  private SystemRun kill;
  
  private Watchdog wdog;
  
  private FileChangeWatcher watcher;
  
  private Config conf;
  
  
  public ProcessKiller() {
    if(!file.exists())
      try {
        file.createNewFile();
        file.setWritable(true, false);
      } catch(IOException ex) {}
    
    conf = new Config(file);
    if(!conf.contains(KEY_PID))
      this.initDefaults();
    
    watcher = new FileChangeWatcher(file);
    wdog = new SimpleWatchdog(watcher, SECONDS_INTERVAL);
    wdog.addListener(this);
    kill = new SystemRun(CMD);
  }
  
  
  private void initDefaults() {
    conf.put(KEY_PID, 0);
    conf.setComment(COMMENT);
    conf.save();
  }
  
  
  public void start() {
    wdog.startWatcher();
  }
  
  
  public void stop() {
    wdog.stopWatcher();
  }


  @Override
  public void stateChanged(Date date, Object o) {
    conf.load();
    int pid = conf.getInt(KEY_PID);
    if(pid > 0) {
      Log.logger().info("Kill process: "+ pid);
      kill.setArgs(String.valueOf(pid)).run();
    }
  }

  
  public static void main(String[] args) {
    ProcessKiller pk = new ProcessKiller();
    pk.start();
  }
  
}
