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
import java.time.temporal.ChronoUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import us.pserver.jc.Clock;
import us.pserver.jc.Task;
import us.pserver.jc.WakeRule;
import us.pserver.jc.alarm.BasicAlarm;
import us.pserver.jc.alarm.BasicTask;
import us.pserver.jc.clock.BasicClock;
import us.pserver.jc.rules.RuleBuilder;
import us.pserver.jc.util.DateTime;
import us.pserver.jcal.CalendarDialog;
import us.pserver.tmo.ctrl.BlackButton;
import us.pserver.tmo.ctrl.PaintStyle;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/04/2014
 */
public class ClockButton extends BlackButton implements ActionListener {
  
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private DateTime time;
	
	private BasicClock clock;
  
  private final CalendarDialog cal;
	
	private final String dateFormat;
	
  
  public ClockButton(String dateFormat) {
    super(PaintStyle.BLACK_STYLE, "");
		if(dateFormat == null 
				|| dateFormat.trim().isEmpty()) {
			dateFormat = DEFAULT_DATE_FORMAT;
		}
		this.dateFormat = dateFormat;
		time = DateTime.now();
		clock = new BasicClock();
    cal = new CalendarDialog(this, false);
    cal.jcalendar().setCloseAction(
        ()->cal.setVisible(false));
    this.setFont(new Font("Monospaced", 
        Font.BOLD, 13));
		//this.setForeground(new Color(0, 177, 255));
		this.setForeground(new Color(240, 240, 255));
    this.setText(" "+time.format(dateFormat)+" ");
    this.addActionListener(this);
  }
	
	
	public ClockButton() {
		this(DEFAULT_DATE_FORMAT);
	}
  
  
  public ClockButton start() {
		Task uptime = new BasicTask(c->{
			time = DateTime.now();
			this.setText(time.format(dateFormat));
			this.repaint();
		});
		WakeRule rule = RuleBuilder
				.ruleIn(1, ChronoUnit.SECONDS)
				.build().get();
		clock.register("uptime", new BasicAlarm(rule, uptime));
		clock.setStopOnEmpty(false)
				.setLoggingEnabled(false);
		clock.startNewThread();
		return this;
  }
  
  
  public ClockButton stop() {
    clock.stop();
		return this;
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    if(cal.isVisible())
      cal.setVisible(false);
    else
      cal.showDialog();
  }

	
	public static void main(String[] args) {
		final JFrame f = new JFrame("Test ClockButton");
		JPanel p = new JPanel();
		p.add(new ClockButton().start());
		f.add(p);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
        //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        f.setVisible(true);
      }
    });
	}
	
}
