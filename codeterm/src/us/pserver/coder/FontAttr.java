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

package us.pserver.coder;

import java.awt.Font;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/08/2014
 */
public class FontAttr {

  private String family;
  
  private int size;
  
  private boolean italic, bold, underline;
  
  
  public FontAttr() {
    family = null;
    size = 0;
    italic = bold = underline = false;
  }


  public String getFontFamily() {
    return family;
  }


  public FontAttr setFontFamily(String family) {
    this.family = family;
    return this;
  }


  public int getSize() {
    return size;
  }


  public FontAttr setSize(int size) {
    this.size = size;
    return this;
  }


  public boolean isItalic() {
    return italic;
  }


  public FontAttr setItalic(boolean italic) {
    this.italic = italic;
    return this;
  }


  public boolean isBold() {
    return bold;
  }


  public FontAttr setBold(boolean bold) {
    this.bold = bold;
    return this;
  }


  public boolean isUnderline() {
    return underline;
  }


  public FontAttr setUnderline(boolean underline) {
    this.underline = underline;
    return this;
  }
  
  
  public Font getFont() {
    if(family == null)
      return null;
    int style = Font.PLAIN;
    if(bold && italic)
      style = Font.BOLD + Font.ITALIC;
    else if(bold)
      style = Font.BOLD;
    else if(italic)
      style = Font.ITALIC;
    return new Font(family, style, size);
  }
  
  
  public FontAttr setFont(Font f) {
    if(f == null) return this;
    family = f.getFamily();
    size = f.getSize();
    bold = f.isBold();
    italic = f.isItalic();
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.family);
    hash = 89 * hash + this.size;
    hash = 89 * hash + (this.italic ? 1 : 0);
    hash = 89 * hash + (this.bold ? 1 : 0);
    hash = 89 * hash + (this.underline ? 1 : 0);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FontAttr other = (FontAttr) obj;
    if (!Objects.equals(this.family, other.family)) {
      return false;
    }
    if (this.size != other.size) {
      return false;
    }
    if (this.italic != other.italic) {
      return false;
    }
    if (this.bold != other.bold) {
      return false;
    }
    if (this.underline != other.underline) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "FontAttr{" + "family=" + family + ", size=" + size + ", italic=" + italic + ", bold=" + bold + ", underline=" + underline + '}';
  }
  
}
