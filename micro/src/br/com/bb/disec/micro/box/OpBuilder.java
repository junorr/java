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

package br.com.bb.disec.micro.box;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/07/2017
 */
public class OpBuilder {

  private final List<Operation> ops;
  
  private String name;
  
  private Class[] types;
  
  private Object[] args;
  
  
  public OpBuilder() {
    this.ops = new ArrayList<>();
  }
  
  
  public OpBuilder withName(String name) {
    this.name = name;
    return this;
  }
  
  
  public OpBuilder withTypes(Class ... cls) {
    this.types = cls;
    return this;
  }
  
  
  public OpBuilder withArgs(Object ... args) {
    this.args = args;
    if(types == null && args != null) {
      types = new Class[args.length];
      for(int i = 0; i < args.length; i++) {
        types[i] = args[i].getClass();
      }
    }
    return this;
  }
  
  
  private OpBuilder clearVars() {
    name = null;
    types = null;
    args = null;
    return this;
  }
  
  
  public OpBuilder reset() {
    ops.clear();
    return this.clearVars();
  }
  
  
  private String checkName() {
    if(name == null) {
      throw new IllegalStateException("Bad null name");
    }
    return name;
  }
  
  
  public OpBuilder get() {
    ops.add(new GetFieldOp(checkName()));
    return this.clearVars();
  }
  
  
  public OpBuilder get(String name) {
    return this.withName(name).get();
  }
  
  
  public OpBuilder set() {
    ops.add(new SetFieldOp(checkName(), args == null || args.length < 1 ? null : args[0]));
    return this.clearVars();
  }
  
  
  public OpBuilder set(String name, Object arg) {
    return this.withName(name).withArgs(arg).set();
  }
  
  
  public OpBuilder method() {
    if(types == null || types.length < 1) {
      ops.add(new MethodOp(name));
    }
    else {
      ops.add(new MethodOp(checkName(), 
          Arrays.asList(types), 
          Arrays.asList(args))
      );
    }
    return this.clearVars();
  }
  
  
  public OpBuilder method(String name) {
    return this.withName(name).method();
  }
  
  
  public OpBuilder method(String name, Object ... args) {
    return this.withName(name).withArgs(args).method();
  }
  
  
  public OpBuilder constructor() {
    if(types == null || types.length < 1) {
      ops.add(new ConstructorOp());
    }
    else {
      ops.add(new ConstructorOp( 
          Arrays.asList(types), 
          Arrays.asList(args))
      );
    }
    return this.clearVars();
  }
  
  
  public Operation build() {
    if(ops.isEmpty()) {
      throw new IllegalStateException("OpBuilder is not configured");
    }
    Operation op = ops.get(ops.size() -1);
    for(int i = ops.size()-2; i >= 0; i--) {
      op = new NextOp(ops.get(i), op);
    }
    return op;
  }
  
}
