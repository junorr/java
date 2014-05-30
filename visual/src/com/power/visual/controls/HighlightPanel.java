package com.power.visual.controls;


import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author f6036477
 */
public class HighlightPanel extends JPanel {
	
	private Object content;
	
	private Component comp;
	
	private MouseListener focusListener;
	
	private boolean focus;
	
	private Color back;
	
	protected static Color DarkBlue = new Color(5, 105, 240, 100);
	
	protected static Color AlphaWhite = new Color(255, 255, 255, 100);
	
	protected static Color AlphaDarkBlue = new Color(5, 105, 240, 80);
	
	protected static Color LightBack = new Color(244, 244, 244);
	
	
	public HighlightPanel() {
		content = null;
		focus = false;
		back = null;
		comp = null;
		
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 6));
		
		focusListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				focus = true;
				back = getBackground();
				setBackground(LightBack);
				repaint();
				//comp.repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				focus = false;
				setBackground(back);
				repaint();
				//comp.repaint();
			}
		};
		this.addMouseListener(focusListener);
	}
	
	
	@Override
	public Component add(Component c) {
		return c;
	}
	@Override
	public Component add(Component c, int index) {
		return c;
	}
	@Override
	public void add(Component c, Object o) {}
	@Override
	public void add(Component c, Object o, int i) {}
	@Override
	public Component add(String s, Component c) {
		return c;
	}
	
	
	public void setContent(Object c) {
		content = c;
		if(content == null) return;
		if(content instanceof Component) {
			comp = (Component) content;
		} else {
			comp = new JLabel(content.toString());
		}
		comp.addMouseListener(focusListener);
		super.add(comp);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g.create();

		if(!focus) {
			g2.setColor(AlphaDarkBlue);
			g2.drawRoundRect(0, 0, this.getSize().width -1, this.getSize().height -1, 10, 10);
			return;
		}
		
		g2.setStroke(new BasicStroke(3f));
		g2.setColor(DarkBlue);
		g2.drawRoundRect(1, 1, this.getSize().width -3, this.getSize().height -3, 10, 10);
		g2.setStroke(new BasicStroke(1f));
		g2.setColor(AlphaWhite);
		g2.drawRoundRect(1, 1, this.getSize().width -3, this.getSize().height -3, 10, 10);
		g2.setColor(AlphaDarkBlue);
		g2.drawRoundRect(2, 2, this.getSize().width -5, this.getSize().height -5, 10, 10);
		g2.dispose();
	}
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame("HighlightPanel");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
		
		HighlightPanel hp = new HighlightPanel();
		hp.setContent("Highlight!!");
		
		f.add(hp);
		f.pack();
		f.setVisible(true);
	}
	
}
