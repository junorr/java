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

package us.pserver.tmo;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import us.pserver.date.SimpleDate;
import us.pserver.jcal.CalendarDialog;
import us.pserver.scron.Schedule;
import us.pserver.scron.SimpleCron;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/04/2014
 */
public class JClock extends JButton implements ActionListener {
  
  private final SimpleDate time;
  
  private final SimpleCron cron;
  
  private final CalendarDialog cal;
  
  
  public JClock() {
    super();
    cron = new SimpleCron()
        .setLogEnabled(false);
    time = SimpleDate.now();
    cal = new CalendarDialog(this, false);
    cal.jcalendar().setCloseAction(
        ()->cal.setVisible(false));
    this.setFont(new Font("Monospaced", 
        Font.PLAIN, 12));
    this.setForeground(Color.BLUE);
    setBackground(Color.WHITE);
    this.setText(" "+time.toString()+" ");
    this.addActionListener(this);
  }
  
  
  public void start() {
    Schedule s = new Schedule()
        .startNow()
        .repeatInSeconds(1);
    cron.put(s, ()-> {
      time.setNow();
      this.setText(" "+time.toString()+" ");
      this.repaint();
    });
  }
  
  
  public void stop() {
    cron.stop();
    cron.list().clear();
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    if(cal.isVisible())
      cal.setVisible(false);
    else
      cal.showDialog();
  }

}
