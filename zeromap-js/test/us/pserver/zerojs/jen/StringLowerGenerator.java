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

package us.pserver.zerojs.jen;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/04/2016
 */
public class StringLowerGenerator implements Generator<String> {

  private final IntegerGenerator cgen;
  
  private final IntegerGenerator lgen;
  
  private final int min;
  
  private final int max;
  
  
  public StringLowerGenerator(int minLen, int maxLen) {
    if(minLen < 1 || minLen > maxLen) {
      throw new IllegalArgumentException(
          "Invalid min length (0 < min < max): "+ minLen
      );
    }
    if(maxLen < 1 || maxLen < minLen) {
      throw new IllegalArgumentException(
          "Invalid min length (0 < max > min): "+ maxLen
      );
    }
    this.min = minLen;
    this.max = maxLen;
    cgen = new IntegerGenerator(25);
    lgen = new IntegerGenerator(max - min+1);
  }
  
  
  @Override
  public String generate() {
    int len = lgen.generate() + min;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < len; i++) {
      sb.append((char)((int)(cgen.generate()+1) + 97));
    }
    return sb.toString();
  }
  
}
