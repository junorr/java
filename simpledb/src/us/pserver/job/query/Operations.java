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

package us.pserver.job.query;

import java.util.Arrays;
import java.util.Date;
import java.util.function.Predicate;
import us.pserver.job.query.op.BooleanEquals;
import us.pserver.job.query.op.BooleanInArray;
import us.pserver.job.query.op.BooleanNotEquals;
import us.pserver.job.query.op.BooleanPredicate;
import us.pserver.job.query.op.DateBetween;
import us.pserver.job.query.op.DateEquals;
import us.pserver.job.query.op.DateGreater;
import us.pserver.job.query.op.DateGreaterEquals;
import us.pserver.job.query.op.DateInArray;
import us.pserver.job.query.op.DateLesser;
import us.pserver.job.query.op.DateLesserEquals;
import us.pserver.job.query.op.DatePredicate;
import us.pserver.job.query.op.DoubleBetween;
import us.pserver.job.query.op.DoubleEquals;
import us.pserver.job.query.op.DoubleGreater;
import us.pserver.job.query.op.DoubleGreaterEquals;
import us.pserver.job.query.op.DoubleLesser;
import us.pserver.job.query.op.DoubleLesserEquals;
import us.pserver.job.query.op.NumberInArray;
import us.pserver.job.query.op.NumberPredicate;
import us.pserver.job.query.op.Operation;
import us.pserver.job.query.op.StringContains;
import us.pserver.job.query.op.StringEndsWith;
import us.pserver.job.query.op.StringEquals;
import us.pserver.job.query.op.StringEqualsIgnoreCase;
import us.pserver.job.query.op.StringInArray;
import us.pserver.job.query.op.StringPredicate;
import us.pserver.job.query.op.StringRegex;
import us.pserver.job.query.op.StringStartsWith;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2017
 */
public abstract class Operations {
  
  
  //// arrayContains ////
  
  public static Operation<Number> arrayContains(int val) {
    return new NumberPredicate(n->val == n.intValue());
  }
  
  public static Operation<Number> arrayContains(long val) {
    return new NumberPredicate(n->val == n.longValue());
  }
  
  public static Operation<Number> arrayContains(double val) {
    return new DoubleEquals(val);
  }
  
  public static Operation<Boolean> arrayContains(Boolean val) {
    return new BooleanEquals(val);
  }
  
  public static Operation<Date> arrayContains(Date val) {
    return new DateEquals(val);
  }
  
  public static Operation<String> arrayContains(String val) {
    return new StringEquals(val);
  }
  
  
  //// between ////
  
  public static Operation<Number> between(int val, int val2) {
    return new NumberPredicate(n->n != null && n.intValue() >= val && n.intValue() <= val2);
  }

  public static Operation<Number> between(long val, long val2) {
    return new NumberPredicate(n->n != null && n.longValue() >= val && n.longValue() <= val2);
  }
  
  public static Operation<Number> between(double val, double val2) {
    return new DoubleBetween(val, val2);
  }
  
  public static Operation<Date> between(Date val, Date val2) {
    return new DateBetween(val, val2);
  }

  
  //// eq ////
  
  public static Operation<Number> eq(int val) {
    return new NumberPredicate(n->n != null && n.intValue() == val);
  }

  public static Operation<Number> eq(long val) {
    return new NumberPredicate(n->n != null && n.longValue() == val);
  }
  
  public static Operation<Number> eq(double val) {
    return new DoubleEquals(val);
  }
  
  public static Operation<Boolean> eq(Boolean val) {
    return new BooleanEquals(val);
  }
  
  public static Operation<Date> eq(Date val) {
    return new DateEquals(val);
  }
  
  public static Operation<String> eq(String val) {
    return new StringEquals(val);
  }
  
  
  //// ge ////
  
  public static Operation<Number> ge(int val) {
    return new NumberPredicate(n->n != null && n.intValue() >= val);
  }

  public static Operation<Number> ge(double val) {
    return new DoubleGreaterEquals(val);
  }
  
