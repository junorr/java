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
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/07/2015
 */
public class StringTransformerFactory {

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
  
  
  public static boolean isPrimitive(Class cls) {
    boolean primitive = false;
    for(Class c : primitives) {
      primitive = primitive || c.equals(cls);
    }
    return primitive;
  }
  
  
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
      Date.class,
      SocketAddress.class
  };
  

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
