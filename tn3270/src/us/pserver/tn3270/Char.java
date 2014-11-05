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
 * Classe que representa um caractere no terminal 3270.
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
  
  
  /**
   * Construtor padrão sem argumentos
   */
  public Char() {
    protect = hidden = bold = false;
    bgcolor = fgcolor = highlight = outline = 0;
    row = col = ch = 0;
    field = null;
  }
  
  
  /**
   * Converte um caractere bruto RW3270Char em um 
   * objeto desta classe Char.
   * @param rch Caractere bruto RW3270Char.
   * @return objeto desta classe Char.
   */
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
  
  
  /**
   * Retorna um cursor com a posicao deste caractere na tela.
   * @return Cursor com a posicao deste caractere na tela.
   */
  public Cursor getCursor() {
    return new Cursor(row, col);
  }


  /**
   * Verifica se este caractere é protegido contra modificacao, ou não.
   * @return <code>true</code> se este caractere é 
   * protegido contra modificação, <code>false</code> caso contrário.
   */
  public boolean isProtected() {
    return protect;
  }


  /**
   * Define se este caractere será protegido contra modificacao, ou não.
   * @param protect <code>true</code> para que este caractere seja 
   * protegido contra modificação, <code>false</code> caso contrário.
   */
  public void setProtected(boolean protect) {
    this.protect = protect;
  }


  /**
   * Verifica se este caractere será exibido na tela ou não.
   * @return <code>true</code> se este caractere será
   * exibido na tela, <code>false</code> caso contrário.
   */
  public boolean isHidden() {
    return hidden;
  }


  /**
   * Define se este caractere será exibido na tela ou não.
   * @param hidden <code>true</code> se este caractere será
   * exibido na tela, <code>false</code> caso contrário.
   */
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }


  /**
   * Verifica se este caractere será negrito ou não.
   * @return <code>true</code> se este caractere será
   * negrito, <code>false</code> caso contrário.
   */
  public boolean isBold() {
    return bold;
  }


  /**
   * Define se este caractere será negrito ou não.
   * @param bold <code>true</code> se este caractere será
   * negrito, <code>false</code> caso contrário.
   */
  public void setBold(boolean bold) {
    this.bold = bold;
  }


  /**
   * Retorna o codigo 3270 da cor de fundo da tela.
   * @return codigo 3270 da cor de fundo da tela.
   */
  public short getBackground() {
    return bgcolor;
  }


  /**
   * Define o codigo 3270 da cor de fundo da tela.
   * @param bgcolor codigo 3270 da cor de fundo da tela.
   */
  public void setBackground(short bgcolor) {
    this.bgcolor = bgcolor;
  }


  /**
   * Retorna o codigo 3270 da cor das letras na tela.
   * @return codigo 3270 da cor das letras na tela.
   */
  public short getForeground() {
    return fgcolor;
  }


  /**
   * Define o codigo 3270 da cor das letras na tela.
   * @param fgcolor codigo 3270 da cor das letras na tela.
   */
  public void setForeground(short fgcolor) {
    this.fgcolor = fgcolor;
  }


  /**
   * Retorna o codigo 3270 do brilho das letras na tela.
   * @return codigo 3270 do brilho das letras na tela.
   */
  public short getHighlight() {
    return highlight;
  }


  /**
   * Define o codigo 3270 do brilho das letras na tela.
   * @param highlight codigo 3270 do brilho das letras na tela.
   */
  public void setHighlight(short highlight) {
    this.highlight = highlight;
  }


  public short getOutline() {
    return outline;
  }


  public void setOutline(short outline) {
    this.outline = outline;
  }


  /**
   * Retorna o caractere representado por este objeto.
   * @return caractere representado por este objeto.
   */
  public char getChar() {
    return ch;
  }


  /**
   * Define o caractere representado por este objeto.
   * @param ch caractere representado por este objeto.
   */
  public void setChar(char ch) {
    this.ch = ch;
  }


  /**
   * Retorna a linha do terminal onde está 
   * posicionado o caractere.
   * @return linha do terminal onde está 
   * posicionado o caractere.
   */
  public int getRow() {
    return row;
  }


  /**
   * Define a linha do terminal onde está 
   * posicionado o caractere.
   * @param row linha do terminal onde está 
   * posicionado o caractere.
   */
  public void setRow(int row) {
    this.row = row;
  }


  /**
   * Retorna a coluna do terminal onde está 
   * posicionado o caractere.
   * @return coluna do terminal onde está 
   * posicionado o caractere.
   */
  public int getColumn() {
    return col;
  }


  /**
   * Define a coluna do terminal onde está 
   * posicionado o caractere.
   * @param col coluna do terminal onde está 
   * posicionado o caractere.
   */
  public void setColumn(int col) {
    this.col = col;
  }


  /**
   * Retorna o campo ao qual pertence este caractere.
   * @return campo ao qual pertence este caractere.
   */
  public Field getField() {
    return field;
  }


  /**
   * Define o campo ao qual pertence este caractere.
   * @param field campo ao qual pertence este caractere.
   */
  public void setField(Field field) {
    this.field = field;
  }


  @Override
  public String toString() {
    return "Char{" + "ch=[" + ch + "] protect=" + protect + ", hidden=" + hidden + ", bold=" + bold + ", bgcolor=" + bgcolor + ", fgcolor=" + fgcolor + ", highlight=" + highlight + ", row=" + row + ", col=" + col + ", field=" + field + '}';
  }

}
