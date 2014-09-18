package com.power.visual;

import com.power.visual.frames.TranslucencyEffect;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 *
 * @author f6036477
 */
public class GlassRobot extends JWindow {
	
	private JFrame owner;
	
	private JPanel panel;
	
	private JTextField field;
	
	private TranslucencyEffect trans;
	
	private Robot robot;
	
	private boolean event;
	
	private ConcurrentLinkedQueue<Integer> keys = new ConcurrentLinkedQueue<Integer>();
	
	private ConcurrentLinkedQueue<Integer> mouse = new ConcurrentLinkedQueue<Integer>();
	
	private Timer noEvent;
	
	
	protected GlassRobot(JFrame f) {
		super(f);
		
		this.owner = f;
		owner.setSize(2, 2);
		owner.setUndecorated(true);
		owner.setLocationRelativeTo(this);
		event = false;
		
		noEvent = new Timer(280, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				releaseEvents();
			}
		});
		noEvent.setRepeats(false);
		
		//
		
		panel = new JPanel();
		field = new JTextField();
		trans = new TranslucencyEffect();
		try {
			robot = new Robot();
		} catch(AWTException ex) {
			Logger.getLogger(GlassRobot.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}
		
		panel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				noEvent.stop();
				int w = (2000 + e.getScrollAmount()) * e.getWheelRotation();
				mouse.add(w);
				noEvent.start();
			}
		});
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				noEvent.stop();
				int btn = 0;
				if(e.getButton() == MouseEvent.BUTTON1) {
					btn = InputEvent.BUTTON1_MASK;
				} else if(e.getButton() == MouseEvent.BUTTON2) {
					btn = InputEvent.BUTTON2_MASK;
				} else if(e.getButton() == MouseEvent.BUTTON3) {
					btn = InputEvent.BUTTON3_MASK;
				}
				mouse.add(btn);
				noEvent.start();
			}
		});
		
		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_CONTROL
						|| e.getKeyCode() == KeyEvent.VK_SHIFT
						|| e.getKeyCode() == KeyEvent.VK_ALT) {
					System.out.println("Modifiers");
					return;
				}
				noEvent.stop();//8--
				int k = e.getKeyCode() | e.getModifiersEx();
				System.out.println(k + " == " + e.getKeyCode());
				System.out.println(e.getKeyChar()+" - "+KeyEvent.getKeyText(k));
				
				if(e.getKeyCode() == e.VK_X && e.isControlDown())
					System.exit(0);
				
				keys.add(k);
				noEvent.start();
			}
		});
		
		panel.add(field);
		this.add(panel);
		
		trans.setTranslucency(0.01f);
		trans.addWindow(this);
		trans.addWindow(owner);
		trans.applyEffect();
		
		this.setLocation(0, 0);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	}
	
	
	private void releaseEvents() {
		this.setVisible(false);
		try {	Thread.sleep(20);	} catch(InterruptedException ex) {}
		
		if(!keys.isEmpty()) {
			while(!keys.isEmpty()) {
				int k = keys.poll();
				System.out.println(KeyEvent.getKeyText(k));
				robot.keyPress(k);
				robot.keyRelease(k);
			}
		}
		if(!mouse.isEmpty()) {
			while(!mouse.isEmpty()) {
				int m = mouse.poll();
				if(m > 2000) {
					System.out.println("scroll = "+(m-2000));
					robot.mouseWheel(m-2000);
				} else if(m < -2000) {
					System.out.println("scroll = "+(m+2000));
					robot.mouseWheel(m+2000);
				} else {
					System.out.println("click: button_");
					robot.mousePress(m);
					robot.mouseRelease(m);
				}
			}
		}
		this.setVisible(true);
	}
	
	
	public void setVisible(boolean v) {
		owner.setVisible(v);
		super.setVisible(v);
		if(v)
			field.requestFocus();
	}
	
	
	public GlassRobot() {
		this(new JFrame());
	}
	
	
	public static void main(String[] args) {
		GlassRobot gr = new GlassRobot();
		gr.setVisible(true);
	}

}
