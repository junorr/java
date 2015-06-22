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

package us.pserver.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import us.pserver.cdr.crypt.CryptKey;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 19/06/2015
 */
public class EncoderInputStream extends FilterInputStream {
  
  public static final Integer DEFAULT_TEMP_BUFFER_SIZE = 4096;
  

  private DynamicBuffer buffer;
  
  private OutputStream encout;
  
  private InputStream source;
  
  private int closed;
  
  private byte[] temp;
  
  
  public EncoderInputStream(InputStream src) {
    super(src);
    if(src == null)
      throw new IllegalArgumentException("Invalid null source InputStream");
    source = src;
    buffer = new DynamicBuffer();
    encout = null;
    closed = 0;
    temp = new byte[DEFAULT_TEMP_BUFFER_SIZE];
  }
  
  
  public EncoderInputStream(InputStream src, int tempBufSize) {
    this(src);
    if(tempBufSize > 0)
      temp = new byte[tempBufSize];
  }
  
  
  public EncoderInputStream setBase64CoderEnabled(boolean enabled) {
    buffer.setBase64CoderEnabled(enabled);
    return this;
  }
  
  
  public EncoderInputStream setGZipCoderEnabled(boolean enabled) {
    buffer.setGZipCoderEnabled(enabled);
    return this;
  }
  
  
  public EncoderInputStream setCryptCoderEnabled(boolean enabled, CryptKey key) {
    buffer.setCryptCoderEnabled(enabled, key);
    return this;
  }
  
  
  public boolean isBase64CoderEnabled() {
    return buffer.isBase64CoderEnabled();
  }
  
  
  public boolean isGZipCoderEnabled() {
    return buffer.isGZipCoderEnabled();
  }
  
  
  public boolean isCryptCoderEnabled() {
    return buffer.isCryptCoderEnabled();
  }
  
  
  private void fillBuffer() throws IOException {
    if(closed > 0) {
      closed++;
      return;
    }
    buffer.reset();
    if(encout == null) {
      encout = buffer.getEncoderStream();
    }
    while(buffer.size() < 1) {
      int read = source.read(temp);
      System.out.println("-> fillBuffer.read = "+ read);
      if(read < 1) {
        closed++;
        source.close();
        encout.flush();
        encout.close();
        System.out.println("-> fillBuffer.size = "+ buffer.size());
        break;
      }
      encout.write(temp, 0, read);
    }
  }
  
  
  @Override
  public int read() throws IOException {
    synchronized(DEFAULT_TEMP_BUFFER_SIZE) {
    if(closed > 1) return -1;
    if(buffer.size() > 0) {
      int read = buffer.getInputStream().read();
      if(read == -1) {
        fillBuffer();
        return read();
      }
      return read;
    }
    else {
      fillBuffer();
      return read();
    }
    }
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    synchronized(DEFAULT_TEMP_BUFFER_SIZE) {
    if(closed > 1) return -1;
    if(bs == null)
      throw new IllegalArgumentException("Invalid null byte array");
    if(bs.length == 0 || len < 1) return -1;
    if(off < 0 || off + len > bs.length)
      throw new IllegalArgumentException("Invalid arguments: off="+ off+ ", len="+ len);
    
    if(buffer.size() > 0) {
      int read = buffer.getInputStream().read(bs, off, len);
      if(read < 1) {
        fillBuffer();
        return read(bs, off, len);
      }
      return read;
    }
    else {
      fillBuffer();
      return read(bs, off, len);
    }
    }
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return read(bs, 0, (bs != null ? bs.length : 0));
  }
  
}
