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

package us.pserver.json;

import com.jpower.rfl.Reflector;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/09/2014
 */
public class ObjectJson {
  
  private Reflector rf;
  
  private ListJson jl;
  
  
  public ObjectJson() {
    rf = new Reflector();
    jl = new ListJson(this);
  }
  
  
  public boolean isPrimitive(Object o) {
    if(o == null) return true;
    return Number.class.isAssignableFrom(o.getClass())
        || String.class.isAssignableFrom(o.getClass())
        || Boolean.class.isAssignableFrom(o.getClass())
        || Character.class.isAssignableFrom(o.getClass())
        || Byte.class.isAssignableFrom(o.getClass());
  }
  
  
  public String primitiveToJson(Object o) {
    if(o == null) return "null";
    if(!isPrimitive(o)) return toJson(o);
    if(String.class.isAssignableFrom(o.getClass())
        || char.class.isAssignableFrom(o.getClass())
        || Character.class.isAssignableFrom(o.getClass())) {
      return "'" + o.toString() + "'";
    }
    else {
      return String.valueOf(o);
    }
  }
  
  
  public boolean isList(Object o) {
    if(o == null) return false;
    return List.class.isAssignableFrom(o.getClass());
  }
  
  
  public boolean isArray(Object o) {
    if(o == null) return false;
    return o.getClass().isArray();
  }
  
  
  public boolean isField(Object o) {
    if(o == null) return false;
    return Field.class.isAssignableFrom(o.getClass());
  }
  
  
  public String toJson(Field f, Object o) {
    if(f == null || o == null)
      return "null";
    Object val = rf.on(o).field(f.getName()).get();
    StringBuffer sb = new StringBuffer();
    sb.append("{'")
        .append(f.getName())
        .append("':")
        .append(toJson(val))
        .append("}");
    return sb.toString();
  }
  
  
  public String toJson(Field[] fs, Object o) {
    if(fs == null || o == null)
      return "null";
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    for(int i = 0; i < fs.length; i++) {
      sb.append(toJson(fs[i], o));
      if(i < fs.length -1)
        sb.append(",");
    }
    return sb.append("]").toString();
  }
  
  
  public String toJson(Object o) {
    if(o == null) return "null";
    if(isPrimitive(o))
      return primitiveToJson(o);
    if(isList(o) || isArray(o))
      return jl.toJson(o);
    
    Reflector rf = new Reflector();
    Field[] fs = rf.on(o).fields();
    StringBuilder sb = new StringBuilder();
    sb.append("{")
        .append("'class':'")
        .append(o.getClass().getName())
        .append("',")
        .append("'fields':")
        .append(toJson(fs, o));
    return sb.append("}").toString();
  }
  
  
  public static void main(String[] args) {
    int i = 5;
    boolean b = true;
    Double d = 1.2;
    String s = "string";
    byte y = 0;
    char c = 35;
    ObjectJson jo = new ObjectJson();
    System.out.println("* i="+ i+ ", isPrimitive? "+ jo.isPrimitive(i));
    System.out.println("* b="+ b+ ", isPrimitive? "+ jo.isPrimitive(b));
    System.out.println("* d="+ d+ ", isPrimitive? "+ jo.isPrimitive(d));
    System.out.println("* s="+ s+ ", isPrimitive? "+ jo.isPrimitive(s));
    System.out.println("* y="+ y+ ", isPrimitive? "+ jo.isPrimitive(y));
    System.out.println("* c="+ c+ ", isPrimitive? "+ jo.isPrimitive(c));
    int[] is = {1, 2, 3, 4, 5};
    System.out.println("* "+ Arrays.toString(is) + ", isArray? "+ jo.isArray(is));
    
    System.out.println("===========================");
    
    class A {
      int integer;
      String string;
      byte[] bytes;
      List list;
    }
    
    A a = new A();
    a.integer = 5;
    a.string = "text";
    a.bytes = new byte[]{0,1,2,3,4,5};
    a.list = new LinkedList();
    a.list.add(10);
    a.list.add(20);
    a.list.add("string");
    a.list.add('c');
    a.list.add(30);
    a.list.add(40);
    System.out.println("* toJson(a) = "+ jo.toJson(a));
  }
  
}
