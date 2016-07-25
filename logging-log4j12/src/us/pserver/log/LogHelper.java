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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
    return new LogHelper(Logger.getLogger(cls));
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
    return this.log(Level.DEBUG, msg);
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
    return this.log(Level.DEBUG, msg, args);
  }
  
  
  /**
   * Log a debug message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper debug(String msg, Throwable th) {
    return this.log(Level.DEBUG, msg, th);
  }
  
  
  /**
   * Log a debug, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper debug(Throwable th) {
    return this.log(Level.DEBUG, th.toString(), th);
  }
  
  
  /**
   * Log an info message.
   * @param msg The messge.
   * @return This instance of LogHelper.
   */
  public LogHelper info(String msg) {
    return this.log(Level.INFO, msg);
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
    return this.log(Level.INFO, msg, args);
  }
  
  
  /**
   * Log an info message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper info(String msg, Throwable th) {
    return this.log(Level.INFO, msg, th);
  }
  
  
  /**
   * Log an info, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper info(Throwable th) {
    return this.log(Level.INFO, th.toString(), th);
  }
  
  
  /**
   * Log a warn message.
   * @param msg The messge.
   * @return This instance of LogHelper.
   */
  public LogHelper warn(String msg) {
    return this.log(Level.WARN, msg);
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
    return this.log(Level.WARN, msg, args);
  }
  
  
  /**
   * Log a warn message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper warn(String msg, Throwable th) {
    return this.log(Level.WARN, msg, th);
  }
  
  
  /**
   * Log a warn, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper warn(Throwable th) {
    return this.log(Level.WARN, th.toString(), th);
  }
  
  
  /**
   * Log an error message.
   * @param msg The messge.
   * @return This instance of LogHelper.
   */
  public LogHelper error(String msg) {
    return this.log(Level.ERROR, msg);
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
    return this.log(Level.ERROR, msg, args);
  }
  
  
  /**
   * Log an error message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper error(String msg, Throwable th) {
    return this.log(Level.ERROR, msg, th);
  }
  
  
  /**
   * Log an error, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper error(Throwable th) {
    return this.log(Level.ERROR, th.toString(), th);
  }
  
  
  /**
   * Log a fatal message.
   * @param msg The messge.
   * @return This instance of LogHelper.
   */
  public LogHelper fatal(String msg) {
    return this.log(Level.FATAL, msg);
  }
  
  
  /**
   * Log a fatal message. Interpolates 
   * arguments in the message with 
   * <code>String.format(String, Ojbect ...)</code>.
   * @param msg The messge.
   * @param args Objects to interpolate in the message.
   * @return This instance of LogHelper.
   */
  public LogHelper fatal(String msg, Object ... args) {
    return this.log(Level.FATAL, msg, args);
  }
  
  
  /**
   * Log a fatal message and a throwable error.
   * @param msg The messge.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper fatal(String msg, Throwable th) {
    return this.log(Level.FATAL, msg, th);
  }
  
  
  /**
   * Log a fatal, throwable error.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper fatal(Throwable th) {
    return this.log(Level.FATAL, th.toString(), th);
  }
  

  /**
   * Log a message with the specified level.
   * @param lvl The log level.
   * @param msg The message.
   * @return This instance of LogHelper.
   */
  public LogHelper log(Level lvl, String msg) {
    if(log.isEnabledFor(lvl)) {
      switch(lvl.toInt()) {
        case Level.DEBUG_INT:
          log.debug(msg);
          break;
        case Level.WARN_INT:
          log.warn(msg);
          break;
        case Level.ERROR_INT:
          log.error(msg);
          break;
        case Level.FATAL_INT:
          log.fatal(msg);
          break;
        default:
          log.info(msg);
          break;
      }
    }
    return this;
  }
  
  
  /**
   * Log a message and a throwable error with the specified level.
   * @param lvl The log level.
   * @param msg The message.
   * @param th Exception to log.
   * @return This instance of LogHelper.
   */
  public LogHelper log(Level lvl, String msg, Throwable th) {
    if(log.isEnabledFor(lvl)) {
      switch(lvl.toInt()) {
        case Level.DEBUG_INT:
          log.debug(msg, th);
          break;
        case Level.WARN_INT:
          log.warn(msg, th);
          break;
        case Level.ERROR_INT:
          log.error(msg, th);
          break;
        case Level.FATAL_INT:
          log.fatal(msg, th);
          break;
        default:
          log.info(msg, th);
          break;
      }
    }
    return this;
  }
  
  
  /**
   * Log a message and the interpolated arguments with the specified level.
   * @param lvl The log level.
   * @param msg The message.
   * @param args Interpolated arguments.
   * @return This instance of LogHelper.
   */
  public LogHelper log(Level lvl, String msg, Object ... args) {
    if(log.isEnabledFor(lvl)) {
      log(lvl, String.format(msg, args));
    }
    return this;
  }
  
}
