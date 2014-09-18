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
import com.jpower.sys.SystemRun;
import com.jpower.wdog.ChangeListener;
import com.jpower.wdog.Watchdog;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/10/2012
 */
public class Killer implements ChangeListener {
  
  public static final File LOCK = new File("./jkiller.lock");
  
  public static final File CONFIG_FILE = new File("./jkiller.conf");
  
  public static final String PID = "pid";
  
  
  private Config conf;
  
  private LockWatcher lock;
  
  private Watchdog dog;
  
  private ConfWatcher cw;
  
  private SystemRun cmd;
  
  
  public Killer() {
    if(!LOCK.exists()) 
      try { LOCK.createNewFile(); }
      catch(IOException ex) {}
    LOCK.setWritable(true, false);
    
    cmd = new SystemRun("kill");
    conf = new Config(CONFIG_FILE);
    if(!CONFIG_FILE.exists())
      initConf();
    
    cw = new ConfWatcher(this, conf);
    lock = new LockWatcher(LOCK);
    dog = new Watchdog(lock, 1000);
    dog.addListener(this);
  }
  
  
  public Killer start() {
    System.out.println("------------------------");
    System.out.println(" * JKiller started!");
    System.out.println("    Lock File  : "+ LOCK.getAbsolutePath());
    System.out.println("    Config File: "+ conf.getFile().getAbsolutePath());
    dog.startWatcher();
    cw.startWatching();
    return this;
  }
  
  
  public Killer stop() {
    System.out.println(" * Stoping JKiller...");
    LOCK.delete();
    cw.stopWatching();
    dog.stopWatcher();
    return this;
  }
  
  
  private void initConf() {
    conf.put(PID, 0);
    conf.setComment("jkiller");
    conf.save();
    CONFIG_FILE.setWritable(true, false);
  }

  
  public Config getConf() {
    return conf;
  }

  
  public void setConf(Config conf) {
    this.conf = conf;
    if(conf != null) {
      this.kill();
    }
  }
  
  
  public Killer kill() {
    int pid = conf.getInt(PID);
    if(pid > 0) {
      System.out.println(" * Process Killed: #"+ pid);
      cmd.setArgs(String.valueOf(pid));
      cmd.run();
      if(cmd.getOutput() != null && !cmd.getOutput().isEmpty())
        System.out.println(" * "+ cmd.getOutput());
    }
    return this;
  }
  
  
  @Override
  public void stateChanged(Date time, Object data) {
    System.out.println(" * Lock File Modified!");
    System.out.println(" * Exiting now...");
    System.out.println("------------------------");
    System.exit(0);
  }
  
  
  public static void main(String[] args) {
    Killer k = new Killer();
    k.start();
  }

}
