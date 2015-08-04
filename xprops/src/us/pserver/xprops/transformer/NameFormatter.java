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

package us.pserver.xprops.transformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import us.pserver.tools.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 04/08/2015
 */
public class NameFormatter {

  public String format(Class cls) {
    Valid.off(cls).forNull().fail(Class.class);
    String name = cls.getSimpleName();
    StringBuilder sb = new StringBuilder();
    Pattern pt = Pattern.compile("[A-Z]");
    Matcher m = pt.matcher(name);
    int before = 0;
    while(m.find()) {
      int st = m.start();
      if(st > 0) {
        sb.append(name.substring(before, st))
            .append("-");
        before = st;
      }
    }
    sb.append(name.substring(before));
    return sb.toString().toLowerCase();
  }
  
}
