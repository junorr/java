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

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 29/07/2013
 */
public class Cursor {
  
  public static final int LIMIT_POSITION = 24*80;

  private int row;
  
  private int col;
  
  private int pos;
  
  
  public Cursor() {
    row = 0;
    col = 0;
    pos = 0;
  }
  
  
  public Cursor(int pos) {
    pos = adjustPosition(pos);
    if(!isValid(pos))
      throw new IllegalArgumentException("Invalid Cursor Position: "+ pos);
    this.setPosition(pos);
  }
  
  
  public Cursor(int row, int col) {
    if(!isValidRow(row))
      throw new IllegalArgumentException("Invalid Cursor Row: "+ row);
    if(!isValidCol(col))
      throw new IllegalArgumentException("Invalid Cursor Column: "+ col);
    this.setPosition(row, col);
  }
  
  
  public static Cursor translate(int pos) {
    return new Cursor(pos);
  }
  
  
  public static int translate(Cursor c) {
    if(c == null) return -1;
    return c.getPosition();
  }
  
  
  public static int translate(int row, int col) {
    int pos = (row-1)*80+col;
    return adjustPosition(pos);
  }
  
  
  public int getRow() {
    return row;
  }
  
  
  public int getColumn() {
    return col;
  }
  
  
  public int getPosition() {
    return pos;
  }
  
  
  public int row() {
    return row;
  }
  
  
  public int column() {
    return col;
  }
  
  
  public int position() {
    return pos -1;
  }
  
  
  public Cursor setPosition(int pos) {
    pos = adjustPosition(pos);
    if(isValid(pos)) {
      double dpos = pos;
      row = (int) Math.ceil(dpos/80);
      col = (int) (dpos % 80);
      if(col == 0) col = 80;
      this.pos = pos;
    }
    return this;
  }
  
  
  public Cursor setPosition(int row, int col) {
    return this.setPosition(
        translate(row, col));
  }
  
  
  public Cursor increment(int pos) {
    return this.setPosition(this.pos + pos);
  }
  
  
  public Cursor incrementRow(int plus) {
    return this.setPosition(pos + 80*plus);
  }
  
  
  public Cursor incrementColumn(int plus) {
    return this.setPosition(pos + plus);
  }
  
  
  private static int adjustPosition(int p) {
    if(p > LIMIT_POSITION)
      p -= LIMIT_POSITION;
    else if(p <= 0)
      p += LIMIT_POSITION;
    return p;
  }
  
  
  public static boolean isValid(int pos) {
    return pos > 0 && pos <= LIMIT_POSITION;
  }
  
  
  public static boolean isValid(int row, int col) {
    return isValidRow(row) && isValidCol(col);
  }
  
  
  public static boolean isValidRow(int row) {
    return row > 0 && row <= 24;
  }
  
  
  public static boolean isValidCol(int col) {
    return col > 0 && col <= 80;
  }
  
  
  public String toString() {
    return "Cursor{ row="+ row+ ", col="+ col+ ", pos="+ pos+" }";
  }
  
  
  public static void main(String[] args) {
    Cursor c = new Cursor();
    System.out.println("* "+ c);
    c.setPosition(23, 79);
    System.out.println("c.setPosition(23, 79);");
    System.out.println("* "+ c);
    
    c.setPosition(1759);
    System.out.println("c.setPosition(1759);");
    System.out.println("* "+ c);
    
    c.setPosition(24, 80);
    System.out.println("c.setPosition(24, 80);");
    System.out.println("* "+ c);
    
    c.increment(1);
    System.out.println("c.increment(1);");
    System.out.println("* "+ c);
    
    c.setPosition(24, 80);
    System.out.println("c.setPosition(24, 80);");
    System.out.println("* "+ c);
    
    c.incrementRow(1);
    System.out.println("c.incrementRow(1);");
    System.out.println("* "+ c);
    
    c.setPosition(1770);
    System.out.println("c.setPosition(1770);");
    System.out.println("* "+ c);
    
    c.setPosition(25, 5);
    System.out.println("c.setPosition(25, 5);");
    System.out.println("* "+ c);
  }
  
}
