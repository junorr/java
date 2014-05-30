package com.jpower.utils;

import java.util.Scanner;

/**
 *
 * @author Juno
 */
public class IndexedString
{

  protected static class EQUALS_METHOD { }

  protected static final class ACCURACY_METHOD extends EQUALS_METHOD { }

  protected static final class DEFAULT_METHOD extends EQUALS_METHOD { }

  protected static final class REGEX_METHOD extends EQUALS_METHOD { }


  public static final ACCURACY_METHOD EQUALS_ACCURACY = new ACCURACY_METHOD();

  public static final DEFAULT_METHOD EQUALS_DEFAULT = new DEFAULT_METHOD();

  public static final REGEX_METHOD EQUALS_REGEX = new REGEX_METHOD();

  public static final double
      ACCURACY_100 = 1.0,
      ACCURACY_90 = 0.9,
      ACCURACY_80 = 0.8,
      ACCURACY_70 = 0.7,
      ACCURACY_60 = 0.6,
      ACCURACY_50 = 0.5,
      ACCURACY_40 = 0.4,
      ACCURACY_30 = 0.3,
      ACCURACY_20 = 0.2,
      ACCURACY_10 = 0.1,
      ACCURACY_5 = 0.05;


  private String text;

  private EQUALS_METHOD method;

  private double defaccuracy;

  private double lastaccuracycompare;


  public IndexedString(String text)
  {
    this.text = text;
    method = EQUALS_ACCURACY;
    defaccuracy = ACCURACY_70;
    lastaccuracycompare = -1;
  }


  public IndexedString()
  {
    text = null;
  }


  public String getText()
  {
    return text;
  }


  public StringUtil toStringUtil()
  {
    return new StringUtil(text);
  }


  public double getLastAccuracyCompare()
  {
    return lastaccuracycompare;
  }


  public void setText(String text)
  {
    this.text = text;
  }


  public EQUALS_METHOD getEqualsMethod()
  {
    return method;
  }


  public void setEqualsMethod(EQUALS_METHOD method)
  {
    this.method = method;
  }


  public double getDefaultAccuracy()
  {
    return defaccuracy;
  }


  public void setDefaultAccuracy(double acc)
  {
    if(acc < 0.0 || acc > 1.0) return;
    defaccuracy = acc;
  }


  @Override
  public boolean equals(Object o)
  {
    if(o == null
        || (!(o instanceof String)
        && !(o instanceof IndexedString)))
      return false;

    IndexedString compare;
    if(o instanceof IndexedString)
      compare = (IndexedString) o;
    else
      compare = new IndexedString(o.toString());

    if(method == EQUALS_ACCURACY) {
      return this.accuracyCompare(compare.getText(), defaccuracy, false);
    }
    else if(method == EQUALS_REGEX) {
      return this.regexCompare(compare.getText());
    }
    else {
      return this.hashCode() == compare.hashCode();
    }
  }

  
  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = (int) (47 * hash + (this.text != null
        ? this.text.hashCode()
        : this.method.hashCode() * defaccuracy));
    return hash;
  }


  @Override
  public String toString()
  {
    return "[IndexedString]: "+ text;
  }


  public boolean regexCompare(String text)
  {
    if(text == null) return false;

    StringUtil utext = new StringUtil(text);

    String[] parts = new String[] { text };
    if(utext.containsIgnoreCase("?")) {
      parts = utext.split("?");
    }

    for(int i = 0; i < parts.length; i++) {
      if(i > 0 && !parts[i].startsWith("*")) {
        parts[i] = "*" + parts[i];
      }
      if(i < parts.length-1 && !parts[i].endsWith("*")) {
        parts[i] = parts[i] + "*";
      }

      if(parts[i].indexOf('*') >= 0)
        if(!this.compareAny(parts[i]))
          return false;
    }//for

    return true;
  }


  private boolean compareAny(String text)
  {
    if(text == null || text.indexOf('*') < 0)
      return false;

    if(text.indexOf('*') == 0 && !text.endsWith("*")) {
      return this.text.endsWith(new StringUtil(text).replaceAll("*", ""));
    }
    else if(text.indexOf('*') != 0 && text.endsWith("*")) {
      return this.text.startsWith(new StringUtil(text).replaceAll("*", ""));
    }
    else if(text.indexOf('*') == 0 && text.endsWith("*")) {
      return new StringUtil(this.text)
          .containsIgnoreCase(
          new StringUtil(text).replaceAll("*", ""));
    }
    else {
      return false;
    }
  }


  public boolean accuracyCompare(String text, double accuracy, boolean withlength)
  {
    double percent = this.accuracyCompare(text, withlength);
    System.out.println(percent+ " >= "+ accuracy);
    return percent >= accuracy;
  }


  public double accuracyCompare(String text, boolean withlength)
  {
    if(text == null)
      return -1;

    if(new StringUtil(this.text).containsIgnoreCase(text))
      return 1.0;

    int equals = 0;
    int errors = 0;
    int max = Math.min(this.text.length(), text.length());
    int count = 0;

    for(int i = 0; i < max; i++) {
      char a = text.charAt(i);
      boolean eq = false;
      int errorparc = 0;

      for(int j = count; j < this.text.length(); j++) {
        char c = this.text.charAt(j);
        //System.out.print(a + " == " + c);
        if(a == c) {
          //System.out.print(" : OK");
          equals++;
          count++;
          eq = true;
          j = this.text.length();
        } else errorparc++;
        //System.out.println();
      }//for
      
      if(eq && errorparc > 4) {
        //System.out.println("error: "+ a);
        //System.out.println("errorparc: "+ errorparc);
        //count--;
        errors++;
      }
    }//for
    /*
    System.out.println();
    System.out.println("text.length: "+ this.text.length());
    System.out.println("max: "+ max);
    System.out.println("equals: "+ equals);
    System.out.println("errors: "+ errors);
    System.out.println("erros : "+
        (text.length() > this.text.length()
           ? (text.length() - this.text.length()) / (double) text.length()
           : (this.text.length() - text.length()) / (double) this.text.length()));
    System.out.println("acertos: "+ equals / (double) max);
    System.out.println();
    */
    double percent = (equals - errors)/ (double) max
        - (withlength ?
        (text.length() > this.text.length()
           ? (text.length() - this.text.length()) / (double) text.length()
           : (this.text.length() - text.length()) / (double) this.text.length()) : 0);

    lastaccuracycompare = percent;
    return percent;
  }


  public static void main(String[] args)
  {
    System.out.println("Sintaxe: <IndexedString> <pesquisa> [percentual]");

    Scanner sc = new Scanner(System.in);
    System.out.print("Informe o texto da IndexedString: ");
    IndexedString fin = new IndexedString(sc.nextLine());
    System.out.println(fin);

    System.out.print("Informe o texto da pesquisa: ");
    String t = sc.nextLine();

    System.out.print("Informe o tipo de pesquisa (regex|accuracy) [regex]: ");
    String tipo = sc.nextLine();

    if(tipo.equals("accuracy")) {
      System.out.print("Informe o percentual de semelhança (0.0 - 1.0): ");
      double d = Double.parseDouble(sc.nextLine());
      System.out.print("IndexedString.accuracyLike( "+ t+ " ): ");
      System.out.println(fin.accuracyCompare(t, false));
    }//if
    else {
      System.out.print("IndexedString.regexLike( "+ t+ " ): ");
      System.out.println(fin.regexCompare(t));
    }
  }

}
