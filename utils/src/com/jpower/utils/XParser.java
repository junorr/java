package com.jpower.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno
 */
public class XParser
{

  private String sign;

  private CalcMemory cmem;


  public XParser()
  {
    cmem = CalcMemory.getInstance();
    sign = null;
  }

  public XParser(String signature)
  {
    this();
    sign = signature;
  }

  public String getSignature()
  {
    return sign;
  }

  public void setSignature(String signature)
  {
    sign = signature;
  }

  private String trim()
  {
    StringUtil su = new StringUtil(sign);
    su.replaceAll(",", ".");
    su.replaceAll(" ", "");
    return su.getString();
  }

  private List deleteCharacters(List l)
  {
    if(l == null) return null;

    List nl = new LinkedList();
    for(int i = 0; i < l.size(); i++) {
      Object o = l.get(i);
      if(o != null && !(o instanceof Character))
        nl.add(o);
    }//for
    return nl;
  }

  public Expression parse()
      throws TypeNotPresentException, IllegalStateException
  {
    String sig = this.trim();

    List l = new LinkedList();
    NumberExpression ne = null;
    MathOperation oper = null;
    boolean isvar = false;
    String var = "";
    int count = 0;
    int bracket = 0;
    char c = 0;

    while(count < sig.length()) {
      c = sig.charAt(count);

      //NumberExpression
      while(count < sig.length() &&
          Character.isDigit(c) || c == '.') {

        if(ne == null) ne = new NumberExpression();
        ne.append(c);

        if(count + 1 < sig.length())
          c = sig.charAt(++count);
        else count++;
      }//NumberExpression

      if(ne != null) {
        ne.init();
        l.add(ne);
        ne = null;
      }

      if(count >= sig.length()) break;

      //parênteses
      if(c == '(') {
        l.add(c);
        bracket++;
      } else if(c == ')') {
        l.add(c);
        bracket--;
      } else if(MathOperation.isOperator(c)) {//operador
        isvar = false;
        if(!isvar && !var.equals("")) {
          this.checkVar(var, l, '0');
          var = "";
        }
        l.add(new MathOperation(c));
      } else if(Character.isLetter(c)) {//variável
        var += String.valueOf(c);
        isvar = true;
      } else
        throw new TypeNotPresentException("\n" +
            "    char: "+ c+ ", index: "+ count + "\n" +
            "    #XParser.parse(): 118#", null);

      count++;
    }//while
    if(isvar)
      this.checkVar(var, l, '0');

    if(bracket != 0)
      throw new IndexOutOfBoundsException("\n" +
          "    Illegal bracket number: "+ bracket + "\n" +
          "    #XParser.parse(): 128#");

    this.analiseSintatica(l);

    Expression e = new Expression(this.deleteCharacters(l));

    cmem.expressions.add(e);

    return e;
  }

  private void analiseSintatica(List objs)
  {
    boolean isnum = false;
    boolean isoper = false;
    boolean antoper = false;
    int priority = 0;

    for(int i = 0; i < objs.size(); i++) {
      Object o = objs.get(i);

      antoper = isoper;

      if(o instanceof Character
          && ((Character) o).charValue() == '(')
        priority++;

      if(o instanceof Character
          && ((Character) o).charValue() == ')')
        priority--;
      
      isoper = this.checkOperation(o, priority);

      if(isoper && (i == 0 || String.valueOf(objs.get(i-1)).equals("(")) && ((MathOperation) o).getOperator() == '-') {
        ((MathOperation) o).setPriority(100);
      }

      //análise sintática de operadores
      if(antoper && isoper) {
        MathOperation aoper = (MathOperation) objs.get(i-1);
        MathOperation oper = (MathOperation) objs.get(i);
        if(aoper.getOperator() != '#'
            && aoper.getOperator() != '%'
            && oper.getOperator() != '-'
            && oper.getOperator() != 'L')
          throw new IllegalStateException(
              "\n    Illegal sequence expression: Number missing!\n" +
              "    [ " + aoper.getOperator() + " ? " + objs.get(i) + " ]\n" +
              "    #XParser.analiseSintatica(): 167#");
        antoper = false;
        isoper = false;
      }

      //substitue String por VarExpression
      if(o instanceof String) {
        o = new VarExpression(o.toString());
        objs.set(i, o);
      }

      //análise sintática de números
      if(o instanceof NumberExpression
          || o instanceof VarExpression) {
        if(isnum)
          throw new IllegalStateException("\n" +
              "    Invalid sequence expression: Operator missing!\n" +
              "    [ " + objs.get(i-1) + " ? " + o + " ]\n" +
              "    #Expression.analiseSintatica(): 187#");
        isnum = true;

      } else
        isnum = false;
    }//for
  }

  private boolean checkOperation(Object o, int priority)
  {
    boolean isoper = false;
    //se for (, muda prioridade
    if(o instanceof MathOperation) {
      MathOperation oper = (MathOperation) o;
      //System.out.println( oper.getOperator() + " | " + priority );
      oper.setPriority(priority);
      isoper = true;
    }//if
    
    return isoper;
  }

  private void checkVar(String var, List l, char c)
  {
    VarExpression ve = null;
    if(cmem.contains(var) && c != '=')
      ve = cmem.get(var);
    else {
      ve = new VarExpression(var);
      cmem.put(ve);
    }
    l.add(ve);
  }


  public static void main(String[] args) throws IOException
  {
    XParser xp = new XParser();
    CalcMemory cm = CalcMemory.getInstance();
    
    //String s = "2.0 * b / (3 - 1.0) + 1.2";
    String s = "b = 2.0 * c / (3 - 1.0) + 16#^2";
    //String s = "b=4^2+(-16)";
    System.out.println(s);
    xp.setSignature(s);
    Expression e = xp.parse();
    cm.putVar("b", 5);
    cm.putVar("c", 3);
    System.out.println(e);
    System.out.println("e' = "+ e.expression1());
    System.out.println("op = "+ e.operation());
    System.out.println("e\" = "+ e.expression2());
    System.out.println(e.resolve());
    System.out.println(cm.get("b"));
   
    
    String vol    = " V = Pi * R^2 * h";
    String raio   = " R = (i/2 + j/2 + k/2) / 3";
    String vgrao  = "Vg = l ^ 3";
    String graos  = " G = V / Vg";
    String tweets = " T = G / 140";

    System.out.println(vol);
    System.out.println(raio);
    System.out.println(vgrao);
    System.out.println(graos);
    System.out.println(tweets);

    cm.putVar("Pi", 3.1416);
    cm.putVar("h", 15.0);
    cm.putVar("i", 2.5);
    cm.putVar("j", 5.0);
    cm.putVar("k", 7.5);
    cm.putVar("l", 0.020);

    xp.setSignature(raio);
    Expression r = xp.parse();

    xp.setSignature(vol);
    Expression v = xp.parse();

    xp.setSignature(vgrao);
    Expression vg = xp.parse();

    xp.setSignature(graos);
    Expression g = xp.parse();

    xp.setSignature(tweets);
    Expression t = xp.parse();

    r.resolve();
    v.resolve();
    vg.resolve();
    g.resolve();
    t.resolve();

    System.out.println(cm.get("R"));
    System.out.println(cm.get("V"));
    System.out.println(cm.get("Vg"));
    System.out.println(cm.get("G"));
    System.out.println(cm.get("T"));
    
  }

}
