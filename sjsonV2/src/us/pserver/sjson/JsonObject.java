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

package us.pserver.sjson;

import com.jpower.rfl.Reflector;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/12/2014
 */
public class JsonObject {
  
  public static final String 
      QUOTA = "\"", 
      COMMA = ",",
      SQUARE_O = "[",
      SQUARE_C = "]",
      BRACKET_O = "{",
      BRACKET_C = "}",
      DOTS = ":";
  

  private Class cls;
  
  private Map<String, Object> map;
  
  private Object obj;
  
  private Reflector ref;
  
  
  public JsonObject() {
    cls = null;
    obj = null;
    init();
  }
  
  
  private void init() {
    if(map == null)
      map = new LinkedHashMap<>();
    if(ref == null)
      ref = new Reflector();
  }
  
  
  public JsonObject(Object obj) {
    this();
    set(obj);
  }
  
  
  public JsonObject set(Object obj) {
    if(obj != null) {
      init();
      this.obj = obj;
      this.cls = obj.getClass();
      
      if(JsonPrimitive.isPrimitive(obj) 
          || JsonArray.isArray(obj))
        return this;
      
      ref.on(obj).constructor(null);
      if(!JsonPrimitive.isPrimitive(obj) 
          && !ref.constructor(null).isConstructorPresent())
        throw new UnsupportedOperationException(
            "Object not contains empty constructor: "+ obj);
      String[] fls = ref.fieldNames();
      for(String f : fls) {
        Object fld = ref.field(f).get();
        if(JsonPrimitive.isPrimitive(fld))
          map.put(f, new JsonPrimitive(fld));
        else if(JsonArray.isArray(fld))
          map.put(f, new JsonArray(fld));
        else
          map.put(f, new JsonObject(fld));
      }
    }
    return this;
  }
  
  
  public JsonObject set(Map mp) {
    if(mp != null) {
      cls = mp.getClass();
      Iterator it = mp.keySet().iterator();
      while(it.hasNext()) {
        Object k = it.next();
        Object o = mp.get(k);
        if(JsonPrimitive.isPrimitive(o))
          map.put(k.toString(), new JsonPrimitive(o));
        else if(JsonArray.isArray(o))
          map.put(k.toString(), new JsonArray(o));
        else
          map.put(k.toString(), new JsonObject(o));
      }
    }
    return this;
  }
  
  
  public Map<String, Object> map() {
    return map;
  }
  
  
  public Class clazz() {
    return cls;
  }
  
  
  public Object get() {
    return obj;
  }


  @Override
  public String toString() {
    if(JsonPrimitive.isPrimitive(obj))
      return new JsonPrimitive(obj).toString();
    else if(JsonArray.isArray(obj))
      return new JsonArray(obj).toString();
    
    StringBuilder sb = new StringBuilder();
    sb.append(BRACKET_O)
        .append(QUOTA)
        .append("class")
        .append(QUOTA)
        .append(DOTS)
        .append(QUOTA)
        .append(cls)
        .append(QUOTA);
    Iterator<String> it = map.keySet().iterator();
    while(it.hasNext()) {
      String k = it.next();
      sb.append(COMMA)
          .append(QUOTA)
          .append(k)
          .append(QUOTA)
          .append(DOTS)
          .append(map.get(k));
    }
    sb.append(BRACKET_C);
    return sb.toString();
  }
  
  
  public static void main(String[] args) {
    class B {
      public B() {}
      String str;
      double dbl;
      public String toString() {
        return "B{str:"+str+",dbl:"+dbl+"}";
      }
    }
    class A {
      public A() {}
      String astr;
      double adbl;
      double[] array;
      B b;
      public String toString() {
        return "A{astr:"+astr+",adbl:"+adbl+",b:"+b+"}";
      }
    }
    
    A a = new A();
    a.astr = "some A";
    a.adbl = 3.1;
    a.array = new double[]{ 1, 2, 3, 4, 0 };
    a.b = new B();
    a.b.str = "some B";
    a.b.dbl = 5.2;
    
    System.out.println("* a = "+ a);
    System.out.println("* JsonObject = "+ new JsonObject(a));
  }
  
}
