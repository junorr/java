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
 * Fábrica de objetos <code>SimpleLog</code> com métodos funcionais.
 * <pre>
 
 Exemplo de utilização:
 SimpleLog log = SimpleLogFactory.instance()
    .newErrOutput()
    .enableErrorLevels()
    .formatter(OutputFormatter
        .errorFormatter())
    .add()
    .create();
 </pre>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/04/2014
 * @see us.pserver.log.SimpleLog
 * @see us.pserver.log.BasicOutputFormatter
 */
public class SimpleLogFactory {
  
  /**
   * Nome padrão do arquivo de log <code>("default.log")</code>.
   */
  public static final String DEFAULT_LOG_FILE = "./default.log";

  private SimpleLog log;
  
  private BasicLogOutput edit;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public SimpleLogFactory() {
    log = new SimpleLog();
    edit = null;
  }
  
  
  /**
   * Construtor protegido que recebe a instância
   * de <code>SimpleLog</code> a ser configurada.
   * @param sl Instância de <code>SimpleLog</code> 
   * a ser configurada.
   */
  protected SimpleLogFactory(SimpleLog sl) {
    if(sl == null)
      throw new IllegalArgumentException(
          "Invalid SimpleLog instance: "+ sl);
    log = sl;
    edit = null;
  }
  
  
  /**
   * Obtém uma instância de <code>SimpleLogFactory</code>.
   * @return instância de <code>SimpleLogFactory</code>.
   */
  public static SimpleLogFactory instance() {
    return new SimpleLogFactory();
  }
  
  
  /**
   * Retorna a instância de <code>SimpleLog</code> 
   * criada e configurada
   * por <code>SimpleLogFactory</code>.
   * @return <code>SimpleLog</code>.
   */
  public SimpleLog create() {
    return log;
  }
  
  
  /**
   * Redefine as configurações para criação de 
   * <code>SimpleLog</code>.
   * @return Esta instância modificada de 
   * <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory reset() {
    log.reset();
    edit = null;
    return this;
  }
  
  
  /**
   * Cria uma instância pré-configurada de <code>SimpleLog</code>.
   * @return <code>SimpleLog</code>.
   */
  public SimpleLog createDefault() {
    return this.reset()
        .newStdOutput()
        .enableNonErrorLevels()
        .add()
        
        .newErrOutput()
        .enableErrorLevels()
        .add()
        
        .create();
  }
  
  
  /**
   * Cria uma instância pré-configurada de <code>SimpleLog</code>,
   * com saída para o arquivo informado <code>logfile</code>.
   * @param logfile Nome do arquivo de log.
   * @return <code>SimpleLog</code>
   */
  public SimpleLog createDefault(String logfile) {
    return this.reset()
        .newStdOutput()
        .enableNonErrorLevels()
        .debug(false)
        .add()
        
        .newErrOutput()
        .enableErrorLevels()
        .add()
        
        .newFileOutput(logfile)
        .enableAllLevels()
        .add()
        
        .create();
  }
  
  
  /**
   * Desabilita todos os níveis de log do
   * <code>LogOutput</code> configurado.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory disableAllLevels() {
    if(edit != null)
      edit.debug(false).info(false)
          .warning(false)
          .error(false).fatal(false);
    return this;
  }
  
  
  /**
   * Habilita todos os níveis de log do 
   * <code>LogOutput</code> configurado.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory enableAllLevels() {
    if(edit != null)
      edit.debug(true).info(true)
          .warning(true)
          .error(true).fatal(true);
    return this;
  }
  
  
  /**
   * Habilita os níveis de log, exceto níveis de erro do 
   * <code>LogOutput</code> configurado.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory enableNonErrorLevels() {
    if(edit != null)
      edit.debug(true).info(true)
          .warning(true)
          .error(false).fatal(false);
    return this;
  }
  
  
  /**
   * Habilita todos os níveis de erros de log do 
   * <code>LogOutput</code> configurado.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory enableErrorLevels() {
    if(edit != null)
      edit.debug(false).info(false)
          .warning(false)
          .error(true).fatal(true);
    return this;
  }
  
  
  /**
   * Cria uma nova instância de <code>LogOutput</code> 
   * com saída configurada para a saída padrão.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory newStdOutput() {
    edit = new BasicLogOutput();
    return this;
  }
  
  
  /**
   * Cria uma nova instância de <code>LogOutput</code> 
   * com saída configurada para a saída de erros padrão.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory newErrOutput() {
    edit = new BasicLogOutput().setErrOutput();
    return this;
  }
  
  
  /**
   * Cria uma nova instância de <code>LogOutput</code> 
   * com saída configurada para o arquivo de log informado.
   * @param file Nome do arquivo de log.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory newFileOutput(String file) {
    edit = new BasicLogOutput().setFileOutput(file);
    return this;
  }
  
  
  /**
   * Cria uma nova instância de <code>LogOutput</code> 
   * com saída configurada para o objeto 
   * <code>Consumer&lt;Character&gt;</code>.
   * @param cs <code>Consumer&lt;Character&gt;</code>.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory newStreamOutput(Consumer<Character> cs) {
    edit = StreamFactory.createLogOutput(cs);
    return this;
  }
  
  
  /**
   * Define o identificador único do <code>LogOutput</code> configurado.
   * @param uid Identificador único.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory uid(long uid) {
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
  public BasicLogOutput get() {
    return edit;
  }
  
  
  /**
   * Define o formatador de log <code>OutputFormatter</code>
   * do <code>LogOutput</code> configurado.
   * @param lf Formatador de saída de log <code>OutputFormatter</code>.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory formatter(BasicOutputFormatter lf) {
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
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory debug(boolean bool) {
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
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory info(boolean bool) {
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
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory warning(boolean bool) {
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
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory error(boolean bool) {
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
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory fatal(boolean bool) {
    if(edit != null)
      edit.fatal(bool);
    return this;
  }
  
  
  /**
   * Adiciona o <code>LogOutput</code> configurado a <code>SimpleLog</code>.
   * @return Esta instância modificada de <code>SimpleLogFactory</code>.
   */
  public SimpleLogFactory add() {
    if(edit != null) {
      log.add(edit);
      edit = null;
    }
    return this;
  }
  
}