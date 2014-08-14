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

package us.pserver.redfs.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import us.pserver.redfs.RemoteFile;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/12/2013
 */
public class FileLabel extends JLabel implements MouseListener {

  private RemoteFile file;
  
  private boolean selected;
  
  private Font selFont, unselFont;
  
  private Color selColor, unselColor;
  
  
  public FileLabel() {
    super();
    selected = false;
    file = null;
    unselFont = this.getFont();
    selFont = new Font(unselFont.getFontName(), Font.BOLD, unselFont.getSize());
    unselColor = Color.WHITE;
    selColor = Color.YELLOW;
    this.setBackground(unselColor);
    this.setOpaque(true);
  }
  
  
  public FileLabel(RemoteFile rf) {
    this();
    this.setFile(file);
  }


  public RemoteFile getFile() {
    return file;
  }


  public FileLabel setFile(RemoteFile file) {
    this.file = file;
    if(file != null && file.getName() != null) {
      this.setText(file.getName());
      if(file.getIcon() != null) {
        this.setIcon(file.getIcon());
      }
    }
    return this;
  }
  
  
  public void setSelected(boolean sel) {
    selected = sel;
    if(selected) {
      this.setFont(selFont);
      this.setBackground(selColor);
    }
    else {
      this.setFont(unselFont);
      this.setBackground(unselColor);
    }
    this.repaint();
  }
  
  
  public boolean isSelected() {
    return selected;
  }


  @Override
  public void mouseClicked(MouseEvent e) {
    setSelected(!selected);
  }


  @Override
  public void mousePressed(MouseEvent e) {
  }


  @Override
  public void mouseReleased(MouseEvent e) {
  }


  @Override
  public void mouseEntered(MouseEvent e) {
  }


  @Override
  public void mouseExited(MouseEvent e) {
  }
  
}
