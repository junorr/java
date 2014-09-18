package com.jpower.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juno Roesler
 */
public class StringUtil
{

  private String string;

  public StringUtil()
  {
    string = null;
  }

  public StringUtil(String s)
  {
    string = s;
  }

  public String getString()
  {
    return string;
  }

  public void setString(String s)
  {
    string = s;
  }

  public int indexOfIgnoreCase(String s)
  {
    if(s == null) return -1;

    int end = 0,
        index = -1;
    for(int i = 0; i < string.length(); i++)
    {
      end = s.length() + i;
      if(end > string.length())
        break;
      String temp = string.substring(i, end);
      if(s.equalsIgnoreCase(temp)) {
        index = i;
        break;
      }
    }//for
    return index;
  }

  public boolean containsIgnoreCase(String s)
  {
    return this.indexOfIgnoreCase(s) >= 0;
  }


  public String[] split(String regex)
  {
    if(regex == null) return null;
    if(string == null) return null;

    List<String> list = new ArrayList<String>();
    int i = 0, ia = 0;
    while((i = string.indexOf(regex, i)) >= 0) {
      if(i == 0 || i >= string.length()) {
        i++;
        continue;
      }//if
      list.add(string.substring(ia, i));
      ia = i+1;
      i++;
    }
    list.add(string.substring(ia));

    String[] ss = new String[list.size()];
    return list.toArray(ss);
  }


  public String[] split(int forindex)
  {
    if(forindex <= 0) return null;
    if(forindex >= string.length()) return null;

    int parts = string.length() / forindex;
    if(string.length() % forindex != 0)
      parts++;

    String[] split = new String[parts];
    int limit = string.length() - string.length() % forindex;

    int n = -1;
    for(int i = 0; i < limit; i += forindex)
      split[++n] = string.substring(i, i+forindex);

    if(n < parts -1)
      split[++n] = string.substring(forindex * n);

    return split;
  }

  public String replaceAll(String regex, String replace)
  {
    if(regex == null || (regex.equals("") ||
        regex.length() > string.length()))
      return null;

    String ns = "";
    for(int i = 0; i < string.length(); i += regex.length())
    {
      int end = (i + regex.length() > string.length() ?
          string.length() : i + regex.length());

      String s = string.substring(i, end);

      if(s.equalsIgnoreCase(regex))
        s = replace;
      
      ns += s;
    }//for

    string = ns;
    return string;
  }


  public static void main(String[] args)
  {
    StringUtil su = new StringUtil("Recibos");
    String[] split = su.split(2);
    System.out.println( "split.length: " + split.length );
    for(int i = 0; i < split.length; i++)
    {
      System.out.println( "split[" + i + "]: " + split[i] );
    }//for

    System.out.println( su.getString() +
        " containsIgnoreCase(\"ReCiBoS\"): " +
        su.containsIgnoreCase("ReCiBoS"));

    System.out.println( su.getString() +
        " indexOfIgnoreCase(\"ReCiBoS\"): " +
        su.indexOfIgnoreCase("ReCiBoS"));

    su.setString("R$  175,40");
    System.out.println( su.getString() +
        " replaceAll(\",\", \".\"): " +
        su.replaceAll(",", "."));

    System.out.println( su.getString() +
        " replaceAll(\"R$  \", \"\"): " +
        su.replaceAll("R$  ", ""));

    System.out.println(" - - - - - - -");
    System.out.println("su.setString(a?b?c?d?e)");
    su.setString("a?b?c?d?e");
    System.out.println("su.split(\"?\")");
    String[] ss = su.split("?");
    for(String s : ss)
      System.out.print(s + "  ");
  }

}
