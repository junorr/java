package com.jpower.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juno
 */
public class Operation
{

  public static final List<Character> OPERATORS =
      new ArrayList<Character>();

  static {
    OPERATORS.add('L');// 0
    OPERATORS.add('#');// 1
    OPERATORS.add('%');// 2
    OPERATORS.add('^');// 3
    OPERATORS.add('*');// 4
    OPERATORS.add('/');// 5
    OPERATORS.add('-');// 6
    OPERATORS.add('+');// 7
    OPERATORS.add('=');// 8
  }

  private int priority;

  private char operator;


  public Operation(char operator)
  {
    this();
    this.setOperator(operator);
  }

  public Operation()
  {
    priority = 0;
    operator = 0;
  }

  public void setOperator(char operator)
  {
    if(!OPERATORS.contains(operator))
      throw new IllegalArgumentException("[ERROR]: " +
          "Illegal Operator: "+operator);
    this.operator = operator;
  }

  public char getOperator()
  {
    return operator;
  }

  public static boolean isOperator(char c)
  {
    return OPERATORS.contains(c);
  }

  public void setPriority(int priority)
  {
    this.priority = priority;
  }

  public void incPriority()
  {
    priority++;
  }

  public void decPriority()
  {
    priority--;
  }

  public int getPriority()
  {
    return priority;
  }

  public int getLevel()
  {
    return OPERATORS.indexOf(operator) + 100 - priority * 10;
  }

  public double calc(Expression exp1, Expression exp2)
  {
    if(operator == 0)
      throw new IllegalArgumentException(
          "Invalid operator: " + operator + "\n" +
          "#Operation.calc( Expresssion, Expression )#");
    if(exp1 == null && exp2 == null)
      throw new IllegalArgumentException(
          "Expressions must be NOT NULL.\n" +
          "#Operation.calc( Expression, Expression )#");
    if(operator != '#' && operator != '%' && operator != '-'
            && operator != 'L' && exp2 == null)
      throw new IllegalArgumentException(
          "Expression exp2 must be NOT NULL for this operator: " + operator + "\n" +
          "#Operation.calc( Expression, Expression )#");

    int i = OPERATORS.indexOf(operator);

    switch(i) {
      case 0://log
        return Math.log10(exp1.resolve());
      case 1://raíz
        return Math.sqrt(exp1.resolve());
      case 2://percentual
        return exp1.resolve() / 100;
      case 3://exponenciação
        return Math.pow(exp1.resolve(), exp2.resolve());
      case 4://multiplicação
        return exp1.resolve() * exp2.resolve();
      case 5://divisão
        return exp1.resolve() / exp2.resolve();
      case 6://subtração
        if(exp2 == null) {
          exp2 = exp1;
          exp1 = new NumberExpression(0.0);
        }
        return exp1.resolve() - exp2.resolve();
      case 7://soma
        return exp1.resolve() + exp2.resolve();
      case 8://igual
        if(exp1 instanceof VarExpression
            || exp1.containsVar()) {
          exp1.getVar().setValue(exp2.resolve());
          return exp1.resolve();
        } else {
          exp2.getVar().setValue(exp1.resolve());
          return exp2.resolve();
        }//else
      default:
        return 0;
    }//switch
  }

  @Override
  public Operation clone()
  {
    Operation clone = new Operation();
    clone.setOperator(operator);
    clone.setPriority(priority);
    return clone;
  }

  @Override
  public String toString()
  {
    return String.valueOf(operator);
  }

}
