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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import javax.swing.JWindow;

/**
 * Implementa o efeito visual de sombra abaixo e
 * à direita da janela.
 * @author Juno Roesler - juno.rr@gmail.com
 * @see com.power.visual.AbstractWindowEffect
 * @see com.power.visual.WindowEffect
 */
public class ShadowEffect extends AbstractWindowEffect
		implements ComponentListener, WindowListener {
		
	/**
	 * Percentual padrão de transparência da sombra (0.5).
	 */
	public static final float DEFAULT_OPACITY = 0.5f;
		
	/**
	 * Tamanho padrão da sombra em pixels (6).
	 */
	public static final int DEFAULT_SHADOW_WIDTH = 5;
	
	/**
	 * Distância do início de ângulo de arredondamento da borda da sombra (10).
	 */
	public static final int ANGLE_WIDTH = 10;
	
	/**
	 * Distância do canto da borda pela qual passa a curva de arredondamento (1).
	 */
	public static final int ANGLE_CTRL = 1;
	
	/**
	 * Tamanho da parte da sombra que ficará sob o componente sombreado, em pixels (2).
	 */
	public static final int SHADOW_SIZE_UNDER_COMPONENT = 2;
	
	
	private int angleWidth, angleCtrl, shwUndComp;
	
	private float opacity;
		
	private int shwWidth;
	
	private boolean roundedCorner;
	
	private GeneralPath shadowShape;
	
	private Window src;
	
	private JWindow shadow;
		
	
	/**
	 * Construtor padrão.
	 * @see com.power.visual.AbstractWindowEffect
	 */
	public ShadowEffect() {
		super();
		this.setOpacity(DEFAULT_OPACITY);
		this.setShadowWidth(DEFAULT_SHADOW_WIDTH);
		angleWidth = ANGLE_WIDTH;
		angleCtrl = ANGLE_CTRL;
		shwUndComp = SHADOW_SIZE_UNDER_COMPONENT;
	}
		
	
	/**
	 * Retorna a instância de java.awt.Window que 
	 * exibe o efeito de sombra da janela.
	 * @return java.awt.Window
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Window.html">java.awt.Window</a>
	 */
	public Window getEffectContainer() {
		return shadow;
	}
	

	@Override	
	public void addWindow(Window w) {
		src = w;
		shadow = new JWindow(src) {
			@Override
			public void paint(Graphics g) {
				draw(g);
			}
		};
		src.addComponentListener(this);
		src.addWindowListener(this);
	}

	
	/**
	 * Retorna o percentual de transparência da sombra.
	 * @return percentual de transparência entre <code>0.0 e 1.0</code>.
	 */
	public float getOpacity() {
		return opacity;
	}

		
	/**
	 * Define o percentual de transparência da sombra.
	 * @param opacity percentual de transparência entre <code>0.0 e 1.0</code>.
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}


	/**
	 * Retorna a espessura da sombra em pixels.
	 * @return espessura da sombra em pixels.
	 */
	public int getShadowWidth() {
		return shwWidth;
	}
	
	
	/**
	 * Retorna a distância antes de começar o ângulo
	 * de curvatura da forma da sombra, que irá 
	 * influenciar no tamanho da curva.
	 * @return distância antes do ângulo
	 * de curvatura.
	 */
	public int getAngleWidth() {
		return angleWidth;
	}
	
	
	/**
	 * Define a distância antes de começar o ângulo
	 * de curvatura da forma da sombra, que irá 
	 * influenciar no tamanho da curva.
	 * @param angle distância antes do ângulo
	 * de curvatura.
	 */
	public void setAngleWidth(int angle) {
		if(angle >= 0) angleWidth = angle;
	}

	
	/**
	 * Retorna a distância do canto da janela,
	 * por onde passará a curvatura da forma
	 * da sombra, influenciando no tamanho da
	 * curvatura.
	 * @return distância do canto da janela,
	 * por onde passa a curvatura.
	 */
	public int getAngleCtrl() {
		return angleCtrl;
	}
	
	
	/**
	 * Define a distância do canto da janela,
	 * por onde passará a curvatura da forma
	 * da sombra, influenciando no tamanho da
	 * curvatura.
	 * @param angle distância do canto da janela,
	 * por onde passa a curvatura.
	 */
	public void setAngleCtrl(int angle) {
		if(angle >= 0) angleCtrl = angle;
	}

		
	/**
	 * Define a espessura da sombra em pixels, que deve ser 
	 * alguns pixels maior (2 pixels por padrão) do que a área a ser exibida, 
	 * definido pelo método 
	 * <code>setShadowSizeUnderComponent(int)</code>.
	 * Esses pixels adicionais ficam por baixo da
	 * janela original, para ajuste visual do efeito.
	 * @param width espessura da sombra em pixels.
	 * @see com.power.visual.frames.ShadowEffect#setShadowSizeUnderComponent(int) 
	 */
	public void setShadowWidth(int width) {
		this.shwWidth = width;
	}
	
	
	/**
	 * Retorna o tamanho da parte da sombra que 
	 * ficará sob o componente sombreado, em pixels.
	 * @return tamanho da parte sob o componente.
	 */
	public int getShadowSizeUnderComponent() {
		return shwUndComp;
	}
	
	
	/**
	 * Define o tamanho da parte da sombra que 
	 * ficará sob o componente sombreado, em pixels.
	 * @param size tamanho da parte sob o componente.
	 */
	public void setShadowSizeUnderComponent(int size) {
		if(size >= 0) shwUndComp = size;
	}
	
	
	/**
	 * Retorna <code>true</code> se a sombra será
	 * desenhada com a bordas arrendadas, <code>false</code>
	 * caso contrário.
	 * @return <code>true</code> se a sombra será
	 * desenhada com a bordas arrendadas, <code>false</code>
	 * caso contrário.
	 */
	public boolean isRoundedCorner() {
		return roundedCorner;
	}
	
	
	/**
	 * Define se as bordas da sombra devem possuir 
	 * os cantos arredondados ou não.
	 * @param rounded <code>true</code> para bordas da sombra com
	 * cantos arredondados, <code>false</code> caso contrário. 
	 * @return A instância modificada de ShadowEffect.
	 */
	public ShadowEffect setRoundedCorner(boolean rounded) {
		roundedCorner = rounded;
		return this;
	}
	
	
	/**
	 * Cria o contorno da sombra a ser desenhada.
	 */
	private void createShadowShape() {
		shadowShape = new GeneralPath();
		shadowShape.moveTo(shadow.getWidth() - shwWidth, 0);
		if(roundedCorner)
			this.createRoundedShadowShape();
		else {
			shadowShape.lineTo(shadow.getWidth() - shwWidth, 
					shadow.getHeight() - shwWidth);
			shadowShape.lineTo(0, shadow.getHeight() - shwWidth);
			shadowShape.lineTo(0, shadow.getHeight());
			shadowShape.lineTo(shadow.getWidth(), shadow.getHeight());
			shadowShape.lineTo(shadow.getWidth(), 0);
			shadowShape.closePath();
		}
	}
	
	
	/**
	 * Cria o contorno da sombra a ser desenhada com 
	 * cantos arredondados da borda.
	 */
	private void createRoundedShadowShape() {
		shadowShape.lineTo(shadow.getWidth() - shwWidth, 
				shadow.getHeight() - shwWidth - angleWidth);
		shadowShape.quadTo(shadow.getWidth() - shwWidth - angleCtrl, 
				shadow.getHeight() - shwWidth - angleCtrl, 
				shadow.getWidth() - shwWidth - angleWidth, 
				shadow.getHeight() - shwWidth);
		shadowShape.lineTo(0, shadow.getHeight() - shwWidth);
		shadowShape.quadTo(angleWidth / 2, shadow.getHeight() - angleCtrl, 
				angleWidth, shadow.getHeight());
		shadowShape.lineTo(shadow.getWidth() - angleWidth, 
				shadow.getHeight());
		shadowShape.quadTo(shadow.getWidth() - angleCtrl, 
				shadow.getHeight() - angleCtrl, shadow.getWidth(), 
				shadow.getHeight() - angleWidth);
		shadowShape.lineTo(shadow.getWidth(), angleWidth);
		shadowShape.quadTo(shadow.getWidth() - angleCtrl, angleWidth / 2, 
				shadow.getWidth() - shwWidth, 0);
	}
		
	
	/**
	 * Desenha a sombra.
	 * @param g java.awt.Graphics
	 */
	protected void draw(Graphics g) {
		if(AWTUtilities.isWindowOpaque(shadow))
			AWTUtilities.setWindowOpaque(shadow, false);
		
		this.createShadowShape();

		BufferedImage buf = new BufferedImage(shadow.getWidth(), 
				shadow.getHeight(), BufferedImage.TRANSLUCENT);
		
		Graphics2D g2 = buf.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(20, 20, 20, (int)(255 * opacity)));
		g2.fill(shadowShape);
		g2.dispose();
		
		g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, shadow.getWidth(), shadow.getHeight());
		g2.setComposite(AlphaComposite.Src);
		g.drawImage(buf, 0, 0, null);
	}
		
	
	@Override
	public void applyEffect() {
		if(src == null || !src.isVisible()) return;
		componentShown(null);
		shadow.setVisible(true);
	}


	@Override
	public void removeEffect() {
		if(src == null) return;
		shadow.dispose();
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
		shadow.setSize(src.getWidth() - shwWidth,
				src.getHeight() - shwWidth);
		shadow.setLocation(src.getLocation().x + shwWidth * 2 - shwUndComp, 
				src.getLocation().y + shwWidth * 2 - shwUndComp);
		if(shadow.isVisible())
			shadow.repaint();
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
