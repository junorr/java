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
 *
*/

package net.powercoder.cdr.hex;

import java.util.Objects;
import net.powercoder.cdr.Coder;
import net.powercoder.cdr.StringByteConverter;
import net.powercoder.cdr.digest.DigestAlgorithm;
import net.powercoder.cdr.digest.Digester;


/**
 * Codificador/Decodificador de <code>String's</code> no 
 * formato hexadecimal.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class HexStringCoder implements Coder<String> {

  private StringByteConverter scv;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HexStringCoder() {
    scv = new StringByteConverter();
  }
  
  
  @Override
  public String apply(String t, boolean encode) {
    Objects.requireNonNull(t);
    return (encode ? encode(t): decode(t));
  }
  
  
  @Override
  public String encode(String t) {
   Objects.requireNonNull(t);
    return HexCoder.toHexString(scv.convert(t));
  }


  @Override
  public String decode(String t) {
    Objects.requireNonNull(t);
    return scv.reverse(HexCoder.fromHexString(t));
  }
  
  
  public static void main(String[] args) {
    String s = "z3VQXufZgpbg$=UWc7d$wUX#%QnTFoDF";
    StringByteConverter conv = new StringByteConverter();
    byte[] bs = conv.convert(s);
    bs = Digester.digest(s, DigestAlgorithm.SHA_256);
    System.out.println(HexCoder.encode(bs));
  }
  
}
