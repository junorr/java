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

import com.thoughtworks.xstream.XStream;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/06/2014
 */
public class Highlighter implements ViewUpdateListener {
  
  public static final String FILE = "./highlights.xml";
  

  private Map<String, Color> words;
  
  private XStream xstream;
  
  
  public Highlighter() {
    words = new HashMap<>();
    xstream = new XStream();
    init();
  }
  
  
  private void init() {
    Path p = Paths.get(FILE);
    if(Files.exists(p)) load();
  }
  
  
  public Highlighter load() {
    Path p = Paths.get(FILE);
    if(Files.exists(p)) {
      try {
        Map<String, Color> map = (Map<String, Color>) 
            xstream.fromXML(p.toFile());
        words.putAll(map);
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    return this;
  }
  
  
  public Highlighter save() {
    if(!words.isEmpty()) {
      Path p = Paths.get(FILE);
      try (OutputStream os = Files.newOutputStream(p, 
          StandardOpenOption.CREATE, 
          StandardOpenOption.WRITE)) {
        xstream.toXML(words, os);
        os.flush();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    return this;
  }
  
  
  public Highlighter add(String str, Color clr) {
    if(str != null && clr != null) {
      words.put(str, clr);
      save();
    }
    return this;
  }
  
  
  public Highlighter clear() {
    words.clear();
    return this;
  }
  
  
  public Map<String, Color> words() {
    return words;
  }


  @Override
  public void update(CharPanel cp) {
    Iterator<String> it = words.keySet().iterator();
    while(it.hasNext()) {
      String str = it.next();
      Color clr = words.get(str);
      findWordAndChange(cp, str, clr, 0);
    }//while
  }
  
  
  private void findWordAndChange(CharPanel cp, String str, Color c, int idx) {
    idx = cp.find(str, idx);
    if(idx < 0) return;
    changeColor(cp, idx, str.length(), c);
    findWordAndChange(cp, str, c, idx + str.length());
  }
  
  
  private void changeColor(CharPanel cp, int idx, int len, Color c) {
    for(int i = idx; i < (idx + len); i++) {
      JChar jc = cp.chars().get(i);
      jc.setForeground(c);
      jc.repaint();
    }//for
  }
  
}
