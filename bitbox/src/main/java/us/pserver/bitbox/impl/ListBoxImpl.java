package us.pserver.bitbox.impl;

import us.pserver.bitbox.ListBox;

import java.util.Objects;

public class ListBoxImpl<T> implements ListBox<T> {
  
  private final BitBuffer buffer;
  
  private final int startPos;
  
  public ListBoxImpl(BitBuffer buffer) {
    this.buffer = Objects.requireNonNull(buffer);
    this.startPos = buffer.position();
    
  }
  
  @Override
  public int size() {
    return 0;
  }
  
  @Override
  public Class<T> type() {
    return null;
  }
  
  @Override
  public T get(int idx) {
    return null;
  }
  
  @Override
  public BitBuffer getData() {
    return null;
  }
  
  @Override
  public List<T> getValue() {
    return null;
  }
}
