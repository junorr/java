package com.jpower.fxs.document;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * NumberDocument implementa <code>javax.swing.text.PlainDocument</code>
 * para permitir apenas números em um JTextField, no formato
 * definido pela instância interna de <code>NumberFormat</code>.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.05.09
 */
public class NumberDocument extends PlainDocument {
  
  public static final int DEFAULT_PRECISION = 2;
  
  
  private double value;

	private NumberFormat format;
  
  private JTextField field;
  
  
  /**
   * Construtor padrão que recebe a instância 
   * de <code>javax.swing.JTextField</code>
   * a ser formatada.
   * @param text <code>javax.swing.JTextField</code>
   * a ser formatado.
   */
  public NumberDocument(JTextField text) {
    if(text == null)
      throw new IllegalArgumentException(
          "javax.swing.JTextField must be not null.");
    
    field = text;
		format = new NumberFormat();
    value = 0;
  }


  /**
   * Retorna a instância de NumberFormat
   * utilizada para formatar os números.
   * @return Instância de NumberFormat.
   */
	public NumberFormat numberFormat() {
		return format;
	}
  
  
  /**
   * Define a instância de NumberFormat
   * utilizada para formatar os números.
   * @param nf Instância de NumberFormat.
   */
  public NumberDocument numberFormat(NumberFormat nf) {
    if(nf != null) format = nf;
    return this;
  }


  @Override
  public void insertString(int offs, String str, AttributeSet a)  
      throws BadLocationException {
    //se str for nula, retorna
    if(str == null || str.trim().isEmpty()) return;

    StringBuffer buf = new StringBuffer();
    buf.append(this.getText(0, this.getLength()));
    buf.insert(offs, str);
    
    //se o conteudo for igual a ',' ou '.' somente, retorna.
    if(buf.toString().equals(format.stringDecimal())
        || buf.toString().equals(format.stringGroup()))
      return;
    
    //se o conteúdo não for número, retorna.
    if(!format.isNumber(buf.toString())) return;
    
    int total = buf.length();
    
    this.remove(0, this.getLength());
    
    String sf = null;
    
    //se terminar com ',' ou ',0' insere sem formatar
    if(buf.toString().endsWith(format.stringDecimal())
        || buf.toString().endsWith(
        format.stringDecimal().concat("0"))) {
      super.insertString(0, buf.toString(), a);

    } else {

      //se a quantidade de casas decimais for
      //maior que o tamanho definido em format.decimalOn(),
      //deleta as casas que ultrapassarem o tamanho.
      if(buf.toString().contains(format.stringDecimal())
          && buf.substring(buf.indexOf(format.stringDecimal()) +1)
          .length() > format.decimalOn())
        buf.delete(buf.indexOf(format.stringDecimal()) 
            +1 +format.decimalOn(), buf.length());
      
      value = format.parse(buf.toString());
      sf = format.format(value);
      System.out.println(sf);
      super.insertString(0, sf, a);
    }
    
    //posiciona o cursorno local onde foi inserido
    //o conteúdo.
    if(sf != null)
      field.setCaretPosition(offs + str.length() + (sf.length() - total));
  }
  
  
  public static void main(String[] args) {

    //System.out.println(value);
    //System.out.println(nd.format(String.valueOf(value)));
    
    JFrame f = new JFrame();
    f.setSize(200, 100);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setLocationRelativeTo(null);
    JTextField tf = new JTextField(15);
    NumberDocument nd = new NumberDocument(tf);
    tf.setDocument(nd);
    f.add(tf);
    f.setVisible(true);
  }
  
}
