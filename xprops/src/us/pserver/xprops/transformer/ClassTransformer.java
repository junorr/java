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

import us.pserver.tools.Valid;

/**
 * String transformer for class types.
 * @author Juno Roesler - juno@pserver.us
 */
public class ClassTransformer extends AbstractStringTransformer<Class> {
  
  /**
   * <code>CLASS_BOOLEAN = "boolean";</code><br>
   * String for boolean class types.
   */
  private static final String CLASS_BOOLEAN = "boolean";
  
  /**
   * <code>CLASS_BYTE = "byte";</code><br>
   * String for byte class types.
   */
  private static final String CLASS_BYTE = "byte";
  
  /**
   * <code>CLASS_CHAR = "char";</code><br>
   * String for char class types.
   */
  private static final String CLASS_CHAR = "char";
  
  /**
   * <code>CLASS_SHORT = "short";</code><br>
   * String for short class types.
   */
  private static final String CLASS_SHORT = "short";
  
  /**
   * <code>CLASS_INT = "int";</code><br>
   * String for int class types.
   */
  private static final String CLASS_INT = "int";
  
  /**
   * <code>CLASS_LONG = "long";</code><br>
   * String for long class types.
   */
  private static final String CLASS_LONG = "long";
  
  /**
   * <code>CLASS_FLOAT = "float";</code><br>
   * String for float class types.
   */
  private static final String CLASS_FLOAT = "float";
  
  /**
   * <code>CLASS_DOUBLE = "double";</code><br>
   * String for double class types.
   */
  private static final String CLASS_DOUBLE = "double";
  
  

  @Override
  public Class fromString(String str) throws IllegalArgumentException {
    Valid.off(str).forEmpty()
        .fail("Invalid String to Transform: ");
    try {
      switch(str) {
        case CLASS_BOOLEAN:
          return boolean.class;
        case CLASS_BYTE:
          return byte.class;
        case CLASS_CHAR:
          return char.class;
        case CLASS_DOUBLE:
          return double.class;
        case CLASS_FLOAT:
          return float.class;
        case CLASS_INT:
          return int.class;
        case CLASS_LONG:
          return long.class;
        case CLASS_SHORT:
          return short.class;
        default:
          return Class.forName(str);
      }
    } catch(ClassNotFoundException e) {
      throw new IllegalArgumentException(e.toString(), e);
    }
  }
  
  
  @Override
  public String toString(Class cls) throws IllegalArgumentException {
    return Valid.off(cls)
        .forNull().getOrFail(Class.class).getName();
  }

}
