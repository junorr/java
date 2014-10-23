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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 20/10/2014
 */
public class DocumentFormat {

  private Reflector ref;
  
  private ObjectJson ojs;
  
  
  public DocumentFormat() {
    ref = new Reflector();
    ojs = new ObjectJson();
  }
  
  
  public Document toDoc(Object obj) {
    if(obj == null) 
      return null;
    if(obj instanceof Document)
      return (Document) obj;
    
    Document doc = new Document();
    doc.label(obj.getClass().getName());
    Field[] fs = ref.on(obj).fields();
    for(int i = 0; i < fs.length; i++) {
      Object val = ref.on(obj).field(fs[i].getName()).get();
      if(val == null) continue;
      if(ojs.isPrimitive(val) || ojs.isArray(val) || ojs.isList(val)) {
        doc.put(fs[i].getName(), val);
      } 
      else if(Map.class.isAssignableFrom(fs[i].getType())) {
        Map map = (Map) val;
        Iterator it = map.keySet().iterator();
        int id = 0;
        Document md = new Document(fs[i].getType().getName());
        while(it.hasNext()) {
          Object key = it.next();
          MapEntry ent = new MapEntry();
          ent.setKey(key);
          ent.setValue(map.get(key));
          md.put(String.valueOf(id++), ent);
        }
        doc.put(fs[i].getName(), md);
      }
      else {
        doc.put(fs[i].getName(), toDoc(val));
      }
    }
    return doc;
  }
  
  
  public boolean isPrimitive(Field fld) {
    if(fld == null) return true;
    Class c = fld.getType();
    return Number.class.isAssignableFrom(c)
        || String.class.isAssignableFrom(c)
        || Character.class.isAssignableFrom(c)
        || Boolean.class.isAssignableFrom(c)
        || byte.class.isAssignableFrom(c)
        || short.class.isAssignableFrom(c)
        || int.class.isAssignableFrom(c)
        || long.class.isAssignableFrom(c)
        || float.class.isAssignableFrom(c)
        || double.class.isAssignableFrom(c)
        || char.class.isAssignableFrom(c);
  }
  
  
  public boolean isNumber(Field fld) {
    if(fld == null) return false;
    Class c = fld.getType();
    return Number.class.isAssignableFrom(c)
        || byte.class.isAssignableFrom(c)
        || short.class.isAssignableFrom(c)
        || int.class.isAssignableFrom(c)
        || long.class.isAssignableFrom(c)
        || float.class.isAssignableFrom(c)
        || double.class.isAssignableFrom(c);
  }
  
  
  public boolean isListOrArray(Field fld) {
    if(fld == null) return false;
    Class c = fld.getType();
    return c.isArray()
        || List.class.isAssignableFrom(c);
  }
  
  
  public boolean isList(Field fld) {
    if(fld == null) return false;
    Class c = fld.getType();
    return List.class.isAssignableFrom(c);
  }
  
  
  public boolean isArray(Field fld) {
    if(fld == null) return false;
    Class c = fld.getType();
    return c.isArray();
  }
  
  
  public boolean isPrimitiveArray(Field fld) {
    if(fld == null) return false;
    Class c = fld.getType();
    if(!c.isArray()) return false;
    return boolean[].class.isAssignableFrom(c)
        || char[].class.isAssignableFrom(c)
        || byte[].class.isAssignableFrom(c)
        || short[].class.isAssignableFrom(c)
        || int[].class.isAssignableFrom(c)
        || float[].class.isAssignableFrom(c)
        || long[].class.isAssignableFrom(c)
        || double[].class.isAssignableFrom(c);
  }
  
  
  public Object getPrimitiveArray(Field fld, List lst) {
    if(fld == null 
        || lst == null 
        || lst.isEmpty()
        || !isPrimitiveArray(fld))
      return null;
    Object o = null;
    Class c = fld.getType();
    if(boolean[].class.isAssignableFrom(c)) {
      boolean[] bs = new boolean[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        bs[i] = (boolean) Boolean.parseBoolean(lst.get(i).toString());
      }
      o = bs;
    }
    else if(char[].class.isAssignableFrom(c)) {
      char[] bs = new char[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        bs[i] = (char) lst.get(i).toString().charAt(0);
      }
      o = bs;
    }
    else if(byte[].class.isAssignableFrom(c)) {
      byte[] bs = new byte[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        bs[i] = (byte) Byte.parseByte(lst.get(i).toString());
      }
      o = bs;
    }
    else if(short[].class.isAssignableFrom(c)) {
      short[] bs = new short[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        bs[i] = (short) Short.parseShort(lst.get(i).toString());
      }
      o = bs;
    }
    else if(int[].class.isAssignableFrom(c)) {
      int[] bs = new int[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        bs[i] = (int) Integer.parseInt(lst.get(i).toString());
      }
      o = bs;
    }
    else if(float[].class.isAssignableFrom(c)) {
      float[] bs = new float[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        bs[i] = (float) Float.parseFloat(lst.get(i).toString());
      }
      o = bs;
    }
    else if(long[].class.isAssignableFrom(c)) {
      long[] bs = new long[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        bs[i] = (long) Long.parseLong(lst.get(i).toString());
      }
      o = bs;
    }
    else if(double[].class.isAssignableFrom(c)) {
      double[] bs = new double[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        bs[i] = (double) Double.parseDouble(lst.get(i).toString());
      }
      o = bs;
    }
    return o;
  }
  
  
  public boolean isObjectArray(Field fld) {
    if(fld == null) return false;
    return fld.getType().isArray() 
        && !isPrimitiveArray(fld);
  }
  
  
  public Object getObjectArray(Field fld, List lst) {
    if(fld == null 
        || lst == null 
        || lst.isEmpty()
        || !isObjectArray(fld))
      return null;
    Object o = null;
    Class c = fld.getType();
    if(String[].class.isAssignableFrom(c)) {
      String[] as = new String[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = lst.get(i).toString();
      }
      o = as;
    }
    else if(Boolean[].class.isAssignableFrom(c)) {
      Boolean[] as = new Boolean[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = Boolean.parseBoolean(lst.get(i).toString());
      }
      o = as;
    }
    else if(Character[].class.isAssignableFrom(c)) {
      Character[] as = new Character[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = lst.get(i).toString().charAt(0);
      }
      o = as;
    }
    else if(Byte[].class.isAssignableFrom(c)) {
      Byte[] as = new Byte[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = Byte.parseByte(lst.get(i).toString());
      }
      o = as;
    }
    else if(Short[].class.isAssignableFrom(c)) {
      Short[] as = new Short[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = Short.parseShort(lst.get(i).toString());
      }
      o = as;
    }
    else if(Integer[].class.isAssignableFrom(c)) {
      Integer[] as = new Integer[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = Integer.parseInt(lst.get(i).toString());
      }
      o = as;
    }
    else if(Float[].class.isAssignableFrom(c)) {
      Float[] as = new Float[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = Float.parseFloat(lst.get(i).toString());
      }
      o = as;
    }
    else if(Long[].class.isAssignableFrom(c)) {
      Long[] as = new Long[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = Long.parseLong(lst.get(i).toString());
      }
      o = as;
    }
    else if(Double[].class.isAssignableFrom(c)) {
      Double[] as = new Double[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = Double.parseDouble(lst.get(i).toString());
      }
      o = as;
    }
    else {
      Object[] as = new Object[lst.size()];
      for(int i = 0; i < lst.size(); i++) {
        as[i] = lst.get(i);
        if(Document.class.isAssignableFrom(as[i].getClass())) {
          as[i] = fromDoc((Document)as[i]);
        }
      }
      o = as;
    }
    return o;
  }
  
  
  public Object getNumber(Field fld, String val) {
    if(fld == null || val == null || !isPrimitive(fld)) 
      return null;
    Object o = null;
    Class c = fld.getType();
    try {
      Double d = Double.parseDouble(val);
      if(Byte.class.isAssignableFrom(c)
          || byte.class.isAssignableFrom(c)) {
        o = d.byteValue();
      }
      else if(Short.class.isAssignableFrom(c)
          || short.class.isAssignableFrom(c)) {
        o = d.shortValue();
      }
      else if(Integer.class.isAssignableFrom(c)
          || int.class.isAssignableFrom(c)) {
        o = d.intValue();
      }
      else if(Float.class.isAssignableFrom(c)
          || float.class.isAssignableFrom(c)) {
        o = d.floatValue();
      }
      else if(Long.class.isAssignableFrom(c)
          || long.class.isAssignableFrom(c)) {
        o = d.longValue();
      }
      else if(Double.class.isAssignableFrom(c)
          || double.class.isAssignableFrom(c)) {
        o = d;
      }
    } catch(NumberFormatException e) {}
    return o;
  }
  
  
  public void assign(Object obj, Field fld, Object val) {
    if(obj == null || fld == null || val == null)
      return;
    if(val.equals("null")) return;
    Class c = fld.getType();
    if(isNumber(fld)) {
      ref.on(obj).field(fld.getName())
          .set(getNumber(fld, val.toString()));
    }
    else if(isPrimitive(fld)) {
      Object o = val.toString();
      if(Boolean.class.isAssignableFrom(c)
          || boolean.class.isAssignableFrom(c))
        o = Boolean.parseBoolean(val.toString());
      else if(Character.class.isAssignableFrom(c)
          || char.class.isAssignableFrom(c))
        o = val.toString().charAt(0);
      ref.on(obj).field(fld.getName()).set(o);
    }
    else if(isArray(fld)) {
      Object o = null;
      if(isPrimitiveArray(fld))
        o = getPrimitiveArray(fld, (List) val);
      else
        o = getObjectArray(fld, (List) val);
      ref.on(obj).field(fld.getName()).set(o);
    }
    else if(isList(fld)) {
      ref.on(obj).field(fld.getName()).set(val);
    }
    else if(Document.class.isAssignableFrom(val.getClass())) {
      val = fromDoc((Document) val);
      ref.on(obj).field(fld.getName()).set(val);
    }
    else {
      throw new UnsupportedOperationException("Unknown type for assignment: "+ fld.getName()+ ":"+ c.getName());
    }
  }
  
  
  public Object fromDoc(Document doc) {
    if(doc == null) return null;
    ref.onClass(doc.label());
    if(ref.hasError()) {
      throw new UnsupportedOperationException(
          "Invalid class ("+ doc.label()+ "): "
              + ref.getError().getMessage(), ref.getError());
    }
    Object obj = ref.create();
    UnsupportedOperationException uex = null;
    if(ref.hasError()) {
      uex = new UnsupportedOperationException(
          "No empty constructor for class ("+ doc.label()+ "): "
              + ref.getError().getMessage(), ref.getError());
    }
    else if(obj == null) {
      uex = new UnsupportedOperationException(
          "No empty constructor for class ("+ doc.label()+ ")");
    }
    if(uex != null) throw uex;
    
    Iterator<String> it = doc.map().keySet().iterator();
    while(it.hasNext()) {
      String key = it.next();
      Field f = ref.on(obj).field(key).field();
      if(f == null) continue;
      assign(obj, f, doc.get(key));
    }
    return obj;
  }
  
  
  public static void main(String[] args) {
    class MyObject {
      String text;
      int number;
      char[] chars;
      List list;
      Map map;
      MyObject other;
      public String toString() {
        return "MyObject{" + "text=" + text + ", number=" + number + ", chars=" + Arrays.toString(chars) + ", list=" + list +", other=" + other + '}';
      }
    }
    MyObject obj = new MyObject();
    obj.chars = new char[]{'a', 'b', 'c'};
    obj.number = 9;
    obj.other = new MyObject();
    obj.text = "Hello World";
    obj.other.number = 5;
    obj.other.text = "Other text";
    obj.list = new LinkedList();
    obj.list.add(1);
    obj.list.add(3);
    obj.list.add(5);
    obj.list.add(7);
    obj.list.add(11);
    obj.list.add(13);
    obj.map = new LinkedHashMap();
    obj.map.put("key1", "val1");
    obj.map.put("key2", "val2");
    obj.map.put("key3", "val3");
    
    DocumentFormat odc = new DocumentFormat();
    ObjectJson ojs = new ObjectJson();
    Document doc = odc.toDoc(obj);
    System.out.println("* toDoc : "+ doc);
    System.out.println("* toJson: "+ ojs.toJson(obj));
    JsonParser jps = new JsonParser();
    doc = jps.parsedoc(ojs.toJson(obj));
    System.out.println("* parsedoc: "+ doc);
    obj = (MyObject) odc.fromDoc(doc);
    System.out.println("* obj: "+ obj);
  }
  
}
