package us.pserver.bitbox.transform;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;


public class ListTransform<T> implements BitTransform<List<T>> {
  
  private final BitBoxConfiguration cfg;
  
  public ListTransform(BitBoxConfiguration cfg) {
    this.cfg = Objects.requireNonNull(cfg);
  }
  
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
    BitTransform<Collection> trans = cfg.getTransform(Collection.class);
    return trans.box(l, buf);
  }
  
  @Override
  public List<T> unbox(BitBuffer buf) {
    BitTransform<Collection> trans = cfg.getTransform(Collection.class);
    return (List<T>) trans.unbox(buf);
  }
  
}
