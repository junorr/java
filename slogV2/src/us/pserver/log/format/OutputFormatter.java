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

package us.pserver.log.format;

import java.util.Date;
import us.pserver.log.LogLevel;

/**
 * A formatter for the log message.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
@FunctionalInterface
public interface OutputFormatter {
  
  /**
   * Format the arguments in a unique log string.
   * @param lvl The log level.
   * @param dte The date of the log.
   * @param name The name of the <code>Log</code> instance.
   * @param msg The log message.
   * @return The formatted log string.
   */
  public String format(LogLevel lvl, Date dte, String name, String msg);
  
}
