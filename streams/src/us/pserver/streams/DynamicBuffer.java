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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/06/2015
 */
public class DynamicBuffer implements Closeable {
  
  public static Integer DEFAULT_PAGE_SIZE = 512 * 1024;

  private List<ByteBuffer> buffers;
  
  private int index;
  
  private int pageSize;
  
  private boolean read;
  
  
  public DynamicBuffer() {
    buffers = new ArrayList<>();
    index = 0;
    read = false;
    pageSize = DEFAULT_PAGE_SIZE;
  }
  
  
  public DynamicBuffer(int pageSize) {
    this();
    if(pageSize > 0) {
      this.pageSize = pageSize;
    }
  }
  
  
  public DynamicBuffer appendNew() {
    synchronized(DEFAULT_PAGE_SIZE) {
    buffers.add(ByteBuffer.allocateDirect(pageSize));
    index = buffers.size() -1;
    return this;
    }
  }
  
  
  public ByteBuffer getCurrentBuffer() {
    if(buffers.size() <= index) {
      this.appendNew();
    }
    return buffers.get(index);
  }
  
  
  public DynamicBuffer clear() {
    synchronized(DEFAULT_PAGE_SIZE) {
    buffers.forEach(b->b.reset());
    buffers.clear();
    return this;
    }
  }
  
  
  @Override
  public void close() {
    clear();
  }
  
  
  public boolean isReadingMode() {
    return read;
  }
  
  
  public DynamicBuffer flip() {
    synchronized(DEFAULT_PAGE_SIZE) {
    read = !read;
    buffers.forEach(b->b.flip());
    return this;
    }
  }
  
  
  private void setWriting() {
    synchronized(DEFAULT_PAGE_SIZE) {
    if(read) {
      flip();
      index = buffers.size() -1;
    }
    }
  }
  
  
  private void setReading() {
    synchronized(DEFAULT_PAGE_SIZE) {
    if(!read) {
      flip();
      index = 0;
    }
    }
  }
  
  
  public List<ByteBuffer> listBuffer() {
    return Collections.synchronizedList(
        Collections.unmodifiableList(buffers));
  }
  
  
  public OutputStream getOutputStream() {
    return new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        synchronized(DEFAULT_PAGE_SIZE) {
        setWriting();
        ByteBuffer buf = getCurrentBuffer();
        if(buf.remaining() < 1) 
          buf = appendNew().getCurrentBuffer();
        buf.put((byte) b);
        }
      }
      @Override
      public void write(byte[] bs, int off, int len) throws IOException {
        if(bs == null || bs.length < 1)
          throw new IllegalArgumentException(
              "Invalid byte array: "+ (bs != null ? bs.length : bs));
        if(off < 0 || off+len > bs.length)
          throw new IllegalArgumentException(
              "Invalid off position: "+ off);
        if(len < 1 || off+len > bs.length)
          throw new IllegalArgumentException(
              "Invalid length: "+ len);
        
        synchronized(DEFAULT_PAGE_SIZE) {
        setWriting();
        ByteBuffer buf = getCurrentBuffer();
        if(buf.remaining() < 1) 
          buf = appendNew().getCurrentBuffer();
        int nlen = Math.min(buf.remaining(), len);
        buf.put(bs, off, nlen);
        off += nlen;
        len -= nlen;
        if(len > 0) {
          write(bs, off, len);
        }
        }
      }
      @Override
      public void write(byte[] bs) throws IOException {
        write(bs, 0, bs.length);
      }
      @Override
      public void close() throws IOException {}
    };
  }
  
  
  public InputStream getInputStream() {
    return new InputStream() {
      @Override
      public int read() throws IOException {
        setReading();
        if(buffers.isEmpty() || index >= buffers.size()) 
          return -1;
        ByteBuffer buf = buffers.get(index);
        if(buf.remaining() < 1) { 
          index++;
          return read();
        } 
        return buf.get();
      }
      @Override
      public int read(byte[] bs, int off, int len) throws IOException {
        if(bs == null || bs.length < 1)
          throw new IllegalArgumentException(
              "Invalid byte array: "+ (bs != null ? bs.length : bs));
        if(off < 0 || off+len > bs.length)
          throw new IllegalArgumentException(
              "Invalid off position: "+ off);
        if(len < 1 || off+len > bs.length)
          throw new IllegalArgumentException(
              "Invalid length: "+ len);
        
        setReading();
        if(buffers.isEmpty() || index >= buffers.size()) 
          return -1;
        ByteBuffer buf = buffers.get(index);
        if(buf.remaining() < 1) { 
          index++;
          return read(bs, off, len);
        } 
        
        int nlen = Math.min(buf.remaining(), len);
        buf.get(bs, off, nlen);
        off += nlen;
        len -= nlen;
        if(len > 0) {
          nlen += read(bs, off, len);
        }
        return nlen;
      }
      @Override
      public int read(byte[] bs) throws IOException {
        return read(bs, 0, bs.length);
      }
    };
  }
  
}
