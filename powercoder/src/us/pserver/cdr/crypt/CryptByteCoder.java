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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import static us.pserver.cdr.Checker.nullarray;
import static us.pserver.cdr.Checker.range;


/**
 * Codificador/Decodificador de criptografia para 
 * byte array <code>(byte[])</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class CryptByteCoder implements CryptCoder<byte[]> {
  
  private Cipher encoder;
  
  private Cipher decoder;
  
  private CryptKey key;
  
  
  /**
   * Construtor padrão que recebe a chave de criptografia.
   * @param key Chave de criptografia <code>CryptKey</code>.
   */
  public CryptByteCoder(CryptKey key) {
    if(key == null || key.getAlgorithm() == null
        || key.getHash() == null || key.getSpec() == null)
      throw new IllegalArgumentException("Invalid key: "+ key);
    
    this.key = key;
    Cipher[] cps = CryptUtils.createEncryptDecryptCiphers(key);
    encoder = cps[0];
    decoder = cps[1];
  }
  
  
  @Override
  public CryptKey getKey() {
    return key;
  }


  /**
   * Retorna o cifrador de criptografia.
   * @return cifrador de criptografia.
   */
  public Cipher getEncoder() {
    return encoder;
  }


  /**
   * Retorna o cifrador de descriptografia.
   * @return cifrador de descriptografia.
   */
  public Cipher getDecoder() {
    return decoder;
  }
  
  
  @Override
  public byte[] encode(byte[] bs) {
    nullarray(bs);
    try {
      return encoder.doFinal(bs);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error encrypting: "+ ex, ex);
    }
  }
  
  
  @Override
  public byte[] decode(byte[] bs) {
    nullarray(bs);
    try {
      return decoder.doFinal(bs);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error decrypting: "+ ex, ex);
    }
  }
  
  
  @Override
  public byte[] apply(byte[] bs, boolean encode) {
    return (encode ? encode(bs) : decode(bs));
  }
  
  
  /**
   * Criptografa parte do byte array informado.
   * @param bs Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados codificados.
   */
  public byte[] encode(byte[] bs, int offset, int length) {
    nullarray(bs);
    range(offset, 0, bs.length -1);
    range(length, 1, bs.length);
    try {
      return encoder.doFinal(bs, offset, length);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error encrypting: "+ ex, ex);
    }
  }
  
  
  /**
   * Descriptografa parte do byte array informado.
   * @param bs Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados decodificados.
   */
  public byte[] decode(byte[] bs, int offset, int length) {
    nullarray(bs);
    range(offset, 0, bs.length -1);
    range(length, 1, bs.length);
    try {
      return decoder.doFinal(bs, offset, length);
    } catch(BadPaddingException | IllegalBlockSizeException ex) {
      throw new RuntimeException("Error decrypting: "+ ex, ex);
    }
  }
  
  
  /**
   * Aplica (des)criptografia em parte do byte array informado.
   * @param bs Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @param encode <code>true</code> para criptografar, 
   * <code>false</code> para descriptografar.
   * @return Byte array contendo os dados (de)codificados.
   */
  public byte[] apply(byte[] bs, int offset, int length, boolean encode) {
    return (encode ? encode(bs, offset, length) 
        : decode(bs, offset, length));
  }
  
  
  public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    CryptKey key = new CryptKey("mySecretPassword", 
        CryptAlgorithm.DES_CBC_PKCS5);
    CryptByteCoder ccd = new CryptByteCoder(key);
    
    byte[] bs = Files.readAllBytes(Paths.get("sample.log"));
    System.out.println("* bytes readed: "+ bs.length);
    bs = ccd.encode(bs);
    Files.write(Paths.get("sample2.log.bce"), bs, StandardOpenOption.WRITE);
    System.out.println("* bytes writed: "+ bs.length);
    
    bs = Files.readAllBytes(Paths.get("sample2.log.bce"));
    System.out.println("* bytes readed: "+ bs.length);
    bs = ccd.decode(bs);
    Files.write(Paths.get("sample2.log"), bs, StandardOpenOption.WRITE);
    System.out.println("* bytes writed: "+ bs.length);
  }
  
}
