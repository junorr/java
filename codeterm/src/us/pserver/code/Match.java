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

package us.pserver.code;

import java.awt.Color;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.lanwen.verbalregex.VerbalExpression;

/**
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/06/2014
 */
public class Match {

  private String regex;
  
  private Color color;
  
  
  public Match() {
    regex = null;
    color = null;
  }
  
  
  public Match(String rgx, Color clr) {
    if(rgx == null) throw new IllegalArgumentException(
        "Invalid Regex ("+ rgx+ ")");
    if(clr == null) throw new IllegalArgumentException(
        "Invalid Color ("+ clr+ ")");
    Pattern ptn = Pattern.compile(rgx);
    regex = rgx;
    color = clr;
  }
  
  
  public String getRegex() {
    return regex;
  }
  
  
  public Color getColor() {
    return color;
  }
  
  
  public Match setRegex(String rgx) {
    if(rgx == null) throw new IllegalArgumentException(
        "Invalid Regex ("+ rgx+ ")");
    Pattern ptn = Pattern.compile(rgx);
    regex = rgx;
    return this;
  }
  
  
  public Match setColor(Color clr) {
    if(clr == null) throw new IllegalArgumentException(
        "Invalid Color ("+ clr+ ")");
    color = clr;
    return this;
  }
  
  
  public Pattern getPattern() {
    if(regex == null)
      return null;
    return Pattern.compile(regex);
  }
  
  
  public VerbalExpression getExpression() {
    return VerbalExpression
        .regex().add(regex).build();
  }
  
  
  public Matcher matcherFor(String str) {
    if(regex == null) return null;
    return Pattern.compile(regex).matcher(str);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 11 * hash + Objects.hashCode(this.regex);
    hash = 11 * hash + Objects.hashCode(this.color);
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
    if (!Objects.equals(this.color, other.color)) {
      return false;
    }
    return true;
  }
  
}
