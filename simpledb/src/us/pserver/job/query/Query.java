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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import us.pserver.job.query.op.Operation;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/01/2017
 */
public interface Query {

  public String name();
  
  public List<Query> childFields();
  
  public List<Query> andFields();
  
  public List<Query> orFields();
  
  public Operation operation();
  
  public Query descend(String field);
  
  public Query filter(String field, Operation op);
  
  public Query and(String field, Operation op);
  
  public Query or(String field, Operation op);
  
  
  public static Query of(Class cls) {
    if(cls == null) {
      throw new IllegalArgumentException("Bad Null Class");
    }
    return new DefQuery(cls.getName());
  }
  
  
  
  
  public static class DefQuery implements Query {
    
    private final String name;
    
    private final Operation oper;
    
    private final List<Query> childs;
    
    private final List<Query> and;
    
    private final List<Query> or;
    
    
    public DefQuery(String name, Operation op) {
      if(name == null) {
        throw new IllegalArgumentException("Bad Null Name");
      }
      this.name = name;
      childs = new ArrayList<>();
      and = new ArrayList<>();
      or = new ArrayList<>();
      oper = op;
    }
    
    
    public DefQuery(String name) {
      this(name, null);
    }
    
    
    public DefQuery() {
      this("", null);
    }
    
    
    @Override
    public List<Query> childFields() {
      return Collections.unmodifiableList(childs);
    }
    
    
    @Override
    public String name() {
      return name;
    }


    @Override
    public Query descend(String field) {
      return createQuery(field, childs, null);
    }


    @Override
    public Operation operation() {
      return oper;
    }


    @Override
    public Query filter(String field, Operation op) {
      return createQuery(field, childs, op);
    }


    @Override
    public Query and(String field, Operation op) {
      return createQuery(field, and, op);
    }


    @Override
    public Query or(String field, Operation op) {
      return createQuery(field, or, op);
    }
    

    @Override
    public List<Query> andFields() {
      return and;
    }
    

    @Override
    public List<Query> orFields() {
      return or;
    }
    
    
    private Query createQuery(String field, List<Query> lst, Operation op) {
      if(field == null) {
        throw new IllegalArgumentException("Bad Null Field");
      }
      Query query = new DefQuery(field, op);
      lst.add(query);
      return query;
    }

    
    @Override
    public String toString() {
      return "Query{" + "name=" + name + ", oper=" + oper + ", childs=" + childs.size() + ", and=" + and.size() + ", or=" + or.size() + '}';
    }

  }
  
}
