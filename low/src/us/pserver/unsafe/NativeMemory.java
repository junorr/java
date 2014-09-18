/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.unsafe;

import sun.misc.Unsafe;


/**
 *
 * @author juno
 */
public class NativeMemory implements Primitives {
  
  private Unsafe unsafe;
  
  private long address;
  
  private long size;
  
  private long lastIndex;
  
  
  public NativeMemory() {
    unsafe = UnsafeFactory.UNSAFE;
    address = -1;
    size = 0;
    lastIndex = 0;
  }
  
  
  public NativeMemory(long address, long size) {
    this();
    if(address <= 0) 
      throw new IllegalArgumentException(
          "Invalid memory address="+ address);
    if(size < 1) 
      throw new IllegalArgumentException(
          "Invalid memory size="+ size);
    unsafe.reallocateMemory(address, size);
    this.address = address;
    this.size = size;
  }
  
  
  public static NativeMemory malloc(long size) {
    if(size < 1) 
      throw new IllegalArgumentException(
          "Invalid memory size="+ size);
    NativeMemory m = new NativeMemory();
    m.alloc(size);
    return m;
  }
  
  
  public static NativeMemory openAddress(long address, long size) {
    return new NativeMemory(address, size);
  }
  
  
  public long alloc(long size) {
    if(address != -1)
      throw new IllegalStateException(
          "Memory already created at address="+ address);
    if(size < 1) 
      throw new IllegalArgumentException(
          "Invalid memory size="+ size);
    address = unsafe.allocateMemory(size);
    this.size = size;
    return address;
  }
  
  
  public long address() { return address; }
  
  public long size() { return size; }
  
