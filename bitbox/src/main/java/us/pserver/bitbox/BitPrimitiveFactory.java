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
public class BitPrimitiveFactory implements BitBoxFactory<BitPrimitive> {
  
  private static final BitPrimitiveFactory _instance = new BitPrimitiveFactory();
  
  private BitPrimitiveFactory() {}
  
  public static BitPrimitiveFactory get() {
    return _instance;
  }
  
  public BitPrimitive createFrom(boolean val) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Byte.BYTES);
    buf.putInt(BitPrimitive.ID_BOOLEAN);
    buf.putInt(buf.capacity());
    buf.put((byte)(val ? 1 : 0));
    buf.flip();
    return new BitPrimitive.Primitive(buf);
  }

  public BitPrimitive createFrom(char val) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Character.BYTES);
    buf.putInt(BitPrimitive.ID_CHAR);
    buf.putInt(buf.capacity());
    buf.putChar(val);
    buf.flip();
    return new BitPrimitive.Primitive(buf);
  }

  public BitPrimitive createFrom(short val) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Short.BYTES);
    buf.putInt(BitPrimitive.ID_SHORT);
    buf.putInt(buf.capacity());
    buf.putShort(val);
    buf.flip();
    return new BitPrimitive.Primitive(buf);
  }

  public BitPrimitive createFrom(int val) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Integer.BYTES);
    buf.putInt(BitPrimitive.ID_INT);
    buf.putInt(buf.capacity());
    buf.putInt(val);
    buf.flip();
    return new BitPrimitive.Primitive(buf);
  }

  public BitPrimitive createFrom(float val) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Float.BYTES);
    buf.putInt(BitPrimitive.ID_FLOAT);
    buf.putInt(buf.capacity());
    buf.putFloat(val);
    buf.flip();
    return new BitPrimitive.Primitive(buf);
  }

  public BitPrimitive createFrom(long val) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Long.BYTES);
    buf.putInt(BitPrimitive.ID_LONG);
    buf.putInt(buf.capacity());
    buf.putLong(val);
    buf.flip();
    return new BitPrimitive.Primitive(buf);
  }

  public BitPrimitive createFrom(double val) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Double.BYTES);
    buf.putInt(BitPrimitive.ID_DOUBLE);
    buf.putInt(buf.capacity());
    buf.putDouble(val);
    buf.flip();
    return new BitPrimitive.Primitive(buf);
  }

  @Override
  public BitPrimitive createFrom(ByteBuffer buf) {
    int lim = buf.limit();
    int pos = buf.position();
    buf.position(pos + Integer.BYTES);
    buf.limit(pos + buf.getInt() - Integer.BYTES * 2).position(pos);
    BitPrimitive reg = new BitPrimitive.Primitive(buf.slice());
    buf.limit(lim);
    return reg;
  }

  @Override
  public BitPrimitive createFrom(ReadableByteChannel ch) throws IOException {
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
    return new BitPrimitive.Primitive(buf);
  }

  @Override
  public BitPrimitive createFrom(DynamicByteBuffer buf) {
    return createFrom(buf.toByteBuffer());
  }

}
