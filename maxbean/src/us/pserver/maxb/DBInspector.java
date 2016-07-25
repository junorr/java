/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;



/**
 *
 * @author juno
 */
public abstract class DBInspector<T> {
	
	public static final String SQL_RESOURCE = "/resources/sql.properties";
	
	protected final Connection conn;
	
	protected final Properties sqlprop;
	
	
	protected DBInspector(Connection cn) {
		if(cn == null) {
			throw new IllegalArgumentException(
					"Connection must be not null"
			);
		}
		this.conn = cn;
		try {
			this.sqlprop = new Properties();
			this.sqlprop.load(this.getClass()
					.getResourceAsStream(SQL_RESOURCE)
			);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public Connection getConnection() {
		return conn;
	}
	
	
	public Properties getSqlProperties() {
		return sqlprop;
	}
	
	
	protected void close(Statement st) {
		try {
			st.close();
		} catch(Exception e) {}
	}
	
	
	protected void close(ResultSet rs) {
		try {
			rs.close();
		} catch(Exception e) {}
	}
	
	
	public abstract T inspect() throws DBInspectorException;
	
	
	
	public static class DBInspectorException extends RuntimeException {
		public DBInspectorException(String msg) {
			super(msg);
		}
		public DBInspectorException(String msg, Exception cause) {
			super(msg, cause);
		}
		public DBInspectorException(Exception cause) {
			super(cause);
		}
	}
	
}
