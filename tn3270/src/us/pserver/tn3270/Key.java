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

package us.pserver.tn3270;

import java.awt.event.KeyEvent;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/07/2013
 */
public enum Key {
  
  PF1              (0xF1),
  PF2              (0xF2),
  PF3              (0xF3),
  PF4              (0xF4),
  PF5              (0xF5),
  PF6              (0xF6),
  PF7              (0xF7),
  PF8              (0xF8),
  PF9              (0xF9),
  PF10             (0x7A),
  PF11             (0x7B),
  PF12             (0x7C),
  PF13             (0xC1),
  PF14             (0xC2),
  PF15             (0xC3),
  PF16             (0xC4),
  PF17             (0xC5),
  PF18             (0xC7),
  PF19             (0xC8),
  PF20             (0xC9),
  PF21             (0xC),
  PF22             (0x4A),
  PF23             (0x4B),
  PF24             (0x4C),
  
  ENTER            (0x7D),
  BACKSPACE        (9000),
  HOME             (9001),
  DELETE           (9002),
  LEFT             (9003),
  RIGHT            (9004),
  UP               (9005),
  DOWN             (9006),
  TAB              (9007),
  BACK_TAB         (9008),
  SHIFT_TAB        (9009);
  
  
  Key(int value) {
    this.value = (short) value;
  }
  
  
  private short value;
  
  
  public short value() {
    return value;
  }
  
  
  public static Key getByKeyCode(int keyCode) {
    switch(keyCode) {
      case KeyEvent.VK_F1:
        return PF1;
      case KeyEvent.VK_F2:
        return PF2;
      case KeyEvent.VK_F3:
        return PF3;
      case KeyEvent.VK_F4:
        return PF4;
      case KeyEvent.VK_F5:
        return PF5;
      case KeyEvent.VK_F6:
        return PF6;
      case KeyEvent.VK_PAGE_UP:
      case KeyEvent.VK_F7:
        return PF7;
      case KeyEvent.VK_PAGE_DOWN:
      case KeyEvent.VK_F8:
        return PF8;
      case KeyEvent.VK_F9:
        return PF9;
      case KeyEvent.VK_F10:
        return PF10;
      case KeyEvent.VK_F11:
        return PF11;
      case KeyEvent.VK_F12:
        return PF12;
      case KeyEvent.VK_F13:
        return PF13;
      case KeyEvent.VK_F14:
        return PF14;
      case KeyEvent.VK_F15:
        return PF15;
      case KeyEvent.VK_F16:
        return PF16;
      case KeyEvent.VK_F17:
        return PF17;
      case KeyEvent.VK_F18:
        return PF18;
      case KeyEvent.VK_F19:
        return PF19;
      case KeyEvent.VK_F20:
        return PF20;
      case KeyEvent.VK_F21:
        return PF21;
      case KeyEvent.VK_F22:
        return PF22;
      case KeyEvent.VK_F23:
        return PF23;
      case KeyEvent.VK_F24:
        return PF24;
      case KeyEvent.VK_ENTER:
        return ENTER;
      case KeyEvent.VK_BACK_SPACE:
        return BACKSPACE;
      case KeyEvent.VK_HOME:
        return HOME;
      case KeyEvent.VK_DELETE:
        return DELETE;
      case KeyEvent.VK_LEFT:
        return LEFT;
      case KeyEvent.VK_RIGHT:
        return RIGHT;
      case KeyEvent.VK_UP:
        return UP;
      case KeyEvent.VK_DOWN:
        return DOWN;
      case KeyEvent.VK_TAB:
        return TAB;
      default:
        return null;
    }
  }
  
  
  public boolean isFunctionKey() {
    return isFunctionKey(this);
  }
  
  
  public static boolean isFunctionKey(Key key) {
    return isFunctionKey(key.value());
  }
  
  
  public static boolean isFunctionKey(int key) {
    switch(key) {
      case 0xF1:
      case 0xF2:
      case 0xF3:
      case 0xF4:
      case 0xF5:
      case 0xF6:
      case 0xF7:
      case 0xF8:
      case 0xF9:
      case 0x7A:
      case 0x7B:
      case 0x7C:
      case 0xC1:
      case 0xC2:
      case 0xC3:
      case 0xC4:
      case 0xC5:
      case 0xC7:
      case 0xC8:
      case 0xC9:
      case 0xC:
      case 0x4A:
      case 0x4B:
      case 0x4C:
      case 0x7D:
        return true;
      default:
        return false;
    }
  }
  
}
