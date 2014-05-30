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

package com.jpower.jremote;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 18/09/2012
 */
public class Shooter {
  
  public static final String IMAGE_FORMAT_JPEG = "jpg";
  
  
  private Robot robot;
  
  private ByteArrayOutputStream buffer;
  
  private Rectangle screenBounds;
  
  
  public Shooter() {
    try {
      buffer = new ByteArrayOutputStream();
      robot = new Robot();
      screenBounds = GraphicsEnvironment
          .getLocalGraphicsEnvironment()
          .getDefaultScreenDevice()
          .getDefaultConfiguration()
          .getBounds();
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public BufferedImage shoot() {
    return robot.createScreenCapture(
        screenBounds);
  }
  
  
  public byte[] createScreenBytes() {
    try {
      ImageIO.write(this.shoot(), IMAGE_FORMAT_JPEG, buffer);
      byte[] bs = buffer.toByteArray();
      buffer.reset();
      return bs;
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    ImageIO.write(new Shooter().shoot(), IMAGE_FORMAT_JPEG, new File("./screen.jpg"));
  }
  
}
