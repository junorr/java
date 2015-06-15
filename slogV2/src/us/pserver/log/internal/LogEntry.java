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

package us.pserver.log.internal;

import java.util.Date;
import java.util.Objects;
import us.pserver.log.LogLevel;

/**
 * <code>LogEntry</code> holds information
 * to perform a log output. 
 * Used in <code>SLogV2 ExecutorService</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public class LogEntry {

  private LogLevel level;
  
  private Date date;
  
  private String name;
  
  private String massage;
  
  private Throwable throwable;
  
  private boolean logStackTrace;


  /**
   * Default constructor without arguments.
   */
  public LogEntry() {
  }


  /**
   * Constructor which receives the log instance name.
   * @param name The log instance name.
   */
  public LogEntry(String name) {
    this.name = name;
  }


  /**
   * Constructor which receives the details of log output to perform.
   * @param level The log level.
   * @param date The log date.
   * @param name The log instance name.
   * @param massage The log message.
   */
  public LogEntry(LogLevel level, Date date, String name, String massage) {
    this.level = level;
    this.date = date;
    this.name = name;
    this.massage = massage;
  }


  /**
   * Constructor which receives the details of log output to perform.
   * @param level The log level.
   * @param date The log date.
   * @param name The log instance name.
   * @param massage The throwable to log.
   * @param logStackTrace If <code>true</code> log all the throwable stack trace.
   */
  public LogEntry(LogLevel level, Date date, String name, Throwable throwable, boolean logStackTrace) {
    this.level = level;
    this.date = date;
    this.name = name;
    this.throwable = throwable;
    this.logStackTrace = logStackTrace;
  }


  /**
   * Get the log level.
   * @return the log level.
   */
  public LogLevel getLevel() {
    return level;
  }


  /**
   * Set the log level.
   * @param level The log level.
   */
  public void setLevel(LogLevel level) {
    this.level = level;
  }


  /**
   * Get the log date.
   * @return The log date.
   */
  public Date getDate() {
    return date;
  }


  /**
   * Set the log date.
   * @param date The log date.
   */
  public void setDate(Date date) {
    this.date = date;
  }


  /**
   * Get the log instance name.
   * @return The log instance name.
   */
  public String getName() {
    return name;
  }


  /**
   * Set the log instance name.
   * @param name The log instance name.
   */
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Get the log message.
   * @return The log message.
   */
  public String getMassage() {
    return massage;
  }


  /**
   * Set the log message.
   * @param message The log message.
   */
  public void setMassage(String massage) {
    this.massage = massage;
  }


  /**
   * Get the log throwable.
   * @return The log throwable.
   */
  public Throwable getThrowable() {
    return throwable;
  }


  /**
   * Set the log throwable.
   * @param throwable The log throwable.
   */
  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }


  /**
   * Verify if this entry is configured to print 
   * all the throwable stack trace.
   * @return <code>true</code> if this entry is 
   * configured to print all the throwable stack trace,
   * <code>false</code> otherwise.
   */
  public boolean isLoggingStackTrace() {
    return logStackTrace;
  }


  /**
   * Set if this entry is configured to print 
   * all the throwable stack trace.
   * @param logStackTrace If <code>true</code>,
   * configure this entry to print all the throwable 
   * stack trace.
   */
  public void setLoggingStackTrace(boolean logStackTrace) {
    this.logStackTrace = logStackTrace;
  }
  

  /**
   * Verify if this entry is configured to log a throwable instance.
   * @return <code>true</code> if this entry is configured to log a 
   * throwable instance, <code>false</code> otherwise.
   */
  public boolean isThrowable() {
    return throwable != null;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + Objects.hashCode(this.level);
    hash = 79 * hash + Objects.hashCode(this.date);
    hash = 79 * hash + Objects.hashCode(this.name);
    hash = 79 * hash + Objects.hashCode(this.massage);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final LogEntry other = (LogEntry) obj;
    if (this.level != other.level) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.massage, other.massage)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "LogEntry{" + "level=" + level + ", date=" + date + ", name=" + name + ", massage=" + massage + '}';
  }
  
}
