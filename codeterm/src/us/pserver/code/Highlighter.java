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
import java.util.List;
import java.util.regex.Matcher;
import ru.lanwen.verbalregex.VerbalExpression;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 02/06/2014
 */
public class Highlighter implements ViewUpdateListener {
  
  public static final String 
      FILE = "./highlights.xml",
      COLOR = "color",
      MATCH = "match";
  

  private List<Match> words;
  
  private XStream xstream;
  
  private ColorConverter conv;
  
  
  public Highlighter() {
    words = new ArrayList<>();
    conv = new ColorConverter();
    xstream = new XStream();
    xstream.alias(MATCH, Match.class);
    xstream.alias(COLOR, Color.class);
    xstream.registerConverter(conv);
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
      } catch(Exception e) {}
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
      } catch(Exception e) {}
    }
    return this;
  }
  
  
  public Highlighter add(VerbalExpression exp, Color clr) {
    if(exp != null && clr != null) {
      Match m = new Match(exp.toString(), clr);
      if(!words.contains(m)) {
        words.add(m);
        save();
      }
    }
    return this;
  }
  
  
  public Highlighter add(Match m) {
    if(m != null && m.getRegex() != null
        && m.getColor() != null
        && !words.contains(m)) {
      words.add(m);
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
    String text = cp.getText();
    for(Match mtc : words) {
      Matcher m = mtc.matcherFor(text);
      while(m.find()) {
        changeColor(cp, m.start(), m.end(), mtc.getColor());
      }
    }
  }
  
  
  private void changeColor(CharPanel chp, int start, int end, Color c) {
    for(int i = start; i < end; i++) {
      JChar jc = chp.chars().get(i);
      jc.setForeground(c);
      jc.paint();
    }//for
  }
  
}
