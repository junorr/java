/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.rules;

import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import us.pserver.jc.WakeRule;
import us.pserver.jc.util.DateTime;
import us.pserver.jc.util.NotNull;



/**
 *
 * @author juno
 */
public class PredicateRule implements WakeRule {
	
	private final List<Predicate<TemporalAccessor>> conditions;
	
	private final WakeRule rule;
	
	private final BinaryOperator<Boolean> operator;
	
	private final boolean negate;
	
	
	private PredicateRule(
			List<Predicate<TemporalAccessor>> cond, 
			WakeRule wr, 
			BinaryOperator<Boolean> operator,
			boolean negate
	) {
		this.conditions = NotNull.of(cond).getOrFail();
		this.rule = NotNull.of(wr).getOrFail();
		this.operator = NotNull.of(operator).getOrFail();
		this.negate = negate;
	}
	
	
	public PredicateRule(WakeRule rule) {
		this(rule, BooleanOperator.AND);
	}
	
	
	public PredicateRule(WakeRule rule, BinaryOperator<Boolean> operator) {
		this(new LinkedList<>(), rule, operator, false);
	}
	
	
	public PredicateRule(WakeRule rule, Predicate<TemporalAccessor> ... ps) {
		this(rule);
		if(ps != null && ps.length > 0) {
			conditions.addAll(Arrays.asList(ps));
		}
	}
	
	
	public PredicateRule(
			WakeRule rule, 
			BinaryOperator<Boolean> operator, 
			Predicate<TemporalAccessor> ... ps
	) {
		this(rule, operator);
		if(ps != null && ps.length > 0) {
			conditions.addAll(Arrays.asList(ps));
		}
	}
	
	
	public PredicateRule addPredicate(Predicate<TemporalAccessor> p) {
		if(p != null) {
			conditions.add(p);
		}
		return this;
	}
	
	
	public PredicateRule negate() {
		return new PredicateRule(conditions, rule, operator, !negate);
	}
	
	
	public List<Predicate<TemporalAccessor>> getConditions() {
		return conditions;
	}
	
	
	public WakeRule getWakeRule() {
		return rule;
	}
	
	
	public BinaryOperator<Boolean> getBooleanOperator() {
		return operator;
	}
	

	@Override
	public long resolve() {
		return rule.resolve();
	}


	@Override
	public Optional<WakeRule> next() {
		Optional<WakeRule> opt = rule.next();
		if(!opt.isPresent()) {
			return opt;
		}
		//System.out.println("    next: "+ opt.get());
		DateTime dt = DateTime.of(opt.get().resolve());
		boolean passed = !operator.apply(true, false);
		for(Predicate<TemporalAccessor> p : conditions) {
			passed = operator.apply(passed, p.test(dt.toLocalDT()));
			//if(passed) {
				//System.out.println("PASSED 4: "+ p+ " >> "+ dt);
			//} else {
				//System.out.println("FAILED 4: "+ p+ " >> "+ dt);
			//}
		}
		PredicateRule pr = new PredicateRule(
				conditions, opt.get(), operator, negate
		);
		passed = (negate ? !passed : passed);
		return (passed ? Optional.of(pr) : pr.next());
	}


	@Override
	public int hashCode() {
		int hash = 3;
		hash = 67 * hash + Objects.hashCode(this.conditions);
		hash = 67 * hash + Objects.hashCode(this.rule);
		hash = 67 * hash + Objects.hashCode(this.operator);
		return hash;
	}


	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final PredicateRule other = (PredicateRule) obj;
		if(!Objects.equals(this.conditions, other.conditions)) {
			return false;
		}
		if(!Objects.equals(this.rule, other.rule)) {
			return false;
		}
		if(!Objects.equals(this.operator, other.operator)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "PredicateRule{operator=" + operator + ", conditions=" + conditions + ", rule=" + rule + '}';
	}
	
}
