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

package us.pserver.jcs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import javax.swing.JTextArea;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/04/2014
 */
public class JConsole extends JTextArea implements Printable {

  public static final Color 
      
      BACKGROUND = new Color(61, 61, 61),
      
      FOREGROUND = new Color(102, 255, 51);
  
  public static final String 
      CONSOLAS = "Consolos",
      MONOSPACED = "Monospaced";
  
  public static final Font 
      DEFAULT_FONT = new Font(CONSOLAS, Font.BOLD, 14),
      ALT_FONT = new Font(MONOSPACED, Font.BOLD, 14);
  
  
  public JConsole() {
    super();
    this.setBackground(BACKGROUND);
    this.setForeground(FOREGROUND);
    this.setEditable(false);
    
    String fonts[] = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames();
    int found = Arrays.binarySearch(fonts, CONSOLAS);
    if(found < 0)
      this.setFont(ALT_FONT);
    else
      this.setFont(DEFAULT_FONT);
  }
  
  
  @Override
  public synchronized void print(String str) {
    this.append(str);
    this.setCaretPosition(this.getText().length());
    this.paint(this.getGraphics());
  }
  
  
  @Override
  public synchronized void println(String str) {
    print(str+LN);
  }
  
}
