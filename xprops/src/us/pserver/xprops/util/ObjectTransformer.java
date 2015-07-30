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

package us.pserver.xprops.util;

import java.awt.Color;
import java.io.File;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class ObjectTransformer extends AbstractStringTransformer<Object> {
  
  private final Class type;
  
  
  public ObjectTransformer(final Class type) {
    this.type = Valid.off(type)
        .getOrFail(Class.class);
  }
  
  
  public Class type() {
    return type;
  }
  
  
  @Override
  public Object transform(String str) throws IllegalArgumentException {
    return getTransformer().transform(str);
  }


  @Override
  public String reverse(Object obj) throws IllegalArgumentException {
    if(ObjectTransformer.isSupported(type)) {
      return getTransformer().reverse(obj);
    }
    return Objects.toString(obj);
  }

  
  public StringTransformer getTransformer() throws IllegalArgumentException {
    if(Number.class.isAssignableFrom(type))
      return new NumberTransformer();
    else if(Boolean.class.isAssignableFrom(type))
      return new BooleanTransformer();
    else if(Color.class.isAssignableFrom(type))
      return new ColorTransformer();
    else if(Class.class.isAssignableFrom(type))
      return new ClassTransformer();
    else if(Date.class.isAssignableFrom(type))
      return new DateTransformer();
    else if(File.class.isAssignableFrom(type))
      return new FileTransformer();
    else if(Path.class.isAssignableFrom(type))
      return new PathTransformer();
    else if(List.class.isAssignableFrom(type))
      return new ListTransformer();
    else if(SocketAddress.class.isAssignableFrom(type))
      return new SocketAddressTransformer();
    else if(String.class.isAssignableFrom(type))
      return new StringTransformer();
    else if(type.isPrimitive() && type.isArray())
      return new ListTransformer();
    else throw new IllegalArgumentException(
        "Not Supported Transformation for: "+ type.getName());
  }

  
  public static boolean isSupported(Class cls) throws IllegalArgumentException {
    return cls.isPrimitive()
        || (cls.isPrimitive() && cls.isArray())
        || Number.class.isAssignableFrom(cls)
        || String.class.isAssignableFrom(cls)
        || Boolean.class.isAssignableFrom(cls)
        || Color.class.isAssignableFrom(cls)
        || Class.class.isAssignableFrom(cls)
        || Date.class.isAssignableFrom(cls)
        || File.class.isAssignableFrom(cls)
        || Path.class.isAssignableFrom(cls)
        || List.class.isAssignableFrom(cls)
        || SocketAddress.class.isAssignableFrom(cls);
  }

}
