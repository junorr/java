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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import us.pserver.dbone.obj.Record;
import us.pserver.dbone.region.Region;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
public interface IndexStore {
  
  public IndexStore putIndex( Class cls, Index idx );
  
  public IndexStore putIndex( Record rec );
  
  
  public <V extends Comparable<V>> Stream<Index<V>> streamIndexes( Class cls, String name, V value );
  
  public <V extends Comparable<V>> Stream<Index<V>> streamIndexes( Class cls, String name, Predicate<V> tvl );
  
  public Stream<Index> streamIndexes( Class cls, Region reg );
  
  public Stream<Index> streamIndexes( Class cls, String name );
  
  public boolean containsIndex( Class cls, Index idx );
  
  
  public <V extends Comparable<V>> List<Index<V>> removeIndexes( Class cls, String name, V value );
  
  public <V extends Comparable<V>> List<Index<V>> removeIndexes( Class cls, String name, Predicate<V> tvl );
  
  public List<Index> removeIndexes( Class cls, String name );
  
  public List<Index> removeIndexes( Class cls, Region reg );
  
  
  
  public <T,R extends Comparable<R>> IndexStore appendIndexBuilder( Class cls, String name, Function<T,R> acessor );
  
  
  public IndexStore appendAnnotatedIndexBuilder( Class cls );
  
  public IndexStore appendAnnotatedIndexBuilder( Class cls, MethodHandles.Lookup lookup );
  
  
  public IndexStore appendReflectiveIndexBuilder( Class cls, String name );
  
  public IndexStore appendReflectiveIndexBuilder( Class cls, String name, MethodHandles.Lookup lookup );
  
  
  public boolean containsIndexBuilder( Class cls, String name );
  
  public IndexBuilder removeIndexBuilder( Class cls, String name );
  
  
  
  
  
  public static class DefIndexStore implements IndexStore {
    
    private final Map< String, List<Index> > indexes;
    
    private final Map< String, List<IndexBuilder> > builders;
    
    
    public DefIndexStore() {
      indexes = new ConcurrentSkipListMap<>();
      builders = new ConcurrentSkipListMap<>();
    }
    
    private String getKey(Class cls, String name) {
      return cls.getName().concat(".").concat(name);
    }
    
    @Override
    public IndexStore putIndex( Class cls, Index idx ) {
      Objects.requireNonNull(cls, "Bad null Class");
      Objects.requireNonNull(idx, "Bad null Index");
      String key = getKey(cls, idx.name());
      if(!indexes.containsKey(key)) {
        CopyOnWriteArrayList<Index> ids = new CopyOnWriteArrayList<>();
        ids.add(idx);
        indexes.put(key, ids);
      }
      else if(!containsIndex(cls, idx)) {
        indexes.get(key).add(idx);
      }
      return this;
    }
    
    @Override
    public IndexStore putIndex(Record rec) {
      Objects.requireNonNull(rec, "Bad null Record");
      String cname = rec.getValueClass().getName();
      if(!builders.containsKey(cname)
          || builders.get(cname).isEmpty()) {
        throw new IllegalStateException("IndexBuilder not found for: "+ cname);
      }
      builders.get(cname).stream()
          .map(i->i.build(rec.getValue(), rec.getRegion()))
          .forEach(i->putIndex(rec.getValueClass(), i));
      return this;
    }
    
    @Override
    public boolean containsIndex(Class cls, Index idx) {
      String key = getKey(cls, idx.name());
      return indexes.containsKey(key)
          && indexes.get(key).contains(idx);
    }
    
    @Override
    public <V extends Comparable<V>> Stream<Index<V>> streamIndexes( Class cls, String name, V value ) {
      return indexes.get(getKey(cls, name))
          .stream().filter(i->i.test(value))
          .map(i->(Index<V>)i);
    }
    
    @Override
    public <V extends Comparable<V>> Stream<Index<V>> streamIndexes( Class cls, String name, Predicate<V> tvl ) {
      return indexes.get(getKey(cls, name))
          .stream().filter(i->tvl.test((V)i.value()))
          .map(i->(Index<V>)i);
    }
    
    @Override
    public Stream<Index> streamIndexes(Class cls, Region reg) {
      Objects.requireNonNull(cls, "Bad null Class");
      Objects.requireNonNull(reg, "Bad null Region");
      return indexes.keySet().stream()
          .filter(k->k.startsWith(cls.getName()))
          .flatMap(k->indexes.get(k).stream())
          .filter(i->i.region().contains(reg));
    }

    @Override
    public Stream<Index> streamIndexes(Class cls, String name) {
      Objects.requireNonNull(cls, "Bad null Class");
      Objects.requireNonNull(name, "Bad null Name");
      String key = getKey(cls, name);
      if(!indexes.containsKey(key)) {
        return Stream.empty();
      }
      return indexes.get(key).stream();
    }
    
