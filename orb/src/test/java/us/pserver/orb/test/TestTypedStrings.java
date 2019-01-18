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

package us.pserver.orb.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import us.pserver.tools.Match;
import us.pserver.orb.TypeStrings;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/01/2018
 */
public class TestTypedStrings {
  
  private final TypeStrings ts = new TypeStrings();
  
  @Test
  public void booleanTypedString() {
    String strue = "tRuE";
    String sfalse = "FaLsE";
    Assertions.assertEquals(Boolean.TRUE, ts.asType(strue, Boolean.class));
    Assertions.assertEquals(Boolean.FALSE, ts.asType(sfalse, Boolean.class));
  }
  
  @Test
  public void byteTypedString() {
    String sb1 = "127";
    String sb2 = "-5";
    Assertions.assertEquals(Byte.valueOf((byte)127), ts.asType(sb1, Byte.class));
    Assertions.assertEquals(Byte.valueOf((byte)-5), ts.asType(sb2, Byte.class));
  }
  
  @Test
  public void characterTypedString() {
    String s1 = "a";
    String s2 = "0";
    Assertions.assertEquals(Character.valueOf('a'), ts.asType(s1, char.class));
    Assertions.assertEquals(Character.valueOf('0'), ts.asType(s2, char.class));
  }
  
  @Test
  public void classTypedString() {
    String s1 = "java.lang.String";
    String s2 = "us.pserver.tools.Match";
    Assertions.assertEquals(String.class, ts.asType(s1, Class.class));
    Assertions.assertEquals(Match.class, ts.asType(s2, Class.class));
  }
  
  @Test
  public void doubleTypedString() {
    String s1 = "-5.5";
    String s2 = "1001.1001";
    Assertions.assertEquals(Double.valueOf(-5.5), ts.asType(s1, double.class));
    Assertions.assertEquals(Double.valueOf(1001.1001), ts.asType(s2, double.class));
  }
  
  @Test
  public void inetAddressTypedString() throws UnknownHostException {
    InetAddress addr1 = InetAddress.getByName("127.0.0.1");
    InetAddress addr2 = InetAddress.getByName("localhost");
    String sip1 = "127.0.0.1";
    String sip2 = "localhost";
    Assertions.assertEquals(addr2, ts.asType(sip1, InetAddress.class));
    Assertions.assertEquals(addr1, ts.asType(sip2, InetAddress.class));
  }
  
  @Test
  public void instantTypedString() throws UnknownHostException {
    Instant it = LocalDateTime.of(2017, 1, 4, 15, 44, 22).toInstant(ZoneOffset.UTC);
    System.out.println(it);
    String sit = "2017-01-04T15:44:22Z";
    Assertions.assertEquals(it, ts.asType(sit, Instant.class));
  }
  
  @Test
  public void localDateTypedString() throws UnknownHostException {
    LocalDate ld = LocalDate.of(2017, 1, 4);
    System.out.println(ld);
    String sld = "2017-01-04";
    Assertions.assertEquals(ld, ts.asType(sld, LocalDate.class));
  }
  
  @Test
  public void localTimeTypedString() throws UnknownHostException {
    LocalTime lt = LocalTime.of(15, 44, 22);
    System.out.println(lt);
    String sld = "15:44:22";
    Assertions.assertEquals(lt, ts.asType(sld, LocalTime.class));
  }
  
  @Test
  public void offsetTimeTypedString() throws UnknownHostException {
    OffsetTime ot = OffsetTime.of(LocalTime.of(15, 44, 22), ZoneOffset.ofHours(-2));
    System.out.println(ot);
    String sld = "15:44:22-02:00";
    Assertions.assertEquals(ot, ts.asType(sld, OffsetTime.class));
  }
  
  @Test
  public void zonedDateTimeTypedString() throws UnknownHostException {
    ZonedDateTime zdt = LocalDateTime.of(2017, 1, 4, 15, 44, 22).atZone(ZoneId.of("America/Sao_Paulo"));
    ZonedDateTime dt = LocalDateTime.of(2017, 1, 4, 15, 44, 22).atZone(ZoneOffset.ofHours(-2).normalized());
    System.out.println(dt);
    String sld = "2017-01-04T15:44:22-02:00";
    Assertions.assertEquals(dt, ts.asType(sld, ZonedDateTime.class));
  }
  
  @Disabled
  @Test
  public void pathTypedString() throws UnknownHostException {
    Path path = Paths.get("/storage/java/toolbox");
    Assertions.assertTrue(Files.exists(path));
    Assertions.assertTrue(Files.isDirectory(path));
    String sld = "/storage/java/toolbox";
    Path typed = ts.asType(sld, Path.class);
    Assertions.assertEquals(path, typed);
    Assertions.assertTrue(Files.exists(typed));
    Assertions.assertTrue(Files.isDirectory(typed));
  }
  
  @Test
  public void guessBooleanFromPattern() {
    String str = "TrUe";
    Assertions.assertEquals(Boolean.class, ts.guessTypeFromPattern(str));
  }
  
  @Test
  public void guessClassFromPattern() {
    String str = "java.lang.String";
    Assertions.assertEquals(Class.class, ts.guessTypeFromPattern(str));
  }
  
  @Test
  public void guessCharacterFromPattern() {
    String str = "/";
    Assertions.assertEquals(Character.class, ts.guessTypeFromPattern(str));
  }
  
  @Test
  public void guessDoubleFromPattern() {
    String str = "-5.5";
    Assertions.assertEquals(Double.class, ts.guessTypeFromPattern(str));
  }
  
  @Test
  public void guessInstantFromPattern() {
    String str = "+5.5";
    Assertions.assertEquals(Double.class, ts.guessTypeFromPattern(str));
  }
  
  @Test
  public void guessIPv4FromPattern() {
    String str = "127.0.0.1";
    Assertions.assertEquals(InetAddress.class, ts.guessTypeFromPattern(str));
  }
  
}
