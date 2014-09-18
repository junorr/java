package com.jpower.fxs.search;

import com.db4o.query.Predicate;
import com.jpower.fxs.contants.Comparison;

/**
 * Classe abstrata de um Predicado para consulta de objetos. 
 * Herda de com.db4o.query.Predicate, mas pode ser 
 * utilizada sem o uso do banco de dados DB4O.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.04.01
 */
public abstract class AbsPredicate extends Predicate {

	protected Comparison compare;

	protected boolean not;


  /**
   * Construtor protegido padrão.
   */
	protected AbsPredicate() {
		compare = Comparison.EQUALS;
		not = false;
	}


  /**
   * Inverte o resultado de <code>match(Object)</code>.
   * @return Esta instância de AbsPredicate modificada.
   */
	public AbsPredicate not() {
		not = !not;
		return this;
	}


  /**
   * Retorna <code>true</code> se <code>not()</code>
   * tenha sido ativado.
   * @return <code>true</code> se <code>not()</code>
   * tenha sido ativado, <code>false</code> caso contrário.
   */
	public boolean isNotComparison() {
    return not;
	}


  /**
   * Retorna <code>true</code> se <code>like()</code>
   * tenha sido ativado, ou seja, para comparação,
   * os objetos são transformados em Strings, verificando
   * se v1 está contido em v2 ou vice-versa.
   * @return <code>true</code> se <code>like()</code>
   * tenha sido ativado, <code>false</code> caso contrário.
   */
	public boolean isLikeComparison() {
		return compare == Comparison.LIKE;
	}


  /**
   * Retorna <code>true</code> se o método de comparação for
   * <code>equals</code>.
   * @return <code>true</code> se o método de comparação for
   * <code>equals</code>, <code>false</code> caso contrário.
   */
	public boolean isEqualsComparison() {
		return compare == Comparison.EQUALS;
	}


  /**
   * Inverte o valor informado e retorna, caso 
   * <code>not()</code> esteja ativado.
   * @param b Valor a ser invertido se for o caso.
   * @return 
   */
	protected boolean ifnot(boolean b) {
		if(not) {
			return !b;
		}
		return b;
	}


  /**
   * Transforma os objetos em String e verifica se
   * v1 está contido em v2 ou vice-versa.
   * @param v1 Objeto 1.
   * @param v2 Objeto 2.
   * @return <code>true</code> se uma das duas
   * Strings está contida na outra.
   */
	protected boolean like(Object v1, Object v2) {
		if(v1 == null || v2 == null)
			return false;

		String s1 = v1.toString();
		String s2 = v2.toString();

		return s1.contains(s2) || s2.contains(s1);
	}


  /**
   * Define o método de comparação para <code>EQUALS</code>.
   * @return Esta instância de AbsPredicate modificada.
   */
	public AbsPredicate equals() {
		compare = Comparison.EQUALS;
		return this;
	}


  /**
   * Define o método de comparação para <code>LIKE</code>.
   * @return Esta instância de AbsPredicate modificada.
   */
	public AbsPredicate like() {
    if(compare == Comparison.LIKE)
      compare = Comparison.EQUALS;
    else compare = Comparison.LIKE;
		return this;
	}


  /**
   * Comara dois objetos e retorna <code>true</code>
   * se forem compatíveis.
   * @param o1 Primeiro objeto a ser comparado.
   * @param o2 Segundo objeto a ser comparado.
   * @return <code>true</code> caso os objetos sejam
   * compatíveis, false caso contrário.
   */
	protected boolean match(Object o1, Object o2) {
		if(o1 == null || o2 == null) return false;

		boolean match = false;
		if(isLikeComparison())
			match = like(o1, o2);
		else
			match = o1.equals(o2);

		return ifnot(match);
	}


  /**
   * Compara o objeto informado com as especificações
   * deste Predicate, retornando <code>true</code>
   * se forem compatíveis.
   * @param o Objeto a ser comparado.
   * @return <code>true</code> se o objeto for
   * compatível com as especificações deste 
   * Predicate, <code>false</code> caso contrário.
   */
	@Override
	public abstract boolean match(Object o);

  
  /**
   * Define o objeto que servirá de parâmetro
   * para as comparações realizadas por este Predicate.
   * @param o Objeto que servirá como parâmtro para as
   * comparações realizadas.
   * @return Esta instância de AbsPredicate modificada.
   */
	public abstract AbsPredicate onParam(Object o);
	
}
