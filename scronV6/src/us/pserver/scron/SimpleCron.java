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

package us.pserver.scron;

import us.pserver.scronv6.repeat.Pair;

/**
 * <b>SimpleCron</b> é o motor de execução dos trabalhos
 * agendados. É configurado por padrão
 * com 3 Threads executantes, uma responsável por monitorar
 * e agendar os trabalhos e outras duas <code>Threads</code> 
 * responsáveis pela execução dos <code>JobsManager</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/04/2014
 */
public class SimpleCron extends AbstractCron {

  private Scheduler scheduler;
  
  
  /**
   * Construtor que recebe o objeto de log padrão.
   * @param l objeto de log padrão.
   */
  public SimpleCron() {
    super();
  }
  
  
  /**
   * Agenda uma tarefa para execução, iniciando 
   * <code>SimpleCron</code> caso ainda não esteja executando.
   * @param s Agendamento <code>Schedule</code> da tarefa.
   * @param j Tarefa <code>Job</code> a ser executada.
   * @return Esta instância modificada de <code>SimpleCron</code>.
   */
  @Override
  public SimpleCron put(Schedule s, Job j) {
    if(s != null && j != null && s.isValid()) {
      if(s.getCountdown() < 0) s.reeschedule();
      Pair p = new Pair(s, j);
      jobs.list().put(p);
      jobs.sort();
      if(logEnabled) log.debug(
          "Job inserted: " + p.toString());
      if(!isRunning()) start();
    }
    return this;
  }
  
  
  /**
   * Agenda um <code>Runnable</code> para execução, iniciando 
   * <code>SimpleCron</code> caso ainda não esteja executando.
   * @param s Agendamento <code>Schedule</code> da tarefa.
   * @param r Tarefa <code>Runnable</code> a ser executada.
   * @return Esta instância modificada de <code>SimpleCron</code>.
   */
  @Override
  public SimpleCron put(Schedule s, Runnable r) {
    if(s != null && r != null && s.isValid()) {
      if(s.getCountdown() < 0) s.reeschedule();
      Pair p = new Pair(s, JobFactory.create(r));
      jobs.list().put(p);
      jobs.sort();
      if(logEnabled) log.debug(
          "Job inserted: " + p.toString());
      if(!isRunning()) start();
    }
    return this;
  }
  
  
  /**
   * Inicia a excução de <code>SimpleCron</code>.
   * @return Esta instância modificada de <code>SimpleCron</code>.
   */
  @Override
  public SimpleCron start() {
    scheduler = new Scheduler();
    log.info("SimpleCron Started!");
    scheduler.start();
    return this;
  }
  
  
  /**
   * Interrompe a execução de <code>SimpleCron</code>.
   * @return Esta instância modificada de <code>SimpleCron</code>.
   */
  @Override
  public SimpleCron stop() {
    if(isRunning()) {
      scheduler.stop();
      scheduler = null;
      log.info("SimpleCron Stopped!");
    }
    return this;
  }
  
  
  /**
   * Verifica se <code>SimpleCron</code> está em execução.
   * @return <code>true</code> se <code>SimpleCron</code> está 
   * em execução, <code>false</code> caso contrário.
   */
  @Override
  public boolean isRunning() {
    return scheduler != null 
        && scheduler.isRunning();
  }
  
  
  /**
   * Scheduler é o <code>Thread</code> interno
   * de <code>SimpleCron</code>, responsável por verificar,
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
        Pair pair = jobs.list().peek();
        
        if(jobs.list().isEmpty()) {
          if(shutdownAtEmpty)
            SimpleCron.this.stop();
          else
            delay(GET_DELAY);
        }
        
        else if(!pair.schedule().isValid()) {
          jobs.list().remove(pair);
          log.debug("Schedule "
              + "Removed: " + pair.toString());
        }
        
        else if(pair.schedule()
            .isCountdownBeteewn(0, MIN_DELAY)) {
          
          jobs.list().remove(pair);
          
          
          if(pair.schedule().isRepeatEnabled()) {
            Schedule s = pair.schedule().clone();
            s.reeschedule();
            jobs.list().put(new Pair(s, pair.job()));
            //pair.schedule().reeschedule();
            jobs.sort();
          }
          execJob(pair);
        }
        
        else if(pair.schedule().getCountdown() < 0) {
          pair.schedule().reeschedule();
        }
        
        else {
          delay(GET_DELAY);
        }
      }
    }
    
    private void execJob(Pair pair) {
      delay(pair.schedule().getCountdown());
      try {
        pair.job().execute(new ExecutionContext(
            pair.schedule(), dataMap));
      } catch(Exception e) {
        pair.job().error(e);
      }
    }
    
    /**
     * Interrompe a execução pelo tempo em 
     * informado em milisegundos.
     * @param millis Tempo em milisegundos.
     */
    public void delay(long millis) {
      if(millis <= 0) return;
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
