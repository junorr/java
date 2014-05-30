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

package com.jpower.csv;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 07/05/2013
 */
public class Table {
  
  private LinkedList<Line> lines;
  
  private Line titles;
  
  
  public Table(Line titles) {
    if(titles == null || titles.isEmpty())
      throw new IllegalArgumentException(
          "Invalid titles: "+ titles);
    lines = new LinkedList<>();
    this.titles = titles;
  }
  
  
  public Table add(Line ln) {
    if(ln != null && ln.size() <= titles.maxSize()) {
      if(ln.size() < titles.maxSize()) {
        int num = titles.maxSize() - ln.size();
        for(int i = 0; i < num; i++) {
          ln.add(null);
        }
      }
      lines.add(ln);
    }
    return this;
  }
  
  
  public List<Line> lines() {
    return lines;
  }
  
  
  public Line titles() {
    return titles;
  }
  
  
  public String toString() {
    if(titles == null || titles.isEmpty()
        || lines == null || lines.isEmpty())
      return null;
    
    StringBuilder sb = new StringBuilder();
    List<String> cels = titles.cels();
    for(int i = 0; i < cels.size(); i++) {
      sb.append(cels.get(i));
      if(i < cels.size() -1)
        sb.append(";");
    }
    sb.append("\n");
    
    for(Line ln : lines) {
      cels = ln.cels();
      for(int i = 0; i < ln.size(); i++) {
      sb.append(cels.get(i));
      if(i < cels.size() -1)
        sb.append(";");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

}
