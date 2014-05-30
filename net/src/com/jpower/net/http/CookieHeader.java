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
public class CookieHeader extends HeaderElement {

  private LinkedList<Cookie> cks;
  
  
  public CookieHeader(String header) {
    super(header);
    cks = new LinkedList<>();
    this.parseCookies();
  }
  
  
  public List<Cookie> getCookies() {
    return cks;
  }
  
  
  public boolean containsCookie(String name) {
    return getCookie(name) != null;
  }
  
  
  public Cookie getCookie(String name) {
    if(cks.isEmpty()) return null;
    for(Cookie c : cks) {
      if(c.getName().equals(name))
        return c;
    }
    return null;
  }
  
  
  private void parseCookies() {
    if(this.getValue() == null) return;
    String[] s = this.getValue().trim().split(";");
    for(int i = 0; i < s.length; i++) {
      Cookie c = new Cookie(s[i].trim());
      if(c.getName() != null && !c.getName().equalsIgnoreCase("Path")
          && !c.getName().equalsIgnoreCase("Expires")
          && !c.getName().equalsIgnoreCase("Domain")) {
        cks.add(c);
      }
    }
  }
  
}
