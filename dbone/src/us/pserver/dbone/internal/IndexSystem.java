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

package us.pserver.dbone.internal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Stream;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
public interface IndexSystem {

  public < V extends Comparable<V> > void put( Class cls, Index<V> idx );
  
  public Stream< Index<String> > getAllUID( Class cls );
  
  public < V extends Comparable<V> >  Stream< Index<V> > get( String name, Class cls, V value );
  
  
  
  
  
  public static class DefIndexSystem implements IndexSystem {
    
    private final Map<String, List< Index<?> >> indexes;
    
    public DefIndexSystem() {
      indexes = new ConcurrentSkipListMap<>();
    }

    @Override
    public < V extends Comparable<V> > void put( Class cls, Index<V> idx ) {
      NotNull.of(cls).failIfNull("Bad null Class");
      NotNull.of(idx).failIfNull("Bad null Index");
      indexes.put(cls.getName().concat(".").concat(idx.name()), (List<Index<?>>) idx);
    }


    @Override
    public Stream< Index<String> > getAllUID( Class cls ) {
      return (Stream< Index<String> >) indexes.get(cls.getName().concat(".uid")).stream();
    }


    @Override
    public < V extends Comparable<V> >  Stream< Index<V> > get( String name, Class cls, V value ) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  }
  
}
