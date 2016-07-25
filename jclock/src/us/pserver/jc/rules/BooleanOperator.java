/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.rules;

import java.util.function.BinaryOperator;



/**
 *
 * @author juno
 */
public enum BooleanOperator implements BinaryOperator<Boolean> {
	
	AND(),
	OR(),
	TRUE(),
	FALSE();
	
	@Override
	public Boolean apply(Boolean b1, Boolean b2) {
		if(this == TRUE) return true;
		if(this == FALSE) return false;
		if(this == AND) return b1 && b2;
		return b1 || b2;
	}
	
}
