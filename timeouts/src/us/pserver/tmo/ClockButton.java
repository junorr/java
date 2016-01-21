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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.JButton;
import us.pserver.jc.Clock;
import us.pserver.jc.Task;
import us.pserver.jc.WakeRule;
import us.pserver.jc.alarm.BasicAlarm;
import us.pserver.jc.alarm.BasicTask;
import us.pserver.jc.clock.BasicClock;
import us.pserver.jc.rules.RuleBuilder;
import us.pserver.jc.util.DateTime;
import us.pserver.jcal.CalendarDialog;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/04/2014
 */
public class ClockButton extends JButton implements ActionListener {
  
  private DateTime time;
	
	private Clock clock;
  
  private final CalendarDialog cal;
	
	private final DateTimeFormatter fmt;
  
  
  public ClockButton() {
    super();
		time = DateTime.now();
		fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		clock = new BasicClock();
    time = DateTime.now();
    cal = new CalendarDialog(this, false);
    cal.jcalendar().setCloseAction(
        ()->cal.setVisible(false));
    this.setFont(new Font("Monospaced", 
        Font.PLAIN, 12));
    this.setForeground(Color.BLUE);
    setBackground(Color.WHITE);
    this.setText(" "+fmt.format(time.toLocalDT())+" ");
    this.addActionListener(this);
  }
  
  
  public void start() {
		Task uptime = new BasicTask(c->{
			time = DateTime.now();
			this.setText(fmt.format(time.toLocalDT()));
			this.repaint();
		});
		WakeRule rule = new RuleBuilder()
				.at(DateTime.now().plus(1, ChronoUnit.SECONDS))
				.in(1, ChronoUnit.SECONDS)
				.build().get();
		clock.register("uptime", new BasicAlarm(rule, uptime));
		clock.setStopOnEmpty(false).start();
  }
  
  
  public void stop() {
    clock.stop();
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    if(cal.isVisible())
      cal.setVisible(false);
    else
      cal.showDialog();
  }

}
