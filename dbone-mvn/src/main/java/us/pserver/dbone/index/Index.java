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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import java.util.function.Predicate;
import us.pserver.dbone.region.Region;
import us.pserver.tools.Match;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
@JsonSerialize(using = IndexSerializer.class)
public interface Index< T extends Comparable<T> > extends Comparable< Index<T> >, Predicate<T> {
  
  public String name();
  
  public T value();
  
  public Region region();
  
  @Override
  public default boolean test(T val) {
    return value().equals(val);
  }
  
  @Override
  public default int compareTo( Index<T> other ) {
    Objects.requireNonNull(other, "Bad null Index");
    int cmp = name().compareTo(other.name());
    if(cmp == 0) {
      cmp = value().compareTo(other.value());
    }
    return (cmp == 0 ? region().compareTo(other.region()) : cmp);
  }
  
  
  public static < U extends Comparable<U> > Index<U> of( String name, U value, Region rec ) {
    return new DefIndex<>(name, value, rec);
  }
  
  
  
  
  
  @JsonDeserialize(using = IndexDeserializer.class)
  public static class DefIndex< T extends Comparable<T> > implements Index<T> {
    
    private final String name;
    
    private final T value;
    
    private final Region record;
    
    public DefIndex(String name, T value, Region region) {
      this.name = Match.notNull(name).getOrFail("Bad null name");
      this.value = Match.notNull(value).getOrFail("Bad null value");
      this.record = Match.notNull(region).getOrFail("Bad null Record");
    }
    
    @Override
    public String name() {
      return name;
    }
    
    @Override
    public T value() {
      return value;
    }
    
    @Override
    public Region region() {
      return record;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 97 * hash + Objects.hashCode(this.name);
      hash = 97 * hash + Objects.hashCode(this.value);
      hash = 97 * hash + Objects.hashCode(this.record);
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
      if (!Objects.equals(this.value, other.value)) {
        return false;
      }
      return Objects.equals(this.record, other.record);
    }
    
    @Override
    public String toString() {
      return String.format("Index{%s, value=%s, %s}", name, value, record);
    }
    
  }
  
}
