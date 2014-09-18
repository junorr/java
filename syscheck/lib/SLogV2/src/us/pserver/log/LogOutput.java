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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Saída de mensagens de log, define onde a
 * mensagem será exibida.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/04/2014
 */
public class LogOutput {

  private PrintStream output;
  
  private boolean debug, info, warn, error, fatal;
  
  private boolean stdout, errout, fileout;
  
  private OutputFormatter format;
  
  private long uid;
  
  
  /**
   * Construtor padrão e sem argumentos,
   * constrói uma saída de log para a saída
   * padrão do sistema.
   */
  public LogOutput() {
    setStdOutput();
    format = OutputFormatter.stdFormatter();
    debug = false;
    info = true;
    warn = true;
    error = true;
    fatal = true;
    stdout = errout = fileout = false;
    uid = System.currentTimeMillis();
  }
  
  
  /**
   * Define este <code>LogOutput</code> para a saída padrão do sistema.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput setStdOutput() {
    stdout = true;
    errout = fileout = false;
    output = new PrintStream(new OutputStream() {
      @Override 
      public void write(int b) throws IOException {
        System.out.write(b);
      }
    });
    format = OutputFormatter.stdFormatter();
    return this;
  }
  
  
  /**
   * Define este <code>LogOutput</code> para a saída de erros padrão do sistema.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput setErrOutput() {
    errout = true;
    stdout = fileout = false;
    output = new PrintStream(new OutputStream() {
      @Override 
      public void write(int b) throws IOException {
        System.err.write(b);
      }
    });
    format = OutputFormatter.errorFormatter();
    return this;
  }
  
  
  /**
   * Define este <code>LogOutput</code> para o arquivo informado.
   * @param file Nome do arquivo de log.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput setFileOutput(String file) {
    fileout = true;
    stdout = errout = false;
    format = OutputFormatter.fileFormatter();
    try {
      output = new PrintStream(
          Files.newOutputStream(Paths.get(file), 
          StandardOpenOption.APPEND, 
          StandardOpenOption.CREATE, 
          StandardOpenOption.SYNC));
      return this;
    } 
    catch(IOException e) {
      throw new IllegalArgumentException(
          "Invalid file: "+ file);
    }
  }
  
  
  /**
   * Define a saída deste <code>LogOutput</code> para o 
   * <code>OutputStream</code> informado.
   * @param os <code>OutputStream</code> de saída.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput setOutputStream(OutputStream os) {
    if(os == null)
      throw new IllegalArgumentException(
          "Invalid OutputStream: "+ os);
    error = stdout = fileout = false;
    output = new PrintStream(os);
    return this;
  }
  
  
  /**
   * Define a saída deste <code>LogOutput</code> para o 
   * <code>PrintStream</code> informado.
   * @param ps <code>PrintStream</code> de saída.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput setPrintStream(PrintStream ps) {
    if(ps == null)
      throw new IllegalArgumentException(
          "Invalid OutputStream: "+ ps);
    error = stdout = fileout = false;
    output = ps;
    return this;
  }
  
  
  /**
   * Verifica se esta instância de <code>LogOutput</code> está 
   * configurada para saída em arquivo de log.
   * @return <code>true</code> se esta instância
   * está configurada para saída em arquivo de log,
   * <code>false</code> caso contrário.
   */
  public boolean isFileOutput() {
    return fileout;
  }
  
  
  /**
   * Verifica se esta instância de <code>LogOutput</code> está 
   * configurada para a saída padrão do sistema.
   * @return <code>true</code> se esta instância
   * está configurada para a saída padrão do sistema,
   * <code>false</code> caso contrário.
   */
  public boolean isStdOutput() {
    return stdout;
  }
  
  
  /**
   * Verifica se esta instância de <code>LogOutput</code> está 
   * configurada para a saída padrão de erros do sistema.
   * @return <code>true</code> se esta instância
   * está configurada para a saída padrão de erros do sistema,
   * <code>false</code> caso contrário.
   */
  public boolean isErrOutput() {
    return errout;
  }
  
  
  /**
   * Verifica se esta instância de <code>LogOutput</code> está 
   * configurada para um stream de saída.
   * @return <code>true</code> se esta instância
   * está configurada para um stream de saída,
   * <code>false</code> caso contrário.
   */
  public boolean isStreamOutput() {
    return !fileout && !stdout && !errout;
  }
  
  
  /**
   * Retorna o número de identificação deste <code>LogOutput</code>.
   * @return long
   */
  public long uid() {
    return uid;
  }
  
  
  /**
   * Define o número de identificação deste <code>LogOutput</code>.
   * @param uid número de identificação deste <code>LogOutput</code>.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput uid(long uid) {
    this.uid = uid;
    return this;
  }
  
  
  /**
   * Verifica se o nível de log de debug está habilitado.
   * @return <code>true</code> se o nível de log de
   * debug está habilitado, <code>false</code>
   * caso contrário.
   */
  public boolean isDebugEnabled() {
    return debug;
  }
  
  
  /**
   * Verifica se o nível de log de informação está habilitado.
   * @return <code>true</code> se o nível de log de
   * informação está habilitado, <code>false</code>
   * caso contrário.
   */
  public boolean isInfoEnabled() {
    return info;
  }
  
  
  /**
   * Verifica se o nível de log de alerta está habilitado.
   * @return <code>true</code> se o nível de log de
   * alerta está habilitado, <code>false</code>
   * caso contrário.
   */
  public boolean isWarningEnabled() {
    return warn;
  }
  
  
  /**
   * Verifica se o nível de log de erro está habilitado.
   * @return <code>true</code> se o nível de log de
   * erro está habilitado, <code>false</code>
   * caso contrário.
   */
  public boolean isErrorEnabled() {
    return error;
  }
  
  
  /**
   * Verifica se o nível de log de erro fatal está habilitado.
   * @return <code>true</code> se o nível de log de
   * erro fatal está habilitado, <code>false</code>
   * caso contrário.
   */
  public boolean isFatalEnabled() {
    return fatal;
  }
  
  
  /**
   * Habilita/desabilita o nível de log de debug.
   * @param bool <code>true</code> para habilitar o 
   * nível de log de debug, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput debug(boolean bool) {
    debug = bool;
    return this;
  }
  
  
  /**
   * Habilita/desabilita o nível de log de informação.
   * @param bool <code>true</code> para habilitar o 
   * nível de log de informação, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput info(boolean bool) {
    info = bool;
    return this;
  }
  
  
  /**
   * Habilita/desabilita o nível de log de alerta.
   * @param bool <code>true</code> para habilitar o 
   * nível de log de alerta, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput warning(boolean bool) {
    warn = bool;
    return this;
  }
  
  
  /**
   * Habilita/desabilita o nível de log de erro.
   * @param bool <code>true</code> para habilitar o 
   * nível de log de erro, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput error(boolean bool) {
    error = bool;
    return this;
  }
  
  
  /**
   * Habilita/desabilita o nível de log de erro fatal.
   * @param bool <code>true</code> para habilitar o 
   * nível de log de erro fatal, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput fatal(boolean bool) {
    fatal = bool;
    return this;
  }
  
  
  /**
   * Retorna o formatador de saída de mensagens de log.
   * @return OutputFormatter.
   */
  public OutputFormatter formatter() {
    return format;
  }
  
  
  /**
   * Define o formatador de saída de mensagens de log.
   * @param lf OutputFormatter.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput formatter(OutputFormatter lf) {
    if(lf == null)
      throw new IllegalArgumentException(
          "Invalid LofFormatter: "+ lf);
    format = lf;
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível debug 
   * para a saída configurada.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput debug(String msg) {
    this.log(msg, LogLevel.DEBUG);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível informação
   * para a saída configurada.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput info(String msg) {
    this.log(msg, LogLevel.INFO);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível alerta
   * para a saída configurada.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput warning(String msg) {
    this.log(msg, LogLevel.WARN);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível erro
   * para a saída configurada.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput error(String msg) {
    this.log(msg, LogLevel.ERROR);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log nível erro fatal
   * para a saída configurada.
   * @param msg Mensagem de log.
   * @return Esta instância modificada de <code>LogOutput</code>.
   */
  public LogOutput fatal(String msg) {
    this.log(msg, LogLevel.FATAL);
    return this;
  }
  
  
  /**
   * Direciona a mensagem de log com o nível informado
   * para a saída configurada.
   * @param msg Mensagem de log.
   * @param level Nível da mensagem de log.
   * @return Esta intância modificada de <code>LogOutput</code>.
   */
  public LogOutput log(String msg, LogLevel level) {
    boolean enabled = false;
    switch(level) {
      case DEBUG:
        enabled = debug;
        break;
      case INFO:
        enabled = info;
        break;
      case WARN:
        enabled = warn;
        break;
      case ERROR:
        enabled = error;
        break;
      case FATAL:
        enabled = fatal;
        break;
      default:
        enabled = false;
        break;
    }
    if(enabled) {
      output.println(format
          .format(msg, level));
      output.flush();
    }
    return this;
  }
  
  
  /**
   * Fecha a saída configurada em <code>LogOutput</code>
   */
  public void close() {
    output.flush();
    output.close();
  }
  
}