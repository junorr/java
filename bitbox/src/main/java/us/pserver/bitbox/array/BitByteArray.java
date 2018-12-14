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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import us.pserver.bitbox.AbstractBitBox;
import us.pserver.bitbox.BitArray;
import us.pserver.bitbox.util.ByteIterator;
import us.pserver.tools.Hash;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public interface BitByteArray extends BitArray<Byte> {
  
  public static final int ID = BitByteArray.class.getName().hashCode();

  public byte getByte(int idx);
  
  public int indexOfByte(byte val);
  
  public boolean containsByte(byte val);
  
  public IntStream intStream();
  
  public IntStream intParallelStream();
  
  @Override
  public ByteIterator iterator();
  
  public PrimitiveIterator.OfInt intIterator();
  
  
  
  public static BitByteArrayFactory factory() {
    return BitByteArrayFactory.get();
  }
  
  
  
  
  
  static class ByteArray extends AbstractBitBox implements BitByteArray {
    
    private final int length;
    
    public ByteArray(ByteBuffer buf) {
      super(buf);
      if(buffer.getInt() != ID) {
        throw new IllegalArgumentException("Not a BitByteArray content");
      }
      buffer.getInt();//bitbox size
      this.length = buf.getInt();//array length
    }
    
    
    @Override
    public int length() {
      return length;
    }
    
    
    @Override
    public Byte get(int idx) {
      return getByte(idx);
    }
    
    
    @Override
    public byte getByte(int idx) {
      if(idx < 0 || idx >= length) {
        throw new IndexOutOfBoundsException(String.format("Bad index: 0 >= [%d] < %d", idx, length));
      }
      buffer.position(Integer.BYTES * 3 + Byte.BYTES * idx);
      return buffer.get();
    }
    
    
    @Override
    public boolean isEmpty() {
      return length == 0;
    }
    
    
    @Override
    public int indexOf(Byte val) {
      return indexOfByte(val);
    }
    
    
    @Override
    public int indexOfByte(byte val) {
      int idx = 0;
      ByteIterator it = iterator();
      while(it.hasNext()) {
        if(it.nextByte() == val) return idx;
        idx++;
      }
      return -1;
    }
    
    
    @Override
    public boolean contains(Byte val) {
      return containsByte(val);
    }
    
    
    @Override
    public boolean containsByte(byte val) {
      return indexOf(val) >= 0;
    }
    
    
    @Override
    public byte[] toArray() {
      byte[] bs = new byte[length];
      int i = 0;
      ByteIterator it = iterator();
      while(it.hasNext()) {
        bs[i++] = it.nextByte();
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
    public ByteIterator iterator() {
      return new ByteIterator() {
        private int index = 0;
        public boolean hasNext() {
          return index < length;
        }
        public Byte next() {
          return get(index++);
        }
        public byte nextByte() {
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
          return (int) get(index++);
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
