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

package us.pserver.download.util;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/03/2017
 */
public class Access {
  
  public static final String RES_ACCESS_JSON = "/resources/access.json";
  

  private final List<String> block;
  
  private final List<String> open;
  
  private final List<String> secure;
  
  
  private Access() {
    block = new ArrayList<>();
    open = new ArrayList<>();
    secure = new ArrayList<>();
  }
  
  
  public List<String> block() {
    return block;
  }
  
  
  public List<String> open() {
    return block;
  }
  
  
  public List<String> secure() {
    return block;
  }
  
  
  public boolean isBlocked(String uri) {
    boolean in = in(uri, block);
    //System.out.println("* Access.isBlocked( "+ uri+ " ): "+ in);
    return in;
  }
  
  
  public boolean isOpen(String uri) {
    boolean in = in(uri, open);
    //System.out.println("* Access.isOpen( "+ uri+ " ): "+ in);
    return in;
  }
  
  
  public boolean isSecure(String uri) {
    boolean in = in(uri, secure);
    //System.out.println("* Access.isSecure( "+ uri+ " ): "+ in);
    return in;
  }
  
  
  private boolean in(String uri, List<String> ls) {
    if(uri == null) return false;
    for(String s : ls) {
      if(s.startsWith("*") && uri.endsWith(s.substring(1))) {
        return true;
      }
      else if(s.endsWith("*") && uri.startsWith(s.substring(0, s.length() -1))) {
        return true;
      }
      else if(s.contains("*")) {
        String[] ss = s.split("\\*");
        if(uri.startsWith(ss[0]) && uri.endsWith(ss[1])) {
          return true;
        }
      }
      else if(uri.equals(s)) {
        return true;
      }
    }
    return false;
  }
  
  
  public static Access load() {
    return new Gson().fromJson(new InputStreamReader(
        Access.class.getResourceAsStream(RES_ACCESS_JSON)), 
        Access.class
    );
  }
  
}
