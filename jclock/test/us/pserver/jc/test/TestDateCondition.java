/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.jc.test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import us.pserver.jc.rules.DatePredicate;
import us.pserver.jc.util.DateTime;



/**
 *
 * @author juno
 */
public class TestDateCondition {
	
	
	public static void main(String[] args) {
		DatePredicate dc = new DatePredicate(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.getValue());
		DateTime dt = DateTime.of(LocalDateTime.of(2016, 1, 24, 12, 0));
		//DateTime dt = DateTime.now();
		System.out.println(dt);
		System.out.println("* dt.dayOfWeek  : "+ dt.toLocalDT().get(ChronoField.DAY_OF_WEEK));
		System.out.println("* dt.monthOfYear: "+ dt.toLocalDT().get(ChronoField.MONTH_OF_YEAR));
		System.out.println("* test: "+ dc.test(dt.toLocalDT()));
	}
	
}
