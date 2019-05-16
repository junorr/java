package us.pserver.bitbox.type;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import us.pserver.bitbox.BitBoxConfiguration;
import us.pserver.bitbox.BitBuffer;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.transform.ArrayTransform;


public class ArrayBoxImpl<T> implements ArrayBox<T> {
  
  private final BitBuffer buffer;
  
  private final int startPos;
  
  private final int size;
  
  private final Class<T> type;
  
  private final BitBoxConfiguration cfg;
  
  
  public ArrayBoxImpl(BitBuffer buf, BitBoxConfiguration cfg) {
    this.buffer = Objects.requireNonNull(buf);
    this.cfg = Objects.requireNonNull(cfg);
    this.startPos = buffer.position();
    this.size = buffer.getInt();
    buffer.position(startPos + Integer.BYTES * (size + 1));
    BitTransform<Class> ct = cfg.getTransform(Class.class);
    this.type = (Class<T>) ct.unbox(buffer);
  }
  
  @Override
  public int size() {
    return size;
  }
  
  @Override
  public Class<T> type() {
    return type;
  }
  
  @Override
  public T get(int idx) {
    buffer.position(startPos + Integer.BYTES * (1 + idx));
    int pos = buffer.getInt();
    BitTransform<T> transform = cfg.getTransform(type);
    return pos < 0 ? null : transform.unbox(buffer.position(pos));
  }
  
  @Override
  public BitBuffer getData() {
    return buffer;
  }
  
  @Override
  public Stream<T> stream() {
    return IntStream.range(0, size).mapToObj(this::get);
  }
  
  @Override
  public T[] getValue() {
    return new ArrayTransform<T>(cfg).unbox(buffer.position(startPos));
  }
  
}
