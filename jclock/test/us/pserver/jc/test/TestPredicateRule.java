/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.test;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import us.pserver.jc.WakeRule;
import us.pserver.jc.rules.BooleanOperator;
import us.pserver.jc.rules.DatePredicate;
import us.pserver.jc.rules.PredicateRule;
import us.pserver.jc.rules.RuleBuilder;
import us.pserver.jc.rules.TimeAmountRule;
import us.pserver.jc.util.DateTime;



/**
 *
 * @author juno
 */
public class TestPredicateRule {
	
	
	public static void main(String[] args) {
		Optional<WakeRule> opt = RuleBuilder
				.ruleIn(1, ChronoUnit.DAYS)
				.compose()
				.condition(
						BooleanOperator.OR, 
						new DatePredicate(ChronoField.DAY_OF_WEEK, 2),
						new DatePredicate(ChronoField.DAY_OF_WEEK, 4),
						new DatePredicate(ChronoField.DAY_OF_WEEK, 6))
				.build();
		
		System.out.println(opt);
		System.out.println("* resolve: "+ DateTime.of(
				opt.get().resolve())
		);
		for(int i = 0; i < 4; i++) {
			if(opt.isPresent()) {
				opt = opt.get().next();
				System.out.println("* next --> "+ DateTime.of(
						opt.get().resolve())
				);
			}
		}
		
	}
	
}
