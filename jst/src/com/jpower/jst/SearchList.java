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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <p style="font-size: medium;">
 * Lista de pesquisa contendo os textos
 * da base de pesquisa.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.11.25
 */
@Version (
		value			= "1.0",
    name      = "SearchList",
		date			= "2011.11.25",
		author		= "Juno Roesler",
		synopsis	= "Lista de textos da base de pesquisa"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class SearchList implements Searchable {

  public List<String> records;

  private List<RField> fields;

  private SearchText search;

  private SearchEngine engine;


  private class RField {
		protected String result;
		protected double accuracy;
  }


  /**
   * Construtor padrão sem argumentos, cria uma lista 
   * vazia, sem os dados base da pesquisa.
   */
	public SearchList() {
		records = new ArrayList<String>();
		fields = new ArrayList<RField>();
		engine = new SearchEngine();
		search = null;
	}


  /**
   * Construtor que recebe uma lista contendo
   * as Strings da base de pesquisa.
   * @param source 
   */
	public SearchList(List<String> source) {
		this();
		this.records = source;
	}
  
  
  /**
   * Define a base de pesquisa.
   * @param s Lista contendo as String da base de pesquisa.
   */
  @Override
  public void setSource(List<String> s) {
    if(s == null) records.clear();
    else this.records = s;
  }
  
  
  /**
   * Retorna uma lista contendo as Strings da base de pesquisa.
   * @return Lista de Strings.
   */
  @Override
  public List<String> getSource() {
    return records;
  }


	public double getAccuracy(String s) {
		for(int i = 0; i < fields.size(); i++) {
			RField rf = fields.get(i);
			if(rf.result.equals(s))
				return rf.accuracy;
		}

		return -1;
	}


  /**
   * Retorna uma lista com os resultados encontrados
   * da pesquisa anterior.
   * @return Lista de resultados encontrados.
   */
	public List<String> founds() {
		if(fields == null || fields.isEmpty())
			return null;

		Comparator<RField> comp = new Comparator<RField>() {
      @Override
			public int compare(RField o1, RField o2) {
				if(o1 == null || o2 == null) return -9;
				if(o1.accuracy < o2.accuracy) return 1;
				else if(o1.accuracy > o2.accuracy) return -1;
				else return 0;
			}
		};

		RField[] fs = new RField[fields.size()];
		fs = fields.toArray(fs);
		Arrays.sort(fs, comp);

		List<String> result = new ArrayList<String>(fields.size());
		for(RField f : fs)
			result.add(f.result);

		return result;
	}


  /**
   * Define o texto a ser pesquisado.
   * @param text Testo a ser pesquisado.
   */
	public void setText(String text) {
    if(text != null)
      search = new SearchText(text);
	}


  /**
   * Retorna o texto a ser pesquisado.
   * @return Texot a ser pesquisado na base.
   */
	public String getText() {
		return (search != null ? search.string() : null);
	}


  /**
   * Define o motor de pesquisa utilizado.
   * @param eng Motor de pesquisa a ser utilizado.
   */
  @Override
	public void setEngine(SearchEngine eng) {
		engine = eng;
	}


  /**
   * Retorna o motor de pesquisa utilizado.
   * @return motor de pesquisa utilizado.
   */
  @Override
	public SearchEngine getEngine() {
		return engine;
	}


  /**
   * Pesquisa na base o texto definido, retornando
   * uma lista de resultados.
   * @return Lista contendo os resultados encontrados.
   */
	public List<String> search() {
		if(search == null
				|| engine == null
				|| records == null
				|| records.isEmpty())
			return null;

		fields.clear();
		engine.setText(search.normalize());

		SearchText src = null;

		for(String s : records) {
			src = SearchEngine.toSearch(s).normalize();
			engine.setSource(src);
			if(engine.match()) {
				RField rf = new RField();
				rf.accuracy = engine.getLastResult();
				rf.result = s;
				fields.add(rf);
			}
		}

		return founds();
	}


  /**
   * Pesquisa na base a String informada, retornando
   * uma Lista contendo os resultados encontrados.
   * @param s String a ser pesquisada na base.
   * @return Lista com os resultados encontrados.
   */
  @Override
	public List<String> search(String s) {
	  this.setText(s);
	  return this.search();
	}


  /**
   * Pesquisa na base a String informada,
   * retornando o primeiro resultado encontrado.
   * @param s String a ser pesquisada na base.
   * @return O primeiro resultado encontrado.
   */
  @Override
	public String searchFirst(String s) {
	  this.setText(s);
	  this.search();
	  List<String> r = this.founds();
	  if(r != null && !r.isEmpty())
		return r.get(0);
	  return null;
	}

}
