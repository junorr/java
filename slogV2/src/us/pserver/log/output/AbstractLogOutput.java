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
import us.pserver.log.format.OutputFormatter;
import us.pserver.log.impl.LogLevels;

/**
 * Implements basic functionalities for <code>LogOutput</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public abstract class AbstractLogOutput implements LogOutput {

  LogLevels levels;
  
  
  /**
   * Default constructor without arguments.
   */
  AbstractLogOutput() {
    levels = new LogLevels();
  }
  
  
  /**
   * Constructor which receives the output formatter class.
   * @param fmt The output formatter class.
   */
  AbstractLogOutput(OutputFormatter fmt) {
    this();
  }

  
  @Override
  public AbstractLogOutput setLevelEnabled(LogLevel lvl, boolean enabled) {
    levels.setLevelEnabled(lvl, enabled);
    return this;
  }


  @Override
  public boolean isLevelEnabled(LogLevel lvl) {
    return levels.isLevelEnabled(lvl);
  }
  
  
  @Override
  public boolean isAnyLevelEnabled() {
    return levels.isAnyLevelEnabled();
  }
  
  
  @Override
  public LogOutput setAllLevelsEnabled(boolean enabled) {
    levels.setAllLevelsEnabled(enabled);
    return this;
  }
  
  
  @Override
  public LogLevels levels() {
    return levels;
  }

}
