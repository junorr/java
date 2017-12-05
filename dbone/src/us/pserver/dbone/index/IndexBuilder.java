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

package us.pserver.dbone.index;

import java.util.function.Function;
import us.pserver.dbone.volume.Record;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
public interface IndexBuilder< T , V extends Comparable<V> > {
  
  public Index<V> build(T object, Record rec);
  
  public String name();
  
  
  public static < U, S extends Comparable<S> > IndexBuilder<U,S> of(String name, Function<U,S> acessor) {
    return new DefIndexBuilder(name, acessor);
  }
  
  
  
  
  
  public static class DefIndexBuilder< T , V extends Comparable<V> > implements IndexBuilder<T, V> {
    
    private final String name;
    
    private final Function<T, V> acessor;
    
    public DefIndexBuilder(String name, Function<T, V> acessor) {
      this.name = NotNull.of(name).getOrFail("Bad null name");
      this.acessor = NotNull.of(acessor).getOrFail("Bad null acessor Function");
    }

    @Override
    public Index<V> build(T object, Record rec) {
      return Index.of(name, acessor.apply(object), rec);
    }
    
    @Override
    public String name() {
      return name;
    }
    
  }
  
}
