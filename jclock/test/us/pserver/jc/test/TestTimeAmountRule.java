/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.test;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import us.pserver.jc.WakeRule;
import us.pserver.jc.rules.DateTimeRule;
import us.pserver.jc.util.DateTime;
import us.pserver.jc.rules.TimeAmountRule;


/**
 *
 * @author juno
 */
public class TestTimeAmountRule {
	
	
	public static void main(String[] args) {
		DateTime dtm = DateTime.now();
    System.out.println("now....: "+ dtm);
		Optional<WakeRule> opt = Optional.of(new TimeAmountRule(
				new DateTimeRule(dtm), 1, ChronoUnit.DAYS)
    );
    System.out.println("rule...: "+ opt.get());
		System.out.println("resolve: "+ DateTime.of(opt.get().resolve()));
		opt = opt.get().next();
		System.out.println("next...: "+ opt.get());
		System.out.println("resolve: "+ DateTime.of(opt.get().resolve()));
		opt = opt.get().next();
		System.out.println("next...: "+ opt.get());
		System.out.println("resolve: "+ DateTime.of(opt.get().resolve()));
	}
	
}
