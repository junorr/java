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

import com.thoughtworks.xstream.XStream;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static us.pserver.coder.Highlighter.COLOR;
import static us.pserver.coder.Highlighter.FONT;
import static us.pserver.coder.Highlighter.MATCH;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/09/2014
 */
public class CodetermConfig {
  
  public static final String CONF_FILE = "./codeterm.xml";

  
  private Color text, textBG, 
      textSelect, lines, linesBG, 
      status, statusBG, statusWarn;
  
  private FontXml textFont;
  
  private FontXml statusFont;
  
  private Rectangle position;
  
  private transient XStream xstream;

  
  public CodetermConfig() {
    xstream = new XStream();
    xstream.alias(MATCH, Match.class);
    xstream.alias(COLOR, Color.class);
    xstream.alias(FONT, FontXml.class);
    xstream.registerConverter(new ColorConverter());
    xstream.registerConverter(new FontAttrConverter());
  }
  
  
  public boolean fileExists() {
    Path p = Paths.get(CONF_FILE);
    return Files.exists(p);
  }
  
  
  public Exception save() {
    try (
        FileOutputStream fos = 
            new FileOutputStream(CONF_FILE);
        ) {
      xstream.toXML(this, fos);
    } 
    catch(IOException e) {
      return e;
    }
    return null;
  }


  public Exception load() {
    try (
        FileInputStream fis = 
            new FileInputStream(CONF_FILE);
        ) {
      CodetermConfig cc = (CodetermConfig) xstream.fromXML(fis);
      this.setLinesBgColor(cc.getLinesBgColor());
      this.setLinesColor(cc.getLinesColor());
      this.setPosition(cc.getPosition());
      statusFont = cc.statusFont;
      this.setStatusColor(cc.getStatusColor());
      this.setStatusBgColor(cc.getStatusBgColor());
      this.setStatusWarnColor(cc.getStatusWarnColor());
      textFont = cc.textFont;
      this.setTextBgColor(cc.getTextBgColor());
      this.setTextColor(cc.getTextColor());
      this.setTextSelectionColor(cc.getTextSelectionColor());
    } 
    catch(IOException e) {
      return e;
    }
    return null;
  }


  public Color getTextColor() {
    return text;
  }


  public void setTextColor(Color text) {
    this.text = text;
  }


  public Color getLinesColor() {
    return lines;
  }


  public void setLinesColor(Color lines) {
    this.lines = lines;
  }


  public Color getStatusColor() {
    return status;
  }


  public void setStatusColor(Color status) {
    this.status = status;
  }


  public Font getTextFont() {
    if(textFont == null)
      return null;
    return textFont.getFont();
  }


  public void setTextFont(Font f) {
    textFont = new FontXml().setFont(f);
  }


  public Color getTextBgColor() {
    return textBG;
  }


  public Color getTextSelectionColor() {
    return textSelect;
  }


  public void setTextSelectionColor(Color text_select) {
    this.textSelect = text_select;
  }


  public void setTextBgColor(Color text_bg) {
    this.textBG = text_bg;
  }


  public Color getLinesBgColor() {
    return linesBG;
  }


  public void setLinesBgColor(Color lines_bg) {
    this.linesBG = lines_bg;
  }


  public Font getStatusFont() {
    if(statusFont == null)
      return null;
    return statusFont.getFont();
  }


  public void setStatusFont(Font f) {
    statusFont = new FontXml().setFont(f);
  }


  public Color getStatusBgColor() {
    return statusBG;
  }


  public void setStatusBgColor(Color status_bg) {
    this.statusBG = status_bg;
  }


  public Color getStatusWarnColor() {
    return statusWarn;
  }


  public void setStatusWarnColor(Color status_warn) {
    this.statusWarn = status_warn;
  }


  public Rectangle getPosition() {
    return position;
  }


  public void setPosition(Rectangle position) {
    this.position = position;
  }
  
}
