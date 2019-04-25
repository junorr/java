package us.pserver.bitbox.transform;

import java.util.List;
import java.util.Optional;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;


public class ListTransform<T> implements BitTransform<List<T>> {
  
  private final CollectionTransform<T> trans = new CollectionTransform<>();
  
  @Override
  public boolean match(Class c) {
    return List.class.isAssignableFrom(c);
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.of(List.class);
  }
  
  @Override
  public int box(List<T> l, BitBuffer buf) {
    return trans.box(l, buf);
  }
  
  @Override
  public List<T> unbox(BitBuffer buf) {
    return (List<T>) trans.unbox(buf);
  }
  
}
