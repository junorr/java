/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.rules;

import us.pserver.jc.util.DateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import us.pserver.jc.WakeRule;



/**
 *
 * @author juno
 */
public class RuleBuilder {
	
	private DateTime start;
	
	private int amount;
	
	private int repeat;
	
	private TemporalUnit unit;
	
	private WakeRule rule;
	
	
	public RuleBuilder() {
		start = null;
		amount = 0;
		unit = null;
		repeat = 0;
		rule = null;
	}
	
	
	public static RuleBuilder ruleAt(DateTime dtm) {
		return new RuleBuilder().at(dtm);
	}
	
	
	public static RuleBuilder ruleIn(int amount, TemporalUnit unit) {
		return new RuleBuilder().in(amount, unit);
	}
	
	
	public RuleBuilder at(DateTime dtm) {
		if(dtm != null) {
			start = dtm;
		}
		return this;
	}
	
	
	public RuleBuilder in(int amount, TemporalUnit unit) {
		if(amount > 0 && unit != null) {
			this.amount = amount;
			this.unit = unit;
		}
		return this;
	}
	
	
	public RuleBuilder repeat(int times) {
		this.repeat = times;
		return this;
	}
	
	
	public RuleBuilder condition(Predicate<TemporalAccessor> ... conds) {
		if(conds != null 
				&& conds.length > 0 
				&& rule != null) {
			rule = new PredicateRule(rule, conds);
		}
		return this;
	}
	
	
	public RuleBuilder condition(BinaryOperator<Boolean> operator, Predicate<TemporalAccessor> ... conds) {
		if(conds != null 
				&& conds.length > 0 
				&& rule != null) {
			PredicateRule pr = new PredicateRule(rule, operator);
			pr.getConditions().addAll(Arrays.asList(conds));
			rule = pr;
		}
		return this;
	}
	
	
	public Optional<WakeRule> build() {
		WakeRule wake = null;
		if(amount > 0 && unit != null) {
			if(start != null) {
				wake = new TimeAmountRule(start, amount, unit);
			}	else {
				wake = new TimeAmountRule(amount, unit);
			}
		}	else if(start != null) {
			wake = new DateTimeRule(start);
		}
		if(wake != null) {
			if(rule != null && rule instanceof ComposedRule) {
				((ComposedRule)rule).addRule(wake);
			} else {
				rule = wake;
			}
		}
		if(repeat > 0 && rule != null) {
			rule = new RecurrentRule(rule, repeat);
		}
		return (rule != null 
				? Optional.of(rule) 
				: Optional.empty()
		);
	}
	
	
	public RuleBuilder compose() {
		this.build();
		if(rule == null) return this;
		RuleBuilder rb = new RuleBuilder();
		rb.rule = (rule instanceof ComposedRule 
				? rule : new ComposedRule().addRule(rule)
		);
		return rb;
	}
	
	
	public static void main(String[] args) {
		Optional<WakeRule> rule = new RuleBuilder()
				.in(60, ChronoUnit.MINUTES)
				.compose()
				.in(90, ChronoUnit.MINUTES)
				.compose()
				.in(105, ChronoUnit.MINUTES)
				.compose()
				.repeat(2).build();
		System.out.println(rule.get());
	}
	
}
