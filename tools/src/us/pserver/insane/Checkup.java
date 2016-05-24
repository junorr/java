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

package us.pserver.insane;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import us.pserver.insane.checkup.CollectionContains;
import us.pserver.insane.checkup.CollectionNotEmpty;
import us.pserver.insane.checkup.EndsWith;
import us.pserver.insane.checkup.Equals;
import us.pserver.insane.checkup.EqualsIgnoreCase;
import us.pserver.insane.checkup.InstantBetween;
import us.pserver.insane.checkup.InstantGreater;
import us.pserver.insane.checkup.InstantGreaterEquals;
import us.pserver.insane.checkup.InstantLesser;
import us.pserver.insane.checkup.InstantLesserEquals;
import us.pserver.insane.checkup.MapContainsKey;
import us.pserver.insane.checkup.MapContainsValue;
import us.pserver.insane.checkup.MapNotEmpty;
import us.pserver.insane.checkup.NotNull;
import us.pserver.insane.checkup.NumberBetween;
import us.pserver.insane.checkup.NumberGreater;
import us.pserver.insane.checkup.NumberGreaterEquals;
import us.pserver.insane.checkup.NumberLesser;
import us.pserver.insane.checkup.NumberLesserEquals;
import us.pserver.insane.checkup.NumberMod;
import us.pserver.insane.checkup.StartsWith;
import us.pserver.insane.checkup.StringContains;
import us.pserver.insane.checkup.StringNotEmpty;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/05/2016
 */
public abstract class Checkup {

  public static <T> SanityCheck<T> isNotNull() {
    return new NotNull<>();
  }
  
  
  public static SanityCheck isEquals(Object obj) {
    return new Equals(obj);
  }
  

  public static SanityCheck<String> isEqualsIgnoreCase(String str) {
    return new EqualsIgnoreCase(str);
  }
  

  public static SanityCheck<String> startsWith(String str) {
    return new StartsWith(str);
  }
  

  public static SanityCheck<String> endsWith(String str) {
    return new EndsWith(str);
  }
  

  public static SanityCheck<String> contains(String str) {
    return new StringContains(str);
  }
  

  public static SanityCheck<String> isNotEmpty() {
    return new StringNotEmpty();
  }
  

  public static SanityCheck<Collection> contains(Object ... objs) {
    return new CollectionContains(objs);
  }
  
  
  public static SanityCheck<Collection> contains(Collection list) {
    return new CollectionContains(
        Sane.of(list).get(new NotNull()).toArray()
    );
  }
  
  
  public static SanityCheck<Map> containsKey(Object obj) {
    return new MapContainsKey(obj);
  }
  

  public static SanityCheck<Map> containsValue(Object obj) {
    return new MapContainsValue(obj);
  }
  

  public static SanityCheck<Map> isNotEmptyMap() {
    return new MapNotEmpty();
  }
  
  
  public static SanityCheck<Collection> isNotEmptyCollection() {
    return new CollectionNotEmpty();
  }
  

  public static SanityCheck<Number> isGreaterThan(Number n) {
    return new NumberGreater(n);
  }
  

  public static SanityCheck<Number> isGreaterEqualsTo(Number n) {
    return new NumberGreaterEquals(n);
  }
  

  public static SanityCheck<Number> isLesserThan(Number n) {
    return new NumberLesser(n);
  }
  

  public static SanityCheck<Number> isLesserEqualsTo(Number n) {
    return new NumberLesserEquals(n);
  }
  

  public static SanityCheck<Number> isBetween(Number min, Number max) {
    return new NumberBetween(min, max);
  }
  
  
  public static SanityCheck<Number> isModOf(Number n) {
    return new NumberMod(n);
  }
  

  public static SanityCheck<Instant> isGreaterThan(Date d) {
    return isGreaterThan(Sane.of(d).get(new NotNull()));
  }
  

  public static SanityCheck<Instant> isGreaterEqualsTo(Date d) {
    return isGreaterEqualsTo(Sane.of(d).get(new NotNull()));
  }
  

  public static SanityCheck<Instant> isLesserThan(Date d) {
    return isLesserThan(Sane.of(d).get(new NotNull()));
  }
  

  public static SanityCheck<Instant> isLesserEqualsTo(Date d) {
    return isLesserEqualsTo(Sane.of(d).get(new NotNull()));
  }
  

  public static SanityCheck<Instant> isBetween(Date min, Date max) {
    return isBetween(
        Sane.of(min).get(new NotNull()), 
        Sane.of(max).get(new NotNull())
    );
  }
  

  public static SanityCheck<Instant> isGreaterThan(Instant d) {
    return new InstantGreater(d);
  }
  

  public static SanityCheck<Instant> isGreaterEqualsTo(Instant d) {
    return new InstantGreaterEquals(d);
  }
  

  public static SanityCheck<Instant> isLesserThan(Instant d) {
    return new InstantLesser(d);
  }
  

  public static SanityCheck<Instant> isLesserEqualsTo(Instant d) {
    return new InstantLesserEquals(d);
  }
  

  public static SanityCheck<Instant> isBetween(Instant min, Instant max) {
    return new InstantBetween(min, max);
  }
  
}
