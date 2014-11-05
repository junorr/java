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
 * Classe que representa um campo no terminal 3270.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 30/07/2013
 */
public class Field {

  private String str;
  
  private int row, col, len;
  
  private boolean protect, hidden, bold;
  
  private short bgcolor, fgcolor, highlight;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public Field() {
    str = null;
    row = col = len = 0;
    protect = hidden = bold = false;
    bgcolor = fgcolor = highlight = 0;
  }
  
   
  /**
   * Construtor que recebe a posição de linha, 
   * coluna e comprimento do campo.
   * @param row linha.
   * @param col coluna.
   * @param length comprimento do campo.
   */
  public Field(int row, int col, int length) {
    this.row = row;
    this.col = col;
    this.len = length;
    this.str = null;
    bgcolor = fgcolor = 0;
    this.setProtected(true);
  }
  
  
  /**
   * Construtor que recebe a posição de linha, 
   * coluna e conteúdo do campo.
   * @param row linha.
   * @param col coluna.
   * @param cont conteúdo do campo.
   */
  public Field(int row, int col, String cont) {
    this.row = row;
    this.col = col;
    this.len = (cont != null ? cont.length() : 0);
    this.str = cont;
    this.setProtected(str == null);
  }


  /**
   * Retorna o conteúdo do campo.
   * @return conteúdo do campo.
   */
  public String getContent() {
    return str;
  }


  /**
   * Define o conteúdo do campo.
   * @param str conteúdo do campo.
   * @return Este objeto Field modificado.
   */
  public Field setContent(String str) {
    this.str = str;
    len = (str != null ? str.length() : 0);
    return this;
  }


  /**
   * Retorna a linha da posição do campo.
   * @return linha da posição do campo.
   */
  public int getRow() {
    return row;
  }


  /**
   * Define a linha da posição do campo.
   * @param row linha da posição do campo.
   * @return Este objeto Field modificado.
   */
  public Field setRow(int row) {
    this.row = row;
    return this;
  }


  /**
   * Retorna a coluna da posição do campo.
   * @return coluna da posição do campo.
   */
  public int getColumn() {
    return col;
  }


  /**
   * Define a coluna da posição do campo.
   * @param col coluna da posição do campo.
   * @return Este objeto Field modificado.
   */
  public Field setColumn(int col) {
    this.col = col;
    return this;
  }
  
  
  /**
   * Retorna o codigo 3270 da cor de fundo do campo.
   * @return codigo 3270 da cor de fundo do campo.
   */
  public short getBackground() {
    return bgcolor;
  }
  
  
  /**
   * Define o codigo 3270 da cor de fundo do campo.
   * @param s codigo 3270 da cor de fundo do campo.
   */
  public void setBackground(short s) {
    bgcolor = s;
  }
  
  
  /**
   * Retorna o codigo 3270 da cor edas letras do campo.
   * @return codigo 3270 da cor das letras do campo.
   */
  public short getForeground() {
    return fgcolor;
  }
  
  
  /**
   * Define o codigo 3270 da cor das letras do campo.
   * @param s codigo 3270 da cor das letras do campo.
   */
  public void setForeground(short s) {
    fgcolor = s;
  }


  /**
   * Retorna o codigo 3270 do brilho do campo.
   * @return codigo 3270 do brilho do campo.
   */
  public short getHighlight() {
    return highlight;
  }


  /**
   * Define o codigo 3270 do brilho do campo.
   * @param highlight codigo 3270 do brilho do campo.
   */
  public void setHighlight(short highlight) {
    this.highlight = highlight;
  }


  /**
   * Retorna um objeto cursor com a posição do campo.
   * @return objeto cursor com a posição do campo.
   */
  public Cursor getCursor() {
    return new Cursor(row, col);
  }
  
  
  /**
   * Define a posição do campo.
   * @param row linha.
   * @param col coluna.
   * @return Este objeto Field modificado.
   */
  public Field setCursor(int row, int col) {
    this.row = row;
    this.col = col;
    return this;
  }


  /**
   * Retorna o comprimento do campo.
   * @return comprimento do campo.
   */
  public int getLength() {
    return len;
  }


  /**
   * Define o comprimento do campo.
   * @param len comprimento do campo.
   * @return Este objeto Field modificado.
   */
  public Field setLength(int len) {
    this.len = len;
    return this;
  }


  /**
   * Verifica se este campo está protegido contra modificação ou não.
   * @return <code>true</code> se este campo está protegido contra
   * modificação, <code>false</code> caso contrário.
   */
  public boolean isProtected() {
    return protect;
  }


  /**
   * Define se este campo será protegido contra modificação ou não.
   * @param protect <code>true</code> se este campo será protegido contra
   * modificação, <code>false</code> caso contrário.
   * @return Este objeto Field modificado.
   */
  public Field setProtected(boolean protect) {
    this.protect = protect;
    return this;
  }


  /**
   * Verifica se este campo será exibido na tela ou não.
   * @return <code>true</code> se este campo será exibido na tela,
   * <code>false</code> caso contrário.
   */
  public boolean isHidden() {
    return hidden;
  }


  /**
   * Define se este campo será exibido na tela ou não.
   * @param hidden <code>true</code> se este campo será exibido na tela,
   * <code>false</code> caso contrário.
   * @return Este objeto Field modificado.
   */
  public Field setHidden(boolean hidden) {
    this.hidden = hidden;
    return this;
  }
  
  
  /**
   * Verifica se este campo é negrito ou não.
   * @return <code>true</code> se este campo é negrito,
   * <code>false</code> caso contrário.
   */
  public boolean isBold() {
    return bold;
  }


  /**
   * Define se este campo será negrito ou não.
   * @param bold <code>true</code> se este campo será negrito,
   * <code>false</code> caso contrário.
   * @return Este objeto Field modificado.
   */
  public Field setBold(boolean bold) {
    this.bold = bold;
    return this;
  }
  
  
  /**
   * Converte um objeto bruto de campo RW3270Field
   * para um objeto Field.
   * @param rf objeto bruto de campo RW3270Field.
   * @return objeto Field.
   */
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
