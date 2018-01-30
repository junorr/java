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

package us.pserver.tools.match.ex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.check.ICheck;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/01/2018
 */
public class TestCheck {

  @Test
  public void notNull() {
    String str = null;
    //System.out.println(ICheck.notNull(IOException.class, str).getOrFail());
    Assertions.assertThrows(IOException.class, ()->ICheck.notNull(IOException.class, str).failIfNotMatch());
  }

  @Test
  public void notEmptyString() {
    String str = null;
    Assertions.assertThrows(IOException.class, ()->ICheck.notEmpty(IOException.class, str).failIfNotMatch());
  }
  
  @Test
  public void notEmptyCollection() {
    List<Integer> ls = Collections.EMPTY_LIST;
    Assertions.assertThrows(Exception.class, ()->ICheck.notEmpty(Exception.class, ls).failIfNotMatch());
  }
  
  @Test
  public void winPathExists() {
    Path path = Paths.get("D:/check.test");
    Assertions.assertThrows(FileNotFoundException.class, ()->ICheck.exists(FileNotFoundException.class, path).failIfNotMatch());
  }
  
  @Test
  public void winFileExists() {
    Path path = Paths.get("D:/check.test");
    Assertions.assertThrows(FileNotFoundException.class, ()->ICheck.exists(FileNotFoundException.class, path.toFile()).failIfNotMatch());
  }
  
  @Test
  public void winPathNotExists() {
    Path path = Paths.get("D:/java");
    Assertions.assertThrows(FileNotFoundException.class, ()->ICheck.notExists(FileNotFoundException.class, path).failIfNotMatch());
  }
  
  @Test
  public void winFileNotExists() {
    Path path = Paths.get("D:/java");
    Assertions.assertThrows(FileNotFoundException.class, ()->ICheck.notExists(FileNotFoundException.class, path.toFile()).failIfNotMatch());
  }
  
  @Test
  public void numberNotBetween() {
    int val = 10;
    int min = 0;
    int max = 5;
    Assertions.assertThrows(IOException.class, ()->ICheck.notBetween(IOException.class, val, min, max).failIfNotMatch());
  }
  
  @Test
  public void numberNotBetweenExclusive() {
    int val = 10;
    int min = 0;
    int max = 10;
    Assertions.assertThrows(IOException.class, ()->ICheck.notBetweenExclusive(IOException.class, val, min, max).failIfNotMatch());
  }
  
}
