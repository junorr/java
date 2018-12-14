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

import java.nio.ByteBuffer;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public interface BitPrimitiveArray extends BitArray<BitPrimitive> {
  
  public int indexOf(boolean val);
  
  public boolean contains(boolean val);
  
  public int indexOf(char val);
  
  public boolean contains(char val);
  
  public int indexOf(short val);
  
  public boolean contains(short val);
  
  public int indexOf(int val);
  
  public boolean contains(int val);
  
  public int indexOf(float val);
  
  public boolean contains(float val);
  
  public int indexOf(long val);
  
  public boolean contains(long val);
  
  public int indexOf(double val);
  
  public boolean contains(double val);
  
  
  public char[] toCharArray();
  
  public boolean[] toBooleanArray();
  
  public short[] toShortArray();
  
  public int[] toIntArray();
  
  public float[] toFloatArray();
  
  public long[] toLongArray();
  
  public double[] toDoubleArray();
  
  
  public IntStream intStream(boolean parallel);
  
  public LongStream longStream(boolean parallel);
  
  public DoubleStream doubleStream(boolean parallel);
  
  public PrimitiveIterator.OfInt intIterator();
  
  public PrimitiveIterator.OfLong longIterator();
  
  public PrimitiveIterator.OfDouble doubleIterator();
  
  
  public default boolean isBooleanArray() {
    return BitPrimitive.ID_BOOLEAN == get(0).boxID();
  }
  
  public default boolean isCharArray() {
    return BitPrimitive.ID_CHAR == get(0).boxID();
  }
  
  public default boolean isShortArray() {
    return BitPrimitive.ID_SHORT == get(0).boxID();
  }
  
  public default boolean isIntArray() {
    return BitPrimitive.ID_INT == get(0).boxID();
  }
  
  public default boolean isFloatArray() {
    return BitPrimitive.ID_FLOAT == get(0).boxID();
  }
  
  public default boolean isLongArray() {
    return BitPrimitive.ID_LONG == get(0).boxID();
  }
  
  public default boolean isDoubleArray() {
    return BitPrimitive.ID_DOUBLE == get(0).boxID();
  }
  
  
  
  public static BitPrimitiveArrayFactory factory() {
    return BitPrimitiveArrayFactory.get();
  }
  
  
  
  
  
  static class PrimitiveArray extends BitArray.BArray<BitPrimitive> implements BitPrimitiveArray {
    
    public PrimitiveArray(ByteBuffer buf) {
      super(buf);
    }
    
    @Override
    public char[] toCharArray() {
      if(!isCharArray()) {
        throw new UnsupportedOperationException();
      }
      char[] array = new char[length()];
      BitPrimitive[] ts = get();
      for(int i = 0; i < ts.length; i++) {
        array[i] = ts[i].getChar();
      }
      return array;
    }
    
    @Override
    public boolean[] toBooleanArray() {
      if(!isBooleanArray()) {
        throw new UnsupportedOperationException();
      }
      boolean[] array = new boolean[length()];
      BitPrimitive[] ts = get();
      for(int i = 0; i < ts.length; i++) {
        array[i] = ts[i].getBoolean();
      }
      return array;
    }
    
    @Override
    public short[] toShortArray() {
      if(!isShortArray()) {
        throw new UnsupportedOperationException();
      }
      short[] array = new short[length()];
      BitPrimitive[] ts = get();
      for(int i = 0; i < ts.length; i++) {
        array[i] = ts[i].getShort();
      }
      return array;
    }
    
    @Override
    public int[] toIntArray() {
      if(!isIntArray()) {
        throw new UnsupportedOperationException();
      }
      int[] array = new int[length()];
      BitPrimitive[] ts = get();
      for(int i = 0; i < ts.length; i++) {
        array[i] = ts[i].getInt();
      }
      return array;
    }
    
    @Override
    public float[] toFloatArray() {
      if(!isFloatArray()) {
        throw new UnsupportedOperationException();
      }
      float[] array = new float[length()];
      BitPrimitive[] ts = get();
      for(int i = 0; i < ts.length; i++) {
        array[i] = ts[i].getFloat();
      }
      return array;
    }
    
    @Override
    public long[] toLongArray() {
      if(!isLongArray()) {
        throw new UnsupportedOperationException();
      }
      long[] array = new long[length()];
      BitPrimitive[] ts = get();
      for(int i = 0; i < ts.length; i++) {
        array[i] = ts[i].getLong();
      }
      return array;
    }
    
    @Override
    public double[] toDoubleArray() {
      if(!isDoubleArray()) {
        throw new UnsupportedOperationException();
      }
      double[] array = new double[length()];
      BitPrimitive[] ts = get();
      for(int i = 0; i < ts.length; i++) {
        array[i] = ts[i].getDouble();
      }
      return array;
    }
    
    @Override
    public IntStream intStream(boolean parallel) {
      return StreamSupport.intStream(Spliterators.spliterator(intIterator(), length(), Spliterator.NONNULL), parallel);
    }
    
    @Override
    public LongStream longStream(boolean parallel) {
      return StreamSupport.longStream(Spliterators.spliterator(longIterator(), length(), Spliterator.NONNULL), parallel);
    }
    
    @Override
    public DoubleStream doubleStream(boolean parallel) {
      return StreamSupport.doubleStream(Spliterators.spliterator(doubleIterator(), length(), Spliterator.NONNULL), parallel);
    }
    
    @Override
    public PrimitiveIterator.OfInt intIterator() {
      if(!isBooleanArray() || !isCharArray() || !isShortArray() || !isIntArray()) {
        throw new UnsupportedOperationException();
      }
      if(isBooleanArray()) {
        return new PrimitiveIterator.OfInt() {
          private int index = 0;
          @Override
          public int nextInt() {
            return get(0).getBoolean() ? 1 : 0;
          }
          @Override
          public boolean hasNext() {
            return index < length();
          }
        };
      }
      else if(isCharArray()) {
        return new PrimitiveIterator.OfInt() {
          private int index = 0;
          @Override
          public int nextInt() {
            return get(0).getChar();
          }
          @Override
          public boolean hasNext() {
            return index < length();
          }
        };
      }
      else if(isShortArray()) {
        return new PrimitiveIterator.OfInt() {
          private int index = 0;
          @Override
          public int nextInt() {
            return get(0).getShort();
          }
          @Override
          public boolean hasNext() {
            return index < length();
          }
        };
      }
      else {
        return new PrimitiveIterator.OfInt() {
          private int index = 0;
          @Override
          public int nextInt() {
            return get(0).getInt();
          }
          @Override
          public boolean hasNext() {
            return index < length();
          }
        };
      }
    }
    
    @Override
    public PrimitiveIterator.OfLong longIterator() {
      if(!isLongArray()) {
        throw new UnsupportedOperationException();
      }
      return new PrimitiveIterator.OfLong() {
        private int index = 0;
        @Override
        public long nextLong() {
          return get(0).getLong();
        }
        @Override
        public boolean hasNext() {
          return index < length();
        }
      };
    }
    
    @Override
    public PrimitiveIterator.OfDouble doubleIterator() {
      if(!isDoubleArray()) {
        throw new UnsupportedOperationException();
      }
      return new PrimitiveIterator.OfDouble() {
        private int index = 0;
        @Override
        public double nextDouble() {
          return get(0).getDouble();
        }
        @Override
        public boolean hasNext() {
          return index < length();
        }
      };
    }
    
    @Override
    public int indexOf(boolean val) {
      PrimitiveIterator.OfInt it = intIterator();
      int idx = 0;
      while(it.hasNext()) {
        if(it.nextInt() == (val ? 1 : 0)) return idx;
        idx++;
      }
      return -1;
    }
    
    @Override
    public boolean contains(boolean val) {
      return indexOf(val) >= 0;
    }
    
    @Override
    public int indexOf(char val) {
      PrimitiveIterator.OfInt it = intIterator();
      int idx = 0;
      while(it.hasNext()) {
        if(it.nextInt() == (int)val) return idx;
        idx++;
      }
      return -1;
    }
    
    @Override
    public boolean contains(char val) {
      return indexOf(val) >= 0;
    }
    
    @Override
    public int indexOf(short val) {
      PrimitiveIterator.OfInt it = intIterator();
      int idx = 0;
      while(it.hasNext()) {
        if(it.nextInt() == (int)val) return idx;
        idx++;
      }
      return -1;
    }
    
    @Override
    public boolean contains(short val) {
      return indexOf(val) >= 0;
    }
    
    @Override
    public int indexOf(int val) {
      PrimitiveIterator.OfInt it = intIterator();
      int idx = 0;
      while(it.hasNext()) {
        if(it.nextInt() == val) return idx;
        idx++;
      }
      return -1;
    }
    
    @Override
    public boolean contains(int val) {
      return indexOf(val) >= 0;
    }
    
    @Override
    public int indexOf(float val) {
      float[] fs = toFloatArray();
      for(int i = 0; i < fs.length; i++) {
        if(fs[i] == val) return i;
      }
      return -1;
    }
    
    @Override
    public boolean contains(float val) {
      return indexOf(val) >= 0;
    }
    
    @Override
    public int indexOf(long val) {
      PrimitiveIterator.OfLong it = longIterator();
      int idx = 0;
      while(it.hasNext()) {
        if(it.nextLong() == val) return idx;
        idx++;
      }
      return -1;
    }
    
    @Override
    public boolean contains(long val) {
      return indexOf(val) >= 0;
    }
    
    @Override
    public int indexOf(double val) {
      PrimitiveIterator.OfDouble it = doubleIterator();
      int idx = 0;
      while(it.hasNext()) {
        if(it.nextDouble() == val) return idx;
        idx++;
      }
      return -1;
    }
    
    @Override
    public boolean contains(double val) {
      return indexOf(val) >= 0;
    }
    
  }
  
}
