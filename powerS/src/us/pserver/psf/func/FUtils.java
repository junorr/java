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

package us.pserver.psf.func;

import java.util.ArrayList;
import java.util.Date;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSObject;
import us.pserver.date.SimpleDate;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/01/2014
 */
public abstract class FUtils {

  public static void checkLen(ArrayList al, int len) throws FSException {
    int sz = 0;
    if(al != null)
      sz = al.size();
    if(sz < len)
      throw new FSException(
          "Insuficient arguments ["
          + sz+ "], expected ["+ len+ "]");
  }
  
  
  public static String str(ArrayList al, int idx) throws FSException {
    checkLen(al, idx+1);
    return str(al.get(idx));
  }
  
  
  public static String str(Object obj) throws FSException {
    if(obj == null) return null;
    if(obj instanceof byte[])
      return new String((byte[]) obj);
    else if(obj instanceof char[])
      return new String((char[]) obj);
    else if(FSObject.class.isAssignableFrom(obj.getClass()))
      return str(((FSObject)obj).getObject());
    else
      return obj.toString();
  }
  
  
  public static SimpleDate date(Object o) throws FSException {
    if(o == null) return SimpleDate.now();
    if(o instanceof FSObject)
      o = ((FSObject)o).getObject();
    if(o instanceof Date)
      return SimpleDate.from((Date) o);
    String s = FUtils.str(o);
    return SimpleDate.parseDate(s);
  }
  
  
  public static <T> T cast(ArrayList al, int idx) throws FSException {
    checkLen(al, idx+1);
    Object obj = al.get(idx);
    if(obj instanceof FSObject)
      return (T) ((FSObject)obj).getObject();
    return (T) al.get(idx);
  }
  
  
  public static int _int(ArrayList al, int idx) throws FSException {
    checkLen(al, idx+1);
    return _int(al.get(idx));
  }
  
  
  public static double _double(ArrayList al, int idx) throws FSException {
    checkLen(al, idx+1);
    return _double(al.get(idx));
  }
  
  
  public static int _int(Object obj) throws FSException {
    if(obj == null) return -1;
    if(obj instanceof Number)
      return ((Number)obj).intValue();
    else {
      try {
        return Integer.parseInt(str(obj));
      } catch(NumberFormatException e) {
        throw new FSException("Is Not a Number ["+ str(obj)+ "]");
      }
    }
  }
  
  
  public static double _double(Object obj) throws FSException {
    if(obj == null) return -1;
    if(obj instanceof Number)
      return ((Number)obj).doubleValue();
    else {
      try {
        return Double.parseDouble(str(obj));
      } catch(NumberFormatException e) {
        throw new FSException("Is Not a Number ["+ str(obj)+ "]");
      }
    }
  }
  
}
