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

package us.pserver.coder;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/06/2014
 */
public class Match {

  private String name;
  
  private String regex;
  
  private TextStyle style;
  
  
  public Match() {
    name = null;
    regex = null;
    style = null;
  }
  
  
  public Match(String rgx, TextStyle ts) {
    this(null, rgx, ts);
  }
  
  
  public Match(String name, String rgx, TextStyle ts) {
    if(rgx == null) throw new IllegalArgumentException(
        "Invalid Regex ("+ rgx+ ")");
    if(ts == null) throw new IllegalArgumentException(
        "Invalid TextStyle ("+ ts+ ")");
    Pattern ptn = Pattern.compile(rgx);
    this.name = name;
    regex = rgx;
    style = ts;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public String getRegex() {
    return regex;
  }
  
  
  public TextStyle getTextStyle() {
    return style;
  }
  
  
  public Match setName(String nm) {
    name = nm;
    return this;
  }
  
  
  public Match setRegex(String rgx) {
    if(rgx == null) throw new IllegalArgumentException(
        "Invalid Regex ("+ rgx+ ")");
    Pattern ptn = Pattern.compile(rgx);
    regex = rgx;
    return this;
  }
  
  
  public Match setTextStyle(TextStyle ts) {
    if(ts == null) throw new IllegalArgumentException(
        "Invalid TextStyle ("+ ts+ ")");
    style = ts;
    return this;
  }
  
  
  public Pattern getPattern() {
    if(regex == null)
      return null;
    return Pattern.compile(regex);
  }
  
  
  public Matcher matcherFor(String str) {
    if(regex == null) return null;
    return Pattern.compile(regex).matcher(str);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 11 * hash + Objects.hashCode(this.regex);
    hash = 11 * hash + Objects.hashCode(this.style);
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
    final Match other = (Match) obj;
    if (!Objects.equals(this.regex, other.regex)) {
      return false;
    }
    if (!Objects.equals(this.style, other.style)) {
      return false;
    }
    return true;
  }
  
}
