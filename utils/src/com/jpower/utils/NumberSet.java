package com.jpower.utils;

/**
 *
 * @author f6036477
 */
public class NumberSet
{

  private NumberExpression n1, n2;


  public NumberSet()
  {
    n1 = null;
    n2 = null;
  }

  public NumberSet(NumberExpression n1, NumberExpression n2)
  {
    this.n1 = n1;
    this.n2 = n2;
  }

  public NumberSet number1(NumberExpression n)
  {
    this.n1 = n;
    return this;
  }

  public NumberSet number2(NumberExpression n)
  {
    this.n1 = n;
    return this;
  }

  public NumberExpression number1()
  {
    return n1;
  }

  public NumberExpression number2()
  {
    return n2;
  }

}
