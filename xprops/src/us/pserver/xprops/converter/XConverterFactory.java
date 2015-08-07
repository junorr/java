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

package us.pserver.xprops.converter;

import java.awt.Color;
import java.io.File;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import us.pserver.xprops.transformer.StringTransformerFactory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/07/2015
 */
public class XConverterFactory {

  public static <T> XConverter<T> getXConverter(Class<T> type) {
    return getXConverter(type, null);
  }
  
  
  public static <T> XConverter<T> getXConverter(Class<T> type, String name) {
    if(Boolean.class.isAssignableFrom(type) 
        || boolean.class.isAssignableFrom(type))
      return (XConverter<T>) new BooleanXConverter();
    else if(Character.class.isAssignableFrom(type) 
        || char.class.isAssignableFrom(type))
      return (XConverter<T>) new CharXConverter();
    else if(String.class.isAssignableFrom(type))
      return (XConverter<T>) new StringXConverter();
    else if(Number.class.isAssignableFrom(type) || isPrimitive(type))
      return (XConverter<T>) new NumberXConverter(type);
    else if(Color.class.isAssignableFrom(type))
      return (XConverter<T>) new ColorXConverter();
    else if(Class.class.isAssignableFrom(type))
      return (XConverter<T>) new ClassXConverter();
    else if(Date.class.isAssignableFrom(type))
      return (XConverter<T>) new DateXConverter();
    else if(File.class.isAssignableFrom(type))
      return (XConverter<T>) new FileXConverter();
    else if(Path.class.isAssignableFrom(type))
      return (XConverter<T>) new PathXConverter();
    else if(SocketAddress.class.isAssignableFrom(type))
      return (XConverter<T>) new SocketAddressXConverter();
    else if(List.class.isAssignableFrom(type))
      return (XConverter<T>) new ListXConverter(type, new ArrayList());
    else if(type.isArray())
      return (XConverter<T>) new ArrayXConverter(type.getComponentType());
    else if(type.isEnum())
      return (XConverter<T>) new EnumXConverter((Class<Enum>)type);
    else
      return (name != null ? new ObjectXConverter(type, name) : new ObjectXConverter(type));
    //else throw new IllegalArgumentException(
      //  "Not Supported Conversion for: "+ type.getName());
  }
  
  
  public static boolean isPrimitive(Class cls) {
    return StringTransformerFactory.isPrimitive(cls);
  }
  
}
