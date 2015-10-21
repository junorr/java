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

package us.pserver.cdr.crypt;

import javax.crypto.spec.SecretKeySpec;
import us.pserver.valid.Valid;


/**
 * Fábrica de especificação de chave <code>SecretKeySpec</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class KeySpecFactory {
  
  
  /**
   * Cria uma <code>SecretKeySpec</code> a partir do byte
   * array e algorítmo informados.
   * @param bs byte array
   * @param algo Algorítmo de criptografia.
   * @return <code>SecretKeySpec</code>
   */
  public static SecretKeySpec createKey(byte[] bs, CryptAlgorithm algo) {
    Valid.off(bs).forEmpty().fail();
    Valid.off(algo).forNull().fail(CryptAlgorithm.class);
    return new SecretKeySpec(bs, getKeyAlgorithm(algo));
  }
  
  
  /**
   * Gera uma chave aleatória com o algorítmo informado.
   * @param algo Algorítmo de criptografia.
   * @return <code>SecretKeySpec</code>
   */
  public SecretKeySpec genetareKey(CryptAlgorithm algo) {
    Valid.off(algo).forNull().fail(CryptAlgorithm.class);
    return new SecretKeySpec(CryptUtils.randomBytes(
        algo.getBytesSize()), getKeyAlgorithm(algo));
  }
  
  
  /**
   * Retorna o nome do algorítmo de criptografia informado.
   * @param algo Algorítmo de criptografia.
   * @return nome do algorítmo.
   */
  public static String getKeyAlgorithm(CryptAlgorithm algo) {
    Valid.off(algo).forNull().fail(CryptAlgorithm.class);
    int idx = algo.toString().indexOf("/");
    if(idx < 0) return null;
    return algo.toString().substring(0, idx);
  }
  
  
  public static void main(String[] args) {
    byte[] secret = "s3cReT-001".getBytes();
    SecretKeySpec key = createKey(secret, CryptAlgorithm.DESede_ECB_PKCS5);
    System.out.println("* key = "+ key);
  }
  
}
