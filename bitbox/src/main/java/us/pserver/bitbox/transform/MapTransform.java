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
  public boolean match(Class c) {
    return Map.class.isAssignableFrom(c);
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.of(Map.class);
  }
  
  private static <K,V> Stream<Map.Entry<K,V>> combine(List<K> lk, List<V> lv) {
    int size = Math.min(lk.size(), lv.size());
    return IntStream.range(0, size)
        .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(lk.get(i), lv.get(i)));
  }
  
  @Override
  public int box(Map<K, V> m, BitBuffer buf) {
    int startPos = buf.position();
    int len = Integer.BYTES;
    List<K> lk = new ArrayList<>(m.keySet());
    List<V> lv = new ArrayList<>();
    lk.forEach(k -> lv.add(m.get(k)));
    buf.position(startPos + Integer.BYTES);
    len += trans.box(lk, buf);
    int vpos = buf.position();
    len += trans.box(lv, buf);
    buf.putInt(startPos, vpos);
    buf.position(startPos + len);
    return len;
  }
  
  @Override
  public Map<K, V> unbox(BitBuffer buf) {
    int vpos = buf.getInt();
    List<K> lk = (List<K>) trans.unbox(buf);
    List<V> lv = (List<V>) trans.unbox(buf.position(vpos));
    Map<K,V> m = new HashMap<>();
    combine(lk, lv).forEach(e -> m.put(e.getKey(), e.getValue()));
    return m;
  }
  
}
