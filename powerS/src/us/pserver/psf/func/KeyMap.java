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

package us.pserver.psf.func;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/02/2014
 */
public class KeyMap {

  private Map<String, Integer> map;
  
  
  public KeyMap() {
    map = new HashMap<>();
    map.put("ESC", KeyEvent.VK_ESCAPE);
    map.put("TAB", KeyEvent.VK_TAB);
    map.put("CAPSLOCK", KeyEvent.VK_CAPS_LOCK);
    map.put("SHIFT", KeyEvent.VK_SHIFT);
    map.put("CTRL", KeyEvent.VK_CONTROL);
    map.put("ALT", KeyEvent.VK_ALT);
    map.put("F1", KeyEvent.VK_F1);
    map.put("F2", KeyEvent.VK_F2);
    map.put("F3", KeyEvent.VK_F3);
    map.put("F4", KeyEvent.VK_F4);
    map.put("F5", KeyEvent.VK_F5);
    map.put("F6", KeyEvent.VK_F6);
    map.put("F7", KeyEvent.VK_F7);
    map.put("F8", KeyEvent.VK_F8);
    map.put("F9", KeyEvent.VK_F9);
    map.put("F10", KeyEvent.VK_F10);
    map.put("F11", KeyEvent.VK_F11);
    map.put("F12", KeyEvent.VK_F12);
    map.put("BKSPACE", KeyEvent.VK_BACK_SPACE);
    map.put("BACKSPACE", KeyEvent.VK_BACK_SPACE);
    map.put("PRINTSCREEN", KeyEvent.VK_PRINTSCREEN);
    map.put("SCROLLLOCK", KeyEvent.VK_SCROLL_LOCK);
    map.put("SCRLOCK", KeyEvent.VK_SCROLL_LOCK);
    map.put("PAUSE", KeyEvent.VK_PAUSE);
    map.put("INSERT", KeyEvent.VK_INSERT);
    map.put("HOME", KeyEvent.VK_HOME);
    map.put("PGDOWN", KeyEvent.VK_PAGE_UP);
    map.put("PGUP", KeyEvent.VK_PAGE_DOWN);
    map.put("PAGEDOWN", KeyEvent.VK_PAGE_UP);
    map.put("PAGEUP", KeyEvent.VK_PAGE_DOWN);
    map.put("END", KeyEvent.VK_END);
    map.put("DEL", KeyEvent.VK_DELETE);
    map.put("DELETE", KeyEvent.VK_DELETE);
    map.put("NUMLOCK", KeyEvent.VK_NUM_LOCK);
    map.put("ENTER", KeyEvent.VK_ENTER);
  }
  
  
  public Map<String, Integer> getMap() {
    return map;
  }
  
  
  public int getKeyCode(String key) {
    if(key == null || key.isEmpty())
      return -1;
    if(map.containsKey(key))
      return map.get(key);
    else
      return KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
  }
  
  
  public static void main(String[] args) throws AWTException, InterruptedException {
    KeyMap map = new KeyMap();
    String key = "TAB";
    int code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "ESC";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "0";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "?";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "/";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "|";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "\\";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "@";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "$";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "£";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "~";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "´";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "+";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "+";
    code = KeyEvent.VK_PLUS;
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "+";
    code = KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "a";
    code = map.getKeyCode(key);
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "a";
    code = KeyEvent.VK_A;
    System.out.println("* Key="+ key+ ", code="+ code);
    
    key = "a";
    code = KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
    System.out.println("* Key="+ key+ ", code="+ code);
  }
  
}
