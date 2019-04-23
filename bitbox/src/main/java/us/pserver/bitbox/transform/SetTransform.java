package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SetTransform<T> implements BitTransform<Set<T>> {
  
  private final CollectionTransform<T> trans = new CollectionTransform<>();
  
  @Override
  public boolean match(Class c) {
    return Set.class.isAssignableFrom(c);
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.of(Set.class);
  }
  
  @Override
  public int box(Set<T> s, BitBuffer buf) {
    return trans.box(s, buf);
  }
  
  @Override
  public Set<T> unbox(BitBuffer buf) {
    return new HashSet<>(trans.unbox(buf));
  }
  
}
