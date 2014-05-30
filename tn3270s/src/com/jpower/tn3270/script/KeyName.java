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

package com.jpower.tn3270.script;

import com.jpower.tn3270.Key;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public abstract class KeyName {

  public static final String
      
      ENTER = "[ENTER]",
      DELETE = "[DEL]",
      BACKSPACE = "[BACKSPACE]",
      HOME = "[HOME]",
      UP = "[UP]",
      DOWN = "[DOWN]",
      LEFT = "[LEFT]",
      RIGHT = "[RIGHT]",
      TAB = "[TAB]",
      BACKTAB = "[BACKTAB]",
      
      F1 = "[F1]",
      F2 = "[F2]",
      F3 = "[F3]",
      F4 = "[F4]",
      F5 = "[F5]",
      F6 = "[F6]",
      F7 = "[F7]",
      F8 = "[F8]",
      F9 = "[F9]",
      F10 = "[F10]",
      F11 = "[F11]",
      F12 = "[F12]",
      F13 = "[F13]",
      F14 = "[F14]",
      F15 = "[F15]",
      F16 = "[F16]",
      F17 = "[F17]",
      F18 = "[F18]",
      F19 = "[F19]",
      F20 = "[F20]",
      F21 = "[F21]",
      F22 = "[F22]",
      F23 = "[F23]",
      F24 = "[F24]";
  
  
  public static Key parse(String key) {
    if(key == null || key.isEmpty())
      return null;
    
    switch(key) {
      case BACKSPACE:
        return Key.BACKSPACE;
      case BACKTAB:
        return Key.BACK_TAB;
      case DELETE:
        return Key.DELETE;
      case DOWN:
        return Key.DOWN;
      case ENTER:
        return Key.ENTER;
      case F1:
        return Key.PF1;
      case F2:
        return Key.PF2;
      case F3:
        return Key.PF3;
      case F4:
        return Key.PF4;
      case F5:
        return Key.PF5;
      case F6:
        return Key.PF6;
      case F7:
        return Key.PF7;
      case F8:
        return Key.PF8;
      case F9:
        return Key.PF9;
      case F10:
        return Key.PF10;
      case F11:
        return Key.PF11;
      case F12:
        return Key.PF12;
      case F13:
        return Key.PF13;
      case F14:
        return Key.PF14;
      case F15:
        return Key.PF15;
      case F16:
        return Key.PF16;
      case F17:
        return Key.PF17;
      case F18:
        return Key.PF18;
      case F19:
        return Key.PF19;
      case F20:
        return Key.PF20;
      case F21:
        return Key.PF21;
      case F22:
        return Key.PF22;
      case F23:
        return Key.PF23;
      case F24:
        return Key.PF24;
      case HOME:
        return Key.HOME;
      case LEFT:
        return Key.LEFT;
      case RIGHT:
        return Key.RIGHT;
      case TAB:
        return Key.TAB;
      case UP:
        return Key.UP;
      default:
        return null;
    }
  }
  
}
