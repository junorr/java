package com.jpower.utils.test;

/**
 *
 * @author Juno
 */
public class ArrayTest
{

  public static <T> Object toObject(T[] ts)
  {
    return (Object) ts;
  }

  public static <T> Object[] toArray(Object o)
  {
    Object[] os = (T[]) o;
    return os;
  }

  public static void print(Object[] os)
  {
    System.out.println("[Array] : size="+ os.length);
    System.out.print(" { ");
    for(Object o : os)
      if(o != os[os.length-1])
        System.out.print(o.toString() + ", ");
      else
        System.out.println(o.toString() + " }");
  }

  public static void print(Object o)
  {
    System.out.println("[Object] : "+ o);
  }

  public static void main(String[] args)
  {
    Integer[] is = { 144, 44 };
    print(is);
    Object o = toObject(is);
    print(o);
    Object[] os = toArray(o);
    print(os);
  }

}
