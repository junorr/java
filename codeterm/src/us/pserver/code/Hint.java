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
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static us.pserver.code.Highlighter.FILE;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/06/2014
 */
public class Hint {
  
  public static final String HINTS_FILE = "./hints.xml";
  

  private List<String> hints;
  
  private XStream xstream;
  
  
  public Hint() {
    hints = new ArrayList<>();
    xstream = new XStream();
    Path p = Paths.get(HINTS_FILE);
    if(Files.exists(p))
      load();
  }
  
  
  public Hint add(String word) {
    if(word != null && !hints.contains(word)) {
      hints.add(word);
      sort().save();
    }
    return this;
  }
  
  
  public String first() {
    if(hints.isEmpty())
      return null;
    return hints.get(0);
  }
  
  
  public String hint(String part) {
    if(part == null)
      return first();
    for(String str : hints) {
      if(str.startsWith(part))
        return str;
    }
    return null;
  }
  
  
  public int startsWithIndex(String str) {
    int idx = -1;
    if(str == null) return idx;
    for(int i = 0; i < hints.size(); i++) {
      if(hints.get(i).startsWith(str)) {
        idx = i;
        break;
      }
    }
    return idx;
  }
  
  
  public List<String> hintList(String part) {
    int idx = startsWithIndex(part);
    if(idx < 0) return hints;
    return hints.subList(idx, hints.size()-1);
  }
  
  
  public boolean isEmpty() {
    return hints.isEmpty();
  }
  
  
  public Hint clear() {
    hints.clear();
    return this;
  }
  
  
  public List<String> hints() {
    return hints;
  }
  
  
  private Hint sort() {
    Collections.sort(hints);
    return this;
  }
  
  
  public Hint load() {
    Path p = Paths.get(FILE);
    if(Files.exists(p)) {
      try {
        List<String> lst = (List<String>) 
            xstream.fromXML(p.toFile());
        hints.addAll(lst);
      } catch(Exception e) {}
    }
    return this;
  }
  
  
  public Hint save() {
    if(!hints.isEmpty()) {
      Path p = Paths.get(HINTS_FILE);
      try (OutputStream os = Files.newOutputStream(p, 
          StandardOpenOption.CREATE, 
          StandardOpenOption.WRITE)) {
        xstream.toXML(hints, os);
        os.flush();
      } catch(Exception e) {}
    }
    return this;
  }
  
  
}
