/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import us.pserver.maxb.code.spec.IColumnCode;
import us.pserver.maxb.code.spec.IIdentCode;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class AbstractColumnCode implements IColumnCode, IIdentCode {
	
	protected final IColumn<?> column;
	
	protected final Ident ident;
	
	
	protected AbstractColumnCode(IColumn<?> col, Ident ident) {
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
		this.column = col;
		this.ident = ident;
	}

	
	@Override
	public IColumn<?> getColumn() {
		return column;
	}


	@Override
	public Ident getIdent() {
		return ident;
	}
	
}
