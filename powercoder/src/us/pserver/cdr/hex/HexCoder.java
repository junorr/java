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

import static us.pserver.chk.Checker.nullarray;
import static us.pserver.chk.Checker.nullstr;


/**
 * Classe utilitária para codificação/decodificação
 * hexadecimal utilizada pelas demais implementações.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class HexCoder {
  
  
  /**
   * Codifica o byte array informado em uma
   * <code>String</code> hexadecimal.
   * @param array byte array a ser convertido.
   * @return <code>String</code> no formato
   * hexadecimal criada a partir dos dados
   * do byte array informado.
   */
  public static String toHexString(byte[] array) {
    nullarray(array);
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < array.length; i++) {
      int high = ((array[i] >> 4) & 0xf) << 4;
      int low = array[i] & 0xf;
      if(high == 0) sb.append("0");
      sb.append(Integer.toHexString(high | low));
    }
    return sb.toString();
  }
  
  
  /**
   * Decodifica a <code>String</code> no formato 
   * hexadecimal para um byte array.
   * @param hex <code>String</code> no formato
   * hexadecimal a ser decodificada.
   * @return byte array com os dados decodificados
   * da <code>String</code> informada.
   */
  public static byte[] fromHexString(String hex) {
    nullstr(hex);
    int len = hex.length();
    byte[] bytes = new byte[len / 2];
    for(int i = 0; i < len; i += 2) {
      if(i == len-1) break;
      else
        bytes[i / 2] = (byte) (
            (Character.digit(hex.charAt(i), 16) << 4)
            + Character.digit(hex.charAt(i + 1), 16));
    }
    return bytes;
  }
  
  
  /**
   * Atalho para {@link #fromHexString(java.lang.String) }.
   * @param str <code>String</code> hexadecimal.
   * @return byte array.
   * @see us.pserver.cdr.hex.HexCoder#fromHexString(java.lang.String) 
   */
  public static byte[] decode(String str) {
    return fromHexString(str);
  }
  
  
  /**
   * Atalho para {@link #toHexString(byte[]) }
   * @param bs byte array.
   * @return <code>String</code> hexadecimal.
   * @see us.pserver.cdr.hex.HexCoder#toHexString(byte[]) 
   */
  public static String encode(byte[] bs) {
    return toHexString(bs);
  }
  
}
