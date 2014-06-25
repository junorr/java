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


/**
 * Algorítmo de criação de hash's SHA-1 e SHA-256.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public enum DigestAlgorithm {
  
  /**
   * SHA-1
   */
  SHA_1("SHA-1"),
  
  /**
   * SHA-256
   */
  SHA_256("SHA-256"),
  
  /**
   * MD5
   */
  MD5;
  
  
  private String algo;
  
  
  private DigestAlgorithm() {
    algo = this.name();
  }
  
  
  private DigestAlgorithm(String algo) {
    this.algo = algo;
  }
  
  
  /**
   * Retorna o nome <code>String</code> do algorítmo.
   * @return Algorithm <code>String</code> name.
   */
  public String getAlgorithm() {
    return algo;
  }
  
  
  @Override
  public String toString() {
    return algo;
  }
  
}
