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

import java.util.Objects;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
public interface Index<T extends Comparable<T>> extends Comparable<Index<T>> {
  
  public String name();
  
  public T value();
  
  public Record record();
  
  @Override
  public default int compareTo(Index<T> other) {
    NotNull.of(other).failIfNull("Bad null Index");
    int cmp = name().compareTo(other.name());
    if(cmp == 0) {
      cmp = value().compareTo(other.value());
    }
    return (cmp == 0 ? record().compareTo(other.record()) : cmp);
  }
  
  
  public static <U extends Comparable<U>> Index<U> of(String name, U value, Record rec) {
    return new DefIndex<>(name, value, rec);
  }
  
  
  
  
  
  public static class DefIndex<T extends Comparable<T>> implements Index<T> {
    
    private final String name;
    
    private final T value;
    
    private final Record record;
    
    public DefIndex(String name, T value, Record rec) {
      this.name = NotNull.of(name).getOrFail("Bad null name");
      this.value = NotNull.of(value).getOrFail("Bad null value");
      this.record = NotNull.of(rec).getOrFail("Bad null Record");
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
    public Record record() {
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
