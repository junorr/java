package com.jpower.fxs.search;

import com.jpower.fxs.contants.Comparison;

/**
 * Predicado para comparação de objetos baseados em números.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.18
 */
public class NumberPredicate extends NumberComparison {
	
	private Number value;


  /**
   * Construtor padrão e sem argumentos.
   */
	public NumberPredicate() {
		super();
		value = null;
		value2 = null;
	}


  /**
   * Construtor que recebe o número que servirá
   * como parâmetro para as comparações realizadas 
   * por NumberPredicate.
   * @param n Número parâmetro para as comparações realizadas.
   */
	public NumberPredicate(Number n) {
		super();
		value = n;
	}


	@Override
	public NumberPredicate onParam(Object n) {
		if(n != null && n instanceof Number) value = (Number) n;
		return this;
	}


	@Override
	public boolean match(Object o) {
		if(o == null || !(o instanceof Number)
				|| value == null)
			return false;

		Number n = (Number) o;
		Number v2 = null;
		if(value2 != null && value2 instanceof Number)
			v2 = (Number) value2;

		boolean match = false;
		if(compare == Comparison.LESSER)
			match = Math.min(value.doubleValue(), n.doubleValue()) == n.doubleValue();
		else if(compare == Comparison.GREATER)
			match = Math.max(value.doubleValue(), n.doubleValue()) == n.doubleValue();
		else if(v2 != null && compare == Comparison.BETWEEN)
			match = Math.min(value.doubleValue(), n.doubleValue()) == value.doubleValue()
					&& Math.max(v2.doubleValue(), n.doubleValue()) == v2.doubleValue();
		else if(compare == Comparison.LIKE)
			match = like(value, n);
		else
			match = value.equals(n);

		return ifnot(match);
	}


	public static void main(String[] args) {
		NumberPredicate np = new NumberPredicate(5);
		boolean b = np.onParam(5).match(22);
		System.out.println("np.onParam(5).match(22): "+b);
		b = np.onParam(5).match(5);
		System.out.println("np.onParam(5).match(5): "+b);
		b = np.onParam(5).not().match(40);
		System.out.println("np.onParam(5).not().match(40): "+b);
		b = np.onParam(5).like().match(425);
		System.out.println("np.onParam(5).like().match(425): "+b);
		b = np.onParam(5).between(7).match(6);
		System.out.println("np.onParam(5).between(7).match(6): "+b);
		b = np.onParam(5).between(7).match(9);
		System.out.println("np.onParam(5).between(7).match(9): "+b);
		b = np.onParam(5).between(7).not().match(9);
		System.out.println("np.onParam(5).between(7).not().match(9): "+b);
	}

}
