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

package us.pserver.log.impl;

import java.util.Arrays;
import us.pserver.log.LogLevel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class LogLevels {

  private final LevelEntry[] levels;
  
  
  public LogLevels() {
    levels = new LevelEntry[] {
      new LevelEntry(LogLevel.DEBUG),
      new LevelEntry(LogLevel.INFO),
      new LevelEntry(LogLevel.WARN),
      new LevelEntry(LogLevel.ERROR)
    };
  }
  
  
  public boolean isLevelEnabled(LogLevel lvl) {
    if(lvl == null) return false;
    return levels[lvl.ordinal()].isEnabled();
  }
  
  
  public LogLevels setLevelEnabled(LogLevel lvl, boolean enabled) {
    if(lvl != null) {
      levels[lvl.ordinal()].setEnabled(enabled);
    }
    return this;
  }
  
  
  public boolean isAnyLevelEnabled() {
    for(LevelEntry e : levels) {
      if(e.isEnabled()) {
        return true;
      }
    }
    return false;
  }
  
  
  public LogLevels setAllLevelsEnabled(boolean enabled) {
    for(LevelEntry e : levels) {
      e.setEnabled(enabled);
    }
    return this;
  }
  
  
  public LevelEntry[] entries() {
    return levels;
  }
  
  
  public LogLevels copyFrom(LogLevels lvl) {
    if(lvl == null) return this;
    if(!lvl.isAnyLevelEnabled()) {
      this.setAllLevelsEnabled(false);
      return this;
    }
    for(LevelEntry le : lvl.entries()) {
      this.setLevelEnabled(le.level(), le.isEnabled());
    }
    return this;
  }
  
  
  @Override
  public String toString() {
    return Arrays.toString(levels);
  }
  
}
