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

package us.pserver.cdr.digest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


/**
 * Classe utilitária para geração de hash's.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class Digester {
  
  
  /**
   * Gera um hash a partir dos bytes, com o algorítmo informado.
   * @param bs byte array a partir do qual será gerado o hash.
   * @param algo Algorítmo de criptografia.
   * @return hash.
   */
  public static byte[] digest(byte[] bs, DigestAlgorithm algo) {
    Objects.requireNonNull(bs);
    Objects.requireNonNull(algo);
    try {
      return MessageDigest.getInstance(algo.getAlgorithm()).digest(bs);
    } catch(NoSuchAlgorithmException e) {
      return null;
    }
  }
  
  
  /**
   * Gera um hash a partir da <code>String</code>, 
   * com o algorítmo informado.
   * @param str <code>String</code> a partir da qual 
   * será gerado o hash.
   * @param algo Algorítmo de criptografia.
   * @return hash.
   */
  public static byte[] digest(String str, DigestAlgorithm algo) {
    Objects.requireNonNull(str);
    Objects.requireNonNull(algo);
    return digest(toBytes(str), algo);
  }
  
  
  /**
   * Gera um hash a partir dos bytes informados, 
   * com o algorítmo SHA-1.
   * @param bs byte array a partir do qual será gerado o hash.
   * @return hash.
   */
  public static byte[] toSHA1(byte[] bs) {
    Objects.requireNonNull(bs);
    return digest(bs, DigestAlgorithm.SHA_1);
  }
  
  
  /**
   * Gera um hash a partir da <code>String</code> informada, 
   * com o algorítmo SHA-1.
   * @param str <code>String</code> a partir da qual 
   * será gerado o hash.
   * @return hash.
   */
  public static byte[] toSHA1(String str) {
    Objects.requireNonNull(str);
    return digest(str, DigestAlgorithm.SHA_1);
  }
  
  
  /**
   * Gera um hash a partir dos bytes informados, 
   * com o algorítmo SHA-256.
   * @param bs byte array a partir do qual será gerado o hash.
   * @return hash.
   */
  public static byte[] toSHA256(byte[] bs) {
    Objects.requireNonNull(bs);
    return digest(bs, DigestAlgorithm.SHA_256);
  }
  
  
  /**
   * Gera um hash a partir da <code>String</code> informada, 
   * com o algorítmo SHA-256.
   * @param str <code>String</code> a partir da qual 
   * será gerado o hash.
   * @return hash.
   */
  public static byte[] toSHA256(String str) {
    Objects.requireNonNull(str);
    return digest(str, DigestAlgorithm.SHA_256);
  }
  
  
  /**
   * Gera um hash a partir dos bytes informados, 
   * com o algorítmo MD5.
   * @param bs byte array a partir do qual será gerado o hash.
   * @return hash.
   */
  public static byte[] toMD5(byte[] bs) {
    Objects.requireNonNull(bs);
    return digest(bs, DigestAlgorithm.MD5);
  }
  
  
  /**
   * Gera um hash a partir da <code>String</code> informada, 
   * com o algorítmo MD5.
   * @param str <code>String</code> a partir da qual 
   * será gerado o hash.
   * @return hash.
   */
  public static byte[] toMD5(String str) {
    Objects.requireNonNull(str);
    return digest(str, DigestAlgorithm.MD5);
  }
  
  
  /**
   * Converte a <code>String</code> informada em um byte array.
   * @param str <code>String</code> a ser convertida.
   * @return byte array gerado a partir dos caracteres da 
   * <code>String</code>.
   */
  public static byte[] toBytes(String str) {
    Objects.requireNonNull(str);
    if(str.isEmpty()) return new byte[0];
    try {
      return str.getBytes("UTF-8");
    } catch(UnsupportedEncodingException e) {
      return new byte[0];
    }
  }
  
  
  public static void main(String[] args) {
    System.out.println("* codeSHA1: "+ toSHA1("s3cReT"));
  }
  
}
