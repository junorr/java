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
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ru.lanwen.verbalregex.VerbalExpression;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/06/2014
 */
public class Highlighter implements ViewUpdateListener {
  
  public static final String FILE = "./highlights.xml";
  

  private List<Match> words;
  
  private XStream xstream;
  
  
  public Highlighter() {
    words = new ArrayList<>();
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
        List<Match> lst = (List<Match>) 
            xstream.fromXML(p.toFile());
        words.addAll(lst);
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
  
  
  public Highlighter add(VerbalExpression exp, Color clr) {
    if(exp != null && clr != null) {
      words.add(new Match(exp.toString(), clr));
      save();
    }
    return this;
  }
  
  
  public Highlighter clear() {
    words.clear();
    return this;
  }
  
  
  public List<Match> words() {
    return words;
  }


  @Override
  public void update(CharPanel cp) {
    for(Match mtc : words) {
      findAndChangeColor(cp, mtc, 0);
    }
  }
  
  
  private void findAndChangeColor(CharPanel chp, Match mtc, int idx) {
    if(idx < 0 || idx > chp.chars().size() -1)
      return;
    
    int isp = chp.find(' ', idx);
    int iln = chp.find('\n', idx);
    int idx2 = minValid(isp, iln);
    if(idx2 < 0) idx2 = chp.chars().size() -1;
    
    String str = chp.getString(idx, idx2-idx);
    if(mtc.getExpression().testExact(str)) {
      changeColor(chp, idx, idx2-idx, mtc.getColor());
    }
    findAndChangeColor(chp, mtc, idx2 +1);
  }
  
  
  private int minValid(int v1, int v2) {
    if(v1 >= 0 && (v1 < v2 || v2 < 0))
      return v1;
    else if(v2 >= 0 && (v2 < v1 || v1 < 0))
      return v2;
    else 
      return -1;
  }
  
  
  private void changeColor(CharPanel chp, int idx, int len, Color c) {
    for(int i = idx; i < (idx + len); i++) {
      JChar jc = chp.chars().get(i);
      jc.setForeground(c);
      jc.repaint();
    }//for
  }
  
}
