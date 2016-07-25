/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.util;

import us.pserver.maxb.sql.spec.Named;



/**
 *
 * @author juno
 */
public class CamelCaseName implements Named {
	
	private final String name;
	
	
	public CamelCaseName(String name) {
		if(name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Name must be not null"
			);
		}
		this.name = name;
	}
	
	
	public static CamelCaseName of(String name) {
		return new CamelCaseName(name);
	}


	@Override
	public String getName() {
		return this.name;
	}
	
	
	public String toCamelCase(boolean firstUpper) {
		char[] chars = name.toLowerCase().toCharArray();
		StringBuilder sb = new StringBuilder();
		char SU = '_', SD = '-';
		boolean toupper = false;
		for(char c : chars) {
			if(c == SU || c == SD) {
				toupper = true;
			}
			else {
				if(toupper || (firstUpper && sb.length() == 0)) {
					sb.append(String.valueOf(c).toUpperCase());
					toupper = false;
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
	
	
	@Override
	public String toString() {
		return toCamelCase(true);
	}
	
}
