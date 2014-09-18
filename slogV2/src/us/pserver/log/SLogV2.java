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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import us.pserver.conc.ExclusiveList;

/**
 * <b>SLogV2</b> é a classe responsável por
 * administrar todas as saídas, níveis 
 * e formatadores de log, utilizando 
 * Executores e <code>Threads</code> independentes 
 * para gerar mensagens de log em diferentes destinos, 
 * unindo robustês e performance em uma interface simples.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 14/04/2014
 */
public class SLogV2 implements Log {
  
  /**
   * Identificação do executor padrão de mensagens 
   * de log <code>(0)</code>.
   */
  public static final long DEFAULT_EXEC_UID = 0;

  
  private ExclusiveList<LogOutput> outs;
  
  private final Map<Long, ExecutorService> execs;
  
  
  /**
   * Construtor padrão e sem argumentos,
   * cria uma instância de <code>SLogV2</code> com configurações
   * padrão, pronto para ser utilizado.
   */
  public SLogV2() {
    outs = new ExclusiveList();
    execs = new TreeMap();
    new LogFactory(this)
        .createDefault();
  }
  
  
  /**
   * Construtor que recebe o nome do arquivo de log,
   * cria uma instância de <code>SLogV2</code> com configurações
   * padrão, pronto para ser utilizado.
   * @param logFile nome do arquivo de log.
   */
  public SLogV2(String logFile) {
    outs = new ExclusiveList();
    execs = new TreeMap();
    new LogFactory(this)
        .createDefault(logFile);
  }
  
  
  /**
   * Redefine todas as configurações de <code>SLogV2</code>,
   * interrompendo sua execução.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 reset() {
    this.stop();
    this.close();
    outs.clear();
    execs.clear();
    return this;
  }
  
  
  /**
   * Adiciona uma saída de log (<code>LogOutput</code>).
   * @param out <code>LogOutput</code>.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 add(LogOutput out) {
    if(out != null)
      outs.add(out);
    return this;
  }
  
  
  /**
   * Exclui todas as saídas de log (<code>LogOutput</code>).
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 clearOutputs() {
    outs.clear();
    return this;
  }
  
  
  /**
   * Retorna um mapa contendo como chave o número 
   * de identificação e como valor o executor 
   * responsável por saídas de log com UID igual à chave.
   * @return <code>Map&lt;Long, ExecutorService&gt;</code>
   * @see java.util.Map
   */
  public Map<Long, ExecutorService> executors() {
    return execs;
  }
  
  
  /**
   * Aloca uma nova <code>Thread</code> para saídas de log 
   * (<code>LogOutput</code>) configurados com 
   * o número de identificação (<code>UID</code>) 
   * fornecido.
   * @param uid Número de identificação (<code>UID</code>).
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 allocExecutorFor(long uid) {
    if(uid != DEFAULT_EXEC_UID)
      execs.put(uid, 
          Executors.newSingleThreadExecutor());
    return this;
  }
  
  
  /**
   * Aloca uma nova <code>Thread</code> para o objeto de saída de log 
   * (<code>LogOutput</code>) informado.
   * @param out saída de log (<code>LogOutput</code>).
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 allocExecutorFor(LogOutput out) {
    if(out != null && out.uid() != DEFAULT_EXEC_UID)
      execs.put(out.uid(), 
          Executors.newSingleThreadExecutor());
    return this;
  }
  
  
  /**
   * Aloca uma nova <code>Thread</code> para saídas de log em arquivo.
   * @return Esta instância modificada de <code>SLogV2</code>.
   * @see us.pserver.log.LogOutput#isFileOutput() 
   */
  public SLogV2 allocFileOutputExecutor() {
    long uid = System.currentTimeMillis() + 3000;
    outs.forEach(o->{
      if(o.isFileOutput())
        o.uid(uid);
    });
    execs.put(uid, Executors
        .newSingleThreadExecutor());
    return this;
  }
  
  
  /**
   * Inicia a execução de <code>SLogV2</code> e todos os executores 
   * configurados.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 start() {
    if(!isRunning()) {
      execs.put(DEFAULT_EXEC_UID, 
          Executors.newSingleThreadExecutor());
    }
    return this;
  }
  
  
  /**
   * Interrompe a execução de <code>SLogV2</code>, remove todos os executores
   * e fecha todos os <code>LogOutput</code> configurados.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 stop() {
    if(!execs.isEmpty()) {
      Collection<ExecutorService> col = execs.values();
      col.stream().forEach(this::shutdownAndWait);
      this.close();
      outs.clear();
      execs.clear();
    }
    return this;
  }
  
  
  private void shutdownAndWait(ExecutorService e) {
    try {
      e.shutdown();
      e.awaitTermination(5, TimeUnit.SECONDS);
    } catch(InterruptedException ex) {}
  }
  
  
  /**
   * Verifica se <code>SLogV2</code> está em execução.
   * @return <code>true</code> se <code>SLogV2</code>
   * está em execução, <code>false</code>
   * caso contrário.
   */
  public boolean isRunning() {
    return execs.get(DEFAULT_EXEC_UID) != null;
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
   * Redefine o formatador de log <code>OutputFormatter</code>
   * de todos os <code>LogOutput</code> adicionados em <code>SLogV2</code>.
   * @param lf Formatador de saída de log <code>OutputFormatter</code>.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 formatter(OutputFormatter lf) {
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
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 debug(boolean bool) {
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
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 info(boolean bool) {
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
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 warning(boolean bool) {
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
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 error(boolean bool) {
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
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 fatal(boolean bool) {
    outs.stream().forEach(o->o.fatal(bool));
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível debug 
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 debug(String msg) {
    outs.forEach(o->submit(o, msg, LogLevel.DEBUG));
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível informação
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 info(String msg) {
    outs.forEach(o->submit(o, msg, LogLevel.INFO));
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível alerta
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 warning(String msg) {
    outs.forEach(o->submit(o, msg, LogLevel.WARN));
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível erro
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 error(String msg) {
    outs.forEach(o->submit(o, msg, LogLevel.ERROR));
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível erro fatal
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 fatal(String msg) {
    outs.forEach(o->submit(o, msg, LogLevel.FATAL));
    return this;
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível debug.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 debug(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.DEBUG, printStackTrace);
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível info.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 info(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.INFO, printStackTrace);
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível warning.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 warning(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.WARN, printStackTrace);
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível error.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 error(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.ERROR, printStackTrace);
  }
  
  
  /**
   * Direciona o erro <code>Throwable</code> para log nível fatal.
   * @param th Exceção.
   * @param printStackTrace Define se a pilha de rastreamento do
   * erro será impressa em log ou não.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  public SLogV2 fatal(Throwable th, boolean printStackTrace) {
    return this.log(th, LogLevel.FATAL, printStackTrace);
  }
  
  
  /**
   * Direciona a mensagem de log com o nível informado
   * para todas as saídas configuradas.
   * @param msg Mensagem de log.
   * @param level Nível da mensagem de log.
   * @return Esta intância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 log(String msg, LogLevel level) {
    outs.forEach(o->submit(o, msg, level));
    return this;
  }
  
  
  /**
   * Direciona a mensagem, referente ao erro 
   * <code>Throwable</code> informado para todas as 
   * saídas de log configuradas.
   * @param th Exceção.
   * @param level Nível da mensagem de log.
   * @param stackTrace Define se a pilha de rastreamento 
   * do erro será direcionada ao log ou não.
   * @return Esta intância modificada de <code>SLogV2</code>.
   */
  public SLogV2 log(Throwable th, LogLevel level, boolean stackTrace) {
    if(th == null) return this;
    StringBuffer msg = new StringBuffer();
    msg.append(th.getClass().getSimpleName())
        .append(": ")
        .append(th.getMessage());
    outs.forEach(o->submit(o, msg.toString(), level));
    
    Throwable cause = th.getCause();
    if(cause != null) {
      msg.delete(0, msg.length());
      msg.append("  Caused by: ")
          .append(cause.getClass().getSimpleName())
          .append(": ")
          .append(cause.getMessage());
      outs.forEach(o->submit(o, msg.toString(), level));
    }
    
    if(stackTrace) {
      StackTraceElement[] stack = th.getStackTrace();
      for(StackTraceElement se : stack) {
        outs.forEach(o->submit(o, "  "+ se.toString(), level));
      }
    }
    return this;
  }
  
  
  /**
   * Fecha todas as saídas de log <code>LogOutput</code>.
   * @return Esta instância modificada de <code>SLogV2</code>.
   */
  @Override
  public SLogV2 close() {
    outs.forEach(o->o.close());
    return this;
  }
  
  
  /**
   * Submete para execução a saída de log informada.
   * @param out <code>LogOutput</code>.
   * @param msg Mensagem de log.
   * @param lvl Nível de log.
   */
  private void submit(LogOutput out, String msg, LogLevel lvl) {
    if(out == null || lvl == null || msg == null)
      return;
    
    if(!isRunning()) this.start();
    
    ExecutorService es = execs.get(DEFAULT_EXEC_UID);
    if(execs.containsKey(out.uid())) {
      es = execs.get(out.uid());
    }
    es.submit(new OutputRunner(out, msg, lvl));
  }
  
}