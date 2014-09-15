/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.coder;

import java.awt.Color;


/**
 *
 * @author juno
 */
public class FormAttribute extends javax.swing.JPanel {

  private Object value;
  
  /**
   * Creates new form FormColor
   */
  public FormAttribute() {
    initComponents();
    value = null;
  }
  
  
  public Object getValue() {
    return value;
  }
  
  
  public void setValue(Object val) {
    value = val;
  }
  
  
  public <T> T castValue() {
    if(value == null) return null;
    try {
      return (T) value;
    } catch(ClassCastException c) {
      return null;
    }
  }
  
  
  public boolean isValueTypeOf(Class c) {
    if(value == null) return false;
    return c.isAssignableFrom(value.getClass());
  }
  
  
  public void setLabelBackground(Color c) {
    if(c == null) return;
    int r, g, b;
    r = c.getRed() + 150;
    r = (r > 255 ? r - 255 : r);
    g = c.getGreen() + 150;
    g = (g > 255 ? g - 255 : g);
    b = c.getBlue() + 150;
    b = (b > 255 ? b - 255 : b);
    label.setForeground(new Color(r, g, b));
    label.setBackground(c);
  }


  public void setLabelForeground(Color c) {
    if(c == null) return;
    int r, g, b;
    r = c.getRed() + 150;
    r = (r > 255 ? r - 255 : r);
    g = c.getGreen() + 150;
    g = (g > 255 ? g - 255 : g);
    b = c.getBlue() + 150;
    b = (b > 255 ? b - 255 : b);
    label.setBackground(new Color(r, g, b));
    label.setForeground(c);
  }


  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    label = new javax.swing.JLabel();

    setBackground(new java.awt.Color(255, 255, 255));
    setPreferredSize(new java.awt.Dimension(250, 30));
    setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 4));

    label.setBackground(new java.awt.Color(255, 255, 255));
    label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    label.setOpaque(true);
    label.setPreferredSize(new java.awt.Dimension(150, 20));
    add(label);
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JLabel label;
  // End of variables declaration//GEN-END:variables
}
