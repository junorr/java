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
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/04/2015
 */
public class MixedBuffer {

  private File temp;
  
  private MemBuffer buffer;
  
  private RandomAccessFile raf;
  
  private long idx, rmark, wmark, rlimit;
  
  private boolean valid, rmode;
  
  private StreamCoderFactory coders;
  
  
  public MixedBuffer(int memsize) {
    if(memsize < 1)
      memsize = MemBuffer.DEF_SIZE;
    buffer = new MemBuffer(memsize)
        .setWriteLimit(memsize);
    idx = 0;
    rmark = 0;
    rlimit = 0;
    wmark = -1;
    valid = true;
    rmode = false;
  }
  
  
  public MixedBuffer() {
    this(0);
  }
  
  
  private void initTemp() throws IOException {
    if(temp == null)
      temp = File.createTempFile("mixbuffer", ".temp");
    if(raf == null) {
      raf = new RandomAccessFile(temp, "rw");
      idx = 0;
    }
  }
  
  
  public StreamCoderFactory getCoderFactory() {
    if(coders == null) {
      coders = StreamCoderFactory.getNew();
    }
    return coders;
  }
  
  
  public MixedBuffer write(int b) throws IOException {
    this.checkValid();
    this.checkLimit();
    try {
      buffer.write(b);
    } catch(IndexOutOfBoundsException e) {
      initTemp();
      if(rmode) {
        raf.seek(wmark);
        rmode = false;
      }
      raf.write(b);
    }
    return this;
  }
  
  
  public MixedBuffer write(byte[] bs, int off, int len) throws IOException {
    this.checkValid();
    this.checkLimit();
    if(bs == null
        || off < 0 || len > bs.length - off) {
      throw new IOException(
          "[MixedBuffer.write( [B, int, int )] "
              + "Invalid byte array parameters: '"
              + bs+ "("+ bs.length+ "), "+ off
              + ", "+ len+ "'");
    }
    if(bs.length == 0 || len == 0 || len > bs.length) return this;
    int writed = 0;
    try {
      for(int i = off; i < len; i++) {
        buffer.write(bs[i]);
        writed = i;
      }
      writed++;
    } catch(IndexOutOfBoundsException e) {
      initTemp();
      if(rmode) {
        raf.seek(wmark);
        rmode = false;
      }
      if(len > bs.length - writed)
        len = bs.length - writed;
      raf.write(bs, writed, len);
      wmark = raf.length();
    }
    return this;
  }

  
  public MixedBuffer write(byte[] bs) throws IOException {
    this.checkValid();
    this.checkLimit();
    if(bs == null) {
      throw new IOException(
          "[MixedBuffer.write( [B )] Invalid byte array: '"+ bs+ "'");
    }
    return this.write(bs, 0, bs.length);
  }
  
  
  public int read() throws IOException {
    this.checkValid();
    int b = -1;
    if(buffer.size() < 1 && temp == null)
      return b;
    if(idx < buffer.size()) {
      b = buffer.read();
      idx++;
    }
    else if(temp != null && (idx - buffer.size()) < raf.length()) {
      if(!rmode) {
        wmark = raf.length();
        rmode = true;
      }
      raf.seek(idx - buffer.size());
      b = raf.read();
      idx++;
    } 
    return b;
  }
  
  
  public int read(byte[] bs, int off, int len) throws IOException {
    //long init = System.currentTimeMillis();
    this.checkValid();
    if(bs == null || bs.length < 1
        || off < 0 || len > bs.length - off) {
      throw new IOException(
          "[MixedBuffer.read( [B, int, int )] "
              + "Invalid byte array parameters: '"
              + bs+ "("+ bs.length+ "), "+ off
              + ", "+ len+ "'");
    }
    int blen = buffer.size() - buffer.index();
    int max = 0;
    if(blen > len) blen = len;
    for(int i = 0; i < blen; i++) {
      bs[off++] = (byte) read();
      max++;
    }
    if(temp != null) {
      int rlen = (int) (raf.length() + buffer.size() - idx);
      if(rlen > len-max) rlen = len-max;
      for(int i = 0; i < rlen; i++) {
        bs[off++] = (byte) read();
        max++;
      }
    }
    //System.out.println("[read( [B, int, int )]: time = "+ (System.currentTimeMillis() - init));
    return max;
  }
  
  
  public int read(byte[] bs) throws IOException {
    this.checkValid();
    if(bs == null) {
      throw new IOException(
          "[MixedBuffer.read( [B )] "+ "Invalid byte array: '"+ bs+ "'");
    }
    return read(bs, 0, bs.length);
  }
  
  
  public long size() throws IOException {
    this.checkValid();
    return buffer.size() + (temp != null ? raf.length() : 0);
  }
  
  
  public MixedBuffer mark() {
    this.checkValid();
    rmark = idx;
    if(buffer.index() < buffer.size())
      buffer.mark();
    return this;
  }
  
  
  public MixedBuffer clearMark() {
    this.checkValid();
    buffer.clearMark();
    rmark = 0;
    return this;
  }
  
  
  public MixedBuffer reset() {
    this.checkValid();
    buffer.reset();
    idx = rmark;
    return this;
  }
  
  
  public long index() {
    this.checkValid();
    return idx;
  }
  
  
  public MixedBuffer index(long i) {
    this.checkValid();
    idx = (i < 0 ? 0 : i);
    return this;
  }
  
  
  public MixedBuffer setWriteLimit(long limit) {
    this.checkValid();
    rlimit = limit;
    return this;
  }
  
  
  private void checkLimit() throws IOException {
    if(rlimit > 0 && this.size() >= rlimit) {
      throw new IOException(
          "[MixedBuffer.write] Write Limit Exceeded: size="+ size()+ ", limit="+ rlimit);
    }
  }
  
  
  private void checkValid() {
    if(!valid) {
      throw new IllegalStateException(
          "[MixedBuffer] This MixedBuffer is Closed");
    }
  }
  
  
  public boolean isLimitExceeded() throws IOException {
    this.checkValid();
    return size() >= rlimit;
  }
  
  
  public InputStream getInputStream() {
    InputStream is = new MixedBufferInputStream(this);
    if(coders != null && coders.isAnyCoderEnabled()) {
      try { is = coders.create(is); }
      catch(IOException e) {
        throw new RuntimeException(e);
      }
    }
    return is;
  }
  
  
  public OutputStream getOutputStream() {
    OutputStream os = new MixedBufferOutputStream(this);
    if(coders != null && coders.isAnyCoderEnabled()) {
      try { os = coders.create(os); }
      catch(IOException e) {
        throw new RuntimeException(e);
      }
    }
    return os;
  }
  
  
  public void close() {
    this.checkValid();
    valid = false;
    buffer.clear();
    try {
      raf.close();
      temp.deleteOnExit();
      temp.delete();
    } catch(Exception e) {}
  }
  
  
  public static void main(String[] args) throws IOException {
    MixedBuffer buf = new MixedBuffer(10).setWriteLimit(30);
    System.out.println("* buf.write( 1..20 )");
    for(int i = 0; i < 20; i++) {
      buf.write(i);
    }
    byte[] bs = new byte[6];
    System.out.println("* buf.size(): "+ buf.size());
    System.out.println("* buf.read( [B ): "+ buf.read(bs)+ " - "+ Arrays.toString(bs));
    System.out.println("* buf.index(): "+ buf.index());
    buf.mark();
    System.out.println("* buf.mark()");
    System.out.println("* buf.read( [B ): "+ buf.read(bs)+ " - "+ Arrays.toString(bs));
    System.out.println("* buf.index(): "+ buf.index());
    buf.reset();
    System.out.println("* buf.reset()");
    System.out.println("* buf.index(): "+ buf.index());
    System.out.println("* buf.read( [B ): "+ buf.read(bs)+ " - "+ Arrays.toString(bs));
    System.out.println("* buf.read( [B ): "+ buf.read(bs)+ " - "+ Arrays.toString(bs));
    System.out.println("* buf.read( [B ): "+ buf.read(bs)+ " - "+ Arrays.toString(bs));
    //System.out.println("* buf.write( 20 )");
    //buf.write(20);
    System.out.println("* buf.write( 20..30 )");
    for(int i = 20; i < 30; i++) {
      buf.write(i);
    }
    System.out.println("* buf.read( [B ): "+ buf.read(bs)+ " - "+ Arrays.toString(bs));
    System.out.println("* buf.read( [B ): "+ buf.read(bs)+ " - "+ Arrays.toString(bs));
    System.out.println("* buf.read( [B ): "+ buf.read(bs)+ " - "+ Arrays.toString(bs));
    buf.close();
  }
  
}
