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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/08/2017
 */
public class Hash {
  
  private static final String ALGORITHM_MD5 = "MD5";
  
  private static final String ALGORITHM_SHA1 = "SHA-1";
  
  private static final String ALGORITHM_SHA256 = "SHA-256";
  
  private static final String ALGORITHM_SHA512 = "SHA-512";
  
  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
  
  
  private final MessageDigest digest;
  
  
  public Hash(MessageDigest md) {
    digest = NotNull.of(md).getOrFail("Bad null MessageDigest");
  }
  
  
  public static Hash md5() {
    return new Hash(getMessageDigest(ALGORITHM_MD5));
  }
  
  
  public static Hash sha1() {
    return new Hash(getMessageDigest(ALGORITHM_SHA1));
  }
  
  
  public static Hash sha256() {
    return new Hash(getMessageDigest(ALGORITHM_SHA256));
  }
  
  
  public static Hash sha512() {
    return new Hash(getMessageDigest(ALGORITHM_SHA512));
  }
  
  
  public String of(String str) {
    return bytesToHex(digest.digest(UTF8String.from(
        NotNull.of(str).getOrFail("Bad null String")
    ).getBytes()));
  }
  
  
  public String of(byte[] bs) {
    return bytesToHex(digest.digest(
        NotNull.of(bs).getOrFail("Bad null byte array"))
    );
  }
  
  
  private static MessageDigest getMessageDigest(String algorithm) {
    try {
      return MessageDigest.getInstance(algorithm);
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException(ex.toString(), ex);
    }
  }
  
  
  private static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }
  
  
  public static void main(String[] args) {
    String s1 = "32132155";
    String s2 = "juno";
    System.out.println("* s1....: "+ s1);
    System.out.println("* s2....: "+ s2);
    System.out.println("* md5...: "+ Hash.md5().of(s1));
    System.out.println("* md5...: "+ Hash.md5().of(s2));
    System.out.println("* sha1..: "+ Hash.sha1().of(s1));
    System.out.println("* sha1..: "+ Hash.sha1().of(s2));
    System.out.println("* sha256: "+ Hash.sha256().of(s1));
    System.out.println("* sha256: "+ Hash.sha256().of(s2));
    System.out.println("* sha512: "+ Hash.sha512().of(s1));
    System.out.println("* sha512: "+ Hash.sha512().of(s2));
  }
  
}
