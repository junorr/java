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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/12/2018
 */
public interface BitPrimitive extends BitBox {
  
  public static final int ID_BOOLEAN = BitPrimitive.class.getName().concat(boolean.class.getName()).hashCode();
  
  public static final int ID_CHAR = BitPrimitive.class.getName().concat(char.class.getName()).hashCode();
  
  public static final int ID_SHORT = BitPrimitive.class.getName().concat(short.class.getName()).hashCode();
  
  public static final int ID_INT = BitPrimitive.class.getName().concat(int.class.getName()).hashCode();
  
  public static final int ID_FLOAT = BitPrimitive.class.getName().concat(float.class.getName()).hashCode();
  
  public static final int ID_LONG = BitPrimitive.class.getName().concat(long.class.getName()).hashCode();
  
  public static final int ID_DOUBLE = BitPrimitive.class.getName().concat(double.class.getName()).hashCode();
  
  
  public static final int BYTES_BOOLEAN = Integer.BYTES * 2 + Byte.BYTES;
  
  public static final int BYTES_CHAR = Integer.BYTES * 2 + Character.BYTES;
  
  public static final int BYTES_SHORT = Integer.BYTES * 2 + Short.BYTES;
  
  public static final int BYTES_INT = Integer.BYTES * 2 + Integer.BYTES;
  
  public static final int BYTES_FLOAT = Integer.BYTES * 2 + Float.BYTES;
  
  public static final int BYTES_LONG = Integer.BYTES * 2 + Long.BYTES;
  
  public static final int BYTES_DOUBLE = Integer.BYTES * 2 + Double.BYTES;
  
  
  public boolean getBoolean();
  
  public char getChar();
  
  public short getShort();
  
  public int getInt();
  
  public float getFloat();
  
  public long getLong();
  
  public double getDouble();
  
  
  public default boolean isBoolean() {
    return ID_BOOLEAN == boxID();
  }
  
  public default boolean isChar() {
    return ID_CHAR == boxID();
  }
  
  public default boolean isShort() {
    return ID_SHORT == boxID();
  }
  
  public default boolean isInt() {
    return ID_INT == boxID();
  }
  
  public default boolean isFloat() {
    return ID_FLOAT == boxID();
  }
  
  public default boolean isLong() {
    return ID_LONG == boxID();
  }
  
  public default boolean isDouble() {
    return ID_DOUBLE == boxID();
  }
  
  
  
  
  
  static class Primitive extends AbstractBitBox implements BitPrimitive {
    
    public Primitive(ByteBuffer buf) {
      super(buf);
    }
    
    @Override
    public Object get() {
      Object ret;
      if(isBoolean()) {
        ret = Boolean.valueOf(getBoolean());
      }
      else if(isChar()) {
        ret = Character.valueOf(getChar());
      }
      else if(isShort()) {
        ret = Short.valueOf(getShort());
      }
      else if(isInt()) {
        ret = Integer.valueOf(getInt());
      }
      else if(isFloat()) {
        ret = Float.valueOf(getFloat());
      }
      else if(isLong()) {
        ret = Long.valueOf(getLong());
      }
      else {
        ret = Double.valueOf(getDouble());
      }
      return ret;
    }
    
    @Override
    public boolean getBoolean() {
      if(!isBoolean()) throw new UnsupportedOperationException();
      buffer.position(Integer.BYTES * 2);
      return buffer.get() == 1;
    }
    
    @Override
    public char getChar() {
      if(!isChar()) throw new UnsupportedOperationException();
      buffer.position(Integer.BYTES * 2);
      return buffer.getChar();
    }
    
    @Override
    public short getShort() {
      if(!isChar()) throw new UnsupportedOperationException();
      buffer.position(Integer.BYTES * 2);
      return buffer.getShort();
    }
    
    @Override
    public int getInt() {
      if(!isInt()) throw new UnsupportedOperationException();
      buffer.position(Integer.BYTES * 2);
      return buffer.getInt();
    }
    
    @Override
    public float getFloat() {
      if(!isFloat()) throw new UnsupportedOperationException();
      buffer.position(Integer.BYTES * 2);
      return buffer.getFloat();
    }
    
    @Override
    public long getLong() {
      if(!isLong()) throw new UnsupportedOperationException();
      buffer.position(Integer.BYTES * 2);
      return buffer.getLong();
    }
    
    @Override
    public double getDouble() {
      if(!isDouble()) throw new UnsupportedOperationException();
      buffer.position(Integer.BYTES * 2);
      return buffer.getDouble();
    }
    
  }
  

}
