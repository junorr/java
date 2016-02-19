/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.cli;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.kohsuke.args4j.Option;
import us.pserver.maxb.sql.Column;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.type.Typed;



/**
 *
 * @author juno
 */
public class MBOptions {
	
	@Option(
			name = "-d",
			aliases = {"--db-config"},
			forbids = {"--db-driver", "--db-url", "--db-user", "--db-pwd"},
			usage = "Database connection configuration (.properties file)"
	)
	private File dbConfig;
	
	
	@Option(
			name = "--db-driver",
			forbids = {"-c"},
			depends = {"--db-url", "--db-user", "--db-pwd"},
			usage = "Database Driver configuration (e.g. 'org.mysql.jdbc.Driver')"
	)
	private String dbDriver;
	
	
	@Option(
			name = "--db-url",
			forbids = {"-c"},
			depends = {"--db-driver", "--db-user", "--db-pwd"},
			usage = "Database URL connection (e.g. 'jdbc:mysql://localhost:3306')"
	)
	private String dbUrl;
	
	
	@Option(
			name = "--db-user",
			forbids = {"-c"},
			depends = {"--db-url", "--db-driver", "--db-pwd"},
			usage = "Database User"
	)
	private String dbUser;
	
	
	@Option(
			name = "--db-pwd",
			forbids = {"-c"},
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
			name = "-t",
			aliases = {"--tables"},
			forbids = {"-f"},
			depends = {"-s"},
			usage = "List of schema tables to inspect (comma separated: 'arg1,arg2,argN...')"
	)
	private String[] tables;
	
	
	@Option(
			name = "-c",
			aliases = {"--columns"},
			forbids = {"-f"},
			depends = {"-s"},
			usage = "List of additional columns than those included with database table. The accepted format is 'col1:sqlType,col2:sqlType...'"
	)
	private String[] columns;

	
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
			depends = {"-s"},
			usage = "Show MaxBean version info"
	)
	private boolean version;
	
	
	public File getDBConfigFile() {
		return dbConfig;
	}
	
	
	private void readDBConfig() throws MaxBeanException {
		if(dbConfig == null) return;
		try {
			System.out.println("* Reading Database configuration...");
			System.out.println("  Properties file: "+ dbConfig);
			Properties p = new Properties();
			p.load(new FileReader(dbConfig));
			dbDriver = p.getProperty("db.driver");
			dbUrl = p.getProperty("db.url");
			dbUser = p.getProperty("db.user");
			dbPwd = p.getProperty("db.pwd");
			System.out.println("  Done!");
		} catch(IOException e) {
			throw new MaxBeanException(
					"Error reading db configuration file", e
			);
		}
	}
	
	
	public Connection createConnection() throws MaxBeanException {
		if(dbConfig == null 
				&& (dbDriver == null 
				|| dbUrl == null 
				|| dbUser == null 
				|| dbPwd == null)) {
			throw new MaxBeanException(
					"No database configuration specified"
			);
		}
		if(dbConfig != null) {
			readDBConfig();
		}
		try {
			System.out.println("* Connecting to Database ["+ dbUrl+ "]...");
			Class.forName(dbDriver);
			Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			System.out.println("  Done!");
			return con;
		} catch(Exception e) {
			throw new MaxBeanException(
					"Error creating db connection", e
			);
		}
	}
	
	
	public List<IColumn<?>> getAdditionalColumns() {
		if(columns == null || columns.length < 1) {
			return null;
		}
		List<IColumn<?>> cols = new LinkedList<>();
		for(String sc : columns) {
			if(!sc.contains(":")) {
				throw new IllegalArgumentException(
						"Invalid additional column format: "+ sc
				);
			}
			String[] ss = sc.split(":");
			IColumn<?> c = new Column(ss[0], Typed.of(ss[1]).getType());
			cols.add(c);
		}
		return cols;
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
	
	
	public String[] getTables() {
		return tables;
	}
	
	
	public boolean isVersion() {
		return version;
	}
	
	
	public Properties getProperties() throws MaxBeanException {
		if(props == null) return null;
		try {
			System.out.println("* Reading inspection properties file...");
			Properties p = new Properties();
			p.load(new FileReader(props));
			System.out.println("  Done!");
			return p;
		} catch(IOException e) {
			throw new MaxBeanException(
					"Error reading properties file: "+ props, e
			);
		}
	}
	
	
	public void printVersion() {
		System.out.println("|             MaxBean V1             |");
		System.out.println("|  Copyright (c) 2016 Juno Roesler   |");
		System.out.println("|      <juno.roesler@bb.com.br>      |");
		System.out.println("|   Distributed under GNU/LGPL V3    |");
		System.out.println("+------------------------------------+");
		System.out.println(" Notice:");
		System.out.println("  MaxBean uses Args4j for arguments parsing (MIT License)");
		System.out.println(" Links:");
		//System.out.println("  MaxBean [http://github.com/junorr/maxbean");
		System.out.println("  GNU/LGPL V3 [http://www.gnu.org/licenses/lgpl-3.0.en.html]");
		System.out.println("  Args4j      [http://args4j.kohsuke.org]");
		System.out.println("  MIT License [http://www.opensource.org/licenses/mit-license.php]");
		System.out.println();
	}
	
}
