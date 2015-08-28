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

import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.tools.Valid;


/**
 * Classe que representa uma chave de criptografia
 * com o respectivo algorítmo.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class CryptKey {
  
  private transient SecretKeySpec spec;
  
  private byte[] hash;
  
  private CryptAlgorithm algorithm;
  
  
  /**
   * Cria uma chave vazia.
   */
  public CryptKey() {
    hash = null;
    spec = null;
    algorithm = null;
  }
  
  
  /**
   * Construtor padrão que recebe a senha 
   * <code>String key</code> e o algorítmo de criptografia.
   * @param key Senha utilizada na criptografia.
   * @param algo Algorítmo de criptografia.
   */
  public CryptKey(String key, CryptAlgorithm algo) {
    if(key == null || key.isEmpty())
      throw new IllegalArgumentException("Invalid key: "+ key);
    if(algo == null)
      throw new IllegalArgumentException("Invalid Algorithm: "+ algo);
    
    algorithm = algo;
    hash = Digester.toSHA256(key);
    spec = KeySpecFactory.createKey(truncate(hash, algo.getBytesSize()), algo);
    if(hash == null || key.isEmpty())
      throw new IllegalArgumentException("Invalid key: "+ key);
    if(spec == null)
      throw new IllegalArgumentException("Invalid Algorithm: "+ algo);
  }


  /**
   * Trunca o byte array para o tamanho <code>length</code>
   * informado, preencehdo com dados repetidos, se necessário.
   * @param bs Byte array a ser truncado.
   * @param length Novo tamanho do byte array;
   * @return Byte array com o novo tamanho.
   */
  public byte[] truncate(byte[] bs, int length) {
    if(bs == null || bs.length == 0 || length <= 0)
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
   * Retorna a especificação da chave <code>SecretKeySpec</code>.
   * @return especificação da chave <code>SecretKeySpec</code>.
   */
  public SecretKeySpec getSpec() {
    if(spec == null && hash != null && algorithm != null)
      spec = KeySpecFactory.createKey(
          truncate(hash, algorithm.getBytesSize()), algorithm);
    return spec;
  }


  /**
   * Define a especificação da chave <code>SecretKeySpec</code>.
   * @param spec especificação da chave <code>SecretKeySpec</code>.
   */
  public void setSpec(SecretKeySpec spec) {
    this.spec = spec;
  }


  /**
   * Retorna o hash SHA-256 gerado a partir da senha.
   * @return Hash SHA-256.
   */
  public byte[] getHash() {
    return hash;
  }


  /**
   * Define o hash SHA-256 gerado a partir da senha.
   * @param hash Hash SHA-256.
   */
  public void setHash(byte[] hash) {
    this.hash = hash;
  }


  /**
   * Retorna o algorítmo de criptografia.
   * @return algorítmo de criptografia <code>CryptAlgorithm</code>.
   */
  public CryptAlgorithm getAlgorithm() {
    return algorithm;
  }


  /**
   * Define o algorítmo de criptografia.
   * @param algorithm algorítmo de criptografia 
   * <code>CryptAlgorithm</code>.
   */
  public void setAlgorithm(CryptAlgorithm algorithm) {
    this.algorithm = algorithm;
  }
  
  
  /**
   * Define o hash SHA-256 gerado a partir da senha
   * e o algorítmo de criptografia.
   * @param bs Hash SHA-256.
   * @param algo algorítmo de criptografia 
   * <code>CryptAlgorithm</code>.
   */
  public void setKey(byte[] bs, CryptAlgorithm algo) {
    hash = bs;
    algorithm = algo;
    spec = KeySpecFactory.createKey(truncate(hash, algo.getBytesSize()), algo);
  }
  
  
  @Override
  public String toString() {
    if(hash == null || algorithm == null)
      return null;
    Base64ByteCoder cd = new Base64ByteCoder();
    StringByteConverter cv = new StringByteConverter();
    byte[] bs = cd.encode(hash);
    return algorithm.getAlgorithm() + "||" + cv.reverse(bs);
  }
  
  
  /**
   * Decodifica a <code>String</code> informada
   * no objeto <code>CryptKey</code> retornado, 
   * ou <code>null</code> caso a <code>String</code> 
   * não esteja no formato adequado.
   * @param str <code>CryptKey String</code>
   * @return <code>CryptKey</code> interpretada
   * a partir da <code>String</code> informada,
   * ou <code>null</code> em caso de erro.
   */
  public static CryptKey fromString(String str) {
    if(str == null || str.isEmpty() || !str.contains("||"))
      return null;
    Base64ByteCoder cd = new Base64ByteCoder();
    StringByteConverter cv = new StringByteConverter();
    String[] ss = str.split("\\|\\|");
    CryptKey k = new CryptKey();
    k.setKey(
        cd.decode(cv.convert(ss[1])), 
        CryptAlgorithm.fromString(ss[0]));
    return k;
  }
  
  
  /**
   * Cria um objeto <code>CryptKey</code> com o 
   * algorítmo informado, utilizando uma senha
   * gerada aleatoriamente.
   * @param algo Algorítmo de criptografia.
   * @return <code>CryptKey</code>.
   */
  public static CryptKey createRandomKey(CryptAlgorithm algo) {
    Valid.off(algo).forNull().fail(CryptAlgorithm.class);
    CryptKey key = new CryptKey();
    key.setKey(KeyGenerator.instance()
        .genAsBytes(algo.getBytesSize()), algo);
    return key;
  }
  
  
  public static void main(String[] args) {
    CryptKey key = new CryptKey("123456", CryptAlgorithm.AES_CBC_PKCS5);
    System.out.println("* key.hash = "+ Arrays.toString(key.getHash()));
    System.out.println("* key = "+ key.toString());
    key = CryptKey.fromString(key.toString());
    System.out.println("* key.hash = "+ Arrays.toString(key.getHash()));
    System.out.println("* key = "+ key.toString());
  }
  
}
