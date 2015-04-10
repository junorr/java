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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 08/04/2015
 */
public class MemBuffer {
  
  public static final int DEF_SIZE = 256 * 1024;
  

  private ArrayList<Byte> buffer;
  
  private int idx;
  
  private int mark;
  
  private int limit;
  
  
  public MemBuffer() {
    this(DEF_SIZE);
    idx = 0;
    mark = 0;
    limit = -1;
  }
  
  
  public MemBuffer(int size) {
    if(size <= 0) size = DEF_SIZE;
    buffer = new ArrayList<>(size);
  }
  
  
  public MemBuffer write(int b) {
    this.checkLimit();
    buffer.add((byte) b);
    return this;
  }
  
  
  public MemBuffer write(byte[] bs, int off, int len) {
    this.checkLimit();
    if(bs == null || bs.length < 1
        || off < 0 || len > bs.length - off) {
      throw new IllegalArgumentException(
          "[MemBuffer.write( [B, int, int )] "
              + "Invalid byte array parameters: '"
              + bs+ "("+ bs.length+ "), "+ off
              + ", "+ len+ "'");
    }
    for(int i = off; i < len; i++) {
      buffer.add(bs[i]);
    }
    return this;
  }
  
  
  public MemBuffer write(byte[] bs) {
    this.checkLimit();
    if(bs == null || bs.length < 1) {
      throw new IllegalArgumentException(
          "[MemBuffer.write( [B )] "
              + "Invalid byte array: '"+ bs
              + (bs != null ? "("+bs.length+")" : "") + "'");
    }
    return this.write(bs, 0, bs.length);
  }
  
  
  public MemBuffer setWriteLimit(int limit) {
    this.limit = limit;
    return this;
  }
  
  
  private void checkLimit() {
    if(limit > 0 && buffer.size() >= limit) {
      throw new IndexOutOfBoundsException(
          "[MemBuffer.write] Write Limit Exceeded: size="+ buffer.size()+ ", limit="+ limit);
    }
  }
  
  
  public boolean isLimitExceeded() {
    return buffer.size() >= limit;
  }
  
  
  public MemBuffer reset() {
    idx = mark;
    return this;
  }
  
  
  public MemBuffer clearMark() {
    mark = 0;
    return this;
  }
  
  
  public MemBuffer clear() {
    buffer.clear();
    limit = -1;
    return this.reset();
  }
  
  
  public int mark() {
    mark = idx;
    return mark;
  }
  
  
  public MemBuffer index(int x) {
    if(x >= 0 && x < buffer.size()) {
      mark = x;
    }
    return this;
  }
  
  
  public int index() {
    return idx;
  }
  
  
  public int read() {
    if(buffer.size() < 1 || idx >= buffer.size()) 
      return -1;
    return buffer.get(idx++);
  }
  
  
  public int read(byte[] bs, int off, int len) {
    if(bs == null || bs.length < 1
        || off < 0 || len > bs.length - off) {
      throw new IllegalArgumentException(
          "[MemBuffer.read( [B, int, int )] "
              + "Invalid byte array parameters: '"
              + bs+ "("+ bs.length+ "), "+ off
              + ", "+ len+ "'");
    }
    if(len > (buffer.size() - idx))
      len = buffer.size() - idx;
    for(int i = idx; i < idx+len; i++) {
      bs[off++] = buffer.get(i);
    }
    idx += len;
    return len;
  } 
  
  
  public int read(byte[] bs) {
    if(bs == null || bs.length < 1) {
      throw new IllegalArgumentException(
          "[MemBuffer.read( [B )] "
              + "Invalid byte array: '"+ bs
              + (bs != null ? "("+bs.length+")" : "") + "'");
    }
    return read(bs, 0, bs.length);
  }
  
  
  public int size() {
    return buffer.size();
  }
  
  
  public InputStream getInputStream() {
    return new MemBufferInputStream(this);
  }
  
  
  public OutputStream getOutputStream() {
    return new MemBufferOutputStream(this);
  }
  
  
  public static void main(String[] args) {
    MemBuffer buf = new MemBuffer(10).setWriteLimit(10);
    System.out.println("* buf.write( 1..10 )");
    for(int i = 0; i < 10; i++) {
      buf.write(i);
    }
    byte[] bs = new byte[4];
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
    System.out.println("* buf.write( 10 )");
    buf.write(10);
  }
  
}
