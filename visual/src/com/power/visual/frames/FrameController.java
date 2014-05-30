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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Classe que implementa as funcionalidades padrão
 * esperadas de uma janela da área de trabalho, como
 * mover, minimizar, maximizar e redimensionar.
 * Classe que necessitam utilizar <code>FrameController</code>
 * devem implementar a interface <code>com.power.visual.ControllableFrame</code>.
 * @author Juno Roesler - juno.rr@gmail.com
 * @see com.power.visual.ControllableFrame
 */
public class FrameController implements MouseListener, MouseMotionListener, ComponentListener {

	/**
	 * Tamanho padrão da área de redimensionamento da janela, em pixels (6).
	 */
	public static final int DEFAULT_RESIZE_WIDTH_AREA = 6;
	
	
	private Point origin;
	
	private ControllableFrame frame;

	private Rectangle resW, resE, resN, resS, resNW, resSW, resNE, resSE;
	
	private int resizeWidth;
	
	
	/**
	 * Construtor padrão que recebe uma instância 
	 * de <code>com.power.visual.ControllableFrame</code>
	 * como parâmetro.
	 * @param f <code>com.power.visual.ControllableFrame</code>
	 */
	public FrameController(ControllableFrame f) {
		if(f == null)
			throw new IllegalArgumentException(
					"ControllableFrame must be not null");
		
		this.frame = f;
		frame.addComponentListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		
		resizeWidth = DEFAULT_RESIZE_WIDTH_AREA;
		origin = null;
		resW = null;
		resE = null;
		resN = null;
		resS = null;
		resNW = null;
		resSW = null;
		resNE = null;
		resSE = null;
	}
	
	
	/**
	 * Define o tamanho da área sensível de redimensionamento
	 * da janela.
	 * @param resize tamanho da área de redimensionamento em pixels.
	 */
	public void setResizeWidthArea(int resize) {
		if(resize >= 0)
			this.resizeWidth = resize;
	}
	
	/**
	 * Retorna o tamanho da área sensível de redimensionamento
	 * da janela.
	 * @return tamanho da área de redimensionamento em pixels.
	 */
	public int getResizeWidthArea() {
		return this.resizeWidth;
	}
	
	
	/**
	 * Cria o contorno das áreas de redimensionamento da janela.
	 */
	private void createResizeAreas() {
		resW = new Rectangle(0, resizeWidth, resizeWidth, 
				frame.getHeight() - resizeWidth * 2);
		resE = new Rectangle(frame.getWidth() - resizeWidth, resizeWidth,
				resizeWidth, frame.getHeight() - resizeWidth * 2);
		resN = new Rectangle(resizeWidth, 0, 
				frame.getWidth() - resizeWidth * 2, resizeWidth);
		resS = new Rectangle(resizeWidth, frame.getHeight() - resizeWidth,
				frame.getWidth() - resizeWidth * 2, resizeWidth);
		resNW = new Rectangle(0, 0, resizeWidth, resizeWidth);
		resSW = new Rectangle(0, frame.getHeight() - resizeWidth,
				resizeWidth, resizeWidth);
		resNE = new Rectangle(frame.getWidth() - resizeWidth, 0,
				resizeWidth, resizeWidth);
		resSE = new Rectangle(frame.getWidth() - resizeWidth, 
				frame.getHeight() - resizeWidth, resizeWidth, resizeWidth);
	}


