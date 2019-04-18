/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;


/**
 *
 * @author juno
 */
public class DynamicMapTransform implements BitTransform<Map>{
  
  private final DynamicEntryTransform etran = new DynamicEntryTransform();

  @Override
  public Predicate<Class> matching() {
    return Map.class::isAssignableFrom;
  }


  @Override
  public BiConsumer<Map, BitBuffer> boxing() {
    return (m,b) -> {
      b.putInt(m.size());
      Set<Map.Entry> entries = m.entrySet();
      entries.stream()
          .forEach(e -> etran.boxing().accept(e, b));
    };
  }


  @Override
  public Function<BitBuffer, Map> unboxing() {
    return b -> {
      int size = b.getInt();
      return IntStream.range(0, size)
          .mapToObj(i -> etran.unboxing().apply(b))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    };
  }
  
}
