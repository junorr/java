package us.pserver.bitbox.impl;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamCombiner {
  
  public static <K,V> Stream<Map.Entry<K,V>> combine(Stream<K> sk, Stream<V> sv) {
    List<K> lk = sk.collect(Collectors.toList());
    List<V> lv = sv.collect(Collectors.toList());
    int size = Math.min(lk.size(), lv.size());
    return IntStream.range(0, size)
        .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(lk.get(i), lv.get(i)));
  }
  
}
