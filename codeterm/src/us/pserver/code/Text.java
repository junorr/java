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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/08/2014
 */
public class Text {

  private List<StringBuffer> lines;
  
  private int curline;
  
  
  public Text() {
    lines = new LinkedList<>();
    lines.add(new StringBuffer());
    curline = 0;
  }
  
  
  public StringBuffer currentLine() {
    return lines.get(curline);
  }
  
  
  public StringBuffer currentLine(int ln) {
    this.setLine(ln);
    return currentLine();
  }
  
  
  public int getLine() {
    return curline;
  }
  
  
  public Text setLine(int ln) {
    if(ln >= 0 && ln < lines.size()) 
      curline = ln;
    return this;
  }
  
  
  public Text addLine() {
    lines.add(new StringBuffer());
    return this;
  }
  
  
  public Text setNewLine() {
    return addLine().setLine(
        lines.size()-1);
  }
  
  
  public List<StringBuffer> lines() {
    return lines;
  }
  
  
  public int size() {
    return lines.size();
  }
  
}
