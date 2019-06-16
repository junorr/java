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

package net.powercoder.cdr.crypt;

import java.util.Objects;
import net.powercoder.cdr.StringByteConverter;
import net.powercoder.cdr.hex.HexByteCoder;


/**
 * Codificador/Decodificador de criptografia 
 * para <code>String's</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class CryptStringCoder implements CryptCoder<String> {
  
  private CryptByteCoder coder;
  
  private HexByteCoder hxc;
  
  private StringByteConverter scv;
  
  
  /**
   * Construtor padrão que recebe a chave de criptografia.
   * @param key chave de criptografia <code>CryptKey</code>.
   */
  public CryptStringCoder(CryptKey key) {
    if(key == null)
      throw new IllegalArgumentException(
          "Illegal CryptKey: "+ key);
    coder = new CryptByteCoder(key);
    hxc = new HexByteCoder();
    scv = new StringByteConverter();
  }
  
  
  @Override
  public CryptKey getKey() {
    return coder.getKey();
  }
  
  
  /**
   * Retorna o codificador de criptografia para byte array.
   * @return codificador de criptografia para byte array.
   */
  public CryptByteCoder getCoder() {
    return coder;
  }
  
  
  @Override
  public String apply(String  str, boolean encode) {
    Objects.requireNonNull(str);
    byte[] bs = scv.convert(str);
    if(encode) {
      bs = coder.encode(bs);
      bs = hxc.encode(bs);
    }
    else {
      bs = hxc.decode(bs);
      bs = coder.decode(bs);
    }
    return scv.reverse(bs);
  }
  
  
  @Override
  public String encode(String str) {
    return apply(str, true);
  }
  
  
  @Override
  public String decode(String str) {
    return apply(str, false);
  }
  
  
  private static final CryptKey KEY = CryptKey.createWithUnsecurePasswordIV(
      "4c036dad7048d8d7d9fa1c42964c54ba5c676a2f53ba9ee9e18d909a997849f1",
      CryptAlgorithm.AES_CBC_256_PKCS5
  );
  
  
  public static void main(String[] args) {
    CryptStringCoder cs = new CryptStringCoder(KEY);
    System.out.println("* Decode 'BS3g1Nd2o/S/wVT5Se6YTg=='"+ "='"+ cs.decode("BS3g1Nd2o/S/wVT5Se6YTg==")+ "'");
    String str = "BLS";
    String enc = cs.encode(str);
    System.out.println("* Encode '"+ str+ "'='"+ enc+ "'");
    str = cs.decode(enc);
    System.out.println("* Decode '"+ enc+ "'='"+ str+ "'");
  }
  
}
