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

package us.pserver.cdr.crypt;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import us.pserver.tools.Valid;


/**
 * Classe utilitária para criação de 
 * cifradores de criptografia.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/06/2014
 */
public class CryptUtils {

  /**
   * Cria um cifrador para criptografia a partir 
   * da chave informada.
   * @param key Chave <code>CryptKey</code>.
   * @return <code>Cipher</code>.
   */
  public static Cipher createEncryptCipher(CryptKey key) {
    Valid.off(key).forNull().fail(CryptKey.class);
    try {
      Cipher encoder = Cipher.getInstance(key.getAlgorithm().getStringAlgorithm());
      
      if(key.getAlgorithm() == CryptAlgorithm.AES_ECB
          || key.getAlgorithm() == CryptAlgorithm.AES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB_PKCS5) {
        encoder.init(Cipher.ENCRYPT_MODE, key.getKeySpec());
      }
      else {
        encoder.init(Cipher.ENCRYPT_MODE, key.getKeySpec(), key.getIV().getIvParameterSpec());
      }
      return encoder;
    } 
    catch(InvalidKeyException | NoSuchAlgorithmException
        | NoSuchPaddingException | InvalidAlgorithmParameterException  ex) {
      throw new IllegalStateException("Error creating Cipher: "+ ex, ex);
    }
  }
  

  /**
   * Cria um cifrador para descriptografia a partir 
   * da chave informada.
   * @param key Chave <code>CryptKey</code>.
   * @return <code>Cipher</code>.
   */
  public static Cipher createDecryptCipher(CryptKey key) {
    Valid.off(key).forNull().fail(CryptKey.class);
    try {
      Cipher decoder = Cipher.getInstance(key.getAlgorithm().getStringAlgorithm());
      
      if(key.getAlgorithm() == CryptAlgorithm.AES_ECB
          || key.getAlgorithm() == CryptAlgorithm.AES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB_PKCS5) {
        decoder.init(Cipher.DECRYPT_MODE, key.getKeySpec());
      }
      else {
        decoder.init(Cipher.DECRYPT_MODE, key.getKeySpec(), key.getIV().getIvParameterSpec());
      }
      return decoder;
    } 
    catch(InvalidKeyException | NoSuchAlgorithmException
        | NoSuchPaddingException | InvalidAlgorithmParameterException  ex) {
      throw new IllegalStateException("Error creating Cipher: "+ ex, ex);
    }
  }
  
  
  /**
   * Cria um par de cifradores para criptografia e descriptografia 
   * respectivamente, a partir da chave informada.
   * @param key Chave <code>CryptKey</code>.
   * @return <code>Cipher</code>.
   */
  public static Cipher[] createEncryptDecryptCiphers(CryptKey key) {
    Valid.off(key).forNull().fail(CryptKey.class);
    try {
      Cipher[] cps = new Cipher[2];
      cps[0] = Cipher.getInstance(key.getAlgorithm().getStringAlgorithm());
      cps[1] = Cipher.getInstance(key.getAlgorithm().getStringAlgorithm());
      
      if(key.getAlgorithm() == CryptAlgorithm.AES_ECB
          || key.getAlgorithm() == CryptAlgorithm.AES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB_PKCS5) {
        cps[0].init(Cipher.ENCRYPT_MODE, key.getKeySpec());
        cps[1].init(Cipher.DECRYPT_MODE, key.getKeySpec());
      }
      else {
        cps[0].init(Cipher.ENCRYPT_MODE, key.getKeySpec(), key.getIV().getIvParameterSpec());
        cps[1].init(Cipher.DECRYPT_MODE, key.getKeySpec(), key.getIV().getIvParameterSpec());
      }
      return cps;
    } 
    catch(InvalidKeyException | NoSuchAlgorithmException
        | NoSuchPaddingException | InvalidAlgorithmParameterException  ex) {
      throw new IllegalStateException("Error creating Cipher: "+ ex, ex);
    }
  }
  
  
  /**
   * Cria um stream de saída para criptografia de dados.
   * @param out <code>OutputStream</code> para escrita 
   * dos dados criptografados.
   * @param key Chave <code>CryptKey</code>.
   * @return <code>OutputStream</code> para criptografia de dados.
   */
  public static CipherOutputStream createCipherOutputStream(OutputStream out, CryptKey key) {
    Valid.off(key).forNull().fail(CryptKey.class);
    Valid.off(out).forNull().fail(OutputStream.class);
    Cipher cp = createEncryptCipher(key);
    return new CipherOutputStream(out, cp);
  }
  
  
  /**
   * Cria um stream de entrada para descriptografia de dados.
   * @param in <code>InputStream</code> de onde serão lidos
   * os dados criptografados.
   * @param key Chave <code>CryptKey</code>.
   * @return <code>InputStream</code> para descriptografia de dados.
   */
  public static CipherInputStream createCipherInputStream(InputStream in, CryptKey key) {
    Valid.off(in).forNull().fail(InputStream.class);
    Valid.off(key).forNull().fail(CryptKey.class);
    Cipher cp = createDecryptCipher(key);
    return new CipherInputStream(in, cp);
  }
  
  
  /**
   * Trunca o byte array para o tamanho <code>length</code>
   * informado, preencehdo com dados repetidos, se necessário.
   * @param bs Byte array a ser truncado.
   * @param length Novo tamanho do byte array;
   * @return Byte array com o novo tamanho.
   */
  public static byte[] truncate(byte[] bs, int length) {
    if(bs == null || bs.length == 0 
        || length <= 0 || length == bs.length)
      return bs;
    
    byte[] nb = new byte[length];
    int idx = 0;
    int ibs = 0;
    while(idx < length) {
      if(ibs >= bs.length)
        ibs = 0;
      nb[idx++] = bs[ibs++];
    }
    return nb;
  }
  
  
  /**
   * Gera um byte array com dados aleatórios.
   * @param size tamanho do byte array a ser gerado.
   * @return byte array com dados aleatórios.
   */
  public static byte[] randomBytes(int size) {
    Valid.off(size).forNotBetween(1, Integer.MAX_VALUE);
    SecureRandom random = new SecureRandom();
    byte[] bs = new byte[size];
    random.nextBytes(bs);
    return bs;
  }
  
}
