package com.jpower.utils;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author F6036477
 */
public class DialogExceptionHandler extends AbstractExceptionHandler
{

  private boolean fatalError;
  
  private DialogExceptionPanel panel;


  public DialogExceptionHandler()
  {
    super();
    panel = new DialogExceptionPanel();
    fatalError = false;
  }


  public DialogExceptionHandler(Throwable th)
  {
    super(th);
    panel = new DialogExceptionPanel();
    fatalError = false;
  }


  public boolean isFatalError()
  {
    return fatalError;
  }


  public void setFatalError(boolean fatalError)
  {
    this.fatalError = fatalError;
  }


  public void handle(Throwable th)
  {
    set(th);
    handle();
  }


  public void handle()
  {
    if(get() == null) return;

    panel.setError(get());
    if(fatalError)
      panel.setPrefix("Ocorreu um erro FATAL de execução:");
    panel.showDialog();
    if(fatalError)
      System.exit(1);
  }


  public static void main(String[] args)
  {
    /*
    JLabel label = new JLabel();
    label.setText(
        "<html>"
        + "<style type='text/css'>"
        + "  .plain {"
        + "    font-weight: normal;"
        + "    font-size: 10px;"
        + "    font-family: Monospaced;"
        + "}</style>"
        + "<table border='1'>"
        + "<tr>"
        + "  <td>"
        + "    <p>Linha1, Coluna1</p>"
        + "  </td>"
        + "  <td>"
        + "    <p>Linha1, Coluna2</p>"
        + "  </td>"
        + "  <td>"
        + "    <p>Linha1, Coluna3</p>"
        + "  </td>"
        + "</tr>"
        + "<tr>"
        + "  <td>"
        + "    <p class='plain'>Linha2, Coluna1</p>"
        + "  </td>"
        + "  <td>"
        + "    <p class='plain'>Linha2, Coluna2</p>"
        + "  </td>"
        + "  <td>"
        + "    <p class='plain'>Linha2, Coluna3</p>"
        + "  </td>"
        + "</tr>"
        + "</table>"
        + "</html>"
        );
    //label.setPreferredSize(new Dimension(500, 200));
    JOptionPane.showMessageDialog(null, label);
    */

    Throwable th = new IllegalStateException("Erro simulado de execução.");
    DialogExceptionHandler handler = new DialogExceptionHandler();
    handler.setFatalError(true);

    try {
      throw th;
    } catch(Throwable ex) {
      handler.set(ex);
      handler.handle();
    }
  }

}
