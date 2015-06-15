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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import us.pserver.log.LogLevel;

/**
 * Utility class to manage configuration of LogLevel enum.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public class LogLevelManager {

  private Map<LogLevel, Boolean> levels;
  
  
  /**
   * Default constructor, create the 4 LogLevel types enabled.
   */
  public LogLevelManager() {
    levels = Collections.synchronizedMap(
        new EnumMap<LogLevel, Boolean>(LogLevel.class));
    levels.put(LogLevel.DEBUG, Boolean.TRUE);
    levels.put(LogLevel.INFO, Boolean.TRUE);
    levels.put(LogLevel.WARN, Boolean.TRUE);
    levels.put(LogLevel.ERROR, Boolean.TRUE);
  }
  
  
  /**
   * Get the internal map with <code>LogLevel's</code>.
   * @return The internal map with <code>LogLevel's</code>.
   */
  public Map<LogLevel, Boolean> levels() {
    return levels;
  }
  
  
  /**
   * Configure the <code>LogLevel</code>.
   * @param lvl The <code>LogLevel</code> to configure.
   * @param enabled <code>true</code> for enable the
   * <code>LogLevel</code>, <code>false</code> otherwise.
   */
  public void setLevelEnabled(LogLevel lvl, boolean enabled) {
    if(lvl == null) return;
    levels.put(lvl, enabled);
  }
  
  
  /**
   * Verify if the <code>LogLevel</code> is enabled.
   * @param lvl The <code>LogLevel</code> type.
   * @return <code>true</code> if the <code>LogLevel</code> 
   * is enabled, <code>false</code> otherwise.
   */
  public boolean isLevelEnabled(LogLevel lvl) {
    return levels.get(lvl);
  }
  
  
  /**
   * Configure all <code>LogLevel</code> types at once.
   * @param enabled <code>true</code> for enable all 
   * <code>LogLevel</code>, <code>false</code> for diable it.
   */
  public void setAllLevelsEnabled(boolean enabled) {
    levels.keySet().forEach(k->levels.put(k, enabled));
  }
  
}
