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
package com.jpower.nnet.http;

import java.util.LinkedList;


/**
 * Construtor de marcação HTML.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2012-07-24
 */
public class HtmlBuilder {
  
  public static final String
      TAG_BODY = "<body>",
      TAG_HTML = "<html>",
      TAG_HEADER = "<header>",
      TAG_H = "<h#>",
      TAG_BR = "</br>";
  
  
  private StringBuilder html;
  
  private LinkedList<String> tags;
  
  
  /**
   * Construtor padrão sem argumentos, abre a primera tag 
   * <code>&lt;HTML&gt;</code>.
   */
  public HtmlBuilder() {
    html = new StringBuilder();
    tags = new LinkedList<String>();
    this.openHtml();
  }
  
  
  /**
   * Construtor que recebe marcação html previa.
   * @param pref Marcação html previa.
   */
  public HtmlBuilder(String pref) {
    html = new StringBuilder();
    tags = new LinkedList<String>();
    this.append(pref);
  }
  
  
  /**
   * Adiciona a tag informada, deixando-a aberta.
   * @param tag Tag html.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   */
  public HtmlBuilder openTag(String tag) {
    if(tag != null && !tag.trim().isEmpty()) {
      html.append(tag);
      tags.add(tag);
    }
    return this;
  }
  

  /**
   * Verifica se existe a tag informada na marcação HTML.
   * @param tag Tag html.
   * @return <code>true</code> se a marcação html contém
   * a tag informada, <code>false</code> caso contrário.
   */
  public boolean containsTag(String tag) {
    return html.indexOf(tag) >= 0;
  }
  
  
  /**
   * Remove a tag informada da marcação html.
   * @param tag Tag html.
   * @return Tag html removida, ou <code>null</code>
   * caso não seja encontrada.
   */
  public String removeTag(String tag) {
    if(tag == null || tag.trim().isEmpty()
        || !this.containsTag(tag))
      return null;
    
    int start = html.indexOf(tag);
    int end = 0;
    if(start < 0) return null;
    
    if(tag.equals(TAG_BR)) {
      end = TAG_BR.length() + start;
    } else {
      end = html.indexOf(this.closedTag(tag), start)
          + this.closedTag(tag).length();
    }
    
    if(end < start) {
      end = html.indexOf("/>", start) + 2;
    }
    
    String rm = html.substring(start, end);
    html.replace(start, end, "");
    return rm;
  }
  
  
  /**
   * Adiciona o conteúdo informado à marcação html.
   * @param s Conteúdo a ser adicionado.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   */
  public HtmlBuilder append(String s) {
    if(s != null && !s.trim().isEmpty())
      html.append(s);
    return this;
  }
  
  
  /**
   * Fecha a tag atualmente aberta.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   * @see #getCurrentTag() 
   */
  public HtmlBuilder closeTag() {
    if(!tags.isEmpty()) {
      String tag = tags.pollLast();
      if(!tag.contains(">")) {
        html.append("/>");
      } else {
        html.append(this.closedTag(tag));
      }
    }
    return this;
  }
  
  
  /**
   * Retorna a tag atualmente aberta.
   * @return Tag html.
   */
  public String getCurrentTag() {
    if(tags.isEmpty()) return null;
    return tags.peekLast();
  }
  
  
  /**
   * Retorna a versão fechada da tag informada.
   * @param tag Tag html.
   * @return Versão fechada da tag html.
   */
  private String closedTag(String tag) {
    if(tag == null || tag.trim().isEmpty())
      return null;
    return tag.substring(0, 1).concat("/")
        .concat(tag.substring(1));
  }
  
  
  /**
   * Limpa a marcação html e todas as tags abertas.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   */
  public HtmlBuilder clear() {
    html = new StringBuilder();
    tags.clear();
    return this;
  }
  
  
  public String getContentOfTag(String tag) {
    if(tag == null || tag.trim().isEmpty()
        || !this.containsTag(tag))
      return null;
    
    int start = html.indexOf(tag);
    int end = 0;
    if(start < 0) return null;
    start += tag.length();
    
    if(tag.equals(TAG_BR))
      return null;
    
    end = html.indexOf(this.closedTag(tag), start);
    
    if(end < start) {
      end = html.indexOf("/>", start);
    }
    
    return html.substring(start, end);
  }
  
  
  /**
   * Retorna a marcação html no estado atual, sem fechar as tags abertas.
   * @return marcação html.
   */
  public String getHtml() {
    return html.toString();
  }
  
  
  /**
   * Abre a tag html <code>&lt;body&gt;</code>.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   */
  public HtmlBuilder openBody() {
    return this.openTag(TAG_BODY);
  }
  
  
  /**
   * Abre a tag html <code>&lt;header&gt;</code>.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   */
  public HtmlBuilder openHeader() {
    return this.openTag(TAG_HEADER);
  }
  
  
  /**
   * Abre a tag html <code>&lt;html&gt;</code>.
   * Esta tag é aberta automaticamente no construtor de <code>HtmlBuilder</code>.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   */
  public HtmlBuilder openHtml() {
    return this.openTag(TAG_HTML);
  }
  
  
  /**
   * Abre a tag de título html <code>&lt;h{level}&gt;</code>. Ex: <code>&lt;h1&gt;</code>.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   */
  public HtmlBuilder openH(int level) {
    if(level >= 1 || level <= 6) {
      this.openTag(TAG_H.replace("#", String.valueOf(level)));
    }
    return this;
  }
  
  
  /**
   * Adiciona a tag html de nova linha <code>&lt;/br&gt;</code>.
   * @return Esta instância modificada de <code>HtmlBuilder</code>.
   */
  public HtmlBuilder newLine() {
    return this.append(TAG_BR);
  }
  
  
  /**
   * Fecha todas as tags abertas.
   */
  private void closeAll() {
    while(!tags.isEmpty())
      this.closeTag();
  }
  
  
  /**
   * Fecha todas as tags html abertas e retorna a marcação html.
   * @return marcação html.
   */
  public String build() {
    this.closeAll();
    return this.getHtml();
  }
  
  
  public static void main(String[] args) {
    HtmlBuilder h = new HtmlBuilder();
    System.out.println("current = "+ h.getCurrentTag());
    h.openH(2).append("24/07/2012").closeTag()
        .append("<input type='text' id='input' name='input'/>").newLine();
    System.out.println("current = "+ h.getCurrentTag());
    System.out.println(h.build());
    System.out.println("content </br> = "+ h.getContentOfTag("</br>"));
    System.out.println("content <input = "+ h.getContentOfTag("<input"));
    System.out.println("content <h2> = "+ h.getContentOfTag("<h2>"));
    System.out.println("remove </br> = "+ h.removeTag("</br>"));
    System.out.println("remove <input = "+ h.removeTag("<input"));
    System.out.println(h.getHtml());
  }
  
}
