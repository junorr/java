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
import java.util.function.BiFunction;
import us.pserver.orb.annotation.Alias;
import us.pserver.orb.annotation.Annotations;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/02/2019
 */
@FunctionalInterface
public interface MethodBind extends BiFunction<List<String>,Method,String> {

  @Override
  public String apply(List<String> prefix, Method meth) throws MethodBindException;
  
  
  
  public static List<String> createNameList(List<String> prefix, Method meth) {
    List<String> splitcc = new ArrayList<>(prefix);
    int start = splitcc.size();
    Optional<String> annotation = Annotations.getAnnotationValue(Alias.class, meth);
    splitcc.addAll(splitCamelCase(annotation.orElse(meth.getName())));
    if(splitcc.get(start).equals("get")) splitcc.remove(start);
    return splitcc;
  }
  
  
  public static List<String> createNameList(List<String> prefix, Class cls) {
    List<String> splitcc = new ArrayList<>(prefix);
    Optional<String> annotation = Annotations.getAnnotationValue(Alias.class, cls);
    splitcc.addAll(splitCamelCase(annotation.orElse(cls.getSimpleName())));
    return splitcc;
  }
  
  
  public static List<String> splitCamelCase(String str) {
    List<String> lst = new ArrayList<>();
    int lastcc = 0;
    for(int i = 0; i < str.length(); i++) {
      if(Character.isUpperCase(str.charAt(i)) && i > lastcc && i < str.length()) {
        lst.add(str.substring(lastcc, i));
        lastcc = i;
      }
      else if(i == str.length() -1) {
      }
    }
    lst.add(str.substring(lastcc, str.length()));
    return lst;
  }
  
}
