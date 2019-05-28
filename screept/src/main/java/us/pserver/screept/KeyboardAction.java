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

package us.pserver.screept;

import java.awt.Robot;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23 de mai de 2019
 */
public enum KeyboardAction implements Consumer<Robot> {

  _0('0', new KeyTypeAction(0x30)),
  _1('1', new KeyTypeAction(0x31)),
  _2('2', new KeyTypeAction(0x32)),
  _3('3', new KeyTypeAction(0x33)),
  _4('4', new KeyTypeAction(0x34)),
  _5('5', new KeyTypeAction(0x35)),
  _6('6', new KeyTypeAction(0x36)),
  _7('7', new KeyTypeAction(0x37)),
  _8('8', new KeyTypeAction(0x38)),
  _9('9', new KeyTypeAction(0x39)),
  
  KP_0('0', new KeyTypeAction(0x60)),
  KP_1('1', new KeyTypeAction(0x61)),
  KP_2('2', new KeyTypeAction(0x62)),
  KP_3('3', new KeyTypeAction(0x63)),
  KP_4('4', new KeyTypeAction(0x64)),
  KP_5('5', new KeyTypeAction(0x65)),
  KP_6('6', new KeyTypeAction(0x66)),
  KP_7('7', new KeyTypeAction(0x67)),
  KP_8('8', new KeyTypeAction(0x68)),
  KP_9('9', new KeyTypeAction(0x69)),
  
