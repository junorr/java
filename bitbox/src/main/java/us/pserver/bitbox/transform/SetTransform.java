package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SetTransform<T> implements BitTransform<Set<T>> {
  
  private final CollectionTransform<T> trans = new CollectionTransform<>();
  
  @Override
  public Predicate<Class> matching() {
    return Set.class::isAssignableFrom;
  }
  
  @Override
  public BiConsumer<Set<T>, BitBuffer> boxing() {
    return trans.boxing()::accept;
  }
  
  @Override
  public Function<BitBuffer, Set<T>> unboxing() {
    return b -> new HashSet<>(trans.unboxing().apply(b));
  }
  
}
