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


import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 12/12/2012
 */
public class Screenshooter {
  
  private Robot bot;
  
  private Rectangle area;
  
  private BufferedImage lastScreen;
  
  
  public Screenshooter(Rectangle area) throws AWTException {
    if(area == null) throw new AWTException("Invalid area: "+ area);
    this.area = area;
    bot = new Robot();
    lastScreen = null;
  }
  
  
  public BufferedImage getScreenshot() {
    lastScreen = bot.createScreenCapture(area);
    return lastScreen;
  }
  
  
  public BufferedImage getLastScreen() {
    return lastScreen;
  }
  
  
  public Screenshooter setArea(Rectangle area) {
    if(area == null) 
      throw new IllegalArgumentException(
          "Invalid area: "+ area);
    this.area = area;
    return this;
  }
  
  
  public void saveLastScreen(String format, String filename) throws IOException {
    this.save(lastScreen, format, filename);
  }
  
  
  public void save(BufferedImage img, String format, String filename) throws IOException {
    ImageIO.write(img, format, new File(filename));
  }
  
  
  public BufferedImage load(String filename) {
    try {
      lastScreen = ImageIO.read(new File(filename));
    } catch(IOException ex) {
      lastScreen = null;
    }
    return lastScreen;
  }

}
