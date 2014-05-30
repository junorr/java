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

package com.jpower.tn3270.script;

import com.jpower.expr.BooleanExpression;
import com.jpower.expr.Expression;
import com.jpower.expr.NumberExpression;
import com.jpower.expr.VarResolver;
import java.util.LinkedList;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public class VarMemory implements VarResolver {
  
  private LinkedList<Var> vars;
  
  private static final VarMemory 
      instance = new VarMemory()
      .put(Var.NL).put(Var.CSV)
      .put(Var.TEMP);
  
  
  private VarMemory() {
    vars = new LinkedList<>();
  }
  
  
  public static VarMemory getInstance() {
    return instance;
  }
  
  
  public static Var getTEMP() {
    return instance.get(Var.STR_TEMP);
  }
  
  
  public static Var getNL() {
    return instance.get(Var.STR_NL);
  }
  
  
  public static Var getCSV() {
    return instance.get(Var.STR_CSV);
  }
  
  
  public VarMemory put(Var var) {
    if(var != null) {
      Var v = this.get(var.name());
      if(v != null) v.setValue(var.value());
      else vars.add(var);
    }
    return this;
  }
  
  
  public boolean contains(Var var) {
    if(var == null || vars.isEmpty())
      return false;
    for(Var v : vars)
      if(v.name().equalsIgnoreCase(var.name()))
        return true;
    return false;
  }
  
  
  public Var get(String varName) {
    if(varName == null || vars.isEmpty())
      return null;
    for(Var v : vars)
      if(v.name().equalsIgnoreCase(varName))
        return v;
    return null;
  }
  
  
  public boolean contains(String varName) {
    if(varName == null || vars.isEmpty())
      return false;
    for(Var v : vars)
      if(v.name().equalsIgnoreCase(varName))
        return true;
    return false;
  }
  
  
  public Var get(int index) {
    if(index < 0 || index >= vars.size())
      return null;
    return vars.get(index);
  }
  
  
  public int indexOf(Var var) {
    if(var == null) return -1;
    for(int i = 0; i < vars.size(); i++) {
      if(var.name().equalsIgnoreCase(vars.get(i).name()))
        return i;
    }
    return -1;
  }
  
  
  public boolean remove(String varName) {
    if(varName == null || vars.isEmpty())
      return false;
    for(Var v : vars)
      if(v.name().equalsIgnoreCase(varName)) {
        vars.remove(v);
        return true;
      }
    return false;
  }
  
  
  public Var remove(int index) {
    if(index < 0 || index >= vars.size())
      return null;
    return vars.remove(index);
  }


  @Override
  public boolean canResolve(String str) {
    return this.contains(str);
  }


  @Override
  public Expression resolve(String str) {
    Var v = this.get(str);
    if(v == null) return null;
    if(v.value().equalsIgnoreCase("true")
        || v.value().equalsIgnoreCase("false"))
      return new BooleanExpression().setBoolean(v.value());
    else
      return new NumberExpression().setNumber(v.value());
  }

}
