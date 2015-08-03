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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/07/2015
 */
public class ClassTransformer extends AbstractStringTransformer<Class> {
  
  private static final String 
      CLASS_BOOLEAN = "boolean",
      CLASS_BYTE = "byte",
      CLASS_CHAR = "char",
      CLASS_SHORT = "short",
      CLASS_INT = "int",
      CLASS_LONG = "long",
      CLASS_FLOAT = "float",
      CLASS_DOUBLE = "double";
  

  @Override
  public Class fromString(String str) throws IllegalArgumentException {
    Validator.off(str).forEmpty()
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
    return Validator.off(cls)
        .forNull().getOrFail(Class.class).getName();
  }

}
