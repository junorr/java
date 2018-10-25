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

/**
 * A UTF-8 String representation.
 * @author Juno Roesler - juno@pserver.us
 */
public class CharsetString {

  
  private final Charset charset;
  
  
  private final String string;
  
  
  /**
   * Constructor which receives the encapsulated String.
   * @param str the encapsulated String.
   */
  public CharsetString(String str, Charset cs) {
    string = Match.notEmpty(str).getOrFail();
    charset = Match.notNull(cs).getOrFail("Bad null Charset");
  }
  
  
  /**
   * Constructor which receives the byte array to encode in a UTF-8 String.
   * @param bs the byte array to encode in a UTF-8 String.
   * @param cs
   */
  public CharsetString(byte[] bs, Charset cs) {
    charset = Match.notNull(cs).getOrFail("Bad null Charset");
    string = new String(bs, cs);
  }
  
  
  /**
   * Constructor which receives the byte array to encode in a UTF-8 String.
   * @param bs the byte array to encode in a UTF-8 String.
   * @param off start array index.
   * @param len Length of bytes to be readed from the byte array.
   */
  public CharsetString(byte[] bs, int off, int len, Charset cs) {
    charset = Match.notNull(cs).getOrFail("Bad null Charset");
    string = new String(bs, off, len, cs);
  }
  
  
  /**
   * Constructor which receives the byte array to encode in a UTF-8 String.
   * @param bs the byte array to encode in a UTF-8 String.
   * @param cs
   */
  public CharsetString(ByteBuffer buf, Charset cs) {
    charset = Match.notNull(cs).getOrFail("Bad null Charset");
    string = cs.decode(buf).toString();
  }
  
  
  /**
   * Get the length of this CharsetString.
   * @return the length of this CharsetString.
   */
  public int length() {
    return string.length();
  }
  
  
  /**
   * Check if this String is empty.
   * @return <code>true</code> if this CharsetString is empty,
   * <code>false</code> otherwise.
   */
  public boolean isEmpty() {
    return string.isEmpty();
  }
  
  
  /**
   * Check if this string is empty when trimmed.
   * @return <code>true</code> if this CharsetString is empty when trimmed,
   * <code>false</code> otherwise.
   */
  public boolean isEmptyTrimmed() {
    return string.trim().isEmpty();
  }
  
  
  /**
   * Get the UTF-8 Charset object.
   * @return the UTF-8 Charset object.
   */
  public Charset getCharset() {
    return charset;
  }
  
  
  /**
   * Return "UTF-8".
   * @return "UTF-8".
   */
  public String getCharsetString() {
    return charset.name();
  }
  
  
  /**
   * Format a string with the interpolated object values in a CharsetString.
   * @param str The string to format.
   * @param cs
   * @param args The objects values to be interpolated into the string.
   * @return a new CharsetString object.
   */
  public static CharsetString format(String str, Charset cs, Object ... args) {
    return from(String.format(str, args), cs);
  }
  
  
  /**
   * Creates a CharsetString object encapsulating the specified String.
   * @param str The encapsulated String.
   * @param cs
   * @return a new CharsetString object.
   */
  public static CharsetString from(String str, Charset cs) {
    return new CharsetString(str, cs);
  }
  
  
  /**
   * Create a CharsetString object, encoding the byte array in a UTF-8 String.
   * @param bs The byte array to be encoded into a UTF-8 String.
   * @param off The start index of the array.
   * @param len The length of bytes.
   * @param cs
   * @return a new CharsetString object.
   */
  public static CharsetString from(byte[] bs, int off, int len, Charset cs) {
    return new CharsetString(bs, off, len, cs);
  }
  
  
  /**
   * Create a CharsetString object, encoding the byte array in a UTF-8 String.
   * @param bs The byte array to be encoded into a UTF-8 String.
   * @param cs
   * @return a new CharsetString object.
   */
  public static CharsetString from(byte[] bs, Charset cs) {
    return new CharsetString(bs, cs);
  }
  
  
  /**
   * Create a CharsetString object, encoding the byte array in a UTF-8 String.
   * @param bs The byte array to be encoded into a UTF-8 String.
   * @param cs
   * @return a new CharsetString object.
   */
  public static CharsetString from(ByteBuffer buf, Charset cs) {
    return new CharsetString(buf, cs);
  }
  
  
  @Override
  public String toString() {
    return string;
  }
  

  /**
   * Return a byte array from the encapsulated UTF-8 String.
   * @return byte array from the encapsulated UTF-8 String.
   */
  public byte[] getBytes() {
    return string.getBytes(getCharset());
  }
  
}
