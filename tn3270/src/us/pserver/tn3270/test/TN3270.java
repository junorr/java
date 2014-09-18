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

package us.pserver.tn3270.test;

import us.pserver.tn3270.Cursor;
import com.ino.freehost.client.RW3270;
import com.ino.freehost.client.RW3270Field;
import com.ino.freehost.client.RWTnAction;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 29/07/2013
 */
public class TN3270 implements RWTnAction {

  private RW3270 sess;
  
  private Point cursor;
  
  public TN3270() {
    sess = new RW3270(this);
  }
  
  
  public void run() {
    sess.connect("172.17.78.220", 8023);
    sess.waitString(20, 39, "SISBB", 5);
    System.out.println("* SISBB OK: ");
    System.out.println(new String(sess.getDisplay()));
    System.out.println("* Fields: ");
    List<RW3270Field> fields = sess.getFields();
    for(int i = 0; i < fields.size(); i++) {
      RW3270Field f = fields.get(i);
      System.out.println("  - Field ("+ Cursor.translate(f.getBegin()+1)+ ", size="+ f.size()+ ", isProtected="+ f.isProtected()+ "):"+ new String(f.getDisplayChars()));
    }
    sess.enter();
    sess.waitString(1, 3, "CIC", 10);
    fields = sess.getFields();
    for(int i = 0; i < fields.size(); i++) {
      RW3270Field f = fields.get(i);
      System.out.println("  - Field (pos="+ Cursor.translate(f.getBegin()+1)+ ", size="+ f.size()+ ", isProtected="+ f.isProtected()+ "):"+ new String(f.getDisplayChars()));
    }
    System.out.println("* sess.getString(1, 3, 3): "+ sess.getString(1, 3, 3));
    sess.disconnect();
  }

  
  public static void main(String[] args) {
    new TN3270().run();
  }
  

  @Override
  public void status(int msg) {
    switch(msg) {
      case RWTnAction.CONNECTION_ERROR:
        System.out.println("* Status: CONNECTION_ERROR");
        break;
      case RWTnAction.DISCONNECTED_BY_REMOTE_HOST:
        System.out.println("* Status: DISCONNECTED_BY_REMOTE_HOST");
        break;
      case RWTnAction.READY:
        System.out.println("* Status: READY");
        break;
      case RWTnAction.X_WAIT:
        System.out.println("* Status: X_WAIT");
        break;
      default:
        System.out.println("* Status: NO_STATUS (should not be here!)");
        break;
    }
  }


  @Override
  public void incomingData() {
    System.out.println("* Incoming Data");
  }


  @Override
  public void cursorMove(int oldPos, int newPos) {
    System.out.println("* Cursor Move: {"+ oldPos+ " -> "+ newPos+ "}");
  }


  @Override
  public void broadcastMessage(String msg) {
    System.out.println("* Broadcast Message: "+ msg);
  }


  @Override
  public void beep() {
    System.out.println("* Beep!");
  }

}
