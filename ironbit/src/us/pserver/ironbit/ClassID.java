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

package us.pserver.ironbit;

import java.util.Comparator;
import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/09/2017
 */
public interface ClassID extends Comparable<ClassID> {

  public int getID();
  
  public String getClassName();
  
  public Class getClazz();
  
  
  public static ClassID of(int id, Class cls) {
    return new DefClassID(id, cls);
  }
  
  
  public static ClassID of(int id, String cls) {
    return new DefClassID(id, IronbitConfiguration.get().loader().loadClass(cls));
  }
  
  
  public static Comparator<ClassID> idComparator() {
    return (a,b)->Integer.compare(a.getID(), b.getID());
  }
  
  
  public static Comparator<ClassID> classComparator() {
    return (a,b)->a.getClassName().compareTo(b.getClassName());
  }
  
  
  
  
  
  public static class DefClassID implements ClassID {
    
    private final int id;
    
    private final String cname;
    
    private final Class clazz;
    
    public DefClassID(int id, Class cls) {
      this.id = id;
      this.clazz = NotNull.of(cls).getOrFail("Bad null Class");
      this.cname = clazz.getName();
    }

    @Override
    public int getID() {
      return id;
    }

    @Override
    public String getClassName() {
      return cname;
    }
    
    @Override
    public Class getClazz() {
      return clazz;
    }
    
    @Override
    public int compareTo(ClassID o) {
      return Integer.compare(this.id, o.getID());
    }
    
    @Override
    public int hashCode() {
      int hash = 5;
      hash = 59 * hash + this.id;
      hash = 59 * hash + Objects.hashCode(this.cname);
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
      final DefClassID other = (DefClassID) obj;
      if (this.id != other.id) {
        return false;
      }
      return Objects.equals(this.cname, other.cname);
    }
    
    @Override
    public String toString() {
      return "ClassID: ("+ id+ ") "+ getClassName();
    }
    
  }
  
}
