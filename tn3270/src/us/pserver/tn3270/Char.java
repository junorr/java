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

import com.ino.freehost.client.RW3270Char;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/02/2014
 */
public class Char {

  private boolean protect, hidden, bold;
  
  private short bgcolor, fgcolor, highlight, outline;
  
  private char ch;
  
  private int row, col;
  
  private Field field;
  
  
  public Char() {
    protect = hidden = bold = false;
    bgcolor = fgcolor = highlight = outline = 0;
    row = col = ch = 0;
    field = null;
  }
  
  
  public static Char from(RW3270Char rch) {
    if(rch == null) return null;
    Char ch = new Char();
    Field f = Field.from(rch.getField());
    Cursor cur = new Cursor(rch.getPosition()+1);
    ch.setBackground(f.getBackground());
    if(f.getBackground() == RW3270Char.BGCOLOR_DEFAULT)
      ch.setBackground((short)rch.getBackground());
    ch.setForeground(f.getForeground());
    if(f.getForeground() == RW3270Char.FGCOLOR_DEFAULT)
      ch.setForeground((short)rch.getForeground());
    ch.setHighlight(rch.getHighlighting());
    ch.setOutline((short) rch.getOutlining());
    ch.setBold(f.isBold());
    if(ch.isBold()) 
      ch.setForeground(RW3270Char.WHITE);
    ch.setChar(rch.getDisplayChar());
    ch.setColumn(cur.column());
    ch.setField(f);
    ch.setHidden(f.isHidden());
    ch.setProtected(f.isProtected());
    ch.setRow(cur.row());
    return ch;
  }
  
  
  public Cursor getCursor() {
    return new Cursor(row, col);
  }


  public boolean isProtected() {
    return protect;
  }


  public void setProtected(boolean protect) {
    this.protect = protect;
  }


  public boolean isHidden() {
    return hidden;
  }


  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }


  public boolean isBold() {
    return bold;
  }


  public void setBold(boolean bold) {
    this.bold = bold;
  }


  public short getBackground() {
    return bgcolor;
  }


  public void setBackground(short bgcolor) {
    this.bgcolor = bgcolor;
  }


  public short getForeground() {
    return fgcolor;
  }


  public void setForeground(short fgcolor) {
    this.fgcolor = fgcolor;
  }


  public short getHighlight() {
    return highlight;
  }


  public void setHighlight(short highlight) {
    this.highlight = highlight;
  }


  public short getOutline() {
    return outline;
  }


  public void setOutline(short outline) {
    this.outline = outline;
  }


  public char getChar() {
    return ch;
  }


  public void setChar(char ch) {
    this.ch = ch;
  }


  public int getRow() {
    return row;
  }


  public void setRow(int row) {
    this.row = row;
  }


  public int getColumn() {
    return col;
  }


  public void setColumn(int col) {
    this.col = col;
  }


  public Field getField() {
    return field;
  }


  public void setField(Field field) {
    this.field = field;
  }


  @Override
  public String toString() {
    return "Char{" + "ch=[" + ch + "] protect=" + protect + ", hidden=" + hidden + ", bold=" + bold + ", bgcolor=" + bgcolor + ", fgcolor=" + fgcolor + ", highlight=" + highlight + ", row=" + row + ", col=" + col + ", field=" + field + '}';
  }

}
