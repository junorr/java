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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static us.pserver.sjson.JsonObject.COMMA;
import static us.pserver.sjson.JsonObject.SQUARE_C;
import static us.pserver.sjson.JsonObject.SQUARE_O;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 23/12/2014
 */
public class JsonArray {

  private List<JsonObject> list;
  
  
  public JsonArray() {
    list = new LinkedList<>();
  }
  
  
  public JsonArray(Object obj) {
    this();
    set(obj);
  }
  
  
  public JsonArray set(Object obj) {
    if(!isArray(obj))
      throw new IllegalArgumentException("Object is not Array: "+ obj);
    Object[] objs = toObjectArray(obj);
    for(int i = 0; i < objs.length; i++) {
      put(objs[i]);
    }
    return this;
  }
  
  
  public JsonArray put(Object obj) {
    if(obj != null)
      list.add(new JsonObject(obj));
    return this;
  }
  
  
  public Object get(int idx) {
    Object o = null;
    if(idx >= 0 && idx < list.size())
      o = list.get(idx).get();
    return o;
  }
  
  
  public JsonObject getJson(int idx) {
    JsonObject o = null;
    if(idx >= 0 && idx < list.size())
      o = list.get(idx);
    return o;
  }
  
  
  public Object rm(int idx) {
    Object o = null;
    if(idx >= 0 && idx < list.size())
      o = list.remove(idx).get();
    return o;
  }
  
  
  public JsonArray rm(Object obj) {
    if(obj != null && list.contains(obj))
      list.remove(obj);
    return this;
  }
  
  
  public JsonArray clear() {
    list.clear();
    return this;
  }
  
  
  public List<JsonObject> list() {
    return list;
  }
  
  
  public Object[] getObjectArray() {
    return list.toArray();
  }
  
  
  public String[] getStringArray() {
    List<String> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      if(JsonPrimitive.isPrimitive(list.get(i)))
        ls.add(String.valueOf(list.get(i).get()));
    }
    String[] ss = new String[ls.size()];
    return ls.toArray(ss);
  }
  
  
  public char[] getCharArray() {
    List<Character> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      if(JsonPrimitive.isPrimitive(list.get(i)))
        ls.add(String.valueOf(list.get(i).get()).charAt(0));
    }
    char[] cs = new char[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public static Double toDouble(String str) {
    try {
      return Double.parseDouble(str);
    } catch(NumberFormatException e) {
      return null;
    }
  }
  
  
  public byte[] getByteArray() {
    List<Byte> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      Double d = toDouble(String.valueOf(
            list.get(i).get()));
      if(d != null)
        ls.add(d.byteValue());
    }
    byte[] cs = new byte[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public boolean[] getBooleanArray() {
    List<Boolean> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      if(JsonPrimitive.isPrimitive(list.get(i))
          && (list.get(i).toString().trim().equalsIgnoreCase("true")
          ||  list.get(i).toString().trim().equalsIgnoreCase("false")))
        ls.add(Boolean.parseBoolean(String.valueOf(
            list.get(i).get())));
    }
    boolean[] cs = new boolean[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public short[] getShortArray() {
    List<Short> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      Double d = toDouble(String.valueOf(
            list.get(i).get()));
      if(d != null)
        ls.add(d.shortValue());
    }
    short[] cs = new short[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public int[] getIntArray() {
    List<Integer> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      Double d = toDouble(String.valueOf(
            list.get(i).get()));
      if(d != null)
        ls.add(d.intValue());
    }
    int[] cs = new int[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public long[] getLongArray() {
    List<Long> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      Double d = toDouble(String.valueOf(
            list.get(i).get()));
      if(d != null)
        ls.add(d.longValue());
    }
    long[] cs = new long[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public float[] getFloatArray() {
    List<Float> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      Double d = toDouble(String.valueOf(
            list.get(i).get()));
      if(d != null)
        ls.add(d.floatValue());
    }
    float[] cs = new float[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public double[] getDoubleArray() {
    List<Double> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      Double d = toDouble(String.valueOf(
            list.get(i).get()));
      if(d != null)
        ls.add(d);
    }
    double[] cs = new double[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public List<Number> getDoubleArray() {
    List<Number> ls = new LinkedList<>();
    for(int i = 0; i < list.size(); i++) {
      Double d = toDouble(String.valueOf(
            list.get(i).get()));
      if(d != null)
        ls.add(d);
    }
    double[] cs = new double[ls.size()];
    for(int i = 0; i < cs.length; i++) {
      cs[i] = ls.get(i);
    }
    return cs;
  }
  
  
  public static boolean isPrimitiveArray(Object obj) {
    if(obj == null) return false;
    return isPrimitiveArray(obj.getClass());
  }
  
  
  public static boolean isPrimitiveArray(Class cls) {
    if(cls == null) return false;
    return byte[].class.isAssignableFrom(cls)
        || char[].class.isAssignableFrom(cls)
        || short[].class.isAssignableFrom(cls)
        || boolean[].class.isAssignableFrom(cls)
        || int[].class.isAssignableFrom(cls)
        || long[].class.isAssignableFrom(cls)
        || float[].class.isAssignableFrom(cls)
        || double[].class.isAssignableFrom(cls);/*
        || Byte[].class.isAssignableFrom(cls)
        || Character[].class.isAssignableFrom(cls)
        || Short[].class.isAssignableFrom(cls)
        || Boolean[].class.isAssignableFrom(cls)
        || Integer[].class.isAssignableFrom(cls)
        || Long[].class.isAssignableFrom(cls)
        || Float[].class.isAssignableFrom(cls)
        || Double[].class.isAssignableFrom(cls)
        || String[].class.isAssignableFrom(cls);*/
  }
  
  
  public static boolean isByteArray(Class cls) {
    return byte[].class.isAssignableFrom(cls);
  }
  
  
  public static boolean isCharArray(Class cls) {
    return char[].class.isAssignableFrom(cls);
  }
  
  
  public static boolean isShortArray(Class cls) {
    return short[].class.isAssignableFrom(cls);
  }
  
  
  public static boolean isBooleanArray(Class cls) {
    return boolean[].class.isAssignableFrom(cls);
  }
  
  
  public static boolean isIntArray(Class cls) {
    return int[].class.isAssignableFrom(cls);
  }
  
  
  public static boolean isLongArray(Class cls) {
    return long[].class.isAssignableFrom(cls);
  }
  
  
  public static boolean isFloatArray(Class cls) {
    return float[].class.isAssignableFrom(cls);
  }
  
  
  public static boolean isDoubleArray(Class cls) {
    return double[].class.isAssignableFrom(cls);
  }
  
  
  public static Object[] toObjectArray(byte[] array) {
    if(array == null) return null;
    Object[] objs = new Object[array.length];
    for(int i = 0; i < array.length; i++) {
      objs[i] = array[i];
    }
    return objs;
  }
  
  
  public static Object[] toObjectArray(char[] array) {
    if(array == null) return null;
    Object[] objs = new Object[array.length];
    for(int i = 0; i < array.length; i++) {
      objs[i] = array[i];
    }
    return objs;
  }
  
  
  public static Object[] toObjectArray(short[] array) {
    if(array == null) return null;
    Object[] objs = new Object[array.length];
    for(int i = 0; i < array.length; i++) {
      objs[i] = array[i];
    }
    return objs;
  }
  
  
  public static Object[] toObjectArray(boolean[] array) {
    if(array == null) return null;
    Object[] objs = new Object[array.length];
    for(int i = 0; i < array.length; i++) {
      objs[i] = array[i];
    }
    return objs;
  }
  
  
  public static Object[] toObjectArray(int[] array) {
    if(array == null) return null;
    Object[] objs = new Object[array.length];
    for(int i = 0; i < array.length; i++) {
      objs[i] = array[i];
    }
    return objs;
  }
  
  
  public static Object[] toObjectArray(long[] array) {
    if(array == null) return null;
    Object[] objs = new Object[array.length];
    for(int i = 0; i < array.length; i++) {
      objs[i] = array[i];
    }
    return objs;
  }
  
  
  public static Object[] toObjectArray(float[] array) {
    if(array == null) return null;
    Object[] objs = new Object[array.length];
    for(int i = 0; i < array.length; i++) {
      objs[i] = array[i];
    }
    return objs;
  }
  
  
  public static Object[] toObjectArray(double[] array) {
    if(array == null) return null;
    Object[] objs = new Object[array.length];
    for(int i = 0; i < array.length; i++) {
      objs[i] = array[i];
    }
    return objs;
  }
  
  
  public static Object[] toObjectArray(Object obj) {
    if(obj == null || !isArray(obj))
      return null;
    if(isObjectArray(obj))
      return (Object[]) obj;
    if(isListArray(obj))
      return ((List) obj).toArray();
    Class c = obj.getClass();
    if(isPrimitiveArray(c)) {
      if(isByteArray(c))
        return toObjectArray((byte[]) obj);
      else if(isCharArray(c))
        return toObjectArray((char[]) obj);
      else if(isShortArray(c))
        return toObjectArray((short[]) obj);
      else if(isBooleanArray(c))
        return toObjectArray((boolean[]) obj);
      else if(isIntArray(c))
        return toObjectArray((int[]) obj);
      else if(isLongArray(c))
        return toObjectArray((long[]) obj);
      else if(isFloatArray(c))
        return toObjectArray((float[]) obj);
      else
        return toObjectArray((double[]) obj);
    }
    return null;
  }
  
  
  public static boolean isObjectArray(Class cls) {
    if(cls == null) return false;
    return Object[].class.isAssignableFrom(cls);
  }
  
  
  public static boolean isObjectArray(Object obj) {
    if(obj == null) return false;
    return isObjectArray(obj.getClass());
  }
  
  
  public static boolean isListArray(Class cls) {
    if(cls == null) return false;
    return List.class.isAssignableFrom(cls);
  }
  
  
  public static boolean isListArray(Object obj) {
    if(obj == null) return false;
    return isListArray(obj.getClass());
  }
  
  
  public static boolean isArray(Class cls) {
    return isPrimitiveArray(cls)
        || isObjectArray(cls)
        || isListArray(cls);
  }
  
  
  public static boolean isArray(Object obj) {
    return isPrimitiveArray(obj)
        || isObjectArray(obj)
        || isListArray(obj);
  }
  
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(SQUARE_O);
    for(int i = 0; i < list.size(); i++) {
      sb.append(list.get(i).toString());
      if(i < list.size()-1)
        sb.append(COMMA);
    }
    sb.append(SQUARE_C);
    return sb.toString();
  }
  
  
  public static void main(String[] args) {
    String[] ss = { "string array" };
    System.out.println("* ss = "+ Arrays.toString(ss));
    JsonArray array = new JsonArray(ss);
    System.out.println("* array = " + array);
  }
  
}
