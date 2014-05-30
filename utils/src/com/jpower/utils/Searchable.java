/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.utils;

import java.util.List;

/**
 *
 * @author f6036477
 */
public interface Searchable {

  public void setSearchText(SearchText s);

  public SearchText getSearchText();

  public void setSearchEngine(SearchEngine eng);

  public SearchEngine engine();

  public List<String> records();

  public List<String> founds();

  public List<String> search();

  public String searchFirst(String s);

  public List<String> search(String s);

}
