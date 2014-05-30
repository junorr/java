package com.jpower.utils;

/**
 *
 * @author Juno
 */
public class NumberExpression extends Expression
{

  private double value;

  private StringBuffer buf;

  public NumberExpression()
  {
    value = 0;
    buf = new StringBuffer();
  }

  public NumberExpression(double value)
  {
    this();
    this.value = value;
  }

  public void setValue(double value)
  {
    this.value = value;
  }

  public double getValue()
  {
    return value;
  }

  public void append(char c)
  {
    buf.append(c);
  }

  public void append(String s)
  {
    buf.append(s);
  }

  @Override
  public void init()
  {
    char c = 0;
    int count = 0;
    String s = buf.toString();
    buf.delete(0, buf.length());
    c = s.charAt(count);
    while(count < s.length() && Character.isDigit(c) || c == '.') {
      c = s.charAt(count);
      append(c);
      count++;
    }
    this.parse();
  }

  public void init(String sign)
  {
    this.append(sign);
    this.init();
  }

  private void parse()
  {
    try {
      value = Double.parseDouble(buf.toString());
    } catch(NumberFormatException nex) {}
  }

  @Override
  public double resolve()
  {
    return value;
  }

  @Override
  public NumberExpression clone()
  {
    NumberExpression clone = new NumberExpression();
    clone.setValue(value);
    return clone;
  }

  @Override
  public String toString()
  {
    return String.valueOf(value);
  }

}
