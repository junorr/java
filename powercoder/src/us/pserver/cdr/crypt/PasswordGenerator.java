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

package us.pserver.cdr.crypt;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import us.pserver.cdr.StringByteConverter;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/07/2014
 */
public class PasswordGenerator {

  public static char[] NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
  
  public static char[] ALPHA_LOWER = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
  
  public static char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  
  public static char[] ALPHA_UPPER = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
  
  public static char[] SPECIAL = {'!', '@', '#', '$', '%', '&', '*', '(', ')', '-', '_', '+', '=', '\\', '|', '<', '>', ',', '.', ':', '?', '?', '{', '[', '}', ']'};
  
  
  private boolean num, spec, upper, lower, hex;
  
  private Random rand;
  
  private List<Character> list;
  
  
  private PasswordGenerator() {
    num = upper = lower = spec = true;
    hex = false;
    list = new LinkedList<>();
    rand = new Random();
  }
  
  
  public static PasswordGenerator instance() {
    return new PasswordGenerator();
  }
  
  
  public PasswordGenerator disableAll() {
    num = upper = lower = spec = hex = false;
    list.clear();
    return this;
  }
  
  
  public PasswordGenerator resetRandom() {
    list.clear();
    num = upper = lower = spec = true;
    hex = false;
    rand = new Random();
    return this;
  }
  
  
  public PasswordGenerator useNumbers(boolean use) {
    num = use;
    return this;
  }
  
  
  public PasswordGenerator useLowerAlpha(boolean use) {
    lower = use;
    return this;
  }
  
  
  public PasswordGenerator useUpperAlpha(boolean use) {
    upper = use;
    return this;
  }
  
  
  public PasswordGenerator useSpecials(boolean use) {
    spec = use;
    return this;
  }
  
  
  public PasswordGenerator useHexSymbolsOnly(boolean use) {
    hex = true;
    num = false;
    lower = false;
    upper = false;
    spec = false;
    return this;
  }
  
  
  public boolean useHexSymbolsOnly() {
    return hex;
  }
  
  
  public boolean useNumbers() {
    return num;
  }
  
  
  public boolean useLowerAlpha() {
    return lower;
  }
  
  
  public boolean useUpperAlpha() {
    return upper;
  }
  
  
  public boolean useSpecials() {
    return spec;
  }
  
  
  public boolean isEnabled() {
    return hex || lower || upper || num || spec;
  }
  
  
  public char randomNumber() {
    return NUMBERS[rand.nextInt(NUMBERS.length)];
  }
  
  
  public char randomUpperAlpha() {
    return ALPHA_UPPER[rand.nextInt(ALPHA_UPPER.length)];
  }
  
  
  public char randomLowerAlpha() {
    return ALPHA_LOWER[rand.nextInt(ALPHA_LOWER.length)];
  }
  
  
  public char randomHex() {
    return HEX[rand.nextInt(HEX.length)];
  }
  
  
  public char randomSpecial() {
    return SPECIAL[rand.nextInt(SPECIAL.length)];
  }
  
  
  public char nextRandom() {
    if(!isEnabled())
      throw new IllegalStateException(
          "No mode enabled [hex="+ hex
          + ", lower="+ lower
          + ", upper="+ upper
          + ", num="+ num+ "spec="+ spec+ "]");
    
    if(useHexSymbolsOnly())
      return randomHex();
    
    if(list.isEmpty()) {
      if(useNumbers()) list.add(randomNumber());
      if(useUpperAlpha()) list.add(randomUpperAlpha());
      if(useLowerAlpha()) list.add(randomLowerAlpha());
      if(useSpecials()) list.add(randomSpecial());
    }
    return list.remove(rand.nextInt(list.size()));
  }
  
  
  public String genAsString(int size) {
    return new String(genAsChars(size));
  }
  
  
  public char[] genAsChars(int size) {
    Sane.of(size).check(Checkup.isBetween(1, Integer.MAX_VALUE));
    char[] cs = new char[size];
    for(int i = 0; i < size; i++) {
      cs[i] = nextRandom();
    }
    return cs;
  }
  
  
  public byte[] genAsBytes(int size) {
    StringByteConverter cv = new StringByteConverter();
    return cv.convert(genAsString(size));
  }
  
  
  public static void main(String[] args) {
    System.out.println("* 8 numbers:");
    System.out.println("  "+ instance().disableAll()
        .useNumbers(true).genAsString(8));
    System.out.println("  "+ instance().disableAll()
        .useNumbers(true).genAsString(8));
    System.out.println("  "+ instance().disableAll()
        .useNumbers(true).genAsString(8));
    
    System.out.println("* 10 hex:");
    System.out.println("  "+ instance().disableAll()
        .useHexSymbolsOnly(true).genAsString(10));
    
    System.out.println("* 10 all:");
    System.out.println("  "+ instance().genAsString(10));
    
    System.out.println("* 8 lower alpha:");
    System.out.println("  "+ instance().disableAll()
        .useLowerAlpha(true).genAsString(8));
    
    System.out.println("* 8 upper alpha:");
    System.out.println("  "+ instance().disableAll()
        .useUpperAlpha(true).genAsString(8));
    
    System.out.println("* 8 specials:");
    System.out.println("  "+ instance().disableAll()
        .useSpecials(true).genAsString(8));
  }
  
}
