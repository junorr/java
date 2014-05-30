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

package us.pserver.tn3270;

import com.ino.freehost.client.RW3270Field;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/07/2013
 */
public class Field {

  private String str;
  
  private int row, col, len;
  
  private boolean protect, hidden, bold;
  
  private short bgcolor, fgcolor, highlight;
  
  
  public Field() {
    str = null;
    row = col = len = 0;
    protect = hidden = bold = false;
    bgcolor = fgcolor = highlight = 0;
  }
  
  
  public Field(int row, int col, int length) {
    this.row = row;
    this.col = col;
    this.len = length;
    this.str = null;
    bgcolor = fgcolor = 0;
    this.setProtected(true);
  }
  
  
  public Field(int row, int col, String cont) {
    this.row = row;
    this.col = col;
    this.len = (cont != null ? cont.length() : 0);
    this.str = cont;
    this.setProtected(str == null);
  }


  public String getContent() {
    return str;
  }


  public Field setContent(String str) {
    this.str = str;
    len = (str != null ? str.length() : 0);
    return this;
  }


  public int getRow() {
    return row;
  }


  public Field setRow(int row) {
    this.row = row;
    return this;
  }


  public int getColumn() {
    return col;
  }


  public Field setColumn(int col) {
    this.col = col;
    return this;
  }
  
  
  public short getBackground() {
    return bgcolor;
  }
  
  
  public void setBackground(short s) {
    bgcolor = s;
  }
  
  
  public short getForeground() {
    return fgcolor;
  }
  
  
  public void setForeground(short s) {
    fgcolor = s;
  }


  public short getHighlight() {
    return highlight;
  }


  public void setHighlight(short highlight) {
    this.highlight = highlight;
  }


  public Cursor getCursor() {
    return new Cursor(row, col);
  }
  
  
  public Field setCursor(int row, int col) {
    this.row = row;
    this.col = col;
    return this;
  }


  public int getLength() {
    return len;
  }


  public Field setLength(int len) {
    this.len = len;
    return this;
  }


  public boolean isProtected() {
    return protect;
  }


  public Field setProtected(boolean protect) {
    this.protect = protect;
    return this;
  }


  public boolean isHidden() {
    return hidden;
  }


  public Field setHidden(boolean hidden) {
    this.hidden = hidden;
    return this;
  }
  
  
  public boolean isBold() {
    return bold;
  }


  public Field setBold(boolean bold) {
    this.bold = bold;
    return this;
  }
  
  
  public static Field from(RW3270Field rf) {
    if(rf == null) return null;
    Field f = new Field();
    f.setBold(rf.isBold());
    f.setHidden(rf.isHidden());
    f.setBackground((short) rf.getBackgroundColor());
    f.setForeground((short) rf.getForegroundColor());
    f.setProtected(rf.isProtected());
    f.setContent(new String(rf.getDisplayChars()));
    f.setHighlight((short) rf.getHighlighting());
    Cursor cr = new Cursor(rf.getBegin()+2);
    f.setRow(cr.row()).setColumn(cr.column());
    return f;
  }


  @Override
  public String toString() {
    return "Field{row=" + row + ", col=" + col + ", len=" + len + ", protect=" + protect + ", hidden=" + hidden + ", bold=" + bold + ", bgcolor=" + bgcolor + ", fgcolor=" + fgcolor + ", highlight=" + highlight + ", str='" + str + "'}";
  }
  
}
