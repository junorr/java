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

import com.jpower.date.SimpleDate;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import us.pserver.redfs.RFile;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/12/2013
 */
public class FileLine extends JPanel implements ListCellRenderer<RFile> {

  private RFile file;
  
  private FileLabel name, date, size;
  
  
  public FileLine() {
    super(new FlowLayout(FlowLayout.LEFT, 1, 1));
    name = new FileLabel();
    name.setPreferredSize(new Dimension(300, 25));
    date = new FileLabel();
    date.setPreferredSize(new Dimension(130, 25));
    size = new FileLabel();
    size.setPreferredSize(new Dimension(100, 25));
  }
  
  
  public FileLine(RFile rf) {
    this();
    this.setFile(rf);
  }
  
  
  public RFile getFile() {
    return file;
  }
  
  
  public FileLine setFile(RFile rf) {
    if(rf != null && rf.getLastModifiedDate() != null
        && rf.getSize() != null) {
      file = rf;
      name.setFile(file);
      date.setText(new SimpleDate(
          rf.getLastModifiedDate()).toString());
      size.setText(rf.getSize().toString());
      this.add(name);
      this.add(date);
      this.add(size);
    }
    return this;
  }
  
  
  public FileLine setSelected(boolean sel) {
    name.setSelected(sel);
    date.setSelected(sel);
    size.setSelected(sel);
    return this;
  }
  
  
  public boolean isSelected() {
    return name.isSelected();
  }


  @Override
  public Component getListCellRendererComponent(
      JList<? extends RFile> list, 
      RFile value, 
      int index, 
      boolean isSelected, 
      boolean cellHasFocus) {
    return new FileLine(value).setSelected(isSelected);
  }
  
}
