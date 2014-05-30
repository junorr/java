/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package com.jpower.expr;

import java.util.ArrayList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 05/08/2013
 */
public class ExpressionParser {

  public static final char
      PLUS = '+',
      MINUS = '-',
      MULTIPLY = '*',
      DIVIDE = '/',
      
      EQUALS = '=',
      NOT = '!',
      GREATER = '>',
      LESSER = '<';
  
  
  public static final int WEIGHT_BRACKET = 800;
  
  
  private ArrayList<Expression> stack;
  
  private VarResolver var;
  
  
  public ExpressionParser() {
    stack = new ArrayList<>();
    var = null;
  }
  
  
  public ExpressionParser(VarResolver ve) {
    this();
    var = ve;
  }
  
  
  public Operator getMathOperator(char c) {
    switch(c) {
      case PLUS:
        return new PlusOperator();
      case MINUS:
        return new MinusOperator();
      case MULTIPLY:
        return new MultiplyOperator();
      case DIVIDE:
        return new DivideOperator();
      default:
        return null;
    }
  }
  
  
  public Operator getLogicalOperator(char c, char d) {
    if(c == EQUALS && d == EQUALS) {
      return new EqualsOperator();
    }
    else if(c == NOT && d == EQUALS) {
      return new NotEqualsOperator();
    }
    else if(c == GREATER && d == EQUALS) {
      return new GreaterEqualsOperator();
    }
    else if(c == LESSER && d == EQUALS) {
      return new LesserEqualsOperator();
    }
    else if(c == GREATER) {
      return new GreaterOperator();
    }
    else if(c == LESSER) {
      return new LesserOperator();
    }
    else {
      return null;
    }
  }
  
  
  public boolean isLogicalOperator(char c, char d) {
    return (c == EQUALS && d == EQUALS)
        || (c == NOT && d == EQUALS)
        || (c == GREATER && d == EQUALS)
        || (c == LESSER && d == EQUALS)
        || (c == GREATER)
        || (c == LESSER);
  }
  
  
  public int logicalOperatorCharSize(char c, char d) {
    if((c == EQUALS && d == EQUALS)
        || (c == NOT && d == EQUALS)
        || (c == GREATER && d == EQUALS)
        || (c == LESSER && d == EQUALS))
      return 1;
    else if(c == GREATER || c == LESSER)
      return 0;
    return -1;
  }
  
  
  public Expression getExpressionFrom(String s) {
    if(s == null || s.isEmpty()) return null;
    if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
      return new BooleanExpression().setBoolean(s);
    else {
      NumberExpression ne = new NumberExpression()
          .setNumber(s);
      if(ne.getNumber() != Integer.MIN_VALUE)
        return ne;
    }
    return null;
  }
  
  
  private void stackIn(String expr) {
    if(expr == null || expr.trim().isEmpty())
      return;
    
    stack.clear();
    char[] cs = expr.trim().toCharArray();
    String s = "";
    int mod = 0;
    Expression exp = new Expression();
    Expression arg = null;
    
    for(int i = 0; i < cs.length; i++) {
      char c = cs[i];
      if(c == ' ') continue;
      if(c == '(') mod += WEIGHT_BRACKET;
      else if(c == ')') mod -= WEIGHT_BRACKET;
      else {
        Operator oper = this.getMathOperator(c);
        
        if(oper == null) {
          if(i < cs.length-1 && isLogicalOperator(c, cs[i+1])) {
            oper = this.getLogicalOperator(c, cs[i+1]);
            i += this.logicalOperatorCharSize(c, cs[i+1]);
          }
        }
        
        if(oper != null) {
          oper.addMod(mod++);
          
          if(!s.isEmpty()) {
            if(var != null && var.canResolve(s))
              arg = var.resolve(s);
            else
              arg = this.getExpressionFrom(s);
            s = "";
          }
          
          if(exp.getExp1() == null) {
            exp.setOper(oper)
                .setExp1(arg);
          } else {
            exp.setExp2(arg);
            stack.add(exp);
            exp = new Expression()
                .setOper(oper)
                .setExp1(arg);
          }
        } else s += c;
      }//else
    }//for
    
    if(exp != null && !s.isEmpty()) {
      if(var != null && var.canResolve(s))
        arg = var.resolve(s);
      else
        arg = this.getExpressionFrom(s);
      exp.setExp2(arg);
      stack.add(exp);
    }
  }
  
  
  public Expression resolve() {
    if(stack == null || stack.isEmpty())
      return null;
    
    int last = 10000;
    int igrt = 0;
    Expression grt = null;
    
    for(int i = 0; i < stack.size(); i++) {
      int wgt = 0;
      for(int j = 0; j < stack.size(); j++) {
        Expression e = stack.get(j);
        if(e.getWeight() > wgt && e.getWeight() < last) {
          wgt = e.getWeight();
          grt = e;
          igrt = j;
        }
      }//for
      
      NumberExpression re = new NumberExpression(grt.resolve());
      last = grt.getWeight();
      
      if(igrt > 0)
        stack.get(igrt-1).setExp2(re);
      if(igrt < stack.size()-1)
        stack.get(igrt+1).setExp1(re);
      
      stack.remove(igrt);
      i--;
    }//for
    return grt;
  }
  
  
  
  public void printStack() {
    System.out.println("* stack: {");
    for(Expression e : stack)
      System.out.println("  - "+ e);
    System.out.println("}: stack");
  }
  
  
  public Expression parse(String expr) {
    if(expr == null || expr.trim().isEmpty())
      return null;
    
    this.stackIn(expr);
    return this.resolve();
  }
  
  
  public static void main(String[] args) {
    ExpressionParser p = new ExpressionParser();
    String s = "10 + (3+3) * 5 + 10 / 2 -1";
    System.out.println(s);
    Expression e = p.parse(s);
    System.out.println(e.toString());
    System.out.println(e.resolve());
  }
  
}
