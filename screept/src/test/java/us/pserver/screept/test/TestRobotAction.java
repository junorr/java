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
import java.util.Objects;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import us.pserver.screept.action.KeyboardAction;
import us.pserver.screept.OS;
import us.pserver.screept.action.StringTypeAction;
import us.pserver.tools.Unchecked;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23 de mai de 2019
 */
public class TestRobotAction {
  
  private final Robot r;
  
  public TestRobotAction() {
    this.r = Unchecked.call(() -> new Robot());
  }

  @Test
  public void test_alt_combo_action() {
    new StringTypeAction(OS.OS_NAME).accept(r);
    KeyboardAction.C_CEDIL_LOWER.accept(r);
    KeyboardAction.C_CEDIL_UPPER.accept(r);
  }
  
  @Test
  public void test_string_type_action() {
    StringTypeAction a = new StringTypeAction("aAàÀáÁãÃâÂäÄ bB cC dD eEèÈéÉẽẼêÊëË fF gG hH iIìÌíÍĩĨîÎïÏ jJ kK lL mM nN oOòÒóÓõÕôÔöÖ pP qQ rR sS tT uUùÙúÚũŨûÛüÜ vV wW xX yYỳỲýÝỹỸŷŶÿŸ zZ çÇ 0123456789 !@#$%&*()-_=+ {[ }] ?/ :; >. <, |\\ '\" ¹²³ ªº° «»←↓→↑ ×÷½¾¼™±©®");
    a.accept(r);
  }
  
}
