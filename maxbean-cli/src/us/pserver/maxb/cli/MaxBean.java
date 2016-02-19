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
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import us.pserver.maxb.SchemaInspector;
import us.pserver.maxb.code.BeanReaderFactory;
import us.pserver.maxb.code.ClassCode;
import us.pserver.maxb.code.IFaceCode;
import us.pserver.maxb.config.SchemaConfig;
import us.pserver.maxb.file.StringFile;
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
  
  private final File outputDir;
  
  
  public MaxBean(MBOptions opts) throws MaxBeanException {
    if(opts == null) {
      throw new IllegalArgumentException(
          "MBOptions must be not null"
      );
    }
    this.opts = opts;
    outputDir = (opts.isOutputDir() 
        ? opts.getOutputDir() 
        : new File("./")
    );
  }
	
	
	public Connection getConnection() {
		return conn;
	}
  
  
  private void configureConnection() throws MaxBeanException {
    String driver = null;
    String url = null;
    String user = null;
    String pwd = null;
		System.out.println("* Configuring DB connection...");
    try {
      if(opts.isDBConfig()) {
				System.out.println("  Properties file: "+ opts.getDBConfig());
        Properties p = new Properties();
        p.load(new FileReader(opts.getDBConfig()));
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
			System.out.println("  Loading driver: "+ driver+ "...");
      Class.forName(driver);
			System.out.println("  Connecting ["+ url+ "]...");
      conn = DriverManager.getConnection(url, user, pwd);
			System.out.println("  Done!");
    }//try
    catch(Exception e) {
      throw new MaxBeanException("Error configuring database connection", e);
    }
  }
  
  
  private void configureSchema() throws MaxBeanException {
		System.out.println("* Configuring Schema...");
    try {
      if(opts.isProperties()) {
				System.out.println("  Properties file: "+ opts.getProperties());
        Properties p = new Properties();
        p.load(new FileReader(opts.getProperties()));
        schema = SchemaConfig.from(p).read();
      }
			else {
				if(!opts.isSchema()) {
					throw new IllegalArgumentException(
							"Missing Schema argument. See --help for more information"
					);
				}
				schema = new Schema(opts.getSchema());
				System.out.println("  Inspecting database...");
	      SchemaInspector insp = new SchemaInspector(conn, schema.getName());
		    schema = insp.inspect();
			}
			System.out.println("  Done!");
    }
    catch(Exception e) {
      throw new MaxBeanException("Error configuring schema", e);
    }
  }
	
	
	private void inspect() throws IOException {
		File f = new File(outputDir, schema.getName()+ ".properties");
		System.out.println("* Writing inspection file: "+ f);
		List<ITable> exec = getExecTables();
		schema.getTables().clear();
		schema.getTables().addAll(exec);
		Properties p = SchemaConfig.from(schema).write();
		p.store(new FileWriter(f), "Inspect file for schema ["+ schema.getName()+ "]");
		System.out.println("  Done!");
	}
	
	
	private void genSourceCode() throws IOException {
		System.out.println("* Generating Java source code...");
    int ident = (opts.isIdentation() ? opts.getIdentation() : 2);
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
		List<ITable> exec = this.getExecTables();
		if(exec == null || exec.isEmpty()) {
			throw new IllegalArgumentException(
					"No tables selected for the schema: "+ schema.getName()
			);
		}
		File dir = new File(outputDir, schema.getName());
		dir.mkdirs();
		Iterator<ITable> it = exec.iterator();
		while(it.hasNext()) {
			ITable t = it.next();
			Ident id = new Ident(' ', ident);
			//interface
			IFaceCode ifc = this.genInterface(t, dir, id);
			//class
			this.genClass(ifc, dir);
			//BeanReader
			this.genBeanReader(t, dir, id);
		}
	}
	
	
	private List<ITable> getExecTables() {
		List<ITable> tables = schema.getTables();
		List<ITable> exec = null;
		if(opts.isTables()) {
			exec = new LinkedList<>();
			for(String tname : opts.getTables()) {
				for(ITable t : tables) {
					if(t.getName().equals(tname)) {
						exec.add(t);
					}
				}//tables
			}//table names
		}
		else {
			exec = tables;
		}
		return exec;
	}
	
	
	private IFaceCode genInterface(ITable t, File dir, Ident id) throws IOException {
		CamelCaseName cn = CamelCaseName.of(t.getName());
		File subdir = new File(dir, "iface");
		subdir.mkdirs();
		File fi = new File(subdir,
				"I" + cn.toString() + ".java"
		);
		System.out.println("  "+ fi);
		IFaceCode ifc = new IFaceCode(t, id, opts.getPackage());
		StringFile.of(fi.toPath(), ifc.interfaceCode()).write(true);
		return ifc;
	}

	
	private void genClass(IFaceCode ifc, File dir) throws IOException {
		CamelCaseName cn = CamelCaseName.of(ifc.getTable().getName());
		ClassCode cc = new ClassCode(ifc);
		File fc = new File(dir, 
				cn.toString() + ".java"
		);
		System.out.println("  "+ fc);
		StringFile.of(fc.toPath(), cc.classCode()).write(true);
	}
	
	
	private void genBeanReader(ITable t, File dir, Ident id) throws IOException {
		CamelCaseName cn = CamelCaseName.of(t.getName());
		File subdir = new File(dir, "reader");
		subdir.mkdirs();
		File fr = new File(subdir, 
				cn.toString() + "Reader.java"
		);
		System.out.println("  "+ fr);
		BeanReaderFactory bf = new BeanReaderFactory(
				t, id, opts.getPackage()
		);
		StringFile.of(fr.toPath(), bf.getCode()).write(true);
	}
	
  
  public void execute() throws MaxBeanException {
    try {
			if(!opts.isProperties()) {
				this.configureConnection();
			}
			this.configureSchema();
      if(opts.isInspect()) {
        this.inspect();
      }
      else {
				this.genSourceCode();
      }//generate
    } catch(Exception e) {
      throw new MaxBeanException(
          "Error Executing MaxBean", e
      );
    }
  }
  
}
