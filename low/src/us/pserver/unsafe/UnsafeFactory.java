/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.unsafe;

import com.jpower.rfl.Reflector;
import sun.misc.Unsafe;
import us.pserver.size.ObjectFactory;
import us.pserver.size.ObjectMeter;


/**
 *
 * @author juno
 */
public class UnsafeFactory {
  
  public static final Class CLASS_UNSAFE = Unsafe.class;
  
  public static final Unsafe UNSAFE = unsafe();
  
  
  public static Unsafe unsafe() {
    if(UNSAFE != null) return UNSAFE;
    
    Reflector ref = new Reflector();
    ref.on(CLASS_UNSAFE).constructor();
    
    if(!ref.isConstructorPresent())
      throw new IllegalStateException(
          "Unsafe empty constructor is not present");
    
    Unsafe usf = (Unsafe) ref.create();
    if(ref.hasError())
      throw new IllegalStateException(
          ref.getError().toString(), ref.getError());
    
    return usf;
  }
  
  
  public static void main(String[] args) {
    System.out.println(UNSAFE);
    //unsafe().throwException(new IOException("Throwed by Unsafe!"));
    //throw new IOException("Random IOException throw.");
    
    class AClass {
      String _str;
      int _int;
      boolean _bol;
      Object _obj;
      public String toString() {
        return "AClass { \n"
            + "  _str = " + _str + "\n"
            + "  _int = " + _int + "\n"
            + "  _bol = " + _bol + "\n"
            + "  _obj = " + _obj + "\n"
            + "}";
      }
    }
    
    AClass a = new AClass();
    a._bol = true;
    a._int = 6;
    a._obj = new AClass();
    a._str = "Some String";
    
    System.out.println(a);
    ObjectMeter meter = new ObjectMeter();
    long aclassSize = meter.sizeof(new ObjectFactory() {
      public Object create() {
        AClass a = new AClass();
        //a._bol = true;
        //a._int = 6;
        a._obj = new AClass();
        //a._str = "Some String";
        return a;
      }
    });
    System.out.println("* AClass size = "+ aclassSize);
    
    long addr = unsafe().allocateMemory(aclassSize + 2);
    System.out.println("* addr = "+ addr);
    AClass b = new AClass();
    b._str = "b string";
    Reflector ref = new Reflector();
    long _objAddr = unsafe().objectFieldOffset(
        ref.on(a).field("_obj").field());
    System.out.println("* _objAddr = "+ _objAddr);
    unsafe().putObject(a, _objAddr, b);
    System.out.println(a);
    unsafe().freeMemory(addr);
    //unsafe().putObject(null, addr, a);
  }
  
}
