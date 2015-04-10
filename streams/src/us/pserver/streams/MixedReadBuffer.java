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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2015
 */
public class MixedReadBuffer {
  
  public static final int DEFAULT_MEM_SIZE = 512 * 1024;

  private File temp;
  
  private ByteBuffer buffer;
  
  private RandomAccessFile raf;
  
  private long mark, idx;
  
  
  protected MixedReadBuffer(ByteBuffer buf, File tmp) throws IOException {
    if(buf == null)
      throw new IllegalArgumentException(
          "[MixeReadBuffer( ByteBuffer, File )] Invalid ByteBuffer: "+ buf);
    this.buffer = buf;
    temp = tmp;
    raf = null;
    mark = 0;
    idx = 0;
    if(temp != null) {
      raf = new RandomAccessFile(temp, "rw");
    }
  }
  
  
  public ByteBuffer getBuffer() {
    return buffer;
  }
  
  
  public File getTemp() {
    return temp;
  }
  
  
  public int read() throws IOException {
    int b = -1;
    if(buffer == null)
      return b;
    if(buffer.hasRemaining()) {
      b = buffer.get();
      idx++;
    }
    else if(raf != null) {
      b = raf.read();
      idx++;
    }
    return b;
  }
  
  
  public int read(byte[] bs, int off, int len) throws IOException {
    if(bs == null || off < 0 || len > bs.length - off)
      throw new IllegalArgumentException(
          "[MixedReadBuffer.read( [B, int, int )] Invalid Arguments {bs="
              + bs+ ", bs.length="+ (bs != null ? bs.length : 0)+ ", off="+ off+ ", len="+ len+ "}");
    
    int read = 0;
    int ilen = len;
    if(buffer.hasRemaining()) {
      if(ilen > buffer.remaining())
        ilen = buffer.remaining();
      buffer.get(bs, off, ilen);
      read = ilen;
      off += ilen;
      idx += ilen;
      ilen = len - ilen;
    }
    if(raf != null && ilen > 0) {
      if(ilen > raf.length())
        ilen = (int) raf.length();
      raf.read(bs, off, ilen);
      read += ilen;
      idx += ilen;
    }
    return read;
  }
  
  
  public int read(byte[] bs) throws IOException {
    if(bs == null)
      throw new IllegalArgumentException(
          "[MixedReadBuffer.write( [B )] Invalid byte array {bs="+ bs+ "}");
    return this.read(bs, 0, bs.length);
  }
  
  
  public MixedReadBuffer rewind() throws IOException {
    buffer.rewind();
    idx = 0;
    mark = 0;
    if(raf != null) {
      raf.seek(0);
    }
    return this;
  }
  
  
  public MixedReadBuffer mark() throws IOException {
    mark = idx;
    if(buffer.hasRemaining())
      buffer.mark();
    return this;
  }
  
  
  public MixedReadBuffer reset() throws IOException {
    idx = mark;
    if(idx < buffer.limit()) {
      buffer.reset();
    }
    else if(raf != null) {
      raf.seek(idx - buffer.limit());
    }
    return this;
  }
  
  
  public long length() throws IOException {
    long sz = buffer.limit();
    if(raf != null)
      sz += raf.length();
    return sz;
  }
  
  
  public long index() {
    return idx;
  }
  
  
  public MixedReadBuffer seek(long pos) throws IOException {
    if(pos < 0 || pos > length()) {
      throw new IndexOutOfBoundsException(
          "[MixedReadBuffer.seek( long )] Invalid position {pos="+ pos+ "}");
    }
    if(pos < buffer.limit()) {
      buffer.position((int) pos);
    }
    else if(raf != null && raf.length() >= pos - buffer.limit()) {
      raf.seek(pos - buffer.limit());
    }
    return this;
  }
  
  
  public InputStream getInputStream() {
    return new MixedBufferInputStream(this);
  }
  
  
  public void close() {
    try {
      buffer.clear();
      buffer = null;
      if(raf != null)
        raf.close();
      if(temp != null) {
        temp.deleteOnExit();
        temp.delete();
      }
    } catch(Exception e) {}
  }
  
  
  public MixedWriteBuffer getWriteBuffer() throws IOException {
    buffer.flip();
    if(raf != null) {
      raf.close();
    }
    return new MixedWriteBuffer(buffer, temp);
  }
  
}
