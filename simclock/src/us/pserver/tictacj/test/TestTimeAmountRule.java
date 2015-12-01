/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tictacj.test;

import java.time.temporal.ChronoUnit;
import us.pserver.tictacj.rules.DateTime;
import us.pserver.tictacj.rules.TimeAmountRule;


/**
 *
 * @author juno
 */
public class TestTimeAmountRule {
	
	
	public static void main(String[] args) {
		DateTime dtm = DateTime.now();
		TimeAmountRule rule = new TimeAmountRule(dtm, 60, ChronoUnit.SECONDS);
		System.out.println("now....: "+ dtm);
		dtm = DateTime.of(rule.resolve());
		System.out.println("resolve: "+ dtm);
		dtm = DateTime.of(rule.resolve());
		System.out.println("resolve: "+ dtm);
		dtm = DateTime.of(rule.reset().resolve());
		System.out.println("reset..: "+ dtm);
	}
	
}
