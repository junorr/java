package com.jpower.utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author f6036477
 */
public class TypeSortList 
    extends LinkedList
{

  private String methodGet;

  private CompareObjects comp;


  private class CompareObjects implements Comparator
  {
    private String method = "toString";

    public String getCompareMethod()
    {
      return method;
    }

    public void setCompareMethod(String compMethod)
    {
      this.method = compMethod;
    }

    //o1 < o2 = -1
    //o1 == o2 = 0
    //o1 > o2 = 1
    private int compareReturns(Object o1, Object o2)
    {
      if(o1 == null || o2 == null)
        return -99;

      if(o1 instanceof Double && o2 instanceof Double)
        return compare((Double) o1, (Double) o2);
      else if(o1 instanceof Integer && o2 instanceof Integer)
        return compare((Integer) o1, (Integer) o2);
      else
        return compare(o1.toString(), o2.toString());
    }

    private int compare(Double d1, Double d2)
    {
      return d1.compareTo(d2);
    }

    private int compare(Integer i1, Integer i2)
    {
      return i1.compareTo(i2);
    }

    private int compare(String s1, String s2)
    {
      return s1.compareToIgnoreCase(s2);
    }

    public int compare(Object o1, Object o2)
    {
      if(o1 == null || o2 == null)
        return -99;
      try {
        Method m1 = o1.getClass().getMethod(methodGet, (Class[]) null);
        Object r1 = m1.invoke(o1, (Object[]) null);
        Method m2 = o2.getClass().getMethod(methodGet, (Class[]) null);
        Object r2 = m2.invoke(o2, (Object[]) null);
        return this.compareReturns(r1, r2);
      } catch (Exception ex) {
        ex.printStackTrace();
        return -99;
      }
    }

  }//CompareObjects


  public TypeSortList()
  {
    super();
    methodGet = "toString";
    comp = new CompareObjects();
    comp.setCompareMethod(methodGet);
  }

  public String getCompareMethod()
  {
    return methodGet;
  }

  public void setCompareMethod(String compareMethod)
  {
    if(compareMethod == null || compareMethod.equals(""))
      return;

    methodGet = compareMethod;
    comp.setCompareMethod(compareMethod);
  }

  public void sort()
  {
    Collections.sort(this, comp);
  }


  public static void main(String[] args)
  {
    TypeSortList slist = new TypeSortList();
    slist.add("A");//A
    slist.add("z");//b
    slist.add("G");//G
    slist.add("b");//z

    for(Object o : slist)
      System.out.println("slist.pop(): "+ o.toString());

    System.out.println("slist.sort()");
    slist.sort();

    while(!slist.isEmpty())
      System.out.println("slist.pop(): "+ slist.pop());

    slist.add(10);
    slist.add(3);
    slist.add(6);
    slist.add(0);

    for(Object o : slist)
      System.out.println("slist.pop(): "+ o.toString());

    System.out.println("slist.setCompareMethod(\"intValue\");");
    slist.setCompareMethod("intValue");
    System.out.println("slist.sort()");
    slist.sort();

    while(!slist.isEmpty())
      System.out.println("slist.pop(): "+ slist.pop());
  }

}
