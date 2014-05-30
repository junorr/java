/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
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

package com.jpower.net.http;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 19/07/2013
 */
public class HtmlTag extends HeaderElement {
  
  public static final String OPEN1 = "<";

  public static final String OPEN2 = "</";

  public static final String CLOSE1 = ">";

  public static final String CLOSE2 = "/>";

  private String tag;
  
  private LinkedList<TagParam> params;
  
  private HtmlTag contTag;
  
  private String contText;
  
  private String parseString;
  
  private boolean closingTag;
  
  private boolean closed;
  
  
  public HtmlTag(String parse) {
    super(parse);
    params = new LinkedList<>();
    contTag = null;
    contText = null;
    tag = null;
    parseString = parse;
    closingTag = false;
    closed = false;
    
    if(parse != null && parse.startsWith(OPEN1)) { 
      closingTag = parse.startsWith(OPEN2);
      tag = "";
      boolean oparam = false;
      boolean tagend = false;
      String param = "";
      char[] cs = parse.toCharArray();
      char ac = 0;
      char c = 0;
      
      for(int i = 0; i < cs.length; i++) {
        ac = c;
        c = cs[i];
        if(c == '<' || c == '/')
          continue;
        if(c == '>') {
          closed = ac == '/';
          if(parse.length() > i+1)
            parseString = parse.substring(i+1);
          break;
        }
        
        if(!tagend && c != ' ') tag += c;
        else if(!tagend && tag.length() > 1) 
          tagend = true;
        else if(tagend) {
          if(c == '\'' || c == '\"') oparam = !oparam;
          if(c != ' ' || oparam) param += c;
          else if(c == ' ' && !oparam) {
            params.add(new TagParam(param));
            param = "";
          }//elseif
        }//elseif
      }//for
      
      if(tag != null) setName(tag);
      
      if(param.length() > 1) {
        params.add(new TagParam(param));
      }
      
      if(!parseString.equals(parse)) {
        if(parseString.startsWith(OPEN1)) {
          contTag = new HtmlTag(parseString);
        } else if(parseString.contains(OPEN1)) {
          contText = parseString.substring(0, 
              parseString.indexOf(OPEN1));
          parseString = parseString.substring(
              parseString.indexOf(OPEN1));
          contTag = new HtmlTag(parseString);
        } else {
          contText = parseString;
          parseString = null;
        }
      }
    }//if
    else if(parse.contains(OPEN1)) {
      contText = parse.substring(0, parse.indexOf(OPEN1));
      parseString = parse.substring(parse.indexOf(OPEN1) +1);
      HtmlTag t = new HtmlTag(parseString);
      if(t.getTag() == null) contText = parseString;
      else contTag = t;
    }
  }
  
  
  public String getParseString() {
    return parseString;
  }
  
  
  public List<TagParam> getParams() {
    return params;
  }
  
  
  public HtmlTag getNestedTag() {
    return contTag;
  }
  
  
  public String getContentText() {
    return contText;
  }
  
  
  public void setContentText(String text) {
    contText = text;
  }
  
  
  public String getTag() {
    return tag;
  }
  
  
  public boolean isClosingTag() {
    return closingTag;
  }
  
  
  public boolean isClosed() {
    return closed;
  }
  
  
  @Override
  public String toString() {
    if(tag == null) return null;
    String s = (closingTag ? OPEN2 : OPEN1);
    s += tag;
    for(TagParam p : params) {
      if(p != null)
        s += " " + p.toString();
    }
    if(closed) s += CLOSE2;
    else s += CLOSE1;
    if(contText != null) s += contText;
    if(contTag != null) s += contTag.toString();
    return s;
  }
  
  
  public static void main(String[] args) {
    String html = "<div width='100%' style='color: gray;'>"
        + "<span>Hello</span>"
        + "</div>";
    HtmlTag tag = new HtmlTag(html);
    System.out.println("* tag: "+ tag.getTag());
    List<TagParam> ps = tag.getParams();
    for(int i = 0; i < ps.size(); i++) {
      System.out.println("  - "+ ps.get(i));
    }
    System.out.println("* contText: "+ tag.getContentText());
    System.out.println("* parseString: "+ tag.getParseString());
    System.out.println(tag);
  }
  
}
