package us.pserver.bitbox.transform;

import java.net.InetAddress;
import java.util.Optional;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.tools.Unchecked;

public class InetAddressTransform implements BitTransform<InetAddress> {
  
  private CharSequenceTransform stran = new CharSequenceTransform();
  
  @Override
  public boolean match(Class c) {
    return InetAddress.class.isAssignableFrom(c);
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(InetAddress a, BitBuffer buf) {
    return stran.box(a.getHostAddress(), buf);
  }
  
  @Override
  public InetAddress unbox(BitBuffer buf) {
    return Unchecked.call(() ->
        InetAddress.getByName(stran.unbox(buf).toString())
    );
  }
  
}
