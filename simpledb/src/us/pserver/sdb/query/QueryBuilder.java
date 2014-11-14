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

package us.pserver.sdb.query;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import us.pserver.sdb.Document;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 07/11/2014
 */
public class QueryBuilder {

  private List<QueryPath> path;
  
  private String label;
  
  private int limit;
  
  private Query1 query;
  
  boolean and;
  
  
  protected QueryBuilder() {
    path = new LinkedList<>();
    label = null;
    limit = -1;
    query = null;
    and = false;
  }
  
  
  protected QueryBuilder(String label) {
    path = new LinkedList<>();
    this.label = label;
    limit = -1;
    query = null;
    and = false;
  }
  
  
  public static QueryBuilder builder() {
    return new QueryBuilder();
  }
  
  
  public static QueryBuilder builder(String label) {
    return new QueryBuilder(label);
  }
  
  
  public QueryBuilder and() {
    if(path.isEmpty() && label == null) {
      throw new IllegalStateException(
          "No conditions applied on this QueryBuilder. Can not add and operator.");
    }
    if(query == null) {
      query = build();
    }
    else {
      if(and) query.and(build());
      else query.or(build());
    }
    and = true;
    return this.clear();
  }
  
  
  public QueryBuilder or() {
    if(path.isEmpty() && label == null) {
      throw new IllegalStateException(
          "No conditions applied on this QueryBuilder. Can not add or operator.");
    }
    if(query == null) {
      query = build();
    }
    else {
      if(and) query.and(build());
      else query.or(build());
    }
    and = false;
    return clear();
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
    path.add(new QueryPath().method(QueryMethod.DESCEND));
    return this;
  }
  
  
  public QueryBuilder limit(int limit) {
    this.limit = limit;
    return this;
  }
  
  
  public QueryBuilder not() {
    path.add(new QueryPath()
        .method(QueryMethod.NOT));
    return this;
  }
  
  
  public QueryBuilder empty() {
    path.add(new QueryPath()
        .method(QueryMethod.EMPTY));
    return this;
  }
  
  
  public QueryBuilder equal(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.EQUALS));
    return this;
  }
  
  
  public QueryBuilder equalIgnoreCase(String val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.EQUALS_ICS));
    return this;
  }
  
  
  public QueryBuilder contains(Object ... vals) {
    checknull(vals, "values");
    path.add(new QueryPath().value(vals));
    path.add(new QueryPath().method(QueryMethod.CONTAINS));
    return this;
  }
  
  
  public QueryBuilder contains(List vals) {
    checknull(vals, "values");
    path.add(new QueryPath().value(vals));
    path.add(new QueryPath().method(QueryMethod.CONTAINS));
    return this;
  }
  
  
  public QueryBuilder contains(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.CONTAINS));
    return this;
  }
  
  
  public QueryBuilder containsIgnoreCase(String val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.CONTAINS_ICS));
    return this;
  }
  
  
  public QueryBuilder endsWith(String val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.ENDS_WITH));
    return this;
  }
  
  
  public QueryBuilder greater(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.GREATER));
    return this;
  }
  
  
  public QueryBuilder greaterEquals(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.GREATER_EQ));
    return this;
  }
  
  
  public QueryBuilder lesser(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.LESSER));
    return this;
  }
  
  
  public QueryBuilder lesserEquals(Object val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.LESSER_EQ));
    return this;
  }
  

  public QueryBuilder startsWith(String val) {
    checknull(val, "value");
    path.add(new QueryPath().value(val));
    path.add(new QueryPath().method(QueryMethod.STARTS_WITH));
    return this;
  }
  
  
  public QueryBuilder clear() {
    path.clear();
    label = null;
    limit = -1;
    return this;
  }
  
  
  private Query1 build() {
    if(path.isEmpty() && label == null) {
      throw new IllegalStateException(
          "No conditions applied on this QueryBuilder");
    }
    LinkedList<QueryPath> list = new LinkedList<>();
    list.addAll(path);
    return new Query1(list, label).limit(limit);
  }
  
  
  public Query1 create() {
    if(query == null 
        && path.isEmpty() 
        && label == null) {
      throw new IllegalStateException(
          "No conditions applied on this QueryBuilder");
    }
    Query1 qry = null;
    if(query != null) {
      if(!path.isEmpty()) {
        if(and) query.and(build());
        else query.or(build());
      }
      qry = query;
    }
    else {
      qry = build();
    }
    this.clear();
    return qry;
  }
  
  
  public Query1 fromExample(Document doc, String dkey) {
    if(doc == null
        || (doc.map().isEmpty() 
        &&  doc.label() == null))
      throw new IllegalArgumentException("Invalid Document: "+ doc);
    
    if(doc.label() != null && dkey == null)
      label = doc.label();
    
    if(dkey != null) this.descend(dkey);
    
    Iterator<String> it = doc.map().keySet().iterator();
    Query1 qry = null;
    boolean first = true;
    
    while(it.hasNext()) {
      String key = it.next();
      Object val = doc.get(key);
      if(val == null) continue;
      if(first) {
        first = false;
        if(val instanceof Document) {
          qry = this.fromExample(doc, key);
        }
        else {
          qry = this.field(key).equal(val).create();
        }
      }
      else {
        QueryBuilder bld = QueryBuilder.builder();
        if(dkey != null) bld.descend(dkey);
        if(val instanceof Document) {
          qry.and(bld.fromExample((Document) val, key));
        }
        else {
          qry.and(bld.field(key).equal(val).create());
        }
      }
    }
    return qry;
  }

  
  public Query1 fromExample(Document doc) {
    if(doc == null 
        || (doc.map().isEmpty() 
        &&  doc.label() == null))
      throw new IllegalArgumentException("Invalid Document: "+ doc);
    return fromExample(doc, null);
  }
  
}
