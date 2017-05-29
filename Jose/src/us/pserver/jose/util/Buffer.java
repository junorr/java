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

package us.pserver.jose.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/03/2017
 */
public interface Buffer {

  public Buffer write(byte b);
  
  public Buffer write(byte[] bs);
  
  public Buffer write(byte[] bs, int off, int len);
  
  public Buffer write(ByteBuffer buf);
  
  public Buffer writeUTF8(String str);
  
  public InputStream getInputStream();
  
  public int size();
  
  public byte[] toBytes();
  
  public ByteBuffer toByteBuffer();
  
  
  
  public static Buffer create() {
    return new BufferImpl();
  }
  
  public static Buffer create(int initSize) {
    return new BufferImpl(initSize);
  }
  
  
  
  
  
  static final class BufferImpl implements Buffer {
    
    public static final int INIT_SIZE = 1024;
    
    
    private Object[] array;
    
    private byte[] buffer;
    
    private int index;
    
    private int position;
    
    
    private BufferImpl(int initsize) {
      int size = (initsize < 2 ? INIT_SIZE : initsize);
      array = new Object[size/2];
      buffer = new byte[size];
      array[0] = buffer;
      index = 0;
      position = 1;
    }
    
    
    private BufferImpl() {
      this(INIT_SIZE);
    }
    
    
    private void grow() {
      Object[] os = new Object[array.length*2];
      System.arraycopy(array, 0, os, 0, array.length);
      position = array.length;
      array = os;
    }
    
    
    private byte[] addBuffer() {
      if(position >= array.length) {
        grow();
      }
      buffer = new byte[buffer.length];
      array[position++] = buffer;
      index = 0;
      return buffer;
    }


    @Override
    public Buffer write(byte[] bs, int off, int len) {
      if(bs == null || bs.length < 1) {
        throw new IllegalArgumentException("Bad Byte Array");
      }
      if(off >= 0 && len > 0 && (off+len) <= bs.length) {
        if(index >= buffer.length) {
          addBuffer();
        }
        int length = Math.min((buffer.length-index), len);
        System.arraycopy(bs, off, buffer, index, length);
        index += length;
        write(bs, off+length, len-length);
      }
      return this;
    }


    @Override
    public Buffer write(byte[] bs) {
      return this.write(bs, 0, (bs != null ? bs.length : 0));
    }


    @Override
    public Buffer writeUTF8(String str) {
      return this.write(UTF8String.from(str).getBytes());
    }


    @Override
    public Buffer write(byte b) {
      if(index >= buffer.length) {
        addBuffer();
      }
      buffer[index++] = b;
      return this;
    }


    @Override
    public int size() {
      return position * buffer.length - buffer.length + index;
    }


    @Override
    public byte[] toBytes() {
      int size = size();
      if(size < 1) return new byte[0];
      byte[] bs = new byte[size];
      byte[] cur;
      int count = 0;
      int ari = -1;
      while(count < bs.length && ++ari < position) {
        cur = (byte[]) array[ari];
        int len = (ari == position-1 ? index : cur.length);
        System.arraycopy(cur, 0, bs, count, len);
        count += len;
      }
      return bs;
    }


    @Override
    public ByteBuffer toByteBuffer() {
      if(size() < 1) return null;
      return ByteBuffer.wrap(toBytes());
    }


    @Override
    public Buffer write(ByteBuffer buf) {
      if(buf != null && buf.hasRemaining()) {
        if(buf.hasArray()) {
          write(buf.array(), buf.position(), buf.remaining());
        }
        else while(buf.hasRemaining()) {
          if(index >= buffer.length) {
            addBuffer();
          }
          int len = Math.min(buffer.length-index, buf.remaining());
          buf.get(buffer, index, len);
        }
      }
      return this;
    }


    @Override
    public InputStream getInputStream() {
      return new InputStream() {
        private int spos = 0;
        private int sidx = 0;
        @Override
        public int read() throws IOException {
          if(sidx >= buffer.length) {
            sidx = 0;
            spos++;
          }
          if(spos >= array.length || spos*buffer.length-buffer.length+sidx >= size()) {
            return -1;
          }
          return ((byte[])array[spos])[sidx++];
        }
      };
    }
    
  }
  
}
