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

import java.awt.FlowLayout;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import us.pserver.redfs.RFile;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 17/12/2013
 */
public class FileListPanel extends JPanel {

  private JList<RFile> list;
  
  private List<RFile> lines;
  
  
  public FileListPanel() {
    super(new FlowLayout(FlowLayout.CENTER, 2, 2));
    list = new JList<>();
    list.setCellRenderer(new FileLine());
    this.add(new JScrollPane(list));
    lines = new LinkedList<>();
  }
  
  
  public FileListPanel add(RFile rf) {
    if(rf != null) {
      lines.add(rf);
      RFile[] fs = new RFile[lines.size()];
      fs = lines.toArray(fs);
      list.setListData(fs);
      this.repaint();
    }
    return this;
  }
  
  
  public FileListPanel setList(List<RFile> ls) {
    for(RFile rf : ls) {
      this.add(rf);
    }
    return this;
  }
  
}
