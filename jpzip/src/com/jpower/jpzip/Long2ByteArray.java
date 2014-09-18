package com.jpower.jpzip;


/**
 *
 * @author juno
 */
public class Long2ByteArray {
  
  public static final int LONG_STRING_SIZE = 19;
  
  public static final int LONG_SHIFT_SIZE = 8;
  
  
  public static void main(String[] args) {  
    long l = Long.MAX_VALUE;  
    byte[] bytes = toByteArray(l);  
    long l2 = fromByteArray(bytes, 0, bytes.length);  
    System.out.println("Size: "+bytes.length);
    System.out.println(l2);
    
    bytes = toStringBytes(l);
    System.out.println("Size: "+bytes.length);
    System.out.println("String: "+new String(bytes));
    System.out.println("Long  : "+fromStringBytes(bytes));
  }  
  
  
  public static byte[] toByteArray(long l) {  
    byte[] data = new byte[8];  
    for (int i=data.length-1; i >= 0; i--) {  
      data[i] = (byte) (l & 0xFF);  
      l >>= 8;  
    }  

    return data;  
  }
  
  
  public static byte[] toStringBytes(long l) {
    return String.valueOf(l).getBytes();
  }
  
  
  public static long fromStringBytes(byte[] bs) {
    System.out.println("fromStringBytes: "+new String(bs));
    return Long.parseLong(new String(bs));
  }
  
  
  public static long fromByteArray(byte[] data, int offset, int length) {  
    long l = 0;  
    int i = 0;  
    while (i < length) {  
      l <<= 8;  
      l += data[offset + i] & 0xFF;  
      i++;  
    }  
    return l;  
  }  
      
}
