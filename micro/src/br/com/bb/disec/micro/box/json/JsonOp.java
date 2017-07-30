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

package br.com.bb.disec.micro.box.json;

import br.com.bb.disec.micro.box.OpBuilder;
import br.com.bb.disec.micro.box.OpResult;
import br.com.bb.disec.micro.box.Operation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2017
 */
public class JsonOp implements Operation {

  public static enum OpType {
    GET, SET, METHOD, CONSTRUCTOR;
  }
  
  private final OpType optype;
  
  private final String name;
  
  private final List<Class> argtypes;
  
  private final List arguments;
  
  private final JsonOp next;
  
  
  public JsonOp(String name, List<Class> argtypes, List args, OpType type, JsonOp next) {
    this.name = Sane.of(name).checkup().isNotNull().with("Bad null name").get();
    this.argtypes = (argtypes == null ? Collections.EMPTY_LIST : argtypes);
    this.arguments = (args == null ? Collections.EMPTY_LIST : args);
    this.optype = Sane.of(type).checkup().isNotNull().with("Bad null OpType").get();
    this.next = next;
  }
  
  
  public JsonOp(String name, List args, OpType type) {
    this.name = Sane.of(name).checkup().isNotNull().with("Bad null name").get();
    this.argtypes = Collections.EMPTY_LIST;
    this.arguments = (args == null ? Collections.EMPTY_LIST : args);
    this.optype = Sane.of(type).checkup().isNotNull().with("Bad null OpType").get();
    this.next = null;
  }
  
  
  public JsonOp(String name, OpType type) {
    this.name = Sane.of(name).checkup().isNotNull().with("Bad null name").get();
    this.argtypes = Collections.EMPTY_LIST;
    this.arguments = Collections.EMPTY_LIST;
    this.optype = Sane.of(type).checkup().isNotNull().with("Bad null OpType").get();
    this.next = null;
  }
  
  
  public JsonOp(String name, OpType type, JsonOp next) {
    this.name = Sane.of(name).checkup().isNotNull().with("Bad null name").get();
    this.argtypes = Collections.EMPTY_LIST;
    this.arguments = Collections.EMPTY_LIST;
    this.optype = Sane.of(type).checkup().isNotNull().with("Bad null OpType").get();
    this.next = next;
  }
  

  @Override
  public String getName() {
    return name;
  }
  
  
  private Class[] getTypes() {
    return (argtypes.isEmpty() 
        ? null 
        : argtypes.toArray(
            new Class[argtypes.size()])
        );
  }


  @Override
  public OpResult execute(Object obj) {
    Sane.of(obj).checkup().isNotNull().with("Bad null object").check();
    switch(optype) {
      case CONSTRUCTOR:
        return new OpBuilder()
            .withTypes(getTypes())
            .withArgs(arguments.toArray())
            .constructor()
            .build().execute(obj);
      case GET:
        return new OpBuilder()
            .get(name)
            .build().execute(obj);
      case METHOD:
        return new OpBuilder()
            .withTypes(getTypes())
            .withArgs(arguments.toArray())
            .method(name)
            .build().execute(obj);
      case SET:
        return new OpBuilder()
            .withName(name)
            .withArgs(arguments.toArray())
            .set()
            .build().execute(obj);
      default:
        throw new UnsupportedOperationException("Unknown operation type: "+ optype);
    }
  }


  @Override
  public Optional<Operation> next() {
    return Optional.ofNullable(next);
  }
  
}
