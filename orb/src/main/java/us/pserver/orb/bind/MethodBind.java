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

package us.pserver.orb.bind;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import us.pserver.orb.annotation.ConfigProperty;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/02/2019
 */
@FunctionalInterface
public interface MethodBind extends Function<Method,String> {

  @Override
  public String apply(Method meth) throws MethodBindException;
  
  
  public static boolean isConfigPropertyAnnotationPresent(Method meth) throws MethodBindException {
    return meth.getAnnotation(ConfigProperty.class) != null;
  }
  
  public static Optional<String> getConfigPropertyAnnotationValue(Method meth) throws MethodBindException {
    return Optional.ofNullable(meth.getAnnotation(ConfigProperty.class))
        .map(c -> c.value());
  }
  
  public static List<String> splitCamelCase(String str) {
    List<String> lst = new ArrayList<>();
    int lastcc = 0;
    for(int i = 0; i < str.length(); i++) {
      if(Character.isUpperCase(str.charAt(i)) && i > lastcc && i < str.length() -1) {
        lst.add(str.substring(lastcc, i));
        lastcc = i;
      }
    }
    return lst;
  }
  
}
