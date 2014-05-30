package com.jpower.utils;

import java.util.Scanner;

/**
 * Motor de pesquisa de textos. Trabalha com um sistema de
 * indicador de precisão, que deve ser definido antes da
 * busca.<br>
 * O método <code>search(): double</code> calcula e
 * retorna a precisão média das palavras encontradas, com
 * precisão maior ou igual à precisão (accuracy) setada no motor.<br>
 * Dessa forma, qualquer palavra com precisão menor que
 * a precisão do motor, não será considerada, e
 * consequentemente não encontrada.<br>
 * Por exemplo: Supondo que deseja-se procurar a
 * palavra <i>"projet"</i> (digitada parcialmente) dentro do texto
 * <i>"Projeto Alpha Centurion"</i>, buscando resultados com
 * precisão maior ou igual a <b>80%</b>.<br><br>
 * <code>
 * SearchEngine eng = new SearchEngine();<br>
 * eng.setAccuracy(SearchEngine.ACCURACY_80);<br>
 * //eng.setAccuracy(0.8);<br>
 * eng.setSource(SearchEngine.toSearch("Projeto Alpha Centurion"));<br>
 * eng.setSearch(SearchEngine.toSearch("projet"));<br>
 * double precisao = eng.search();<br>
 * //precisao = eng.getLastResult();<br>
 * </code><br>
 * O resultado (precisão média obtida na pesquisa) seria <b>1.0 (100%)</b>,
 * porque o trecho de texto <i>"projet"</i> está contido com 100% de
 * igualdade no texto original.<br>
 * Por outro lado, se a palavra a ser pesquisada fosse
 * <i>"proeto"</i> (digitada errada) e a precisão continuasse em 80%,
 * o valor retornado seria <b>0.0</b>, ou seja não encontrada. Isso
 * porque <i>"proeto"</i>, está contida com <b>70%</b> de igualdade no
 * texto original, <u>que é menor que a precisão setada no motor</u>.
 *
 * @author f6036477
 */
public class SearchEngine {


	/**
	 * Nível de precisão do motor de pesquisa.
	 */
	public static final double
			ACCURACY_10 = 0.1,
			ACCURACY_20 = 0.2,
			ACCURACY_30 = 0.3,
			ACCURACY_40 = 0.4,
			ACCURACY_50 = 0.5,
			ACCURACY_60 = 0.6,
			ACCURACY_70 = 0.7,
			ACCURACY_80 = 0.8,
			ACCURACY_90 = 0.9,
			ACCURACY_100 = 1,
			ACCURACY_5 = 0.05;

	private static final double
			MATCH_POINT_GREAT = 1.0,
			MATCH_POINT_GOOD = 0.5,
			MATCH_POINT_SMOOTH = 0.2;


	private double accuracy, result;

	private int count;

	private SearchText source;

	private SearchText search;


	/**
	 * Construtor padrão e sem argumentos.
	 * Define a precisão do motor em 70%
	 * (<code>setAccuracy(0.7)</code>).
	 */
	public SearchEngine() {
		accuracy = ACCURACY_80;
		count = 0;
		result = 0;
	}


	/**
	 * Construtor que recebe o texto onde serao feitas as pesquisas.
	 * @param source Texto no qual será realizada a pesquisa.
	 */
	public SearchEngine(SearchText source) {
		this();
		this.source = source;
	}


	/**
	 * Retorna a precisão setada para o motor.
	 * @return precisão do motor de busca.
	 */
	public double accuracy() { return accuracy; }

	/**
	 * Seta a precisão do motor de busca.
	 * @param acc precisão do motor de busca.
	 */
	public void setAccuracy(double acc) {
		if(acc < 0 || acc > ACCURACY_100) return;
		accuracy = acc;
	}


	/**
	 * Retorna o texto a ser pesquisado.
	 * @return texto a ser pesquisado.
	 */
	public SearchText getSearch() { return search; }

	/**
	 * Seta o texto a ser pesquisado.
	 * @param search texto a ser pesquisado.
	 */
	public void setSearch(SearchText search) {
		this.search = search;
	}


	/**
	 * Retorna o texto no qual será executada a pesquisa.
	 * @return texto no qual será executada a pesquisa.
	 */
	public SearchText source() { return source; }

	/**
	 * Seta o texto no qual será executada a pesquisa.
	 * @param source texto no qual será executada a pesquisa.
	 */
	public void setSource(SearchText source) {
		this.source = source;
	}


	/**
	 * Pesquisa o texto "search" em "source", retornando a média
	 * de precisão das ocorrências encontradas (com resultado maior
	 * do que a precisão do motor).
	 * @return média de precisão das ocorrências encontradas.
	 */
	public double search() {
		if(source == null || search == null) return -1;
		if(source.text() == null || search.text() == null) return -1;

		source.prepare();
		search.prepare();

		String[] words = search.text().split(" ");
		count = 0;
		String[] comp;
		double totacc = 0;

		for(String w : words) {

			for(int i = 0; i < source.text().length() - w.length(); i++) {
				comp = selectToCompare(i, source.text(), w);
				double acc = compareChars(comp[0], comp[1]);
				if(acc >= accuracy) {
					totacc += acc;
					count++;
				}
			}//for
		}

		//System.out.println("med  : "+(totacc / count));
		//System.out.println("count: "+count);
		result = totacc / count;
		return result;
	}


