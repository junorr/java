/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import us.pserver.maxb.code.spec.IConstructorCode;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class EmptyConstructorCode 
		extends AbstractTableCode 
		implements IConstructorCode {
	
	
	public EmptyConstructorCode(ITable table, Ident ident) {
		super(table, ident);
	}
	
	
	@Override
	public String constructorCode() {
		return new StringBuilder()
				.append(ident.getIdentation())
				.append("public ")
				.append(CamelCaseName.of(table.getName()))
				.append("() {}\n")
				.toString();
	}


	@Override
	public String getCode() {
		return constructorCode();
	}
	
}
