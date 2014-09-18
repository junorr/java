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

package us.pserver.log;

import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/05/2014
 */
public interface Log {

  /**
   * Redefine todas as configurações de <code>Log</code>.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log reset();
  
  /**
   * Adiciona uma saída de log (<code>LogOutput</code>).
   * @param out <code>LogOutput</code>.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log add(LogOutput out);
  
  /**
   * Exclui todas as saídas de log (<code>LogOutput</code>).
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log clearOutputs();
  
  /**
   * Retorna uma lista com todos os <code>LogOutput</code>.
   * @return <code>List&lt;LogOutput&gt;</code>
   */
  public List<LogOutput> outputs();
  
  /**
   * Define o formatador de log <code>OutputFormatter</code>
   * de todos os <code>LogOutput</code> adicionados em <code>SLogV2</code>.
   * @param fmt Formatador de saída de log <code>OutputFormatter</code>.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log formatter(OutputFormatter fmt);
  
  /**
   * Habilita/Desabilita o nível de log de debug 
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de debug em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log debug(boolean bool);
  
  /**
   * Direciona a mensagem de log nível debug 
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log debug(String msg);
  
  /**
   * Habilita/Desabilita o nível de log de informação 
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de informação em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log info(boolean bool);
  
  /**
   * Direciona a mensagem de log nível de informação 
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log info(String msg);
  
  /**
   * Habilita/Desabilita o nível de log de alerta 
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de alerta em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log warning(boolean bool);
  
  /**
   * Direciona a mensagem de log nível alerta
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log warning(String msg);
  
  /**
   * Habilita/Desabilita o nível de log de erro 
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de erro em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log error(boolean bool);
  
  /**
   * Direciona a mensagem de log nível erro
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log error(String msg);
  
  /**
   * Habilita/Desabilita o nível de log de erro fatal 
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de erro fatal em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log fatal(boolean bool);
  
  /**
   * Direciona a mensagem de log nível erro fatal
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log fatal(String msg);
  
  /**
   * Direciona a mensagem de log com o nível informado
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @param lvl Nível da mensagem de log.
   * @return Esta intância modificada de <code>Log</code>.
   */
  public Log log(String msg, LogLevel lvl);
  
  /**
   * Fecha todas as saídas de log <code>LogOutput</code>.
   * @return Esta instância modificada de <code>Log</code>.
   */
  public Log close();
  
}
