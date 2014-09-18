/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.imgc;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;


/**
 * PowerImage apresenta funções utilitárias
 * para manipulação de imagens.
 * @author Juno
 */
public class PowerImage
{

  private BufferedImage bufimg;

  private ImageObserver imgob;

  private int width, height;


  /**
   * Construtor padr�o, recebe o nome e o caminho
   * da imagem representada.
   * @param filename Nome e caminho da imagem representada.
   */
  public PowerImage(String filename) throws IOException
  {
		this(new File(filename));
		init();
  }


  /**
   * Construtor padr�o, recebe o nome e o caminho
   * da imagem representada.
   * @param imgfile Caminho da imagem representada.
   */
  public PowerImage(URL imgfile) throws IOException
  {
		bufimg = ImageIO.read(imgfile);
		init();
  }


  public PowerImage(File f) throws IOException
  {
    bufimg = ImageIO.read(f);
		init();
  }


  public PowerImage(BufferedImage bufimg)
  {
		this.bufimg = bufimg;
		init();
  }


  private void init()
  {
    width = bufimg.getWidth(imgob);
    height = bufimg.getHeight(imgob);
  }
	
	
	public static PowerImage getInstance(String filename) {
		try {
			return new PowerImage(filename);
		} catch(IOException ex) {
			return null;
		}
	}


	public static PowerImage getInstance(File file) {
		try {
			return new PowerImage(file);
		} catch(IOException ex) {
			return null;
		}
	}


	public static PowerImage getInstance(URL image) {
		try {
			return new PowerImage(image);
		} catch(IOException ex) {
			return null;
		}
	}



  /**
   * M�todo est�tico para criar um BufferedImage a
   * partir da imagem informada.
   * @param img Imagem fonte.
   * @return java.awt.image.BufferedImage.
   */
  public static BufferedImage createBufferedImage(Image img)
  {
    if(img == null)
      throw new IllegalArgumentException("Image must be not null");

    BufferedImage big = new BufferedImage(
            img.getWidth(null),
            img.getHeight(null),
            BufferedImage.TRANSLUCENT);
    Graphics g = big.getGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();
    return big;
  }

	
  /**
   * Retorna um BufferedImage da imagem
   * representada por PowerImage
   * @return java.awt.image.BufferedImage.
   */
  public BufferedImage getBufferedImage()
  {
    return bufimg;
  }

	
  /**
   * Retorna a imagem representada por PowerImage.
   * @return java.awt.Image.
   */
  public Image getImage()
  {
    return bufimg;
  }

	
  /**
   * Retorna a largura da imagem.
   * @return Image Width.
   */
  public int getWidth()
  {
    return width;
  }

	
  /**
   * Retorna a altura da imagem.
   * @return Image Height.
   */
  public int getHeight()
  {
    return height;
  }

	
  public void rotate(int degrees)
  {
    bufimg = PowerImage.rotate(bufimg, degrees);
  }

	
  public static BufferedImage rotate(Image img, int degrees)
  {
    BufferedImage big = PowerImage.createBufferedImage(img);

    AffineTransform tx = AffineTransform.getRotateInstance(
      Math.toRadians(degrees),
        big.getWidth()/2., big.getHeight()/2.);

		BufferedImage rotated = new BufferedImage(
				img.getWidth(null), img.getHeight(null), 
				BufferedImage.TRANSLUCENT);
		
		Graphics2D g = rotated.createGraphics();
		g.drawImage(img, tx, null);
		g.dispose();
		
		return rotated;
  }

	
  public static BufferedImage scale(Image toScale, int width, int height)
  {
    if(toScale == null)
      throw new IllegalArgumentException("Image must be not null.");
    if(width <= 0 || height <= 0)
      throw new IllegalArgumentException("Invalid size: {width: "+
              width+ ", height: "+ height+ "}");

    BufferedImage big = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) big.getGraphics();

    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(toScale, 0, 0, width, height, null);
    g.dispose();

    return big;
  }

  public void scale(int width, int height)
  {
    bufimg = PowerImage.scale(bufimg, width, height);
  }


  public boolean blur(int size)
  {
		BufferedImage blur = blur(bufimg, size);
		if(blur == null) return false;
		
		bufimg = blur;
		return true;
  }


  public static BufferedImage blur(BufferedImage toBlur, int size)
  {
    if(size < 3) return null;

    float[] filter = new float[size * size];

    for(int i = 0; i < size * size; i++)
    {
      filter[i] = 1f / (float) (size * size);
    }

    Kernel k = new Kernel(size, size, filter);

    BufferedImageOp op = new ConvolveOp(k);
    return op.filter(toBlur, null);
  }


}
