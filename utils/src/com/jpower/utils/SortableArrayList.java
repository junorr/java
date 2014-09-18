package com.jpower.utils;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author f6036477
 */
public class SortableArrayList
{

  private Map<Integer, SortableEntry> map;

  private int listsize;

  private int index;


  public SortableArrayList()
  {
    map = new Hashtable<Integer, SortableEntry>();
    index = 0;
    listsize = 0;
  }

  public boolean add(List l, Comparator c)
  {
    if(l == null || l.isEmpty() || c == null)
      return false;

    if(this.size() > 0) {
      if(l.size() != this.get(0).size())
        return false;
    }//if

    map.put(index++, new SortableEntry(l, c));
    listsize = l.size();
    return true;
  }

  public boolean addObjects(Object ... os)
  {
    if(os == null || os.length < map.size())
      return false;

    for(int i = 0; i < map.size(); i++) {
      map.get(i).getList().add(os[i]);
    }//for
    listsize = map.get(0).getList().size();
    return true;
  }

  public int listSize()
  {
    return listsize;
  }

  public Comparator getComparator(int index)
  {
    return map.get(index).getComparator();
  }

  public List get(int index)
  {
    if(index < 0 || index > map.size())
      return null;
    
    return map.get(index).getList();
  }

  public List remove(int index)
  {
    List l = this.get(index);
    if(l == null) return null;

    map.remove(index);
    return l;
  }

  public Object[] removeObjects(int index)
  {
    if(index < 0 || index > listsize)
      return null;

    Object[] os = new Object[map.size()];
    List list = null;
    for(int i = 0; i < map.size(); i++) {
      list = map.get(i).getList();
      os[i] = list.remove(index);
    }
    return os;
  }

  public Object[] getObjects(int index)
  {
    if(index < 0 || index > listsize)
      return null;

    Object[] os = new Object[map.size()];
    List list = null;
    for(int i = 0; i < map.size(); i++) {
      list = map.get(i).getList();
      os[i] = list.get(index);
    }
    return os;
  }

  public int size()
  {
    return map.size();
  }

  private void sortOthers(List l, int i, int j)
  {
    List list = null;
    Object o = null, n = null;
    for(int k = 0; k < map.size(); k++) {
      list = this.get(k);
      if(list.equals(l)) continue;
      o = list.get(j);
      n = list.get(i);
      list.set(j, n);
      list.set(i, o);
    }//for
  }

  public boolean sort(int index, boolean reverse)
  {
    List l = map.get(index).getList();
    Comparator c = map.get(index).getComparator();

    if(l == null || l.isEmpty())
      return false;
    if(c == null) return false;

    Object o = null;
    Object n = null;
    int j = 0;
    int x = 0;
    int mx = l.size();
    if(mx < 4) mx = 4;

    while(x < mx) {

      o = l.get(j);

      for(int i = 0; i < l.size(); i++) {

        n = l.get(i);

        if(j < i) {

          if(!reverse) {
            if(c.compare(o, n) > 0) {
              l.set(j, n);
              l.set(i, o);
              this.sortOthers(l, i, j);
              i = l.size();
            }//if
          } else {
            if(c.compare(o, n) < 0) {
              l.set(j, n);
              l.set(i, o);
              this.sortOthers(l, i, j);
              i = l.size();
            }//if
          }//else

        } else if(j > i) {

          if(!reverse) {
            if(c.compare(o, n) < 0) {
              l.set(j, n);
              l.set(i, o);
              this.sortOthers(l, i, j);
              i = l.size();
            }//if
          } else {
            if(c.compare(o, n) > 0) {
              l.set(j, n);
              l.set(i, o);
              this.sortOthers(l, i, j);
              i = l.size();
            }//if
          }//else

        }//else

      }//for

      j++;
      if(j >= l.size()) {
        j = 0;
        x++;
      }
    }//while

    return true;
  }


  private class SortableEntry
  {
    private List list;
    private Comparator comp;

    public SortableEntry()
    {
      list = null;
      comp = null;
    }
    public SortableEntry(List l, Comparator c)
    {
      list = l;
      comp = c;
    }
    public List getList()
    {
      return list;
    }
    public Comparator getComparator()
    {
      return comp;
    }
    public void setList(List l)
    {
      list = l;
    }
    public void setComparator(Comparator c)
    {
      comp = c;
    }
  }


  public static void print(List l)
  {
    System.out.print("[ ");
    for(int i = 0; i < l.size(); i++) {
      System.out.print(l.get(i));
      System.out.print(" ");
    }//for
    System.out.println("]");
  }

  public static void main(String[] args)
  {
    List l = new LinkedList();
    List m = new LinkedList();
    Comparator c = new Comparator()
    {
      public int compare(Object o1, Object o2) {
        if(o1 instanceof Integer && o2 instanceof Integer) {
          if(((Integer) o1).intValue() > (((Integer) o2).intValue()))
            return 1;
          else if(((Integer) o1).intValue() < (((Integer) o2).intValue()))
            return -1;
          else return 0;
        }//if
        return -1;
      }
    };

    l.add(6); l.add(4); l.add(7); l.add(9); l.add(1); l.add(3);
    l.add(10); l.add(8); l.add(5); l.add(2); l.add(11); l.add(12);
    l.add(17); l.add(16); l.add(14); l.add(13); l.add(18); l.add(15);
    l.add(20); l.add(22); l.add(19); l.add(24); l.add(21); l.add(23);
    l.add(25); l.add(28); l.add(27); l.add(26); l.add(30); l.add(29);

    m.add(6); m.add(4); m.add(7); m.add(9); m.add(1); m.add(3);
    m.add(10); m.add(8); m.add(5); m.add(2); m.add(11); m.add(12);
    m.add(17); m.add(16); m.add(14); m.add(13); m.add(18); m.add(15);
    m.add(20); m.add(22); m.add(19); m.add(24); m.add(21); m.add(23);
    m.add(25); m.add(28); m.add(27); m.add(26); m.add(30); m.add(29);

    SortableArrayList sl = new SortableArrayList();
    System.out.println("sl.add(l, c): "+ sl.add(l, c));
    System.out.println("sl.add(m, c): "+ sl.add(m, c));
    System.out.println("sl.listSize(): "+ sl.listSize());
    System.out.println("sl.size(): "+ sl.size());

    print(sl.get(0));
    print(sl.get(1));

    long time = System.currentTimeMillis();
    sl.sort(0, false);
    time = System.currentTimeMillis() - time;
    System.out.println( "Sort time: "+ time+ " ms" );

    print(sl.get(0));
    print(sl.get(1));

    time = System.currentTimeMillis();
    sl.sort(0, true);
    time = System.currentTimeMillis() - time;
    System.out.println( "Sort time: "+ time+ " ms" );

    print(sl.get(0));
    print(sl.get(1));

    time = System.currentTimeMillis();
    sl.sort(0, false);
    time = System.currentTimeMillis() - time;
    System.out.println( "Sort time: "+ time+ " ms" );

    print(sl.get(0));
    print(sl.get(1));
  }

}
