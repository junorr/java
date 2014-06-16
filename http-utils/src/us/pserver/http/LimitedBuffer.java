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

package us.pserver.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import static us.pserver.http.StreamUtils.UTF8;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/06/2014
 */
public class LimitedBuffer {

  private byte[] buffer;
  
  private int index;
  
  
  public LimitedBuffer(int size) {
    if(size < 1)
      throw new IllegalArgumentException(
          "Invalid buffer size ["+ size+ "]");
    buffer = new byte[size];
    index = 0;
  }
  
  
  public byte[] buffer() {
    return buffer;
  }
  
  
  public int size() {
    return index;
  }
  
  
  public int length() {
    return buffer.length;
  }
  
  
  public LimitedBuffer reset() {
    index = 0;
    return this;
  }
  
  
  public boolean checkIndex(int idx) {
    return idx >= 0 && idx < buffer.length;
  }
  
  
  private void throwIndexException(int idx) {
    throw new IllegalArgumentException(
        "Invalid index ["+ idx+ "] (bounds=0,"
        + (buffer.length-1)+ ")");
  }
  
  
  public byte get(int idx) {
    if(!checkIndex(idx))
      throwIndexException(idx);
    return buffer[idx];
  }
  
  
  public LimitedBuffer set(int idx, byte b) {
    if(!checkIndex(idx))
      throwIndexException(idx);
    buffer[idx] = b;
    return this;
  }
  
  
  public LimitedBuffer put(byte b) {
    if(index >= buffer.length) {
      for(int i = 0; i < buffer.length -1; i++) {
        buffer[i] = buffer[i+1];
      }
      index = buffer.length -1;
    }
    buffer[index++] = b;
    return this;
  }
  
  
  public LimitedBuffer put(int b) {
    return put((byte) b);
  }
  
  
  public String toString(String charset) {
    try {
      return new String(buffer, 0, index, charset);
    } catch(UnsupportedEncodingException e) {
      return new String(buffer);
    }
  }
  
  
  public String toUTF8() {
    return toString(UTF8);
  }
  
  
  public String toStringArray() {
    return Arrays.toString(Arrays.copyOfRange(buffer, 0, index));
  }
  
  
  public LimitedBuffer writeTo(OutputStream out) throws IOException {
    out.write(buffer, 0, index);
    return this;
  }
  
  
  public static void main(String[] args) {
    LimitedBuffer lb = new LimitedBuffer(10);
    for(int i = 0; i < 15; i++) {
      lb.put(i);
      System.out.println(lb.toStringArray());
    }
  }
  
}
