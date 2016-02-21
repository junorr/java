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

import java.util.LinkedList;
import java.util.List;
import us.pserver.maxb.cli.MBOptions;
import us.pserver.maxb.cli.MaxBeanException;
import us.pserver.maxb.sql.spec.ISchema;
import us.pserver.maxb.sql.spec.ITable;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/02/2016
 */
public class TablesParser extends AbstractOptionsParser<ISchema> {

  private final ISchema schema;
  
  public TablesParser(MBOptions opt, ISchema sch) {
    super(opt);
    if(sch == null || sch.isEmpty()) {
      throw new IllegalArgumentException(
          "Schema must be not null"
      );
    }
    this.schema = sch;
  }
  
  
  public static TablesParser get(MBOptions opt, ISchema sch) {
    return new TablesParser(opt, sch);
  }
  
  
  public ISchema getSchema() {
    return schema;
  }
  
  
  @Override
  public ISchema parse() throws MaxBeanException {
    if(opt.getTables() == null) {
      throw new MaxBeanException(
          "Invalid tables option: "+ opt.getTables()
      );
    }
    List<ITable> select = new LinkedList<>();
    String[] tables = opt.getTables().split(",");
    for(String tname : tables) {
      for(ITable table : schema.getTables()) {
        if(table.getName().equals(tname)) {
          select.add(table);
        }
      }
    }
    System.out.print("  Tables: ");
    for(ITable tb : select) {
      System.out.print(tb.getName()+ ", ");
    }
    System.out.println();
    schema.getTables().clear();
    schema.getTables().addAll(select);
    return schema;
  }
  
}
