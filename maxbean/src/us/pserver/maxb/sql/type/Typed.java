/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.type;

import us.pserver.maxb.sql.spec.Named;



/**
 *
 * @author juno
 */
public class Typed implements Named {
	
	private final String name;
	
	
	public Typed(String name) {
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Name must be not null"
			);
		}
		this.name = name;
	}
	
	
	public static Typed of(String name) {
		return new Typed(name);
	}
	
	
	@Override
	public String getName() {
		return name;
	}
	
	
	public SqlType<?> getType() {
		String stype = name.toLowerCase();
		int ip = stype.indexOf("(");
		int is = stype.indexOf(" ");
		if(ip > 0 || is > 0) {
			int i = Math.min(ip, is);
			if(i <= 0) {
				i = (ip <= 0 ? is : ip);
			}
			stype = stype.substring(0, i);
		}
		SqlType<?> sqltype = null;
		switch(stype) {
			case "char":
				sqltype = Text.CHAR;
				break;
			case "varchar":
				sqltype = Text.VARCHAR;
				break;
			case "text":
				sqltype = Text.TEXT;
				break;
			case "mediumtext":
				sqltype = Text.MEDIUMTEXT;
				break;
			case "enum":
				sqltype = Text.ENUM;
				break;
			case "tinyint":
				sqltype = Numeric.TINYINT;
				break;
			case "smallint":
				sqltype = Numeric.SMALLINT;
				break;
			case "int":
				sqltype = Numeric.INT;
				break;
			case "integer":
				sqltype = Numeric.INTEGER;
				break;
			case "mediumint":
				sqltype = Numeric.MEDIUMINT;
				break;
			case "decimal":
				sqltype = Numeric.DECIMAL;
				break;
			case "bigint":
				sqltype = Numeric.BIGINT;
				break;
			case "numeric":
				sqltype = Numeric.NUMERIC;
				break;
			case "float":
				sqltype = Numeric.FLOAT;
				break;
			case "double":
				sqltype = Numeric.DOUBLE;
				break;
			case "real":
				sqltype = Numeric.REAL;
				break;
			case "date":
				sqltype = DateTime.DATE;
				break;
			case "datetime":
				sqltype = DateTime.DATETIME;
				break;
			case "time":
				sqltype = DateTime.TIME;
				break;
			case "timestamp":
				sqltype = DateTime.TIMESTAMP;
				break;
			default:
				throw new IllegalArgumentException(
						"Not supported type: "+ name
				);
		}
		return sqltype;
	}
	
}
