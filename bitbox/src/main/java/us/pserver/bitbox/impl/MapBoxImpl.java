package us.pserver.bitbox.impl;

import us.pserver.bitbox.ArrayBox;
import us.pserver.bitbox.MapBox;
import us.pserver.bitbox.transform.MapTransform;
import us.pserver.tools.Indexed;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapBoxImpl<K,V> implements MapBox<K,V> {
  
  private final BitBuffer buffer;
  
  private final int startPos;
  
  private final ArrayBox<K> keys;
  
  private final ArrayBox<V> vals;
  
  
  public MapBoxImpl(BitBuffer buffer) {
    this.buffer = Objects.requireNonNull(buffer);
    this.startPos = buffer.position();
    int vpos = buffer.getInt();
    this.keys = new ArrayBoxImpl<>(buffer.duplicate());
    this.vals = new ArrayBoxImpl<>(buffer.position(vpos).duplicate());
  }
  
  @Override
  public int size() {
    return keys.size();
  }
  
  @Override
  public Stream<K> keys() {
    return Stream.of(keys.getValue());
  }
  
  @Override
  public Stream<V> values() {
    return Stream.of(vals.getValue());
  }
  
  private static <K,V> Stream<Map.Entry<K,V>> combine(Stream<K> sk, Stream<V> sv) {
    List<K> lk = sk.collect(Collectors.toList());
    List<V> lv = sv.collect(Collectors.toList());
    int size = Math.min(lk.size(), lv.size());
    return IntStream.range(0, size)
        .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(lk.get(i), lv.get(i)));
  }
  
  @Override
  public Stream<Map.Entry<K, V>> entries() {
    return combine(keys(), values());
  }
  
  @Override
  public V get(K key) {
    Function<K, Indexed<K>> fid = Indexed.builder();
    int idx = keys().map(fid::apply)
        .filter(x -> key.equals(x.value()))
        .mapToInt(Indexed::index)
        .findAny().orElseThrow(IllegalArgumentException::new);
    return vals.get(idx);
  }
  
  @Override
  public boolean contains(K key) {
    return keys().anyMatch(Predicate.isEqual(key));
  }
  
  @Override
  public BitBuffer getData() {
    return buffer;
  }
  
  @Override
  public Map<K, V> getValue() {
    return new MapTransform<K,V>().unboxing().apply(buffer.position(startPos));
  }
  
}
