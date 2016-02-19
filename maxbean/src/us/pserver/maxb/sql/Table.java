/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import us.pserver.maxb.sql.spec.AbstractIList;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.sql.type.SqlType;



/**
 *
 * @author juno
 */
public class Table extends AbstractIList<IColumn<?>> implements ITable {
	
	private final String schema;
	
	private final String name;
	
	
	public Table(String name, String schema) {
		super(new ArrayList<IColumn<?>>());
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Table name must be not null"
			);
		}
		if(schema == null) {
			throw new IllegalArgumentException(
					"Schema must be not null"
			);
		}
		this.schema = schema;
		this.name = name;
	}
	
	
	public Table(String name, String schema, IColumn<?> ... cols) {
		this(name, schema);
		this.addColumns(cols);
	}
	
	
	@Override
	public Table addColumn(IColumn<?> col) {
		if(col != null) {
			list.add(col);
		}
		return this;
	}
	
	
	public <T> Table addColumn(String name, SqlType<T> type, T value) {
		if(name != null && type != null) {
			this.addColumn(new Column<T>(name, type, value));
		}
		return this;
	}
	
	
	@Override
	public Table addColumns(IColumn<?> ... cols) {
		if(cols != null && cols.length > 0) {
			list.addAll(Arrays.asList(cols));
		}
		return this;
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public String getSchema() {
		return schema;
	}
	
	
	@Override
	public List<IColumn<?>> getColumns() {
		return list;
	}
	
	
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 11 * hash + Objects.hashCode(this.name);
		hash = 11 * hash + Objects.hashCode(this.schema);
		hash = 11 * hash + Objects.hashCode(this.list);
		return hash;
	}


	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final Table other = (Table) obj;
		if(!Objects.equals(this.name, other.name)) {
			return false;
		}
		if(!Objects.equals(this.schema, other.schema)) {
			return false;
		}
		if(!Objects.equals(this.list, other.list)) {
			return false;
		}
		return true;
	}
	
	
	@Override
	public String toString() {
		return "Table("+ schema+ "."+ name+ ", columns="+ list.size()+ ")";
	}
	
}
