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

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A UTF-8 String representation.
 * @author Juno Roesler - juno@pserver.us
 */
public class UTF8String extends CharsetString {

  
  /**
   * <code>utf8 = "UTF-8";</code><br>
   * String UTF-8 Charset representation.
   */
  private static final String utf8 = "UTF-8";
  
  
  /**
   * Constructor which receives the encapsulated String.
   * @param str the encapsulated String.
   */
  public UTF8String(String str) {
    super(str, Charset.forName(utf8));
  }
  
  
  /**
   * Constructor which receives the byte array to encode in a UTF-8 String.
   * @param bs the byte array to encode in a UTF-8 String.
   */
  public UTF8String(byte[] bs) {
    super(bs, Charset.forName(utf8));
  }
  
  
  /**
   * Constructor which receives the byte array to encode in a UTF-8 String.
   * @param bs the byte array to encode in a UTF-8 String.
   * @param off start array index.
   * @param len Length of bytes to be readed from the byte array.
   */
  public UTF8String(byte[] bs, int off, int len) {
    super(bs, off, len, Charset.forName(utf8));
  }
  
  
  /**
   * Constructor which receives the byte array to encode in a UTF-8 String.
   * @param buf ByteBuffer to decode.
   */
  public UTF8String(ByteBuffer buf) {
    super(buf, StandardCharsets.UTF_8);
  }
  
  
  /**
   * Format a string with the interpolated object values in a UTF8String.
   * @param str The string to format.
   * @param args The objects values to be interpolated into the string.
   * @return a new UTF8String object.
   */
  public static UTF8String format(String str, Object ... args) {
    return from(String.format(str, args));
  }
  
  
  /**
   * Creates a UTF8String object encapsulating the specified String.
   * @param str The encapsulated String.
   * @return a new UTF8String object.
   */
  public static UTF8String from(String str) {
    return new UTF8String(str);
  }
  
  
  /**
   * Create a UTF8String object, encoding the byte array in a UTF-8 String.
   * @param bs The byte array to be encoded into a UTF-8 String.
   * @param off The start index of the array.
   * @param len The length of bytes.
   * @return a new UTF8String object.
   */
  public static UTF8String from(byte[] bs, int off, int len) {
    return new UTF8String(bs, off, len);
  }
  
  
  /**
   * Create a UTF8String object, encoding the byte array in a UTF-8 String.
   * @param bs The byte array to be encoded into a UTF-8 String.
   * @return a new UTF8String object.
   */
  public static UTF8String from(byte[] bs) {
    return new UTF8String(bs);
  }
  
  
  /**
   * Create a UTF8String object, encoding the byte array in a UTF-8 String.
   * @param bs The byte array to be encoded into a UTF-8 String.
   * @return a new UTF8String object.
   */
  public static UTF8String from(ByteBuffer buf) {
    return new UTF8String(buf);
  }
  
}
