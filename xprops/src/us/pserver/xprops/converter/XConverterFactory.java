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

import us.pserver.xprops.util.*;
import java.awt.Color;
import java.io.File;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/07/2015
 */
public class XConverterFactory {

  public static <T> XConverter<T> getXConverter(Class<T> type) {
    if(Number.class.isAssignableFrom(type))
      return (XConverter<T>) new XNumber();
    else if(Boolean.class.isAssignableFrom(type))
      return (XConverter<T>) new XBoolean();
    else if(Color.class.isAssignableFrom(type))
      return (XConverter<T>) new XColor();
    else if(Class.class.isAssignableFrom(type))
      return (XConverter<T>) new XClass();
    else if(Date.class.isAssignableFrom(type))
      return (XConverter<T>) new XDate();
    else if(File.class.isAssignableFrom(type))
      return (XConverter<T>) new XFile();
    else if(Path.class.isAssignableFrom(type))
      return (XConverter<T>) new XPath();
    else if(SocketAddress.class.isAssignableFrom(type))
      return (XConverter<T>) new XSocketAddress();
    else if(String.class.isAssignableFrom(type))
      return (XConverter<T>) new XString();
    /* 
    else if(type.isPrimitive() && type.isArray())
      return new ListTransformer();*/
    else throw new IllegalArgumentException(
        "Not Supported Transformation for: "+ type.getName());
  }
  
  
  public static boolean isSupported(Class cls) {
    return StringTransformerFactory.isSupported(cls);
  }
  
  
  public static boolean isPrimitive(Class cls) {
    return StringTransformerFactory.isPrimitive(cls);
  }
  
}
