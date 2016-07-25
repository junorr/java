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

package us.pserver.maxb.cli.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import us.pserver.maxb.cli.MBOptions;
import us.pserver.maxb.cli.MaxBeanException;
import us.pserver.maxb.sql.Column;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ISchema;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.sql.type.Typed;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/02/2016
 */
public class ColumnParser extends AbstractOptionsParser<ISchema> {

  private final ISchema schema;
  
  
  public ColumnParser(MBOptions opt, ISchema schema) {
    super(opt);
    if(schema == null) {
      throw new IllegalArgumentException(
          "Schema must be not null"
      );
    }
    this.schema = schema;
  }
  
  
  public static ColumnParser get(MBOptions opt, ISchema sch) {
    return new ColumnParser(opt, sch);
  }
  
  
  public ISchema getSchema() {
    return schema;
  }
  
  
  private Map<String,List<IColumn<?>>> parseColumns() throws MaxBeanException {
    if(opt.getColumns() == null) {
      throw new MaxBeanException(
          "Invalid columns option: "+ opt.getColumns()
      );
    }
    Map<String,List<IColumn<?>>> map = new HashMap<>();
    String[] columns = opt.getColumns().split(",");
    for(String scol : columns) {
      if(!scol.contains(".") || !scol.contains("=")) {
        throw new MaxBeanException(
            "Invalid column format: '"+ scol+ "'"
        );
      }
      String[] tcol = scol.split("\\.");
      String[] acol = tcol[1].split("=");
      if(!map.containsKey(tcol[0])) {
        map.put(tcol[0], new LinkedList<IColumn<?>>());
      }
      map.get(tcol[0]).add(new Column(
          acol[0], Typed.of(acol[1]).getType())
      );
    }
    return map;
  }
  
  
  private ISchema addColumns() throws MaxBeanException {
    Map<String,List<IColumn<?>>> map = this.parseColumns();
    Iterator<String> keys = map.keySet().iterator();
    while(keys.hasNext()) {
      String key = keys.next();
      for(ITable tb : schema.getTables()) {
        if(tb.getName().equals(key)) {
          tb.getColumns().addAll(map.get(key));
        }
      }
    }
    return schema;
  }
  
  
  @Override
  public ISchema parse() throws MaxBeanException {
    this.addColumns();
    return schema;
  }
  
}
