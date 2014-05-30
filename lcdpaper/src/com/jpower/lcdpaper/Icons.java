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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.lcdpaper;

import java.awt.Image;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/12/2012
 */
public abstract class Icons {
  
  public static final String
      
      BACKGROUND_ICON = "icons/background-icon-18.png",
      
      COLOR_ICON = "icons/color-icon-18.png",
      
      ERASER_ICON = "icons/eraser-icon-18.png",
      
      ERASER_DRAW = "icons/eraser-icon-200.png",
      
      EXIT_ICON = "icons/exit-icon-18.png",
      
      FREE_DRAW = "icons/free-draw-icon-18.png",
      
      LCDPAPER_ICON = "icons/lcd-mix-icon-48.png",
      
      FONT_ICON = "icons/font-icon-18.png",
      
      LINE_DRAW = "icons/line-draw-icon-18.png",
      
      OVAL_DRAW = "icons/oval-draw-icon-18.png",
      
      OVAL_FILL = "icons/oval-fill-icon-18.png",
      
      PAPER_ICON = "icons/paper-icon-18.png",
  
      RECT_DRAW = "icons/rect-draw-icon-18.png",
  
      RECT_FILL = "icons/rect-fill-icon-18.png",
      
      SAVE_ICON = "icons/save-icon-18.png",
  
      OPEN_ICON = "icons/open-new-icon-18.png",
  
      TRI_DRAW = "icons/tri-draw-icon-18.png",
  
      TRI_FILL = "icons/tri-fill-icon-18.png",
  
      ARROW_DRAW = "icons/arrow-draw-icon-18.png",
  
      ARROW_FILL = "icons/arrow-fill-icon-18.png",
      
      GEAR_ICON = "icons/gear-icon-18.png",
      
      TRASH_ICON = "icons/trash-icon-18.png",
      
      CHECK_DRAW = "icons/check-icon-64.png",
      
      WORLD_ICON = "icons/world-icon-18.png",
      
      CHECK_ICON = "icons/check-icon-18.png";
  
  
  public static Icon getIcon(String path) {
    if(path == null) return null;
    URL url = Icons.class.getResource(path);
    if(url == null) return null;
    return new ImageIcon(url);
  }

  
  public static Image getImage(String path) {
    if(path == null) return null;
    URL url = Icons.class.getResource(path);
    if(url == null) return null;
    return new ImageIcon(url).getImage();
  }

}
