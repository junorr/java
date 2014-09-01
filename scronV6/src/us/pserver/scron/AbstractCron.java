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

import us.pserver.scronv6.hide.JobsManager;
import us.pserver.scronv6.hide.Pair;
import us.pserver.conc.ExclusiveList;
import us.pserver.log.Log;
import us.pserver.log.LogProvider;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/05/2014
 */
public abstract class AbstractCron implements SCron {
  
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
   * Mapa de armazenamento <code>DataMap</code> único para todas as intâncias.
   */
  public static final DataMap dataMap = new DataMap();
  
  /**
   * Chave <code>String ("us.pserver.log.Log")</code> para 
   * recuperação do mecanismo padrão de log.
   */
  public static final String KEY_LOGGER = "us.pserver.log.Log";
  
  
  JobsManager jobs;
  
  boolean shutdownAtEmpty;
  
  Log log;
  
  boolean logEnabled;
  
  
  /**
   * Construtor que recebe o objeto de log padrão.
   * @param l objeto de log padrão.
   */
  public AbstractCron(Log l) {
    jobs = new JobsManager();
    shutdownAtEmpty = false;
    logEnabled = true;
    if(l == null) l = LogProvider.getSimpleLog();
    log = l;
    dataMap.put(KEY_LOGGER, log)
        .put(getClass().getSimpleName(), this);
  }
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public AbstractCron() {
    this(null);
  }
  
  
  /**
   * Retorna o logger padrão de <code>SimpleCron</code>.
   * @return <code>com.jpower.log.Logger</code>
   */
  @Override
  public Log getLogger() {
    return log;
  }
  
  
  /**
   * Define o logger padrão de <code>SimpleCron</code>.
   * @param l <code>com.jpower.log.Logger</code>
   * @return Esta instância modificada de <code>SCron</code>.
   */
  @Override
  public SCron setLogger(Log l) {
    if(l != null) log = l;
    return this;
  }
  
  
  /**
   * Verifica se o log automático de mensagens de 
   * <code>SimpleCron</code> está ativado.
   * @return <code>true</code> se <code>SimpleCron</code> efetuará
   * log de mensages, <code>false</code> caso contrário.
   */
  @Override
  public boolean isLogEnabled() {
    return logEnabled;
  }
  
  
  /**
   * Define se <code>SimpleCron</code> efetuará log automático
   * de mensagens ou não.
   * @param bool <code>true</code> para que <code>SimpleCron</code>
   * efetue log automático de mensagens, 
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>SCron</code>.
   */
  @Override
  public SCron setLogEnabled(boolean bool) {
    logEnabled = bool;
    return this;
  }
  
  
  /**
   * Verifica se a função "Desligar Quando Vazio" 
   * <code>(ShutdownAtEmpty)</code> está ativada (Desativada por padrão).
   * @return <code>true</code> se a função ShutdownAtEmpty
   * estiver ativada, <code>false</code> caso contrário.
   */
  @Override
  public boolean isShutdownAtEmpty() {
    return shutdownAtEmpty;
  }
  
  
  /**
   * Define se a função "Desligar Quando Vazio"
   * <code>(ShutdownAtEmpty)</code> estará ativa ou 
   * não (Desativada por padrão).
   * @param bool <code>true</code> para ativar função 
   * ShutdownAtEmpty, <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>SCron</code>.
   */
  @Override
  public SCron setShutdownAtEmpty(boolean bool) {
    shutdownAtEmpty = bool;
    return this;
  }
  
  
  /**
   * Retorna <code>JobsManager</code>
   * utilizado para organizar os agendamentos.
   * @return <code>JobsManager</code>.
   * @see us.pserver.scronv6.hide.JobsManager
   */
  @Override
  public JobsManager manager() {
    return jobs;
  }
  
  
  /**
   * Retorna a lista de trabalhos
   * <code>ExclusiveList&lt;Pair&gt;</code>.
   * @return <code>ExclusiveList&lt;Pair&gt;</code>.
   */
  @Override
  public ExclusiveList<Pair> jobs() {
    return jobs.list();
  }
  
  
  /**
   * Retorna o mapa de dados <code>DataMap</code>
   * utilizado por <code>SCron</code>.
   * @return <code>DataMap</code>.
   * @see us.pserver.scron.DataMap
   */
  @Override
  public DataMap dataMap() {
    return dataMap;
  }
  
}
