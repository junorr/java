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

package us.pserver.scronv6;

import us.pserver.scron.JobFactory;
import us.pserver.scron.ExecutionContext;
import us.pserver.scronv6.hide.Pair;
import us.pserver.scron.Job;
import us.pserver.scron.Schedule;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import us.pserver.log.Log;
import us.pserver.log.LogFactory;
import us.pserver.scron.AbstractCron;

/**
 * <b>SCronV6</b> é o motor de execução dos trabalhos
 * agendados. É configurado por padrão
 * com 3 Threads executantes, uma responsável por monitorar
 * e agendar os trabalhos e outras duas <code>Threads</code> 
 * responsáveis pela execução dos <code>JobsManager</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/04/2014
 */
public class SCronV6 extends AbstractCron {

  /**
   * Quantidade padrão <code>(2)</code> 
   * de <code>Threads</code> que executarão os <code>JobsManager</code>.
   */
  public static final int DEFAULT_THREADS = 2;
  
  
  private ExecutorService exes;
  
  private int threads;
  
  private Scheduler scheduler;
  
  private Log log;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public SCronV6() {
    super();
    threads = DEFAULT_THREADS;
    log = LogFactory.getSimpleLog(this.getClass());
  }
  
  
  /**
   * Agenda uma tarefa para execução, iniciando 
   * <code>SCronV6</code> caso ainda não esteja executando.
   * @param s Agendamento <code>Schedule</code> da tarefa.
   * @param j Tarefa <code>Job</code> a ser executada.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  @Override
  public SCronV6 put(Schedule s, Job j) {
    if(s != null && j != null && s.isValid()) {
      if(s.getCountdown() < 0) s.reeschedule();
      Pair p = new Pair(s, j);
      manager().list().put(p);
      manager().sort();
      if(isLogEnabled()) log.debug(
          "Job inserted: " + p.toString());
      if(!isRunning()) start();
    }
    return this;
  }
  
  
  public Log getLog() {
    return log;
  }
  
  
  public void setLog(Log l) {
    if(l != null) log = l;
  }
  
  
  /**
   * Agenda um <code>Runnable</code> para execução, iniciando 
   * <code>SCronV6</code> caso ainda não esteja executando.
   * @param s Agendamento <code>Schedule</code> da tarefa.
   * @param r Tarefa <code>Runnable</code> a ser executada.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  @Override
  public SCronV6 put(Schedule s, Runnable r) {
    if(s != null && r != null && s.isValid()) {
      if(s.getCountdown() < 0) s.reeschedule();
      Pair p = new Pair(s, JobFactory.create(r));
      manager().list().put(p);
      manager().sort();
      if(isLogEnabled()) log.debug(
          "Job inserted: " + p.toString());
      if(!isRunning()) start();
    }
    return this;
  }
  
  
  /**
   * Retorna a quantidade de <code>Threads</code>
   * responsáveis pela execução dos <code>JobsManager</code>.
   * @return quantidade de <code>Threads</code>.
   */
  public int getAvailableThreads() {
    return threads;
  }
  
  
  /**
   * Define a quantidade de <code>Threads</code>
   * responsáveis pela execução dos <code>JobsManager</code>.
   * @param th quantidade de <code>Threads</code>.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  public SCronV6 setAvailableThreads(int th) {
    if(th > 0 && th < 100) threads = th;
    return this;
  }
  
  
  /**
   * Inicia a excução de <code>SCronV6</code>.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  @Override
  public SCronV6 start() {
    exes = Executors.newFixedThreadPool(threads);
    scheduler = new Scheduler();
    scheduler.start();
    log.info("SCronV6 Started!");
    return this;
  }
  
  
  /**
   * Interrompe a execução de <code>SCronV6</code>.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  @Override
  public SCronV6 stop() {
    if(isRunning()) {
      scheduler.stop();
      exes.shutdown();
      exes = null;
      log.info("SCronV6 Stopped!");
    }
    return this;
  }
  
  
  /**
   * Verifica se <code>SCronV6</code> está em execução.
   * @return <code>true</code> se <code>SCronV6</code> está 
   * em execução, <code>false</code> caso contrário.
   */
  @Override
  public boolean isRunning() {
    return scheduler != null 
        && scheduler.isRunning() 
        && exes != null;
  }
  
  
  
  /**
   * Scheduler é o <code>Thread</code> interno
   * de <code>SCronV6</code>, responsável por verificar,
   * organizar e determinar a execução de
   * todos os agendamentos.
   */
  class Scheduler implements Runnable {
    
    private final Object O = new Object();
    
    private boolean run;
    
    private final Thread thread;
    
    /**
     * Construtor padrão sem argumentos.
     */
    public Scheduler() {
      run = false;
      thread = new Thread(this, "Scheduler");
    }
    
    /**
     * Retorna a <code>Thread</code> de execução
     * de <code>Scheduler</code>.
     * @return <code>Thread</code>.
     */
    public Thread getThread() {
      return thread;
    }
    
    /**
     * Inicia a execução da <code>Threade</code> 
     * de <code>Scheduler</code>.
     */
    public void start() {
      run = true;
      thread.start();
    }
    
    /**
     * Interrompe a execução de <code>Scheduler</code>.
     */
    public void stop() {
      run = false;
    }
    
    /**
     * Verifica se <code>Scheduler</code> está em execução.
     * @return <code>true</code> se <code>Scheduler</code>
     * está em execução, <code>false</code>
     * caso contrário.
     */
    public boolean isRunning() {
      return run;
    }
    
    /**
     * Excuta as funções de <code>Scheduler</code>.
     */
    @Override
    public void run() {
      while(run) {
        Pair pair = jobs().peek();
        
        if(jobs().isEmpty()) {
          if(isShutdownAtEmpty())
            SCronV6.this.stop();
          else
            delay(GET_DELAY);
        }
        
        else if(!pair.schedule().isValid()) {
          jobs().remove(pair);
          log.debug("Schedule "
              + "Removed: " + pair.toString());
        }
        
        else if(pair.schedule()
            .isCountdownBeteewn(0, MIN_DELAY)) {
          
          jobs().remove(pair);
          exes.submit(new Executor(pair, 
              new ExecutionContext(pair.schedule(), dataMap)));
          
          if(pair.schedule().isRepeatEnabled()) {
            Schedule s = pair.schedule().clone();
            s.reeschedule();
            jobs().put(new Pair(s, pair.job()));
          }
        }
        
        else if(pair.schedule().getCountdown() < 0) {
          pair.schedule().reeschedule();
        }
        
        else {
          delay(GET_DELAY);
        }
        
        manager().sort();
      }
    }
    
    /**
     * Interrompe a execução pelo tempo em 
     * informado em milisegundos.
     * @param millis Tempo em milisegundos.
     */
    public void delay(int millis) {
      try {
        synchronized(O) {
          O.wait(millis);
        }
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
}
