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
import java.nio.channels.WritableByteChannel;
import us.pserver.tools.Hash;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2018
 */
public interface BitNumber extends BitBox {

  public static final int ID_BYTE = Byte.class.getName().hashCode();
  
  public static final int ID_SHORT = Short.class.getName().hashCode();
  
  public static final int ID_INT = Integer.class.getName().hashCode();
  
  public static final int ID_FLOAT = Float.class.getName().hashCode();
  
  public static final int ID_LONG = Long.class.getName().hashCode();
  
  public static final int ID_DOUBLE = Double.class.getName().hashCode();
  
  
  public <T extends Number> T get();
  
  public byte getByte();
  
  public short getShort();
  
  public int getInt();
  
  public float getFloat();
  
  public long getLong();
  
  public double getDouble();
  
  public boolean isByte();
  
  public boolean isShort();
  
  public boolean isInt();
  
  public boolean isFloat();
  
  public boolean isLong();
  
  public boolean isDouble();
  
  
  
  public static BitNumberFactory factory() {
    return BitNumberFactory.get();
  }
  
  
  
  
  
  static class BNumber extends AbstractBitBox implements BitNumber {
    
    public BNumber(ByteBuffer buf) {
      super(buf);
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
      return boxSize();
    }
    
    @Override
    public int writeTo(WritableByteChannel ch) throws IOException {
      buffer.position(0);
      return ch.write(buffer);
    }
    
    @Override
    public int writeTo(DynamicByteBuffer buf) {
      buffer.position(0);
      buf.put(buffer);
      return boxSize();
    }
    
    @Override
    public <T extends Number> T get() {
      Number num = 0;
      if(isByte()) {
        buffer.position(Integer.BYTES * 2);
        num = buffer.get();
      }
      else if(isShort()) {
        buffer.position(Integer.BYTES * 2);
        num = buffer.getShort();
      }
      else if(isInt()) {
        buffer.position(Integer.BYTES * 2);
        num = buffer.getInt();
      }
      else if(isFloat()) {
        buffer.position(Integer.BYTES * 2);
        num = buffer.getFloat();
      }
      else if(isLong()) {
        buffer.position(Integer.BYTES * 2);
        num = buffer.getLong();
      }
      else {
        buffer.position(Integer.BYTES * 2);
        num = buffer.getDouble();
      }
      return (T) num;
    }
    
    @Override
    public byte getByte() {
      return get().byteValue();
    }
    
    @Override
    public short getShort() {
      return get().shortValue();
    }
    
    @Override
    public int getInt() {
      return get().intValue();
    }
    
    @Override
    public float getFloat() {
      return get().floatValue();
    }
    
    @Override
    public long getLong() {
      return get().longValue();
    }
    
    @Override
    public double getDouble() {
      return get().doubleValue();
    }
    
    @Override
    public boolean isByte() {
      return boxID() == ID_BYTE;
    }
    
    @Override
    public boolean isShort() {
      return boxID() == ID_SHORT;
    }
    
    @Override
    public boolean isInt() {
      return boxID() == ID_INT;
    }
    
    @Override
    public boolean isFloat() {
      return boxID() == ID_FLOAT;
    }
    
    @Override
    public boolean isLong() {
      return boxID() == ID_LONG;
    }
    
    @Override
    public boolean isDouble() {
      return boxID() == ID_DOUBLE;
    }
    
  }
  
}
