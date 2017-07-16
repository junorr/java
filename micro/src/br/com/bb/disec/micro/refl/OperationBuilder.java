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

package br.com.bb.disec.micro.refl;

import br.com.bb.disec.micro.refl.impl.OperationImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/06/2017
 */
public class OperationBuilder {

  private final List<String> argtypes;
  
  private final List args;
  
  private String name;
  
  private final OperationType optype;
  
  private String className;
  
  private OperationBuilder parent;
  
  private OperationBuilder next;
  
  
  public OperationBuilder(OperationType type) {
    if(type == null) {
      throw new IllegalArgumentException("Bad null OperationType");
    }
    this.argtypes = new ArrayList<>();
    this.args = new ArrayList();
    this.name = null;
    this.optype = type;
    this.className = null;
    this.parent = null;
    this.next = null;
  }
  
  
  protected OperationBuilder(OperationType type, OperationBuilder parent) {
    this.argtypes = new ArrayList<>();
    this.args = new ArrayList();
    this.name = null;
    this.optype = type;
    this.className = null;
    this.parent = parent;
    this.next = null;
  }
  
  
  public OperationBuilder addArgType(String cls) {
    if(cls != null) {
      argtypes.add(cls);
    }
    return this;
  }
  
  
  public OperationBuilder addArgClass(Object arg) {
    if(arg != null) {
      args.add(arg);
      argtypes.add(arg.getClass().getName());
    }
    return this;
  }
  
  
  public OperationBuilder addArg(Object arg) {
    if(arg != null) {
      args.add(arg);
      argtypes.add(arg.getClass().getName());
    }
    return this;
  }
  
  
  public List<String> argsTypes() {
    return argtypes;
  }
  
  
  public List arguments() {
    return args;
  }
  
  
  public OperationType operationType() {
    return optype;
  }
  
  
  public OperationBuilder next(OperationType type) {
    this.next = new OperationBuilder(type, this);
    return next;
  }
  
  
  public OperationBuilder parent() {
    return parent;
  }
  
  
  public String className() {
    return className;
  }
  
  
  public OperationBuilder className(String cls) {
    if(cls != null) {
      this.className = cls;
    }
    return this;
  }
  
  
  public String name() {
    return name;
  }
  
  
  public OperationBuilder name(String name) {
    if(name != null) {
      this.name = name;
    }
    return this;
  }
  
  
  public Operation build() {
    if(this.name == null) {
      throw new IllegalStateException("Bad null op name");
    }
    return new OperationImpl(
        name, 
        optype, 
        Collections.unmodifiableList(args), 
        Collections.unmodifiableList(argtypes), 
        className, 
        next != null ? next.build() : null
    );
  }
  
}
