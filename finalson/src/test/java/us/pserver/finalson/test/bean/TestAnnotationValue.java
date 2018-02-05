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

package us.pserver.finalson.test.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2018
 */
public class TestAnnotationValue {

  private final Class annotation = Property.class;
  
  @Test
  public void testReflectionValue() {
    Optional<Method> opt = Arrays.asList(AObj.class.getMethods())
        .stream().filter(m->m.isAnnotationPresent(annotation))
        .findAny();
    if(!opt.isPresent()) {
      throw new IllegalStateException("Should have an annotated (@Property) method here");
    }
    Annotation annot = opt.get().getAnnotation(annotation);
    System.out.println("* annotation: "+ annot);
    Reflector ref = new Reflector(annot);
    Object value = ref.selectMethod("value").invoke();
    System.out.println("* value: "+ value);
  }
  
}
