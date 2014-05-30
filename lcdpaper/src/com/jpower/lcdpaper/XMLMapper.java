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

package com.jpower.lcdpaper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/12/2012
 */
public class XMLMapper {

  private String text;
  
  private Font font;
  
  private Color background;
  
  private Color foreground;
  
  private Rectangle bounds;
  
  private boolean selected;
  
  private String className;
  
  
  public XMLMapper(LCDComponent c) {
    if(c == null) throw new 
        IllegalArgumentException(
        "Invalid TextComponent: "+ c);
    
    text = c.getText();
    font = c.getTextFont();
    background = c.getTextBackground();
    foreground = c.getTextColor();
    bounds = c.getBounds();
    className = c.getClass().getSimpleName();
    
    selected = false;
    if(c instanceof CheckBoxPanel)
      selected = ((CheckBoxPanel) c).isSelected();
  }
  
  
  public Component toComponent() {
    if(className.equals("TextPanel")) {
      TextPanel c = new TextPanel();
      c.setText(text);
      c.setTextFont(font);
      c.setTextBackground(background);
      c.setTextColor(foreground);
      c.setBounds(bounds);
      return c;
    }
    else if(className.equals("CheckBoxPanel")) {
      CheckBoxPanel c = new CheckBoxPanel();
      c.setText(text);
      c.setTextFont(font);
      c.setTextBackground(background);
      c.setTextColor(foreground);
      c.setBounds(bounds);
      c.setSelected(selected);
      return c;
    }
    return null;
  }


  public String getText() {
    return text;
  }


  public void setText(String text) {
    this.text = text;
  }


  public Font getFont() {
    return font;
  }


  public void setFont(Font font) {
    this.font = font;
  }


  public Color getBackground() {
    return background;
  }


  public void setBackground(Color background) {
    this.background = background;
  }


  public Color getForeground() {
    return foreground;
  }


  public void setForeground(Color foreground) {
    this.foreground = foreground;
  }


  public Rectangle getBounds() {
    return bounds;
  }


  public void setBounds(Rectangle bounds) {
    this.bounds = bounds;
  }


  public boolean isSelected() {
    return selected;
  }


  public void setSelected(boolean selected) {
    this.selected = selected;
  }
  
}
