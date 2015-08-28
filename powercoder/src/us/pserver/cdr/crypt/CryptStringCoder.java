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

import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import us.pserver.tools.Valid;


/**
 * Codificador/Decodificador de criptografia 
 * para <code>String's</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class CryptStringCoder implements CryptCoder<String> {
  
  private CryptByteCoder coder;
  
  private Base64ByteCoder bbc;
  
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
    bbc = new Base64ByteCoder();
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
    Valid.off(str).forEmpty().fail("Invalid String: ");
    byte[] bs = scv.convert(str);
    if(encode) {
      bs = coder.encode(bs);
      bs = bbc.encode(bs);
    }
    else {
      bs = bbc.decode(bs);
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
  
}
