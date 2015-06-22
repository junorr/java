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

import java.io.BufferedOutputStream;
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
public class MixedWriteBuffer extends AbstractMixedBuffer {
  
  
  protected MixedWriteBuffer(ByteBuffer buf, File tmp) throws IOException {
    super();
    if(buf == null)
      throw new IllegalArgumentException("Invalid ByteBuffer: "+ buf);
    this.buffer = buf;
    temp = tmp;
    raf = null;
    valid = true;
    if(temp != null) {
      raf = new RandomAccessFile(temp, "rw");
    }
  }
  
  
  public MixedWriteBuffer(int memsize) {
    super();
    if(memsize < 1) memsize = DEFAULT_MEM_SIZE;
    buffer = ByteBuffer.allocateDirect(memsize);
    raf = null;
    valid = true;
  }
  
  
  public MixedWriteBuffer() {
    this(0);
  }
  
  
  private void initTemp() throws IOException {
    if(temp == null) {
      temp = File.createTempFile("mixed_buffer_", ".tmp");
      temp.deleteOnExit();
      raf = new RandomAccessFile(temp, "rw");
    }
  }
  
  
  private void validate() {
    if(!valid) {
      throw new IllegalStateException("This buffer becomes invalid. It is closed or being readed?");
    }
  }
  
  
  public void write(int b) throws IOException {
    validate();
    if(buffer.hasRemaining()) {
      buffer.put((byte) b);
    }
    else {
      initTemp();
      raf.write(b);
    }
  }
  
  
  public void write(byte[] bs, int off, int len) throws IOException {
    validate();
    if(bs == null || off < 0 || len > bs.length - off)
      throw new IllegalArgumentException("Invalid Arguments {bs="
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
    validate();
    if(bs == null)
      throw new IllegalArgumentException("Invalid byte array {bs="+ bs+ "}");
    this.write(bs, 0, bs.length);
  }
  
  
  public MixedWriteBuffer clear() {
    valid = true;
    close();
    valid = true;
    return this;
  }
  
  
  public MixedWriteBuffer load(Path p) throws IOException {
    validate();
    if(p == null)
      throw new IllegalArgumentException("Invalid null path {p="+ p+ "}");
    if(!Files.exists(p))
      throw new IllegalArgumentException("Path does not exist {p="+ p+ "}");
    IO.tc(IO.is(p), getOutputStream());
    return this;
  }
  
  
  public MixedWriteBuffer load(InputStream in) throws IOException {
    validate();
    if(in == null)
      throw new IllegalArgumentException("Invalid null stream {in="+ in+ "}");
    IO.tc(in, getOutputStream());
    return this;
  }
  
  
  public OutputStream getRawOutputStream() {
    return new BufferedOutputStream(new MixedBufferOutputStream(this));
  }
  
  
  public OutputStream getOutputStream() throws IOException {
    validate();
    OutputStream out = new BufferedOutputStream(new MixedBufferOutputStream(this));
    if(coderfac.isAnyCoderEnabled()) {
      out = coderfac.create(out);
    }
    return out;
  }
  
  
  public MixedReadBuffer getReadBuffer() throws IOException {
    validate();
    valid = false;
    buffer.flip();
    MixedReadBuffer rbuf = new MixedReadBuffer(buffer, temp, this);
    rbuf.setCoderFactory(getCoderFactory());
    return rbuf;
  }
  
}
