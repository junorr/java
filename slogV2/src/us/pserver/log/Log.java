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
import us.pserver.log.output.LogOutput;
import java.util.Map;
import java.util.Objects;
import us.pserver.log.format.OutputFormatter;
import us.pserver.log.impl.LogLevels;

/**
 * Basic log service interface.
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public interface Log {

  
  /**
   * Adds a <code>LogOutput</code> to this <code>Log</code> instance.
   * @param id The <code>LogOutput</code> ID.
   * @param out The <code>LogOutput</code> to be added.
   * @return This modified <code>Log</code> instance.
   */
  public Log put(String id, LogOutput out);
  
  
  /**
   * Clear all <code>LogOutput</code> objects from 
   * this <code>Log</code> instance.
   * @return This modified <code>Log</code> instance.
   */
  public Log clearOutputs();
  
  
  /**
   * Get the map with all <code>LogOutput</code> objects 
   * from this <code>Log</code> instance.
   * @return The map with all <code>LogOutput</code> objects 
   * from this <code>Log</code> instance.
   */
  public Map<String, LogOutput> outputsMap();
  
  
  /**
   * Get a collection with all <code>LogOutput</code> objects 
   * from this <code>Log</code> instance.
   * @return A collection with all <code>LogOutput</code> objects 
   * from this <code>Log</code> instance.
   */
  public Collection<LogOutput> outputs();
  
  
  /**
   * Get the LogOutput identified by the string <code>id</code>.
   * @param id The <code>LogOutput</code> ID.
   * @return The retrieved <code>LogOutput</code> or <code>null</code>.
   */
  public LogOutput get(String id);
  
  
  /**
   * Remove the <code>LogOutput</code> identified by 
   * the string <code>id</code>.
   * @param id The <code>LogOutput</code> ID.
   * @return The removed <code>LogOutput</code> or <code>null</code>.
   */
  public LogOutput remove(String id);
  
  
  /**
   * Verify if the map of <code>LogOutput</code> objects contains 
   * the specified string <code>id</code>.
   * @param id The <code>LogOutput</code> ID.
   * @return <code>true</code> if the <code>LogOutput</code> map
   * contains the specified string <code>id</code>, 
   * <code>false</code> otherwise.
   */
  public boolean contains(String id);
  
  
  /**
   * Get the log name for this <code>Log</code> instance.
   * @return The log name.
   */
  public String getLogName();
  
  
  /**
   * Set the <code>OutputFormatter</code> to 
   * this <code>Log</code> instance.
   * @param fmt Log output formatter.
   * @return This modified <code>Log</code> instance.
   */
  public Log setOutputFormatter(OutputFormatter fmt);
  
  
  /**
   * Get the <code>OutputFormatter</code> for 
   * this <code>Log</code> instance.
   * @return Log output formatter.
   */
  public OutputFormatter getOutputFormatter();
  
  
  /**
   * Enable or disable the specified LogLevel.
   * @param lvl The LogLevel to change.
   * @param enabled <code>true</code> if the LogLevel should be enabled, 
   * <code>false</code> otherwise.
   * @return This modified <code>Log</code> instance.
   */
  public Log setLevelEnabled(LogLevel lvl, boolean enabled);
  
  
  /**
   * Enable/disable all the log levels for this 
   * <code>Log</code> instance.
   * @param enabled <code>true</code> if the log levels should 
   * be enabled, <code>false</code> for disable.
   * @return This modified <code>Log</code> instance.
   */
  public Log setAllLevelsEnabled(boolean enabled);
  
  
  public boolean isAnyLevelEnabled();
  
  
  public boolean isLevelEnabled(LogLevel lvl);
  
  
  public LogLevels levels();
  
  
  /**
   * Logs a message with DEBUG level.
   * @param msg The message to be logged.
   * @return This modified <code>Log</code> instance.
   */
  public Log debug(String msg);
  
  
  /**
   * Logs a message with arguments to be interpolated, 
   * specified in the string by the <b>'{}'</b> chars.
   * @param msg The message to interpolated and logged.
   * @param args The arguments which will be interpolated 
   * in the string message.
   * @return This modified <code>Log</code> instance.
   */
  public Log debug(String msg, Object ... args);

  
  /**
   * Logs an exception with DEBUG level.
   * @param th The exception to be logged.
   * @param logStackTrace <code>true</code> if all
   * the stack trace of the exception should be logged,
   * <code>false</code> to log only the exception message.
   * @return This modified <code>Log</code> instance.
   */
  public Log debug(Throwable th, boolean logStackTrace);
  
  
  /**
   * Logs a message with INFO level.
   * @param msg The message to be logged.
   * @return This modified <code>Log</code> instance.
   */
  public Log info(String msg);

  
  /**
   * Logs a message with arguments to be interpolated, 
   * specified in the string by the <b>'{}'</b> chars.
   * @param msg The message to interpolated and logged.
   * @param args The arguments which will be interpolated 
   * in the string message.
   * @return This modified <code>Log</code> instance.
   */
  public Log info(String msg, Object ... args);
  
  
  /**
   * Logs an exception with INFO level.
   * @param th The exception to be logged.
   * @param logStackTrace <code>true</code> if all
   * the stack trace of the exception should be logged,
   * <code>false</code> to log only the exception message.
   * @return This modified <code>Log</code> instance.
   */
  public Log info(Throwable th, boolean logStackTrace);
  
  
  /**
   * Logs a message with WARNING level.
   * @param msg The message to be logged.
   * @return This modified <code>Log</code> instance.
   */
  public Log warn(String msg);

  
  /**
   * Logs a message with arguments to be interpolated, 
   * specified in the string by the <b>'{}'</b> chars.
   * @param msg The message to interpolated and logged.
   * @param args The arguments which will be interpolated 
   * in the string message.
   * @return This modified <code>Log</code> instance.
   */
  public Log warn(String msg, Object ... args);
  
  
  /**
   * Logs an exception with WARNING level.
   * @param th The exception to be logged.
   * @param logStackTrace <code>true</code> if all
   * the stack trace of the exception should be logged,
   * <code>false</code> to log only the exception message.
   * @return This modified <code>Log</code> instance.
   */
  public Log warn(Throwable th, boolean logStackTrace);
  
  
  /**
   * Logs a message with ERROR level.
   * @param msg The message to be logged.
   * @return This modified <code>Log</code> instance.
   */
  public Log error(String msg);

  
  /**
   * Logs a message with arguments to be interpolated, 
   * specified in the string by the <b>'{}'</b> chars.
   * @param msg The message to interpolated and logged.
   * @param args The arguments which will be interpolated 
   * in the string message.
   * @return This modified <code>Log</code> instance.
   */
  public Log error(String msg, Object ... args);
  
  
  /**
   * Logs an exception with ERROR level.
   * @param th The exception to be logged.
   * @param logStackTrace <code>true</code> if all
   * the stack trace of the exception should be logged,
   * <code>false</code> to log only the exception message.
   * @return This modified <code>Log</code> instance.
   */
  public Log error(Throwable th, boolean logStackTrace);
  
  
  /**
   * Logs a message with the specified <code>LogLevel</code>.
   * @param msg The message to be logged.
   * @param lvl The level of the log.
   * @return This modified <code>Log</code> instance.
   */
  public Log log(LogLevel lvl, String msg);

  
  /**
   * Log the <code>Throwable</code> instance on the
   * specified level.
   * @param lvl The <code>LogLevel</code> to this log.
   * @param th The <code>Throwable</code> instance to be logged.
   * @param logStackTrace <code>true</code> if all the stack trace
   * for this <code>Throwable</code> should be logged,
   * <code>false</code> otherwise.
   * @return This modified <code>Log</code> instance.
   */
  public Log log(LogLevel lvl, Throwable th, boolean logStackTrace);
  
  
  /**
   * Log the message, replacing the <b>'{}'</b> marks on
   * the string by the objects arguments.
   * @param lvl The <code>LogLevel</code> for this log.
   * @param msg The message to be logged.
   * @param args The objects arguments to replaced in the 
   * string message.
   * @return This modified <code>Log</code> instance.
   */
  public Log log(LogLevel lvl, String msg, Object ... args);
  
  
  /**
   * Close all resources used by this <code>Log</code> and 
   * his <code>LogOutput</code> instances.
   * @return This modified <code>Log</code> instance.
   */
  public Log close();
  
}
