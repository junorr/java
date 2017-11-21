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

package us.pserver.dbone;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import us.pserver.tools.Hash;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/11/2017
 */
public class OUIDFactory {

  
  public static OUID create(Object obj) {
    Class cls = obj.getClass();
    Field[] fls = cls.getDeclaredFields();
    Hash hash = Hash.sha1();
    hash.put(cls.getName());
    Arrays.asList(fls).stream()
        .filter(OUIDFactory::take)
        .forEach(f->calcHash(obj, f, hash));
    return OUID.of(hash.get(), cls.getName());
  }
  
  
  private static void calcHash(Object o, Field f, Hash h) {
    if(!f.isAccessible()) {
      f.setAccessible(true);
    }
    h.put(get(f, o));
  }
  
  
  private static boolean take(Field f) {
    return !Modifier.isStatic(f.getModifiers()) 
        && !Modifier.isTransient(f.getModifiers());
  }
  
  
  private static String get(Field f, Object owner) {
    try {
      return Objects.toString(f.get(owner));
    } catch(IllegalAccessException | IllegalArgumentException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
}
