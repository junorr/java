package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapTransform<K,V> implements BitTransform<Map<K,V>> {
  
  private final ListTransform trans = new ListTransform();
  
  @Override
  public Predicate<Class> matching() {
    return Map.class::isAssignableFrom;
  }
  
  private static <K,V> Stream<Map.Entry<K,V>> combine(List<K> lk, List<V> lv) {
    int size = Math.min(lk.size(), lv.size());
    return IntStream.range(0, size)
        .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(lk.get(i), lv.get(i)));
  }
  
  @Override
  public BiConsumer<Map<K, V>, BitBuffer> boxing() {
    return (m,b) -> {
      int startPos = b.position();
      List<K> lk = new ArrayList<>(m.keySet());
      List<V> lv = new ArrayList<>();
      lk.forEach(k -> lv.add(m.get(k)));
      b.position(startPos + Integer.BYTES);
      trans.boxing().accept(lk, b);
      int vpos = b.position();
      trans.boxing().accept(lv, b);
      b.position(startPos).putInt(vpos);
    };
  }
  
  @Override
  public Function<BitBuffer, Map<K, V>> unboxing() {
    return b -> {
      int vpos = b.getInt();
      List<K> lk = (List<K>) trans.unboxing().apply(b);
      List<V> lv = (List<V>) trans.unboxing().apply(b.position(vpos));
      Map<K,V> m = new HashMap<>();
      combine(lk, lv).forEach(e -> m.put(e.getKey(), e.getValue()));
      return m;
    };
  }
  
}
