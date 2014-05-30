/*
  Direitos Autorais Reservados (c) 2011 Juno Roesler
  Contato: juno.rr@gmail.com
  
  Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
  termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
  Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
  versão posterior.
  
  Esta biblioteca é distribuído na expectativa de que seja útil, porém, SEM
  NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
  OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
  Geral Menor do GNU para mais detalhes.
  
  Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
  com esta biblioteca; se não, escreva para a Free Software Foundation, Inc., no
  endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
*/

package com.power.visual.frames;

import com.sun.awt.AWTUtilities;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JWindow;

/**
 * Implementa o efeito visual de reflexo, como
 * se a janela estivesse em cima de uma superfície
 * reflexiva.
 * @author Juno Roesler - juno.rr@gmail.com
 * @see com.power.visual.AbstractWindowEffect
 * @see com.power.visual.WindowEffect
 */
public class ReflectionEffect extends AbstractWindowEffect 
		implements ComponentListener, WindowListener {

	private JWindow reflex;
	
	private BufferedImage buffer;
	
	private Window src;
	
	
	/**
	 * Construtor padrão.
	 * @see com.power.visual.AbstractWindowEffect
	 */
	public ReflectionEffect() {
		super();
		buffer = null;
	}
	
	
	/**
	 * Retorna a instância de java.awt.Window que 
	 * exibe o efeito de sombra da janela.
	 * @return java.awt.Window
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Window.html">java.awt.Window</a>
	 */
	public Window getEffectContainer() {
		return reflex;
	}
	

	@Override	
	public void addWindow(Window w) {
		if(w == null) return;
    
    this.src = w;
		reflex = new JWindow(src) {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				if(buffer != null)
					g.drawImage(buffer, 0, 0, reflex.getWidth(), 
							reflex.getHeight(), null);
			}
		};
		src.addComponentListener(this);
		src.addWindowListener(this);
	}

		
	@Override
	public void applyEffect() {
		if(src == null || !src.isVisible()) return;
		reflex.setVisible(true);
    System.out.println("ReflectionEffect.applyEffect()");
		componentShown(null);
	}
	
	
	/**
	 * Pinta o reflexo da janela.
	 */
	private void paintReflex() {
		if(AWTUtilities.isWindowOpaque(reflex))
			AWTUtilities.setWindowOpaque(reflex, false);
		
		this.clearBuffer();
		BufferedImage mask = this.createGradientMask();
		this.reflectWindow();
		
		Graphics2D g = buffer.createGraphics();
		g.setComposite(AlphaComposite.DstOut);
		g.drawImage(mask, 0, 0, null);
		g.dispose();
	}
	
	
	/**
	 * Cria uma nova imagem em buffer para desenho do reflexo.
	 */
	private void clearBuffer() {
		buffer = new BufferedImage(src.getWidth(), 
				src.getHeight() / 2, BufferedImage.TRANSLUCENT);
		Graphics2D g = buffer.createGraphics();
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		g.dispose();
	}
	
	
	/**
	 * Cria uma imagem em buffer com o efeito gradiente,
	 * começando em 50% até totalmente transparente.
	 */
	private BufferedImage createGradientMask() {
		BufferedImage mask = new BufferedImage(buffer.getWidth(), 
				buffer.getHeight(), BufferedImage.TRANSLUCENT);
		
		Graphics2D g = mask.createGraphics();
		g.setPaint(new GradientPaint(0, 0, new Color(1f, 1f, 1f, 0.6f), 
				0, mask.getHeight(), new Color(1f, 1f, 1f, 1f)));
		g.fillRect(0, 0, mask.getWidth(), mask.getHeight());
		g.dispose();
		
		return mask;
	}
	
	
	/**
	 * Transforma a imagem refletida da janela,
	 * girando 180°.
	 */
	private void reflectWindow() {
		BufferedImage dst = new BufferedImage(src.getWidth(), 
				src.getHeight(), BufferedImage.TRANSLUCENT);
		Graphics2D g = dst.createGraphics();
		src.paint(g);
		g.dispose();
		
    AffineTransform trans = AffineTransform.getRotateInstance(
      Math.toRadians(180), dst.getWidth()/2., dst.getHeight()/2.);
		
		g = buffer.createGraphics();
		g.drawImage(dst, trans, null);
		g.dispose();
	}


	@Override
	public void removeEffect() {
		if(src == null) return;
		reflex.dispose();
	}


	@Override
	public void componentResized(ComponentEvent e) {
		componentShown(null);
	}


	@Override
	public void componentMoved(ComponentEvent e) {
		componentShown(null);
	}


	@Override
	public void componentShown(ComponentEvent e) {
		this.paintReflex();
		reflex.setSize(src.getWidth(), src.getHeight() / 2);
		reflex.setLocation(src.getLocation().x, 
				src.getLocation().y + src.getHeight());
		if(reflex.isVisible())
			reflex.repaint();
	}


	@Override
	public void windowClosing(WindowEvent e) {
		this.removeEffect();
	}


	@Override
	public void windowIconified(WindowEvent e) {
		this.removeEffect();
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		this.applyEffect();
	}


	@Override	public void componentHidden(ComponentEvent e) {}
	@Override	public void windowOpened(WindowEvent e) {}
	@Override	public void windowClosed(WindowEvent e) {}
	@Override	public void windowActivated(WindowEvent e) {}
	@Override	public void windowDeactivated(WindowEvent e) {}

}
