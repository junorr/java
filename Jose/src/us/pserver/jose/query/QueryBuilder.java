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

package us.pserver.jose.query;

import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;
import us.pserver.jose.query.Query.QueryImpl;
import us.pserver.jose.query.op.BooleanEquals;
import us.pserver.jose.query.op.BooleanNotEquals;
import us.pserver.jose.query.op.BooleanPredicate;
import us.pserver.jose.query.op.DateBetween;
import us.pserver.jose.query.op.DateEquals;
import us.pserver.jose.query.op.DateGreater;
import us.pserver.jose.query.op.DateGreaterEquals;
import us.pserver.jose.query.op.DateInArray;
import us.pserver.jose.query.op.DateLesser;
import us.pserver.jose.query.op.DateLesserEquals;
import us.pserver.jose.query.op.DatePredicate;
import us.pserver.jose.query.op.DoubleBetween;
import us.pserver.jose.query.op.DoubleEquals;
import us.pserver.jose.query.op.DoubleGreater;
import us.pserver.jose.query.op.DoubleGreaterEquals;
import us.pserver.jose.query.op.DoubleLesser;
import us.pserver.jose.query.op.DoubleLesserEquals;
import us.pserver.jose.query.op.NumberInArray;
import us.pserver.jose.query.op.NumberPredicate;
import us.pserver.jose.query.op.ObjectOperation;
import us.pserver.jose.query.op.StringContains;
import us.pserver.jose.query.op.StringEndsWith;
import us.pserver.jose.query.op.StringEquals;
import us.pserver.jose.query.op.StringEqualsIgnoreCase;
import us.pserver.jose.query.op.StringInArray;
import us.pserver.jose.query.op.StringPredicate;
import us.pserver.jose.query.op.StringRegex;
import us.pserver.jose.query.op.StringStartsWith;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 02/02/2017
 */