	/**
	 * Define o trecho do texto original a ser comparado, a partir de index.
	 * @return Array String com duas posições (index 0: source, index 1: search).
	 */
	private String[] selectToCompare(int index, String src, String sch) {
		if(src == null || sch == null || index >= src.length() || index < 0)
			return null;

		String[] s = new String[2];
		s[0] = src.substring(index);
		s[1] = sch;

		if(sch.length() > src.length())
			s[1] = sch.substring(0, src.length());
		else if(sch.length() + 2 <= src.length()) {
			s[0] = src.substring(index, (index + sch.length() + 2 <= src.length() ? index + sch.length() + 2 : src.length()));
		} else if (sch.length() + 1 <= src.length()) {
			s[0] = src.substring(index, (index + sch.length() + 1 <= src.length() ? index + sch.length() + 1 : src.length()));
		}

		return s;
	}


	/**
	 * Compara duas porções de texto, retornando a precisão de igualdade dos dois.
	 * @return precisão de igualdade dos textos.
	 */
	private double compareChars(String src, String sch) {
		if(src == null || sch == null)
			return 0;
		char s, h;
		double cnt = 0, add = 0, acc = 0;

		for(int i = 0; i < src.length(); i++) {
			cnt += add;
			s = src.charAt(i);
			h = sch.charAt(i);

			if(s == h) {
				cnt++;
				add = 0;
			} else if (h == src.charAt((i + 1 < src.length() ? i + 1 : i))) {
				add = MATCH_POINT_GOOD;
			} else if (h == src.charAt((i + 2 < src.length() ? i + 2 : i))) {
				add = MATCH_POINT_SMOOTH;
			} //else add = -0.2;

			if(i+1 >= sch.length()) break;
		}//for
		if(cnt % (int)cnt >= 0.5) add = MATCH_POINT_GREAT;
		else add = 0;
		return (cnt + add) / sch.length();
	}


	/**
	 * Retorna o último resultado de pesquisa obtido.
	 */
	public double getLastResult() { return result; }


	/**
	 * Retorna <b><code>true</code></b> caso a média de precisão
	 * das ocorrências encontradas seja maior ou igual à precisão
	 * setada no motor.
	 */
	public boolean match() {
		return search() >= accuracy;
	}


	/**
	 * Método utilitário para retornar um objeto <code>SearchText</code>
	 * a partir do texto dado.
	 */
	public static SearchText toSearch(String string) {
		return new SearchText(string);
	}


	public static void main(String[] args) {
		String source =
				  "  Desde  o  lançamento   da   sua   primeira   versão   em   1998,\n"
				+ "  o KDE passou  por  diversos  melhoramentos.  A   versão   4.5.1,\n"
				+ "  distribuída com o Kubuntu 10.10 impressiona em termos de visual,\n"
				+ "  funcionalidade e estabilidade  mesmo  àqueles   que   criticaram\n"
				+ "  o projeto quando do lançamento do KDE 4. Confira  neste   artigo\n"
				+ "  como você pode personalizar a aparência do seu ambiente  gráfico\n"
				+ "  e sugestões para um elegante visualt.";

		String search = null;
		Scanner sc = new Scanner(System.in);

		System.out.println("SOURCE: ");
		System.out.println(source);
		System.out.print("\nDigite o texto a pesquisar: ");
		search = sc.nextLine();

		SearchEngine eng = new SearchEngine();
		eng.setSource(SearchEngine.toSearch(source));
		eng.setSearch(SearchEngine.toSearch(search));

		System.out.println("engine.search();");
		System.out.println("\nSearch Accuracy: ");
		System.out.println(eng.search());

		String word =  "projetos";
		String word1 = "projet";
		String word2 = "proeto";
		String word3 = "pogeto";
		String word4 = "poeta";

		String[] s = eng.selectToCompare(0, word, word1);
		System.out.println("eng.selectToCompare(0, " + word + ", " + word1 + ") = [ " + s[0] + ", " + s[1] + " ]");
		System.out.println("eng.compareChars('"+s[0]+"', '"+s[1]+"') = " + eng.compareChars(s[0], s[1]));
		s = eng.selectToCompare(0, word, word2);
		System.out.println("eng.selectToCompare(0, " + word + ", " + word2 + ") = [ " + s[0] + ", " + s[1] + " ]");
		System.out.println("eng.compareChars('"+s[0]+"', '"+s[1]+"') = " + eng.compareChars(s[0], s[1]));
		s = eng.selectToCompare(0, word, word3);
		System.out.println("eng.selectToCompare(0, " + word + ", " + word3 + ") = [ " + s[0] + ", " + s[1] + " ]");
		System.out.println("eng.compareChars('"+s[0]+"', '"+s[1]+"') = " + eng.compareChars(s[0], s[1]));
		s = eng.selectToCompare(0, word, word4);
		System.out.println("eng.selectToCompare(0, " + word + ", " + word4 + ") = [ " + s[0] + ", " + s[1] + " ]");
		System.out.println("eng.compareChars('"+s[0]+"', '"+s[1]+"') = " + eng.compareChars(s[0], s[1]));
	}

}
