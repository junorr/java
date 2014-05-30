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

package com.jpower.tn3270;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/07/2013
 */
public class Field {

  private String str;
  
  private int row, col, len;
  
  private boolean protect, hidden, bold;
  
  
  public Field() {
    str = null;
    row = col = len = 0;
    protect = hidden = bold = false;
  }
  
  
  public Field(int row, int col, int length) {
    this.row = row;
    this.col = col;
    this.len = length;
    this.str = null;
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
  
  
  public String toString() {
    return "Field{ row="+ row+ ", col="+ col
        + (str != null ? (", str='"+ str+ "'") : ", len="+ len)
        + " }";
  }
  
}
