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

package us.pserver.coder;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/08/2014
 */
public class TestEditorPane {

  
  public static void main(String[] args) {
    JFrame f = new JFrame("Editor Pane Test");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLocationRelativeTo(null);
    f.setSize(450, 300);
    
    final Editor edit = new Editor();
    
    Highlighter hl = edit.getSintaxHighlighter();
    
    hl.add(new Match("Constants", "[A-Z]+\\w*",
        new TextStyle().setForeground(Color.WHITE)
            .setFontItalic(true)));
    hl.add(new Match("Integer", "\\bint\\b", 
        new TextStyle().setForeground(new Color(200, 225, 255))
        .setFontBold(true)));
    hl.add(new Match("Double", "\\bdouble\\b", 
        new TextStyle().setForeground(new Color(200, 225, 255))
        .setFontBold(true)));
    hl.add(new Match("String", "\\bstring\\b", 
        new TextStyle().setForeground(new Color(200, 225, 255))
        .setFontBold(true)));
    hl.add(new Match("Object", "\\bobject\\b", 
        new TextStyle().setForeground(new Color(200, 225, 255))
        .setFontBold(true)));
    hl.add(new Match("Method", "[a-zA-Z_]+\\w*\\(",
        new TextStyle().setForeground(Color.WHITE)
            .setFontBold(true)));
    hl.add(new Match("String Constant", "\".*\"",
        new TextStyle().setForeground(new Color(255, 200, 80))
            .setFontItalic(true)));
    hl.add(new Match("Comment", "#.*",
        new TextStyle().setForeground(new Color(255, 255, 160))
            .setFontItalic(true)));
    
    edit.setBackground(new Color(77, 77, 77));
    edit.setForeground(new Color(200, 255, 210));
    edit.setSelectionColor(new Color(170, 170, 170));
    edit.setCaretColor(new Color(200, 255, 210));
    Dimension d = new Dimension(420, 240);
    edit.setSize(d);
    edit.setPreferredSize(d);
    f.add(edit);
    f.setVisible(true);
    edit.requestFocus();
  }
  
}
