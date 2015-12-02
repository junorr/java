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

import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Helper class to provide useful log methods for log4j.
 * @author Juno Roesler - juno@pserver.us
 */
public class LogHelper {

  private final Logger log;
  
  
  /**
   * Default constructor which receives the Logger.
   * @param log Logger.
   */
  public LogHelper(Logger log) {
    if(log == null)
      throw new IllegalArgumentException("Invalid Logger: "+ log);
    this.log = log;
  }
  
  
  /**
   * Create a LogHelper instance with a Logger configured for the given name.
   * @param logname The log name.
   * @return LogHelper instance with a Logger configured for the given name.
   */
  public static LogHelper off(String logname) {
    if(logname == null)
      throw new IllegalArgumentException("Invalid log name: "+ logname);
    return new LogHelper(Logger.getLogger(logname));
  }
  
  
  /**
   * Create a LogHelper instance with a Logger configured for the given class.
   * @param cls The log class.
   * @return LogHelper instance with a Logger configured for the given class.
   */
  public static LogHelper off(Class cls) {
    if(cls == null)
      throw new IllegalArgumentException("Invalid log Class: "+ cls);
    return new LogHelper(Logger.getLogger(cls.getName()));
  }
  
  
  /**
   * Get the Logger object.
   * @return the Logger object.
   */
  public Logger logger() {
    return log;
  }
  
  
  /**
   * Log a debug message.
   * @param msg The messge.
   * @return This instance of LogHelper.
   */
  public LogHelper debug(String msg) {
    log.log(Level.FINE, msg);
		return this;
  }
  
  
  /**
   * Log a debug message. Interpolates 
   * arguments in the message with 
   * <code>String.format(String, Ojbect ...)</code>.
   * @param msg The messge.
   * @param args Objects to interpolate in the message.
   * @return This instance of LogHelper.
   */
  public LogHelper debug(String msg, Object ... args) {
    log.log(Level.FINE, msg, args);
		return this;
  }
  
  
  /**
   * Log a debug message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper debug(String msg, Throwable th) {
    log.log(Level.FINE, msg, th);
		return this;
  }
  
  
  /**
   * Log a debug, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper debug(Throwable th) {
    log.log(Level.FINE, th.toString(), th);
		return this;
  }
  
  
  /**
   * Log an info message.
   * @param msg The messge.
   * @return This instance of LogHelper.
   */
  public LogHelper info(String msg) {
    log.log(Level.INFO, msg);
		return this;
  }
  
  
  /**
   * Log an info message. Interpolates 
   * arguments in the message with 
   * <code>String.format(String, Ojbect ...)</code>.
   * @param msg The messge.
   * @param args Objects to interpolate in the message.
   * @return This instance of LogHelper.
   */
  public LogHelper info(String msg, Object ... args) {
    log.log(Level.INFO, msg, args);
		return this;
  }
  
  
  /**
   * Log an info message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper info(String msg, Throwable th) {
    log.log(Level.INFO, msg, th);
		return this;
  }
  
  
  /**
   * Log an info, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper info(Throwable th) {
    log.log(Level.INFO, th.toString(), th);
		return this;
  }
  
  
  /**
   * Log a warn message.
   * @param msg The messge.
   * @return This instance of LogHelper.
   */
  public LogHelper warn(String msg) {
    log.log(Level.WARNING, msg);
		return this;
  }
  
  
  /**
   * Log a warn message. Interpolates 
   * arguments in the message with 
   * <code>String.format(String, Ojbect ...)</code>.
   * @param msg The messge.
   * @param args Objects to interpolate in the message.
   * @return This instance of LogHelper.
   */
  public LogHelper warn(String msg, Object ... args) {
    log.log(Level.WARNING, msg, args);
		return this;
  }
  
  
  /**
   * Log a warn message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper warn(String msg, Throwable th) {
    log.log(Level.WARNING, msg, th);
		return this;
  }
  
  
  /**
   * Log a warn, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper warn(Throwable th) {
    log.log(Level.WARNING, th.toString(), th);
		return this;
  }
  
  
  /**
   * Log an error message.
   * @param msg The messge.
   * @return This instance of LogHelper.
   */
  public LogHelper error(String msg) {
    log.log(Level.SEVERE, msg);
		return this;
  }
  
  
  /**
   * Log an error message. Interpolates 
   * arguments in the message with 
   * <code>String.format(String, Ojbect ...)</code>.
   * @param msg The messge.
   * @param args Objects to interpolate in the message.
   * @return This instance of LogHelper.
   */
  public LogHelper error(String msg, Object ... args) {
    log.log(Level.SEVERE, msg, args);
		return this;
  }
  
  
  /**
   * Log an error message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper error(String msg, Throwable th) {
    log.log(Level.SEVERE, msg, th);
		return this;
  }
  
  
  /**
   * Log an error, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper error(Throwable th) {
    log.log(Level.SEVERE, th.toString(), th);
		return this;
  }
  
}
