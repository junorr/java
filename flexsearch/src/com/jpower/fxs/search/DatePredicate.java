package com.jpower.fxs.search;

import com.jpower.date.SimpleDate;
import com.jpower.fxs.contants.Comparison;
import java.util.Date;

/**
 * Predicado para comparação de Datas.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.18
 * @see com.jpower.fxs.search.NumberComparison;
 * @see com.jpower.fxs.search.AbsPredicate;
 */
public class DatePredicate extends NumberComparison {

	private Date value;


	@Override
	public DatePredicate onParam(Object d) {
		if(d != null && d instanceof Date) value = (Date) d;
		return this;
	}


	@Override
	public boolean match(Object o) {
		if(o == null || !(o instanceof Date)
				|| value == null)
			return false;

		SimpleDate val = new SimpleDate(value);
		SimpleDate sdo = new SimpleDate((Date)o);
		SimpleDate vl2 = null;
		if(value2 != null && value2 instanceof Date)
			vl2 = new SimpleDate((Date)value2);

		boolean match = false;
		if(compare == Comparison.LESSER)
			match = sdo.before(val);
		else if(compare == Comparison.GREATER)
			match = sdo.after(val);
		else if(vl2 != null && compare == Comparison.BETWEEN)
			match = sdo.after(val) && sdo.before(vl2);
		else if(compare == Comparison.LIKE)
			match = like(val, sdo);
		else
			match = val.equals(sdo);

		return ifnot(match);
	}


	public static void main(String[] args) {
		SimpleDate d3 = SimpleDate.parseDate("10/03/2011", SimpleDate.DDMMYYYY_SLASH);
		SimpleDate d4 = SimpleDate.parseDate("10/04/2011", SimpleDate.DDMMYYYY_SLASH);
		SimpleDate d42 = SimpleDate.parseDate("10/04/2011", SimpleDate.DDMMYYYY_SLASH);
		SimpleDate d5 = SimpleDate.parseDate("10/05/2011", SimpleDate.DDMMYYYY_SLASH);

		System.out.println("d3: " + d3);
		System.out.println("d4: " + d4);
		System.out.println("d42: " + d42);
		System.out.println("d5: " + d5);

		DatePredicate dp = new DatePredicate();

		boolean b = false;
		b = dp.onParam(d4).match(d42);
		System.out.println("dp.onParam(d4).match(d42): " + b);

		b = dp.onParam(d3).between(d5).match(d4);
		System.out.println("dp.onParam(d3).between(d5).match(d4): " + b);

		b = dp.onParam(d3).between(d42).match(d5);
		System.out.println("dp.onParam(d3).between(d42).match(d5): " + b);

		b = dp.onParam(d3).between(d42).not().match(d5);
		System.out.println("dp.onParam(d3).between(d42).not().match(d5): " + b);

		b = dp.onParam(d42).greater().match(d5);
		System.out.println("dp.onParam(d42).greater().match(d5): " + b);

		b = dp.onParam(d42).lesser().match(d5);
		System.out.println("dp.onParam(d42).lesser().match(d5): " + b);

		b = dp.onParam(d42).lesser().not().match(d5);
		System.out.println("dp.onParam(d42).lesser().not().match(d5): " + b);
	}

}
