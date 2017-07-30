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

import br.com.bb.disec.micro.box.*;
import br.com.bb.disec.micro.box.def.NextOp;
import br.com.bb.disec.micro.util.JsonClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/07/2017
 */
public class JsonOpBuilder {

  private final List<JsonOp> ops;
  
  private String name;
  
  private Class[] types;
  
  private Object[] args;
  
  
  public JsonOpBuilder() {
    this(new ArrayList<>());
  }
  
  
  private JsonOpBuilder(List<JsonOp> ops) {
    this.ops = ops;
  }
  
  
  public JsonOpBuilder withName(String name) {
    this.name = name;
    return this;
  }
  
  
  public JsonOpBuilder withTypes(Class ... cls) {
    this.types = cls;
    return this;
  }
  
  
  public JsonOpBuilder withArgs(Object ... args) {
    this.args = args;
    if(types == null && args != null) {
      types = new Class[args.length];
      for(int i = 0; i < args.length; i++) {
        types[i] = args[i].getClass();
      }
    }
    return this;
  }
  
  
  private JsonOpBuilder clearVars() {
    name = null;
    types = null;
    args = null;
    return this;
  }
  
  
  public JsonOpBuilder reset() {
    ops.clear();
    return this.clearVars();
  }
  
  
  private String checkName() {
    if(name == null) {
      throw new IllegalStateException("Bad null name");
    }
    return name;
  }
  
  
  public JsonOpBuilder get() {
    JsonOp op = new JsonOp(name, JsonOp.OpType.GET);
    ops.add(op);
    return this.clearVars();
  }
  
  
  public JsonOpBuilder get(String name) {
    return this.withName(name).get();
  }
  
  
  public JsonOpBuilder set() {
    JsonOp op = new JsonOp(checkName(), Arrays.asList(args), JsonOp.OpType.SET);
    ops.add(op);
    return this.clearVars();
  }
  
  
  public JsonOpBuilder set(String name, Object arg) {
    return this.withName(name).withArgs(arg).set();
  }
  
  
  public JsonOpBuilder method() {
    if(types == null || types.length < 1) {
      JsonOp op = new JsonOp(name, JsonOp.OpType.METHOD);
      ops.add(op);
    }
    else {
      JsonOp op = new JsonOp(name, 
          Arrays.asList(types), 
          Arrays.asList(args), 
          JsonOp.OpType.METHOD, null
      );
      ops.add(op);
    }
    return this.clearVars();
  }
  
  
  public JsonOpBuilder method(String name) {
    return this.withName(name).method();
  }
  
  
  public JsonOpBuilder method(String name, Object ... args) {
    return this.withName(name).withArgs(args).method();
  }
  
  
  public JsonOpBuilder constructor() {
    if(types == null || types.length < 1) {
      JsonOp op = new JsonOp(name, JsonOp.OpType.CONSTRUCTOR);
      ops.add(op);
    }
    else {
      JsonOp op = new JsonOp("constructor", 
          Arrays.asList(types), 
          Arrays.asList(args), 
          JsonOp.OpType.CONSTRUCTOR, null
      );
      ops.add(op);
    }
    return this.clearVars();
  }
  
  
  public Operation build() {
    if(ops.isEmpty()) {
      throw new IllegalStateException("JsonOpBuilder is not configured");
    }
    Operation op = ops.get(ops.size() -1);
    for(int i = ops.size()-2; i >= 0; i--) {
      op = new NextOp(ops.get(i), op);
    }
    return op;
  }
  
  
  public String buildJson() {
    if(ops.isEmpty()) {
      throw new IllegalStateException("JsonOpBuilder is not configured");
    }
    return new GsonBuilder()
        .registerTypeAdapter(Class.class, new JsonClass())
        .setPrettyPrinting()
        .create()
        .toJson(ops);
  }
  
  
  public static JsonOpBuilder fromJson(String json) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Class.class, new JsonClass())
        .create();
    List<JsonOp> ops = new ArrayList<>();
    JsonParser par = new JsonParser();
    JsonArray array = (JsonArray) par.parse(json);
    for(int i = 0; i < array.size(); i++) {
      ops.add(gson.fromJson(array.get(i), JsonOp.class));
    }
    return new JsonOpBuilder(ops);
  }
  
}
