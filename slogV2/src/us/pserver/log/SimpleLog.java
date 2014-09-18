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

import java.util.LinkedList;
import java.util.List;

/**
 * <b>SimpleLog</b> é uma versão alternativa à 
 * <code>SLogV2</code>, que executa suas
 * funções utilizando apenas a <code>Thread</code>
 * atual.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 14/04/2014
 * @see us.pserver.log.SLogV2
 */
public class SimpleLog implements Log {
  
  private List<LogOutput> outs;
  
  
  /**
   * Construtor padrão e sem argumentos,
   * cria uma instância de <code>SimpleLog</code> com configurações
   * padrão, pronto para ser utilizado.
   */
  public SimpleLog() {
    outs = new LinkedList();
    new SimpleLogFactory(this)
        .createDefault();
  }
  
  
  /**
   * Construtor que recebe o nome do arquivo de log,
   * cria uma instância de <code>SimpleLog</code> com configurações
   * padrão, pronto para ser utilizado.
   * @param logfile nome do arquivo de log.
   */
  public SimpleLog(String logfile) {
    if(logfile == null)
      throw new IllegalArgumentException(
          "Invalid File: "+ logfile);
    outs = new LinkedList();
    new SimpleLogFactory(this)
        .createDefault(logfile);
  }
  
  
  /**
   * Redefine todas as configurações de <code>SimpleLog</code>.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog reset() {
    this.close();
    outs.clear();
    return this;
  }
  
  
  /**
   * Adiciona uma saída de log (<code>LogOutput</code>).
   * @param out <code>LogOutput</code>.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog add(LogOutput out) {
    if(out != null)
      outs.add(out);
    return this;
  }
  
  
  /**
   * Exclui todas as saídas de log (<code>LogOutput</code>).
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog clearOutputs() {
    outs.clear();
    return this;
  }
  
  
  /**
   * Retorna uma lista com todos os <code>LogOutput</code>.
   * @return <code>List&lt;LogOutput&gt;</code>
   */
  @Override
  public List<LogOutput> outputs() {
    return outs;
  }
  
  
  /**
   * Define a lista com todos os <code>LogOutput</code>.
   * @param lo <code>List&lt;LogOutput&gt;</code>
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  public SimpleLog outputs(List<LogOutput> lo) {
    if(lo == null)
      throw new IllegalArgumentException(
          "Invalid LogOutput List: "+ lo);
    outs = lo;
    return this;
  }
  
  
  /**
   * Redefine o formatador de log <code>OutputFormatter</code>
   * de todos os <code>LogOutput</code> adicionados em <code>SimpleLog</code>.
   * @param lf Formatador de saída de log <code>OutputFormatter</code>.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog formatter(OutputFormatter lf) {
    if(lf == null)
      throw new IllegalArgumentException(
          "Invalid LogFormatter: "+ lf);
    if(!outs.isEmpty()) {
      outs.stream().forEach(o->o.formatter(lf));
    }
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de log de debug 
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de debug em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog debug(boolean bool) {
    outs.stream().forEach(o->o.debug(bool));
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de log de informação 
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de informação em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog info(boolean bool) {
    outs.stream().forEach(o->o.info(bool));
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de log de alerta
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de alerta em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog warning(boolean bool) {
    outs.stream().forEach(o->o.warning(bool));
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de log de erro
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de erro em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog error(boolean bool) {
    outs.stream().forEach(o->o.error(bool));
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de log de erro fatal
   * de todos os <code>LogOutput</code> adicionados.
   * @param bool <code>true</code> para habilitar
   * o nível de log de erro fatal em todos os 
   * <code>LogOutput</code>, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog fatal(boolean bool) {
    outs.stream().forEach(o->o.fatal(bool));
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível debug 
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog debug(String msg) {
    log(msg, LogLevel.DEBUG);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível informação
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog info(String msg) {
    log(msg, LogLevel.INFO);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível alerta
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog warning(String msg) {
    log(msg, LogLevel.WARN);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível erro
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog error(String msg) {
    log(msg, LogLevel.ERROR);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível erro fatal
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog fatal(String msg) {
    log(msg, LogLevel.FATAL);
    return this;
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível debug.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  public SimpleLog debug(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.DEBUG, printStackTrace);
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível info.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  public SimpleLog info(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.INFO, printStackTrace);
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível warning.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  public SimpleLog warning(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.WARN, printStackTrace);
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível error.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  public SimpleLog error(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.ERROR, printStackTrace);
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível fatal.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  public SimpleLog fatal(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.FATAL, printStackTrace);
  }
  
  
  /**
   * Direciona a mensagem, referente ao erro 
   * <code>Throwable</code> informado para todas as 
   * saídas de log configuradas.
   * @param th Exceção.
   * @param level Nível da mensagem de log.
   * @param printStackTrace Define se a pilha de rastreamento 
   * do erro será direcionada ao log ou não.
   * @return Esta intância modificada de <code>SimpleLog</code>.
   */
  public SimpleLog log(Throwable th, LogLevel level, boolean printStackTrace) {
    if(th == null) return this;
    StringBuffer msg = new StringBuffer();
    msg.append(th.getClass().getSimpleName())
        .append(": ")
        .append(th.getMessage());
    outs.forEach(o->o.log(msg.toString(), level));
    
    Throwable cause = th.getCause();
    if(cause != null) {
      msg.delete(0, msg.length());
      msg.append("  Caused by: ")
          .append(cause.getClass().getSimpleName())
          .append(": ")
          .append(cause.getMessage());
      outs.forEach(o->o.log(msg.toString(), level));
    }
    
    if(printStackTrace) {
      StackTraceElement[] stack = th.getStackTrace();
      for(StackTraceElement se : stack) {
        outs.forEach(o->o.log("  "+ se.toString(), level));
      }
    }
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log com o nível informado
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @param level Nível da mensagem de log.
   * @return Esta intância modificada de LogOutput.
   */
  @Override
  public SimpleLog log(String msg, LogLevel level) {
    outs.forEach(o->o.log(msg, level));
    return this;
  }
  
  
  /**
   * Fecha todas as saídas de log <code>LogOutput</code>.
   * @return Esta instância modificada de <code>SimpleLog</code>.
   */
  @Override
  public SimpleLog close() {
    outs.forEach(o->o.close());
    return this;
  }
  
}