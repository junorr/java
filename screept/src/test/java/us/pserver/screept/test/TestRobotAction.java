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

package us.pserver.screept.test;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import org.junit.jupiter.api.Test;
import us.pserver.screept.KeyComboAction;
import us.pserver.screept.KeyTypeAction;
import us.pserver.screept.StringTypeAction;
import us.pserver.tools.Unchecked;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23 de mai de 2019
 */
public class TestRobotAction {
  
  private final Robot r;
  
  //
  
  public TestRobotAction() {
    this.r = Unchecked.call(() -> new Robot());
  }

  //@Test
  public void teste_key_type_action() {
    KeyTypeAction a = new KeyTypeAction(0xA1);
    a.accept(r);
  }
  
  //@Test
  public void test_key_combo_action() {
    KeyComboAction a = new KeyComboAction(KeyEvent.VK_SHIFT, KeyEvent.VK_Z);
    a.accept(r);
  }
  
  @Test
  public void test_string_type_action() {
    StringTypeAction a = new StringTypeAction("aAàÀáÁãÃâÂäÄ bB cC dD eEèÈéÉẽẼêÊëË fF gG hH iIìÌíÍĩĨîÎïÏ jJ kK lL mM nN oOòÒóÓõÕôÔöÖ pP qQ rR sS tT uUùÙúÚũŨûÛüÜ vV wW xX yYỳỲýÝỹỸŷŶÿŸ zZ 0123456789 ¹²³ ªº° !@#$%&*()-_=+ {[ }] ?/ :; >. <, |\\ '\" çÇ «»←↓→↑ ×÷½¾¼⅜⅞™±©®");
    a.accept(r);
  }
  
  @Test 
  public void test_key_char_code() {
    char c = (char) -2;
    int d = (int) c;
    System.out.printf("char = %s, code = %d%n", c, d);
    
    d = 0x2191;
    c = Character.toString((char)d).charAt(0);
    System.out.printf("unicode = %d, char = %s%n", d, c);
  }
  
}