	/**
	 * "Ouve" cliques do mouse na janela controlada,
	 * implementando as funcionalidades de minimizar e
	 * maximizar a janela.
	 * @param e java.awt.event.MouseEvent
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseEvent.html">java.awt.event.MouseEvent</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseListener.html">java.awt.event.MouseListener</a>
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(frame.getTitleBarArea() == null) return;
		
		//Iconify
		if(frame.getTitleBarArea().contains(e.getPoint())
				&& e.getButton() == MouseEvent.BUTTON3)
			frame.setExtendedState(Frame.ICONIFIED);
				
		//if not resizable 
		else if(!frame.isResizable())
			return;
				
		//Maximize
		else if(frame.getTitleBarArea().contains(e.getPoint())
				&& e.getButton() == MouseEvent.BUTTON1
				&& e.getClickCount() == 2) {
			
			if(frame.getExtendedState() != Frame.MAXIMIZED_BOTH) {
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				Insets si = Toolkit.getDefaultToolkit()
						.getScreenInsets( GraphicsEnvironment
						.getLocalGraphicsEnvironment()
						.getDefaultScreenDevice()
						.getDefaultConfiguration() );
				frame.setMaximizedBounds(new Rectangle(
						si.top, si.left, d.width - si.right, 
						d.height - si.bottom));
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.repaint();
				
			} else {
				frame.setExtendedState(Frame.NORMAL);
				frame.repaint();
			}
		}
	}

	/**
	 * "Ouve" o pressionamento dos botões do mouse na janela controlada,
	 * implementando as funcionalidades de arrastar e redimensionar.
	 * @param e java.awt.event.MouseEvent
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseEvent.html">java.awt.event.MouseEvent</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseListener.html">java.awt.event.MouseListener</a>
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(frame.getTitleBarArea() != null 
				&& frame.getTitleBarArea().contains(e.getPoint()))
			origin = e.getPoint();
		else if(resizeWidth > 0 
				&& (resW.contains(e.getPoint())
				|| resE.contains(e.getPoint())
				|| resS.contains(e.getPoint())
				|| resN.contains(e.getPoint())
				|| resNW.contains(e.getPoint())
				|| resSW.contains(e.getPoint())
				|| resSE.contains(e.getPoint())
				|| resNE.contains(e.getPoint())))
			origin = e.getPoint();
	}


	/**
	 * "Ouve" a liberação dos botões do mouse na janela controlada.
	 * @param e java.awt.event.MouseEvent
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseEvent.html">java.awt.event.MouseEvent</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseListener.html">java.awt.event.MouseListener</a>
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		origin = null;
	}


	@Override	public void mouseEntered(MouseEvent e) {}
	@Override	public void mouseExited(MouseEvent e) {}


	/**
	 * "Ouve" a ação de arrastar o mouse na janela controlada,
	 * implementando as funcionalidades de arrastar e redimensionar.
	 * @param e java.awt.event.MouseEvent
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseEvent.html">java.awt.event.MouseEvent</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseListener.html">java.awt.event.MouseListener</a>
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if(origin == null) return;
		if(frame.getSize() == null
				|| frame.getLocation() == null) return;
		
		Cursor c = frame.getCursor();
		Point p = e.getPoint();
		int w = 0, h = 0;
		
		//Move frame
		if(c == null || c.getType() == Cursor.DEFAULT_CURSOR) {
			w = p.x - origin.x;
			h = p.y - origin.y;
			w += frame.getLocation().x;
			h += frame.getLocation().y;
			frame.setLocation(w, h);
			
		//Resize frame
		} else {
			w = p.x - origin.x;
			h = p.y - origin.y;
			
			// NW resize
			if(c.getType() == Cursor.NW_RESIZE_CURSOR) {
				frame.setLocation(frame.getLocation().x + w, frame.getLocation().y + h);
				w = frame.getSize().width - w;
				h = frame.getSize().height - h;
			// SW resize
			} else if(c.getType() == Cursor.SW_RESIZE_CURSOR) {
				frame.setLocation(frame.getLocation().x + w, frame.getLocation().y);
				w = frame.getSize().width - w;
				h = frame.getSize().height + h;
				origin = new Point(origin.x, p.y);
			// SE resize
			} else if(c.getType() == Cursor.SE_RESIZE_CURSOR) {
				w = frame.getSize().width + w;
				h = frame.getSize().height + h;
				origin = p;
			// NE resize
			} else if(c.getType() == Cursor.NE_RESIZE_CURSOR) {
				frame.setLocation(frame.getLocation().x, frame.getLocation().y + h);
				w = frame.getSize().width + w;
				h = frame.getSize().height - h;
				origin = new Point(p.x, origin.y);
			// N resize
			} else if(c.getType() == Cursor.N_RESIZE_CURSOR) {
				frame.setLocation(frame.getLocation().x, frame.getLocation().y + h);
				w = frame.getSize().width;
				h = frame.getSize().height - h;
			// S resize
			} else if(c.getType() == Cursor.S_RESIZE_CURSOR) {
				w = frame.getSize().width;
				h = frame.getSize().height + h;
				origin = new Point(origin.x, p.y);
			// W resize
			} else if(c.getType() == Cursor.W_RESIZE_CURSOR) {
				frame.setLocation(frame.getLocation().x + w, frame.getLocation().y);
				w = frame.getSize().width - w;
				h = frame.getSize().height;
			// E resize
			} else if(c.getType() == Cursor.E_RESIZE_CURSOR) {
				w = frame.getSize().width + w;
				h = frame.getSize().height;
				origin = new Point(p.x, origin.y);
			}
			
			frame.setSize(w, h);
			frame.repaint();
		}
	}


	/**
	 * "Ouve" o movimento do mouse na janela controlada,
	 * alterando o ícone do cursor, case esteja em uma 
	 * das áreas sensíveis à redimensionamento.
	 * @param e java.awt.event.MouseEvent
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseEvent.html">java.awt.event.MouseEvent</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/MouseListener.html">java.awt.event.MouseListener</a>
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		if(!frame.isResizable()) {
			return;
		//nw area
		} else if(resNW.contains(e.getPoint())) {
			frame.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
		//w area
		} else if(resW.contains(e.getPoint())) {
			frame.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		//sw area
		} else if(resSW.contains(e.getPoint())) {
			frame.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
		//s area
		} else if(resS.contains(e.getPoint())) {
			frame.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
		//se area
		} else if(resSE.contains(e.getPoint())) {
			frame.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
		//e area
		} else if(resE.contains(e.getPoint())) {
			frame.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		//ne area
		} else if(resNE.contains(e.getPoint())) {
			frame.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
		//n area
		} else if(resN.contains(e.getPoint())) {
			frame.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
		//default cursor
		} else {
			frame.setCursor(Cursor.getDefaultCursor());
		}
	}
	
	
	@Override	public void componentMoved(ComponentEvent e) {}
	@Override	public void componentHidden(ComponentEvent e) {}
	
	
	/**
	 * "Ouve" quando a janela controlada é redimensionada.
	 * @param e java.awt.event.ComponentEvent
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/ComponentEvent.html">java.awt.event.ComponentEvent</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/ComponentListener.html">java.awt.event.ComponentListener</a>
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		this.createResizeAreas();
	}
	
	
	/**
	 * "Ouve" quando a janela controlada é exibida.
	 * @param e java.awt.event.ComponentEvent
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/ComponentEvent.html">java.awt.event.ComponentEvent</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/event/ComponentListener.html">java.awt.event.ComponentListener</a>
	 */
	@Override
	public void componentShown(ComponentEvent e) {
		this.createResizeAreas();
	}
	
}
