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
public class TagParam {
  
  public static final String ATTR = "=";

  public static final String SQUOTA = "'";

  public static final String DQUOTA = "\"";
  
  
  private String name;
  
  private String value;


  public TagParam(String param) {
    if(param != null && param.contains(ATTR)) {
      int iat = param.indexOf(ATTR);
      name = param.substring(0, iat);
      int iqt = param.indexOf(SQUOTA);
      if(iqt < 0) iqt = param.indexOf(DQUOTA);
      if(iqt < 0) return;
      int iqt2 = param.indexOf(SQUOTA, iqt+1);
      if(iqt2 < 0) iqt2 = param.indexOf(DQUOTA, iqt+1);
      if(iqt2 < 0) return;
      value = param.substring(iqt+1, iqt2);
    }
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
    return name.concat(ATTR)
        .concat(DQUOTA).concat(value)
        .concat(DQUOTA);
  }
  
  
  public static void main(String[] args) {
    TagParam tp = new TagParam("style=\"color: white;\"");
    System.out.println("* name  : "+ tp.getName());
    System.out.println("* value : "+ tp.getValue());
    System.out.println("* string: "+ tp);
  }
  
}
