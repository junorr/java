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
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2015
 */
public class MixedWriteBuffer {
  
  public static final int DEFAULT_MEM_SIZE = 512 * 1024;

  private File temp;
  
  private ByteBuffer buffer;
  
  private RandomAccessFile raf;
  
  
  protected MixedWriteBuffer(ByteBuffer buf, File tmp) throws IOException {
    if(buf == null)
      throw new IllegalArgumentException(
          "[MixeWritedBuffer( ByteBuffer, File )] Invalid ByteBuffer: "+ buf);
    this.buffer = buf;
    temp = tmp;
    raf = null;
    if(temp != null) {
      raf = new RandomAccessFile(temp, "rw");
    }
  }
  
  
  public MixedWriteBuffer(int memsize) {
    if(memsize < 1) memsize = DEFAULT_MEM_SIZE;
    buffer = ByteBuffer.allocateDirect(memsize);
    raf = null;
  }
  
  
  public MixedWriteBuffer() {
    this(0);
  }
  
  
  public ByteBuffer getBuffer() {
    return buffer;
  }
  
  
  public File getTemp() {
    return temp;
  }
  
  
  private void initTemp() throws IOException {
    if(temp == null) {
      temp = File.createTempFile("MixedBuffer-", ".tmp");
      raf = new RandomAccessFile(temp, "rw");
    }
  }
  
  
  public void write(int b) throws IOException {
    if(buffer.hasRemaining()) {
      buffer.put((byte) b);
    }
    else {
      initTemp();
      raf.write(b);
    }
  }
  
  
  public void write(byte[] bs, int off, int len) throws IOException {
    if(bs == null || off < 0 || len > bs.length - off)
      throw new IllegalArgumentException(
          "[MixedWriteBuffer.write( [B, int, int )] Invalid Arguments {bs="
              + bs+ ", bs.length="+ (bs != null ? bs.length : 0)+ ", off="+ off+ ", len="+ len+ "}");
    
    if(buffer.hasRemaining()) {
      if(buffer.remaining() >= len) {
        buffer.put(bs, off, len);
      }
      else {
        int ilen = buffer.remaining();
        buffer.put(bs, off, ilen);
        off += ilen;
        len -= ilen;
        initTemp();
        raf.write(bs, off, len);
      }
    }
    else {
      initTemp();
      raf.write(bs, off, len);
    }
  }
  
  
  public void write(byte[] bs) throws IOException {
    if(bs == null)
      throw new IllegalArgumentException(
          "[MixedWriteBuffer.write( [B )] Invalid byte array {bs="+ bs+ "}");
    this.write(bs, 0, bs.length);
  }
  
  
  public OutputStream getOutputStream() {
    return new MixedBufferOutputStream(this);
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
  
  
  public MixedReadBuffer getReadBuffer() throws IOException {
    buffer.flip();
    if(raf != null) {
      raf.close();
    }
    return new MixedReadBuffer(buffer, temp);
  }
  
}
