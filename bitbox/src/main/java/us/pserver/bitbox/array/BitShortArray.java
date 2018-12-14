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

package us.pserver.bitbox.array;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import us.pserver.bitbox.AbstractBitBox;
import us.pserver.bitbox.BitBox;
import us.pserver.bitbox.util.ShortIterator;
import us.pserver.tools.Hash;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public interface BitShortArray extends BitBox {
  
  public static final int ID = BitShortArray.class.getName().hashCode();

  public int length();
  
  public short get(int idx);
  
  public boolean isEmpty();
  
  public int indexOf(short val);
  
  public boolean contains(short val);
  
  public short[] toArray();
  
  public IntStream stream();
  
  public IntStream parallelStream();
  
  public ShortIterator iterator();
  
  public PrimitiveIterator.OfInt intIterator();
  
  
  
  public static BitShortArrayFactory factory() {
    return BitShortArrayFactory.get();
  }
  
  
  
  
  
  static class ShortArray extends AbstractBitBox implements BitShortArray {
    
    private final int length;
    
    public ShortArray(ByteBuffer buf) {
      super(buf);
      if(buffer.getInt() != ID) {
        throw new IllegalArgumentException("Not a BitShortArray content");
      }
      buffer.getInt();//bitbox size
      this.length = buf.getInt();//array length
    }
    
    
    @Override
    public int length() {
      return length;
    }
    
    
    @Override
    public short get(int idx) {
      if(idx < 0 || idx >= length) {
        throw new IndexOutOfBoundsException(String.format("Bad index: 0 >= [%d] < %d", idx, length));
      }
      buffer.position(Integer.BYTES * 3 + Short.BYTES * idx);
      return buffer.getShort();
    }
    
    
    @Override
    public boolean isEmpty() {
      return length == 0;
    }
    
    
    @Override
    public int indexOf(short val) {
      int idx = 0;
      ShortIterator it = iterator();
      while(it.hasNext()) {
        if(it.nextShort() == val) return idx;
        idx++;
      }
      return -1;
    }
    
    
    @Override
    public boolean contains(short val) {
      return indexOf(val) >= 0;
    }
    
    
    @Override
    public short[] toArray() {
      short[] bs = new short[length];
      int i = 0;
      ShortIterator it = iterator();
      while(it.hasNext()) {
        bs[i++] = it.nextShort();
      }
      return bs;
    }
    
    
    @Override
    public IntStream stream() {
      return StreamSupport.intStream(Spliterators.spliterator(intIterator(), length, Spliterator.NONNULL), false);
    }
    
    
    @Override
    public IntStream parallelStream() {
      return StreamSupport.intStream(Spliterators.spliterator(intIterator(), length, Spliterator.NONNULL), true);
    }
    
    
    @Override
    public ShortIterator iterator() {
      return new ShortIterator() {
        private int index = 0;
        public boolean hasNext() {
          return index < length;
        }
        public Short next() {
          return get(index++);
        }
        public short nextShort() {
          return get(index++);
        }
      };
    }
    
    
    @Override
    public PrimitiveIterator.OfInt intIterator() {
      return new PrimitiveIterator.OfInt() {
        private int index = 0;
        public boolean hasNext() {
          return index < length;
        }
        public Integer next() {
          return Short.valueOf(get(index++)).intValue();
        }
        public int nextInt() {
          return get(index++);
        }
      };
    }
    
    
    @Override
    public String sha256sum() {
      return Hash.sha256().of(toByteArray());
    }
    
    
    @Override
    public ByteBuffer toByteBuffer() {
      buffer.position(0);
      return buffer.duplicate();
    }
    
    
    @Override
    public byte[] toByteArray() {
      if(buffer.hasArray() && buffer.array().length == boxSize()) {
        return buffer.array();
      }
      byte[] bs = new byte[boxSize()];
      buffer.position(0);
      buffer.get(bs);
      return bs;
    }
    
    
    @Override
    public int writeTo(ByteBuffer buf) {
      buffer.position(0);
      buf.put(buffer);
      return buffer.limit();
    }
    
    
    @Override
    public int writeTo(WritableByteChannel ch) throws IOException {
      buffer.position(0);
      ch.write(buffer);
      return buffer.limit();
    }
    
    
    @Override
    public int writeTo(DynamicByteBuffer buf) {
      buffer.position(0);
      buf.put(buffer);
      return buffer.limit();
    }
    
  }
  
}
