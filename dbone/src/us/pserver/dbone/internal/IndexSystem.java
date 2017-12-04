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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import us.pserver.dbone.IndexBuilder;
import us.pserver.tools.NotNull;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
public interface IndexSystem {

  public void putIndex( Class cls, Index idx );
  
  public Stream<Index> getIndex( String name, Class cls, Object value );
  
  public void createIndex(Object obj, Record rec);
  
  public List<Index> removeIndex( String name, Class cls, Object value );
  
  public List<Index> removeIndex( String name, Class cls );
  
  public Stream< Index<String> > getUIDIndexes( Class cls );
  
  public void createIndexBuilder(String name, Class cls, Function acessor);
  
  public IndexBuilder removeIndexBuilder(String name, Class cls);
  
  
  
  
  
  public static class DefIndexSystem implements IndexSystem {
    
    private final Map< String, List<Index> > indexes;
    
    private final Map< String, List<IndexBuilder> > builders;
    
    
    public DefIndexSystem() {
      indexes = new ConcurrentSkipListMap<>();
      builders = new ConcurrentSkipListMap<>();
    }
    
    @Override
    public void putIndex( Class cls, Index idx ) {
      NotNull.of(cls).failIfNull("Bad null Class");
      NotNull.of(idx).failIfNull("Bad null Index");
      String key = cls.getName().concat(".").concat(idx.name());
      if(!indexes.containsKey(key)) {
        indexes.put(key, new CopyOnWriteArrayList<>());
      }
      indexes.get(key).add(idx);
    }
    
    @Override
    public Stream<Index> getIndex( String name, Class cls, Object value ) {
      return indexes.get(cls.getName().concat(".").concat(name))
          .stream().filter(i->i.test(value));
    }
    
    @Override
    public void createIndex(Object obj, Record rec) {
      NotNull.of(obj).failIfNull("Bad null Object");
      NotNull.of(rec).failIfNull("Bad null Record");
      String cname = obj.getClass().getName();
      if(!builders.containsKey(cname)
          || builders.get(cname).isEmpty()) {
        throw new IllegalStateException("IndexBuilder not found for: "+ cname);
      }
      builders.get(cname).stream()
          .map(i->i.build(obj, rec))
          .collect(Collectors.toList())
          .forEach(i->putIndex(obj.getClass(), i));
    }
    
    @Override
    public <V extends Comparable<V>> List<Index> removeIndex( String name, Class cls, V value ) {
      NotNull.of(name).failIfNull("Bad null name");
      NotNull.of(cls).failIfNull("Bad null Class");
      NotNull.of(value).failIfNull("Bad null Object");
      String key = cls.getName().concat(".").concat(name);
      List<Index> ids = indexes.get(key);
      if(ids == null || ids.isEmpty()) {
        return Collections.EMPTY_LIST;
      }
      List<Index> rms = ids.stream().filter(i->i.test(value)).collect(Collectors.toList());
      ids.removeAll(rms);
      return rms;
    }
    
    @Override
    public void createIndexBuilder(String name, Class cls, Function acessor) {
      NotNull.of(name).failIfNull("Bad null name");
      NotNull.of(cls).failIfNull("Bad null Class");
      NotNull.of(acessor).failIfNull("Bad null acessor Function");
      if(!builders.containsKey(cls.getName())) {
        builders.put(cls.getName(), new CopyOnWriteArrayList<>());
      }
      builders.get(cls.getName()).add(IndexBuilder.of(name, acessor));
    }
    
    @Override
    public IndexBuilder removeIndexBuilder(String name, Class cls) {
      if(!builders.containsKey(cls.getName())
          || builders.get(cls.getName()).isEmpty()) {
        return null;
      }
      return builders.get(cls.getName()).stream()
          .filter(b->b.name().equals(name))
          .findFirst().orElse(null);
    }
    
    @Override
    public Stream< Index<String> > getUIDIndexes( Class cls ) {
      return indexes.get(cls.getName().concat(".uid")).stream().map(i->(Index<String>)i);
    }
    
    
  }
  
}
