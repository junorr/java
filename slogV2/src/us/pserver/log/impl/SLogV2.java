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

package us.pserver.log.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import us.pserver.log.Log;
import us.pserver.log.LogLevel;
import us.pserver.log.format.OutputFormatter;
import us.pserver.log.format.OutputFormatterFactory;
import us.pserver.log.internal.LogEntry;

/**
 * High performance, double threaded log system implementation.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 14/04/2014
 */
public class SLogV2 extends AbstractLog implements Runnable {
  
  /**
   * <code>
   *  DEFAULT_AVAILABLE_THREADS = 2
   * </code><br>
   * Default number of worker <code>Threads</code> for client requests attending.
   */
  public static final Integer DEFAULT_AVAILABLE_THREADS = 2;
  
  
  private final List<LogEntry> entries;
  
  private ExecutorService exec;
  
  private int availableThreads;
  
  private boolean running;
  
  private boolean stopOnFinish;
  
  
  public SLogV2(String name, OutputFormatter fmt) {
    super(name);
    this.setOutputFormatter(fmt);
    entries = Collections.synchronizedList(new LinkedList<LogEntry>());
    availableThreads = DEFAULT_AVAILABLE_THREADS;
    running = true;
    stopOnFinish = false;
  }
  
  
  public SLogV2(String name) {
    this(name, OutputFormatterFactory.standardFormatter());
  }
  
  
  public SLogV2(Class cls) {
    this((cls == null ? null : cls.getName()));
  }
  
  public SLogV2(Class cls, OutputFormatter fmt) {
    this((cls == null ? null : cls.getName()), fmt);
  }
  
  
  public SLogV2 setAvailableThreads(int num) {
    if(num > 0)
      availableThreads = num;
    return this;
  }
  
  
  public int getAvailableThreads() {
    return availableThreads;
  }
  
  
  public boolean isRunning() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      return running;
    }
  }
  
  
  public SLogV2 start() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      running = true;
      exec = Executors.newFixedThreadPool(availableThreads);
      new Thread(this, "SLogV2@"+ System.currentTimeMillis()).start();
    }
    return this;
  }
  
  
  public SLogV2 stop() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      running = false;
    }
    return this;
  }
  
  
  public SLogV2 stopOnFinish() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      stopOnFinish = true;
    }
    return this;
  }
  
  
  public boolean isStopOnFinish() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      return stopOnFinish;
    }
  }
  
  
  @Override
  public void run() {
    while(running) {
      if(entries.isEmpty()) {
        if(isStopOnFinish()) break;
        else sleep(50);
      }
      else 
        exec.submit(new LogExecutor(entries.remove(0)));
    }
    running = false;
    exec.shutdown();
    entries.clear();
  }
  
  
  private void sleep(long millis) {
    try { Thread.sleep(millis); } 
    catch(InterruptedException e) {}
  }
  

  @Override
  public Log log(LogLevel lvl, String msg) {
    if(lvl != null && msg != null && levels.isLevelEnabled(lvl)) {
      entries.add(new LogEntry(lvl, new Date(), logname, msg));
    }
    return this;
  }
  
  
  @Override
  public Log log(LogLevel lvl, Throwable th, boolean logStackTrace) {
    if(lvl != null && th != null && levels.isLevelEnabled(lvl)) {
      entries.add(new LogEntry(lvl, new Date(), logname, th, logStackTrace));
    }
    return this;
  }
  
  
  @Override
  public Log close() {
    stop();
    exec.shutdownNow();
    outputs.values().forEach(o->o.close());
    entries.clear();
    outputs.clear();;
    return this;
  }
  
  
  
  /**************************************************/
  /********  INTERNAL CLASS LOG_EXECUTOR  ***********/
  /**************************************************/
  
  
  class LogExecutor implements Runnable {
    private LogEntry entry;
    
    public LogExecutor(LogEntry le) {
      this.entry = le;
    }
    
    @Override
    public void run() {
      if(outputs.isEmpty()
          || entry == null) return;
      if(entry.isThrowable()) {
        log(entry.getLevel(), entry.getDate(), 
            entry.getThrowable(), entry.isLoggingStackTrace());
      }
      else {
        log(entry.getLevel(), entry.getDate(), entry.getMassage());
      }
    }
    
    public void log(LogLevel lvl, Date dte, String msg) {
      if(lvl != null && msg != null && dte != null) {
        outputs.values().forEach(o->
            o.log(lvl, formatter.format(lvl, dte, logname, msg)));
      }
    }
  
    public void log(LogLevel lvl, Date dte, Throwable th, boolean logStackTrace) {
      if(lvl != null && th != null && dte != null) {
        Date dt = new Date();
        if(logStackTrace) {
          outputs.values().forEach(o->
              o.log(lvl, formatter.format(
                  lvl, dt, th.getClass().getName(), 
                  th.getLocalizedMessage())));
        } 
        else {
          String trace = th.getStackTrace()[0].toString();
          outputs.values().forEach(o->
              o.log(lvl, formatter.format(
                  lvl, dt, trace, th.getLocalizedMessage())));
        }
        if(logStackTrace) {
          Arrays.asList(th.getStackTrace()).forEach(e->
              outputs.values().forEach(o->
                  o.log(lvl, continueFormat.format(
                      lvl, dt, logname, e.toString()))));
        }//if
      }//if
    }//log method
  }//class LogExecutor
  
  
}//class SLogV2