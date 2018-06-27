/************************************************************************
 * Copyright (C) 2017 Juno Roelser - juno.rr@gmail.com                  *                            
 * This program is free software: you can redistribute it and/or modify *
 * it under the terms of the GNU General Public License as published by *
 * the Free Software Foundation, either version 3 of the License, or    *
 * (at your option) any later version.                                  *
 * This program is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of       *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the        *
 * GNU General Public License for more details.                         *
 * You should have received a copy of the GNU General Public License    *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.*
 ************************************************************************/

package us.pserver.dbone;

import java.util.Objects;
import us.pserver.dbone.index.Index;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
public interface Selected< T, V extends Comparable<V> > {

  public T get();
  
  public Index<V> index();
  
  
  public static < U, X extends Comparable<X> > Selected<U, X> of(U value, Index<X> idx) {
    return new DefSelected(value, idx);
  }
  
  
  
  
  
  public static class DefSelected< T, V extends Comparable<V> > implements Selected<T, V> {
    
    private final T value;
    
    private final Index<V> idx;
    
    public DefSelected(T value, Index<V> idx) {
      this.value = NotNull.of(value).getOrFail("Bad null value");
      this.idx = NotNull.of(idx).getOrFail("Bad null Index");
    }

    @Override
    public T get() {
      return value;
    }

    @Override
    public Index<V> index() {
      return idx;
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 29 * hash + Objects.hashCode(this.value);
      hash = 29 * hash + Objects.hashCode(this.idx);
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
      final DefSelected<?, ?> other = (DefSelected<?, ?>) obj;
      if (!Objects.equals(this.value, other.value)) {
        return false;
      }
      return Objects.equals(this.idx, other.idx);
    }
    
    @Override
    public String toString() {
      return String.format("Selected{value=%s, %s}", value, idx);
    }
    
  }
  
}
