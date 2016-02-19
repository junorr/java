/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import us.pserver.maxb.code.spec.IConstructorCode;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class FullConstructorCode 
		extends AbstractTableCode 
		implements IConstructorCode {

	
	public FullConstructorCode(ITable table, Ident ident) {
		super(table, ident);
	}
	
	
	@Override
	public String constructorCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(ident.getIdentation())
				.append("public ")
				.append(CamelCaseName.of(table.getName()))
				.append("(\n");
		ident.increment().increment();
		for(int i = 0; i < table.size(); i++) {
			IColumn<?> c = table.getColumns().get(i);
			sb.append(ident.getIdentation())
					.append(c.getType().getJavaClass().getName())
					.append(" ")
					.append(CamelCaseName.of(c.getName()).toCamelCase(false));
			if(i < table.size()-1) {
				sb.append(", \n");
			} else {
				sb.append(") {\n");
			}
		}
		ident.decrement();
		for(IColumn<?> c : table.getColumns()) {
			String var = CamelCaseName.of(c.getName()).toCamelCase(false);
			sb.append(ident.getIdentation())
					.append("this.")
					.append(var)
					.append(" = ")
					.append(var)
					.append(";\n");
		}
		return sb.append(
				ident.decrement().getIdentation()
		).append("}\n").toString();
	}


	@Override
	public String getCode() {
		return constructorCode();
	}
	
}
