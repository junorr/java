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

package us.pserver.tools;

import java.util.Random;
import java.util.function.Function;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/10/2017
 */
public class RandomString {

  public static final char[] VOWELS = {
    'a', 'e', 'i', 'o', 'u', 'y'
  };
  
  public static final char[] CONSONANTS = {
    'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'
  };
  
  
  public static enum StringCase {
    LOWER(s->s), 
    UPPER(String::toUpperCase), 
    FIRST_UPPER(s->s.substring(0, 1).toUpperCase().concat(s.substring(1)));
    
    private final Function<String,String> fncase;
    
    private StringCase(Function<String,String> fncase) {
      this.fncase = fncase;
    }
    
    public String applyCase(String str) {
      return fncase.apply(str);
    }
    
  }
  
  
  private final StringCase scase;
  
  private final int size;
  
  private final Random rdm;
  
  
  public RandomString(int size, StringCase scase) {
    if(size < 1) throw new IllegalArgumentException("Bad String size (< 1)");
    this.size = size;
    this.scase = NotNull.of(scase).getOrFail("Bad null StringCase");
    this.rdm = new Random(System.currentTimeMillis());
  }
  
  
  public RandomString(int size) {
    this(size, StringCase.LOWER);
  }
  
  
  public String generate() {
    StringBuilder str = new StringBuilder();
    boolean isVowel = true;
    for(int i = 0; i < size; i++) {
      if(isVowel) {
        int x = rdm.nextInt(VOWELS.length);
        str.append(VOWELS[x]);
      }
      else {
        int x = rdm.nextInt(CONSONANTS.length);
        str.append(CONSONANTS[x]);
      }
      isVowel = !isVowel;
    }
    return scase.applyCase(str.toString());
  }
  
  
  public static RandomString of(int size) {
    return new RandomString(size);
  }
  
  
  public static RandomString of(int size, StringCase scase) {
    return new RandomString(size, scase);
  }
  
}
