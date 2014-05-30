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
 *
 */

package us.pserver.jcal;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import us.pserver.date.SimpleDate;


/**
 * <b>JCalendar</b> implementa um <code>JPanel</code>
 * com uma série de componentes que representam
 * um calendário seletor de datas.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 02/05/2014
 */
public class JCalendar extends javax.swing.JPanel
    implements MouseListener, MouseMotionListener {

  public static final String[] months = 
      { "January", "February", "March", 
        "April", "May", "June", 
        "July", "August", "September", 
        "October", "November", "December" };
  
  public static final String
      PATH_ICON_CLOSE = "/us/pserver/jcal/imgs/close-20.png",
      PATH_ICON_OVER = "/us/pserver/jcal/imgs/close_white-20.png",
      PATH_CURSOR_ARROW = "/us/pserver/jcal/imgs/arrow.png",
      PATH_CURSOR_LINK = "/us/pserver/jcal/imgs/link.png",
      PATH_CURSOR_MOVE = "/us/pserver/jcal/imgs/move.png";
  
  
  private SimpleDate date;
  
  private ImageIcon normal, over;
  
  private Point last;
  
  private Window owner;
  
  private RunAction dayAction;
  
  private Runnable closeAction;
  
  private Consumer<SimpleDate> changeAction;
  
  private Cursor arrow, move, link;
  
  
  /**
   * Construtor padrão que recebe o <code>Window</code>
   * que irá conter <code>JCalendar</code>.
   * @param owner <code>Window</code> dono de <code>JCalendar</code>.
   */
  public JCalendar(Window owner) {
    this(owner, SimpleDate.now());
  }
  
  
  /**
   * Construtor que recebe o <code>Window</code>
   * que irá conter <code>JCalendar</code> e a data
   * inicial definida no seletor de data.
   * @param owner <code>Window</code> dono de <code>JCalendar</code>.
   * @param date Data inicial definida no seletor de data.
   */
  public JCalendar(Window owner, SimpleDate date) {
    if(owner == null)
      throw new IllegalArgumentException(
          "Invalid owner Window: "+ owner);
    if(date == null) 
      throw new IllegalArgumentException(
          "Invalid Date: "+ date);
    
    this.owner = owner;
    this.date = date;
    
    initComponents();
    this.removeAll();
    this.setLayout(null);
    headerPanel.setBounds(1, 1, 310, 47);
    this.add(headerPanel);
    weekPanel.setBounds(1, 49, 310, 30);
    this.add(weekPanel);
    daysPanel.setBounds(1, 80, 310, 179);
    this.add(daysPanel);
    this.setSize(310, 260);
    daysPanel.setLayout(new GridLayout(0, 7));
    monthCombo.setSelectedIndex(date.month()-1);
    
    last = null;
    normal = new ImageIcon(getClass()
        .getResource(PATH_ICON_CLOSE));
    over = new ImageIcon(getClass()
        .getResource(PATH_ICON_OVER));
    
    dayAction = new RunAction(this::setDay);
    closeAction = ()->owner.dispose();
    changeAction = null;
    
    closeLabel.addMouseListener(this);
    headerPanel.addMouseListener(this);
    headerPanel.addMouseMotionListener(this);
    populate();
    
    arrow = Toolkit.getDefaultToolkit()
        .createCustomCursor(new ImageIcon(
        getClass().getResource(PATH_CURSOR_ARROW))
        .getImage(), new Point(10, 10), "Arrow");
    link = Toolkit.getDefaultToolkit()
        .createCustomCursor(new ImageIcon(
        getClass().getResource(PATH_CURSOR_LINK))
        .getImage(), new Point(10, 10), "Link");
    move = Toolkit.getDefaultToolkit()
        .createCustomCursor(new ImageIcon(
        getClass().getResource(PATH_CURSOR_MOVE))
        .getImage(), new Point(15, 15), "Move");
    
    this.setCursor(arrow);
  }
  
  
  /**
   * Define a data exibida no seletor de <code>JCalendar</code>.
   * @param date Data a ser exibida.
   * @return Esta instância modificada de <code>JCalendar</code>.
   */
  public JCalendar setDate(SimpleDate date) {
    if(date != null) {
      this.date = date;
      if(this.isVisible()) populate();
    }
    return this;
  }
  
  
  /**
   * Retorna a data representado por <code>JCalendar</code>.
   * @return data <code>SimpleDate</code>.
   */
  public SimpleDate getDate() {
    return date;
  }
  
  
  /**
   * Define a ação a ser executada quando um botão de dia
   * for selecionado em <code>JCalendar</code>.
   * @param r Ação a ser executada quando um dia for selecionado.
   * @return Esta instância modificada de <code>JCalendar</code>.
   */
  public JCalendar setDayButtonAction(Consumer<JButton> r) {
    if(r != null) {
      dayAction = new RunAction(r);
      populate();
    }
    return this;
  }
  
  
  /**
   * Define a ação a ser executada quando o botão de
   * fechar for pressionado.
   * @param r Ação de fechamento.
   * @return Esta instância modificada de <code>JCalendar</code>.
   */
  public JCalendar setCloseAction(Runnable r) {
    if(r != null)
      closeAction = r;
    return this;
  }
  
  
  /**
   * Define a ação a ser executada quando ocorrer uma 
   * alteração na data representada por <code>JCalendar</code>.
   * @param cs Ação de alteração na data.
   * @return Esta instância modificada de <code>JCalendar</code>.
   */
  public JCalendar setDateChangeAction(Consumer<SimpleDate> cs) {
    if(cs != null)
      changeAction = cs;
    return this;
  }
  
  
  private void setDay(JButton b) {
    date.setDay(Integer.parseInt(b.getText()));
    owner.dispose();
  }
  
  
  /**
   * Define Nimbus LookAndFeel.
   * @return Esta instância modificada de <code>JCalendar</code>.
   */
  JCalendar setLookAndFeel() {
    try {
      UIManager.setLookAndFeel(new NimbusLookAndFeel());
    } catch (UnsupportedLookAndFeelException ex) {}
    SwingUtilities.updateComponentTreeUI(this);
    return this;
  }
  
  
  private void populate() {
    SimpleDate sd = date.clone();
    SimpleDate now = new SimpleDate();
    int dow = sd.day(1).dayOfWeek();
    int last = sd.lastDayOfMonth();
    int month = sd.month();
    daysPanel.removeAll();
    
    for(int i = 0; i < dow-1; i++) {
      daysPanel.add(new JLabel());
    }
    
    while(sd.day() <= last && sd.month() == month) {
      JButton b = new JButton(String.valueOf(sd.day()));
      b.addActionListener(dayAction);
      b.setToolTipText("Select Day");
      if(sd.equalsDate(now)) {
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        b.setBackground(Color.WHITE);
        b.setForeground(new Color(0, 0, 150));
      }
      daysPanel.add(b);
      sd.addDay(1);
    }
    
    daysPanel.revalidate();
    daysPanel.repaint();
  }


  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    headerPanel = new javax.swing.JPanel();
    monthCombo = new javax.swing.JComboBox();
    yearSpin = new JSpinner();
    yearSpin.setValue(date.year());
    closeLabel = new javax.swing.JLabel();
    iconLabel = new javax.swing.JLabel();
    weekPanel = new javax.swing.JPanel();
    sunLb = new javax.swing.JLabel();
    monLb = new javax.swing.JLabel();
    tueLb = new javax.swing.JLabel();
    wedLb = new javax.swing.JLabel();
    thuLb = new javax.swing.JLabel();
    friLb = new javax.swing.JLabel();
    satLb = new javax.swing.JLabel();
    daysPanel = new javax.swing.JPanel();

    setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));

    headerPanel.setBackground(new java.awt.Color(70, 80, 80));

    monthCombo.setModel(
      new DefaultComboBoxModel(months));
    monthCombo.setToolTipText("Select Month");
    monthCombo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        monthComboActionPerformed(evt);
      }
    });

    yearSpin.setToolTipText("Select Year");
    yearSpin.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        yearSpinStateChanged(evt);
      }
    });

    closeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/jcal/imgs/close-20.png"))); // NOI18N

    iconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/us/pserver/jcal/imgs/calendar-20.png"))); // NOI18N

    javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
    headerPanel.setLayout(headerPanelLayout);
    headerPanelLayout.setHorizontalGroup(
      headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(headerPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(iconLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(yearSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(closeLabel)
        .addContainerGap())
    );
    headerPanelLayout.setVerticalGroup(
      headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(headerPanelLayout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(closeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(iconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(yearSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(12, 12, 12))
    );

    weekPanel.setBackground(new java.awt.Color(255, 255, 255));
    weekPanel.setLayout(new java.awt.GridLayout(1, 7));

    sunLb.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    sunLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    sunLb.setText("Sun");
    weekPanel.add(sunLb);

    monLb.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    monLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    monLb.setText("Mon");
    weekPanel.add(monLb);

    tueLb.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    tueLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    tueLb.setText("Tue");
    weekPanel.add(tueLb);

    wedLb.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    wedLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    wedLb.setText("Wed");
    weekPanel.add(wedLb);

    thuLb.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    thuLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    thuLb.setText("Thu");
    weekPanel.add(thuLb);

    friLb.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    friLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    friLb.setText("Fri");
    weekPanel.add(friLb);

    satLb.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    satLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    satLb.setText("Sat");
    weekPanel.add(satLb);

    daysPanel.setBackground(new java.awt.Color(255, 255, 255));

    javax.swing.GroupLayout daysPanelLayout = new javax.swing.GroupLayout(daysPanel);
    daysPanel.setLayout(daysPanelLayout);
    daysPanelLayout.setHorizontalGroup(
      daysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    daysPanelLayout.setVerticalGroup(
      daysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 176, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(daysPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addComponent(weekPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGap(0, 0, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(35, 35, 35)
        .addComponent(daysPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addGap(49, 49, 49)
          .addComponent(weekPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addContainerGap(181, Short.MAX_VALUE)))
    );
  }// </editor-fold>//GEN-END:initComponents

  
  private void monthComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthComboActionPerformed
    date.setMonth(monthCombo.getSelectedIndex()+1);
    populate();
    if(changeAction != null)
      changeAction.accept(date);
  }//GEN-LAST:event_monthComboActionPerformed

  
  private void yearSpinStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yearSpinStateChanged
    date.setYear((int) yearSpin.getValue());
    populate();
    if(changeAction != null)
      changeAction.accept(date);
  }//GEN-LAST:event_yearSpinStateChanged


  @Override
  public void mouseClicked(MouseEvent e) {
    if(e.getSource() == closeLabel) {
      closeLabel.setIcon(normal);
      closeLabel.repaint();
      closeLabel.setCursor(link);
      closeAction.run();
    }
  }


  @Override
  public void mousePressed(MouseEvent e) {
    if(e.getSource() == headerPanel) {
      last = e.getLocationOnScreen();
    }
  }


  @Override
  public void mouseReleased(MouseEvent e) {
    if(e.getSource() == headerPanel) {
      last = null;
    }
  }


  @Override
  public void mouseEntered(MouseEvent e) {
    if(e.getSource() == closeLabel) {
      closeLabel.setIcon(over);
      closeLabel.repaint();
      closeLabel.setCursor(link);
    }
    else if(e.getSource() == headerPanel) {
      headerPanel.setCursor(move);
    }
  }


  @Override
  public void mouseExited(MouseEvent e) {
    if(e.getSource() == closeLabel) {
      closeLabel.setIcon(normal);
      closeLabel.repaint();
      closeLabel.setCursor(arrow);
    }
    else if(e.getSource() == headerPanel) {
      headerPanel.setCursor(arrow);
    }
  }


  @Override
  public void mouseDragged(MouseEvent e) {
    if(e.getSource() != headerPanel
        || last == null) return;
    Point p = e.getLocationOnScreen();
    Point loc = this.getLocationOnScreen();
    int x = p.x - last.x;
    int y = p.y - last.y;
    owner.setLocation((loc.x + x), (loc.y + y));
    last = p;
  }


  @Override public void mouseMoved(MouseEvent e) {}


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel closeLabel;
  private javax.swing.JPanel daysPanel;
  private javax.swing.JLabel friLb;
  private javax.swing.JPanel headerPanel;
  private javax.swing.JLabel iconLabel;
  private javax.swing.JLabel monLb;
  private javax.swing.JComboBox monthCombo;
  private javax.swing.JLabel satLb;
  private javax.swing.JLabel sunLb;
  private javax.swing.JLabel thuLb;
  private javax.swing.JLabel tueLb;
  private javax.swing.JLabel wedLb;
  private javax.swing.JPanel weekPanel;
  private javax.swing.JSpinner yearSpin;
  // End of variables declaration//GEN-END:variables



  /**
   * Classe <i>wrapper</i> de <code>ActioListener</code>
   * para <code>Consumer&lt;JButton&gt;</code>.
   */
  class RunAction implements ActionListener {
    
    private Consumer<JButton> run;
    
    public RunAction(Consumer<JButton> r) {
      if(r == null)
        throw new IllegalArgumentException(
            "Invalid Consumer: "+ r);
      run = r;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      run.accept((JButton) e.getSource());
    }
  }
  
}
