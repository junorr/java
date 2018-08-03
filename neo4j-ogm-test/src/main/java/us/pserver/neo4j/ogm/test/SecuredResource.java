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

package us.pserver.neo4j.ogm.test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/03/2018
 */
public class SecuredResource extends Entity {

  private final String name;
  
  private final List<SecurityRole> rules;
  
  
  private SecuredResource() {
    name = null;
    rules = null;
  }
  
  public SecuredResource(String name, SecurityRole ... rules) {
    this(name, Arrays.asList(rules));
  }
  
  public SecuredResource(String name, List<SecurityRole> rules) {
    this.name = name;
    this.rules = rules;
  }


  public String getName() {
    return name;
  }


  public List<SecurityRole> getRules() {
    return rules;
  }
  
  
  public SecuredResource setName(String name) {
    return new SecuredResource(name, rules);
  }
  
  
  public SecuredResource setRules(List<SecurityRole> rules) {
    return new SecuredResource(name, rules);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 71 * hash + Objects.hashCode(this.name);
    hash = 71 * hash + Objects.hashCode(this.rules);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SecuredResource other = (SecuredResource) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.rules, other.rules)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "SecuredResource{" + "name=" + name + ", rules=" + rules + '}';
  }
  
}
