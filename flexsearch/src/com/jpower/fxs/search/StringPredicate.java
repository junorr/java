package com.jpower.fxs.search;

/**
 * Predicado para comparação de Strings.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.18
 */
public class StringPredicate extends AbsPredicate {

	private String value;


  /**
   * Construtor padrão e sem argumentos.
   */
	public StringPredicate() {
		super();
		value = null;
	}


  /**
   * Construtor que recebe a String parâmetro
   * para as comparações realizadas por StringPredicate.
   * @param s String parêmetro das comparações realizadas.
   */
	public StringPredicate(String s) {
		super();
		value = s;
	}


	@Override
	public StringPredicate onParam(Object o) {
		if(o != null) value = o.toString();
		return this;
	}


	@Override
	public boolean match(Object o) {
		return match(value, o);
	}


	public static void main(String[] args) {
		StringPredicate sp = new StringPredicate();

		boolean b = sp.onParam("Predicate").match("Predicate");
		System.out.println("sp.onParam(\"Predicate\").match(\"Predicate\"): "+b);

		b = sp.onParam("Predicate").like().match("Pre");
		System.out.println("sp.onParam(\"Predicate\").like().match(\"Pre\"): "+b);

		b = sp.onParam("Predicate").match("Pre");
		System.out.println("sp.onParam(\"Predicate\").match(\"Pre\"): "+b);

		b = sp.onParam("Predicate").not().match("AbsPredicate");
		System.out.println("sp.onParam(\"Predicate\").not().match(\"AbsPredicate\"): "+b);
	}

}
