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
import us.pserver.maxb.sql.spec.Constants;
import us.pserver.maxb.sql.Schema;
import us.pserver.maxb.sql.spec.ISchema;



/**
 *
 * @author juno
 */
public class SchemaInspector extends DBInspector<ISchema> {

	private ISchema schema;
	
	public SchemaInspector(Connection cn, String schemaName) {
		super(cn);
		if(schemaName == null || schemaName.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Schema name must be not null"
			);
		}
		this.schema = new Schema(schemaName);
	}
	
	
	public ISchema getSchema() {
		return schema;
	}
	

	@Override
	public ISchema inspect() throws DBInspectorException {
		String sql = sqlprop.getProperty(Constants.SQL_SHOW_TABLES);
		Statement st = null;
		ResultSet rs = null;
		try {
			sql = sql.replace(Constants.PARAM_SCHEMA, schema.getName());
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			schema.getTables().clear();
			while(rs.next()) {
				TableInspector tins = new TableInspector(conn, schema.getName(), rs.getString(1));
				schema.addTable(tins.inspect());
			}
		}
		catch(SQLException e) {
			throw new DBInspectorException("SQL Error: "+ sql, e);
		}
		finally {
			close(st);
			close(rs);
		}
		return schema;
	}
	
}
