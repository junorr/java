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

package com.jpower.csv;

import com.jpower.rfl.Reflector;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 07/05/2013
 */
public class TableMapper {
  
  private Reflector ref;
  
  
  public TableMapper() {
    ref = new Reflector();
  }
  
  
  private void checkObjectsClass(List objects) {
    if(objects == null || objects.isEmpty())
      return;
    Class cls = objects.get(0).getClass();
    for(Object o : objects) {
      if(!o.getClass().equals(cls))
        throw new IllegalStateException("Objects class not equals");
    }
  }
  
  
  private Table initTable(List objects) {
    Field[] fls = ref.on(objects.get(0)).fields();
    Line titles = new Line(fls.length);
    for(Field f : fls) {
      titles.add(f.getName());
    }
    return new Table(titles);
  }
  
  
  public Table map(List objs) {
    if(objs == null || objs.isEmpty())
      return null;
    
    this.checkObjectsClass(objs);
    Table table = initTable(objs);
    
    for(Object o : objs) {
      Line ln = new Line(table.titles().maxSize());
      String[] fields = ref.on(o).fieldNames();
      for(String f : fields) {
        ln.add(ref.field(f).get());
      }
      table.add(ln);
    }
    return table;
  }

  
  public static void main(String[] args) {
    class A {
      int aint;
      double dint;
      String sint;
      public A() {
        aint = 0;
        dint = 0;
        sint = String.valueOf(aint);
      }
      public A(int i) {
        aint = i;
        dint = i;
        sint = String.valueOf(i);
      }
      @Override
      public String toString() {
        return "A{" + "aint=" + aint + ", dint=" + dint + ", sint=" + sint + '}';
      }
    }
    
    List objs = new LinkedList();
    for(int i = 0; i < 15; i++) {
      objs.add(new A(i));
    }
    print(objs);
    System.out.println();
    TableMapper m = new TableMapper();
    System.out.println("* mapping...");
    Table table = m.map(objs);
    System.out.println(table);
    ObjectMapper op = new ObjectMapper(new ObjectConverter(A.class));
    System.out.println("* demapping...");
    objs = null;
    objs = op.map(table);
    print(objs);
  }
  
  
  public static void print(List l) {
    if(l == null || l.isEmpty()) return;
    System.out.println("* list.size(): "+ l.size());
    for(int i = 0; i < l.size(); i++) {
      System.out.println("  ["+ i+ "]: "+ l.get(i));
    }
  }
  
}