  A_UPPER('A', new KeyComboAction(0x10, 0x41)),
  A_LOWER('a', new KeyTypeAction(0x41)),
  A_GRAVE_UPPER('À', new KeyComboAction(0x10, 0x81, 0x41)),
  A_GRAVE_LOWER('à', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x41))),
  A_ACUTE_UPPER('Á', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x41))),
  A_ACUTE_LOWER('á', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x41))),
  A_CIRC_UPPER('Â', new KeyComboAction(0x10, 0x83, 0x41)),
  A_CIRC_LOWER('â', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x41))),
  A_TILDE_UPPER('Ã', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x41))),
  A_TILDE_LOWER('ã', new KeyComboAction(0x83, 0x41)),
  A_UMBRA_UPPER('Ä', new KeyComboAction(0x10, 0x36, 0x41)),
  A_UMBRA_LOWER('ä', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x41))),

  B_UPPER('B', new KeyComboAction(0x10, 0x42)),
  B_LOWER('b', new KeyTypeAction(0x42)),
  C_UPPER('C', new KeyComboAction(0x10, 0x43)),
  C_LOWER('c', new KeyTypeAction(0x43)),
  C_CEDIL_UPPER('Ç', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x69, 0x69)) : new KeyTypeAction(0x80).andThen(new KeyComboAction(0x10, 0x43))),
  C_CEDIL_LOWER('ç', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x62, 0x63, 0x61)) : new KeyComboAction(0x80, 0x43)),
  D_UPPER('D', new KeyComboAction(0x10, 0x44)),
  D_LOWER('d', new KeyTypeAction(0x44)),
  
  E_UPPER('E', new KeyComboAction(0x10, 0x45)),
  E_LOWER('e', new KeyTypeAction(0x45)),
  E_GRAVE_UPPER('È', new KeyComboAction(0x10, 0x81, 0x45)),
  E_GRAVE_LOWER('è', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x45))),
  E_ACUTE_UPPER('É', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x45))),
  E_ACUTE_LOWER('é', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x45))),
  E_CIRC_UPPER('Ê', new KeyComboAction(0x10, 0x83, 0x45)),
  E_CIRC_LOWER('ê', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x45))),
  E_TILDE_UPPER('Ẽ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x45))),
  E_TILDE_LOWER('ẽ', new KeyComboAction(0x83, 0x45)),
  E_UMBRA_UPPER('Ë', new KeyComboAction(0x10, 0x36, 0x45)),
  E_UMBRA_LOWER('ë', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x45))),

  F_UPPER('F', new KeyComboAction(0x10, 0x46)),
  F_LOWER('f', new KeyTypeAction(0x46)),
  G_UPPER('G', new KeyComboAction(0x10, 0x47)),
  G_LOWER('g', new KeyTypeAction(0x47)),
  H_UPPER('H', new KeyComboAction(0x10, 0x48)),
  H_LOWER('h', new KeyTypeAction(0x48)),
  
  I_UPPER('I', new KeyComboAction(0x10, 0x49)),
  I_LOWER('i', new KeyTypeAction(0x49)),
  I_GRAVE_UPPER('Ì', new KeyComboAction(0x10, 0x81, 0x49)),
  I_GRAVE_LOWER('ì', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x49))),
  I_ACUTE_UPPER('Í', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x49))),
  I_ACUTE_LOWER('í', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x49))),
  I_CIRC_UPPER('Î', new KeyComboAction(0x10, 0x83, 0x49)),
  I_CIRC_LOWER('î', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x49))),
  I_TILDE_UPPER('Ĩ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x49))),
  I_TILDE_LOWER('ĩ', new KeyComboAction(0x83, 0x49)),
  I_UMBRA_UPPER('Ï', new KeyComboAction(0x10, 0x36, 0x49)),
  I_UMBRA_LOWER('ï', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x49))),

  J_UPPER('J', new KeyComboAction(0x10, 0x4A)),
  J_LOWER('j', new KeyTypeAction(0x4A)),
  K_UPPER('K', new KeyComboAction(0x10, 0x4B)),
  K_LOWER('k', new KeyTypeAction(0x4B)),
  L_UPPER('L', new KeyComboAction(0x10, 0x4C)),
  L_LOWER('l', new KeyTypeAction(0x4C)),
  M_UPPER('M', new KeyComboAction(0x10, 0x4D)),
  M_LOWER('m', new KeyTypeAction(0x4D)),
  N_UPPER('N', new KeyComboAction(0x10, 0x4E)),
  N_LOWER('n', new KeyTypeAction(0x4E)),
  
  O_UPPER('O', new KeyComboAction(0x10, 0x4F)),
  O_LOWER('o', new KeyTypeAction(0x4F)),
  O_GRAVE_UPPER('Ò', new KeyComboAction(0x10, 0x81, 0x4F)),
  O_GRAVE_LOWER('ò', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x4F))),
  O_ACUTE_UPPER('Ó', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x4F))),
  O_ACUTE_LOWER('ó', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x4F))),
  O_CIRC_UPPER('Ô', new KeyComboAction(0x10, 0x83, 0x4F)),
  O_CIRC_LOWER('ô', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x4F))),
  O_TILDE_UPPER('Õ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x4F))),
  O_TILDE_LOWER('õ', new KeyComboAction(0x83, 0x4F)),
  O_UMBRA_UPPER('Ö', new KeyComboAction(0x10, 0x36, 0x4F)),
  O_UMBRA_LOWER('ö', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x4F))),

  P_UPPER('P', new KeyComboAction(0x10, 0x50)),
  P_LOWER('p', new KeyTypeAction(0x50)),
  Q_UPPER('Q', new KeyComboAction(0x10, 0x51)),
  Q_LOWER('q', new KeyTypeAction(0x51)),
  R_UPPER('R', new KeyComboAction(0x10, 0x52)),
  R_LOWER('r', new KeyTypeAction(0x52)),
  S_UPPER('S', new KeyComboAction(0x10, 0x53)),
  S_LOWER('s', new KeyTypeAction(0x53)),
  T_UPPER('T', new KeyComboAction(0x10, 0x54)),
  T_LOWER('t', new KeyTypeAction(0x54)),
  
  U_UPPER('U', new KeyComboAction(0x10, 0x55)),
  U_LOWER('u', new KeyTypeAction(0x55)),
  U_GRAVE_UPPER('Ù', new KeyComboAction(0x10, 0x81, 0x55)),
  U_GRAVE_LOWER('ù', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x55))),
  U_ACUTE_UPPER('Ú', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x55))),
  U_ACUTE_LOWER('ú', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x55))),
  U_CIRC_UPPER('Û', new KeyComboAction(0x10, 0x83, 0x55)),
  U_CIRC_LOWER('û', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x55))),
  U_TILDE_UPPER('Ũ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x55))),
  U_TILDE_LOWER('ũ', new KeyComboAction(0x83, 0x55)),
  U_UMBRA_UPPER('Ü', new KeyComboAction(0x10, 0x36, 0x55)),
  U_UMBRA_LOWER('ü', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x55))),

  V_UPPER('V', new KeyComboAction(0x10, 0x56)),
  V_LOWER('v', new KeyTypeAction(0x56)),
  W_UPPER('W', new KeyComboAction(0x10, 0x57)),
  W_LOWER('w', new KeyTypeAction(0x57)),
  X_UPPER('X', new KeyComboAction(0x10, 0x58)),
  X_LOWER('x', new KeyTypeAction(0x58)),
  
  Y_UPPER('Y', new KeyComboAction(0x10, 0x59)),
  Y_LOWER('y', new KeyTypeAction(0x59)),
  Y_GRAVE_UPPER('Ỳ', new KeyComboAction(0x10, 0x81, 0x59)),
  Y_GRAVE_LOWER('ỳ', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x59))),
  Y_ACUTE_UPPER('Ý', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x59))),
  Y_ACUTE_LOWER('ý', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x59))),
  Y_CIRC_UPPER('Ŷ', new KeyComboAction(0x10, 0x83, 0x59)),
  Y_CIRC_LOWER('ŷ', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x59))),
  Y_TILDE_UPPER('Ỹ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x59))),
  Y_TILDE_LOWER('ỹ', new KeyComboAction(0x83, 0x59)),
  Y_UMBRA_UPPER('Ÿ', new KeyComboAction(0x10, 0x36, 0x59)),
  Y_UMBRA_LOWER('ÿ', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x59))),

  Z_UPPER('Z', new KeyComboAction(0x10, 0x5A)),
  Z_LOWER('z', new KeyTypeAction(0x5A)),
  
  SPACE(' ', new KeyTypeAction(0x20)),
  QUOTE('\'', new KeyTypeAction(0xDE)),
  QUOTEDBL('"', new KeyComboAction(0x10, 0xDE)),
  SUPER_1('¹', new KeyComboAction(0xFF7E, 0x31)),
  SUPER_2('²', new KeyComboAction(0xFF7E, 0x32)),
  SUPER_3('³', new KeyComboAction(0xFF7E, 0x33)),
  EXCLAMATION('!', new KeyComboAction(0x10, 0x31)),
  AT('@', new KeyComboAction(0x10, 0x32)),
  SHARP('#', new KeyComboAction(0x10, 0x33)),
  DOLLAR('$', new KeyComboAction(0x10, 0x34)),
  EUR('€', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x62, 0x68)) : new KeyComboAction(0xFF7E, 0x45)),
  POUND('£', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x66, 0x63)) : new KeyComboAction(0xFF7E, 0x34)),
  PERCENT('%', new KeyComboAction(0x10, 0x35)),
  AMPERSAND('&', new KeyComboAction(0x10, 0x37)),
  ASTERISK('*', new KeyComboAction(0x10, 0x38)),
  PARENTHESISL('(', new KeyComboAction(0x10, 0x39)),
  PARENTHESISR(')', new KeyComboAction(0x10, 0x30)),
  MINUS('-', new KeyTypeAction(0x2D)),
  UNDERSCORE('_', new KeyComboAction(0x10, 0x2D)),
  EQUALS('=', new KeyTypeAction(0x3D)),
  PLUS('+', new KeyComboAction(0x10, 0x3D)),
  
  COPYRIGHT('©', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x66, 0x69)) : new KeyComboAction(0xFF7E, 0x43)),
  REGISTERED('®', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x67, 0x64)) : new KeyComboAction(0xFF7E, 0x52)),
  LEFTDBL('«', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x67, 0x61)) : new KeyComboAction(0xFF7E, 0x5A)),
  RIGHTDBL('»', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x68, 0x67)) : new KeyComboAction(0xFF7E, 0x58)),
  ARROW_LEFT('←', OS.isWindows() ? altmod(new NumpadComboAction(0x62, 0x67)) : new KeyComboAction(0xFF7E, 0x59)),
  ARROW_UP('↑', OS.isWindows() ? altmod(new NumpadComboAction(0x62, 0x64)) : new KeyComboAction(0xFF7E, 0x10, 0x55)),
  ARROW_RIGHT('→', OS.isWindows() ? altmod(new NumpadComboAction(0x62, 0x66)) : new KeyComboAction(0xFF7E, 0x49)),
  ARROW_DOWN('↓', OS.isWindows() ? altmod(new NumpadComboAction(0x62, 0x65)) : new KeyComboAction(0xFF7E, 0x55)),
  PARAGRAPH('─', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x65, 0x61)) : new KeyComboAction(0xFF7E, 0x2C)),
  MULTIPLY('×', new KeyComboAction(0xFF7E, 0x10, 0x2C)),
  DIVISION('÷', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x62, 0x64, 0x67)) : new KeyComboAction(0xFF7E, 0x10, 0x2E)),
  FRACTION_1_2('½', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x68, 0x69)) : new KeyComboAction(0xFF7E, 0x10, 0x32)),
  FRACTION_3_4('¾', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x69, 0x60)) : new KeyComboAction(0xFF7E, 0x10, 0x33)),
  FRACTION_1_4('¼', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x68, 0x68)) : new KeyComboAction(0xFF7E, 0x10, 0x34)),
  FRACTION_3_8('⅜', new KeyComboAction(0xFF7E, 0x10, 0x35)),
  FRACTION_7_8('⅞', new KeyComboAction(0xFF7E, 0x10, 0x37)),
  TM('™', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x65, 0x63)) : new KeyComboAction(0xFF7E, 0x10, 0x38)),
  MORE_OR_LESS('±', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x67, 0x67)) : new KeyComboAction(0xFF7E, 0x10, 0x39)),
  BRACKETL('[', new KeyTypeAction(0x5B)),
  BRACKETL_CURLY('{', new KeyComboAction(0x10, 0x5B)),
  SUPER_A('ª', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x67, 0x60)) : new KeyComboAction(0xFF7E, 0xA1)),
  BRACKETR(']', new KeyTypeAction(0x5D)),
  BRACKETR_CURLY('}', new KeyComboAction(0x10, 0x5D)),
  SUPER_O('º', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x68, 0x66)) : new KeyComboAction(0xFF7E, 0xA2)),
  SUPER_DOT('·', new KeyComboAction(0xFF7E, 0x2E)),
  GRAVE('`', new KeyTypeAction(0x80)),
  TILDE('~', new KeyTypeAction(0x83)),
  CIRCUMFLEX('^', new KeyTypeAction(0x82)),
  SLASH('/', OS.isWindows() ? altmod(new NumpadComboAction(0x64, 0x67)) : new KeyTypeAction(0x2F)),
  QUESTION('?', OS.isWindows() ? altmod(new NumpadComboAction(0x66, 0x63)) : new KeyComboAction(0x10, 0x2F)),
  DEGREES('°', OS.isWindows() ? altmod(new NumpadComboAction(0x60, 0x61, 0x67, 0x66)) : new KeyComboAction(0xFF7E, 0x2F)),
  SEMICOLON(';', new KeyTypeAction(0x3B)),
  COLON(':', new KeyComboAction(0x10, 0x3B)),
  DOT('.', new KeyTypeAction(0x2E)),
  GREATER('>', new KeyComboAction(0x10, 0x2E)),
  COMMA(',', new KeyTypeAction(0x2C)),
  LESSER('<', new KeyComboAction(0x10, 0x2C)),
  BACKSLASH('\\', new KeyTypeAction(0x5C)),
  PIPE('|', new KeyComboAction(0x10, 0x5C)),
  
  ENTER('\n', new KeyTypeAction('\n')),
  BACKSPACE('\b', new KeyTypeAction('\b')),
  TAB('\t', new KeyTypeAction('\t')),
  
  INSERT((char)-1, new KeyTypeAction(0x9B)),
  HOME((char)-2, new KeyTypeAction(0x24)),
  PGUP((char)-3, new KeyTypeAction(0x21)),
  DELETE((char)-4, new KeyTypeAction(0x7F)),
  END((char)-5, new KeyTypeAction(0x23)),
  PGDOWN((char)-6, new KeyTypeAction(0x22)),
  
  UP((char)-7, new KeyTypeAction(0x26)),
  LEFT((char)-8, new KeyTypeAction(0x25)),
  DOWN((char)-9, new KeyTypeAction(0x28)),
  RIGHT((char)-14, new KeyTypeAction(0x27)),
  
  SHIFT((char)-10, new KeyTypeAction(0x10)),
  CONTROL((char)-11, new KeyTypeAction(0x11)),
  ALT((char)-12, new KeyTypeAction(0x12)),
  ALTGR((char)-13, new KeyTypeAction(0xFF7E)),
  
  NUM_LOCK((char)-15, new KeyTypeAction(0x90)),
  NUM_LOCK_ENABLED((char)-16, r -> OS.setNumLockEnabled(true)),
  NUM_LOCK_DISABLED((char)-17, r -> OS.setNumLockEnabled(false)),
  SCROLL_LOCK((char)-18, new KeyTypeAction(0x91)),
  SCROLL_LOCK_ENABLED((char)-19, r -> OS.setScrollLockEnabled(true)),
  SCROLL_LOCK_DISABLED((char)-20, r -> OS.setScrollLockEnabled(false)),
  CAPS_LOCK((char)-21, new KeyTypeAction(0x14)),
  CAPS_LOCK_ENABLED((char)-22, r -> OS.setCapsLockEnabled(true)),
  CAPS_LOCK_DISABLED((char)-23, r -> OS.setCapsLockEnabled(false)),
  PRINT_SCREEN((char)-24, new KeyTypeAction(0x9A)),
  META((char)-25, new KeyTypeAction(0x9D)),
  ESCAPE((char)-26, new KeyTypeAction(0x1B)),
  WINDOWS((char)-27, new KeyTypeAction(0x020C)),
  CONTEXT_MENU((char)-28, new KeyTypeAction(0x020D)),
  
  F1((char)-29, new KeyTypeAction(0x70)),
  F2((char)-30, new KeyTypeAction(0x71)),
  F3((char)-31, new KeyTypeAction(0x72)),
  F4((char)-32, new KeyTypeAction(0x73)),
  F5((char)-33, new KeyTypeAction(0x74)),
  F6((char)-34, new KeyTypeAction(0x75)),
  F7((char)-35, new KeyTypeAction(0x76)),
  F8((char)-36, new KeyTypeAction(0x77)),
  F9((char)-37, new KeyTypeAction(0x78)),
  F10((char)-38, new KeyTypeAction(0x79)),
  F11((char)-39, new KeyTypeAction(0x7A)),
  F12((char)-40, new KeyTypeAction(0x7B)),
  
  CUT((char)-41, new KeyComboAction(0x11, 0x58)),
  COPY((char)-42, new KeyComboAction(0x11, 0x43)),
  PASTE((char)-43, new KeyComboAction(0x11, 0x56)),
  UNDO((char)-44, new KeyComboAction(0x11, 0x5A)),
  REDO((char)-45, new KeyComboAction(0x11, 0x10, 0x5A))
  ;
  
  private KeyboardAction(char ch, Consumer<Robot> action) {
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
  
  @Override
  public void accept(Robot r) {
    action.accept(r);
  }
  
  @Override
  public String toString() {
    return String.format("KeyboardAction(%s)", ch);
  }
  
  
  public static Optional<Consumer<Robot>> getCharAction(int c) {
    return Arrays.asList(KeyboardAction.values()).stream()
        .filter(a -> c == a.getChar())
        .map(KeyboardAction::getAction)
        .findAny();
  }
  
  
  public static Optional<KeyboardAction> getKeyboardAction(int c) {
    return Arrays.asList(KeyboardAction.values()).stream()
        .filter(a -> c == a.getChar())
        .findAny();
  }
  
  
  public static Consumer<Robot> shiftmod(Consumer<Robot> a) {
    return r -> {
      r.keyPress(0x10);
      a.accept(r);
      r.delay(15);
      r.keyRelease(0x10);
    };
  }
  
  public static Consumer<Robot> ctrlmod(Consumer<Robot> a) {
    return r -> {
      r.keyPress(0x11);
      a.accept(r);
      r.delay(15);
      r.keyRelease(0x11);
    };
  }
  
  public static Consumer<Robot> altmod(Consumer<Robot> a) {
    return r -> {
      r.keyPress(0x12);
      a.accept(r);
      r.delay(15);
      r.keyRelease(0x12);
    };
  }
  
}
