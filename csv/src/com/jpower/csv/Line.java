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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 07/05/2013
 */
public class Line {

  private List<String> cels;
  
  private int cols;
  
  
  public Line(int cols) {
    if(cols <= 0) throw new 
        IllegalArgumentException(
        "Invalid number of cols: "+ cols);
    this.cols = cols;
    cels = new ArrayList<>(cols);
  }
  
  
  public int maxSize() {
    return cols;
  }
  
  
  public Line add(String val) {
    if(cels.size() < cols) {
      if(val == null) val = "";
      cels.add(val);
    }
    return this;
  }
  
  
  public Line add(Object val) {
    if(cels.size() < cols) {
      if(val == null)
        this.add(null);
      else
        this.add(val.toString());
    }
    return this;
  }
  
  
  public String get(int idx) {
    return cels.get(idx);
  }
  
  
  public List<String> cels() {
    return Collections.unmodifiableList(cels);
  }
  
  
  public boolean remove(String val) {
    return cels.remove(val);
  }
  
  
  public String remove(int idx) {
    return cels.remove(idx);
  }
  
  
  public String set(int idx, String val) {
    if(idx < 0 || idx >= cols) 
      return null;
    return cels.set(idx, val);
  }
  
  
  public Line clear() {
    cels.clear();
    return this;
  }
  
  
  public boolean isEmpty() {
    return cels.isEmpty();
  }
  
  
  public int size() {
    return cels.size();
  }
  
}
