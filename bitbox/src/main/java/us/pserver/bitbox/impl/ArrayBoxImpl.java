package us.pserver.bitbox.impl;

import us.pserver.bitbox.ArrayBox;
import us.pserver.bitbox.BitTransform;
import us.pserver.bitbox.BoxRegistry;
import us.pserver.bitbox.transform.ArrayTransform;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class ArrayBoxImpl<T> implements ArrayBox<T> {
  
  private final BitBuffer buffer;
  
  private final int startPos;
  
  private final int size;
  
  private final Class<T> type;
  
  private final BitTransform<T> transform;
  
  
  public ArrayBoxImpl(BitBuffer buf) {
    this.buffer = Objects.requireNonNull(buf);
    this.startPos = buffer.position();
    this.size = buffer.getInt();
    int cpos = buffer.getInt();
    BitTransform<Class> ct = BoxRegistry.INSTANCE.getAnyTransform(Class.class);
    this.type = (Class<T>) ct.unboxing().apply(buffer.position(cpos));
    this.transform = BoxRegistry.INSTANCE.getAnyTransform(type);
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
    buffer.position(startPos + Integer.BYTES * (2 + idx));
    int pos = buffer.getInt();
    return transform.unboxing().apply(buffer.position(pos));
  }
  
  @Override
  public BitBuffer getData() {
    return buffer;
  }
  
  public Stream<T> stream() {
    return IntStream.range(0, size).mapToObj(this::get);
  }
  
  @Override
  public T[] getValue() {
    return new ArrayTransform<T>().unboxing().apply(buffer.position(startPos));
  }
  
}
