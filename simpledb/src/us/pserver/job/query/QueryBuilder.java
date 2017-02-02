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
import us.pserver.job.query.Query.DefQuery;
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
 * @version 0.0 - 02/02/2017
 */
public interface QueryBuilder {
  
  
  public static QueryBuilder builder(QueryOp op, String field) {
    return new DefQueryBuilder(op, field);
  }
  
  
  public static Query query(Class cls) {
    return Query.of(cls);
  }

  
  //// arrayContains ////
  
  public Query arrayContains(int val);
  
  public Query arrayContains(long val);
  
  public Query arrayContains(double val);
  
  public Query arrayContains(Boolean val);
  
  public Query arrayContains(Date val);
  
  public Query arrayContains(String val);
  
  
  //// between ////
  
  public Query between(int val, int val2);

  public Query between(long val, long val2);
  
  public Query between(double val, double val2);
  
  public Query between(Date val, Date val2);

  
  //// eq ////
  
  public Query eq(int val);

  public Query eq(long val);
  
  public Query eq(double val);
  
  public Query eq(Boolean val);
  
  public Query eq(Date val);
  
  public Query eq(String val);
  
  
  //// ge ////
  
  public Query ge(int val);

  public Query ge(double val);
  
  public Query ge(long val);
  
  public Query ge(Date val);

  
  //// gt ////
  
  public Query gt(int val);

  public Query gt(double val);
  
  public Query gt(long val);
  
  public Query gt(Date val);

  
  //// in ////
  
  public Query in(Boolean ... vals);
  
  public Query in(Number ... vals);
  
  public Query in(String ... vals);
  
  public Query in(Date ... vals);
  
  
  //// le ////
  
  public Query le(int val);
  
  public Query le(double val);
  
  public Query le(long val);
  
  public Query le(Date val);
  
  
  //// lt ////
  
  public Query lt(int val);

  public Query lt(double val);
  
  public Query lt(long val);
  
  public Query lt(Date val);

  
  //// ne ////
  
  public Query ne(int val);

  public Query ne(double val);
  
  public Query ne(long val);
  
  public Query ne(Boolean val);

  public Query ne(Date val);

  public Query ne(String val);
  
  
  //// predicate ////
  
  public Query predicateBool(Predicate<Boolean> pred);
  
  public Query predicateDate(Predicate<Date> pred);
  
  public Query predicateNum(Predicate<Number> pred);
  
  public Query predicateStr(Predicate<String> pred);

  
  //// string ops ////

  public Query contains(String val);
  
  public Query endsWith(String val);
  
  public Query eqIgnCase(String val);
  
  public Query regex(String val);
  
  public Query startsWith(String val);
  
  
  
  
  
  @FunctionalInterface
  public static interface QueryOp {
    public Query op(Query q);
  }

  
  
  
  
  public static class DefQueryBuilder implements QueryBuilder {
    
    private final String field;
    
    private final QueryOp qop;
    
    
    public DefQueryBuilder() {
      this(null, null);
    }
    
    
    public DefQueryBuilder(QueryOp qop, String field) {
      this.field = field;
      this.qop = qop;
    }
    
    
    //// arrayContains ////

