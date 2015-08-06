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
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class LogLevels {

  private LevelEntry debug;
  
  private LevelEntry info;
  
  private LevelEntry warn;
  
  private LevelEntry error;
  
  
  public LogLevels() {
    debug = new LevelEntry(LogLevel.DEBUG);
    info = new LevelEntry(LogLevel.INFO);
    warn = new LevelEntry(LogLevel.WARN);
    error = new LevelEntry(LogLevel.ERROR);
  }
  
  
  public LevelEntry getLevelEntry(LogLevel lvl) {
    Valid.off(lvl).forNull().fail(LogLevel.class);
    switch(lvl) {
      case DEBUG:
        return debug;
      case INFO:
        return info;
      case WARN:
        return warn;
      case ERROR:
        return error;
      default:
        throw new IllegalArgumentException(
            "Unknown LogLevel: "+ lvl.name());
    }
  }
  
  
  public boolean isLevelEnabled(LogLevel lvl) {
    if(lvl == null) return false;
    return getLevelEntry(lvl).isEnabled();
  }
  
  
  public LogLevels setLevelEnabled(LogLevel lvl, boolean enabled) {
    getLevelEntry(lvl).setEnabled(enabled);
    return this;
  }
  
  
  public boolean isAnyLevelEnabled() {
    return debug.isEnabled()
        || info.isEnabled()
        || warn.isEnabled()
        || error.isEnabled();
  }
  
  
  public LogLevels setAllLevelsEnabled(boolean enabled) {
    debug.setEnabled(enabled);
    info.setEnabled(enabled);
    warn.setEnabled(enabled);
    error.setEnabled(enabled);
    return this;
  }
  
  
  public LogLevels copyFrom(LogLevels lvl) {
    if(lvl != null) {
      debug.setEnabled(lvl.isLevelEnabled(LogLevel.DEBUG));
      info.setEnabled(lvl.isLevelEnabled(LogLevel.INFO));
      warn.setEnabled(lvl.isLevelEnabled(LogLevel.WARN));
      error.setEnabled(lvl.isLevelEnabled(LogLevel.ERROR));
    }
    return this;
  }
  
  
  @Override
  public String toString() {
    return String.format(
        "LogLevels{debug=%s, info=%s, warn=%s, error=%s}", 
        debug.isEnabled(), info.isEnabled(), 
        warn.isEnabled(), error.isEnabled()
    );
  }
  
}
