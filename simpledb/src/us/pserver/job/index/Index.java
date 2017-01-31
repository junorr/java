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

package us.pserver.job.index;

import us.pserver.job.Region;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/12/2016
 */
public interface Index<T> {

  public String getName();
  
  public T getValue();
  
  public List<Region> regions();
  
  
  public static <U> Index<U> of(String name, U value, List<Region> regions) {
    return new DefIndex(name, value, regions);
  }
  
  
  
  
  
  public static class DefIndex<T> implements Index<T> {
    
    private final String name;
    
    private final T value;
    
    private final List<Region> regions;
    
    
    private DefIndex() {
      name = null;
      value = null;
      regions = null;
    }
    
    
    public DefIndex(String name, T value) {
      this(name, value, new ArrayList<>());
    }
    
    
    public DefIndex(String name, T value, List<Region> regs) {
      this.name = Sane.of(name)
          .with("Bad Name: "+ name)
          .get(Checkup.isNotEmpty());
      this.value = Sane.of(value)
          .with("Bad Null Value")
          .get(Checkup.isNotNull());
      this.regions = Collections.unmodifiableList(
          (List<Region>) Sane.of(regs)
              .with("Bad Region List")
              .with(Checkup.isNotNull())
              .and(Checkup.isNotEmptyCollection())
              .get()
      );
    }
    

    @Override
    public String getName() {
      return name;
    }


    @Override
    public T getValue() {
      return value;
    }
    
    
    @Override
    public List<Region> regions() {
      return regions;
    }
    
    
    @Override
    public int hashCode() {
      int hash = 3;
      hash = 83 * hash + Objects.hashCode(this.name);
      hash = 83 * hash + Objects.hashCode(this.value);
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
      final DefIndex<?> other = (DefIndex<?>) obj;
      if (!Objects.equals(this.name, other.name)) {
        return false;
      }
      return Objects.equals(this.value, other.value);
    }


    @Override
    public String toString() {
      return "Index{\"" + name + "\", " + value + ", " + regions + '}';
    }
    
  }
  
}
