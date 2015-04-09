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

import java.io.InputStream;
import java.util.ArrayList;

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
  
  
  public MemBuffer() {
    this(DEF_SIZE);
    idx = 0;
  }
  
  
  public MemBuffer(int size) {
    if(size <= 0) size = DEF_SIZE;
    buffer = new ArrayList<>(size);
  }
  
  
  public MemBuffer write(int b) {
    buffer.add((byte) b);
    return this;
  }
  
  
  public MemBuffer write(byte[] bs, int off, int len) {
    if(bs != null && bs.length > 0
        && off >= 0 && len <= (bs.length - off)) {
      for(int i = off; i < len; i++) {
        buffer.add(bs[i]);
      }
    }
    return this;
  }
  
  
  public MemBuffer write(byte[] bs) {
    return this.write(bs, 0, bs.length);
  }
  
  
  public MemBuffer reset() {
    idx = -1;
    mark = -1;
    return this;
  }
  
  
  public MemBuffer remark() {
    mark = -1;
    return this;
  }
  
  
  public MemBuffer clear() {
    buffer.clear();
    return this.reset();
  }
  
  
  public MemBuffer mark() {
    mark = idx;
    return this;
  }
  
  
  public MemBuffer index(int x) {
    if(x >= 0 && x < buffer.size()) {
      mark = x;
    }
    return this;
  }
  
  
  public int read() {
    if(buffer.size() < 1 || idx >= buffer.size()) 
      return -1;
    return buffer.get(idx++);
  }
  
  
  public int read(byte[] bs, int off, int len) {
    if(bs == null || bs.length < 1
        || off < 0 && len > (bs.length - off)) {
      return -1;
    }
    if(len > (buffer.size() - idx))
      len = buffer.size() - idx;
    for(int i = idx; i < len; i++) {
      bs[off++] = buffer.get(i);
    }
    return len;
  } 
  
  
  public int read(byte[] bs) {
    return read(bs, 0, bs.length);
  }
  
  
  public int size() {
    return buffer.size();
  }
  
  
  public InputStream getInputStream() {
    return new InputStream() {
      public int read() {
        return MemBuffer.this.read();
      }
      public int available() {
        return buffer.size() - idx;
      }
      public boolean mark() {
        MemBuffer.this.mark();
        return true;
      }
      public int read(byte[] bs, int off, int len) {
        return MemBuffer.this.read(bs, off, len);
      }
      public int read(byte[] bs) {
        return MemBuffer.this.read(bs);
      }
    };
  }
  
}
