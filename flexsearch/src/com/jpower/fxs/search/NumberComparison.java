package com.jpower.fxs.search;

import com.jpower.fxs.contants.Comparison;

/**
 * Superclasse de Predicado para comparação
 * de objetos baseados em números.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.18
 */
public abstract class NumberComparison extends AbsPredicate {

	protected Object value2;


  /**
   * Define o método de comparação para <code>LESSER</code>,
   * ou seja, retornará <code>true</code> se o objeto
   * PARÂMETRO INTERNO de NumberComparison for MENOR
   * que o argumento informado em <code>match(Object)</code>.
   * @return Esta instância de NumberComparison modificada.
   */
	public NumberComparison lesser() {
		if(compare == Comparison.LESSER)
      compare = Comparison.EQUALS;
    else
      compare = Comparison.LESSER;
		return this;
	}


  /**
   * Retorna <code>true</code> se o método de comparação 
   * de Number comparison for <code>LESSER</code>,
   * ou seja, o método <code>match(Object)</code> retornará 
   * <code>true</code> se o objeto PARÂMETRO INTERNO 
   * de NumberComparison for MENOR que o argumento informado
   * em <code>match(Object)</code>.
   * @return <code>true</code> se o método de comparação 
   * de Number comparison for <code>LESSER</code>.
   */
	public boolean isLesserComparison() {
		return compare == Comparison.LESSER;
	}


  /**
   * Define o método de comparação para <code>GREATER</code>,
   * ou seja, retornará <code>true</code> se o objeto
   * PARÂMETRO INTERNO de NumberComparison for MAIOR
   * que o argumento informado em <code>match(Object)</code>.
   * @return Esta instância de NumberComparison modificada.
   */
	public NumberComparison greater() {
		if(compare == Comparison.GREATER)
      compare = Comparison.EQUALS;
    else
      compare = Comparison.GREATER;
		return this;
	}


  /**
   * Retorna <code>true</code> se o método de comparação 
   * de Number comparison for <code>GREATER</code>,
   * ou seja, o método <code>match(Object)</code> retornará 
   * <code>true</code> se o objeto PARÂMETRO INTERNO 
   * de NumberComparison for MAIOR que o argumento informado
   * em <code>match(Object)</code>.
   * @return <code>true</code> se o método de comparação 
   * de Number comparison for <code>GREATER</code>.
   */
	public boolean isGreaterComparison() {
		return compare == Comparison.GREATER;
	}


  /**
   * Define o método de comparação para <code>BETWEEN</code>,
   * ou seja, o método <code>match(Object)</code> 
   * retornará <code>true</code> se o objeto
   * PARÂMETRO INTERNO de NumberComparison estiver ENTRE
   * o parâmetro interno de NumberComparison e o objeto 
   * <code>v2</code> informado.
   * @return Esta instância de NumberComparison modificada.
   */
	public NumberComparison between(Object v2) {
		value2 = v2;
		compare = Comparison.BETWEEN;
		return this;
	}


  /**
   * Retorna <code>true</code> se o método de comparação 
   * de Number comparison for <code>BETWEEN</code>,
   * ou seja, o método <code>match(Object)</code> 
   * retornará <code>true</code> se o objeto
   * PARÂMETRO INTERNO de NumberComparison estiver ENTRE
   * o parâmetro interno de NumberComparison e o objeto 
   * <code>v2</code> informado.
   * @return <code>true</code> se o método de comparação 
   * de Number comparison for <code>BETWEEN</code>.
   */
	public boolean isBetweenComparison() {
		return compare == Comparison.BETWEEN;
	}

}
