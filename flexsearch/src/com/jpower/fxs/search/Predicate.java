package com.jpower.fxs.search;

import com.jpower.fxs.contants.Comparison;
import com.jpower.reflect.Reflector;

/**
 * Predicado genérico para comparação de 
 * atributos internos de objetos. Comparação 
 * exclusiva através do método <code>equals(Objecto)</code>.
 * Utiliza reflection para extração dos atributos internos.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.04.01
 */
public class Predicate extends AbsPredicate {

	private Reflector rfc;
	
	private String method;
	
	private String field;
	
	private Object param;
	
	private Object value;
	
	
  /**
   * Construtor padrão e sem argumentos.
   */
	public Predicate() {
		rfc = new Reflector();
		param = null;
		value = null;
		method = null;
		field = null;
	}
	
	
  /**
   * Construtor que recebe o objeto parâmetro
   * para as comparações realizadas. Cabe apontar que
   * este objeto deve ser o próprio valor a ser comparado,
   * ou seja, não terá dados extraídos.
   * @param param Objeto parâmetro para as comparações realizadas.
   */
	public Predicate(Object param) {
		this();
		this.param = param;
	}
	
	
  /**
   * Define extração do atributo através de seu nome.
   * @param f Nome do atributo a ser extraído.
   * @return Esta instância de Predicate modificada.
   */
	public Predicate byField(String f) {
		field = f;
		method = null;
		return this;
	}
	
	
  /**
   * Define extração do atributo através de seu método de retorno.
   * @param f Nome do método de retorno do atributo a ser extraído.
   * @return Esta instância de Predicate modificada.
   */
	public Predicate byMethod(String m) {
		method = m;
		field = null;
		return this;
	}
	
	
	@Override
	public Predicate onParam(Object o) {
		param = o;
		return this;
	}
	
	
  /**
   * Estrai o valor a ser comparado.
   * @param o Objeto cujo atributo será extraído.
   */
	private void extract(Object o) {
		if(o == null) return;
		if(method != null)
			value = rfc.on(o).method(method, null).invoke(null);
		else
			value = rfc.on(o).field(field).get();
	}
	
	
	@Override
	public boolean match(Object o) {
		if(o == null) return false;
		if(method == null && field == null)
			return false;
		
		extract(o);
		if(value == null || param == null)
			return false;

		boolean ret = false;
		if(isLikeComparison())
			ret = like(param, value);
		else
			ret = param.equals(value);

		return ifnot(ret);
	}
	
	
	public static void main(String[] args) {
		class A {
			private String val;
			public A(String s) {
				val = s;
			}
			protected String get() {
				return val;
			}
			public void set(String s) {
				val = s;
			}
			public String toString() {
				return "A: "+val;
			}
		}
		
		A a = new A("aaa");
		String bbb = "bbb";
		String aa = "aa";

		System.out.println(a);
		
		Predicate p = new Predicate();
		
		boolean b = p.onParam(bbb).byField("val").not().match(a);
		
		System.out.println("p.onParam(bbb).byField(\"val\").not().match(a): "+b);
		
		b = p.onParam(bbb).byMethod("get").not().match(a);
		
		System.out.println("p.onParam(bbb).byMethod(\"get\").not().match(a): "+b);
		
		b = p.onParam(aa).byMethod("get").like().match(a);
		
		System.out.println("p.onParam(aa).byMethod(\"get\").like().match(a): "+b);
	}
	
}
