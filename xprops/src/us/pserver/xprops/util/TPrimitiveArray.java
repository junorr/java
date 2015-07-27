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

import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class TPrimitiveArray extends AbstractXmlTransformer<Object> {

  private List list;


  @Override
  public Object apply(String str) throws IllegalArgumentException {
    list = new TList().apply(str);
    return list;
  }
  
  
  public String back(Object obj) throws IllegalArgumentException {
    Valid.off(obj)
        .testNull(Object.class)
        .test(!isPrimitiveArray(obj.getClass()) 
            && !isPrimitiveWrapperArray(obj.getClass()), 
            "Invalid Object Type: "+ obj.getClass().getName());
    
  }
  
  
  public TPrimitiveArray transform(String str) throws IllegalArgumentException {
    List list = new TList().apply(str);
    return this;
  }
  
  
  public static boolean isPrimitiveArray(Class cls) {
    return Valid.off(cls)
        .getOrFail(Class.class)
        .getName().startsWith("[")
        && cls.getName().length() == 2;
  }
  
  
  public static boolean isPrimitiveWrapperArray(Class cls) {
    return Valid.off(cls)
        .getOrFail(Class.class)
        .getName().startsWith("[")
        && (cls.getName().endsWith("Boolean;")
          || cls.getName().endsWith("Byte;")
          || cls.getName().endsWith("Character;")
          || cls.getName().endsWith("String;")
          || cls.getName().endsWith("Integer;")
          || cls.getName().endsWith("Short;")
          || cls.getName().endsWith("Long;")
          || cls.getName().endsWith("Float;")
          || cls.getName().endsWith("Double;")
          || cls.getName().endsWith("Number;")
        );
  }

}
