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
import us.pserver.log.output.LogOutput;

/**
 * High performance, double threaded log system implementation.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
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
  
  
  /**
   * Constructor which receives the log instance name and 
   * the <code>OutputFormatter</code> object.
   * @param name This Log instance name.
   * @param fmt The <code>OutputFormatter</code> object.
   * @see us.pserver.log.format.OutputFormatter
   */
  public SLogV2(String name, OutputFormatter fmt) {
    super(name);
    this.setOutputFormatter(fmt);
    entries = Collections.synchronizedList(new LinkedList<LogEntry>());
    availableThreads = DEFAULT_AVAILABLE_THREADS;
    running = true;
    stopOnFinish = false;
  }
  
  
  /**
   * Default construtor, receives this log instance name.
   * @param name This log instance name.
   */
  public SLogV2(String name) {
    this(name, OutputFormatterFactory.standardFormatter());
  }
  
  
  /**
   * Constructor which receives this log instance class name.
   * @param cls This log instance class name.
   */
  public SLogV2(Class cls) {
    this((cls == null ? null : cls.getName()));
  }
  
  /**
   * Constructor which receives this log instance class 
   * name and the <code>OutputFormatter</code> object.
   * @param cls This log instance class name.
   * @param fmt The <code>OutputFormatter</code> object.
   */
  public SLogV2(Class cls, OutputFormatter fmt) {
    this((cls == null ? null : cls.getName()), fmt);
  }
  
  
  /**
   * Set the default number of available threads to 
   * perform asynchronous log outputs requests.
   * @param num The default number of available threads.
   * @return This modified <code>SLogV2</code> instance.
   */
  public SLogV2 setAvailableThreads(int num) {
    if(num > 0)
      availableThreads = num;
    return this;
  }
  
  
  /**
   * Get the default number of available threads to 
   * perform asynchronous log outputs requests.
   * @return The default number of available threads.
   */
  public int getAvailableThreads() {
    return availableThreads;
  }
  
  
  /**
   * Return <code>true</code> if the executor threads are running.
   * @return <code>true</code> if the executor threads are running,
   * <code>false</code> otherwise.
   */
  public boolean isRunning() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      return running;
    }
  }
  
  
  /**
   * Start asynchronous log processing.
   * @return This modified <code>SLogV2</code> instance.
   */
  public SLogV2 start() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      running = true;
      exec = Executors.newFixedThreadPool(availableThreads);
      new Thread(this, "SLogV2@"+ System.currentTimeMillis()).start();
    }
    return this;
  }
  
  
  /**
   * Stop asynchronous log processing.
   * @return This modified <code>SLogV2</code> instance.
   */
  public SLogV2 stop() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      running = false;
    }
    return this;
  }
  
  /**
   * Stop asynchronous log processing when all requests are done.
   * @return This modified <code>SLogV2</code> instance.
   */
  public SLogV2 stopOnFinish() {
    synchronized(DEFAULT_AVAILABLE_THREADS) {
      stopOnFinish = true;
    }
    return this;
  }
  
  
  /**
   * Verify if the log processing is configured to stop 
   * when all requests are done.
   * @return <code>true</code> if the log processing is 
   * configured to stop when all requests are done, 
   * <code>false</code> otherwise.
   */
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
    for(LogOutput o : outputs.values()) {
      o.close();
    }
    entries.clear();
    outputs.clear();;
    return this;
  }
  
  
  
  /**************************************************/
  /********  INTERNAL CLASS LOG_EXECUTOR  ***********/
  /**************************************************/
  
  
  /**
   * Internal class which perform log outputs requests, 
   * to be used in <code>ExecutorService</code>.
   */
  class LogExecutor implements Runnable {
    
    private LogEntry entry;
    
    /**
     * Default Constructor receives a LogEntry object to be processed.
     * @param le LogEntry object to be processed.
     */
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
    
    /**
     * Perform the log output.
     * @param lvl Log Level.
     * @param dte Log Date.
     * @param msg Log message.
     */
    public void log(LogLevel lvl, Date dte, String msg) {
      if(lvl != null && msg != null && dte != null) {
        for(LogOutput o : outputs.values()) {
          o.log(lvl, formatter.format(lvl, dte, logname, msg));
        }
      }
    }
  
    /**
     * Perform the log output.
     * @param lvl Log Level.
     * @param dte Log Date.
     * @param msg Error to be logged.
     * @param logStackTrace If <code>true</code>, all the 
     * throwable stack trace will be logged.
     */
    public void log(LogLevel lvl, Date dte, Throwable th, boolean logStackTrace) {
      if(lvl != null && th != null && dte != null) {
        Date dt = new Date();
        if(logStackTrace) {
          for(LogOutput o : outputs.values()) {
            o.log(lvl, formatter.format(
                lvl, dte, th.getClass().getName(), 
                th.getLocalizedMessage()));
          }
        } 
        else {
          String trace = th.getStackTrace()[0].toString();
          for(LogOutput o : outputs.values()) {
            o.log(lvl, formatter.format(
                lvl, dte, trace, th.getLocalizedMessage()));
          }
        }
        if(logStackTrace) {
          StackTraceElement[] elts = th.getStackTrace();
          for(StackTraceElement st : elts) {
            for(LogOutput o : outputs.values()) {
              o.log(lvl, continueFormat.format(lvl, dt, logname, st.toString()));
            }
          }
        }//if
      }//if
    }//log method
  }//class LogExecutor
  
  
}//class SLogV2