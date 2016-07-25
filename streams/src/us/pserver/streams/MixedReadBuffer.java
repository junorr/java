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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 10/04/2015
 */
public class MixedReadBuffer extends AbstractMixedBuffer {
  
  private long mark, idx;
  
  private MixedWriteBuffer writer;
  
  
  protected MixedReadBuffer(ByteBuffer buf, File tmp, MixedWriteBuffer writer) throws IOException {
    super();
    if(buf == null)
      throw new IllegalArgumentException("Invalid ByteBuffer: "+ buf);
    this.buffer = buf;
    temp = tmp;
    raf = null;
    mark = 0;
    idx = 0;
    valid = true;
    this.writer = writer;
    if(temp != null) {
      raf = new RandomAccessFile(temp, "r");
    }
  }
  
  
  private void validate() {
    if(!valid) {
      throw new IllegalStateException("This buffer becomes invalid. It is closed or being writed?");
    }
  }
  
  
  public int read() throws IOException {
    validate();
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
    validate();
    if(bs == null || off < 0 || len > bs.length - off)
      throw new IllegalArgumentException("Invalid Arguments {bs="
              + bs+ ", bs.length="+ (bs != null ? bs.length : 0)+ ", off="+ off+ ", len="+ len+ "}");
    
    int read = -1;
    int ilen = len;
    
    if(buffer.hasRemaining()) {
      ilen = Math.min(ilen, buffer.remaining());
      buffer.get(bs, off, ilen);
      read = ilen;
      off += ilen;
      idx += ilen;
      ilen = len - ilen;
    }
    if(raf != null && ilen > 0) {
      ilen = (int) Math.min(ilen, raf.length());
      int rread = raf.read(bs, off, ilen);
      if(rread > 0) {
        read = (read <= 0 ? rread : read + rread);
        idx += rread;
      }
    }
    return read;
  }
  
  
  public int read(byte[] bs) throws IOException {
    validate();
    if(bs == null)
      throw new IllegalArgumentException("Invalid byte array {bs="+ bs+ "}");
    return this.read(bs, 0, bs.length);
  }
  
  
  public MixedReadBuffer flush(Path p) throws IOException {
    validate();
    if(p == null)
      throw new IllegalArgumentException("Invalid null path {p="+ p+ "}");
    if(!Files.exists(p)) {
      if(p.getParent() != null && !Files.exists(p.getParent())) {
        Files.createDirectories(p.getParent());
      }
      Files.createFile(p);
    }
    IO.tc(getInputStream(), IO.os(p));
    return this;
  }
  
  
  public MixedReadBuffer flush(OutputStream out) throws IOException {
    validate();
    if(out == null)
      throw new IllegalArgumentException("Invalid null stream {out="+ out+ "}");
    IO.tc(getInputStream(), out);
    return this;
  }
  
  
  public MixedReadBuffer rewind() throws IOException {
    validate();
    buffer.rewind();
    idx = 0;
    mark = 0;
    if(raf != null) {
      raf.seek(0);
    }
    return this;
  }
  
  
  public MixedReadBuffer mark() throws IOException {
    validate();
    mark = idx;
    if(buffer.hasRemaining())
      buffer.mark();
    return this;
  }
  
  
  public MixedReadBuffer reset() throws IOException {
    validate();
    idx = mark;
    if(idx < buffer.limit()) {
      buffer.reset();
    }
    else if(raf != null) {
      raf.seek(idx - buffer.limit());
    }
    return this;
  }
  
  
  public long index() {
    validate();
    return idx;
  }
  
  
  public InputStream getRawInputStream() {
    return new BufferedInputStream(new MixedBufferInputStream(this));
  }
  
  
  public InputStream getInputStream() throws IOException {
    validate();
    InputStream in = new BufferedInputStream(new MixedBufferInputStream(this));
    if(coderfac.isAnyCoderEnabled()) {
      in = coderfac.create(in);
    }
    return in;
  }

  
  public MixedWriteBuffer getWriteBuffer() throws IOException {
    validate();
    valid = false;
    buffer.flip();
    if(raf != null) {
      raf.close();
    }
    writer.valid = true;
    return writer;
  }
  
}
