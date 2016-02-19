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
import us.pserver.maxb.sql.spec.ISchema;
import us.pserver.maxb.sql.spec.ITable;



/**
 *
 * @author juno
 */
public class Schema extends AbstractIList<ITable>  implements ISchema {
	
	private final String name;
	
	
	public Schema(String name) {
		super(new ArrayList<ITable>());
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Schema name must be not null"
			);
		}
		this.name = name;
	}
	
	
	@Override
	public Schema addTable(ITable t) {
		if(t != null) {
			list.add(t);
		}
		return this;
	}
	
	
	@Override
	public Schema addTables(ITable ... tbs) {
		if(tbs != null && tbs.length > 0) {
			list.addAll(Arrays.asList(tbs));
		}
		return this;
	}
	
	
	@Override
	public List<ITable> getTables() {
		return list;
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.name);
		hash = 97 * hash + Objects.hashCode(this.list);
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
		final Schema other = (Schema) obj;
		if(!Objects.equals(this.name, other.name)) {
			return false;
		}
		if(!Objects.equals(this.list, other.list)) {
			return false;
		}
		return true;
	}
	
	
	@Override
	public String toString() {
		return "Schema("+ name+ ", tables="+ this.size()+ ")";
	}
	
}