    @Override
    public Query arrayContains(int val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->val == n.intValue())));
    }

    @Override
    public Query arrayContains(long val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->val == n.longValue())));
    }

    @Override
    public Query arrayContains(double val) {
      return qop.op(new DefQuery(field, 
          new DoubleEquals(val)));
    }

    @Override
    public Query arrayContains(Boolean val) {
      return qop.op(new DefQuery(field, 
          new BooleanEquals(val)));
    }

    @Override
    public Query arrayContains(Date val) {
      return qop.op(new DefQuery(field, 
          new DateEquals(val)));
    }

    @Override
    public Query arrayContains(String val) {
      return qop.op(new DefQuery(field, 
          new StringEquals(val)));
    }


    //// between ////

    @Override
    public Query between(int val, int val2) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.intValue() >= val && n.intValue() <= val2)));
    }

    @Override
    public Query between(long val, long val2) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.longValue() >= val && n.longValue() <= val2)));
    }

    @Override
    public Query between(double val, double val2) {
      return qop.op(new DefQuery(field, 
          new DoubleBetween(val, val2)));
    }

    @Override
    public Query between(Date val, Date val2) {
      return qop.op(new DefQuery(field, 
          new DateBetween(val, val2)));
    }


    //// eq ////

    @Override
    public Query eq(int val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.intValue() == val)));
    }

    @Override
    public Query eq(long val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.longValue() == val)));
    }

    @Override
    public Query eq(double val) {
      return qop.op(new DefQuery(field, 
          new DoubleEquals(val)));
    }

    @Override
    public Query eq(Boolean val) {
      return qop.op(new DefQuery(field, 
          new BooleanEquals(val)));
    }

    @Override
    public Query eq(Date val) {
      return qop.op(new DefQuery(field, 
          new DateEquals(val)));
    }

    @Override
    public Query eq(String val) {
      return qop.op(new DefQuery(field, 
          new StringEquals(val)));
    }


    //// ge ////

    @Override
    public Query ge(int val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.intValue() >= val)));
    }

    @Override
    public Query ge(double val) {
      return qop.op(new DefQuery(field, 
          new DoubleGreaterEquals(val)));
    }

    @Override
    public Query ge(long val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.longValue() >= val)));
    }

    @Override
    public Query ge(Date val) {
      return qop.op(new DefQuery(field, 
          new DateGreaterEquals(val)));
    }


    //// gt ////

    @Override
    public Query gt(int val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.intValue() > val)));
    }

    @Override
    public Query gt(double val) {
      return qop.op(new DefQuery(field, 
          new DoubleGreater(val)));
    }

    @Override
    public Query gt(long val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.longValue() > val)));
    }

    @Override
    public Query gt(Date val) {
      return qop.op(new DefQuery(field, 
          new DateGreater(val)));
    }


    //// in ////

    @Override
    public Query in(Boolean ... vals) {
      return qop.op(new DefQuery(field, 
          new BooleanInArray(Arrays.asList(vals))));
    }

    @Override
    public Query in(Number ... vals) {
      return qop.op(new DefQuery(field, 
          new NumberInArray(Arrays.asList(vals))));
    }

    @Override
    public Query in(String ... vals) {
      return qop.op(new DefQuery(field, 
          new StringInArray(Arrays.asList(vals))));
    }

    @Override
    public Query in(Date ... vals) {
      return qop.op(new DefQuery(field, 
          new DateInArray(Arrays.asList(vals))));
    }


    //// le ////

    @Override
    public Query le(int val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.intValue() <= val)));
    }

    @Override
    public Query le(double val) {
      return qop.op(new DefQuery(field, 
          new DoubleLesserEquals(val)));
    }

    @Override
    public Query le(long val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.longValue() <= val)));
    }

    @Override
    public Query le(Date val) {
      return qop.op(new DefQuery(field, 
          new DateLesserEquals(val)));
    }


    //// lt ////

    @Override
    public Query lt(int val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.intValue() < val)));
    }

    @Override
    public Query lt(double val) {
      return qop.op(new DefQuery(field, 
          new DoubleLesser(val)));
    }

    @Override
    public Query lt(long val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.longValue() < val)));
    }

    @Override
    public Query lt(Date val) {
      return qop.op(new DefQuery(field, 
          new DateLesser(val)));
    }


    //// ne ////

    @Override
    public Query ne(int val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.intValue() != val)));
    }

    @Override
    public Query ne(double val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.doubleValue() != val)));
    }

    @Override
    public Query ne(long val) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(n->n != null && n.longValue() != val)));
    }

    @Override
    public Query ne(Boolean val) {
      return qop.op(new DefQuery(field, 
          new BooleanNotEquals(val)));
    }

    @Override
    public Query ne(Date val) {
      return qop.op(new DefQuery(field, 
          new DatePredicate(d->d != null && !d.equals(val))));
    }

    @Override
    public Query ne(String val) {
      return qop.op(new DefQuery(field, 
          new StringPredicate(s->s != null && !s.equals(val))));
    }


    //// predicate ////

    @Override
    public Query predicateBool(Predicate<Boolean> pred) {
      return qop.op(new DefQuery(field, 
          new BooleanPredicate(pred)));
    }

    @Override
    public Query predicateDate(Predicate<Date> pred) {
      return qop.op(new DefQuery(field, 
          new DatePredicate(pred)));
    }

    @Override
    public Query predicateNum(Predicate<Number> pred) {
      return qop.op(new DefQuery(field, 
          new NumberPredicate(pred)));
    }

    @Override
    public Query predicateStr(Predicate<String> pred) {
      return qop.op(new DefQuery(field, 
          new StringPredicate(pred)));
    }


    //// string ops ////

    @Override
    public Query contains(String val) {
      return qop.op(new DefQuery(field, 
          new StringContains(val)));
    }

    @Override
    public Query endsWith(String val) {
      return qop.op(new DefQuery(field, 
          new StringEndsWith(val)));
    }

    @Override
    public Query eqIgnCase(String val) {
      return qop.op(new DefQuery(field, 
          new StringEqualsIgnoreCase(val)));
    }

    @Override
    public Query regex(String val) {
      return qop.op(new DefQuery(field, 
          new StringRegex(val)));
    }

    @Override
    public Query startsWith(String val) {
      return qop.op(new DefQuery(field, 
          new StringStartsWith(val)));
    }

  }
  
}
