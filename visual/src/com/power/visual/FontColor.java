/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.power.visual;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author f6036477
 */
public class FontColor {

  private Font font;

  private Color color;


  public FontColor()
  {
    font = null;
    color = null;
  }

  public FontColor(Font font, Color color)
  {
    this.font = font;
    this.color = color;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public Font getFont()
  {
    return font;
  }

  public void setFont(Font font)
  {
    this.font = font;
  }

}
