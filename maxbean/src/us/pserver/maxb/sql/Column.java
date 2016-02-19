/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql;

import us.pserver.maxb.sql.spec.IColumn;
import java.util.Objects;
import us.pserver.maxb.sql.type.SqlType;



/**
 *
 * @author juno
 */
public class Column<T> implements IColumn<T> {
	
	private final String name;
	
	private final SqlType<T> type;
	
	private final T value;
	
	
	public Column(String name, SqlType<T> type, T value) {
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Column name must be not null"
			);
		}
		if(type == null) {
			throw new IllegalArgumentException(
					"Column type must be not null"
			);
		}
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	
	public Column(String name, SqlType<T> type) {
		this(name, type, null);
	}
	
	
	@Override
	public Column<T> withValue(T value) {
		return new Column<>(name, type, value);
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public SqlType<T> getType() {
		return type;
	}
	
	
	@Override
	public T getValue() {
		return value;
	}


	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.name);
		hash = 97 * hash + Objects.hashCode(this.type);
		hash = 97 * hash + Objects.hashCode(this.value);
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
		final Column<?> other = (Column<?>) obj;
		if(!Objects.equals(this.name, other.name)) {
			return false;
		}
		if(!Objects.equals(this.type, other.type)) {
			return false;
		}
		if(!Objects.equals(this.value, other.value)) {
			return false;
		}
		return true;
	}
	
	
	public String toString() {
		return "Column("+ name+ ":"+ type.getName()+ ")";
	}
	
}
