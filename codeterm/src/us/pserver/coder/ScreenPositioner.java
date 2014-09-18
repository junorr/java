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

package us.pserver.coder;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 08/09/2014
 */
public class ScreenPositioner {

  
  public static Dimension getScreenDimension() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }
  
  
  public static Point getCenterScreenPoint() {
    Dimension d = getScreenDimension();
    int x = d.width / 2;
    int y = d.height / 2;
    return new Point(x, y);
  }
  
  
  public static Point getCenterScreenPoint(Window w) {
    if(w == null) return getCenterScreenPoint();
    Dimension d = getScreenDimension();
    int x = (d.width - w.getWidth()) / 2;
    int y = (d.height - w.getHeight()) / 2;
    return new Point(x, y);
  }
  
  
  public static Point getZeroPoint() {
    return new Point(0, 0);
  }
  
  
  public static Point getCenterWindowPoint(Window main, Window sec) {
    if(main == null) return getZeroPoint();
    Point mp = main.getLocationOnScreen();
    Dimension md = main.getSize();
    int x = mp.x + md.width / 2;
    int y = mp.y + md.height / 2;
    if(sec == null) return new Point(x, y);
    x -= sec.getWidth() / 2;
    y -= sec.getHeight() / 2;
    return new Point(x, y);
  }
  
}
