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

package us.pserver.sdb;

import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 22/10/2014
 */
public class OID {
  
  private long block;
  
  private Object object;
  
  
  public OID() {
    block = -1;
    object = null;
  }


  public OID(long block, Object object) {
    this.block = block;
    this.object = object;
  }
  
  
  public long block() {
    return block;
  }
  
  
  public OID block(long blk) {
    block = blk;
    return this;
  }
  
  
  public Object get() {
    return object;
  }
  
  
  public <T> T getAs() {
    return (T) object;
  }
  
  
  public boolean isFromType(Class c) {
    if(c == null || object == null) 
      return false;
    return c.isAssignableFrom(object.getClass());
  }
  
  
  public OID set(Object obj) {
    object = obj;
    return this;
  }
  
  
  public boolean hasObject() {
    return object != null;
  }
  
  
  public boolean hasBlock() {
    return block != -1;
  }
  
  
  public boolean isSetted() {
    return hasObject() && hasBlock();
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 17 * hash + (int) (this.block ^ (this.block >>> 32));
    hash = 17 * hash + Objects.hashCode(this.object);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final OID other = (OID) obj;
    if (this.block != other.block) {
      return false;
    }
    if (!Objects.equals(this.object, other.object)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "OID{" + "block=" + block + ", object=" + object + '}';
  }

}
