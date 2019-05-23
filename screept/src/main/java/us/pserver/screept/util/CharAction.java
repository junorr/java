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

package us.pserver.screept.util;

import java.awt.Robot;
import java.util.function.Consumer;
import us.pserver.screept.KeyComboAction;
import us.pserver.screept.KeyTypeAction;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23 de mai de 2019
 */
public enum CharAction {

  ZERO('0', new KeyTypeAction(0x30)),
  ONE('1', new KeyTypeAction(0x31)),
  TWO('2', new KeyTypeAction(0x32)),
  THREE('3', new KeyTypeAction(0x33)),
  FOUR('4', new KeyTypeAction(0x34)),
  FIVE('5', new KeyTypeAction(0x35)),
  SIX('6', new KeyTypeAction(0x36)),
  SEVEN('7', new KeyTypeAction(0x37)),
  EIGHT('8', new KeyTypeAction(0x38)),
  NINE('9', new KeyTypeAction(0x39)),
  
  A('A', new KeyTypeAction(0x41)),
  B('B', new KeyTypeAction(0x42)),
  C('C', new KeyTypeAction(0x43)),
  D('D', new KeyTypeAction(0x44)),
  E('E', new KeyTypeAction(0x45)),
  F('F', new KeyTypeAction(0x46)),
  G('G', new KeyTypeAction(0x47)),
  H('H', new KeyTypeAction(0x48)),
  I('I', new KeyTypeAction(0x49)),
  J('J', new KeyTypeAction(0x4A)),
  K('K', new KeyTypeAction(0x4B)),
  L('L', new KeyTypeAction(0x4C)),
  M('M', new KeyTypeAction(0x4D)),
  N('N', new KeyTypeAction(0x4E)),
  O('O', new KeyTypeAction(0x4F)),
  P('P', new KeyTypeAction(0x50)),
  Q('Q', new KeyTypeAction(0x51)),
  R('R', new KeyTypeAction(0x52)),
  S('S', new KeyTypeAction(0x53)),
  T('T', new KeyTypeAction(0x54)),
  U('U', new KeyTypeAction(0x55)),
  V('V', new KeyTypeAction(0x56)),
  W('W', new KeyTypeAction(0x57)),
  X('X', new KeyTypeAction(0x58)),
  Y('Y', new KeyTypeAction(0x59)),
  Z('Z', new KeyTypeAction(0x5A)),
  
  QUOTE('\'', new KeyTypeAction(0xDE)),
  QUOTEDBL('"', new KeyTypeAction(0x98)),
  EXCLAMATION('!', new KeyTypeAction(0x0205)),
  AT('@', new KeyTypeAction(0x0200)),
  SHARP('#', new KeyTypeAction(0x0208)),
  DOLLAR('$', new KeyTypeAction(0x0203)),
  PERCENT('%', new KeyComboAction(0x10, 0x35)),
  AMPERSAND('&', new KeyTypeAction(0x96)),
  ASTERISK('*', new KeyTypeAction(0x97)),
  PARENTHESISL('(', new KeyTypeAction(0x0207)),
  PARENTHESISR(')', new KeyTypeAction(0x020A)),
  MINUS('-', new KeyTypeAction(0x2D)),
  UNDERSCORE('_', new KeyTypeAction(0x020B)),
  PLUS('+', new KeyTypeAction(0x0209)),
  EQUALS('=', new KeyTypeAction(0x3D)),
  
  CEDIL('Ç', new KeyTypeAction(0x8B)),
  BRACKETL('[', new KeyTypeAction(0xA1)),
  CURLY_BRACKETL('{', new KeyComboAction(0x10, 0xA1)),
  BRACKETR(']', new KeyTypeAction(0xA2)),
  GRAVE('`', new KeyTypeAction(0x80)),
  TILDE('~', new KeyTypeAction(0x83)),
  QUESTION('?', new KeyComboAction(0x10, 0x2F)),
  ;
  
  private CharAction(char ch, Consumer<Robot> action) {
    this.ch = ch;
    this.action = action;
  }
  
  private final char ch;
  
  private final Consumer<Robot> action;
  
  public char getChar() {
    return ch;
  }
  
  public Consumer<Robot> getAction() {
    return action;
  }
  
}
