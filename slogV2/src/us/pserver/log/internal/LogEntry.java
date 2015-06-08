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
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2015
 */
public class LogEntry {

  private LogLevel level;
  
  private Date date;
  
  private String name;
  
  private String massage;
  
  private Throwable throwable;
  
  private boolean logStackTrace;


  public LogEntry() {
  }


  public LogEntry(String name) {
    this.name = name;
  }


  public LogEntry(LogLevel level, Date date, String name, String massage) {
    this.level = level;
    this.date = date;
    this.name = name;
    this.massage = massage;
  }


  public LogEntry(LogLevel level, Date date, String name, Throwable throwable, boolean logStackTrace) {
    this.level = level;
    this.date = date;
    this.name = name;
    this.throwable = throwable;
    this.logStackTrace = logStackTrace;
  }


  public LogLevel getLevel() {
    return level;
  }


  public void setLevel(LogLevel level) {
    this.level = level;
  }


  public Date getDate() {
    return date;
  }


  public void setDate(Date date) {
    this.date = date;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getMassage() {
    return massage;
  }


  public void setMassage(String massage) {
    this.massage = massage;
  }


  public Throwable getThrowable() {
    return throwable;
  }


  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }


  public boolean isLoggingStackTrace() {
    return logStackTrace;
  }


  public void setLoggingStackTrace(boolean logStackTrace) {
    this.logStackTrace = logStackTrace;
  }
  
  
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
    return "LogInput{" + "level=" + level + ", date=" + date + ", name=" + name + ", massage=" + massage + '}';
  }
  
}