  public long index() { return lastIndex; }
  
  
  public void free() {
    if(address == -1) return;
    unsafe.freeMemory(address);
    address = -1;
    size = 0;
  }
  
  
  public long realloc(long size) {
    if(size < 1) 
      throw new IllegalArgumentException(
          "Invalid memory size="+ size);
    
    if(size == this.size) return address;
    if(address == -1) return alloc(size);
    
    address = unsafe.reallocateMemory(address, size);
    this.size = size;
    return address;
  }
  
  
  private void checkInitialized() {
    if(address == -1)
      throw new IllegalStateException(
          "Memory not initilized [addr = "+ address+"]");
  }
  
  
  private void checkIndex(long index, int typeSize) {
    if(index < 0)
      throw new IllegalArgumentException(
          "Invalid index ["+ index+ "]");
    long len = (index+1) * typeSize;
    /*
    if(len > size)
      throw new IllegalArgumentException(
          "Insuficient space for length ["+ len+ "]");*/
  }
  
  
  private long addressOf(long index, int typeSize) {
    lastIndex = index;
    if(index == 0) return address;
    else return address + index * typeSize;
  }
  
  
  public void putInt(int i, long index) {
    checkInitialized();
    checkIndex(index, SIZE_INT);
    unsafe.putInt(addressOf(index, SIZE_INT), i);
  }
  
  
  public int getInt(long index) {
    checkInitialized();
    checkIndex(index, SIZE_INT);
    return unsafe.getInt(addressOf(index, SIZE_INT));
  }
  
  
  public void putByte(byte i, long index) {
    checkInitialized();
    checkIndex(index, SIZE_BYTE);
    unsafe.putByte(addressOf(index, SIZE_BYTE), i);
  }
  
  
  public byte getByte(long index) {
    checkInitialized();
    checkIndex(index, SIZE_BYTE);
    return unsafe.getByte(addressOf(index, SIZE_BYTE));
  }
  
  
  public void putChar(char i, long index) {
    checkInitialized();
    checkIndex(index, SIZE_CHAR);
    unsafe.putChar(addressOf(index, SIZE_CHAR), i);
  }
  
  
  public int getChar(long index) {
    checkInitialized();
    checkIndex(index, SIZE_CHAR);
    return unsafe.getChar(addressOf(index, SIZE_CHAR));
  }
  
  
  public void putLong(long i, long index) {
    checkInitialized();
    checkIndex(index, SIZE_LONG);
    unsafe.putLong(addressOf(index, SIZE_LONG), i);
  }
  
  
  public long getLong(long index) {
    checkInitialized();
    checkIndex(index, SIZE_LONG);
    return unsafe.getLong(addressOf(index, SIZE_LONG));
  }
  
  
  public void putFloat(float i, long index) {
    checkInitialized();
    checkIndex(index, SIZE_FLOAT);
    unsafe.putFloat(addressOf(index, SIZE_FLOAT), i);
  }
  
  
  public float getFloat(long index) {
    checkInitialized();
    checkIndex(index, SIZE_FLOAT);
    return unsafe.getFloat(addressOf(index, SIZE_FLOAT));
  }
  
  
  public void putDouble(double i, long index) {
    checkInitialized();
    checkIndex(index, SIZE_DOUBLE);
    unsafe.putDouble(addressOf(index, SIZE_DOUBLE), i);
  }
  
  
  public double getDouble(long index) {
    checkInitialized();
    checkIndex(index, SIZE_DOUBLE);
    return unsafe.getDouble(addressOf(index, SIZE_DOUBLE));
  }
  
  
  public void putString(String str, long index) {
    if(str == null || str.isEmpty())
      return;
    checkInitialized();
    checkIndex(index, SIZE_CHAR * str.length());
    char[] cs = str.toCharArray();
    for(int i = 0; i < cs.length; i++) {
      unsafe.putChar(addressOf(index + i, SIZE_CHAR), cs[i]);
    }
  }
  
  
  public String getString(long index, int length) {
    if(length < 1) return null;
    checkInitialized();
    checkIndex(index, SIZE_CHAR * length);
    char[] cs = new char[length];
    for(int i = 0; i < length; i++) {
      cs[i] = unsafe.getChar(addressOf(index + i, SIZE_CHAR));
    }
    return new String(cs);
  }
  
  
  @Override
  public String toString() {
    return "NativeMemory{ addr="+ address+ ", size="+ size+ " }";
  }
  
  
  public static void main(String[] args) throws InterruptedException {
    NativeMemory mem = malloc(1);
    NativeMemory mem2 = openAddress(mem.address(), mem.size());
    System.out.println(mem2);
    
    System.out.println(mem);
    Unsafe unsafe = UnsafeFactory.UNSAFE;
    System.out.println("unsafe.addressSize()="+unsafe.addressSize());
    
    long index = 0;
    int value = 500;
    System.out.println("* mem.putInt( "+ value+ ", "+ index+ " )");
    mem.putInt(value, index);
    
    index = 1;
    value = 100;
    System.out.println("* mem.putInt( "+ value+ ", "+ index+ " )");
    mem.putInt(value, index);
    value = 0;
    
    index = 2;
    value = 300;
    System.out.println("* mem.putInt( "+ value+ ", "+ index+ " )");
    mem.putInt(value, index);
    value = 0;
    
    index = 0;
    System.out.println("* mem2.getInt( "+ index+ " ) = "
        + (value = mem2.getInt(index)));
    
    index = 1;
    System.out.println("* mem2.getInt( "+ index+ " ) = "
        + (value = mem2.getInt(index)));
    
    index = 2;
    System.out.println("* mem2.getInt( "+ index+ " ) = "
        + (value = mem2.getInt(index)));
    
    
    index = 0;
    String str = "abcdefghijkl";
    System.out.println("* str = '"+ str+ "'");
    System.out.println("* mem.putString( "+ str+ ", "+ index+ " )");
    mem.putString(str, index);
    
    int length = str.length();
    System.out.println("* mem.getString( "+ index+ ", "+ length
        + " ) = " + (str = mem.getString(index, length)));
    
    //Thread.sleep(20000);
    mem2.free();
    mem.free();
    System.out.println(mem);
  }
  
}
