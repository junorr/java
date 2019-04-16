package us.pserver.bitbox.transform;

import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.tools.Unchecked;

import java.net.InetAddress;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class InetAddressTransform implements BitTransform<InetAddress> {
  
  @Override
  public Predicate<Class> matching() {
    return c -> InetAddress.class.isAssignableFrom(c);
  }
  
  @Override
  public BiConsumer<InetAddress, BitBuffer> boxing() {
    return (i,b) -> BoxRegistry.INSTANCE.getAnyTransform(String.class)
        .boxing().accept(i.getHostAddress(), b);
  }
  
  @Override
  public Function<BitBuffer, InetAddress> unboxing() {
    return b -> Unchecked.call(() ->
        InetAddress.getByName(BoxRegistry.INSTANCE
            .getAnyTransform(String.class)
            .unboxing().apply(b))
    );
  }
  
}
