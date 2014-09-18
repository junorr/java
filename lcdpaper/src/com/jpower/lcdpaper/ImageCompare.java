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
import java.awt.image.BufferedImage;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/12/2012
 */
public class ImageCompare {

  public static double match(BufferedImage img1, BufferedImage img2, double pixelPrecision) {
    if(img1 == null || img2 == null) return 0;
    
    double matches = 0;
    double total = 0;
    
    int[] px1 = new int[img1.getWidth() * img1.getHeight()];
    getPixels(img1, px1);
    int[] px2 = new int[img2.getWidth() * img2.getHeight()];
    getPixels(img2, px2);
    
    for(int i = 0; i < Math.min(px1.length, px2.length); i++) {
      Color c1 = new Color(px1[i]);
      Color c2 = new Color(px2[i]);
      
      int r = c2.getRed();
      int g = c2.getGreen();
      int b = c2.getBlue();
      
      int margin = (int) (c1.getRed() * (1.0 - pixelPrecision));
      if(margin == 0) margin = (int) (100 * (1.0 - pixelPrecision));
      int rmin = c1.getRed() - margin;
      int rmax = c1.getRed() + margin;
      
      margin = (int) (c1.getGreen() * (1.0 - pixelPrecision));
      if(margin == 0) margin = (int) (100 * (1.0 - pixelPrecision));
      int gmin = c1.getGreen() - margin;
      int gmax = c1.getGreen() + margin;
      
      margin = (int) (c1.getBlue() * (1.0 - pixelPrecision));
      if(margin == 0) margin = (int) (100 * (1.0 - pixelPrecision));
      int bmin = c1.getBlue() - margin;
      int bmax = c1.getBlue() + margin;
      
      total++;
      
      /*
      System.out.print("r="+r);
      System.out.print(", val="+ c1.getRed());
      System.out.print(", rmin="+ rmin);
      System.out.println(", rmax="+ rmax);
      
      System.out.print("g="+g);
      System.out.print(", val="+ c1.getGreen());
      System.out.print(", gmin="+ gmin);
      System.out.println(", gmax="+ gmax);
      
      System.out.print("b="+b);
      System.out.print(", val="+ c1.getBlue());
      System.out.print(", bmin="+ bmin);
      System.out.println(", bmax="+ bmax);
      */
      
      if(r == c1.getRed() || (r > rmin && r < rmax
          && g > gmin && g < gmax
          && b > bmin && b < bmax))
        matches++;
    }
    
    System.out.println("matches="+matches);
    System.out.println("total="+total);
    System.out.println("accuracy: "+ (matches / total) + "%");
    
    return matches / total;
  }
  
  
  public static void getPixels(BufferedImage img, int[] array) {
    if(array == null || array.length == 0 
        || img == null || array.length < img.getWidth()) 
      return;
    
    int my = array.length / img.getWidth();
    int ai = 0;
    
    for(int y = 0; y < my; y++) {
      for(int x = 0; x < img.getWidth(); x++) {
        array[ai++] = img.getRGB(x, y);
      }
    }
  }
  
  
  public static void main(String[] args) {
    BufferedImage img1 = new BufferedImage(3, 1, BufferedImage.TYPE_INT_ARGB);
    BufferedImage img2 = new BufferedImage(3, 1, BufferedImage.TYPE_INT_ARGB);
    
    img1.setRGB(0, 0, Color.BLUE.getRGB());
    img1.setRGB(1, 0, Color.BLACK.getRGB());
    img1.setRGB(2, 0, Color.WHITE.getRGB());
    
    img2.setRGB(0, 0, Color.BLUE.getRGB());
    img2.setRGB(1, 0, Color.DARK_GRAY.getRGB());
    img2.setRGB(2, 0, Color.WHITE.getRGB());
    
    System.out.println("match: "+ match(img1, img2, 0.8));
  }
  
}
