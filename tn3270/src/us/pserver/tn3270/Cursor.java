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

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Classe que representa a posição do cursor no terminal.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 29/07/2013
 */
public class Cursor {
  
  /**
   * <code>LIMIT_POSITION = 24*80;</code><br>.
   * Posição máxima do cursor no terminal 
   */
  public static final int LIMIT_POSITION = 24*80;

  private int row;
  
  private int col;
  
  private int pos;
  
  
  /**
   * Construtor padrão sem argumentos, inicia o cursor na posição {0,0}.
   */
  public Cursor() {
    row = 0;
    col = 0;
    pos = 0;
  }
  
  
  /**
   * Construtor que recebe a posição linear do cursor.
   * @param pos posição linear do cursor.
   */
  public Cursor(int pos) {
    pos = adjustPosition(pos);
    if(!isValid(pos))
      throw new IllegalArgumentException("Invalid Cursor Position: "+ pos);
    this.setPosition(pos);
  }
  
  
  /**
   * Construtor que recebe a linha e coluna da posição do cursor.
   * @param row linha da posição do cursor.
   * @param col coluna da posição do cursor.
   */
  public Cursor(int row, int col) {
    if(!isValidRow(row))
      throw new IllegalArgumentException("Invalid Cursor Row: "+ row);
    if(!isValidCol(col))
      throw new IllegalArgumentException("Invalid Cursor Column: "+ col);
    this.setPosition(row, col);
  }
  
  
  /**
   * Traduz a posição linear em um objeto Cursor.
   * @param pos posição linear.
   * @return objeto Cursor.
   */
  public static Cursor translate(int pos) {
    return new Cursor(pos);
  }
  
  
  /**
   * Traduz um objeto Cursor em posição linear.
   * @param c objeto Cursor.
   * @return posição linear.
   */
  public static int translate(Cursor c) {
    if(c == null) return -1;
    return c.getPosition();
  }
  
  
  /**
   * Traduz linha e coluna em posição linear.
   * @param row linha.
   * @param col coluna.
   * @return posição linear.
   */
  public static int translate(int row, int col) {
    int pos = (row-1)*80+col;
    return adjustPosition(pos);
  }
  
  
  /**
   * Retorna a linha do Cursor.
   * @return linha do Cursor.
   */
  public int getRow() {
    return row;
  }
  
  
  /**
   * Retorna a coluna do Cursor.
   * @return coluna do Cursor.
   */
  public int getColumn() {
    return col;
  }
  
  
  /**
   * Retorna a posição linear do Cursor.
   * @return posição linear do Cursor.
   */
  public int getPosition() {
    return pos;
  }
  
  
  /**
   * Retorna a linha do Cursor.
   * @return linha do Cursor.
   */
  public int row() {
    return row;
  }
  
  
  /**
   * Retorna a coluna do Cursor.
   * @return coluna do Cursor.
   */
  public int column() {
    return col;
  }
  
  
  /**
   * Retorna a posição linear do Cursor.
   * @return posição linear do Cursor.
   */
  public int position() {
    return pos -1;
  }
  
  
  /**
   * Retorna um novo objeto Cursor representando a 
   * próxima posição referente à este objeto Cursor.
   * @return novo objeto Cursor representando a 
   * próxima posição referente à este objeto Cursor.
   */
  public Cursor next() {
    return new Cursor(pos+1);
  }
  
  
  /**
   * Retorna um novo objeto Cursor representando a 
   * posição anterior referente à este objeto Cursor.
   * @return novo objeto Cursor representando a 
   * posição anterior referente à este objeto Cursor.
   */
  public Cursor prev() {
    return new Cursor(pos-1);
  }
  
  
  /**
   * Define a posição linear deste Cursor.
   * @param pos posição linear.
   * @return Este objeto Cursor modificado.
   */
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
  
  
  /**
   * Define a posição da linha e coluna deste Cursor.
   * @param row linha.
   * @param col coluna.
   * @return Este objeto Cursor modificado.
   */
  public Cursor setPosition(int row, int col) {
    return this.setPosition(
        translate(row, col));
  }
  
  
  /**
   * Incrementa a posição deste Cursor em <code>(pos)</code> posições.
   * @param pos Número de posições a ser incrementado.
   * @return Este objeto Cursor modificado.
   */
  public Cursor increment(int pos) {
    return this.setPosition(this.pos + pos);
  }
  
  
  /**
   * Incrementa a posição da linha deste Cursor em 
   * <code>(pos)</code> posições.
   * @param plus Número de linhas a ser incrementado.
   * @return Este objeto Cursor modificado.
   */
  public Cursor incrementRow(int plus) {
    return this.setPosition(pos + 80*plus);
  }
  
  
  /**
   * Incrementa a posição da coluna deste Cursor em 
   * <code>(pos)</code> posições.
   * @param plus Número de colunas a ser incrementado.
   * @return Este objeto Cursor modificado.
   */
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
  
  
  /**
   * Verifica se a posição informada é uma posição 
   * válida para um terminal 3270.
   * @param pos posição linear.
   * @return <code>true</code> se a posição informada 
   * é uma posição válida para um terminal 3270,
   * <code>false</code> caso contrário.
   */
  public static boolean isValid(int pos) {
    return pos > 0 && pos <= LIMIT_POSITION;
  }
  
  
  /**
   * Verifica se a posição informada é uma posição 
   * válida para um terminal 3270.
   * @param row linha.
   * @param col coluna.
   * @return <code>true</code> se a posição informada 
   * é uma posição válida para um terminal 3270,
   * <code>false</code> caso contrário.
   */
  public static boolean isValid(int row, int col) {
    return isValidRow(row) && isValidCol(col);
  }
  
  
  /**
   * Verifica se a posição da linha informada é uma posição 
   * válida para um terminal 3270.
   * @param row linha.
   * @return <code>true</code> se a posição informada 
   * é uma posição válida para um terminal 3270,
   * <code>false</code> caso contrário.
   */
  public static boolean isValidRow(int row) {
    return row > 0 && row <= 24;
  }
  
  
  /**
   * Verifica se a posição da coluna informada é uma posição 
   * válida para um terminal 3270.
   * @param col coluna.
   * @return <code>true</code> se a posição informada 
   * é uma posição válida para um terminal 3270,
   * <code>false</code> caso contrário.
   */
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
