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

import java.awt.Point;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/08/2014
 */
public class Cursor {
  
  private int cur;
  
  private int cols;
  
  
  public Cursor(int cols) {
    if(cols < 1) throw new IllegalArgumentException(
        "Must have at least one column");
    this.cols = cols;
    cur = 0;
  }
  
  
  public int columns() {
    return cols;
  }
  
  
  public int position() {
    return cur;
  }
  
  
  public Cursor position(int pos) {
    if(pos >= 0)
      cur = pos;
    return this;
  }
  
  
  public Cursor position(int col, int row) {
    if(col >= 0 && col < cols
        && row >= 0) {
      cur = row * cols + col;
    }
    return this;
  }
  
  
  public int col() {
    return cur % cols;
  }
  
  
  public int row() {
    return cur / cols;
  }
  
  
  public Point point() {
    return new Point(col(), row());
  }
  
}
