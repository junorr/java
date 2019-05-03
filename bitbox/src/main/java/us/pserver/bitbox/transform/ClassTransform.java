package us.pserver.bitbox.transform;

import java.util.Optional;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BitBoxRegistry;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.tools.Unchecked;


public class ClassTransform implements BitTransform<Class> {
  
  private final CharSequenceTransform strans = new CharSequenceTransform();
  
  @Override
  public boolean match(Class c) {
    return c == Class.class;
  }
  
  @Override
  public Optional<Class> serialType() {
    return Optional.empty();
  }
  
  @Override
  public int box(Class c, BitBuffer buf) {
    //Logger.debug(c);
    return strans.box(c.getName(), buf);
  }
  
  @Override
  public Class unbox(BitBuffer buf) {
    Class c = Unchecked.call(() ->
        BitBoxRegistry.INSTANCE.lookup().findClass(strans.unbox(buf).toString())
    );
    //Logger.debug(c);
    return c;
  }
  
}
