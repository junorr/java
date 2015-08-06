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

package us.pserver.log.test;

import java.util.Date;
import us.pserver.log.LogLevel;
import us.pserver.log.format.PatternOutputFormatter;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 06/08/2015
 */
public class TestPatternFormatter {

  
  public static void main(String[] args) {
    String pattern = "{DATE}  [{LEVEL}]  {NAME}:{LINE} - {MESSAGE}";
    PatternOutputFormatter pof = new PatternOutputFormatter(pattern);
    System.out.printf("* pattern: %s%n", pattern);
    System.out.printf("* format: %s%n", pof.format(LogLevel.INFO, new Date(), "Pattern", 39, "A Log Message"));
  }
  
}
