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

package us.pserver.scronV6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import us.pserver.log.LogFactory;
import us.pserver.log.SLogV2;

/**
 * <b>SCronV6</b> é o motor de execução dos trabalhos
 * agendados. É configurado por padrão
 * com 3 Threads executantes, uma responsável por monitorar
 * e agendar os trabalhos e outras duas <code>Threads</code> 
 * responsáveis pela execução dos <code>Jobs</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/04/2014
 */
public class SCronV6 {

  /**
   * Tempo máximo em milisegundos <code>(600)</code> 
   * de espera pelo <code>Executor</code> 
   * até o momento da execução.
   */
  public static final int MIN_DELAY = 600;
  
  /**
   * Tempo em milisegundos <code>(200)</code> 
   * para verificação dos agendamentos.
   */
  public static final int GET_DELAY = 200;
  
  /**
   * Quantidade padrão <code>(2)</code> 
   * de <code>Threads</code> que executarão os <code>Jobs</code>.
   */
  public static final int DEFAULT_THREADS = 2;
  
  /**
   * Mapa de armazenamento <code>DataMap</code> único para todas as intâncias.
   */
  public static final DataMap dataMap = new DataMap();
  
  /**
   * Chave <code>String ("SLogV2")</code> para 
   * recuperação do mecanismo padrão de log.
   */
  public static final String LOGGER = "SLogV2";
  
  /**
   * Nome do arquivo de log <code>("scronv6.log")</code> 
   * utilizado por SCronV6.
   */
  public static final String LOGFILE = "scronv6.log";

  
  private final JobList jobs;
  
  private ExecutorService exes;
  
  private boolean shutdownAtEmpty;
  
  private int threads;
  
  private final Scheduler scheduler;
  
  private SLogV2 log;
  
