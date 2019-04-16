package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListTransform<T> implements BitTransform<List<T>> {
  
  private final CollectionTransform<T> trans = new CollectionTransform<>();
  
  @Override
  public Predicate<Class> matching() {
    return List.class::isAssignableFrom;
  }
  
  @Override
  public BiConsumer<List<T>, BitBuffer> boxing() {
    return trans.boxing()::accept;
  }
  
  @Override
  public Function<BitBuffer, List<T>> unboxing() {
    return b -> (List<T>) trans.unboxing().apply(b);
  }
  
}
