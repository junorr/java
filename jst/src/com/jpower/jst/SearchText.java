/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package com.jpower.jst;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p style="font-size: medium;">
 * Classe que encapsula um texto utilizado no motor de busca
 * <code>SearchEngine</code>.
 * </p>
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.11.25
 */
@Version (
		value			= "1.0",
    name      = "SearchText",
		date			= "2011.11.25",
		author		= "Juno Roesler",
		synopsis	= "Encapsula textos para SearchEngine"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class SearchText {


	/**
	 * Letras com acentuação a serem substituídas no texto.
	 */
	public static final String[] EXCLUDE =
			{ "ã", "â", "á", "à", "é", "ê", "í", "î",  
        "õ", "ô", "ó", "û", "ú", "ü", "ç" };

	/**
	 * Letras sem acentuação.
	 */
	public static final String[] INCLUDE =
			{ "a", "a", "a", "a", "e", "e", "i", "i",  
        "o", "o", "o", "u", "u", "u", "c" };

	/**
	 * Conjunto de preposições e conjunções a serem removidas do texto para execução da pesquisa.
	 */
	public static final String[] COMMONS =
			{ " o "	  , " a "  , " e "  , " ou "  ,
				" de "	, " da " , " do " , " em "  , " como ",
				" para ", " pra ", " por ", " pois ", " um "  ,
				" ate " , " com ", " na "	, " no "  , " uma " ,
				" que " , " me " , " mas ", " ora " , " pois " };

	/**
	 * Conjunto de preposições e conjunções a serem removidas do texto para execução da pesquisa.
	 */
	public static final String[] COMMONS_START =
			{ "o "	 , "a "  ,	"e " , "ou "  ,
				"de "	 , "da " , "do " , "em "	, "como ",
				"para ", "pra ", "por ", "pois ", "um "  ,
				"ate " , "com ", "na " , "no "  , "uma " ,
				"que " , "me " , "mas ", "ora " , "pois " };

	/**
	 * Conjunto de preposições e conjunções a serem removidas do texto para execução da pesquisa.
	 */
	public static final String[] COMMONS_END =
			{ " o"	 , " a"  , " e"	 , " ou"	,
				" de"	 , " da" , " do" , " em"	, " como",
				" para", " pra", " por", " pois", " um"  ,
				" ate" , " com", " na" , " no"  , " uma" ,
				" que" , " me" , " mas", " ora" , " pois" };


	private List<String> commons, commonsEnd, commonsStart;

	private String orig;
	
	protected String text;


	/**
	 * Construtor padrão sem argumentos.
	 */
	public SearchText() {
		commons = new LinkedList<String>();
		commonsEnd = new LinkedList<String>();
		commonsStart = new LinkedList<String>();
		commons.addAll(Arrays.asList(COMMONS));
		commonsEnd.addAll(Arrays.asList(COMMONS_END));
		commonsStart.addAll(Arrays.asList(COMMONS_START));
		orig = null;
		text = null;
	}


	/**
	 * Construtor que recebe o texto a ser encapsulado e preparado para a busca.
	 * @param text
	 */
	public SearchText(String text) {
		this();
		this.orig = text;
		this.text = orig.toLowerCase();
	}


	/**
	 * Retorna a lista de palavras a serem removidas do texto.
	 * Pode ser modificada, porém não deve conter palavras acentuadas.
	 */
	public List<String> commons() { return commons; }

	/**
	 * Retorna a lista de palavras a serem removidas do FINAL do texto.
	 * Pode ser modificada, porém não deve conter palavras acentuadas.
	 */
	public List<String> commonsEnd() { return commonsEnd; }

	/**
	 * Retorna a lista de palavras a serem removidas do INICIO do texto.
	 * Pode ser modificada, porém não deve conter palavras acentuadas.
	 */
	public List<String> commonsStart() { return commonsStart; }


	/**
	 * Retorna o texto original encapsulado.
	 * @return
	 */
	public String string() { return orig; }

	/**
	 * Retorna o texto modificado para a pesquisa.
	 * @return
	 */
	public String text() { return text; }


	/**
	 * Seta o texto a ser encapsulado e preparado para a pesquisa.
	 * @param t
	 */
	public void setText(String t) {
		orig = t;
		text = orig.toLowerCase();
	}


	/**
	 * Exclui as palavras comuns (preposições e conjunções) do texto.
	 */
	private String excludeCommons(String s) {
		if(s == null || s.length() < 3) return s;

		s = s.toLowerCase();
		int end = 0, len = 0, tlen;
		String temp = null, ns = null;
		tlen = s.length();

		for(String c : commons) {
			ns = s.replaceAll(c, " ");
			s = ns;
		}//for

		s = s.replaceAll("  ", " ");

		return s;
	}


	/**
	 * Substitui as letras acentuadas.
	 */
	private String excludeAccents(String s) {
		String ns = s.toLowerCase();
		for(int i = 0; i < EXCLUDE.length; i++) {
			ns = ns.replaceAll(EXCLUDE[i], INCLUDE[i]);
		}//for
		return ns;
	}


	/**
	 * Exclui as palavras comuns (preposições e conjunções) do começo do texto.
	 */
	private String excludeStartCommons(String s) {
		String ns = s.toLowerCase();
		for(String c : commonsStart) {
			if(ns.startsWith(c))
				ns = ns.substring(c.length());
		}
		return ns;
	}


	/**
	 * Exclui as palavras comuns (preposições e conjunções) do final do texto.
	 */
	private String excludeEndCommons(String s) {
		String ns = s.toLowerCase();
		for(String c : commonsEnd) {
			if(ns.endsWith(c))
				ns = ns.substring(0, ns.length()-c.length());
		}
		return ns;
	}


	/**
	 * Prepara o texto para a pesquisa, realizando as exclusões e substituições necessarias.
	 * @return auto referência.
	 */
	public SearchText normalize() {
		if(text == null) return this;

		text = excludeAccents(orig);
		text = excludeCommons(text);
		text = excludeStartCommons(text);
		text = excludeEndCommons(text);

		return this;
	}


	public static void main(String[] args) {

		SearchText s = new SearchText();
		String string = "O Rato Que Levou uma pancada é um idiota, pois";
		System.out.println("string: " + string);
		System.out.println("s.excludeAccents(string)");
		string = s.excludeAccents(string);
		System.out.println("s.excludeCommons(string)");
		string = s.excludeCommons(string);
		System.out.println("s.excludeStartCommons(string)");
		string = s.excludeStartCommons(string);
		System.out.println("s.excludeEndCommons(string)");
		string = s.excludeEndCommons(string);
		System.out.println("string: " + string);
	}

}
