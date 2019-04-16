package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.tools.Unchecked;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClassTransform implements BitTransform<Class> {
  
  private final CharSequenceTransform strans = new CharSequenceTransform();
  
  @Override
  public Predicate<Class> matching() {
    return c -> c == Class.class;
  }
  
  @Override
  public BiConsumer<Class, BitBuffer> boxing() {
    return (c,b) -> strans.boxing().accept(c.getName(), b);
  }
  
  @Override
  public Function<BitBuffer, Class> unboxing() {
    return b -> Unchecked.call(() ->
        BoxRegistry.INSTANCE.lookup().findClass(strans.unboxing().apply(b).toString())
    );
  }
  
}
