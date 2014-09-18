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
package badraadv.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 31/05/2012
 */
@ManagedBean
@SessionScoped
public class Switcher implements Serializable {
  
  private List<String> themes;
  
  private String backColor;
  
  private String current;
  
  
  public Switcher() {
    backColor = "#606060";
    current = "aristo";
    themes = new ArrayList<String>();
    themes.add("aristo");
    themes.add("afterdark");
    themes.add("afternoon");
    themes.add("afterwork");
    themes.add("black-tie");
    themes.add("blitzer");
    themes.add("bluesky");
    themes.add("casablanca");
    themes.add("cupertino");
    themes.add("dark-hive");
    themes.add("dot-luv");
    themes.add("eggplant");
    themes.add("excite-bike");
    themes.add("flick");
    themes.add("glass-x");
    themes.add("home");
    themes.add("hot-sneaks");
    themes.add("humanity");
    themes.add("le-frog");
    themes.add("midnight");
    themes.add("mint-choc");
    themes.add("overcast");
    themes.add("pepper-grinder");
    themes.add("redmond");
    themes.add("rocket");
    themes.add("sam");
    themes.add("smoothness");
    themes.add("south-street");
    themes.add("start");
    themes.add("sunny");
    themes.add("swanky-purse");
    themes.add("trontastic");
    themes.add("ui-darkness");
    themes.add("ui-lightness");
    themes.add("vader");
  }
  
  
  public void setThemes(List<String> l) {
    themes = l;
  }
  
  
  public List<String> getThemes() {
    return themes;
  }
  
  
  public void setBackColor(String color) {
    backColor = color;
  }
  
  
  public String getBackColor() {
    return backColor;
  }
  
  
  public String getCurrent() {
    return current;
  }
  
  
  public void setCurrent(String c) {
    current = c;
  }
  
}
