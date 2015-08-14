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

package us.pserver.xprops.transformer;

import java.awt.Color;
import java.io.File;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.Date;

/**
 * Factory class for getting default string transformers.
 * @author Juno Roesler - juno@pserver.us
 */
public class StringTransformerFactory {

  
  /**
   * Get a transformer accordingly to the specified type.
   * @param <T> The type of the class
   * @param type The type of object to transform.
   * @return A StringTransfomer for the specified type.
   */
  public static <T> StringTransformer<T> getTransformer(Class<T> type) {
    if(Boolean.class.isAssignableFrom(type) 
        || boolean.class.isAssignableFrom(type))
      return (StringTransformer<T>) new BooleanTransformer();
    else if(Character.class.isAssignableFrom(type) 
        || char.class.isAssignableFrom(type))
      return (StringTransformer<T>) new CharTransformer();
    else if(String.class.isAssignableFrom(type))
      return (StringTransformer<T>) new StringTransformer<String>() {
        @Override public String fromString(String str) { return str; }
        @Override public String toString(String obj) { return obj; }
      };
    else if(Number.class.isAssignableFrom(type) || isPrimitive(type))
      return (StringTransformer<T>) new NumberTransformer();
    else if(Color.class.isAssignableFrom(type))
      return (StringTransformer<T>) new ColorTransformer();
    else if(Class.class.isAssignableFrom(type))
      return (StringTransformer<T>) new ClassTransformer();
    else if(Date.class.isAssignableFrom(type))
      return (StringTransformer<T>) new DateTransformer();
    else if(File.class.isAssignableFrom(type))
      return (StringTransformer<T>) new FileTransformer();
    else if(Path.class.isAssignableFrom(type))
      return (StringTransformer<T>) new PathTransformer();
    else if(SocketAddress.class.isAssignableFrom(type))
      return (StringTransformer<T>) new SocketAddressTransformer();
    else throw new IllegalArgumentException(
        "Not Supported Transformation for: "+ type.getName());
  }
  
  
  /**
   * Check if the specified type is a java 
   * primitive or object wrapper equivalent.
   * @param cls The type to be checked as primitive.
   * @return <code>true</code> if the specified type
   * is a java primitive or a object wrapper equivalent,
   * <code>false</code> otherwise.
   */
  public static boolean isPrimitive(Class cls) {
    boolean primitive = false;
    for(Class c : primitives) {
      primitive = primitive || c.equals(cls);
    }
    return primitive;
  }
  
  
  /**
   * Check if the specified type is a java 
   * primitive or an object natively supported by
   * with default converters/transformers.
   * @param type The type to be checked as primitive.
   * @return <code>true</code> if the specified type
   * is a java primitive or a object wrapper equivalent,
   * <code>false</code> otherwise.
   */
  public static boolean isSupportedValue(Class type) {
    if(isPrimitive(type)) return true;
    for(Class c : supported) {
      if(c.equals(type))
        return true;
    }
    return false;
  }
  
  
  /**
   * Array with supported types.
   */
  static final Class[] supported = {
      Boolean.class,
      Byte.class,
      Character.class,
      Short.class,
      Integer.class,
      Long.class,
      Float.class,
      Double.class,
      String.class,
      File.class,
      Class.class,
      Date.class,
      SocketAddress.class
  };
  

  /**
   * Array with java primitive types.
   */
  static final Class[] primitives = {
      boolean.class,
      byte.class,
      char.class,
      short.class,
      int.class,
      long.class,
      float.class,
      double.class
  };
}
