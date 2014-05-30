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

package us.pserver.remote.http;

import java.util.Objects;


/**
 * Representa um cabeçalho do protocolo HTTP.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class Header implements HttpConst {
  
  public static final String BOUNDARY = "Boundary";
  
  private String name;
  
  private String value;
  
  
  /**
   * Construtor padrão sem argumentos,
   * constrói um cabeçalho vazio.
   */
  public Header() {
    name = null;
    value = null;
  }
  
  
  /**
   * Construtor que recebe o nome e o valor do cabeçalho.
   * @param name nome <code>String</code>
   * @param value valor <code>String</code>.
   */
  public Header(String name, String value) {
    this.name = name;
    this.value = value;
  }


  /**
   * Retorna o nome do cabeçalho.
   * @return <code>String</code>.
   */
  public String getName() {
    return name;
  }


  /**
   * Define o nome do cabeçalho.
   * @param name <code>String</code>.
   */
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Retorna o valor do cabeçalho.
   * @return <code>String</code>.
   */
  public String getValue() {
    return value;
  }


  /**
   * Define o valor do cabeçalho.
   * @param value <code>String</code>.
   */
  public void setValue(String value) {
    this.value = value;
  }
  
  
  /**
   * Cria um cabeçalho de delimitação de conteúdo.
   * @param value <code>String</code> delimitadora.
   * @return <code>Header</code>.
   */
  public static Header boundary(String value) {
    return new Header(BOUNDARY, value);
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.name);
    hash = 97 * hash + Objects.hashCode(this.value);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Header other = (Header) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return true;
  }


  /**
   * Retorna o conteúdo do cabeçalho no formato 
   * <code>[nome]: [valor]</code>.
   * @return <code>String</code>.
   */
  @Override
  public String toString() {
    if(name == null) return null;
    if(BOUNDARY.equalsIgnoreCase(name))
      return value;
    
    return name + ": " + (value == null ? "" : value);
  }
  
}
