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

package us.pserver.maxb.cli;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import us.pserver.maxb.SchemaInspector;
import us.pserver.maxb.code.IFaceCode;
import us.pserver.maxb.config.SchemaConfig;
import us.pserver.maxb.sql.Schema;
import us.pserver.maxb.sql.spec.ISchema;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/02/2016
 */
public class MaxBean {

  private final MBOptions opts;
  
  private Connection conn;
  
  private ISchema schema;
  
  private File outputDir;
  
  
  public MaxBean(MBOptions opts) throws MaxBeanException {
    if(opts == null) {
      throw new IllegalArgumentException(
          "MBOptions must be not null"
      );
    }
    this.opts = opts;
    this.configureConnection();
    this.configureSchema();
    outputDir = (opts.isOutputDir() 
        ? opts.getOutputDir() 
        : new File("./")
    );
    this.execute();
  }
  
  
  private void configureConnection() throws MaxBeanException {
    String driver = null;
    String url = null;
    String user = null;
    String pwd = null;
    try {
      if(opts.isConfigFile()) {
        Properties p = new Properties();
        p.load(new FileReader(opts.getConfigFile()));
        driver = p.getProperty("db.driver");
        url = p.getProperty("db.url");
        user = p.getProperty("db.user");
        pwd = p.getProperty("db.password");
      } 
      else if(!opts.isDBDriver()
          || !opts.isDBUrl()
          || !opts.isDBUser()
          || !opts.isDBPassword()) {
        throw new IllegalArgumentException(
            "To configure a database connection, "
            + "all db options must be specified. "
            + "Type '-h' or '--help' for more informations."
        );
      }
      else {
        driver = opts.getDBDriver();
        url = opts.getDBUrl();
        user = opts.getDBUser();
        pwd = opts.getDBPassword();
      }
      if(driver == null) {
        throw new IllegalArgumentException("Invalid db driver: "+ driver);
      }
      if(url == null) {
        throw new IllegalArgumentException("Invalid db url: "+ url);
      }
      if(user == null) {
        throw new IllegalArgumentException("Invalid db user: "+ user);
      }
      if(pwd == null) {
        throw new IllegalArgumentException("Invalid db password: "+ pwd);
      }
      Class.forName(driver);
      conn = DriverManager.getConnection(url, user, pwd);
    }//try
    catch(Exception e) {
      throw new MaxBeanException("Error configuring database connection", e);
    }
  }
  
  
  private void configureSchema() throws MaxBeanException {
    try {
      if(opts.isSchema()) {
        schema = new Schema(opts.getSchema());
      }
      else if(opts.isProperties()) {
        Properties p = new Properties();
        p.load(new FileReader(opts.getProperties()));
        schema = SchemaConfig.from(p).read();
      }
      else {
        throw new IllegalArgumentException(
            "Schema not configured. Please inform "
                + "schema through '-s' or '-p' options."
        );
      }
    }
    catch(Exception e) {
      throw new MaxBeanException("Error configuring schema", e);
    }
  }
  
  
  public void execute() throws MaxBeanException {
    try {
      SchemaInspector insp = new SchemaInspector(conn, schema.getName());
      schema = insp.inspect();
      int ident = (opts.isIdentation() ? opts.getIdentation() : 2);
      if(opts.isInspect()) {
        File f = new File(outputDir, schema.getName()+ ".properties");
        Properties p = SchemaConfig.from(schema).write();
        p.store(new FileWriter(f), "Inspect file for the schema ["+ schema.getName()+ "]");
      }
      else if(opts.isGenerate()) {
        if(schema.isEmpty()) {
          throw new IllegalArgumentException(
              "No tables selected for the schema: "+ schema.getName()
          );
        }
        if(!opts.isPackage()) {
          throw new IllegalArgumentException(
              "No java package specified. Please inform "
                  + "the package name for the source code."
          );
        }
        List<ITable> tables = schema.getTables();
        List<ITable> exec = new LinkedList<>();
        if(opts.isTables()) {
          for(String tname : opts.getTables()) {
            for(ITable t : tables) {
              if(t.getName().equals(tname)) {
                exec.add(t);
              }
            }//tables
          }//table names
          if(exec.isEmpty()) {
            throw new IllegalArgumentException(
                "No tables selected for the schema: "+ schema.getName()
            );
          }
          CamelCaseName schemaName = CamelCaseName.of(schema.getName());
          File dir = new File(outputDir, schemaName.toString());
          dir.mkdirs();
          Iterator<ITable> it = exec.iterator();
          while(it.hasNext()) {
            ITable t = it.next();
            File f = new File(dir, 
                CamelCaseName.of(t.getName()).toString()+ ".java"
            );
            Ident id = new Ident(' ', ident);
            IFaceCode ifc = new IFaceCode()
          }
        }// -t
      }//generate
    }
    catch(Exception e) {
      throw new MaxBeanException(
          "Error inspecting schema: "+ schema.getName(), e
      );
    }
  }
  
}