    @Override
    public <V extends Comparable<V>> List<Index<V>> removeIndexes( Class cls, String name, V value ) {
      Objects.requireNonNull(cls, "Bad null Class");
      Objects.requireNonNull(name, "Bad null Name");
      Objects.requireNonNull(value, "Bad null Value");
      String key = getKey(cls, name);
      List<Index> ids = indexes.get(key);
      if(ids == null || ids.isEmpty()) {
        return Collections.EMPTY_LIST;
      }
      List<Index<V>> rms = streamIndexes(cls, name, value)
          .collect(Collectors.toList());
      ids.removeAll(rms);
      return rms;
    }
    
    @Override
    public <V extends Comparable<V>> List<Index<V>> removeIndexes( Class cls, String name, Predicate<V> tvl ) {
      Objects.requireNonNull(cls, "Bad null Class");
      Objects.requireNonNull(name, "Bad null Name");
      Objects.requireNonNull(tvl, "Bad null Value");
      String key = getKey(cls, name);
      List<Index> ids = indexes.get(key);
      if(ids == null || ids.isEmpty()) {
        return Collections.EMPTY_LIST;
      }
      List<Index<V>> rms = streamIndexes(cls, name, tvl)
          .collect(Collectors.toList());
      ids.removeAll(rms);
      return rms;
    }
    
    @Override
    public List<Index> removeIndexes(Class cls, String name) {
      List<Index> rms = streamIndexes(cls, name).collect(Collectors.toList());
      if(!rms.isEmpty()) {
        indexes.get(getKey(cls, name)).removeAll(rms);
      }
      return rms;
    }

    @Override
    public List<Index> removeIndexes(Class cls, Region reg) {
      List<Index> rms = streamIndexes(cls, reg).collect(Collectors.toList());
      if(!rms.isEmpty()) {
        rms.forEach(i->indexes.get(getKey(cls, i.name())).remove(i));
      }
      return rms;
    }
    
    @Override
    public <T,R extends Comparable<R>> IndexStore appendIndexBuilder(Class cls, String name, Function<T,R> acessor) {
      Objects.requireNonNull(cls, "Bad null Class");
      Objects.requireNonNull(name, "Bad null Name");
      Objects.requireNonNull(acessor, "Bad null acessor Function");
      if(!builders.containsKey(cls.getName())) {
        CopyOnWriteArrayList<IndexBuilder> bds = new CopyOnWriteArrayList<>();
        bds.add(new AcessorIndexBuilder(name, acessor));
        builders.put(cls.getName(), bds);
      }
      else if(!containsIndexBuilder(cls, name)) {
        builders.get(cls.getName()).add(new AcessorIndexBuilder(name, acessor));
      }
      return this;
    }
    
    
    @Override
    public IndexStore appendAnnotatedIndexBuilder(Class cls) {
      return appendAnnotatedIndexBuilder(cls, MethodHandles.lookup());
    }
    
    @Override
    public IndexStore appendAnnotatedIndexBuilder(Class cls, MethodHandles.Lookup lookup) {
      Objects.requireNonNull(cls, "Bad null Class");
      Objects.requireNonNull(lookup, "Bad null Lookup");
      MethodHandleUtils.streamAnnotatedMethodHandlesWithName(cls, Indexed.class, lookup)
          .forEach(t->appendIndexBuilder(cls, t.key(), 
              new MethodHandleAcessor(t.value())));
      return this;
    }
    
    @Override
    public boolean containsIndexBuilder(Class cls, String name) {
      return builders.containsKey(cls.getName())
          && builders.get(cls.getName())
              .stream().anyMatch(b->b.name().equals(name));
    }
    
    @Override
    public IndexStore appendReflectiveIndexBuilder(Class cls, String name) {
      return appendReflectiveIndexBuilder(cls, name, MethodHandles.lookup());
    }
    
    @Override
    public IndexStore appendReflectiveIndexBuilder(Class cls, String name, MethodHandles.Lookup lookup) {
      Objects.requireNonNull(cls, "Bad null Class");
      Objects.requireNonNull(name, "Bad null Name");
      Objects.requireNonNull(lookup, "Bad null Lookup");
      Optional<MethodHandle> opt = MethodHandleUtils.getComparableMethodHandle(cls, name, lookup);
      if(!opt.isPresent()) {
        opt = MethodHandleUtils.getComparableFieldHandle(cls, name, lookup);
      }
      if(!opt.isPresent()) {
        throw new IllegalArgumentException(String.format(
            "Field/Method (%s.%s) not found", 
            cls.getSimpleName(), name)
        );
      }
      return this.appendIndexBuilder(cls, name, new MethodHandleAcessor(opt.get()));
    }
    
    
    @Override
    public IndexBuilder removeIndexBuilder(Class cls, String name) {
      if(!builders.containsKey(cls.getName())
          || builders.get(cls.getName()).isEmpty()) {
        return null;
      }
      return builders.get(cls.getName()).stream()
          .filter(b->b.name().equals(name))
          .findFirst().orElse(null);
    }
    
  }
  
}
