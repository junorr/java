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
public class BooleanExpression extends Expression {

  private boolean bool;
  
  
  public BooleanExpression() {}
  
  
  public BooleanExpression(boolean b) { bool = b; }
  
  
  public boolean getBoolean() { return bool; }
  
  
  public BooleanExpression setBoolean(boolean b) { 
    bool = b; 
    return this;
  }
  
  
  public BooleanExpression setBoolean(String str) {
    if(str == null || str.trim().isEmpty()
        || (!str.equalsIgnoreCase("true")
        && !str.equalsIgnoreCase("false")))
      throw new IllegalArgumentException(
          "Argument is not a Boolean: "+ str);
    
    bool = Boolean.parseBoolean(str);
    return this;
  }
  
  
  public Number resolve() {
    return (bool ? 1 : 0);
  }
  
  
  public String toString() {
    return String.valueOf(bool);
  }
  
}
