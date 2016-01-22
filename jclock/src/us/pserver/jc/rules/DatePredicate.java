/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.rules;

import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.function.Predicate;
import us.pserver.jc.util.NotNull;



/**
 *
 * @author juno
 */
public class DatePredicate implements Predicate<TemporalAccessor> {
	
	private final TemporalField field;
	
	private final int value;
	
	
	public DatePredicate(TemporalField field, int value) {
		this.field = NotNull.of(field).getOrFail();
		this.value = value;
	}
	
	public TemporalField getTemporalField() {
		return this.field;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public boolean test(TemporalAccessor t) {
		if(t == null) return false;
		return t.get(field) == this.value;
	}
	
	@Override
	public String toString() {
		return "DatePredicate{field="+ field+ ", value="+ value+ "}";
	}
	
}
