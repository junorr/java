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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 05/08/2013
 */
public class Expression {
  
  protected Operator oper;
  
  protected Expression e1, e2;
  
  protected Number result;
  
  
  public Expression() {
    oper = null;
    e1 = e2 = null;
    result = null;
  }
  
  
  public Expression(Expression e1, Expression e2, Operator op) {
    this.e1 = e1;
    this.e2 = e2;
    this.oper = op;
  }


  public Operator getOper() {
    return oper;
  }


  public Expression setOper(Operator oper) {
    this.oper = oper;
    return this;
  }


  public Expression getExp1() {
    return e1;
  }


  public Expression setExp1(Expression e1) {
    this.e1 = e1;
    return this;
  }


  public Expression getExp2() {
    return e2;
  }


  public Expression setExp2(Expression e2) {
    this.e2 = e2;
    return this;
  }
  
  
  public int getWeight() {
    return (oper == null 
        ? 1 : oper.getTotalWeight());
  }
  
  
  public Number resolve() {
    if(oper == null) {
      if(e1 != null) return e1.resolve();
      if(e2 != null) return e2.resolve();
      return Integer.MIN_VALUE;
    }
    return oper.apply(e1, e2);
  }
  
  
  public static Expression getExpressionFor(String s) {
    NumberExpression e = new NumberExpression().setNumber(s);
    if(e.getNumber() != Integer.MIN_VALUE)
      return e;
    return new BooleanExpression().setBoolean(s);
  }
  
  
  public String toString() {
    return (e1 != null ? e1.toString() : "null")
        + (oper != null ? " "+ oper.toString()+ " " : " ")
        + (e2 != null ? e2.toString() : "null");
  }
  
}
