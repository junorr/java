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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledEditorKit;

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
    
    Highlighter hl = new Highlighter();
    
    hl.add(new Match("\\bpublic\\b", 
        new TextStyle().setForeground(Color.red)
            .setFontBold(true)));
    
    final JEditorPane edit = new JEditorPane();
    edit.setEditorKit(new StyledEditorKit());
    edit.setDocument(new DefaultStyledDocument());
    edit.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    edit.setFont(new Font("Verdana", Font.PLAIN, 16));
    edit.setEditable(true);
    Dimension d = new Dimension(420, 240);
    edit.setSize(d);
    edit.setPreferredSize(d);
    edit.addKeyListener(new KeyListener() {
      @Override public void keyTyped(KeyEvent e) {}
      @Override public void keyReleased(KeyEvent e) {
        if(Highlighter.shouldUpdate(e))
          hl.update(edit);
      }
      @Override public void keyPressed(KeyEvent e) {}
    });
    
    f.add(edit);
    f.setVisible(true);
  }
  
}
