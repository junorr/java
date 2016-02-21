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

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import us.pserver.maxb.DBInspector;
import us.pserver.maxb.SchemaInspector;
import us.pserver.maxb.cli.MBOptions;
import us.pserver.maxb.cli.MaxBeanException;
import us.pserver.maxb.config.SchemaConfig;
import us.pserver.maxb.sql.Schema;
import us.pserver.maxb.sql.spec.ISchema;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/02/2016
 */
public class SchemaParser extends AbstractOptionsParser<ISchema> {

  public SchemaParser(MBOptions opt) {
    super(opt);
  }
  
  
  public static OptionsParser<ISchema> get(MBOptions opt) {
    return new SchemaParser(opt);
  }
  
  
  @Override
  public ISchema parse() throws MaxBeanException {
    if(opt.getProps() == null
        && opt.getSchema() == null) {
      throw new MaxBeanException("No database Schema specified");
    }
    try {
      ISchema schema = null;
      if(opt.getProps() != null) {
        System.out.println("* Reading inspection file...");
        System.out.println("  Properties file: "+ opt.getProps());
        Properties p = opt.getProperties(opt.getProps());
        schema = SchemaConfig.from(p).read();
        System.out.println("  Done!");
      }
      else {
        schema = new Schema(opt.getSchema());
        Connection con = ConnectionParser.get(opt).parse();
        System.out.println("* Inspecting database ["+ opt.getSchema()+ "]...");
        SchemaInspector insp = new SchemaInspector(con, schema.getName());
        schema = insp.inspect();
        System.out.println("  Done!");
        System.out.println("  "+ schema);
      }
      return schema;
    }
    catch(DBInspector.DBInspectorException | IOException e) {
      throw new MaxBeanException("Error reading schema", e);
    }
  }
  
}
