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

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import java.io.File;
import java.net.SocketAddress;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class PrimitiveArrayTransformer extends AbstractXmlTransformer<Object> {
  
  static final Class[] primitives = {
      new Boolean[0].getClass(),
      new boolean[0].getClass(),
      new Byte[0].getClass(),
      new byte[0].getClass(),
      new char[0].getClass(),
      new Character[0].getClass(),
      new Short[0].getClass(),
      new short[0].getClass(),
      new Integer[0].getClass(),
      new int[0].getClass(),
      new Long[0].getClass(),
      new long[0].getClass(),
      new Float[0].getClass(),
      new float[0].getClass(),
      new Double[0].getClass(),
      new double[0].getClass(),
      new String[0].getClass(),
      new File[0].getClass(),
      new Date[0].getClass(),
      new SocketAddress[0].getClass()
  };
  

  private List list;


  @Override
  public Object transform(String str) throws IllegalArgumentException {
    list = new ListTransformer().transform(str);
    return list;
  }
  
  
  public String reverse(Object obj) throws IllegalArgumentException {
    Valid.off(obj)
        .testNull(Object.class)
        .test(!isPrimitiveArray(obj.getClass()) 
            && !isPrimitiveWrapperArray(obj.getClass()), 
            "Invalid Object Type: "+ obj.getClass().getName());
    
  }
  
  
  public PrimitiveArrayTransformer transform(String str) throws IllegalArgumentException {
    List list = new ListTransformer().transform(str);
    return this;
  }
  
  
  public static boolean isPrimitiveArray(Class cls) {
    if(cls == null) return false;
    boolean primitive = false;
    for(Class c : primitives) {
      primitive = primitive || c.equals(cls);
    }
    return primitive;
  }

}
