/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import us.pserver.maxb.sql.Column;
import us.pserver.maxb.sql.spec.Constants;
import us.pserver.maxb.sql.Table;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.sql.type.SqlType;
import us.pserver.maxb.sql.type.Typed;



/**
 *
 * @author juno
 */
public class TableInspector extends DBInspector<ITable> {
	
	private final String name;
	
	private final String schema;
	
	private ITable table;
	
	
	public TableInspector(Connection cn, String schema, String table) {
		super(cn);
		if(table == null || table.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Table name must be not null"
			);
		}
		if(schema == null || schema.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Schema name must be not null"
			);
		}
		this.name = table;
		this.schema = schema;
	}
	
	
	public String getSchemaName() {
		return this.schema;
	}
	
	
	public String getTableName() {
		return this.name;
	}
	
	
	public ITable getTable() {
		return table;
	}
	
	
	@Override
	public ITable inspect() throws DBInspectorException {
		String sql = sqlprop.getProperty(Constants.SQL_SHOW_COLS);
		Statement st = null;
		ResultSet rs = null;
		try {
			sql = sql.replace(Constants.PARAM_SCHEMA, schema);
			sql = sql.replace(Constants.PARAM_TABLE, name);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			table = new Table(name, schema);
			while(rs.next()) {
				table.addColumn(new Column(
						rs.getString(1), 
						Typed.of(rs.getString(2)).getType())
				);
			}
		}
		catch(SQLException e) {
			throw new DBInspectorException("SQL Error: "+ sql, e);
		}
		finally {
			close(st);
			close(rs);
		}
		return table;
	}
	
}
