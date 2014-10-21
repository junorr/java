/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.json;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/09/2014
 */
public class ListJson {

  private ObjectJson jo;
  
  
  public ListJson(ObjectJson jo) {
    this.jo = jo;
  }
  
  
  public String toJson(Object o) {
    if(o == null) return "null";
    if(jo.isList(o)) {
      return toObjectJsonArray(((List)o).toArray());
    }
    else if(jo.isArray(o)) {
      if(isPrimitiveArray(o))
        return toPrimitiveJsonArray(o);
      else
        return toObjectJsonArray((Object[])o);
    }
    else return jo.toJson(o);
  }
  
  
  private String toObjectJsonArray(Object[] os) {
    if(os == null) return "null";
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for(int i = 0; i < os.length; i++) {
      sb.append(jo.toJson(os[i]));
      if(i < os.length -1)
        sb.append(",");
    }
    sb.append("]");
    return sb.toString();
  }
  
  
  public boolean isPrimitiveArray(Object o) {
    if(o == null || !o.getClass().isArray())
      return false;
    return int[].class.equals(o.getClass())
        || long[].class.equals(o.getClass())
        || byte[].class.equals(o.getClass())
        || short[].class.equals(o.getClass())
        || double[].class.equals(o.getClass())
        || float[].class.equals(o.getClass())
        || char[].class.equals(o.getClass())
        || boolean[].class.equals(o.getClass());
  }
  
  
  private String toPrimitiveJsonArray(Object o) {
    if(o == null) return null;
    if(!isPrimitiveArray(o)) return null;
    Object[] os = null;
    if(int[].class.equals(o.getClass())) {
      int[] array = (int[]) o;
      os = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
        os[i] = array[i];
      }
    }
    else if(long[].class.equals(o.getClass())) {
      long[] array = (long[]) o;
      os = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
        os[i] = array[i];
      }
    }
    else if(byte[].class.equals(o.getClass())) {
      byte[] array = (byte[]) o;
      os = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
        os[i] = array[i];
      }
    }
    else if(short[].class.equals(o.getClass())) {
      short[] array = (short[]) o;
      os = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
        os[i] = array[i];
      }
    }
    else if(double[].class.equals(o.getClass())) {
      double[] array = (double[]) o;
      os = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
        os[i] = array[i];
      }
    }
    else if(float[].class.equals(o.getClass())) {
      float[] array = (float[]) o;
      os = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
        os[i] = array[i];
      }
    }
    else if(boolean[].class.equals(o.getClass())) {
      boolean[] array = (boolean[]) o;
      os = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
        os[i] = array[i];
      }
    }
    else if(char[].class.equals(o.getClass())) {
      char[] array = (char[]) o;
      os = new Object[array.length];
      for(int i = 0; i < array.length; i++) {
        os[i] = array[i];
      }
      return toObjectJsonArray(os);
    }
    return toJsonArray(os);
  }
  
  
  private <T> String toJsonArray(T[] ts) {
    if(ts == null) return "null";
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for(int i = 0; i < ts.length; i++) {
      sb.append(ts[i]);
      if(i < ts.length -1)
        sb.append(",");
    }
    sb.append("]");
    return sb.toString();
  }
  
}
