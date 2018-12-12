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
 * @version 0.0 - 12/12/2018
 */
public class BitNumberFactory implements BitBoxFactory<BitNumber> {
  
  private static final BitNumberFactory _instance = new BitNumberFactory();
  
  private BitNumberFactory() {}
  
  public static BitNumberFactory get() {
    return _instance;
  }
  
  public BitNumber createFrom(byte n) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Byte.BYTES);
    buf.putInt(BitNumber.ID_BYTE);
    buf.putInt(buf.capacity());
    buf.put(n);
    buf.flip();
    return new BitNumber.BNumber(buf);
  }
  
  public BitNumber createFrom(short n) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Short.BYTES);
    buf.putInt(BitNumber.ID_SHORT);
    buf.putInt(buf.capacity());
    buf.putShort(n);
    buf.flip();
    return new BitNumber.BNumber(buf);
  }
  
  public BitNumber createFrom(int n) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Integer.BYTES);
    buf.putInt(BitNumber.ID_INT);
    buf.putInt(buf.capacity());
    buf.putInt(n);
    buf.flip();
    return new BitNumber.BNumber(buf);
  }
  
  public BitNumber createFrom(float n) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Float.BYTES);
    buf.putInt(BitNumber.ID_FLOAT);
    buf.putInt(buf.capacity());
    buf.putFloat(n);
    buf.flip();
    return new BitNumber.BNumber(buf);
  }
  
  public BitNumber createFrom(long n) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Long.BYTES);
    buf.putInt(BitNumber.ID_LONG);
    buf.putInt(buf.capacity());
    buf.putLong(n);
    buf.flip();
    return new BitNumber.BNumber(buf);
  }
  
  public BitNumber createFrom(double n) {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2 + Double.BYTES);
    buf.putInt(BitNumber.ID_DOUBLE);
    buf.putInt(buf.capacity());
    buf.putDouble(n);
    buf.flip();
    return new BitNumber.BNumber(buf);
  }
  
  public BitNumber createFrom(Number n) {
    if(Byte.class.isAssignableFrom(n.getClass())) {
      return createFrom(n.byteValue());
    }
    else if(Short.class.isAssignableFrom(n.getClass())) {
      return createFrom(n.shortValue());
    }
    else if(Integer.class.isAssignableFrom(n.getClass())) {
      return createFrom(n.intValue());
    }
    else if(Float.class.isAssignableFrom(n.getClass())) {
      return createFrom(n.floatValue());
    }
    else if(Long.class.isAssignableFrom(n.getClass())) {
      return createFrom(n.longValue());
    }
    else {
      return createFrom(n.doubleValue());
    }
  }
  
  @Override
  public BitNumber createFrom(ByteBuffer buf) {
    int pos = buf.position();
    int lim = buf.limit();
    int id = buf.getInt();
    if(id != BitNumber.ID_BYTE
        && id != BitNumber.ID_SHORT
        && id != BitNumber.ID_INT
        && id != BitNumber.ID_FLOAT
        && id != BitNumber.ID_LONG
        && id != BitNumber.ID_DOUBLE) {
      throw new IllegalArgumentException("Not a BitNumber content");
    }
    int len = buf.getInt();
    buf.position(pos).limit(pos + len);
    BitNumber num = new BitNumber.BNumber(buf);
    buf.limit(lim);
    return num;
  }
  
  @Override
  public BitNumber createFrom(ReadableByteChannel ch) throws IOException {
    ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES * 2);
    ch.read(buf);
    buf.flip();
    int id = buf.getInt();
    int len = buf.getInt();
    buf = ByteBuffer.allocate(len - Integer.BYTES * 2);
    buf.putInt(id);
    buf.putInt(len);
    ch.read(buf);
    buf.flip();
    return new BitNumber.BNumber(buf);
  }
  
  @Override
  public BitNumber createFrom(DynamicByteBuffer buf) {
    return createFrom(buf.toByteBuffer());
  }

}
