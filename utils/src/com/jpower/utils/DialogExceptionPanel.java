package com.jpower.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.JOptionPane;
import net.java.dev.transparentlayout.TransparentLayout;

/**
 *
 * @author F6036477
 */
public class DialogExceptionPanel extends javax.swing.JPanel {

  private String prefix;

  private Throwable error;

  private Component component;


  public DialogExceptionPanel() {
    prefix = "Ocorreu um erro de execução:";
    error = null;
    component = null;
    initComponents();
  }


  public Throwable getError()
  {
    return error;
  }


  public void setError(Throwable error)
  {
    this.error = error;
  }


  public Component getComponent()
  {
    return component;
  }


  public void setComponent(Component component)
  {
    this.component = component;
  }


  public String getPrefix()
  {
    return prefix;
  }


  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }


  public void showDialog()
  {
    if(error == null) return;

    prefixLabel.setText("<html><style type=\"text/css\">"
        + ".plan {"
        + "  font-weight: bold;"
        + "  font-size: 11px;"
        + "  font-family: Monospaced;"
        + "  color: red;"
        + "}</style>"
        + "<h4>" + prefix
        + "</h4><div class='plan'>" + error.getMessage()
        + "</div></html");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(bos, true);
    error.printStackTrace(ps);

    errorArea.setText(new String(bos.toByteArray()));

    JOptionPane.showMessageDialog(component, this, "ERROR", JOptionPane.ERROR_MESSAGE);
  }


  private void initComponents() {

    prefixLabel = new javax.swing.JLabel();
    scroll = new javax.swing.JScrollPane();
    errorArea = new javax.swing.JTextArea();
    det = new javax.swing.JButton("Detalhar");

    setLayout(null);
    setPreferredSize(new Dimension(360, 200));

    prefixLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    prefixLabel.setBounds(new Rectangle(0, 0, 360, 80));
    add(prefixLabel);

    errorArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    errorArea.setColumns(20);
    errorArea.setEditable(false);
    //errorArea.setLineWrap(true);
    errorArea.setRows(5);
    scroll.setViewportView(errorArea);
    scroll.setBounds(new Rectangle(0, 90, 360, 110));

    //add(jScrollPane1);

    det.setBounds(new Rectangle(115, 110, 90, 25));
    det.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        DialogExceptionPanel.this.remove(det);
        DialogExceptionPanel.this.add(scroll);
        scroll.setVisible(true);
        DialogExceptionPanel.this.repaint();
        scroll.revalidate();
      }
    });
    add(det);
  }


  private javax.swing.JTextArea errorArea;
  private javax.swing.JScrollPane scroll;
  private javax.swing.JLabel prefixLabel;
  private javax.swing.JButton det;

}
