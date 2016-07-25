/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import us.pserver.maxb.code.spec.IIdentCode;
import us.pserver.maxb.code.spec.ITableCode;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public abstract class AbstractTableCode implements ITableCode, IIdentCode {
	
	protected final ITable table;
	
	protected final Ident ident;
	
	
	protected AbstractTableCode(ITable col, Ident ident) {
		if(col == null) {
			throw new IllegalArgumentException(
					"Column must be not null"
			);
		}
		if(ident == null) {
			throw new IllegalArgumentException(
					"Ident must be not null"
			);
		}
		this.table = col;
		this.ident = ident;
	}

	
	@Override
	public ITable getTable() {
		return table;
	}
	

	@Override
	public Ident getIdent() {
		return ident;
	}
	
}
