package com.jpower.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Lista de pesquisa contendo todos os textos onde serão
 * realizadas as pesquisas.
 * @author f6036477
 */
public class SearchList implements Searchable {

  public List<String> records;

  private List<RField> fields;

  private SearchText search;

  private SearchEngine engine;


  private class RField {
		protected String result;
		protected double accuracy;
  }


	public SearchList() {
		records = new ArrayList<String>();
		fields = new ArrayList<RField>();
		engine = new SearchEngine();
		search = null;
	}


	public SearchList(List<String> source) {
		this();
		this.records = source;
	}


	public double getAccuracy(String s) {
		for(int i = 0; i < fields.size(); i++) {
			RField rf = fields.get(i);
			if(rf.result.equals(s))
				return rf.accuracy;
		}

		return -1;
	}


	public List<String> founds() {
		if(fields == null || fields.isEmpty())
			return null;

		Comparator<RField> comp = new Comparator<RField>() {
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


	public void setSearchText(SearchText text) {
		search = text;
	}


	public SearchText getSearchText() {
		return search;
	}


	public void setSearchEngine(SearchEngine eng) {
		engine = eng;
	}


	public SearchEngine engine() {
		return engine;
	}


	public List<String> records() {
		return records;
	}


	public List<String> search() {
		if(search == null
				|| engine == null
				|| records == null
				|| records.isEmpty())
			return null;

		fields.clear();
		engine.setSearch(search.prepare());

		SearchText src = null;

		for(String s : records) {
			src = SearchEngine.toSearch(s).prepare();
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


	public List<String> search(String s) {
	  this.setSearchText(new SearchText(s));
	  return this.search();
	}


	public String searchFirst(String s) {
	  this.setSearchText(new SearchText(s));
	  this.search();
	  List<String> r = this.founds();
	  if(r != null && !r.isEmpty())
		return r.get(0);
	  return null;
	}

}
