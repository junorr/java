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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 19/07/2013
 */
public class HeaderElement {

  public static final String HEADER_DIV = ": ";
  
  private String name;
  
  private String value;
  
  
  public HeaderElement(String header) {
    if(header != null && header.contains(HEADER_DIV)) {
      int idiv = header.indexOf(HEADER_DIV);
      name = header.substring(0, idiv);
      value = header.substring(idiv + HEADER_DIV.length());
    }
  }
  
  
  public boolean isNameEquals(String str) {
    return name != null && name.equals(str);
  }
  
  
  public boolean isValueEquals(String str) {
    return value != null && value.equals(str);
  }
  
  
  public boolean valueContains(String str) {
    return value != null && value.contains(str);
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getValue() {
    return value;
  }


  public void setValue(String value) {
    this.value = value;
  }
  
  
  public String toString() {
    if(name == null || value == null) 
      return null;
    return name.concat(HEADER_DIV).concat(value);
  }
  
  
  public static void main(String[] args) {
    HeaderElement hd = new HeaderElement("Accept: text/html");
    System.out.println("* Name  : "+ hd.getName());
    System.out.println("* Value : "+ hd.getValue());
    System.out.println("* Header: "+ hd);
  }
  
}
