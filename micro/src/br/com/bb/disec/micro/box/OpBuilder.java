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

import br.com.bb.disec.micro.box.def.ChainOp;
import br.com.bb.disec.micro.box.def.SetFieldOp;
import br.com.bb.disec.micro.box.def.NextOp;
import br.com.bb.disec.micro.box.def.MethodOp;
import br.com.bb.disec.micro.box.def.ConstructorOp;
import br.com.bb.disec.micro.box.def.GetFieldOp;
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
  
  private final String name;
  
  private final String className;
  
  private final Class[] types;
  
  private final Object[] args;


  private OpBuilder(List<Operation> ops, String name, String className, Class[] types, Object[] args) {
    this.ops = ops;
    this.name = name;
    this.className = className;
    this.types = types;
    this.args = args;
  }
  
  
  public OpBuilder() {
    this(new ArrayList<>(), null, null, null, null);
  }
  
  
  public OpBuilder withName(String name) {
    return new OpBuilder(ops, name, className, types, args);
  }
  
  
  public OpBuilder onClass(String cls) {
    return new OpBuilder(ops, name, cls, types, args);
  }
  
  
  public OpBuilder withTypes(Class ... cls) {
    return new OpBuilder(ops, name, className, cls, args);
  }
  
  
  public OpBuilder withArgs(Object ... args) {
    Class[] types = this.types;
    if(types == null && args != null) {
      types = new Class[args.length];
      for(int i = 0; i < args.length; i++) {
        types[i] = args[i].getClass();
      }
    }
    return new OpBuilder(ops, name, className, types, args);
  }
  
  
  private String checkName() {
    if(name == null) {
      throw new IllegalStateException("Bad null name");
    }
    return name;
  }
  
  
  public OpBuilder get() {
    ops.add(new GetFieldOp(checkName()));
    return new OpBuilder(ops, null, className, null, null);
  }
  
  
  public OpBuilder get(String name) {
    return this.withName(name).get();
  }
  
  
  public OpBuilder set() {
    ops.add(new SetFieldOp(checkName(), args == null || args.length < 1 ? null : args[0]));
    return new OpBuilder(ops, null, className, null, null);
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
    return new OpBuilder(ops, null, className, null, null);
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
    return new OpBuilder(ops, null, className, null, null);
  }
  
  
  public Operation build() {
    if(ops.isEmpty()) {
      throw new IllegalStateException("OpBuilder is not configured");
    }
    Operation op = ops.get(ops.size() -1);
    for(int i = ops.size()-2; i >= 0; i--) {
      op = new NextOp(ops.get(i), op);
    }
    return (className != null ? new ChainOp(className, op) : op);
  }
  
}
