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

package us.pserver.code;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import ru.lanwen.verbalregex.VerbalExpression;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/05/2014
 */
public class TestCharPanel {

  
  public static void main(String[] args) {
    JFrame f = new JFrame("TestCharPanel");
    f.setSize(400, 350);
    f.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    f.setLocationRelativeTo(null);
    CharPanel cp = new CharPanel(new JChar());
    cp.setBorder(new LineBorder(Color.GRAY, 1));
    cp.setOriginalSize(300, 200);
    
    cp.getHighlighter().clear();
    cp.getHighlighter().add(
        VerbalExpression.regex()
            .startOfLine().anything().then("\"")
            .anything().then("\"").anything()
            .build(), Color.LIGHT_GRAY);
    cp.getHighlighter().add(
        VerbalExpression.regex()
            .startOfLine().then("public")
            .build(), Color.WHITE);
    /*
    cp.setBackground(Color.WHITE);
    cp.setForeground(Color.BLACK);
    cp.setUnderColor(Color.RED)
        .setInsertColor(Color.BLUE);
    */
    f.add(cp);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
    cp.requestFocus();
    cp.requestFocusInWindow();
  }
  
}
