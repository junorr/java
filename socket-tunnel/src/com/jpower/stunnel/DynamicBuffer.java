/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.stunnel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author juno
 */
public class DynamicBuffer {
  
  private ArrayList<Byte> buffer;
  
  private int index;
  
  
  private class Output extends OutputStream {
    @Override
    public void write(int b) throws IOException {
      buffer.add((byte) b);
    }
  }
  
  
  public DynamicBuffer() {
    buffer = new ArrayList<Byte>();
    index = 0;
  }
  
  
  public DynamicBuffer(int size) {
    if(size <= 0) size = 32;
    buffer = new ArrayList<Byte>(size);
    index = 0;
  }
  
  
  public DynamicBuffer reset() {
    index = 0;
    return this;
  }
  
  
  public int size() {
    return buffer.size();
  }
  
  
  public boolean isEmpty() {
    return buffer.isEmpty();
  }
  
  
  public Iterator<Byte> iterator() {
    return buffer.iterator();
  }
  
  
  public InputStream input() {
    if(buffer.isEmpty())
      return null;
    return new ByteArrayInputStream(this.toArray());
  }
  
  
  public OutputStream output() {
    return new Output();
  }
  
  
  public byte[] toArray() {
    if(buffer.isEmpty())
      return null;
    byte[] bs = new byte[this.size()];
    this.read(bs);
    return bs;
  }
  
  
  public DynamicBuffer clear() {
    buffer.clear();
    return this;
  }
  
  
  public int read() {
    if(index >= this.size())
      index = 0;
    return buffer.get(index++);
  }
  
  
  public boolean hasNext() {
    return index +1 < this.size();
  }
  
  
  public int read(byte[] bs) {
    if(bs == null) return -1;
    return this.read(bs, 0, bs.length);
  }
  
  
  public int read(int start, byte[] bs) {
    if(bs == null || bs.length == 0
        || start >= this.size()
        || start < 0)
      return -1;
    
    int max = Math.min((this.size() - start), bs.length);
    int idx = 0;
    for(int i = start; i < max; i++) {
      bs[idx++] = buffer.get(i);
    }
    return max;
  }
  
  
  public int read(byte[] bs, int start, int length) {
    if(bs == null || bs.length == 0 
        || start < 0 
        || start >= bs.length 
        || start >= length
        || this.isEmpty()
        || length > (bs.length - start))
      return -1;
    
    int max = length;
    for(int i = 0; i < max; i++) {
      bs[start++] = buffer.get(i);
    }
    return max;
  }
  
  
  public DynamicBuffer write(byte b) {
    buffer.add(b);
    return this;
  }
  
  
  public int write(byte[] bs) {
    if(bs == null) return -1;
    return this.write(bs, 0, bs.length);
  }
  
  
  public int write(byte[] bs, int start, int length) {
    if(bs == null || bs.length == 0
        || start < 0
        || start >= bs.length
        || start > length
        || length > (bs.length - start))
      return -1;
    
    int max = length;
    for(int i = 0; i < max; i++) {
      buffer.add(bs[i]);
    }
    return max;
  }
  
  
  private static void print(byte[] bs) {
    if(bs == null || bs.length == 0)
      return;
    System.out.print("[ ");
    for(int i = 0; i < bs.length; i++) {
      if(i > 0) System.out.print(", ");
      System.out.print(bs[i]);
    }
    System.out.println(" ]: "+ bs.length);
  }
  
}
