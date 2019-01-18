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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/12/2018
 */
public class BitPrimitiveArrayFactory extends AbstractBitBoxFactory<BitPrimitiveArray,Integer[]> {
  
  public BitPrimitiveArrayFactory(BitBoxConfiguration conf) {
    super(conf);
  }
  
  @Override
  public BitPrimitiveArray createFrom(Integer[] vals) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + BitPrimitive.BYTES_INT * vals.length);
    buf.putInt(BitArray.ID);
    buf.putInt(buf.capacity());
    buf.putInt(vals.length);
    for(int i = 0; i < vals.length; i++) {
      buf.putInt(BitPrimitive.ID_INT);
      buf.putInt(BitPrimitive.BYTES_INT);
      buf.putInt(vals[i]);
    }
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }
  
  public BitPrimitiveArray createFrom(boolean[] vals) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + BitPrimitive.BYTES_BOOLEAN * vals.length);
    buf.putInt(BitArray.ID);
    buf.putInt(buf.capacity());
    for(int i = 0; i < vals.length; i++) {
      buf.putInt(BitPrimitive.ID_BOOLEAN);
      buf.putInt(BitPrimitive.BYTES_BOOLEAN);
      buf.put((byte)(vals[i] ? 1 : 0));
    }
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }

  public BitPrimitiveArray createFrom(char[] vals) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + BitPrimitive.BYTES_CHAR * vals.length);
    buf.putInt(BitArray.ID);
    buf.putInt(buf.capacity());
    for(int i = 0; i < vals.length; i++) {
      buf.putInt(BitPrimitive.ID_CHAR);
      buf.putInt(BitPrimitive.BYTES_CHAR);
      buf.putChar(vals[i]);
    }
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }

  public BitPrimitiveArray createFrom(short[] vals) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + BitPrimitive.BYTES_SHORT * vals.length);
    buf.putInt(BitArray.ID);
    buf.putInt(buf.capacity());
    for(int i = 0; i < vals.length; i++) {
      buf.putInt(BitPrimitive.ID_SHORT);
      buf.putInt(BitPrimitive.BYTES_SHORT);
      buf.putShort(vals[i]);
    }
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }

  public BitPrimitiveArray createFrom(int[] vals) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + BitPrimitive.BYTES_INT * vals.length);
    buf.putInt(BitArray.ID);
    buf.putInt(buf.capacity());
    buf.putInt(vals.length);
    for(int i = 0; i < vals.length; i++) {
      buf.putInt(BitPrimitive.ID_INT);
      buf.putInt(BitPrimitive.BYTES_INT);
      buf.putInt(vals[i]);
    }
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }

  public BitPrimitiveArray createFrom(float[] vals) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + BitPrimitive.BYTES_FLOAT * vals.length);
    buf.putInt(BitArray.ID);
    buf.putInt(buf.capacity());
    for(int i = 0; i < vals.length; i++) {
      buf.putInt(BitPrimitive.ID_FLOAT);
      buf.putInt(BitPrimitive.BYTES_FLOAT);
      buf.putFloat(vals[i]);
    }
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }

  public BitPrimitiveArray createFrom(long[] vals) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + BitPrimitive.BYTES_LONG * vals.length);
    buf.putInt(BitArray.ID);
    buf.putInt(buf.capacity());
    for(int i = 0; i < vals.length; i++) {
      buf.putInt(BitPrimitive.ID_LONG);
      buf.putInt(BitPrimitive.BYTES_LONG);
      buf.putLong(vals[i]);
    }
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }

  public BitPrimitiveArray createFrom(double[] vals) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 3 + BitPrimitive.BYTES_DOUBLE * vals.length);
    buf.putInt(BitArray.ID);
    buf.putInt(buf.capacity());
    for(int i = 0; i < vals.length; i++) {
      buf.putInt(BitPrimitive.ID_DOUBLE);
      buf.putInt(BitPrimitive.BYTES_DOUBLE);
      buf.putDouble(vals[i]);
    }
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }

  @Override
  public BitPrimitiveArray createFrom(ByteBuffer buf) {
    int lim = buf.limit();
    int pos = buf.position();
    buf.position(pos + Integer.BYTES);
    buf.limit(pos + buf.getInt()).position(pos);
    BitPrimitiveArray reg = new BitPrimitiveArray.PrimitiveArray(buf.slice());
    buf.limit(lim);
    return reg;
  }

  @Override
  public BitPrimitiveArray createFrom(ReadableByteChannel ch) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2);
    ch.read(buf);
    buf.flip();
    int id = buf.getInt();
    int len = buf.getInt();
    buf = ByteBuffer.allocate(len);
    buf.putInt(id);
    buf.putInt(len);
    ch.read(buf);
    buf.flip();
    return new BitPrimitiveArray.PrimitiveArray(buf);
  }

  @Override
  public BitPrimitiveArray createFrom(DynamicByteBuffer buf) {
    return createFrom(buf.toByteBuffer());
  }

}
