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

package us.pserver.bitbox;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import us.pserver.bitbox.util.Region;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public interface BitArray<T extends BitBox> extends BitBox<T[]> {
  
  public static final int HEADER_BYTES = BitBox.HEADER_BYTES + Integer.BYTES;
  
  public static final int ID = BitArray.class.getName().hashCode();
  
  
  public int length();
  
  public T get(int idx);
  
  public boolean isEmpty();
  
  public int indexOf(T bin);
  
  public boolean contains(T bin);
  
  public Stream<T> stream(boolean parallel);
  
  public Iterator<T> iterator();
  
  
  
  
  
  static class BArray<T extends BitBox> extends AbstractBitBox<T[]> implements BitArray<T> {
    
    private final int length;
    
    public BArray(ByteBuffer buf) {
      super(buf);
      if(buffer.getInt() != ID) {
        throw new IllegalArgumentException("Not a BitArray content");
      }
      buffer.getInt();//bitbox size
      this.length = buf.getInt();//array length
    }
    
    @Override
    public int length() {
      return length;
    }
    
    protected Region getRegion(int idx) {
      int i = 0;
      Region reg = Region.of(BitArray.HEADER_BYTES, 0);
      while(i++ <= idx) {
        buffer.position(reg.end() + Integer.BYTES);
        reg = Region.of(reg.offset(), buffer.getInt());
      }
      return reg;
    }
    
    @Override
    public T[] get() {
      Class<T> cls = (Class<T>) get(0).getClass();
      T[] ts = (T[]) Array.newInstance(cls, length);
      int i = 0;
      Iterator<T> it = iterator();
      while(it.hasNext()) {
        ts[i++] = it.next();
      }
      return ts;
    }
    
    @Override
    public T get(int idx) {
      if(idx < 0 || idx >= length) {
        throw new IndexOutOfBoundsException(String.format("Bad index: 0 >= [%d] < %d", idx, length));
      }
      Region reg = getRegion(idx);
      int lim = buffer.limit();
      buffer.position(reg.offset()).limit(reg.end());
      T bin = (T) BitBoxFactory.get().createFrom(buffer.slice());
      buffer.limit(lim);
      return bin;
    }
    
    @Override
    public boolean isEmpty() {
      return length == 0;
    }
    
    @Override
    public int indexOf(T bin) {
      int idx = 0;
      Iterator<T> it = iterator();
      while(it.hasNext()) {
        if(bin.equals(it.next())) return idx;
        idx++;
      }
      return -1;
    }
    
    @Override
    public boolean contains(T bin) {
      return indexOf(bin) >= 0;
    }
    
    @Override
    public Stream<T> stream(boolean parallel) {
      Spliterator<T> spi = Spliterators.spliterator(iterator(), length, Spliterator.NONNULL);
      return StreamSupport.stream(spi, parallel);
    }
    
    @Override
    public Iterator<T> iterator() {
      return new Iterator<T>() {
        private int index = 0;
        public boolean hasNext() {
          return index < length;
        }
        public T next() {
          return BArray.this.get(index++);
        }
      };
    }
    
  }
  
}
