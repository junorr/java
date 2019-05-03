package us.pserver.bitbox.transform;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BitBoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;

public class SetTransform<T> implements BitTransform<Set<T>> {
  
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
    BitTransform<Collection> trans = BitBoxRegistry.INSTANCE.getAnyTransform(Collection.class);
    return trans.box(s, buf);
  }
  
  @Override
  public Set<T> unbox(BitBuffer buf) {
    BitTransform<Collection> trans = BitBoxRegistry.INSTANCE.getAnyTransform(Collection.class);
    return new HashSet<>(trans.unbox(buf));
  }
  
}
