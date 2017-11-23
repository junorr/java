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

package us.pserver.dbone.internal;

import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/10/2017
 */
public interface StoreUnit {

  public String getObjectUID();
  
  public Object getObject();
  
  
  public static StoreUnit of(String uid, Object obj) {
    return new DefStoreUnit(uid, obj);
  }
  
  
  
  
  
  public static class DefStoreUnit implements StoreUnit {
    
    private final Object obj;
    
    private final String uid;
    
    public DefStoreUnit(String uid, Object obj) {
      this.uid = NotNull.of(uid).getOrFail("Bad null ObjectUID");
      this.obj = NotNull.of(obj).getOrFail("Bad null Object");
    }

    @Override
    public String getObjectUID() {
      return uid;
    }

    @Override
    public Object getObject() {
      return obj;
    }


    @Override
    public int hashCode() {
      int hash = 7;
      hash = 47 * hash + Objects.hashCode(this.obj);
      hash = 47 * hash + Objects.hashCode(this.uid);
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
      final DefStoreUnit other = (DefStoreUnit) obj;
      if (!Objects.equals(this.uid, other.uid)) {
        return false;
      }
      return Objects.equals(this.obj, other.obj);
    }


    @Override
    public String toString() {
      return "StoreUnit{uid="+ uid + ", obj=" + obj + '}';
    }
  
  }
  
}
