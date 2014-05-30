package com.jpower.fxs.table;

import java.util.LinkedList;
import java.util.List;

/**
 * ContentHandler armazena informações para
 * extração de atributos de objetos em nível virtualmente
 * infinito de profundidade, utilizando reflection.
 * ContentHandler utiliza uma lista encadeada de
 * Extractors, que são invocados sucessivamente sobre
 * os resultados, de forma a extrair a informação
 * desejada.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.11
 */
public class ContentHandler {

	private List<Extractor> extractors;

	private Object extracted;


	/**
	 * Construtor padrão e sem argumentos
	 */
	public ContentHandler() {
		extractors = new LinkedList<Extractor>();
		extracted = null;
	}


	/**
	 * Retorna a lista de Extractos utilizada
	 * na obtenção do atributo.
	 * @return <code>List<Extractor></code>.
	 */
	public List<Extractor> extractors() {
		return extractors;
	}


	/**
	 * Adiciona um Extractor à ContentHandler
	 * @param ext Extractor a ser adicionado.
	 * @return Esta instância de ContentHandler modificada.
	 */
	public ContentHandler add(Extractor ext) {
		extractors.add(ext);
		return this;
	}


	/**
	 * Retorna o último objeto extraído.
	 * @return o atributo extraído.
	 */
	public Object getExtracted() {
		return extracted;
	}


	/**
	 * Inicia a extração incremental, retornando
	 * o atributo desejado do objeto informado
	 * como parâmetro, ou <code>null</code>
	 * caso ocorra algum erro.
	 * @param o Objeto do qual será extraído o atributo.
	 * @return Objeto extraído.
	 */
	public Object extract(Object o) {
		if(o == null || extractors.isEmpty())
			return null;

		extracted = o;
		for(Extractor ex : extractors)
			extracted = ex.extract(extracted);

		return extracted;
	}


	public static void main(String[] args) {
		class A {
			int value = 5;
		}

		class B {
			A a = new A();
		}

		class C {
			B b = new B();
		}

		class D {
			C c = new C();
		}


		ContentHandler cm = new ContentHandler();
		cm.add(new Extractor().byField("c"))
				.add(new Extractor().byField("b"))
				.add(new Extractor().byField("a"))
				.add(new Extractor().byField("value"));
		
		System.out.println("cm.extract(new D()): " + cm.extract(new D()));
	}

}
