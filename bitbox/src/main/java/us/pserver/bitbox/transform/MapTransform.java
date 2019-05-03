package us.pserver.bitbox.transform;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;

public class MapTransform<K,V> implements BitTransform<Map<K,V>> {
  
  private final BitBoxConfiguration cfg;
  
  public MapTransform(BitBoxConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg);
  }
  
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
    BitTransform<List> trans = cfg.getTransform(List.class);
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
    BitTransform<List> trans = cfg.getTransform(List.class);
    List<K> lk = (List<K>) trans.unbox(buf);
    List<V> lv = (List<V>) trans.unbox(buf.position(vpos));
    Map<K,V> m = new HashMap<>();
    combine(lk, lv).forEach(e -> m.put(e.getKey(), e.getValue()));
    return m;
  }
  
}
