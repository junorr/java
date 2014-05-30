/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.size;

import java.util.Arrays;

/**
 *
 * @author Juno
 */
public class ObjectMeter {
  
  public static final int DEFAULT_NUM_OBJECTS = 1000;
  
  private final Runtime runtime;
  
  
  public ObjectMeter() {
    runtime = Runtime.getRuntime();
  }
  
  
  public long sizeof(ObjectFactory fact) {
    if(fact == null) return -1;
    gc();
    long mem1 = usedMem();
    Object[] objs = populate(fact);
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofChar() {
    gc();
    long mem1 = usedMem();
    char[] objs = populateChar();
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofByte() {
    gc();
    long mem1 = usedMem();
    byte[] objs = populateByte();
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofShort() {
    gc();
    long mem1 = usedMem();
    short[] objs = populateShort();
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofBool() {
    gc();
    long mem1 = usedMem();
    boolean[] objs = populateBool();
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofInt() {
    gc();
    long mem1 = usedMem();
    int[] objs = populateInt();
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofLong() {
    gc();
    long mem1 = usedMem();
    long[] objs = populateLong();
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofFloat() {
    gc();
    long mem1 = usedMem();
    float[] objs = populateFloat();
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofDouble() {
    gc();
    long mem1 = usedMem();
    double[] objs = populateDouble();
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public long sizeofString(String str) {
    gc();
    long mem1 = usedMem();
    String[] objs = populateString(str);
    gc();
    long mem2 = usedMem();
    return Math.round((mem2 - mem1)/DEFAULT_NUM_OBJECTS);
  }
  
  
  public void gc() {
    for(int i = 0; i < 4; i++) {
      stabilizeGC();
    }
  }
  
  
  private Object[] populate(ObjectFactory fact) {
    if(fact == null) return null;
    Object[] objs = new Object[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = fact.create();
    }
    return objs;
  }
  
  
  private byte[] populateByte() {
    byte[] objs = new byte[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = (byte) (Math.random() * 1000);
    }
    return objs;
  }
  
  
  private boolean[] populateBool() {
    boolean[] objs = new boolean[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      short rdm = (short) (Math.random() * 1);
      objs[i] = (rdm == 0 ? false : true);
    }
    return objs;
  }
  
  
  private short[] populateShort() {
    short[] objs = new short[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = (short) (Math.random() * 1000);
    }
    return objs;
  }
  
  
  private int[] populateInt() {
    int[] objs = new int[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = (int) (Math.random() * 1000);
    }
    return objs;
  }
  
  
  private char[] populateChar() {
    char[] objs = new char[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = (char) (Math.random() * 1000);
    }
    return objs;
  }
  
  
  private long[] populateLong() {
    long[] objs = new long[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = (long) (Math.random() * 1000);
    }
    return objs;
  }
  
  
  private float[] populateFloat() {
    float[] objs = new float[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = (float) (Math.random() * 1000);
    }
    return objs;
  }
  
  
  private double[] populateDouble() {
    double[] objs = new double[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = Math.random() * 1000.0;
    }
    return objs;
  }
  
  
  private String[] populateString(String str) {
    if(str == null || str.isEmpty()) 
      return new String[0];
    char[] chars = str.toCharArray();
    String[] objs = new String[DEFAULT_NUM_OBJECTS];
    for(int i = 0; i < DEFAULT_NUM_OBJECTS; i++) {
      objs[i] = new String(Arrays.copyOf(chars, chars.length));
    }
    chars = null;
    return objs;
  }
  
  
  private void stabilizeGC() {
    long usedMem1 = usedMem(), usedMem2 = Long.MAX_VALUE;
    for(int i = 0; (usedMem1 < usedMem2) && (i < DEFAULT_NUM_OBJECTS); ++ i) {
      runtime.runFinalization();
      runtime.gc ();
      Thread.currentThread().yield ();
            
      usedMem2 = usedMem1;
      usedMem1 = usedMem();
    }  
  }
  
  
  public long usedMem() {
    return runtime.totalMemory() - runtime.freeMemory();
  }
  
  
  public static void main(String[] args) {
    ObjectMeter meter = new ObjectMeter();
    
    System.out.println("Size of objects and primitives");
    System.out.println("---------------------");
    System.out.println("[OS  Name: "+ System.getProperty("os.name")+ "]");
    System.out.println("[OS  Arch: "+ System.getProperty("os.arch")+ "]");
    System.out.println("[JVM Bits: "+ System.getProperty("sun.arch.data.model")+ "]");
    System.out.println("---------------------");
    
    System.out.println("* byte   =  "+ meter.sizeofByte());
    System.out.println("* bool   =  "+ meter.sizeofBool());
    System.out.println("* char   =  "+ meter.sizeofChar());
    System.out.println("* short  =  "+ meter.sizeofShort());
    System.out.println("* int    =  "+ meter.sizeofInt());
    System.out.println("* float  =  "+ meter.sizeofFloat());
    System.out.println("* long   =  "+ meter.sizeofLong());
    System.out.println("* double =  "+ meter.sizeofDouble());
    System.out.println("* Object = "+ meter.sizeof(new ObjectFactory() {
      public Object create() {
        return new Object();
      }
    }));
    System.out.println("* \"Hello World!\" = "+ meter.sizeofString("Hello World!"));
  }
  
}
