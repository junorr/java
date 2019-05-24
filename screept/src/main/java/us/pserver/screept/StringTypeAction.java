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

package us.pserver.screept;

import java.awt.Robot;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23 de mai de 2019
 */
public class StringTypeAction implements Consumer<Robot> {
  
  private final String str;
  
  private final List<KeyboardAction> actions;
  
  public StringTypeAction(String str) {
    if(str == null || str.isBlank()) {
      throw new IllegalArgumentException("Bad null/empty string");
    }
    this.str = str;
    this.actions = new LinkedList<>();
    char[] cs = str.toCharArray();
    for(int i = 0; i < cs.length; i++) {
      KeyboardAction.getActionFor(cs[i]).ifPresent(actions::add);
    }
    actions.forEach(System.out::println);
  }
  
  @Override
  public void accept(Robot r) {
    this.actions.forEach(a -> a.accept(r));
  }

}
