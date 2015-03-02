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

package us.pserver.rob.channel;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/03/2015
 */
public class ClassHelper {

  public static final String
      INT = "int",
      AINT = "[I",
      BYTE = "byte",
      ABYTE = "[B",
      CHAR = "char",
      ACHAR = "[C",
      BOOL = "boolean",
      ABOOL = "[Z",
      SHORT = "short",
      ASHORT = "[S",
      LONG = "long",
      ALONG = "[J",
      FLOAT = "float",
      AFLOAT = "[F",
      DOUBLE = "double",
      ADOUBLE = "[D";
  
  
  public static Class getClass(String name) throws ClassNotFoundException {
    if(name == null || name.trim().isEmpty())
      return null;
    switch(name) {
      case ABOOL:
        return boolean[].class;
      case ABYTE:
        return byte[].class;
      case ACHAR:
        return char[].class;
      case ADOUBLE:
        return double[].class;
      case AFLOAT:
        return float[].class;
      case AINT:
        return int[].class;
      case ALONG:
        return long[].class;
      case ASHORT:
        return short[].class;
      case BOOL:
        return boolean.class;
      case BYTE:
        return byte.class;
      case CHAR:
        return char.class;
      case DOUBLE:
        return double.class;
      case FLOAT:
        return float.class;
      case INT:
        return int.class;
      case LONG:
        return long.class;
      case SHORT:
        return short.class;
      default:
        return Class.forName(name);
    }
  }
  
  
  public static boolean isNumber(Class cls) {
    if(cls == null) return false;
    if(!cls.isPrimitive())
      return Number.class.isAssignableFrom(cls);
    else
      switch(cls.getName()) {
        case BYTE:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
          return true;
        default:
          return false;
    }
  }
  
  
  public static void main(String[] args) throws ClassNotFoundException {
    Class cls = int[].class;
    String name = cls.getName();
    System.out.println(name);
    System.out.println(getClass(name));
  }
}
