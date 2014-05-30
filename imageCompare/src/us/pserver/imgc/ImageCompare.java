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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 20/02/2014
 */
public class ImageCompare {
  
  public static final int IMAGE_SIZE = 300;
  
  public static final int NUM_AREAS = 100;
  
  public static final int CENTER_MIN = 1;
  
  public static final int CENTER_MAX = 8;
  
  public static final int AREAS_SIZE = 30;
  
  public static final int AREAS_SPACE = 0;
  
  public static final double CENTER_WEIGHT = 3;
  
  public static final double SIDE_WEIGHT = 1;
  
  public static final double PERC_NUM_AREAS = 0.33;
  
  public static final double MAX_DISTANCE = getMaxDistance();
  
  public static final Rectangle[] AREAS = createAreas();

  
  private static double getMaxDistance() {
    return 36*CENTER_WEIGHT*Math.sqrt(255*255+255*255+255*255)
        +64*SIDE_WEIGHT*Math.sqrt(255*255+255*255+255*255);
  }
  
  
  private static Rectangle[] createAreas() {
    Rectangle[] areas = new Rectangle[NUM_AREAS];
    int idx = 0;
    int inc = AREAS_SIZE + AREAS_SPACE;
    int max = IMAGE_SIZE - inc;
    for(int x = AREAS_SPACE; x <= max; x += inc) {
      for(int y = AREAS_SPACE; y <= max; y += inc) {
        areas[idx++] = new Rectangle(x, y, 
            AREAS_SIZE, AREAS_SIZE);
      }
    }
    return areas;
  }
  
  
  private static Color calcRectAverage(BufferedImage img, Rectangle rect) {
    int r = 0, g = 0, b = 0;
    for(int i = rect.x; i < rect.width+rect.x; i++) {
      for(int j = rect.y; j < rect.height+rect.y; j++) {
        Color c = new Color(img.getRGB(i, j));
        r += c.getRed();
        g += c.getGreen();
        b += c.getBlue();
      }
    }
    int size = rect.width * rect.height;
    return new Color(r/size, g/size, b/size);
  }
  
  
  private static Color[] calcSignature(BufferedImage img) {
    Color[] sign = new Color[NUM_AREAS];
    for(int i = 0; i < NUM_AREAS; i++) {
      sign[i] = calcRectAverage(img, AREAS[i]);
    }
    return sign;
  }
  
  
  private static double calcDistance(Color c1, Color c2) {
    double r = Math.pow((c1.getRed() - c2.getRed()), 2);
    double g = Math.pow((c1.getGreen() - c2.getGreen()), 2);
    double b = Math.pow((c1.getBlue() - c2.getBlue()), 2);
    return Math.sqrt((r + g + b));
  }
  
  
  private static Point matrixPos(int pos) {
    return new Point(pos % 5, pos / 5);
  }
  
  
  private static boolean isInCenter(Point p) {
    return p.x > CENTER_MIN && p.x < CENTER_MAX 
        && p.y > CENTER_MIN && p.y < CENTER_MAX;
  }
  
  
  private static double distance(BufferedImage img1, BufferedImage img2) {
    Color[] sign1 = calcSignature(img1);
    Color[] sign2 = calcSignature(img2);
    double dist = 0;
    for(int i = 0; i < NUM_AREAS; i++) {
      double tmp = calcDistance(sign1[i], sign2[i]);
      if(isInCenter(matrixPos(i)))
        tmp *= CENTER_WEIGHT;
      else
        tmp *= SIDE_WEIGHT;
      dist += tmp;
    }
    return dist;
  }
  
  
  public static double compare(BufferedImage img1, BufferedImage img2) {
    if(img1 == null)
      throw new IllegalArgumentException(
          "Invalid image 1 ["+ img1+ "]");
    if(img2 == null)
      throw new IllegalArgumentException(
          "Invalid image 2 ["+ img2+ "]");
    
    PowerImage pi1 = new PowerImage(img1);
    PowerImage pi2 = new PowerImage(img2);
    pi1.scale(IMAGE_SIZE, IMAGE_SIZE);
    pi2.scale(IMAGE_SIZE, IMAGE_SIZE);
    return (MAX_DISTANCE - distance(img1, img2)) / MAX_DISTANCE;
  }
  
  
  public static double compareNoScale(BufferedImage img1, BufferedImage img2) {
    if(img1 == null)
      throw new IllegalArgumentException(
          "Invalid image 1 ["+ img1+ "]");
    if(img2 == null)
      throw new IllegalArgumentException(
          "Invalid image 2 ["+ img2+ "]");
    
    PowerImage pi1 = new PowerImage(img1);
    PowerImage pi2 = new PowerImage(img2);
    pi1.scale(IMAGE_SIZE, IMAGE_SIZE);
    pi2.scale(IMAGE_SIZE, IMAGE_SIZE);
    return (MAX_DISTANCE - distance(img1, img2)) / MAX_DISTANCE;
  }
  
  
  public static double compare(PowerImage img1, PowerImage img2) {
    if(img1 == null)
      throw new IllegalArgumentException(
          "Invalid image 1 ["+ img1+ "]");
    if(img2 == null)
      throw new IllegalArgumentException(
          "Invalid image 2 ["+ img2+ "]");
    
    img1.scale(IMAGE_SIZE, IMAGE_SIZE);
    img2.scale(IMAGE_SIZE, IMAGE_SIZE);
    double dist = distance(img1.getBufferedImage(), 
        img2.getBufferedImage());
    System.out.println("  distance = "+ dist);
    return (MAX_DISTANCE - dist) / MAX_DISTANCE;
  }
  
  
  public static void main(String[] args) throws IOException, AWTException {
    System.out.println("* compare(img1, img1) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img1.jpg")));
    System.out.println("* compare(img1, img2) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img2.jpg")));
    System.out.println("* compare(img1, img3) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img3.jpg")));
    System.out.println("* compare(img1, img4) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img4.jpg")));
    System.out.println("* compare(img1, img5) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img5.jpg")));
    System.out.println("* compare(img1, img6) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img6.jpg")));
    System.out.println("* compare(img1, lock_64) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/lock_64.png")));
    System.out.println("* compare(img1, img7) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img7.jpg")));
    System.out.println("* compare(img1, img8) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img8.jpg")));
    System.out.println("* compare(img1, img9) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img9.jpg")));
    System.out.println("* compare(img1, img10) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img10.jpg")));
    System.out.println("* compare(img1, img11) = "+ compare(new PowerImage("F:/img1.jpg"), new PowerImage("F:/img11.jpg")));
    
    /*
    System.out.println("* compare(img1, img1) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img1.jpg")));
    System.out.println("* compare(img1, img2) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("g:/img2.jpg")));
    System.out.println("* compare(img1, img3) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img3.jpg")));
    System.out.println("* compare(img1, img4) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img4.jpg")));
    System.out.println("* compare(img1, img5) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img5.jpg")));
    System.out.println("* compare(img1, img6) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img6.jpg")));
    System.out.println("* compare(img1, lock_64) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/lock_64.png")));
    System.out.println("* compare(img1, img7) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img7.jpg")));
    System.out.println("* compare(img1, img8) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img8.jpg")));
    System.out.println("* compare(img1, img9) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img9.jpg")));
    System.out.println("* compare(img1, img10) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img10.jpg")));
    System.out.println("* compare(img1, img11) = "+ compare(new PowerImage("G:/img1.jpg"), new PowerImage("G:/img11.jpg")));
    */
  }
  
}
