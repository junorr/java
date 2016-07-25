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

package us.pserver.log.output;

import us.pserver.log.LogLevel;
import us.pserver.log.impl.LogLevels;

/**
 * Interface that defines an output for log messages.
 * Implementations of this interface must support multithreading.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 201506
 */
public interface LogOutput {
  
  /**
   * Set the specified log level enabled/disabled.
   * @param lvl The log level to be setted.
   * @param enabled <code>true</code> for enable the log level, 
   * <code>false</code> to disable it.
   * @return This modified <code>LogOutput</code> instance.
   */
  public LogOutput setLevelEnabled(LogLevel lvl, boolean enabled);
  
  /**
   * Set all log levels enabled/disabled.
   * @param enabled <code>true</code> for enable all log levels, 
   * <code>false</code> to disable them.
   * @return This modified <code>LogOutput</code> instance.
   */
  public LogOutput setAllLevelsEnabled(boolean enabled);
  
  /**
   * Verify if the specified log level is enabled.
   * @param lvl The log level to be verified;
   * @return <code>true</code> if the log level
   * is enabled, <code>false</code> otherwise.
   */
  public boolean isLevelEnabled(LogLevel lvl);
  
  public boolean isAnyLevelEnabled();
  
  public LogLevels levels();
  
  /**
   * Direct the log to the output implemented 
   * by this <code>LogOutput</code>.
   * @param lvl The log level.
   * @param msg The log message.
   * @return This modified <code>LogOutput</code> instance.
   */
  public LogOutput log(LogLevel lvl, String msg);
  
  /**
   * Closes any resources used by this log output.
   */
  public void close();
  
}
