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
 * Classe que representa um algorítmo de criptografia.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 25/06/2014
 */
public enum CryptAlgorithm {
  
  /**
   * AES/CBC/NoPadding de 128 bits.
   */
  AES_CBC("AES/CBC/NoPadding", 128),
  
  /**
   * AES/CBC/PKCS5Padding de 128 bits.
   */
  AES_CBC_PKCS5("AES/CBC/PKCS5Padding", 128),
  
  /**
   * DES/CBC/NoPadding de 64 bits.
   */
  DES_CBC("DES/CBC/NoPadding", 64),
  
  /**
   * DES/CBC/PKCS5Padding de 64 bits.
   */
  DES_CBC_PKCS5("DES/CBC/PKCS5Padding", 64),
  
  /**
   * DESede/CBC/NoPadding de 192 bits.
   */
  DESede_CBC("DESede/CBC/NoPadding", 192),
  
  /**
   * DESede/CBC/PKCS5Padding de 192 bits.
   */
  DESede_CBC_PKCS5("DESede/CBC/PKCS5Padding", 192),
  
  /**
   * AES/ECB/NoPadding de 128 bits.
   */
  AES_ECB("AES/ECB/NoPadding", 128),
  
  /**
   * AES/ECB/PKCS5Padding de 128 bits.
   */
  AES_ECB_PKCS5("AES/ECB/PKCS5Padding", 128),
  
  /**
   * DES/ECB/NoPadding de 64 bits.
   */
  DES_ECB("DES/ECB/NoPadding", 64),
  
  /**
   * DES/ECB/PKCS5Padding de 64 bits.
   */
  DES_ECB_PKCS5("DES/ECB/PKCS5Padding", 64),
  
  /**
   * DESede/ECB/NoPadding de 192 bits.
   */
  DESede_ECB("DESede/ECB/NoPadding", 192),
  
  /**
   * DESede/ECB/PKCS5Padding de 192 bits.
   */
  DESede_ECB_PKCS5("DESede/ECB/PKCS5Padding", 192);
  
  
  private CryptAlgorithm(String transform, int size) {
    this.transform = transform;
    keySize = size;
  }
  
  
  /**
   * Retorna o nome do algorítmo.
   * @return Algorithm <code>String</code> name.
   */
  public String getAlgorithm() {
    return transform;
  }
  
  
  /**
   * Retorna o tamanho do algorítmo em bits.
   * @return Algorithm bits size.
   */
  public int getBitsSize() {
    return keySize;
  }
  
  
  /**
   * Retorna o tamanho do algorítmo em bytes.
   * @return Algorithm bytes size.
   */
  public int getBytesSize() {
    return keySize / 8;
  }
  
  
  @Override
  public String toString() {
    return transform;
  }
  
  private String transform;
  
  private int keySize;
  
  
  /**
   * Transforma a string fornecida em um objeto 
   * <code>CryptAlgorithm</code>.
   * @param str <code>String</code>.
   * @return <code>CryptAlgorithm</code>.
   */
  public static CryptAlgorithm fromString(String str) {
    if(str == null) return null;
    if(str.equals(CryptAlgorithm.AES_CBC.getAlgorithm()))
      return CryptAlgorithm.AES_CBC;
    else if(str.equals(CryptAlgorithm.AES_CBC_PKCS5.getAlgorithm()))
      return CryptAlgorithm.AES_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.AES_ECB.getAlgorithm()))
      return CryptAlgorithm.AES_ECB;
    else if(str.equals(CryptAlgorithm.AES_ECB_PKCS5.getAlgorithm()))
      return CryptAlgorithm.AES_ECB_PKCS5;
    else if(str.equals(CryptAlgorithm.DES_CBC.getAlgorithm()))
      return CryptAlgorithm.DES_CBC;
    else if(str.equals(CryptAlgorithm.DES_CBC_PKCS5.getAlgorithm()))
      return CryptAlgorithm.DES_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.DES_ECB.getAlgorithm()))
      return CryptAlgorithm.DES_ECB;
    else if(str.equals(CryptAlgorithm.DES_ECB_PKCS5.getAlgorithm()))
      return CryptAlgorithm.DES_ECB_PKCS5;
    else if(str.equals(CryptAlgorithm.DESede_CBC.getAlgorithm()))
      return CryptAlgorithm.DESede_CBC;
    else if(str.equals(CryptAlgorithm.DESede_CBC_PKCS5.getAlgorithm()))
      return CryptAlgorithm.DESede_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.DESede_ECB.getAlgorithm()))
      return CryptAlgorithm.DESede_ECB;
    else if(str.equals(CryptAlgorithm.DESede_ECB_PKCS5.getAlgorithm()))
      return CryptAlgorithm.DESede_ECB_PKCS5;
    else 
      return null;
  }
}
