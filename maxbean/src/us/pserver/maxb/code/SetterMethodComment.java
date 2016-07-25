/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import us.pserver.maxb.code.spec.ICode;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class SetterMethodComment extends AbstractColumnCode implements ICode {
	
	public SetterMethodComment(IColumn<?> col, Ident ident) {
		super(col, ident);
	}
	
	
	public String commentCode() {
		StringBuilder sb = new StringBuilder();
		return new StringBuilder()
				.append(ident.getIdentation())
				.append("/**\n")
				.append(ident.getIdentation())
				.append(" * ").append("Set the value relative to the database\n")
				.append(ident.getIdentation())
				.append(" * ").append("column [")
				.append(column.getName())
				.append(": ")
				.append(column.getType().getName())
				.append("].\n")
				.append(ident.getIdentation())
				.append(" * ").append("@param ")
				.append(CamelCaseName.of(column.getName()).toCamelCase(false))
				.append(" The value of the column [")
				.append(column.getName())
				.append("].\n")
				.append(ident.getIdentation())
				.append(" * ").append("@return This modified object instance.\n")
				.append(ident.getIdentation())
				.append(" */")
				.toString();
	}


	@Override
	public String getCode() {
		return commentCode();
	}
	
}
