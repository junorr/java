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
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import static us.pserver.chk.Checker.nullarg;


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
    nullarg(CryptKey.class, key);
    try {
      int size = 8;
      if(key.getAlgorithm() == CryptAlgorithm.AES_CBC
          || key.getAlgorithm() == CryptAlgorithm.AES_CBC_PKCS5)
        size = 16;
      
      IvParameterSpec iv = new IvParameterSpec(key.truncate(key.getHash(), size));
      Cipher encoder = Cipher.getInstance(key.getAlgorithm().toString());
      
      if(key.getAlgorithm() == CryptAlgorithm.AES_ECB
          || key.getAlgorithm() == CryptAlgorithm.AES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB_PKCS5) {
        encoder.init(Cipher.ENCRYPT_MODE, key.getSpec());
      }
      else {
        encoder.init(Cipher.ENCRYPT_MODE, key.getSpec(), iv);
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
    nullarg(CryptKey.class, key);
    try {
      int size = 8;
      if(key.getAlgorithm() == CryptAlgorithm.AES_CBC
          || key.getAlgorithm() == CryptAlgorithm.AES_CBC_PKCS5)
        size = 16;
      
      IvParameterSpec iv = new IvParameterSpec(key.truncate(key.getHash(), size));
      Cipher decoder = Cipher.getInstance(key.getAlgorithm().toString());
      
      if(key.getAlgorithm() == CryptAlgorithm.AES_ECB
          || key.getAlgorithm() == CryptAlgorithm.AES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB_PKCS5) {
        decoder.init(Cipher.DECRYPT_MODE, key.getSpec());
      }
      else {
        decoder.init(Cipher.DECRYPT_MODE, key.getSpec(), iv);
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
    nullarg(CryptKey.class, key);
    try {
      int size = 8;
      if(key.getAlgorithm() == CryptAlgorithm.AES_CBC
          || key.getAlgorithm() == CryptAlgorithm.AES_CBC_PKCS5)
        size = 16;
      
      IvParameterSpec iv = new IvParameterSpec(key.truncate(key.getHash(), size));
      Cipher[] cps = new Cipher[2];
      cps[0] = Cipher.getInstance(key.getAlgorithm().toString());
      cps[1] = Cipher.getInstance(key.getAlgorithm().toString());
      
      if(key.getAlgorithm() == CryptAlgorithm.AES_ECB
          || key.getAlgorithm() == CryptAlgorithm.AES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB
          || key.getAlgorithm() == CryptAlgorithm.DES_ECB_PKCS5
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB
          || key.getAlgorithm() == CryptAlgorithm.DESede_ECB_PKCS5) {
        cps[0].init(Cipher.ENCRYPT_MODE, key.getSpec());
        cps[1].init(Cipher.DECRYPT_MODE, key.getSpec());
      }
      else {
        cps[0].init(Cipher.ENCRYPT_MODE, key.getSpec(), iv);
        cps[1].init(Cipher.DECRYPT_MODE, key.getSpec(), iv);
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
    nullarg(CryptKey.class, key);
    nullarg(OutputStream.class, out);
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
    nullarg(CryptKey.class, key);
    nullarg(OutputStream.class, in);
    Cipher cp = createDecryptCipher(key);
    return new CipherInputStream(in, cp);
  }
  
}
