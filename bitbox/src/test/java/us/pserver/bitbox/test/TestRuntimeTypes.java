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

package us.pserver.bitbox.test;

import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.Reflect;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26 de abr de 2019
 */
public class TestRuntimeTypes {

  @Test
  public void test() {
    try {
      Reflect ref = Reflect.defineClass("us.pserver.bitbox.test.Hello")
          .append("package us.pserver.bitbox.test;")
          .append("import java.util.Objects;")
          .append("public class Hello {")
          .append("private final String greet;")
          .append("public Hello(String greet) {")
          .append("this.greet = Objects.requireNonNull(greet, \"Bad null greet\");")
          .append("}")
          .append("public String greet() { return String.format(\"Hello, %s\", greet); }")
          .append("public void printGreet() {")
          .append("System.out.println(greet()); }")
          .append("}")
          .reflectCompiled();
      Supplier<String> sup = ref
          .selectConstructor(String.class)
          .createReflected("Juno")
          .selectMethod("greet")
          .methodAsSupplier();
      Assertions.assertEquals("Hello, Juno", sup.get());
    }
    catch(Throwable t) {
      t.printStackTrace();
      throw t;
    }
  }
  
}
