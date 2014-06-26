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

package us.pserver.cdr.b64;

import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import static us.pserver.cdr.Checker.nullarray;
import static us.pserver.cdr.Checker.range;
import us.pserver.cdr.Coder;


/**
 * Codificador/Decodificador Base64 para byte 
 * array <code>byte[]</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class Base64ByteCoder implements Coder<byte[]> {

  
  @Override
  public byte[] apply(byte[] t, boolean encode) {
    nullarray(t);
    
    if(encode)
      return Base64.encodeBase64(t);
    else
      return Base64.decodeBase64(t);
  }


  @Override
  public byte[] encode(byte[] t) {
    return apply(t, true);
  }


  @Override
  public byte[] decode(byte[] t) {
    return apply(t, false);
  }
  

  /**
   * Aplica (de)codificação Base64 na porção do byte 
   * array informado.
   * @param t Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @param encode <code>true</code> para codificar no formato
   * Base64, <code>false</code> para decodificar do formato Base64.
   * @return Byte array contendo os dados (de)codificados.
   */
  public byte[] apply(byte[] t, int offset, int length, boolean encode) {
    nullarray(t);
    range(offset, 0, t.length-2);
    range(length, 1, t.length-offset);
    
    byte[] bs = Arrays.copyOfRange(t, offset, offset+length);
    if(encode)
      return Base64.encodeBase64(bs);
    else
      return Base64.decodeBase64(bs);
  }


  /**
   * Codifica parte do byte array informado no formato Base64.
   * @param t Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados codificados.
   */
  public byte[] encode(byte[] t, int offset, int length) {
    return apply(t, offset, length, true);
  }


  /**
   * Decodifica parte do byte array informado no formato Base64.
   * @param t Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados decodificados.
   */
  public byte[] decode(byte[] t, int offset, int length) {
    return apply(t, offset, length, false);
  }
  
}
