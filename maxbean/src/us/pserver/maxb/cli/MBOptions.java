/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.cli;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.kohsuke.args4j.Option;



/**
 *
 * @author juno
 */
public class MBOptions {
	
	@Option(
			name = "-d",
			aliases = {"--db-config"},
			forbids = {"--db-driver", "--db-url", "--db-user", "--db-password"},
			usage = "Database connection configuration (.properties file)"
	)
	private File dbConfig;
	
	
	@Option(
			name = "--db-driver",
			forbids = {"-d"},
			depends = {"--db-url", "--db-user", "--db-password"},
			usage = "Database Driver configuration (e.g. 'org.mysql.jdbc.Driver')"
	)
	private String dbDriver;
	
	
	@Option(
			name = "--db-url",
			forbids = {"-d"},
			depends = {"--db-driver", "--db-user", "--db-password"},
			usage = "Database URL connection (e.g. 'jdbc:mysql://localhost:3306')"
	)
	private String dbUrl;
	
	
	@Option(
			name = "--db-user",
			forbids = {"-d"},
			depends = {"--db-url", "--db-driver", "--db-password"},
			usage = "Database User"
	)
	private String dbUser;
	
	
	@Option(
			name = "--db-password",
			forbids = {"-d"},
			depends = {"--db-url", "--db-user", "--db-driver"},
			usage = "Database Password"
	)
	private String dbPwd;
	
	
	@Option(
			name = "-k",
			aliases = {"--package"},
			forbids = {"-i"},
			usage = "Java package for the generated source code"
	)
	private String pack;
	
	
	@Option(
			name = "-s",
			aliases = {"--schema"},
			usage = "Database Schema to inspect"
	)
	private String schema;
	
	
	@Option(
			name = "-i",
			aliases = {"--inspect"},
			forbids = {"-k", "-f"},
			depends = {"-s"},
			usage = "Inspect the database and create a properties file with schema information"
	)
	private boolean inspect;
	
	
	@Option(
			name = "-f",
			aliases = {"--file"},
			forbids = {"-i", "-s", "-t"},
			depends = {"-k"},
			usage = "Properties file with schema information to generate source code"
	)
	private File props;
	
	
	@Option(
			name = "-o",
			aliases = {"--output"},
			usage = "Output directory for MaxBean generated files"
	)
	private File output;
	
	
	@Option(
			name = "-t",
			aliases = {"--tables"},
			forbids = {"-f"},
			depends = {"-s"},
			usage = "List of schema tables to inspect (comma separated: 'arg1,arg2,argN...')"
	)
	private String tables;
	
	
	@Option(
			name = "-c",
			aliases = {"--columns"},
			forbids = {"-f"},
			depends = {"-s"},
			usage = "List of additional columns than those included with database table. The accepted format is 'table1.column1=sqlType,tableN.columnN=sqlType...'"
	)
	private String columns;

	
	@Option(
			name = "-I",
			aliases = {"--identation"},
			forbids = {"-i"},
			depends = {"-k"},
			usage = "Set the number of spaces for source code identation"
	)
	private int identation;

	
	@Option(
			name = "-h",
			aliases = {"--help"},
			help = true,
			usage = "Show this help message"
	)
	private boolean help;
	
	
	@Option(
			name = "-v",
			aliases = {"--version"},
			forbids = {"-f", "-k", "-s", "-i", "-t", "-c", "-d", "--db-driver", "--db-url", "--db-user", "--db-pwd"},
			usage = "Show MaxBean version info"
	)
	private boolean version;
  
  
  public File getOutput() {
    return output;
  }
  
  
  public int getIdentation() {
    return identation;
  }
	
	
	public String getPackage() {
		return pack;
	}
	
	
	public String getSchema() {
		return schema;
	}
	
	
	public boolean isInspect() {
		return inspect;
	}
	
	
	public String getTables() {
		return tables;
	}
	
	
	public boolean isVersion() {
		return version;
	}


  public File getDbConfig() {
    return dbConfig;
  }


  public String getDbDriver() {
    return dbDriver;
  }


  public String getDbUrl() {
    return dbUrl;
  }


  public String getDbUser() {
    return dbUser;
  }


  public String getDbPwd() {
    return dbPwd;
  }


  public boolean getHelp() {
    return help;
  }


  public File getProps() {
    return props;
  }


  public String getColumns() {
    return columns;
  }


  public boolean isHelp() {
    return help;
  }
	
	
	public Properties getProperties(File f) throws IOException {
		if(f == null) return null;
		try(FileReader fr = new FileReader(f)) {
			Properties p = new Properties();
			p.load(fr);
			return p;
		}
	}
	
}
