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

package com.power.visual;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Interface que representa uma uma janela
 * a ser controlada pela class FrameController.
 * O único método adicional à uma instância de
 * java.awt.Frame que deve ser implementado é
 * <code>getTitleBarArea():java.awt.Shape</code>
 * que deve retornar a área (e.g. java.awt.Rectangle)
 * correspondente à barra de título.
 * @see com.power.visual.FrameController
 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
 * @author Juno Roesler - juno.rr@gmail.com
 */
public interface ControllableFrame {

	/**
	 * Retorna a área correspondente à barra
	 * de título da janela.
	 * @return java.awt.Shape
	 */
	public Shape getTitleBarArea();
	
	
	//java.awt.Frame required methods
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public void addMouseListener(MouseListener l);
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public void addMouseMotionListener(MouseMotionListener l);
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public void addComponentListener(ComponentListener l);
	
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public int getWidth();
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public int getHeight();
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public Cursor getCursor();
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public void repaint();
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public void setCursor(Cursor c);
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public void setSize(int width, int height);
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public Dimension getSize();
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public Point getLocation();
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public void setLocation(int x, int y);
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public boolean isResizable();
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public int getExtendedState();
	
	/**
	 * See <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/awt/Frame.html">java.awt.Frame</a>
	 */
	public void setExtendedState(int state);
	
}
