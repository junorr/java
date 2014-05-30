package com.jpower.utils;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno
 */
public class Expression
{

  private Expression exp1, exp2;

  private Operation op;

  private List objs;

  private String description;
  
  private String name;


  public Expression()
  {
    this.op = null;
    this.exp1 = null;
    this.exp2 = null;
    this.description = null;
    objs = null;
    name = null;
  }
  
  
  public Expression(String name) {
    this();
    this.name = name;
  }
  

  public Expression(List parsedObjects)
  {
    this();
    if(parsedObjects == null
        || parsedObjects.isEmpty())
      throw new IllegalArgumentException(
          "List of parsed objects must be NOT NULL.\n" +
          "#Expression( List )#");
    objs = parsedObjects;
    init();
  }

  @Override
  public Expression clone()
  {
    Expression clone = new Expression();
    if(exp1 != null)
      clone.expression1(exp1.clone());
    if(exp2 != null)
      clone.expression2(exp2.clone());
    if(op != null)
      clone.operation(op.clone());
    if(description != null)
      clone.setDescription(description);
    return clone;
  }

  public void setDescription(String desc)
  {
    description = desc;
  }

  public String getDescription()
  {
    return description;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }

  public Expression expression1()
  {
    return exp1;
  }

  public Expression expression2()
  {
    return exp2;
  }

  public Expression expression1(Expression exp)
  {
    exp1 = exp;
    return this;
  }

  public Expression expression2(Expression exp)
  {
    exp2 = exp;
    return this;
  }

  public Operation operation()
  {
    return op;
  }

  public Expression operation(Operation op)
  {
    this.op = op;
    return this;
  }

  public boolean containsVar()
  {
    return exp1 instanceof VarExpression
        || exp2 instanceof VarExpression;
  }

  public VarExpression getVar()
  {
    VarExpression v = exp1.getVar();
    if(v == null) v = exp2.getVar();
    return v;
  }

  public double resolve()
  {
    if(op == null
        && exp1 == null
        && exp2 == null)
      throw new IllegalStateException(
        "Expression not initialized correctly.\n" +
        "#Expression.resolve()#");
    else if(op != null
        && exp1 == null && exp2 == null)
      throw new IllegalStateException(
        "Expression not initialized correctly: Expression exp1/exp2 == <null>\n" +
        "#Expression.resolve()#");

    if(exp1 != null
        && op == null)
      return exp1.resolve();

    return op.calc(exp1, exp2);
  }

  public void init()
  {
    if(objs == null || objs.isEmpty())
      throw new IllegalStateException(
          "List of parsed objects must be different of: " +
          (objs == null ? "NULL" : objs.size()) +
          "\nUse [ init( List ) ] method.\n" +
          "#Expression.init()#");
    
    System.out.println("* objs.size() = "+ objs.size());
    for(Object o : objs) System.out.println("  "+ o);
    System.out.println("* objs");

    if(objs.size() == 1
        && objs.get(0) instanceof Expression)
      exp1 = (Expression) objs.get(0);
    else if(objs.size() == 1
        && !(objs.get(0) instanceof Expression))
      throw new IllegalStateException(
          "Unexpected object: " + objs.get(0) + "\n" +
          "Expression instance expected!\n" +
          "#Expression.init()#");
    else if(objs.size() == 2) {
      if(objs.get(0) instanceof Expression) {
        exp1 = (Expression) objs.get(0);
        op = (Operation) objs.get(1);
      } else {
        exp1 = (Expression) objs.get(1);
        op = (Operation) objs.get(0);
      }
    } else if(objs.size() == 3) {
      for(int i = 0; i < objs.size(); i++) {
        if(objs.get(i) instanceof Expression) {
          if(exp1 == null)
            exp1 = (Expression) objs.get(i);
          else
            exp2 = (Expression) objs.get(i);
        } else if(objs.get(i) instanceof Operation)
          op = (Operation) objs.get(i);
      }//for
    } else {

      int imaior = this.findMaxOperator(objs);

      op = (Operation) objs.get(imaior);
      System.out.println("* max op: "+ op);
      List[] ls = Expression.split(objs, imaior);
      
      if(ls != null) {
        exp1 = new Expression(ls[0]);
        if(ls.length > 1) {
          exp2 = new Expression(ls[1]);
        }
      }
    }//else
  }

  public void init(List objects)
  {
    objs = objects;
    this.init();
  }

  private int findMaxOperator(List l)
  {
    int max = -1;
    int amx = -1;
    int index = 0;
    int andex = 0;

    for(int i = 0; i < l.size(); i++) {
      Object o = l.get(i);

      amx = max;
      andex = index;

      if(o instanceof Operation) {
        Operation oper = (Operation) o;
        max = oper.getLevel();
        index = i;
      }

      if(max < amx) {
        max = amx;
        index = andex;
      }//if
    }//for

    return index;
  }

  public static List[] split(List list, int index)
  {
    if(list == null
        || list.isEmpty()
        || index <= 0)
      return null;
    
    List[] ls;
    
    if(index + 1 >= list.size()) {
      ls = new List[1];
      ls[0] = new LinkedList();
      for(int i = 0; i < index; i++)
        ls[0].add(list.get(i));
    } 
    
    else {
      ls = new List[2];
      ls[0] = new LinkedList();
      ls[1] = new LinkedList();
      for(int i = 0; i < index; i++)
        ls[0].add(list.get(i));
      for(int i = index+1; i < list.size(); i++)
        ls[1].add(list.get(i));
    }
    
    return ls;
  }

  @Override
  public String toString()
  {
    return 
        (exp1 == null ? "" : exp1+ " ") +
        (op == null ? "" : op+ " ") +
        (exp2 == null ? "" : exp2);
  }

}
