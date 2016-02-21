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

package us.pserver.maxb.cli.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import us.pserver.maxb.cli.MBOptions;
import us.pserver.maxb.cli.MaxBeanException;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ISchema;
import us.pserver.maxb.sql.spec.ITable;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/02/2016
 */
public class Inspection extends AbstractHandler {

  private final Properties prop;
  
  
  public Inspection(MBOptions opt, ISchema sch) {
    super(opt, sch);
    this.prop = new Properties();
  }
  
  
  public static Inspection get(MBOptions opt, ISchema sch) {
    return new Inspection(opt, sch);
  }
  
  
  public MBOptions getOptions() {
    return opt;
  }
  
  
  public ISchema getSchema() {
    return schema;
  }
  
  
  public Properties getProperties() {
    return prop;
  }
  
  
  @Override
  public Inspection handle() {
    if(schema.isEmpty()) return this;
    Iterator<ITable> itb = schema.getTables().iterator();
    prop.setProperty("schema", schema.getName());
    while(itb.hasNext()) {
      ITable table = itb.next();
      Iterator<IColumn<?>> icl = table.getColumns().iterator();
      while(icl.hasNext()) {
        IColumn col = icl.next();
        String key = "column."+ table.getName()+ "."+ col.getName();
        prop.setProperty(key, col.getType().getName());
      }
    }
    return this;
  }
  
  
  public void save() throws MaxBeanException {
    if(prop.isEmpty()) return;
    File f = new File((opt.getOutput() != null 
        ? opt.getOutput() 
        : new File("./")), 
        schema.getName()+ ".properties"
    );
    System.out.println("  Properties file: "+ f);
    try(FileWriter fw = new FileWriter(f)) {
      prop.store(fw, "Inspection properties file "
          + "for database schema ["
          + schema.getName()+ "]."
      );
    } catch(IOException e) {
      throw new MaxBeanException("Error saving properties file: "
              + opt.getProps(), e
      );
    }
  }
  
}
