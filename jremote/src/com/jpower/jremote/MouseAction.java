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

package com.jpower.jremote;

import java.awt.Point;
import java.awt.Robot;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class MouseAction extends AbstractAction {
  
  public static final int 
      BUTTON1 = 1024,
      BUTTON2 = 2048,
      BUTTON3 = 4096;
  
  
  private Point point;
  
  private int button;
  
  private int wheel;
  
  
  public MouseAction() {
    point = null;
    button = 0;
    wheel = 0;
  }
  
  
  public MouseAction(TYPE t, Point p, int button, int wheel) {
    this.type(t);
    this.setButton(button);
    this.setPoint(p);
    this.setWheel(wheel);
  }


  public Point getPoint() {
    return point;
  }


  public MouseAction setPoint(Point point) {
    this.point = point;
    return this;
  }


  public int getButton() {
    return button;
  }


  public MouseAction setButton(int button) {
    this.button = button;
    return this;
  }


  public int getWheel() {
    return wheel;
  }


  public MouseAction setWheel(int wheel) {
    this.wheel = wheel;
    return this;
  }


  @Override
  public Action execute(Robot r) {
    if(r == null) throw new IllegalArgumentException(
        "Invalid Robot instance: "+ r);
    
    if(point != null)
      r.mouseMove(point.x, point.y);
    if(button != 0) {
      if(type == TYPE.PRESS_ACTION)
        r.mousePress(button);
      else
        r.mouseRelease(button);
    }
    if(wheel != 0)
      r.mouseWheel(wheel);
    return this;
  }
  
  
  public String toString() {
    return "[MouseAction: type: "+ type.name()+ ", point: "+ point+ ", button: "+ button+ ", wheel: "+ wheel+ "]";
  }

}
