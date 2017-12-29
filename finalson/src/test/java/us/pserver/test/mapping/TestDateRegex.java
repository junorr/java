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

package us.pserver.test.mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/12/2017
 */
public class TestDateRegex {

  private final Instant now = Instant.now();
  
  private final LocalDateTime ldt = LocalDateTime.now();
  
  private final ZonedDateTime zdt = ZonedDateTime.now();
  
  private final DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
  
  private final String pattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*";
  
  //@Test
  //public void instantRegexMatch() {
    //String str = DateTimeFormatter.ISO_DATE_TIME.format(now);
    //System.out.println(str);
    //assertTrue(str.matches(pattern));
  //}
  
  @Test
  public void localDateRegexMatch() {
    String str = DateTimeFormatter.ISO_DATE_TIME.format(ldt);
    System.out.println(str);
    assertTrue(str.matches(pattern));
  }
  
  //@Test
  //public void zonedDateRegexMatch() {
    //String str = DateTimeFormatter.ISO_DATE_TIME.format(now);
    //System.out.println(str);
    //assertTrue(str.matches(pattern));
  //}
  
  @Test public void testSplit() {
    String sa = "\"a123b456c\"";
    String sb = "\"a123b456c\",\"d789e101112f\",\"g131415h161718i\"";
    List<String> la = Arrays.asList(sa.split(",")).stream().map(String::trim).collect(Collectors.toList());
    List<String> lb = Arrays.asList(sb.split(",")).stream().map(String::trim).collect(Collectors.toList());
    System.out.printf("- la : %d : %s%n", la.size(), la);
    System.out.printf("- lb : %d : %s%n", lb.size(), lb);
  }
  
}
