/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.impl;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import us.pserver.bitbox.MapBox;
import us.pserver.bitbox.transform.DynamicEntryTransform;


/**
 *
 * @author juno
 */
public class DynamicMapBox implements MapBox<Object,Object> {
  
  private final DynamicEntryTransform etran = new DynamicEntryTransform();
  
  private final BitBuffer buffer;
  
  private final int startPos;
  
  private final int size;
  
  public DynamicMapBox(BitBuffer buf) {
    this.buffer = Objects.requireNonNull(buf);
    this.startPos = buffer.position();
    this.size = buffer.getInt(startPos);
  }

  @Override
  public int size() {
    return size;
  }


  @Override
  public Stream<Object> keys() {
    buffer.position(startPos + Integer.BYTES);
    return IntStream.range(0, size)
        .mapToObj(i -> etran.unboxing().apply(buffer))
        .map(Map.Entry::getKey);
  }


  @Override
  public Stream<Object> values() {
    buffer.position(startPos + Integer.BYTES);
    return IntStream.range(0, size)
        .mapToObj(i -> etran.unboxing().apply(buffer))
        .map(Map.Entry::getValue);
  }


  @Override
  public Stream<Map.Entry<Object,Object>> entries() {
    buffer.position(startPos + Integer.BYTES);
    return IntStream.range(0, size)
        .mapToObj(i -> etran.unboxing().apply(buffer));
  }


  @Override
  public Object get(Object key) {
    return entries().filter(e -> key.equals(e.getKey()))
        .map(Map.Entry::getValue)
        .findAny()
        .orElse(null);
  }


  @Override
  public boolean contains(Object key) {
    return entries().anyMatch(e -> key.equals(e.getKey()));
  }


  @Override
  public BitBuffer getData() {
    return buffer;
  }


  @Override
  public Map<Object,Object> getValue() {
    return entries().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
  
}
