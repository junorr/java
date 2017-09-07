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
import br.com.bb.disec.micro.box.def.ChainOp;
import br.com.bb.disec.micro.box.def.NextOp;
import br.com.bb.disec.micro.util.JsonClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
  
  private final String name;
  
  private final String className;
  
  private final Class[] types;
  
  private final Object[] args;


  public JsonOpBuilder(List<JsonOp> ops, String name, String className, Class[] types, Object[] args) {
    this.ops = ops;
    this.name = name;
    this.className = className;
    this.types = types;
    this.args = args;
  }
  
  
  public JsonOpBuilder() {
    this(new ArrayList<>(), null, null, null, null);
  }
  
  
  private JsonOpBuilder(List<JsonOp> ops) {
    this(ops, null, null, null, null);
  }
  
  
  public JsonOpBuilder withName(String name) {
    return new JsonOpBuilder(ops, name, className, types, args);
  }
  
  
  public JsonOpBuilder onClass(String className) {
    return new JsonOpBuilder(ops, name, className, types, args);
  }
  
  
  public JsonOpBuilder withTypes(Class ... cls) {
    return new JsonOpBuilder(ops, name, className, cls, args);
  }
  
  
  public JsonOpBuilder withArgs(Object ... args) {
    Class[] types = this.types;
    if(types == null && args != null) {
      types = new Class[args.length];
      for(int i = 0; i < args.length; i++) {
        types[i] = args[i].getClass();
      }
    }
    return new JsonOpBuilder(ops, name, className, types, args);
  }
  
  
  private String checkName() {
    if(name == null) {
      throw new IllegalStateException("Bad null name");
    }
    return name;
  }
  
  
  public JsonOpBuilder get() {
    ops.add(new JsonOp(name, JsonOp.OpType.GET));
    return new JsonOpBuilder(ops, null, className, null, null);
  }
  
  
  public JsonOpBuilder get(String name) {
    return this.withName(name).get();
  }
  
  
  public JsonOpBuilder set() {
    ops.add(new JsonOp(checkName(), Arrays.asList(args), JsonOp.OpType.SET));
    return new JsonOpBuilder(ops, null, className, null, null);
  }
  
  
  public JsonOpBuilder set(String name, Object arg) {
    return this.withName(name).withArgs(arg).set();
  }
  
  
  public JsonOpBuilder method() {
    if(types == null || types.length < 1) {
      ops.add(new JsonOp(name, JsonOp.OpType.METHOD));
    }
    else {
      ops.add(new JsonOp(name, 
          Arrays.asList(types), 
          Arrays.asList(args), 
          JsonOp.OpType.METHOD, null)
      );
    }
    return new JsonOpBuilder(ops, null, className, null, null);
  }
  
  
  public JsonOpBuilder method(String name) {
    return this.withName(name).method();
  }
  
  
  public JsonOpBuilder method(String name, Object ... args) {
    return this.withName(name).withArgs(args).method();
  }
  
  
  public JsonOpBuilder constructor() {
    if(types == null || types.length < 1) {
      ops.add(new JsonOp(name, JsonOp.OpType.CONSTRUCTOR));
    }
    else {
      ops.add(new JsonOp("constructor", 
          Arrays.asList(types), 
          Arrays.asList(args), 
          JsonOp.OpType.CONSTRUCTOR, null)
      );
    }
    return new JsonOpBuilder(ops, null, className, null, null);
  }
  
  
  public Operation build() {
    if(ops.isEmpty()) {
      throw new IllegalStateException("JsonOpBuilder is not configured");
    }
    Operation op = ops.get(ops.size() -1);
    for(int i = ops.size()-2; i >= 0; i--) {
      op = new NextOp(ops.get(i), op);
    }
    return (className != null ? new ChainOp(className, op) : op);
  }
  
  
  public String buildJson() {
    if(ops.isEmpty()) {
      throw new IllegalStateException("JsonOpBuilder is not configured");
    }
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Class.class, new JsonClass())
        .setPrettyPrinting()
        .create();
    JsonObject obj = new JsonObject();
    obj.addProperty("class", className);
    obj.add("ops", gson.toJsonTree(ops));
    return gson.toJson(obj);
  }
  
  
  public static JsonOpBuilder fromJson(String json) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Class.class, new JsonClass())
        .create();
    List<JsonOp> ops = new ArrayList<>();
    JsonParser par = new JsonParser();
    JsonObject obj = (JsonObject) par.parse(json);
    String className = (obj.get("class") != null 
        ? obj.get("class").getAsString() : null);
    JsonArray array = obj.getAsJsonArray("ops");
    for(int i = 0; i < array.size(); i++) {
      ops.add(gson.fromJson(array.get(i), JsonOp.class));
    }
    return new JsonOpBuilder(ops, null, className, null, null);
  }
  
}