  public static Operation<Number> ge(long val) {
    return new NumberPredicate(n->n != null && n.longValue() >= val);
  }
  
  public static Operation<Date> ge(Date val) {
    return new DateGreaterEquals(val);
  }

  
  //// gt ////
  
  public static Operation<Number> gt(int val) {
    return new NumberPredicate(n->n != null && n.intValue() > val);
  }

  public static Operation<Number> gt(double val) {
    return new DoubleGreater(val);
  }
  
  public static Operation<Number> gt(long val) {
    return new NumberPredicate(n->n != null && n.longValue() > val);
  }
  
  public static Operation<Date> gt(Date val) {
    return new DateGreater(val);
  }

  
  //// in ////
  
  public static Operation<Boolean> in(Boolean ... vals) {
    return new BooleanInArray(Arrays.asList(vals));
  }
  
  public static Operation<Number> in(Number ... vals) {
    return new NumberInArray(Arrays.asList(vals));
  }
  
  public static Operation<String> in(String ... vals) {
    return new StringInArray(Arrays.asList(vals));
  }
  
  public static Operation<Date> in(Date ... vals) {
    return new DateInArray(Arrays.asList(vals));
  }
  
  
  //// le ////
  
  public static Operation<Number> le(int val) {
    return new NumberPredicate(n->n != null && n.intValue() <= val);
  }
  
  public static Operation<Number> le(double val) {
    return new DoubleLesserEquals(val);
  }
  
  public static Operation<Number> le(long val) {
    return new NumberPredicate(n->n != null && n.longValue() <= val);
  }
  
  public static Operation<Date> le(Date val) {
    return new DateLesserEquals(val);
  }
  
  
  //// lt ////
  
  public static Operation<Number> lt(int val) {
    return new NumberPredicate(n->n != null && n.intValue() < val);
  }

  public static Operation<Number> lt(double val) {
    return new DoubleLesser(val);
  }
  
  public static Operation<Number> lt(long val) {
    return new NumberPredicate(n->n != null && n.longValue() < val);
  }
  
  public static Operation<Date> lt(Date val) {
    return new DateLesser(val);
  }

  
  //// ne ////
  
  public static Operation<Number> ne(int val) {
    return new NumberPredicate(n->n != null && n.intValue() != val);
  }

  public static Operation<Number> ne(double val) {
    return new NumberPredicate(n->n != null && n.doubleValue() != val);
  }
  
  public static Operation<Number> ne(long val) {
    return new NumberPredicate(n->n != null && n.longValue() != val);
  }
  
  public static Operation<Boolean> ne(Boolean val) {
    return new BooleanNotEquals(val);
  }

  public static Operation<Date> ne(Date val) {
    return new DatePredicate(d->d != null && !d.equals(val));
  }

  public static Operation<String> ne(String val) {
    return new StringPredicate(s->s != null && !s.equals(val));
  }
  
  
  //// predicate ////
  
  public static Operation<Boolean> predicateBool(Predicate<Boolean> pred) {
    return new BooleanPredicate(pred);
  }
  
  public static Operation<Date> predicateDate(Predicate<Date> pred) {
    return new DatePredicate(pred);
  }
  
  public static Operation<Number> predicateNum(Predicate<Number> pred) {
    return new NumberPredicate(pred);
  }
  
  public static Operation<String> predicateStr(Predicate<String> pred) {
    return new StringPredicate(pred);
  }

  
  //// string ops ////

  public static Operation<String> contains(String val) {
    return new StringContains(val);
  }
  
  public static Operation<String> endsWith(String val) {
    return new StringEndsWith(val);
  }
  
  public static Operation<String> eqIgnCase(String val) {
    return new StringEqualsIgnoreCase(val);
  }
  
  public static Operation<String> regex(String val) {
    return new StringRegex(val);
  }
  
  public static Operation<String> startsWith(String val) {
    return new StringStartsWith(val);
  }
  
}
