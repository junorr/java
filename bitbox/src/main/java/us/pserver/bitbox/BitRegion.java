
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
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import us.pserver.tools.Hash;
import us.pserver.tools.io.DynamicByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2017
 */
public interface BitRegion extends BitBox {
  
  public static final int BYTES = Integer.BYTES * 4;
  
  public static final int ID = BitRegion.class.getName().hashCode();
  
  public int offset();
  
  public int length();
  
  public int end();
  
  public boolean isValid();
  
  public boolean contains(BitRegion r);
  
  
  
  public static BitRegionFactory factory() {
    return BitRegionFactory.get();
  }
  
  
  
  public static BitRegion of(int offset, int length) {
    return new Region(offset, length);
  }
  
  
  public static BitRegion of(ByteBuffer buf) {
    return new Region(buf);
  }
  
  
  public static BitRegion invalid() {
    return of(-1, -1);
  }
  
  
  
  
  
  static class Region extends AbstractBitBox implements BitRegion {
    
    public Region(ByteBuffer buf) {
      super(buf);
      if(buffer.getInt() != ID) {
        throw new IllegalArgumentException("Not a BinaryArray content");
      }
    }
    
    public Region(int ofs, int len) {
      super(ByteBuffer.allocate(BYTES));
      buffer.putInt(ID);
      buffer.putInt(BYTES);
      buffer.putInt(ofs);
      buffer.putInt(len);
    }
    
    @Override
    public int[] get() {
      return new int[] {offset(), length()};
    }
    
    @Override
    public int offset() {
      buffer.position(Integer.BYTES * 2);
      return buffer.getInt();
    }
    
    @Override
    public int length() {
      buffer.position(Integer.BYTES * 3);
      return buffer.getInt();
    }
    
    @Override
    public int end() {
      return offset() + length();
    }
    
    @Override
    public boolean isValid() {
      return offset() >= 0 && length() >= 1;
    }
    
    @Override
    public boolean contains(BitRegion r) {
      return offset() <= r.offset() && end() >= r.end();
    }
    
    @Override
    public ByteBuffer toByteBuffer() {
      return buffer;
    }

    @Override
    public String toString() {
      return String.format("BinaryRegion{offset=%d, length=%d}", offset(), length());
    }
    

    @Override
    public String sha256sum() {
      if(buffer.hasArray()) {
        return Hash.sha256().of(buffer.array(), buffer.arrayOffset(), BYTES);
      }
      return Hash.sha256().of(toByteArray());
    }


    @Override
    public byte[] toByteArray() {
      if(buffer.hasArray() && buffer.array().length == BYTES) {
        return buffer.array();
      }
      byte[] bs = new byte[BYTES];
      buffer.position(0);
      buffer.get(bs);
      return bs;
    }


    @Override
    public int writeTo(ByteBuffer buf) {
      buffer.position(0);
      buf.put(buf);
      return BYTES;
    }


    @Override
    public int writeTo(WritableByteChannel ch) throws IOException {
      buffer.position(0);
      ch.write(buffer);
      return BYTES;
    }


    @Override
    public int writeTo(DynamicByteBuffer buf) {
      buffer.position(0);
      buf.put(buf);
      return BYTES;
    }

  }
  
}
