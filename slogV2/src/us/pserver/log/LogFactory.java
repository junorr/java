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

import java.util.function.Consumer;

/**
 * Fábrica de objetos <code>SLogV2</code> com métodos funcionais.
 * <pre>
 * 
 * Exemplo de utilização:
 * SLogV2 log = LogFactory.instance()
 *    .newErrOutput()
 *    .enableErrorLevels()
 *    .formatter(
 *        OutputFormatter.errorFormatter())
 *    .add()
 *    .create();
 * </pre>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/04/2014
 * @see us.pserver.log.SLogV2
 * @see us.pserver.log.OutputFormatter
 */
public class LogFactory {
  
  /**
   * Nome padrão do arquivo de log <code>("default.log")</code>.
   */
  public static final String DEFAULT_LOG_FILE = "./default.log";

  private SLogV2 log;
  
  private LogOutput edit;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public LogFactory() {
    log = new SLogV2();
    edit = null;
  }
  
  
  /**
   * Construtor protegido que recebe a instância
   * de <code>SLogV2</code> a ser configurada.
   * @param sl Instância de <code>SLogV2</code> a ser configurada.
   */
  protected LogFactory(SLogV2 sl) {
    if(sl == null)
      throw new IllegalArgumentException(
          "Invalid SLogV2 instance: "+ sl);
    log = sl;
    edit = null;
  }
  
  
  /**
   * Obtém uma instância de <code>LogFactory</code>.
   * @return instância de <code>LogFactory</code>.
   */
  public static LogFactory instance() {
    return new LogFactory();
  }
  
  
  /**
   * Retorna a instância de <code>SLogV2</code> criada e configurada
   * por <code>LogFactory</code>.
   * @return <code>SLogV2</code>.
   */
  public SLogV2 create() {
    return log.start();
  }
  
  
  /**
   * Redefine as configurações para criação de <code>SLogV2</code>.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory reset() {
    log.reset();
    edit = null;
    return this;
  }
  
  
  /**
   * Cria uma instância pré-configurada de <code>SLogV2</code>.
   * @return <code>SLogV2</code>.
   */
  public SLogV2 createDefault() {
    return createDefault(DEFAULT_LOG_FILE);
  }
  
  
  /**
   * Cria uma instância pré-configurada de <code>SLogV2</code>,
   * com saída para o arquivo informado <code>logFile</code>.
   * @param logFile Nome do arquivo de log.
   * @return <code>SLogV2</code>
   */
  public SLogV2 createDefault(String logFile) {
    return this.reset()
        .newStdOutput()
        .enableNonErrorLevels()
        .debug(false)
        .add()
        
        .newErrOutput()
        .enableErrorLevels()
        .add()
        
        .newFileOutput(logFile)
        .enableAllLevels()
        .add()
        
        .create();
  }
  
  
  /**
   * Desabilita todos os níveis de log do
   * <code>LogOutput</code> configurado.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory disableAllLevels() {
    if(edit != null)
      edit.debug(false).info(false)
          .warning(false)
          .error(false).fatal(false);
    return this;
  }
  
  
  /**
   * Habilita todos os níveis de log do 
   * <code>LogOutput</code> configurado.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory enableAllLevels() {
    if(edit != null)
      edit.debug(true).info(true)
          .warning(true)
          .error(true).fatal(true);
    return this;
  }
  
  
  /**
   * Habilita os níveis de log, exceto níveis de erro do 
   * <code>LogOutput</code> configurado.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory enableNonErrorLevels() {
    if(edit != null)
      edit.debug(true).info(true)
          .warning(true)
          .error(false).fatal(false);
    return this;
  }
  
  
  /**
   * Habilita todos os níveis de erros de log do 
   * <code>LogOutput</code> configurado.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory enableErrorLevels() {
    if(edit != null)
      edit.debug(false).info(false)
          .warning(false)
          .error(true).fatal(true);
    return this;
  }
  
  
  /**
   * Cria uma nova instância de <code>LogOutput</code> 
   * com saída configurada para a saída padrão.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory newStdOutput() {
    edit = new LogOutput();
    return this;
  }
  
  
  /**
   * Cria uma nova instância de <code>LogOutput</code> 
   * com saída configurada para a saída de erros padrão.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory newErrOutput() {
    edit = new LogOutput().setErrOutput();
    return this;
  }
  
  
  /**
   * Cria uma nova instância de <code>LogOutput</code> 
   * com saída configurada para o arquivo de log informado.
   * @param file Nome do arquivo de log.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory newFileOutput(String file) {
    edit = new LogOutput().setFileOutput(file);
    return this;
  }
  
  
  /**
   * Cria uma nova instância de <code>LogOutput</code> 
   * com saída configurada para o objeto 
   * <code>Consumer&lt;Character&gt;</code>.
   * @param cs <code>Consumer&lt;Character&gt;</code>.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory newStreamOutput(Consumer<Character> cs) {
    edit = StreamFactory.createLogOutput(cs);
    return this;
  }
  
  
  /**
   * Define o identificador único do <code>LogOutput</code> configurado.
   * @param uid Identificador único.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory uid(long uid) {
    if(edit != null)
      edit.uid(uid);
    return this;
  }
  
  
  /**
   * Retorna o identificador único do <code>LogOutput</code> configurado.
   * @return identificador único do <code>LogOutput</code> configurado.
   */
  public long uid() {
    if(edit != null)
      return edit.uid();
    return -1;
  }
  
  
  /**
   * Retorna o <code>LogOutput</code> configurado.
   * @return <code>LogOutput</code>.
   */
  public LogOutput get() {
    return edit;
  }
  
  
  /**
   * Define o formatador de log <code>OutputFormatter</code>
   * do <code>LogOutput</code> configurado.
   * @param lf Formatador de saída de log <code>OutputFormatter</code>.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory formatter(OutputFormatter lf) {
    if(edit != null)
      edit.formatter(lf);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de debug do <code>LogOutput</code> 
   * configurado.
   * @param bool <code>true</code> para habilitar 
   * o nível de debug do <code>LogOutput</code> configurado,
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory debug(boolean bool) {
    if(edit != null)
      edit.debug(bool);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de informação do <code>LogOutput</code> 
   * configurado.
   * @param bool <code>true</code> para habilitar 
   * o nível de info do <code>LogOutput</code> configurado,
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory info(boolean bool) {
    if(edit != null)
      edit.info(bool);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de alerta do <code>LogOutput</code> 
   * configurado.
   * @param bool <code>true</code> para habilitar 
   * o nível de warning do <code>LogOutput</code> configurado,
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory warning(boolean bool) {
    if(edit != null)
      edit.warning(bool);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de erro do <code>LogOutput</code> 
   * configurado.
   * @param bool <code>true</code> para habilitar 
   * o nível de error do <code>LogOutput</code> configurado,
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory error(boolean bool) {
    if(edit != null)
      edit.error(bool);
    return this;
  }
  
  
  /**
   * Habilita/Desabilita o nível de erro fatal do <code>LogOutput</code> 
   * configurado.
   * @param bool <code>true</code> para habilitar 
   * o nível de fatal do <code>LogOutput</code> configurado,
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory fatal(boolean bool) {
    if(edit != null)
      edit.fatal(bool);
    return this;
  }
  
  
  /**
   * Adiciona o <code>LogOutput</code> configurado a <code>SLogV2</code>.
   * @return Esta instância modificada de <code>LogFactory</code>.
   */
  public LogFactory add() {
    if(edit != null) {
      log.add(edit);
      edit = null;
    }
    return this;
  }
  
}