public interface QueryBuilder {
  
  
  public static QueryBuilder builder(Function<Query,Query> func, String field) {
    return new QueryBuilderImpl(func, field);
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
  
  public Query arrayContains(Query val);
  
  
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
  
  
  
  
  
  
  
  public static class QueryBuilderImpl implements QueryBuilder {
    
    private final String field;
    
    private final Function<Query,Query> func;
    
    
    public QueryBuilderImpl() {
      this(null, null);
    }
    
    
    public QueryBuilderImpl(Function<Query,Query> fnc, String field) {
      this.field = field;
      this.func = fnc;
    }
    
    
    //// arrayContains ////

    @Override
    public Query arrayContains(int val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->val == n.intValue())));
    }

    @Override
    public Query arrayContains(long val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->val == n.longValue())));
    }

    @Override
    public Query arrayContains(double val) {
      return func.apply(new QueryImpl(field, 
          new DoubleEquals(val)));
    }

    @Override
    public Query arrayContains(Boolean val) {
      return func.apply(new QueryImpl(field, 
          new BooleanEquals(val)));
    }

    @Override
    public Query arrayContains(Date val) {
      return func.apply(new QueryImpl(field, 
          new DateEquals(val)));
    }
    
    @Override
    public Query arrayContains(String val) {
      return func.apply(new QueryImpl(field, 
          new StringEquals(val)));
    }
    
    @Override
    public Query arrayContains(Query qry) {
      return func.apply(new QueryImpl(field, 
          new ObjectOperation(qry)));
    }


    //// between ////

    @Override
    public Query between(int val, int val2) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.intValue() >= val && n.intValue() <= val2)));
    }

    @Override
    public Query between(long val, long val2) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.longValue() >= val && n.longValue() <= val2)));
    }

    @Override
    public Query between(double val, double val2) {
      return func.apply(new QueryImpl(field, 
          new DoubleBetween(val, val2)));
    }

    @Override
    public Query between(Date val, Date val2) {
      return func.apply(new QueryImpl(field, 
          new DateBetween(val, val2)));
    }


    //// eq ////

    @Override
    public Query eq(int val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.intValue() == val)));
    }

    @Override
    public Query eq(long val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.longValue() == val)));
    }

    @Override
    public Query eq(double val) {
      return func.apply(new QueryImpl(field, 
          new DoubleEquals(val)));
    }

    @Override
    public Query eq(Boolean val) {
      return func.apply(new QueryImpl(field, 
          new BooleanEquals(val)));
    }

    @Override
    public Query eq(Date val) {
      return func.apply(new QueryImpl(field, 
          new DateEquals(val)));
    }

    @Override
    public Query eq(String val) {
      return func.apply(new QueryImpl(field, 
          new StringEquals(val)));
    }


    //// ge ////

    @Override
    public Query ge(int val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.intValue() >= val)));
    }

    @Override
    public Query ge(double val) {
      return func.apply(new QueryImpl(field, 
          new DoubleGreaterEquals(val)));
    }

    @Override
    public Query ge(long val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.longValue() >= val)));
    }

    @Override
    public Query ge(Date val) {
      return func.apply(new QueryImpl(field, 
          new DateGreaterEquals(val)));
    }


    //// gt ////

    @Override
    public Query gt(int val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.intValue() > val)));
    }

    @Override
    public Query gt(double val) {
      return func.apply(new QueryImpl(field, 
          new DoubleGreater(val)));
    }

    @Override
    public Query gt(long val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.longValue() > val)));
    }

    @Override
    public Query gt(Date val) {
      return func.apply(new QueryImpl(field, 
          new DateGreater(val)));
    }


    //// in ////

    @Override
    public Query in(Number ... vals) {
      return func.apply(new QueryImpl(field, 
          new NumberInArray(Arrays.asList(vals))));
    }

    @Override
    public Query in(String ... vals) {
      return func.apply(new QueryImpl(field, 
          new StringInArray(Arrays.asList(vals))));
    }

    @Override
    public Query in(Date ... vals) {
      return func.apply(new QueryImpl(field, 
          new DateInArray(Arrays.asList(vals))));
    }


    //// le ////

    @Override
    public Query le(int val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.intValue() <= val)));
    }

    @Override
    public Query le(double val) {
      return func.apply(new QueryImpl(field, 
          new DoubleLesserEquals(val)));
    }

    @Override
    public Query le(long val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.longValue() <= val)));
    }

    @Override
    public Query le(Date val) {
      return func.apply(new QueryImpl(field, 
          new DateLesserEquals(val)));
    }


    //// lt ////

    @Override
    public Query lt(int val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.intValue() < val)));
    }

    @Override
    public Query lt(double val) {
      return func.apply(new QueryImpl(field, 
          new DoubleLesser(val)));
    }

    @Override
    public Query lt(long val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.longValue() < val)));
    }

    @Override
    public Query lt(Date val) {
      return func.apply(new QueryImpl(field, 
          new DateLesser(val)));
    }


    //// ne ////

    @Override
    public Query ne(int val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.intValue() != val)));
    }

    @Override
    public Query ne(double val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.doubleValue() != val)));
    }

    @Override
    public Query ne(long val) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(n->n != null && n.longValue() != val)));
    }

    @Override
    public Query ne(Boolean val) {
      return func.apply(new QueryImpl(field, 
          new BooleanNotEquals(val)));
    }

    @Override
    public Query ne(Date val) {
      return func.apply(new QueryImpl(field, 
          new DatePredicate(d->d != null && !d.equals(val))));
    }

    @Override
    public Query ne(String val) {
      return func.apply(new QueryImpl(field, 
          new StringPredicate(s->s != null && !s.equals(val))));
    }


    //// predicate ////

    @Override
    public Query predicateBool(Predicate<Boolean> pred) {
      return func.apply(new QueryImpl(field, 
          new BooleanPredicate(pred)));
    }

    @Override
    public Query predicateDate(Predicate<Date> pred) {
      return func.apply(new QueryImpl(field, 
          new DatePredicate(pred)));
    }

    @Override
    public Query predicateNum(Predicate<Number> pred) {
      return func.apply(new QueryImpl(field, 
          new NumberPredicate(pred)));
    }

    @Override
    public Query predicateStr(Predicate<String> pred) {
      return func.apply(new QueryImpl(field, 
          new StringPredicate(pred)));
    }


    //// string ops ////

    @Override
    public Query contains(String val) {
      return func.apply(new QueryImpl(field, 
          new StringContains(val)));
    }

    @Override
    public Query endsWith(String val) {
      return func.apply(new QueryImpl(field, 
          new StringEndsWith(val)));
    }

    @Override
    public Query eqIgnCase(String val) {
      return func.apply(new QueryImpl(field, 
          new StringEqualsIgnoreCase(val)));
    }

    @Override
    public Query regex(String val) {
      return func.apply(new QueryImpl(field, 
          new StringRegex(val)));
    }

    @Override
    public Query startsWith(String val) {
      return func.apply(new QueryImpl(field, 
          new StringStartsWith(val)));
    }

  }
  
}
