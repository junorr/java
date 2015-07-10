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

import us.pserver.log.LogLevel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2015
 */
public class LevelEntry {

  private LogLevel level;
  
  private boolean enabled;
  
  
  public LevelEntry(LogLevel lvl) {
    if(lvl == null)
      throw new IllegalArgumentException("Invalid LogLevel: "+ lvl);
    level = lvl;
    enabled = false;
  }
  
  
  public LogLevel level() {
    return level;
  }
  
  
  public boolean isEnabled() {
    return enabled;
  }
  
  
  public LevelEntry setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }
  
  
  @Override
  public String toString() {
    return level.name()+ "("+ enabled+ ")";
  }
  
}
