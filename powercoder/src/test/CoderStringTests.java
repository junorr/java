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

package test;

import us.pserver.cdr.Coder;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.hex.HexStringCoder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 20/06/2014
 */
public class CoderStringTests {
  
  
  public static char[] CHARS = new char[62];
  
  
  public static void fillChars() {
    int idx = 0;
    for(char c = 'a'; c <= 'z'; c++) {
      CHARS[idx++] = c;
    }
    for(char c = 'A'; c <= 'Z'; c++) {
      CHARS[idx++] = c;
    }
    for(char c = '0'; c <= '9'; c++) {
      CHARS[idx++] = c;
    }
  }
  
  
  public static void fillRandom(char[] cs) {
    for(int i = 0; i < cs.length; i++) {
      int rdm = (int) (Math.random() * CHARS.length);
      cs[i] = CHARS[rdm];
    }
  }

  
  public static void main(String[] args) {
    //Coder<String> cdr = new Base64StringCoder();
    Coder<String> cdr = new HexStringCoder();
    fillChars();
    
    char[] ten = new char[10];
    for(int i = 0; i < 10; i++) {
      fillRandom(ten);
      String str = new String(ten);
      System.out.println("* str = '"+ str+ "'");
      System.out.println("* DEC length = "+ str.length());
      str = cdr.encode(str);
      System.out.println("* str = '"+ str+ "'");
      System.out.println("* ENC length = "+ str.length());
      System.out.println("---------------------");
    }
  }
  
}
