/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.type;

import java.math.BigDecimal;



/**
 *
 * @author juno
 */
public final class Numeric<T extends Numeric> extends AbstractType<T> {
	
	public static final Numeric
			
			TINYINT = new Numeric(Short.class, "tinyint"),
			
			SMALLINT = new Numeric(Integer.class, "smallint"),
			
			INT = new Numeric(Integer.class, "int"),
			
			INTEGER = new Numeric(Integer.class, "integer"),
			
			MEDIUMINT = new Numeric(Integer.class, "mediumint"),
			
			BIGINT = new Numeric(Long.class, "bigint"),
			
			DECIMAL = new Numeric(BigDecimal.class, "decimal"),
			
			NUMERIC = new Numeric(BigDecimal.class, "numeric"),
			
			FLOAT = new Numeric(Float.class, "float"),
			
			DOUBLE = new Numeric(Double.class, "double"),
			
			REAL = new Numeric(Double.class, "real");
	
	
	private Numeric(Class<T> type, String name) {
		super(type, name);
	}
	
}
