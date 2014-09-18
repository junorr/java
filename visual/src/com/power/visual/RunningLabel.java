/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.power.visual;


import com.jpower.utils.PowerImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Juno
 */
public class RunningLabel
        extends JLabel
        implements ActionListener
{

  private Timer timer;

  private static int DELAY = 50;

  public static int ROTATION = 10;

  private static PowerImage img;


  public RunningLabel()
  {
    timer = new Timer(DELAY, this);
    this.loadImage();
    timer.setRepeats(true);
    timer.start();
  }

  private void loadImage()
  {
    img = PowerImage.getInstance(this.getClass().
        getResource("running-circle[256].png"));
  }//method()

  public void setDelay(int delay)
  {
    if(delay <= 0) return;
    DELAY = delay;
  }//method()

  public int getDelay()
  {
    return DELAY;
  }//method()

  public void stop()
  {
    ROTATION = 0;
    timer.stop();
  }//method()

  public void start()
  {
    ROTATION = 10;
    timer.start();
  }//method()

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    img.rotate(ROTATION);

    //g2.clearRect(0, 0, this.getWidth(), this.getHeight());

    g2.drawImage( img.getImage(), 2, 2,
      this.getWidth()-3, this.getHeight()-3, null);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.repaint();
  }

}
