/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import us.pserver.maxb.sql.Column;
import us.pserver.maxb.sql.Schema;
import us.pserver.maxb.sql.Table;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ISchema;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.sql.type.Typed;


/**
 *
 * @author juno
 */
public class SchemaConfig implements ISchemaConfig {
	
	private final ISchema schema;
	
	private final Properties props;
	
	
	public SchemaConfig(Properties config) {
		if(config == null) {
			throw new IllegalArgumentException(
					"Properties config must be not null"
			);
		}
		this.props = config;
		if(!props.contains("schema")) {
			throw new IllegalArgumentException(
					"Invalid properties. Do not contains 'schema'"
			);
		}
		schema = new Schema(props.getProperty("schema"));
	}
  
  
  public SchemaConfig(ISchema schema) {
    if(schema == null || schema.isEmpty()) {
      throw new IllegalArgumentException(
          "Schema must be not null and not empty"
      );
    }
    this.schema = schema;
    this.props = new Properties();
  }
  
  
  public static ISchemaConfig from(Properties config) {
    return new SchemaConfig(config);
  }
  
  
  public static ISchemaConfig from(ISchema schema) {
    return new SchemaConfig(schema);
  }
	
	
  @Override
	public ISchema getSchema() {
		return schema;
	}
	
	
  @Override
	public Properties getProperties() {
		return props;
	}
	
	
  @Override
	public ISchema read() {
		Iterator<String> keys = props
				.stringPropertyNames().iterator();
		Map<String,ITable> tables = new HashMap<>();
		while(keys.hasNext()) {
			String key = keys.next();
			if(key.startsWith("column.")) {
				String[] column = key.split("\\.");
				String value = props.getProperty(key);
				if(column.length == 3) {
					ITable tb = tables.get(column[1]);
					if(tb == null) {
						tb = new Table(column[1], schema.getName());
					}
					tb.addColumn(new Column(
							column[2], 
							Typed.of(value).getType())
					);
					tables.put(column[1], tb);
				}
			}
		}
		Iterator<ITable> tbs = tables.values().iterator();
		schema.getTables().clear();
		while(tbs.hasNext()) {
			schema.addTable(tbs.next());
		}
		return schema;
	}
	
	
  @Override
	public Properties write() {
		props.setProperty("schema", schema.getName());
		Iterator<ITable> tbs = schema.getTables().iterator();
		while(tbs.hasNext()) {
      ITable table = tbs.next();
			Iterator<IColumn<?>> cols = table.getColumns().iterator();
      while(cols.hasNext()) {
        IColumn<?> col = cols.next();
        props.setProperty("column."
            + table.getName()
            + "."+ col.getName(), 
            col.getType().getName()
        );
      }
		}
		return props;
	}
	
}
