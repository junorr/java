/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.power.visual.frames;

import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author f6036477
 */
public class Tip extends JWindow implements MouseMotionListener {
	
	public static final int 
			DEFAULT_TIME_TO_SHOW = 850,
			DEFAULT_TIME_TO_DISPOSE = 2000;
	
	
	private Object tip;
	
	private Rectangle area;
	
	private Point p, param;
	
	private Timer show, dispose;
	
	private boolean disposeOut;
	
	private Component comp;
	
	private ShadowEffect shadow;
	
	private FaderEffect fader;
	
	private JPanel border;
	
	private boolean hasFocus, compFocus;
	
	private MouseListener focusListener;

	private int ttd, tts;

	
	public Tip(Window owner, Component parent, Object tip) {
		super(owner);
		this.setFocusable(true);
		this.setFocusableWindowState(true);
		this.init(parent, tip);
	}
	
	public Tip(Component parent, Object tip) {
		this.init(parent, tip);
	}
	
	private void init(Component parent, Object tip) {
		comp = parent;
		
		this.tip = tip;
		
		border = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setColor(new Color(252, 252, 228));
				g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 8, 8);
				g2.setColor(new Color(0, 0, 120));
				g2.drawRoundRect(0, 0, this.getWidth() -1, this.getHeight() -1, 8, 8);
			}
		};

		if(ShadowEffect.isTranslucencyCapable()) {
			shadow = new ShadowEffect();
			shadow.addWindow(this);
			shadow.setRoundedCorner(true);
			shadow.setAngleCtrl(0);
			shadow.setAngleWidth(4);
			shadow.setShadowWidth(3);
			shadow.setShadowSizeUnderComponent(1);
			fader = new FaderEffect();
			fader.addWindow(this);
			fader.addWindow(shadow.getEffectContainer());
		}
		
		hasFocus = false;
		compFocus = false;
		
		focusListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent me) {
				if(me.getSource().equals(comp))
					compFocus = true;
				else
					hasFocus = true;
			}
			@Override
			public void mouseExited(MouseEvent me) {
				if(me.getSource().equals(comp))
					compFocus = false;
				else
					hasFocus = false;
			}
		};
		this.addMouseListener(focusListener);
		border.addMouseListener(focusListener);
		
		this.setTip(tip);
		
		p = null;
		param = null;
		area = null;
		disposeOut = false;
		
		setTimeToShow(DEFAULT_TIME_TO_SHOW);
		setTimeToDispose(DEFAULT_TIME_TO_DISPOSE);
		
		comp.addMouseMotionListener(this);
		comp.addMouseListener(focusListener);
	}
	
	
	private void applyShape() {
		RoundRectangle2D rect = new RoundRectangle2D
				.Double(0, 0, this.getWidth(), this.getHeight(), 8.0, 8.0);
		AWTUtilities.setWindowShape(this, rect);
	}
	
	
	public static Tip createFocusableTip(Component visibleParent, Object tip) {
		Component c = getOwnerWindow(visibleParent);
		if(c != null && c instanceof Window)
			return new Tip((Window) c, visibleParent, tip);
		else
			return new Tip(c, tip);
	}
	
	
	private static Component getOwnerWindow(Component c) {
		if(c == null) return null;
		if(c instanceof Window) {
			return c;
		}	else return getOwnerWindow(c.getParent());
	}
	
	
	public void setTimeToShow(int t) {
		if(t <= 0) return;
		tts = t;
		show = new Timer(tts, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(compFocus && p != null && param != null
						&& p.x == param.x && p.y == param.y) {
					Tip.this.showTip(p, true);
				}
			}
		});
		show.setRepeats(false);
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		p = e.getPoint();
		if(area == null || area.contains(p)) {
			show.restart();
			param = new Point(p.x, p.y);
		} else {
			if(show.isRunning())
				show.stop();
			if(disposeOut && this.isVisible()) {
				this.dispose();
				shadow.removeEffect();
			}
			p = null;
		}
	}
	
	
	public void setTimeToDispose(int t) {
		if(t <= 0) return;
		ttd = t;
		dispose = new Timer(t, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Tip.this.isVisible() && !hasFocus) {
					if(FaderEffect.isTranslucencyCapable())
						fader.fadeOut();
					else {
						Tip.this.dispose();
					}
				} else if(Tip.this.isVisible()) {
					dispose.start();
				}
			}
		});
		dispose.setRepeats(false);
	}
	
	
	public void setTip(Object tip) {
		if(tip == null) return;
		this.tip = tip;
		border.removeAll();
		this.getContentPane().removeAll();

		Component c = null;

		if(tip instanceof JComboBox) {
			this.configureJComboBox((JComboBox) tip);
		}
		if(tip instanceof Component) {
			c = (Component) tip;
		} else {
			c = new JLabel(tip.toString());
		}

		c.addMouseListener(focusListener);
		border.add(c);

		this.add(border);
		this.pack();
	}


	private void configureJComboBox(JComboBox box) {
		if(box == null) return;

		box.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				hasFocus = true;
				if(dispose != null && dispose.isRunning())
					dispose.stop();
			}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				hasFocus = false;
				if(dispose != null)
					dispose.start();
			}
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				hasFocus = false;
				if(dispose != null)
					dispose.start();
			}
		});
	}
	
	
	public Object getTip() {
		return tip;
	}
	
	
	public void closeTip() {
		if(this.isVisible()) {
			this.dispose();
		}
	}
	
	
	public void showTip(Point p, boolean timeout) {
		if(p == null || comp == null) {
			System.out.println("ERROR: Point == null");
			return;
		}
		this.setLocation(comp.getLocationOnScreen().x + p.x, 
				comp.getLocationOnScreen().y + p.y);
		
		if(FaderEffect.isTranslucencyCapable()) {
			this.applyShape();
			fader.fadeIn();
			shadow.applyEffect();
		} else {
			this.setVisible(true);
		}
		
		if(timeout && dispose != null) dispose.start();
	}


	public int getTimeToShow() {
		return tts;
	}


	public int getTimeToDispose() {
		return ttd;
	}
	
	
	public void setActiveArea(Rectangle r) {
		area = r;
	}
	
	
	public Rectangle getActiveArea() {
		return area;
	}
	
	
	public Component getComponent() {
		return comp;
	}
	
	
	public boolean isDisposeOutActiveArea() {
		return disposeOut;
	}
	
	
	public void setDisposeOutActiveArea(boolean b) {
		disposeOut = b;
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {}


	public static void main(String[] args) {
		JFrame f = new JFrame("Tip Test");
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(300, 280);
		f.setLayout(new BorderLayout());
		final JLabel label = new JLabel("  -  ");
		JPanel p = new JPanel();
		p.add(label);
		f.add(p, BorderLayout.SOUTH);
		f.getContentPane().addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {}
			public void mouseMoved(MouseEvent e) {
				label.setText(e.getPoint().x + " - " + e.getPoint().y);
				label.repaint();
			}
		});
		
		Tip tip = Tip.createFocusableTip(f.getContentPane(),
				"Caixa de dica (ToolTip) !");
				//new JTextField("Test ..."));
				//new JComboBox(new String[] {"Selecione", "Tip Test", "Test Passed"}));
		tip.setActiveArea(null);//new Rectangle(30, 30, 50, 50));
		//tip.setDisposeOutActiveArea(true);
		f.setVisible(true);
	}


}
