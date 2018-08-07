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

package us.pserver.cdr.hex;

import java.util.Arrays;
import java.util.Objects;
import us.pserver.cdr.Coder;
import us.pserver.cdr.StringByteConverter;


/**
 * Codificador/Decodificador Hexadecimal para byte 
 * array <code>(byte[])</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class HexByteCoder implements Coder<byte[]> {
  
  private HexStringCoder hsc;
  
  private StringByteConverter scv;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public HexByteCoder() {
    hsc = new HexStringCoder();
    scv = new StringByteConverter();
  }


  @Override
  public byte[] apply(byte[] t, boolean encode) {
    return (encode ? encode(t) : decode(t));
  }


  @Override
  public byte[] encode(byte[] t) {
    Objects.requireNonNull(t);
    return scv.convert(HexCoder.toHexString(t));
  }


  @Override
  public byte[] decode(byte[] t) {
    Objects.requireNonNull(t);
    return HexCoder.fromHexString(scv.reverse(t));
  }
  

  /**
   * Aplica (de)codificação Hexadecimal em parte do byte 
   * array informado.
   * @param t Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @param encode <code>true</code> para codificar no formato
   * hexadecimal, <code>false</code> para decodificar.
   * @return Byte array contendo os dados (de)codificados.
   */
  public byte[] apply(byte[] t, int offset, int length, boolean encode) {
    return (encode ? encode(t, offset, length) 
        : decode(t, offset, length));
  }


  /**
   * Codifica parte do byte array informado no formato Hexadecimal.
   * @param t Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados codificados.
   */
  public byte[] encode(byte[] t, int offset, int length) {
    Objects.requireNonNull(t);
    return scv.convert(HexCoder.toHexString(
        Arrays.copyOfRange(t, offset, offset+length)));
  }


  /**
   * Decodifica parte do byte array informado no formato Hexadecimal.
   * @param t Byte array cuja parte será (de)codificada.
   * @param offset Índice inicial da parte do byte array.
   * @param length Tamanho da parte do byte array.
   * @return Byte array contendo os dados decodificados.
   */
  public byte[] decode(byte[] t, int offset, int length) {
    Objects.requireNonNull(t);
    return HexCoder.fromHexString(
        scv.reverse(Arrays.copyOfRange(
            t, offset, offset+length)));
  }
  
}
