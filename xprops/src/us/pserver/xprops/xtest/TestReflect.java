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

package us.pserver.xprops.xtest;

import java.lang.reflect.Method;
import us.pserver.xprops.util.TObject;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/07/2015
 */
public class TestReflect {

  
  public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException {
    Object obj = new Object();
    Method m = obj.getClass().getMethod("notify", null);
    System.out.println(obj);
    System.out.println(m);
    System.out.println(m.getReturnType());
    System.out.println(void.class.equals(m.getReturnType()));
    
    System.out.println(TObject.hasDefaultTransformer(String.class));
    obj = new byte[1];
    System.out.println("isArray? "+ Class.forName(obj.getClass().getName()).isArray());
    System.out.println("isPrimitive? "+ Class.forName(obj.getClass().getName()).isPrimitive());
    
    System.out.println("byte[]: "+ obj.getClass());
    obj = new char[1];
    System.out.println("char[]: "+ obj.getClass());
    obj = new boolean[1];
    System.out.println("bool[]: "+ obj.getClass());
    obj = new short[1];
    System.out.println("short[]: "+ obj.getClass());
    obj = new int[1];
    System.out.println("int[]: "+ obj.getClass());
    obj = new long[1];
    System.out.println("long[]: "+ obj.getClass());
    obj = new float[1];
    System.out.println("float[]: "+ obj.getClass());
    obj = new double[1];
    System.out.println("double[]: "+ obj.getClass());
    obj = new Boolean[1];
    System.out.println("Boolean[]: "+ obj.getClass().getName());
    System.out.println("Number.class.isAssignableFrom(int.class): "+ Number.class.isAssignableFrom(int.class));
  }
  
}
