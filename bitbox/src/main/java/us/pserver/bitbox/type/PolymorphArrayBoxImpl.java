package us.pserver.bitbox.type;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import us.pserver.bitbox.ArrayBox;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.impl.BitBuffer;
import us.pserver.bitbox.transform.PolymorphNodeTransform;


public class PolymorphArrayBoxImpl implements ArrayBox<Object> {
  
  private final BitBuffer buffer;
  
  private final int startPos;
  
  private final int size;
  
  private final PolymorphNodeTransform transform;
  
  private final BitBoxConfiguration cfg;
  
  
  public PolymorphArrayBoxImpl(BitBuffer buf, BitBoxConfiguration cfg) {
    this.buffer = Objects.requireNonNull(buf);
    this.cfg = Objects.requireNonNull(cfg);
    this.startPos = buffer.position();
    this.size = buffer.getInt();
    this.transform = new PolymorphNodeTransform(cfg);
  }
  
  @Override
  public int size() {
    return size;
  }
  
  @Override
  public Class type() {
    return Object.class;
  }
  
  @Override
  public Object get(int idx) {
    buffer.position(startPos + Integer.BYTES * (1 + idx));
    int pos = buffer.getInt();
    return pos < 0 ? null : transform.unbox(buffer.position(pos));
  }
  
  @Override
  public BitBuffer getData() {
    return buffer;
  }
  
  @Override
  public Stream<Object> stream() {
    return IntStream.range(0, size).mapToObj(this::get);
  }
  
  @Override
  public Object[] getValue() {
    return stream().toArray();
  }
  
}
