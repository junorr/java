package com.power.visual.controls;

import com.jpower.utils.PowerImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Juno Roesler
 */
public class AnimationPanel
    extends JPanel
    implements ActionListener
{

  private PowerImage[] imgs;

  private PowerImage current;

  private int imgIndex;

  private int delay;

  private int topMargin, leftMargin, bottonMargin, rightMargin;

  public static final int DEFAULT_DELAY = 120;

  private boolean resizeImage = false;

  private Timer swapTimer;


  public AnimationPanel()
  {
    imgs = null;
    current = null;
    imgIndex = 0;
    topMargin = leftMargin = 0;
    bottonMargin = rightMargin = 0;
    delay = DEFAULT_DELAY;
    swapTimer = new Timer(delay, this);
    swapTimer.setRepeats(true);
  }

  public AnimationPanel(String path, String prefix, int start, int end, String sufix)
      throws IllegalArgumentException, IOException
  {
    this();
    this.loadImages(path, prefix, start, end, sufix);
  }

  public int getDelay()
  {
    return delay;
  }

  public void setDelay(int d)
  {
    if(d <= 0) return;
    delay = d;
    if(swapTimer.isRunning())
      swapTimer.stop();
    swapTimer.setDelay(delay);
  }

  public void loadImages(String path, String prefix, int start, int end, String sufix)
      throws IllegalArgumentException, IOException {
		if(prefix == null || sufix == null)
			throw new IllegalArgumentException("Prefix/Sufix must be not null");
		if(start > end)
			throw new IllegalArgumentException("End index must be greater/equal start");
		
		StringBuilder imgfile = null;
		int qtd = end - start + 1;
		imgs = new PowerImage[qtd];
		int idx = 0;
		for(int i = start; i <= end; i++) {
			imgfile = new StringBuilder();
			if(path != null)
				imgfile.append(path);
			imgfile.append(prefix);
			imgfile.append(i);
			imgfile.append(sufix);
			imgfile.trimToSize();
			imgs[idx] = new PowerImage(imgfile.toString());
			idx++;
		}
		current = imgs[0];
  }

  public void setImages(PowerImage[] imgs)
  {
    this.imgs = imgs;
    imgIndex = 0;

    if(imgs != null)
      current = imgs[imgIndex];
  }

  public PowerImage[] getImages()
  {
    return imgs;
  }

  public PowerImage getCurrentImage()
  {
    return current;
  }

  public void setCurrentImage(PowerImage i)
  {
    current = i;
  }

  public int getCurrentIndex()
  {
    return imgIndex;
  }

  public boolean autoResizeImage()
  {
    return resizeImage;
  }

  public void setAutoResizeImage(boolean autoResize)
  {
    resizeImage = autoResize;
  }

  public int getTopMargin()
  {
    return topMargin;
  }

  public int getLeftMargin()
  {
    return leftMargin;
  }

  public void setTopMargin(int margin)
  {
    topMargin = margin;
  }

  public void setLeftMargin(int margin)
  {
    leftMargin = margin;
  }

  public int getBottonMargin()
  {
    return bottonMargin;
  }

  public int getRightMargin()
  {
    return rightMargin;
  }

  public void setBottonMargin(int margin)
  {
    bottonMargin = margin;
  }

  public void setRightMargin(int margin)
  {
    rightMargin = margin;
  }

  public void setMargins(int top, int botton, int left, int right)
  {
    topMargin = top;
    leftMargin = left;
    bottonMargin = botton;
    rightMargin = right;
  }

  private int nextIndex()
  {
    imgIndex++;
    if(imgIndex >= imgs.length)
      imgIndex = 0;
    return imgIndex;
  }


  public void start()
  {
    swapTimer.start();
  }

  public void stop()
  {
    swapTimer.stop();
  }

  public boolean isRunning()
  {
    return swapTimer.isRunning();
  }


  @Override
  public void actionPerformed(ActionEvent e)
  {
    current = imgs[this.nextIndex()];
    this.repaint();
  }


  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;

    int width = this.getWidth() - leftMargin - rightMargin;
    int height = this.getHeight() - topMargin - bottonMargin;

    if(width < 0 || height < 0) {
      this.drawMarginError(g2);
      return;
    }

    if(resizeImage)
      current.scale(width, height);

    BufferedImage buf = current.getBufferedImage();

    if(current.getWidth() > width || current.getHeight() > height)
      buf = this.drawSize(buf, width, height);

    g2.drawImage(buf, topMargin, leftMargin, null);
    this.getParent().repaint();
  }


  private BufferedImage drawSize(BufferedImage buf, int width, int height)
  {
    if(buf == null) return null;
    if(width < 0 || height < 0) return null;

    BufferedImage newbuf = new BufferedImage(width, height,
        BufferedImage.TRANSLUCENT);

    Graphics g = newbuf.getGraphics();
    g.drawImage(buf, 0, 0, null);
    g.dispose();
    return newbuf;
  }

  private void drawMarginError(Graphics2D g2)
  {
    g2.setFont(new Font("Monospaced", Font.BOLD + Font.ITALIC, 14));
    g2.setColor(Color.red);
    FontMetrics fm = g2.getFontMetrics();

    String error = "ERROR: WRONG MARGINS!";
    int x = this.getWidth() / 2 - fm.stringWidth(error) / 2;
    x = (x < 0 ? 2 : x);
    int y = this.getWidth() / 2 + fm.getHeight() - fm.getHeight() / 4;
    y = (y < 0 ? 2 : y);

    g2.drawString(error, x, y);
  }


  public static void main(String[] args) throws InterruptedException, IOException
  {
    JFrame f = new JFrame("Power Dice");
    f.setLayout(null);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLocation(300, 300);
    f.setSize(215, 195);

    final JLabel j = new JLabel();

    final AnimationPanel ap = new AnimationPanel();

		String path = ap.getClass().getResource("../images/dice1.PNG").getFile();
		path = path.substring(1, path.lastIndexOf("/")+1);
		//System.out.println(path);
		ap.loadImages(path, "dice", 1, 6, ".PNG");

    ap.setSize(200, 120);
    ap.setAutoResizeImage(true);
    ap.setMargins(5, 5, 5, 5);
    ap.setLayout(null);

    final BlackButton b = new BlackButton("Roll  Dice");
    b.setBounds(55, 125, 100, 30);

    f.add(ap);
    f.add(b);
    f.setVisible(true);

    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae)
      {
        ap.removeAll();

        if(ap.isRunning()) {
          b.setText("Roll  Dice");
          ap.stop();

          PowerImage blur = new PowerImage(
              PowerImage.createBufferedImage(
                ap.getCurrentImage().getImage()));
          blur.blur(10);

          ap.setCurrentImage(blur);

          int n = new Random().nextInt(6) + 1;

          j.setForeground(Color.white);
          j.setFont(new Font("SansSerif", Font.BOLD, 26));
          j.setText(String.valueOf(n));
          j.setHorizontalAlignment(JLabel.CENTER);
          j.setBounds(75, 45, 50, 30);

          ap.add(j);

          ap.repaint();
          ap.getParent().repaint();
        } else {
          b.setText("Stop");
          ap.start();
        }
        
      }
    });

  }

}