  private boolean logEnabled;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public SCronV6() {
    jobs = new JobList();
    threads = DEFAULT_THREADS;
    scheduler = new Scheduler();
    shutdownAtEmpty = false;
    logEnabled = true;
    log = LogFactory.instance()
        .createDefault(LOGFILE);
    dataMap.put(LOGGER, log)
        .put(getClass().getSimpleName(), this);
  }
  
  
  /**
   * Agenda uma tarefa para execução, iniciando 
   * <code>SCronV6</code> caso ainda não esteja executando.
   * @param s Agendamento <code>Schedule</code> da tarefa.
   * @param j Tarefa <code>Job</code> a ser executada.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  public SCronV6 put(Schedule s, Job j) {
    if(s != null && j != null) {
      Pair p = new Pair(s, j);
      jobs.put(p).sort();
      if(logEnabled) log.debug(
          "Job inserted: " + p.toString());
      if(!isRunning()) start();
    }
    return this;
  }
  
  
  /**
   * Agenda um <code>Runnable</code> para execução, iniciando 
   * <code>SCronV6</code> caso ainda não esteja executando.
   * @param s Agendamento <code>Schedule</code> da tarefa.
   * @param r Tarefa <code>Runnable</code> a ser executada.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  public SCronV6 put(Schedule s, Runnable r) {
    if(s != null && r != null) {
      Pair p = new Pair(s, JobFactory.create(r));
      jobs.put(p).sort();
      if(logEnabled) log.debug(
          "Job inserted: " + p.toString());
      if(!isRunning()) start();
    }
    return this;
  }
  
  
  /**
   * Retorna o logger padrão de <code>SCronV6</code>.
   * @return <code>com.jpower.log.Logger</code>
   */
  public SLogV2 getLogger() {
    return log;
  }
  
  
  /**
   * Define o logger padrão de <code>SCronV6</code>.
   * @param l <code>com.jpower.log.Logger</code>
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  public SCronV6 setLogger(SLogV2 l) {
    if(l != null) log = l;
    return this;
  }
  
  
  /**
   * Verifica se o log automático de mensagens de 
   * <code>SCronV6</code> está ativado.
   * @return <code>true</code> se <code>SCronV6</code> efetuará
   * log de mensages, <code>false</code> caso contrário.
   */
  public boolean isLogEnabled() {
    return logEnabled;
  }
  
  
  /**
   * Define se <code>SCronV6</code> efetuará log automático
   * de mensagens ou não.
   * @param bool <code>true</code> para que <code>SCronV6</code>
   * efetue log automático de mensagens, 
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  public SCronV6 setLogEnabled(boolean bool) {
    logEnabled = bool;
    return this;
  }
  
  
  /**
   * Verifica se a função "Desligar Quando Vazio" 
   * <code>(ShutdownAtEmpty)</code> está ativada (Desativada por padrão).
   * @return <code>true</code> se a função ShutdownAtEmpty
   * estiver ativada, <code>false</code> caso contrário.
   */
  public boolean isShutdownAtEmpty() {
    return shutdownAtEmpty;
  }
  
  
  /**
   * Define se a função "Desligar Quando Vazio"
   * <code>(ShutdownAtEmpty)</code> estará ativa ou 
   * não (Desativada por padrão).
   * @param bool <code>true</code> para ativar função 
   * ShutdownAtEmpty, <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  public SCronV6 setShutdownAtEmpty(boolean bool) {
    shutdownAtEmpty = bool;
    return this;
  }
  
  
  /**
   * Retorna a quantidade de <code>Threads</code>
   * responsáveis pela execução dos <code>Jobs</code>.
   * @return quantidade de <code>Threads</code>.
   */
  public int getAvailableThreads() {
    return threads;
  }
  
  
  /**
   * Define a quantidade de <code>Threads</code>
   * responsáveis pela execução dos <code>Jobs</code>.
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
  public SCronV6 start() {
    exes = Executors.newFixedThreadPool(threads);
    log.info("SCronV6 Started!");
    scheduler.start();
    return this;
  }
  
  
  /**
   * Interrompe a execução de <code>SCronV6</code>.
   * @return Esta instância modificada de <code>SCronV6</code>.
   */
  public SCronV6 stop() {
    scheduler.stop();
    exes.shutdown();
    exes = null;
    log.info("SCronV6 Stopped!");
    return this;
  }
  
  
  /**
   * Verifica se <code>SCronV6</code> está em execução.
   * @return <code>true</code> se <code>SCronV6</code> está 
   * em execução, <code>false</code> caso contrário.
   */
  public boolean isRunning() {
    return scheduler.isRunning() 
        && exes != null;
  }
  
  
  /**
   * Retorna a lista <code>JobList</code>
   * utilizada por <code>SCronV6</code>.
   * @return <code>JobList</code>.
   * @see us.pserver.scronV6.JobList
   */
  public JobList jobList() {
    return jobs;
  }
  
  
  /**
   * Retorna o mapa de dados <code>DataMap</code>
   * utilizado por <code>SCronV6</code>.
   * @return <code>DataMap</code>.
   * @see us.pserver.scronV6.DataMap
   */
  public DataMap dataMap() {
    return dataMap;
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
        if(jobs.isEmpty()) {
          if(shutdownAtEmpty)
            SCronV6.this.stop();
          else
            delay(GET_DELAY);
        }
        else if(jobs.peek().schedule()
            .isCountdownBeteewn(0, MIN_DELAY)) {
          
          Pair p = jobs.pop();
          if(p.schedule().isValid()) {
            exes.submit(new Executor(p, 
                new ExecutionContext(p.schedule(), dataMap)));
          }
          
          if(p.schedule().isRepeatEnabled()) {
            Schedule s = p.schedule().clone();
            s.reeschedule();
            jobs.put(new Pair(s, p.job())).sort();
          }
        }
        else if(!jobs.peek().schedule().isValid()
            && !jobs.peek().schedule().isRepeatEnabled()) {
          Pair p = jobs.pop();
          log.debug("Schedule "
              + "Removed: " + p.toString());
          jobs.sort();
        }
        else {
          delay(GET_DELAY);
        }
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
