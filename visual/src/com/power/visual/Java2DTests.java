package com.power.visual;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author f6036477
 */
public class Java2DTests extends JPanel {
	
	public static final Color HEADER_OUT_COLOR = new Color(46, 70, 120);
	
	public static final Color HEADER_IN_COLOR = new Color(165, 194, 228);

	public static final int HEADER_HEIGHT = 30;
	
	public static final int ANGLE_WIDTH = 10;
	
	public static final int ANGLE_CTRL = 1;
	
	
	private BufferedImage buffer;
	
	public Java2DTests() {
		super();
	}
	
	public BufferedImage getBufferedImage() {
		if(!this.isVisible()) return null;
		if(buffer == null || (buffer != null
				&& (buffer.getWidth() != this.getWidth()
				||  buffer.getHeight() != this.getHeight()))) {
			buffer = new BufferedImage(this.getWidth(), this.getHeight(),
					BufferedImage.TRANSLUCENT);
		}
		return buffer;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(this.getBufferedImage() == null) return;
		
		GeneralPath header = new GeneralPath();
		header.moveTo(0, HEADER_HEIGHT);
		header.lineTo(this.getWidth(), HEADER_HEIGHT);
		header.lineTo(this.getWidth(), ANGLE_WIDTH);
		header.quadTo(this.getWidth() -ANGLE_CTRL, ANGLE_CTRL, 
				this.getWidth() -ANGLE_WIDTH, 0);
		header.lineTo(ANGLE_WIDTH, 0);
		header.quadTo(ANGLE_CTRL, ANGLE_CTRL, 0, ANGLE_WIDTH);
		
		Graphics2D bg = buffer.createGraphics();
		bg.setComposite(AlphaComposite.Clear); 
		bg.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		bg.setComposite(AlphaComposite.Src);
		bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		bg.setPaint(new GradientPaint(0, 0, HEADER_OUT_COLOR, 
				0, HEADER_HEIGHT / 2f, HEADER_IN_COLOR));
		bg.fill(header);
		bg.setPaint(new GradientPaint(0, HEADER_HEIGHT / 2f, HEADER_IN_COLOR, 
				0, HEADER_HEIGHT, HEADER_OUT_COLOR));
		bg.fillRect(0, HEADER_HEIGHT / 2, this.getWidth(), HEADER_HEIGHT / 2);
		bg.dispose();
		
		g.drawImage(buffer, 0, 0, null);
	}
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Java2DTests");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setContentPane(new Java2DTests());
		f.setSize(300, 300);
		f.setUndecorated(true);
		f.setVisible(true);
	}

}
