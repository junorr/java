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

package us.pserver.jcal;

import java.awt.Component;
import java.awt.Point;
import javax.swing.JDialog;
import us.pserver.date.SimpleDate;

/**
 * Implementa um <code>JDialog</code>
 * configurado para conter <code>JCalendar</code>.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/05/2014
 */
public class CalendarDialog extends JDialog {

  private final JCalendar calendar;
  
  private final Component relative;
  
  
  /**
   * Construtor padrão que recebe o componente a partir 
   * do qual será definida a posição inicial e
   * <code>boolean</code> definindo se a janela será
   * <i>modal</i> ou não.
   * @param relative <code>Component</code>.
   * @param modal <i>modal</i>.
   */
  public CalendarDialog(Component relative, boolean modal) {
    super();
    calendar = new JCalendar(this);
    this.relative = relative;
    this.setTitle("JCalendar");
    this.setModal(modal);
    this.setLocationRelativeTo(null);
    this.setUndecorated(true);
    this.add(calendar.setLookAndFeel());
    this.setSize(calendar.getWidth() + 2, calendar.getHeight());
  }
  
  
  /**
   * Retorna a instância de <code>JCalendar</code>
   * @return <code>JCalendar</code>.
   */
  public JCalendar jcalendar() {
    return calendar;
  }
  
  
  public void showDialog() {
    if(relative != null && relative.isVisible()) {
      Point p = relative.getLocationOnScreen();
      this.setLocation(
          p.x + relative.getWidth()/2 - this.getWidth()/2, 
          p.y + relative.getHeight() + 2);
    }
    setVisible(true);
  }
  
  
  /**
   * Exibe uma janela <b>não</b> <i>modal</i> com 
   * <code>JCalendar</code> configurado 
   * com a data atual.
   * @return A instância configurada de <code>CalendarDialog</code>.
   */
  public static CalendarDialog showCalendarDialog() {
    CalendarDialog cd = new CalendarDialog(null, false);
    cd.showDialog();
    return cd;
  }
  
  
  /**
   * Exibe uma janela <b>não</b> <i>modal</i> com 
   * <code>JCalendar</code> configurado 
   * com a data informada.
   * @param date Data a ser exibida por <code>JCalendar</code>.
   * @return A instância configurada de <code>CalendarDialog</code>.
   */
  public static CalendarDialog showCalendarDialog(SimpleDate date) {
    CalendarDialog cd = new CalendarDialog(null, false);
    if(date != null)
      cd.jcalendar().setDate(date);
    cd.showDialog();
    return cd;
  }
  
  
  /**
   * Exibe uma janela <b>não</b> <i>modal</i> com 
   * <code>JCalendar</code> configurado 
   * com a data informada.
   * @param c Componente a partir do qual será
   * definida a posição inicial da janela de <code>JCalendar</code>.
   * @param date Data a ser exibida por <code>JCalendar</code>.
   * @return A instância configurada de <code>CalendarDialog</code>.
   */
  public static CalendarDialog showCalendarDialog(Component c, SimpleDate date) {
    CalendarDialog cd = new CalendarDialog(c, false);
    if(date != null)
      cd.jcalendar().setDate(date);
    cd.showDialog();
    return cd;
  }
  
  
  /**
   * Exibe uma janela <i>modal</i> com 
   * <code>JCalendar</code> configurado 
   * com a data atual.
   * @return Data selecionada em <code>JCalendar</code>.
   */
  public static SimpleDate showModalCalendarDialog() {
    CalendarDialog cd = new CalendarDialog(null, true);
    cd.showDialog();
    return cd.jcalendar().getDate();
  }
  
  
  /**
   * Exibe uma janela <i>modal</i> com 
   * <code>JCalendar</code> configurado 
   * com a data informada.
   * @param date Data a ser exibida por <code>JCalendar</code>.
   * @return Data selecionada em <code>JCalendar</code>.
   */
  public static SimpleDate showModalCalendarDialog(SimpleDate date) {
    CalendarDialog cd = new CalendarDialog(null, true);
    if(date != null)
      cd.jcalendar().setDate(date);
    cd.showDialog();
    return cd.jcalendar().getDate();
  }
  
  
  /**
   * Exibe uma janela <i>modal</i> com 
   * <code>JCalendar</code> configurado 
   * com a data informada.
   * @param c Componente a partir do qual será
   * definida a posição inicial da janela de <code>JCalendar</code>.
   * @param date Data a ser exibida por <code>JCalendar</code>.
   * @return Data selecionada em <code>JCalendar</code>.
   */
  public static SimpleDate showModalCalendarDialog(Component c, SimpleDate date) {
    CalendarDialog cd = new CalendarDialog(c, true);
    if(date != null)
      cd.jcalendar().setDate(date);
    cd.showDialog();
    return cd.jcalendar().getDate();
  }
  
  
  public static void main(String[] args) {
    System.out.println("Date Selected: "
        + CalendarDialog.showModalCalendarDialog());
  }
  
}
