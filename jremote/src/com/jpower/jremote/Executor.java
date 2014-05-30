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

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.util.LinkedList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class Executor {
  
  private Robot robot;
  
  
  public Executor() {
    try {
      robot = new Robot();
      robot.setAutoDelay(10);
    } catch(AWTException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public Executor exec(Command c) {
    if(c == null || c.isEmpty())
      return this;
    c.execute(robot);
    return this;
  }
  
  
  public static void main(String[] args) {
    Executor e = new Executor();
    MouseAction m = new MouseAction(Action.TYPE.PRESS_ACTION, new Point(750, 500), MouseAction.BUTTON3, 0);
    Command c = new Command(m);
    m = new MouseAction(Action.TYPE.RELEASE_ACTION, new Point(750, 500), MouseAction.BUTTON3, 0);
    c.add(m);
    e.exec(c);
  }

}
