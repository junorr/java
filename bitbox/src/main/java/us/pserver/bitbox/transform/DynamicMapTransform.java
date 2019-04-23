/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.transform;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
  public boolean match(Class c) {
    return Map.class.isAssignableFrom(c);
  }


  @Override
  public Optional<Class> serialType() {
    return Optional.of(Map.class);
  }
  
  @Override
  public int box(Map m, BitBuffer b) {
    int len = Integer.BYTES;
    b.putInt(m.size());
    Set<Map.Entry> entries = m.entrySet();
    return entries.stream()
        .mapToInt(e -> etran.box(e, b))
        .reduce(len, Integer::sum);
  }


  @Override
  public Map unbox(BitBuffer b) {
    int size = b.getInt();
    return IntStream.range(0, size)
        .mapToObj(i -> etran.unbox(b))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
  
}
