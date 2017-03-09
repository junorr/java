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

package us.pserver.jose;

import us.pserver.jose.driver.Writable;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2016
 */
public interface Region extends Writable, Serializable {

  public int start();
  
  public int length();
  
  public boolean isValid();
  
  public boolean contains(Region reg);
  
  
  public static Region of(int start, int length) {
    return new DefRegion(start, length);
  }
  
  
  public static Region of(ByteBuffer buf) {
    if(buf == null || buf.capacity() < 8) {
      throw new IllegalArgumentException("Bad Null/Empty ByteBuffer");
    }
    return new DefRegion(buf.getInt(), buf.getInt());
  }
  
  
  
  
  
  public static class DefRegion implements Region {
    
    private final int start;
    
    private final int length;
    
    
    public DefRegion() {
      start = length = -1;
    }
    
    
    public DefRegion(int start, int length) {
      this.start = start;
      this.length = length;
    }
    

    @Override
    public int start() {
      return start;
    }


    @Override
    public int length() {
      return length;
    }


    @Override
    public boolean isValid() {
      return start != -1 && length > 0;
    }
    
    
    @Override
    public boolean contains(Region reg) {
      return reg != null
          && reg.start() < (start + length)
          && (reg.start() + reg.length()) > start;
    }


    @Override
    public Region write(ByteBuffer buf) {
      if(buf == null || buf.capacity() < 16) {
        throw new IllegalArgumentException("Bad Null/Empty ByteBuffer");
      }
      buf.putLong(start);
      buf.putLong(length);
      return this;
    }


    @Override
    public int hashCode() {
      int hash = 5;
      hash = 29 * hash + (int) (this.start ^ (this.start >>> 32));
      hash = 29 * hash + (int) (this.length ^ (this.length >>> 32));
      return hash;
    }


    @Override
    public boolean equals(Object obj) {
      return obj != null
          && Region.class.isAssignableFrom(obj.getClass())
          && ((Region)obj).start() == start
          && ((Region)obj).length()== length;
    }


    @Override
    public String toString() {
      return start + "-" + length;
    }
    
  }
  
}
