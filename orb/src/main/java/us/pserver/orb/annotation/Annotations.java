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

package us.pserver.orb.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/02/2019
 */
public abstract class Annotations {

  public static boolean isAnnotationPresent(Class annotation, Method meth) {
    return meth.getAnnotation(annotation) != null;
  }
  
  public static boolean isAnnotationPresent(Class annotation, Class cls) {
    return cls.getAnnotation(annotation) != null;
  }
  
  public static <A extends Annotation> Optional<A> getAnnotation(Class<A> annotation, Method meth) {
    return Optional.ofNullable((A) meth.getAnnotation(annotation));
  }
  
  public static <A extends Annotation> Optional<A> getAnnotation(Class<A> annotation, Class cls) {
    return Optional.ofNullable((A) cls.getAnnotation(annotation));
  }
  
  public static Optional<String> getAnnotationProperty(String property, Class annotation, Method meth) {
    return getAnnotation(annotation, meth)
        .map(a -> Objects.toString(Reflector.of(a).selectMethod(property).invoke()));
  }
  
  public static Optional<String> getAnnotationProperty(String property, Class annotation, Class cls) {
    return getAnnotation(annotation, cls)
        .map(a -> Objects.toString(Reflector.of(a).selectMethod(property).invoke()));
  }
  
  public static Optional<String> getAnnotationValue(Class annotation, Method meth) {
    return getAnnotationProperty("value", annotation, meth);
  }
  
  public static Optional<String> getAnnotationValue(Class annotation, Class cls) {
    return getAnnotationProperty("value", annotation, cls);
  }
  
}
