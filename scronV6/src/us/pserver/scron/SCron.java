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

import us.pserver.scronv6.repeat.JobsManager;
import us.pserver.scronv6.repeat.Pair;
import java.util.List;
import us.pserver.log.LogHelper;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/05/2014
 */
public interface SCron {

  /**
   * Chave <code>String ("us.pserver.scron.SCron")</code> para 
   * recuperação desta instância de <code>SCron</code>.
   */
  public static final String KEY_SCRON = "us.pserver.scron.SCron";
  
  
  /**
   * Agenda uma tarefa para execução, iniciando 
   * <code>SimpleCron</code> caso ainda não esteja executando.
   * @param sch Agendamento <code>Schedule</code> da tarefa.
   * @param job Tarefa <code>Job</code> a ser executada.
   * @return Esta instância modificada de <code>SCron</code>.
   */
  public SCron put(Schedule sch, Job job);
  
  /**
   * Agenda um <code>Runnable</code> para execução, iniciando 
   * <code>SimpleCron</code> caso ainda não esteja executando.
   * @param sch Agendamento <code>Schedule</code> da tarefa.
   * @param run Tarefa <code>Runnable</code> a ser executada.
   * @return Esta instância modificada de <code>SCron</code>.
   */
  public SCron put(Schedule sch, Runnable run);
  
  /**
   * Retorna o logger padrão de <code>SimpleCron</code>.
   * @return <code>com.jpower.log.Logger</code>
   */
  public LogHelper getLogger();
  
  /**
   * Define o logger padrão de <code>SimpleCron</code>.
   * @param l <code>com.jpower.log.Logger</code>
   * @return Esta instância modificada de <code>SCron</code>.
   */
  public SCron setLogger(LogHelper l);
  
  /**
   * Verifica se o log automático de mensagens de 
   * <code>SimpleCron</code> está ativado.
   * @return <code>true</code> se <code>SimpleCron</code> efetuará
   * log de mensages, <code>false</code> caso contrário.
   */
  public boolean isLogEnabled();
  
  /**
   * Define se <code>SimpleCron</code> efetuará log automático
   * de mensagens ou não.
   * @param bool <code>true</code> para que <code>SCron</code>
   * efetue log automático de mensagens, 
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>SimpleCron</code>.
   */
  public SCron setLogEnabled(boolean bool);
  
  /**
   * Verifica se a função "Desligar Quando Vazio" 
   * <code>(ShutdownAtEmpty)</code> está ativada (Desativada por padrão).
   * @return <code>true</code> se a função ShutdownAtEmpty
   * estiver ativada, <code>false</code> caso contrário.
   */
  public boolean isShutdownAtEmpty();
  
  /**
   * Define se a função "Desligar Quando Vazio"
   * <code>(ShutdownAtEmpty)</code> estará ativa ou 
   * não (Desativada por padrão).
   * @param bool <code>true</code> para ativar função 
   * ShutdownAtEmpty, <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>SimpleCron</code>.
   */
  public SCron setShutdownAtEmpty(boolean bool);
  
  /**
   * Inicia a excução de <code>SimpleCron</code>.
   * @return Esta instância modificada de <code>SCron</code>.
   */
  public SCron start();
  
  /**
   * Interrompe a execução de <code>SimpleCron</code>.
   * @return Esta instância modificada de <code>SCron</code>.
   */
  public SCron stop();
  
  /**
   * Verifica se <code>SimpleCron</code> está em execução.
   * @return <code>true</code> se <code>SCron</code> está 
   * em execução, <code>false</code> caso contrário.
   */
  public boolean isRunning();
  
  /**
   * Retorna <code>JobsManager</code>
   * utilizado para organizar os agendamentos.
   * @return <code>JobsManager</code>.
   * @see us.pserver.scronv6.repeat.JobsManager
   */
  public JobsManager manager();
  
  /**
   * Retorna a lista de trabalhos
   * <code>ExclusiveList&lt;Pair&gt;</code>.
   * @return <code>ExclusiveList&lt;Pair&gt;</code>.
   */
  public List<Pair> jobs();
  
  /**
   * Retorna o mapa de dados <code>DataMap</code>
   * utilizado por <code>SCron</code>.
   * @return <code>DataMap</code>.
   * @see us.pserver.scron.DataMap
   */
  public DataMap dataMap();
  
}
