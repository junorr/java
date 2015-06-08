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
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 05/06/2015
 */
public class LogLevelManager {

  private Map<LogLevel, Boolean> levels;
  
  
  public LogLevelManager() {
    levels = Collections.synchronizedMap(
        new EnumMap<LogLevel, Boolean>(LogLevel.class));
    levels.put(LogLevel.DEBUG, Boolean.TRUE);
    levels.put(LogLevel.INFO, Boolean.TRUE);
    levels.put(LogLevel.WARN, Boolean.TRUE);
    levels.put(LogLevel.ERROR, Boolean.TRUE);
  }
  
  
  public Map<LogLevel, Boolean> levels() {
    return levels;
  }
  
  
  public void setLevelEnabled(LogLevel lvl, boolean enabled) {
    if(lvl == null) return;
    levels.put(lvl, enabled);
  }
  
  
  public boolean isLevelEnabled(LogLevel lvl) {
    return levels.get(lvl);
  }
  
  
  public void setAllLevelsEnabled(boolean enabled) {
    levels.keySet().forEach(k->levels.put(k, enabled));
  }
  
}
