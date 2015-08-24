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
import us.pserver.cdr.crypt.CryptKey;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/06/2015
 */
public class DynamicBuffer implements Closeable {
  
  public static final Integer DEFAULT_PAGE_SIZE = 512 * 1024;
  

  private List<ByteBuffer> pages;
  
  private int index;
  
  private int pageSize;
  
  private Instance<Long> size;
  
  private Instance<Boolean> read;
  
  private StreamCoderFactory coder;
  
  
  public DynamicBuffer() {
    pages = Collections.synchronizedList(new ArrayList<ByteBuffer>());
    index = 0;
    read = new Instance<>(false);
    pageSize = DEFAULT_PAGE_SIZE;
    coder = StreamCoderFactory.getNew();
    size = new Instance<>(0L);
  }
  
  
  public DynamicBuffer(int pageSize) {
    this();
    if(pageSize > 0) {
      this.pageSize = pageSize;
    }
  }
  
  
  private DynamicBuffer appendNew() {
    synchronized(DEFAULT_PAGE_SIZE) {
      pages.add(ByteBuffer.allocate(pageSize));
      index = pages.size() -1;
      return this;
    }
  }
  
  
  public DynamicBuffer setCryptCoderEnabled(boolean enabled, CryptKey key) {
    coder.setCryptCoderEnabled(enabled, key);
    return this;
  }
  
  
  public DynamicBuffer setGZipCoderEnabled(boolean enabled) {
    coder.setGZipCoderEnabled(enabled);
    return this;
  }
  
  
  public DynamicBuffer setBase64CoderEnabled(boolean enabled) {
    coder.setBase64CoderEnabled(enabled);
    return this;
  }
  
  
  public boolean isCryptCoderEnabled() {
    return coder.isCryptCoderEnabled();
  }
  
  
  public boolean isGZipCoderEnabled() {
    return coder.isGZipCoderEnabled();
  }
  
  
  public boolean isBase64CoderEnabled() {
    return coder.isBase64CoderEnabled();
  }
  
  
  public boolean isAnyCoderEnabled() {
    return coder.isAnyCoderEnabled();
  }
  
  
  private ByteBuffer getCurrentPage() {
    if(pages.size() <= index) {
      this.appendNew();
    }
    return pages.get(index);
  }
  
  
  private ByteBuffer nextPage() {
    ByteBuffer buf = getCurrentPage();
    if(buf.remaining() < 1) {
      if(index + 1 < pages.size() 
          && pages.get(index+1).remaining() > 0)
        buf = pages.get(++index);
      else
        buf = appendNew().getCurrentPage();
    }
    return buf;
  }
  
  
  public int getPageSize() {
    return pageSize;
  }
  
  
  public int getIndex() {
    return index;
  }
  
  
  public DynamicBuffer setPageSize(int pg) {
    if(pg > 0)
      pageSize = pg;
    return this;
  }
  
  
  public int getUsedPages() {
    return pages.size();
  }
  
  
  public List<ByteBuffer> pagesList() {
    return Collections.unmodifiableList(pages);
  }
  
  
  public long size() {
    return size.getNumber().longValue();
  }
  
  
  public DynamicBuffer rewind() {
    pages.forEach(b->b.rewind());
    return positionStart();
  }
  
  
  @Override
  public void close() {
    pages.forEach(b->b.reset());
    pages.clear();
  }
  
  
  public boolean isReading() {
    return read.isTrue();
  }
  
  
  public DynamicBuffer reset() {
    read.setFalse();
    pages.forEach(b->b.clear());
    size.set(0L);
    return positionStart();
  }
  
  
  public DynamicBuffer flip() {
    synchronized(DEFAULT_PAGE_SIZE) {
      read.invertBoolean();
      pages.forEach(b->b.flip());
      return this;
    }
  }
  
  
  public DynamicBuffer positionEnd() {
    if(!pages.isEmpty())
      synchronized(DEFAULT_PAGE_SIZE) {
        index = pages.size() -1;
      }
    return this;
  }
  
  
  public DynamicBuffer positionStart() {
    synchronized(DEFAULT_PAGE_SIZE) {
      index = 0;
    }
    return this;
  }
  
  
  public DynamicBuffer setWriting() {
    if(read.isTrue()) flip();
    return this;
  }
  
  
  public DynamicBuffer setReading() {
    if(!read.isTrue()) {
      flip();
      positionStart();
    }
    return this;
  }
  
  
  public OutputStream getEncoderStream() throws IOException {
    if(!coder.isAnyCoderEnabled())
      return getOutputStream();
    return coder.create(getOutputStream());
  }
  
  
  public InputStream getDecoderStream() throws IOException {
    if(!coder.isAnyCoderEnabled())
      return getInputStream();
    return coder.create(getInputStream());
  }
  
  
  public OutputStream getOutputStream() {
    return new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        synchronized(DEFAULT_PAGE_SIZE) {
          setWriting();
          ByteBuffer buf = nextPage();
          buf.put((byte) b);
          size.increment();
        }
      }
      @Override
      public void write(byte[] bs, int off, int len) throws IOException {
        if(bs == null) {
          throw new IllegalArgumentException(
              "Invalid byte array: "+ bs);
        }
        if(bs.length == 0 || len == 0) return;
        
        if(off < 0 || off+len > bs.length)
          throw new IllegalArgumentException(
              "Invalid off position: "+ off);
        if(len < 1 || off+len > bs.length)
          throw new IllegalArgumentException(
              "Invalid length: "+ len);
        
        synchronized(DEFAULT_PAGE_SIZE) {
          setWriting();
          ByteBuffer buf = nextPage();
          int nlen = Math.min(buf.remaining(), len);
          buf.put(bs, off, nlen);
          off += nlen;
          len -= nlen;
          size.plus(nlen);
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
        synchronized(DEFAULT_PAGE_SIZE) {
        setReading();
        int read = -1;
        if(pages.isEmpty() || index >= pages.size()) {
          return read;
        }
        ByteBuffer buf = pages.get(index);
        if(buf.remaining() < 1) { 
          index++;
          read = read();
        } 
        else {
          read = buf.get();
        }
        return read;
        }
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
        
        synchronized(DEFAULT_PAGE_SIZE) {
        setReading();
        if(pages.isEmpty() || index >= pages.size()) 
          return -1;
        ByteBuffer buf = pages.get(index);
        if(buf.remaining() < 1) { 
          index++;
          return read(bs, off, len);
        } 
        
        int nlen = Math.min(buf.remaining(), len);
        buf.get(bs, off, nlen);
        off += nlen;
        len -= nlen;
        if(len > 0) {
          int nextRead = read(bs, off, len);
          nlen += (nextRead < 0 ? 0 : nextRead);
        }
        return nlen;
        }
      }
      @Override
      public int read(byte[] bs) throws IOException {
        return read(bs, 0, bs.length);
      }
    };
  }
  
}
