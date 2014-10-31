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

package us.pserver.sdb.util;

import com.jpower.rfl.Reflector;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import us.pserver.sdb.Document;
import us.pserver.sdb.OID;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/10/2014
 */
public class ObjectUtils {

  
  public ObjectUtils() {
  }
  
  
  public static Document forExample(Object obj) {
    if(obj == null) return null;
    if(obj instanceof OID)
      obj = ((OID)obj).get();
    if(obj == null) return null;
    
    Document doc = new Document(obj.getClass().getName());
    Reflector ref = new Reflector();
    Field[] fls = ref.on(obj).fields();
    for(int i = 0; i < fls.length; i++) {
      Class c = fls[i].getType();
      Object val = ref.on(obj).field(fls[i].getName()).get();
      if(val != null && !val.toString().equals("0")
          && !val.toString().equals("false")
          && !val.toString().equals("null")
          && !val.toString().isEmpty()) {
        if(isPrimitive(c) || isArray(c) || isList(c) || isMap(c)) {
          doc.put(fls[i].getName(), val);
        }
        else {
          doc.put(fls[i].getName(), forExample(val));
        }
      }
    }
    return doc;
  }
  
  
  public static Document toDocument(Object obj, boolean includeNullFields) {
    if(obj == null) return null;
    if(obj instanceof OID)
      obj = ((OID)obj).get();
    if(obj == null) return null;
    
    Document doc = new Document(obj.getClass().getName());
    Reflector ref = new Reflector();
    Field[] fls = ref.on(obj).fields();
    for(int i = 0; i < fls.length; i++) {
      Class c = fls[i].getType();
      Object val = ref.on(obj).field(fls[i].getName()).get();
      if(val == null && includeNullFields) {
        doc.put(fls[i].getName(), val);
      }
      else if(isPrimitive(c) || isArray(c) || isList(c) || isMap(c)) {
        doc.put(fls[i].getName(), val);
      }
      else {
        doc.put(fls[i].getName(), toDocument(val, includeNullFields));
      }
    }
    return doc;
  }
  
  
  public static Object fromDocument(Document doc) {
    if(doc == null || doc.label() == null)
      return null;
    
    Reflector ref = new Reflector();
    ref.onClass(doc.label());
    Object obj = ref.create();
    if(ref.hasError() || obj == null) 
      return null;
    
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      Object val = doc.get(key);
      if(val == null) continue;
      if(Document.class.isAssignableFrom(val.getClass())) {
        val = fromDocument((Document)val);
      }
      ref.on(obj).field(key);
      if(!ref.isFieldPresent())
        continue;
      ref.set(val);
    }
    return obj;
  }
  
  
  public static boolean isPrimitive(Class c) {
    return c == null
        || String.class.isAssignableFrom(c)
        || Number.class.isAssignableFrom(c)
        || Character.class.isAssignableFrom(c)
        || Boolean.class.isAssignableFrom(c)
        || boolean.class.isAssignableFrom(c)
        || char.class.isAssignableFrom(c)
        || byte.class.isAssignableFrom(c)
        || short.class.isAssignableFrom(c)
        || int.class.isAssignableFrom(c)
        || float.class.isAssignableFrom(c)
        || long.class.isAssignableFrom(c)
        || double.class.isAssignableFrom(c);
  }
  
  
  public static boolean isArray(Class c) {
    if(c == null) return false;
    return c.isArray();
  }
  
  
  public static boolean isList(Class c) {
    if(c == null) return false;
    return List.class.isAssignableFrom(c);
  }
  
  
  public static boolean isMap(Class c) {
    if(c == null) return false;
    return Map.class.isAssignableFrom(c);
  }
  
  
  public static boolean isPrimitive(Object obj) {
    if(obj == null) return false;
    return isPrimitive(obj.getClass());
  }
  
  
  public static boolean isArray(Object obj) {
    if(obj == null) return false;
    return isArray(obj.getClass());
  }
  
  
  public static boolean isList(Object obj) {
    if(obj == null) return false;
    return isList(obj.getClass());
  }
  
  
  public static boolean isMap(Object obj) {
    if(obj == null) return false;
    return isMap(obj.getClass());
  }
  
  
  public static String doc2str(Document doc) {
    if(doc == null) return null;
    StringBuffer sb = new StringBuffer();
    sb.append(doc.label())
        .append("{");
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      Object v = doc.get(key);
      if(v == null) continue;
      sb.append(key).append(":");
      if(v instanceof Document)
        v = doc2str((Document)v);
      sb.append(v.toString());
      if(it.hasNext())
        sb.append(",");
    }
    return sb.append("}").toString();
  }
  
  
  public static void main(String[] args) {
    class MyObject {
      int number;
      String text;
      char[] array;
      MyObject object;
      public String toString() {
        return "MyObject{" + "number=" + number + ", text=" + text + ", array=" + Arrays.toString(array) + ", object=" + object + '}';
      }
    }
    MyObject obj = new MyObject();
    obj.array = new char[]{'a', 'b', 'c'};
    obj.number = 9;
    obj.object = new MyObject();
    obj.text = "Hello";
    obj.object.number = 5;
    obj.object.text = "World";
    System.out.println("* myobject="+ obj);
    Document doc = toDocument(obj, true);
    System.out.println("* document="+ doc2str(doc));
    MyObject my = (MyObject) fromDocument(doc);
    System.out.println("* fromdoc="+ my);
  }
  
}
