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

package us.pserver.dbone.volume;

import java.nio.ByteBuffer;
import us.pserver.dbone.ObjectUID;
import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2017
 */
public interface StoreUnit {

  public ObjectUID objectUID();
  
  public ByteBuffer value();
  
  
  public static StoreUnit of(ObjectUID uid, ByteBuffer val) {
    return new StoreUnitImpl(uid, val);
  }
  
  
  
  
  
  public static class StoreUnitImpl implements StoreUnit {
    
    private final ObjectUID uid;
    
    private final ByteBuffer value;
    
    
    public StoreUnitImpl(ObjectUID uid, ByteBuffer val) {
      this.uid = NotNull.of(uid).getOrFail("Bad null ObjectUID");
      this.value = NotNull.of(val).getOrFail("Bad null MappedValue");
    }
    
    @Override
    public ObjectUID objectUID() {
      return this.uid;
    }
    
    @Override
    public ByteBuffer value() {
      return this.value;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 71 * hash + Objects.hashCode(this.uid);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final StoreUnitImpl other = (StoreUnitImpl) obj;
      return Objects.equals(this.uid, other.uid);
    }

    @Override
    public String toString() {
      return "StoreUnit{" + "uid=" + uid + ", length=" + value.remaining() + '}';
    }
    
  }
  
}
