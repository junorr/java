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

package us.pserver.imgc;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/12/2012
 */
public class ImageCompareOld {
  
  public static final int DEFAULT_IMAGE_RESIZE = 100;
  
  public static final double DEFAULT_PRECISION = 0.8;
  
  public static final double SEARCH_SCALE = 0.25;
  
  
  private int imgresize;
  
  private double precision;
  
  
  public ImageCompareOld() {
    imgresize = DEFAULT_IMAGE_RESIZE;
    precision = DEFAULT_PRECISION;
  }
  
  
  public ImageCompareOld(int imageResize, double pixelPrecision) {
    imgresize = imageResize;
    setPixelPrecision(pixelPrecision);
  }


  public int getImageResize() {
    return imgresize;
  }


  public ImageCompareOld setImageResize(int imgresize) {
    this.imgresize = imgresize;
    return this;
  }


  public double getPixelPrecision() {
    return precision;
  }


  public ImageCompareOld setPixelPrecision(double pixelPrecision) {
    if(pixelPrecision < 0.001 || pixelPrecision > 1.0)
      throw new IllegalArgumentException(
          "Invalid precision ["+ pixelPrecision+ "]. Valid range {0.001 - 1.0}");
    this.precision = pixelPrecision;
    return this;
  }
  
  
  public BufferedImage resize(BufferedImage img) {
    if(img == null) return img;
    PowerImage pi = new PowerImage(img);
    pi.scale(imgresize, imgresize);
    return pi.getBufferedImage();
  }

  
  private BufferedImage scale(BufferedImage img, int width, int height) {
    if(img == null) return img;
    PowerImage pi = new PowerImage(img);
    pi.scale(width, height);
    return pi.getBufferedImage();
  }

  
  private boolean isInPrecision(int orig, int comp) {
    int margin = (int) Math.round(orig * (1.0 - precision));
    return orig == comp || (comp >= (orig-margin) 
        && comp <= (orig+margin));
  }
  
  
  public void getPixels(BufferedImage img, int[] pixels) {
    if(pixels == null || pixels.length == 0 
        || img == null || pixels.length < img.getWidth()) 
      return;
    
    int my = pixels.length / img.getWidth();
    int ai = 0;
    
    for(int y = 0; y < my; y++) {
      for(int x = 0; x < img.getWidth(); x++) {
        pixels[ai++] = img.getRGB(x, y);
      }
    }
  }
  
  
  public double match(BufferedImage img1, BufferedImage img2) {
    if(img1 == null)
      throw new IllegalArgumentException(
          "Invalid image 1 ["+ img1+ "]");
    if(img2 == null)
      throw new IllegalArgumentException(
          "Invalid image 2 ["+ img2+ "]");
    
    img1 = resize(img1);
    img2 = resize(img2);
    double matches = 0;
    double total = 0;
    
    int[] px1 = new int[img1.getWidth() * img1.getHeight()];
    getPixels(img1, px1);
    int[] px2 = new int[img2.getWidth() * img2.getHeight()];
    getPixels(img2, px2);
    
    for(int i = 0; i < Math.min(px1.length, px2.length); i++) {
      Color c1 = new Color(px1[i]);
      Color c2 = new Color(px2[i]);
      total++;
      if(isInPrecision(c1.getRed(), c2.getRed())
          && isInPrecision(c1.getGreen(), c2.getGreen())
          && isInPrecision(c1.getBlue(), c2.getBlue()))
        matches++;
    }
      
    return matches / total;
  }
  
  
  public double matchNoScale(BufferedImage img1, BufferedImage img2) {
    if(img1 == null)
      throw new IllegalArgumentException(
          "Invalid image 1 ["+ img1+ "]");
    if(img2 == null)
      throw new IllegalArgumentException(
          "Invalid image 2 ["+ img2+ "]");
    
    double matches = 0;
    double total = 0;
    
    int[] px1 = new int[img1.getWidth() * img1.getHeight()];
    getPixels(img1, px1);
    int[] px2 = new int[img2.getWidth() * img2.getHeight()];
    getPixels(img2, px2);
    
    for(int i = 0; i < Math.min(px1.length, px2.length); i++) {
      Color c1 = new Color(px1[i]);
      Color c2 = new Color(px2[i]);
      total++;
      if(isInPrecision(c1.getRed(), c2.getRed())
          && isInPrecision(c1.getGreen(), c2.getGreen())
          && isInPrecision(c1.getBlue(), c2.getBlue()))
        matches++;
    }
      
    return matches / total;
  }
  
  
  public double match(PowerImage img1, PowerImage img2) { 
    if(img1 == null)
      throw new IllegalArgumentException(
          "Invalid image 1 ["+ img1+ "]");
    if(img2 == null)
      throw new IllegalArgumentException(
          "Invalid image 2 ["+ img2+ "]");
    
    img1.scale(imgresize, imgresize);
    img2.scale(imgresize, imgresize);
    return match(img1.getBufferedImage(), img2.getBufferedImage());
  }
  
  
  public Rectangle search(BufferedImage source, BufferedImage search, double matchPercent) {
    if(source == null)
      throw new IllegalArgumentException(
          "Invalid source image ["+ source+ "]");
    if(search == null)
      throw new IllegalArgumentException(
          "Invalid source image ["+ search+ "]");
    if(source.getWidth() <= search.getWidth()
        || source.getHeight() <= search.getHeight())
      throw new IllegalArgumentException(
          "Source image size <= search image size");
    if(matchPercent < 0.001 || matchPercent > 1.0)
      throw new IllegalArgumentException(
          "Invalid match percent ["+ matchPercent+ "]. Valid range {0.001 - 1.0}");
    
    int width = (int) Math.round(source.getWidth() * SEARCH_SCALE);
    int height = (int) Math.round(source.getHeight() * SEARCH_SCALE);
    source = this.scale(source, width, height);
    try {
      ImageIO.write(source, "png", new File("f:/source.png"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    width = (int) Math.round(search.getWidth() * SEARCH_SCALE);
    height = (int) Math.round(search.getHeight() * SEARCH_SCALE);
    search = this.scale(search, width, height);
    try {
      ImageIO.write(search, "png", new File("f:/search.png"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    
    for(int x = 0; x < source.getWidth()-width; x += 1) {
      for(int y = 0; y < source.getHeight()-height; y += 1) {
        BufferedImage part = source.getSubimage(x, y, width, height);
        double percent = this.matchNoScale(part, search);
        if(percent >= 0.3) {
          System.out.println("* percent="+ percent);
          System.out.println("  "+ new Rectangle(x, y, width, height));
          try {
            //ImageIO.write(part, "png", new File("f:/match_"+ x+ ".png"));
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        if(percent >= matchPercent) {
          double mult = 1 / SEARCH_SCALE;
          System.out.println("* found="+ percent);
          try {
            ImageIO.write(part, "png", new File("f:/found.png"));
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          System.out.println("x="+ x+ ", y="+ y);
          return new Rectangle(
              (int) Math.round(mult * x), 
              (int) Math.round(mult * y), 
              (int) Math.round(mult * width), 
              (int) Math.round(mult * height));
        }
      }
    }
    return null;
  }
  
  
  public static void main(String[] args) throws IOException, AWTException {
    ImageCompareOld ic = new ImageCompareOld()
        .setPixelPrecision(0.8);
    Robot rb = new Robot();
    
    Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle r = new Rectangle(0, 0, sd.width, sd.height);
    BufferedImage screen = rb.createScreenCapture(r);
    
    PowerImage icon = new PowerImage("F:/img11.jpg");
    System.out.println("* search( screen, icon, 0.8 ): "
        + ic.search(screen, icon.getBufferedImage(), 0.8));
  }
  
}
