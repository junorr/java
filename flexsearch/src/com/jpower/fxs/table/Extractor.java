/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.fxs.table;

import com.jpower.reflect.Reflector;

/**
 * Extractor mantém informações e manipula objetos
 * para extrair determinado atributo de classe
 * utilizando reflexão.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.11
 */
public class Extractor {

	private String field;

	private String method;

	private Object extracted;

	private Reflector rf;


	/**
	 * Construtor padrão e sem argumentos.
	 */
	public Extractor() {
		field = null;
		method = null;
		extracted = null;
		rf = new Reflector();
	}


	/**
	 * Configura o nome do atributo a ser obtido.
	 * @param f Nome do atributo.
	 * @return Esta instância de Extractor modificada.
	 */
	public Extractor byField(String f) {
		field = f;
		method = null;
		return this;
	}


	/**
	 * Configura o nome do metodo pelo qual será
	 * obtido o atributo..
	 * @param f Nome do metodo.
	 * @return Esta instância de Extractor modificada.
	 */
	public Extractor byMethod(String m) {
		method = m;
		field = null;
		return this;
	}


	/**
	 * Retorna <code>true</code> caso Extractor obterá
	 * o atributo através do nome do próprio atributo,
	 * <code>false</code> caso contrário.
	 */
	public boolean byField() {
		return field != null;
	}


	/**
	 * Retorna <code>true</code> caso Extractor obterá
	 * o atributo através da incovação de um método,
	 * <code>false</code> caso contrário.
	 */
	public boolean byMethod() {
		return method != null;
	}


	/**
	 * Retorna o nome do atributo a ser obtido.
	 * @return Nome do atributo.
	 */
	public String getField() {
		return field;
	}


	/**
	 * Retorna o nome do método a ser invocado para
	 * obtenção do atributo.
	 * @return Nome do método a ser invocado.
	 */
	public String getMethod() {
		return method;
	}


	/**
	 * Retorna o atributo extraído.
	 * @return Objeto extraído.
	 */
	public Object getExtracted() {
		return extracted;
	}


	/**
	 * Retorna a exceção caso tenha sido lançada
	 * durante a extração.
	 * @return Exceção lançada.
	 */
	public Throwable getError() {
		return rf.getError();
	}


	/**
	 * Extrai o atributo configurado do objeto informado.
	 * @param o Objeto do qual será extraído o atributo.
	 * @return o atributo extraído.
	 */
	public Object extract(Object o) {
		if(o == null) return null;
		if(field == null && method == null) return null;

		if(byField())
			return rf.on(o).field(field).get();
		else
			return rf.on(o).method(method, null).invoke(null);
	}

}
