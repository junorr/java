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
  
  A('A', new KeyComboAction(0x10, 0x41)),
  LOWER_A('a', new KeyTypeAction(0x41)),
  AGRAVE_UPPER('À', new KeyComboAction(0x10, 0x81, 0x41)),
  AGRAVE_LOWER('à', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x41))),
  AACUTE_UPPER('Á', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x41))),
  AACUTE_LOWER('á', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x41))),
  ACIRC_UPPER('Â', new KeyComboAction(0x10, 0x83, 0x41)),
  ACIRC_LOWER('â', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x41))),
  ATILDE_UPPER('Ã', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x41))),
  ATILDE_LOWER('ã', new KeyComboAction(0x83, 0x41)),
  AUMBRA_UPPER('Ä', new KeyComboAction(0x10, 0x36, 0x41)),
  AUMBRA_LOWER('ä', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x41))),

  B('B', new KeyComboAction(0x10, 0x42)),
  LOWER_B('b', new KeyTypeAction(0x42)),
  C('C', new KeyComboAction(0x10, 0x43)),
  LOWER_C('c', new KeyTypeAction(0x43)),
  D('D', new KeyComboAction(0x10, 0x44)),
  LOWER_D('d', new KeyTypeAction(0x44)),
  
  E('E', new KeyComboAction(0x10, 0x45)),
  LOWER_E('e', new KeyTypeAction(0x45)),
  EGRAVE_UPPER('È', new KeyComboAction(0x10, 0x81, 0x45)),
  EGRAVE_LOWER('è', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x45))),
  EACUTE_UPPER('É', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x45))),
  EACUTE_LOWER('é', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x45))),
  ECIRC_UPPER('Ê', new KeyComboAction(0x10, 0x83, 0x45)),
  ECIRC_LOWER('ê', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x45))),
  ETILDE_UPPER('Ẽ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x45))),
  ETILDE_LOWER('ẽ', new KeyComboAction(0x83, 0x45)),
  EUMBRA_UPPER('Ë', new KeyComboAction(0x10, 0x36, 0x45)),
  EUMBRA_LOWER('ë', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x45))),

  F('F', new KeyComboAction(0x10, 0x46)),
  LOWER_F('f', new KeyTypeAction(0x46)),
  G('G', new KeyComboAction(0x10, 0x47)),
  LOWER_G('g', new KeyTypeAction(0x47)),
  H('H', new KeyComboAction(0x10, 0x48)),
  LOWER_H('h', new KeyTypeAction(0x48)),
  
  I('I', new KeyComboAction(0x10, 0x49)),
  LOWER_I('i', new KeyTypeAction(0x49)),
  IGRAVE_UPPER('Ì', new KeyComboAction(0x10, 0x81, 0x49)),
  IGRAVE_LOWER('ì', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x49))),
  IACUTE_UPPER('Í', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x49))),
  IACUTE_LOWER('í', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x49))),
  ICIRC_UPPER('Î', new KeyComboAction(0x10, 0x83, 0x49)),
  ICIRC_LOWER('î', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x49))),
  ITILDE_UPPER('Ĩ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x49))),
  ITILDE_LOWER('ĩ', new KeyComboAction(0x83, 0x49)),
  IUMBRA_UPPER('Ï', new KeyComboAction(0x10, 0x36, 0x49)),
  IUMBRA_LOWER('ï', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x49))),

  J('J', new KeyComboAction(0x10, 0x4A)),
  LOWER_J('j', new KeyTypeAction(0x4A)),
  K('K', new KeyComboAction(0x10, 0x4B)),
  LOWER_K('k', new KeyTypeAction(0x4B)),
  L('L', new KeyComboAction(0x10, 0x4C)),
  LOWER_L('l', new KeyTypeAction(0x4C)),
  M('M', new KeyComboAction(0x10, 0x4D)),
  LOWER_M('m', new KeyTypeAction(0x4D)),
  N('N', new KeyComboAction(0x10, 0x4E)),
  LOWER_N('n', new KeyTypeAction(0x4E)),
  
  O('O', new KeyComboAction(0x10, 0x4F)),
  LOWER_O('o', new KeyTypeAction(0x4F)),
  OGRAVE_UPPER('Ò', new KeyComboAction(0x10, 0x81, 0x4F)),
  OGRAVE_LOWER('ò', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x4F))),
  OACUTE_UPPER('Ó', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x4F))),
  OACUTE_LOWER('ó', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x4F))),
  OCIRC_UPPER('Ô', new KeyComboAction(0x10, 0x83, 0x4F)),
  OCIRC_LOWER('ô', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x4F))),
  OTILDE_UPPER('Õ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x4F))),
  OTILDE_LOWER('õ', new KeyComboAction(0x83, 0x4F)),
  OUMBRA_UPPER('Ö', new KeyComboAction(0x10, 0x36, 0x4F)),
  OUMBRA_LOWER('ö', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x4F))),

  P('P', new KeyComboAction(0x10, 0x50)),
  LOWER_P('p', new KeyTypeAction(0x50)),
  Q('Q', new KeyComboAction(0x10, 0x51)),
  LOWER_Q('q', new KeyTypeAction(0x51)),
  R('R', new KeyComboAction(0x10, 0x52)),
  LOWER_R('r', new KeyTypeAction(0x52)),
  S('S', new KeyComboAction(0x10, 0x53)),
  LOWER_S('s', new KeyTypeAction(0x53)),
  T('T', new KeyComboAction(0x10, 0x54)),
  LOWER_T('t', new KeyTypeAction(0x54)),
  
  U('U', new KeyComboAction(0x10, 0x55)),
  LOWER_U('u', new KeyTypeAction(0x55)),
  UGRAVE_UPPER('Ù', new KeyComboAction(0x10, 0x81, 0x55)),
  UGRAVE_LOWER('ù', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x55))),
  UACUTE_UPPER('Ú', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x55))),
  UACUTE_LOWER('ú', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x55))),
  UCIRC_UPPER('Û', new KeyComboAction(0x10, 0x83, 0x55)),
  UCIRC_LOWER('û', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x55))),
  UTILDE_UPPER('Ũ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x55))),
  UTILDE_LOWER('ũ', new KeyComboAction(0x83, 0x55)),
  UUMBRA_UPPER('Ü', new KeyComboAction(0x10, 0x36, 0x55)),
  UUMBRA_LOWER('ü', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x55))),

  V('V', new KeyComboAction(0x10, 0x56)),
  LOWER_V('v', new KeyTypeAction(0x56)),
  W('W', new KeyComboAction(0x10, 0x57)),
  LOWER_W('w', new KeyTypeAction(0x57)),
  X('X', new KeyComboAction(0x10, 0x58)),
  LOWER_X('x', new KeyTypeAction(0x58)),
  
  Y('Y', new KeyComboAction(0x10, 0x59)),
  LOWER_Y('y', new KeyTypeAction(0x59)),
  YGRAVE_UPPER('Ỳ', new KeyComboAction(0x10, 0x81, 0x59)),
  YGRAVE_LOWER('ỳ', new KeyComboAction(0x10, 0x81).andThen(new KeyTypeAction(0x59))),
  YACUTE_UPPER('Ý', new KeyTypeAction(0x81).andThen(new KeyComboAction(0x10, 0x59))),
  YACUTE_LOWER('ý', new KeyTypeAction(0x81).andThen(new KeyTypeAction(0x59))),
  YCIRC_UPPER('Ŷ', new KeyComboAction(0x10, 0x83, 0x59)),
  YCIRC_LOWER('ŷ', new KeyComboAction(0x10, 0x83).andThen(new KeyTypeAction(0x59))),
  YTILDE_UPPER('Ỹ', new KeyTypeAction(0x83).andThen(new KeyComboAction(0x10, 0x59))),
  YTILDE_LOWER('ỹ', new KeyComboAction(0x83, 0x59)),
  YUMBRA_UPPER('Ÿ', new KeyComboAction(0x10, 0x36, 0x59)),
  YUMBRA_LOWER('ÿ', new KeyComboAction(0x10, 0x36).andThen(new KeyTypeAction(0x59))),

  Z('Z', new KeyComboAction(0x10, 0x5A)),
  LOWER_Z('z', new KeyTypeAction(0x5A)),
  
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
  EUR('€', new KeyComboAction(0xFF7E, 0x45)),
  POUND('£', new KeyComboAction(0xFF7E, 0x34)),
  PERCENT('%', new KeyComboAction(0x10, 0x35)),
  AMPERSAND('&', new KeyComboAction(0x10, 0x37)),
  ASTERISK('*', new KeyComboAction(0x10, 0x38)),
  PARENTHESISL('(', new KeyTypeAction(0x0207)),
  PARENTHESISR(')', new KeyTypeAction(0x020A)),
  MINUS('-', new KeyTypeAction(0x2D)),
  UNDERSCORE('_', new KeyComboAction(0x10, 0x2D)),
  EQUALS('=', new KeyTypeAction(0x3D)),
  PLUS('+', new KeyComboAction(0x10, 0x3D)),
  
  CEDIL_UPPER('Ç', new KeyTypeAction(0x80).andThen(new KeyComboAction(0x10, 0x43))),
  CEDIL_LOWER('ç', new KeyComboAction(0x80, 0x43)),
  COPYRIGHT('©', new KeyComboAction(0xFF7E, 0x43)),
  REGISTERED('®', new KeyComboAction(0xFF7E, 0x52)),
  LEFTDBL('«', new KeyComboAction(0xFF7E, 0x5A)),
  RIGHTDBL('»', new KeyComboAction(0xFF7E, 0x58)),
  ARROW_UP('↑', new KeyComboAction(0xFF7E, 0x10, 0x55)),
  ARROW_LEFT('←', new KeyComboAction(0xFF7E, 0x59)),
  ARROW_RIGHT('→', new KeyComboAction(0xFF7E, 0x49)),
  ARROW_DOWN('↓', new KeyComboAction(0xFF7E, 0x55)),
  PARAGRAPH('─', new KeyComboAction(0xFF7E, 0x2C)),
  MULTIPLY('×', new KeyComboAction(0xFF7E, 0x10, 0x2C)),
  DIVIDE('÷', new KeyComboAction(0xFF7E, 0x10, 0x2E)),
  FRACTION_1_2('½', new KeyComboAction(0xFF7E, 0x10, 0x32)),
  FRACTION_3_4('¾', new KeyComboAction(0xFF7E, 0x10, 0x33)),
  FRACTION_1_4('¼', new KeyComboAction(0xFF7E, 0x10, 0x34)),
  FRACTION_3_8('⅜', new KeyComboAction(0xFF7E, 0x10, 0x35)),
  FRACTION_7_8('⅞', new KeyComboAction(0xFF7E, 0x10, 0x37)),
  TM('™', new KeyComboAction(0xFF7E, 0x10, 0x38)),
  MORE_OR_LESS('±', new KeyComboAction(0xFF7E, 0x10, 0x39)),
  BRACKETL('[', new KeyTypeAction(0xA1)),
  BRACKETL_CURLY('{', new KeyComboAction(0x10, 0xA1)),
  SUPER_A('ª', new KeyComboAction(0xFF7E, 0xA1)),
  BRACKETR(']', new KeyTypeAction(0xA2)),
  BRACKETR_CURLY('}', new KeyComboAction(0x10, 0xA2)),
  SUPER_O('º', new KeyComboAction(0xFF7E, 0xA2)),
  SUPER_DOT('·', new KeyComboAction(0xFF7E, 0x2E)),
  GRAVE('`', new KeyTypeAction(0x80)),
  TILDE('~', new KeyTypeAction(0x83)),
  CIRCUMFLEX('^', new KeyTypeAction(0x82)),
  SLASH('/', new KeyTypeAction(0x2F)),
  QUESTION('?', new KeyComboAction(0x10, 0x2F)),
  DEGREES('°', new KeyComboAction(0xFF7E, 0x2F)),
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
  RIGHT((char)-10, new KeyTypeAction(0x27)),
  
  NUM_LOCK((char)-11, new KeyTypeAction(0x90)),
  SCROLL_LOCK((char)-12, new KeyTypeAction(0x91)),
  CAPS_LOCK((char)-13, new KeyTypeAction(0x14)),
  PRINT_SCREEN((char)-14, new KeyTypeAction(0x9A)),
  SHIFT((char)-15, new KeyTypeAction(0x10)),
  CONTROL((char)-16, new KeyTypeAction(0x11)),
  ALT((char)-17, new KeyTypeAction(0x12)),
  ALTGR((char)-18, new KeyTypeAction(0xFF7E)),
  META((char)-19, new KeyTypeAction(0x9D)),
  ESCAPE((char)-20, new KeyTypeAction(0x1B)),
  WINDOWS((char)-21, new KeyTypeAction(0x020C)),
  CONTEXT_MENU((char)-22, new KeyTypeAction(0x020D)),
  
  F1((char)-23, new KeyTypeAction(0x70)),
  F2((char)-24, new KeyTypeAction(0x71)),
  F3((char)-25, new KeyTypeAction(0x72)),
  F4((char)-26, new KeyTypeAction(0x73)),
  F5((char)-27, new KeyTypeAction(0x74)),
  F6((char)-28, new KeyTypeAction(0x75)),
  F7((char)-29, new KeyTypeAction(0x76)),
  F8((char)-30, new KeyTypeAction(0x77)),
  F9((char)-31, new KeyTypeAction(0x78)),
  F10((char)-32, new KeyTypeAction(0x79)),
  F11((char)-33, new KeyTypeAction(0x7A)),
  F12((char)-34, new KeyTypeAction(0x7B)),
  
  CUT((char)-35, new KeyComboAction(0x11, 0x58)),
  COPY((char)-36, new KeyComboAction(0x11, 0x43)),
  PASTE((char)-37, new KeyComboAction(0x11, 0x56)),
  UNDO((char)-38, new KeyComboAction(0x11, 0x5A)),
  REDO((char)-39, new KeyComboAction(0x11, 0x10, 0x5A))
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
    return String.format("CharAction(%s)", ch);
  }
  
  
  public static Optional<KeyboardAction> getActionFor(char c) {
    return Arrays.asList(KeyboardAction.values()).stream()
        .filter(a -> a.getChar() == c)
        .findAny();
  }//¬¹²³£¢¬\§ª´~º°̣·─µn”“©»«ºæßðđŋħ̉ĸłø→↓←ŧ®€?/
  
}
