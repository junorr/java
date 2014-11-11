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

package us.pserver.sdb;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 07/11/2014
 */
public class QueryBuilder {

  private List<QueryPath> path;
  
  private String label;
  
  
  protected QueryBuilder() {
    path = new LinkedList<>();
    label = null;
  }
  
  
  protected QueryBuilder(String label) {
    path = new LinkedList<>();
    this.label = label;
  }
  
  
  public static QueryBuilder builder() {
    return new QueryBuilder();
  }
  
  
  public static QueryBuilder builder(String label) {
    return new QueryBuilder(label);
  }
  
  
  private void checknull(Object obj, String name) {
    if(obj == null)
      throw new IllegalArgumentException(
          "Invalid null argument: "+ name+ "="+ obj);
  }
  
  
  public QueryBuilder label(String label) {
    checknull(label, "label");
    this.label = label;
    return this;
  }
  
  
  public QueryBuilder label(Class cls) {
    checknull(cls, "label");
    this.label = cls.getName();
    return this;
  }
  
  
  public QueryBuilder field(String field) {
    checknull(field, "field");
    path.add(new QueryPath().field(field));
    return this;
  }
  
  
  public QueryBuilder descend(String field) {
    checknull(field, "field");
    path.add(new QueryPath().field(field));
    path.add(new QueryPath().method(Query.QueryMethod.DESCEND));
    return this;
  }
  
  
  public QueryBuilder not() {
    path.add(new QueryPath()
        .method(Query.QueryMethod.NOT));
    return this;
  }
  
  
  public QueryBuilder empty() {
    path.add(new QueryPath()
        .method(Query.QueryMethod.EMPTY));
    return this;
  }
  
  
  public QueryBuilder equal(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.EQUALS));
    return this;
  }
  
  
  public QueryBuilder equalIgnoreCase(String val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.EQUALS_ICS));
    return this;
  }
  
  
  public QueryBuilder contains(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.CONTAINS));
    return this;
  }
  
  
  public QueryBuilder containsIgnoreCase(String val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.CONTAINS_ICS));
    return this;
  }
  
  
  public QueryBuilder endsWith(String val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.ENDS_WITH));
    return this;
  }
  
  
  public QueryBuilder greater(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.GREATER));
    return this;
  }
  
  
  public QueryBuilder greaterEquals(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.GREATER_EQ));
    return this;
  }
  
  
  public QueryBuilder lesser(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.LESSER));
    return this;
  }
  
  
  public QueryBuilder lesserEquals(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.LESSER_EQ));
    return this;
  }
  

  public QueryBuilder startsWith(String val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(Query.QueryMethod.STARTS_WITH));
    return this;
  }
  
  
  public Query1 create() {
    return new Query1(path, label);
  }
  
}
