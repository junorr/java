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

import java.awt.Color;
import java.util.Objects;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/08/2014
 */
public class TextStyle {

  private Color fg, bg;
  
  private FontAttr font;
  
  
  public TextStyle() {
    fg = null;
    bg = null;
    font = null;
  }
  
  
  public TextStyle setForeground(Color c) {
    fg = c;
    return this;
  }
  
  
  public TextStyle setBackground(Color c) {
    bg = c;
    return this;
  }
  
  
  public TextStyle setFontAttr(FontAttr f) {
    font = f;
    return this;
  }
  
  
  public Color getForeground() {
    return fg;
  }
  
  
  public Color getBackground() {
    return bg;
  }
  
  
  public FontAttr getFontAttr() {
    return font;
  }
  
  
  public TextStyle setFontSize(int size) {
    if(font == null)
      font = new FontAttr();
    font.setSize(size);
    return this;
  }
  
  
  public TextStyle setFontBold(boolean bool) {
    if(font == null)
      font = new FontAttr();
    font.setBold(bool);
    return this;
  }
  
  
  public TextStyle setFontItalic(boolean bool) {
    if(font == null)
      font = new FontAttr();
    font.setItalic(bool);
    return this;
  }
  
  
  public TextStyle setFontUnderline(boolean bool) {
    if(font == null)
      font = new FontAttr();
    font.setUnderline(bool);
    return this;
  }
  
  
  public TextStyle setFontFamily(String family) {
    if(font == null)
      font = new FontAttr();
    font.setFontFamily(family);
    return this;
  }
  
  
  public static void clearStyles(JEditorPane jep) {
    if(jep == null) return;
    Document d = jep.getDocument();
    if(!StyledDocument.class
        .isAssignableFrom(d.getClass()))
      return;
    StyledDocument doc = (StyledDocument) d;
    AttributeSet set = SimpleAttributeSet.EMPTY; 
    doc.setCharacterAttributes(0, 
        doc.getLength(), set, true);
  }
  
  
  private AttributeSet setFontAttributes(AttributeSet set) {
    if(set == null) return set;
    StyleContext cont = StyleContext.getDefaultStyleContext();
    if(font != null) {
      if(font.getFontFamily() != null) {
        set = cont.addAttribute(set, 
            StyleConstants.FontFamily, font.getFontFamily());
      }
      if(font.getSize() > 0) {
        set = cont.addAttribute(set, 
            StyleConstants.FontSize, font.getSize());
      }
      set = cont.addAttribute(set, 
          StyleConstants.Bold, 
          Boolean.valueOf(font.isBold()));
      set = cont.addAttribute(set, 
          StyleConstants.Italic, 
          Boolean.valueOf(font.isItalic()));
      set = cont.addAttribute(set, 
          StyleConstants.Underline, 
          Boolean.valueOf(font.isUnderline()));
    }
    return set;
  }
  
  
  private AttributeSet setColorAttributes(AttributeSet set) {
    if(set == null) return set;
    StyleContext cont = StyleContext.getDefaultStyleContext();
    if(fg != null) {
      set = cont.addAttribute(set,
        StyleConstants.Foreground, fg);
    }
    if(bg != null) {
      set = cont.addAttribute(set, 
          StyleConstants.Background, bg);
    }
    return set;
  }
  
  
  public TextStyle apply(int off, int len, JEditorPane jep) {
    if(off < 0 || len < 1 || jep == null)
      throw new IllegalArgumentException(
          "Invalid parameters [off="+ off
              + ", len="+ len+ ", jep="+ jep+ "]");
    
    AttributeSet attr = new SimpleAttributeSet();
    attr = setColorAttributes(attr);
    attr = setFontAttributes(attr);
    Document d = jep.getDocument();
    if(!StyledDocument.class
        .isAssignableFrom(d.getClass()))
      throw new IllegalStateException(
          "Invalid Document type: "
              + d.getClass().getSimpleName());
    
    StyledDocument doc = (StyledDocument) d;
    doc.setCharacterAttributes(off, len, attr, true);
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.fg);
    hash = 89 * hash + Objects.hashCode(this.bg);
    hash = 89 * hash + Objects.hashCode(this.font);
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
    final TextStyle other = (TextStyle) obj;
    if (!Objects.equals(this.fg, other.fg)) {
      return false;
    }
    if (!Objects.equals(this.bg, other.bg)) {
      return false;
    }
    if (!Objects.equals(this.font, other.font)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "TextStyle{" + "fg=" + fg + ", bg=" + bg + ", font=" + font + '}';
  }
  
}
