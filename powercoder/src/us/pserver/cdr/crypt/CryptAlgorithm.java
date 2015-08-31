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

import java.io.IOException;
import java.lang.reflect.Field;


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
  AES_CBC("AES/CBC/NoPadding-128", 128),
  
  /**
   * AES/CBC/NoPadding de 256 bits.
   */
  AES_CBC_256("AES/CBC/NoPadding-256", 256),
  
  /**
   * AES/CBC/PKCS5Padding de 128 bits.
   */
  AES_CBC_PKCS5("AES/CBC/PKCS5Padding-128", 128),
  
  /**
   * AES/CBC/PKCS5Padding de 256 bits.
   */
  AES_CBC_256_PKCS5("AES/CBC/PKCS5Padding-256", 256),
  
  /**
   * DES/CBC/NoPadding de 64 bits.
   */
  DES_CBC("DES/CBC/NoPadding-64", 64),
  
  /**
   * DES/CBC/PKCS5Padding de 64 bits.
   */
  DES_CBC_PKCS5("DES/CBC/PKCS5Padding-64", 64),
  
  /**
   * DESede/CBC/NoPadding de 192 bits.
   */
  DESede_CBC("DESede/CBC/NoPadding-192", 192),
  
  /**
   * DESede/CBC/PKCS5Padding de 192 bits.
   */
  DESede_CBC_PKCS5("DESede/CBC/PKCS5Padding-192", 192),
  
  /**
   * AES/ECB/NoPadding de 128 bits.
   */
  AES_ECB("AES/ECB/NoPadding-128", 128),
  
  /**
   * AES/ECB/NoPadding de 256 bits.
   */
  AES_ECB_256("AES/ECB/NoPadding-256", 256),
  
  /**
   * AES/ECB/PKCS5Padding de 128 bits.
   */
  AES_ECB_PKCS5("AES/ECB/PKCS5Padding-128", 128),
  
  /**
   * AES/ECB/PKCS5Padding de 256 bits.
   */
  AES_ECB_256_PKCS5("AES/ECB/PKCS5Padding-256", 256),
  
  /**
   * DES/ECB/NoPadding de 64 bits.
   */
  DES_ECB("DES/ECB/NoPadding-64", 64),
  
  /**
   * DES/ECB/PKCS5Padding de 64 bits.
   */
  DES_ECB_PKCS5("DES/ECB/PKCS5Padding-64", 64),
  
  /**
   * DESede/ECB/NoPadding de 192 bits.
   */
  DESede_ECB("DESede/ECB/NoPadding-192", 192),
  
  /**
   * DESede/ECB/PKCS5Padding de 192 bits.
   */
  DESede_ECB_PKCS5("DESede/ECB/PKCS5Padding-192", 192);
  
  
  private CryptAlgorithm(String transform, int size) {
    this.transform = transform;
    keySize = size;
    if(size == 256 && !isUnlimitedStrengthJCE()) {
      setUnlimitedStrengthJCE();
    }
  }
  
  
  /**
   * Retorna o nome do algorítmo.
   * @return Algorithm <code>String</code> name.
   */
  public String getStringAlgorithm() {
    return transform.substring(0, transform.indexOf("-"));
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
  
  private final String transform;
  
  private final int keySize;
  
  
  public static void setUnlimitedStrengthJCE() throws SecurityException {
    try {
      Field field = Class.forName("javax.crypto.JceSecurity")
          .getDeclaredField("isRestricted");
      field.setAccessible(true);
      field.set(null, java.lang.Boolean.FALSE);
    } catch(Exception e) {
      throw new SecurityException("Fail trying to force Unlimited Strength Java Cryptography Extension", e);
    }
  }
  
  
  public static boolean isUnlimitedStrengthJCE() throws SecurityException {
    try {
      Field field = Class.forName("javax.crypto.JceSecurity")
          .getDeclaredField("isRestricted");
      field.setAccessible(true);
      return !field.getBoolean(null);
    } catch(Exception e) {
      throw new SecurityException("Fail trying to get state of Unlimited Strength Java Cryptography Extension", e);
    }
  }
  
  
  /**
   * Transforma a string fornecida em um objeto 
   * <code>CryptAlgorithm</code>.
   * @param str <code>String</code>.
   * @return <code>CryptAlgorithm</code>.
   */
  public static CryptAlgorithm fromString(String str) {
    if(str == null) return null;
    if(str.equals(CryptAlgorithm.AES_CBC.toString()))
      return CryptAlgorithm.AES_CBC;
    else if(str.equals(CryptAlgorithm.AES_CBC_256.toString()))
      return CryptAlgorithm.AES_CBC_256;
    else if(str.equals(CryptAlgorithm.AES_CBC_PKCS5.toString()))
      return CryptAlgorithm.AES_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.AES_CBC_256_PKCS5.toString()))
      return CryptAlgorithm.AES_CBC_256_PKCS5;
    else if(str.equals(CryptAlgorithm.AES_ECB.toString()))
      return CryptAlgorithm.AES_ECB;
    else if(str.equals(CryptAlgorithm.AES_ECB_256.toString()))
      return CryptAlgorithm.AES_ECB_256;
    else if(str.equals(CryptAlgorithm.AES_ECB_PKCS5.toString()))
      return CryptAlgorithm.AES_ECB_PKCS5;
    else if(str.equals(CryptAlgorithm.AES_ECB_256_PKCS5.toString()))
      return CryptAlgorithm.AES_ECB_256_PKCS5;
    else if(str.equals(CryptAlgorithm.DES_CBC.toString()))
      return CryptAlgorithm.DES_CBC;
    else if(str.equals(CryptAlgorithm.DES_CBC_PKCS5.toString()))
      return CryptAlgorithm.DES_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.DES_ECB.toString()))
      return CryptAlgorithm.DES_ECB;
    else if(str.equals(CryptAlgorithm.DES_ECB_PKCS5.toString()))
      return CryptAlgorithm.DES_ECB_PKCS5;
    else if(str.equals(CryptAlgorithm.DESede_CBC.toString()))
      return CryptAlgorithm.DESede_CBC;
    else if(str.equals(CryptAlgorithm.DESede_CBC_PKCS5.toString()))
      return CryptAlgorithm.DESede_CBC_PKCS5;
    else if(str.equals(CryptAlgorithm.DESede_ECB.toString()))
      return CryptAlgorithm.DESede_ECB;
    else if(str.equals(CryptAlgorithm.DESede_ECB_PKCS5.toString()))
      return CryptAlgorithm.DESede_ECB_PKCS5;
    else 
      return null;
  }
  
  
  public static void main(String[] args) {
    System.out.println(CryptAlgorithm.AES_CBC_256_PKCS5.toString());
    System.out.println(CryptAlgorithm.AES_CBC_256_PKCS5.getStringAlgorithm());
  }
  
}
