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

package us.pserver.test.index;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/12/2017
 */
public class TestTemporalAcessor {

  
  public static void main(String[] args) {
    DateTimeFormatter fmt = DateTimeFormatter.ISO_INSTANT;
    LocalDateTime ldt = LocalDateTime.of(2017, 12, 24, 23, 59, 59);
    System.out.println(ldt);
    ZoneId zi = ZoneId.of("America/Sao_Paulo");
    ZoneId zii = ZoneId.of("America/New_York");
    ZoneId ziii = ZoneId.of("-3");
    System.out.println(zi);
    System.out.println(zii);
    System.out.println(ziii);
    System.out.println(ZonedDateTime.of(ldt, zi));
    System.out.println(ZonedDateTime.of(ldt, zii));
    System.out.println(ZonedDateTime.of(ldt, ziii));
    System.out.println(Instant.from(ZonedDateTime.of(ldt, zii)));
    
    System.out.println();
    ZonedDateTime saoPaulo = ZonedDateTime.of(ldt, zi);
    ZonedDateTime newYork = saoPaulo.withZoneSameInstant(zii);
    System.out.println("saoPaulo="+ saoPaulo);
    System.out.println("newYork_="+ newYork);
    System.out.println("saoPaulo.instant="+ saoPaulo.toInstant());
    System.out.println("newYork_.instant="+ newYork.toInstant());
    System.out.println("localDat.instant="+ ZonedDateTime.of(ldt, ZoneId.of("GMT")).toInstant());
    Instant now = Instant.now();
    System.out.println("now="+ now);
    
    System.out.println();
    DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
    String s1 = dtf.format(ldt);
    String s2 = dtf.format(saoPaulo);
    String s3 = dtf.format(newYork);
    System.out.println("s1="+ s1);
    System.out.println("s2="+ s2);
    System.out.println("s3="+ s3);
    System.out.println("parse.s1="+ dtf.parse(s1));
    System.out.println("parse.s2="+ dtf.parse(s2).getClass());
    System.out.println("parse.s3="+ dtf.parse(s3));
  }
  
}
