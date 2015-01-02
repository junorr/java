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

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/09/2014
 */
public abstract class IconProvider {

  public static final String
      RES_PATH = "/us/pserver/coder/images/",
      
      ICON_CANCEL = "cancel-white-20.png",
      ICON_CHECK = "check-white-20.png",
      ICON_CLOSE = "close-16.png",
      ICON_CODETERM = "codeterm-24x20.png",
      ICON_COPY = "copy-white-20.png",
      ICON_FONT_GRAY = "font-gray-20.png",
      ICON_FONT_WHITE = "font-white-20.png",
      ICON_GEAR_GRAY = "gear-gray-20.png",
      ICON_GEAR_WHITE = "gear-white-20.png",
      ICON_OPEN_GRAY = "open-gray-20.png",
      ICON_OPEN_WHITE = "open-white-20.png",
      ICON_PASTE = "paste-white-20.png",
      ICON_REDO = "redo-white-20.png",
      ICON_SAVE_GRAY = "save-gray-20.png",
      ICON_SAVE_WHITE = "save-white-20.png",
      ICON_SEARCH_GRAY = "search-gray-20.png",
      ICON_SEARCH_WHITE = "search-white-20.png",
      ICON_UNDO = "undo-white-20.png";
  
  
  public static Image getIconImage(String name) {
    return new ImageIcon(IconProvider.class.getClass()
        .getResource(RES_PATH + name)).getImage();
  }
  
  
  public static Image getIconCancel() {
    return getIconImage(ICON_CANCEL);
  }
  
  
  public static Image getIconCheck() {
    return getIconImage(ICON_CHECK);
  }
  
  
  public static Image getIconClose() {
    return getIconImage(ICON_CLOSE);
  }
  
  
  public static Image getIconCodeterm() {
    return getIconImage(ICON_CODETERM);
  }
  
  
  public static Image getIconCopy() {
    return getIconImage(ICON_COPY);
  }
  
  
  public static Image getIconFontGray() {
    return getIconImage(ICON_FONT_GRAY);
  }
  
  
  public static Image getIconFontWhite() {
    return getIconImage(ICON_FONT_WHITE);
  }
  
  
  public static Image getIconGearGray() {
    return getIconImage(ICON_GEAR_GRAY);
  }
  
  
  public static Image getIconGearWhite() {
    return getIconImage(ICON_GEAR_WHITE);
  }
  
  
  public static Image getIconOpenGray() {
    return getIconImage(ICON_OPEN_GRAY);
  }
  
  
  public static Image getIconOpenWhite() {
    return getIconImage(ICON_OPEN_WHITE);
  }
  
  
  public static Image getIconPaste() {
    return getIconImage(ICON_PASTE);
  }
  
  
  public static Image getIconRedo() {
    return getIconImage(ICON_REDO);
  }
  
  
  public static Image getIconSaveGray() {
    return getIconImage(ICON_SAVE_GRAY);
  }
  
  
  public static Image getIconSaveWhite() {
    return getIconImage(ICON_SAVE_WHITE);
  }
  
  
  public static Image getIconSearchGray() {
    return getIconImage(ICON_SEARCH_GRAY);
  }
  
  
  public static Image getIconSearchWhite() {
    return getIconImage(ICON_SEARCH_WHITE);
  }
  
  
  public static Image getIconUndo() {
    return getIconImage(ICON_UNDO);
  }
  
}
