package com.power.visual.controls;

import com.jpower.utils.PowerImage;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;

/**
 * BlackLabel eh um Rótulo <code>JLabel</code>
 * customizado em duas cores com gradiente vertical.
 * Apesar de ser chamado BlackLabel, pode ser configurado
 * com quaisquer cores atraves da classe
 * <code>PaintStyle</code>. A classe possui alguns atributos
 * estaticos do tipo <code>PaintStyle</code>, que podem
 * ser passados para o construtor, criando um label com
 * o estilo corespondente. <BR>
 * O uso de <code>BlackLabel</code> eh muito similar
 * aa sua superclasse <code>JLabel</code>, o exemplo
 * mais simples seria:<br><code>
 * BlackLabel label = new BlackLabel("My Label");</code>
 * <br>
 * O restante do uso eh identico ao
 * <code>javax.swing.JLabel</code>.
 * Para usar um dos estilos estaticos existentes em
 * PaintStyle, um exemplo seria:<br><code>
 * BlackButton winxpLabel = new BlackLabel(
 * BlackLabel.WINXP_STYLE, "My Label");</code>
 * @author Juno D. R. Roesler - juno.rr@gmail.com
 * @version BlackButton 0.1.0 - build#2  2009-03-26
 */
public class BlackLabel extends JLabel {

  /**
   * SerialVersionUID para fins de serializacao do objeto.
   */
  protected static final long serialVersionUID = 200903261L;

  /**
   * Estilo com o qual o botao serah pintado.
   */
  private PaintStyle style;

  /**
   * Percentual de divisao da altura para pintura
   * gradiente do componente.
   */
  private double graddiv = 0.3;

  /**
   * Percentual de divisão da altura para pintura
   * do componente.
   */
  private double div = 0.45;

  /**
   * Ícone do componente.
   */
  private PowerImage icon;

  /**
   * Texto do componente.
   */
  private String text;

  private int imgap;


  class DrawMetrics {
    Graphics g;
    int horizSpace;
    int vertSpace;
    int stringWidth;
    int stringHeight;
    int gap;
  }



  /**
   * Construtor sem argumentos, instancia um objeto
   * <code>BlackLabel</code> com o estilo
   * <code>PaintStyle.GRADIENT_BLACK</code>.
   */
  public BlackLabel() {
    super();
    this.setHorizontalAlignment(SwingConstants.CENTER);
    this.setVerticalAlignment(SwingConstants.CENTER);
    icon = null;
    style = PaintStyle.GRADIENT_BLACK;
    imgap = 5;

    this.setBorder(
        BorderFactory.createLineBorder(
        style.getUpColor()));
  }//blackButton()


  /**
   * Construtor que recebe o texto que
   * serah exibido no label. Instancia
   * um label com o estilo <code>PaintStyle.GRADIENT_BLACK</code>
   * @param text Texto do label.
   */
  public BlackLabel(String text) {
    this();
    this.setText(text);
  }


  /**
   * Construtor com dois argumentos que recebe um objeto
   * <code>PaintStyle</code>, configurando o estilo de
   * pintura do label, e uma String com o texto que
   * serah exibido.
   * @param style Configura o estilo de pintura do label.
   * @param text Texo que serah exibido no label.
   */
  public BlackLabel(PaintStyle style, String text) {
    this();
    if(style != null)
      this.style = style;
    this.setText(text);

    this.setBorder(
        BorderFactory.createLineBorder(
        style.getUpColor()));
  }//BlackButton()


  /**
   * Construtor com três argumentos que recebe um objeto
   * <code>PaintStyle</code>, configurando o estilo de
   * pintura do label, o ícone do componente e o texto que
   * será exibido.
   * @param style Configura o estilo de pintura do label.
   * @param icon Ícone que será exibido no componente.
   * @param text Texo que serah exibido no label.
   */
  public BlackLabel(PaintStyle style, PowerImage icon, String text) {
    this();
    if(style != null)
      this.style = style;
    this.setText(text);
    this.icon = icon;

    this.setBorder(
        BorderFactory.createLineBorder(
        style.getUpColor()));
  }//BlackButton()


  /**
   * Retorna o ícone do componente.
   * @return PowerImage icon.
   */
  public PowerImage getImageIcon()
  {
    return icon;
  }

  /**
   * Seta o ícone do componente.
   * @param icon PowerImage.
   */
  public void setImageIcon(PowerImage icon)
  {
    this.icon = icon;
  }

  /**
   * Seta o texto a ser exibido.
   * @param text
   */
  @Override
  public void setText(String text)
  {
    this.text = text;
  }

  /**
   * Retorna o texto a ser exibido.
   * @return String text;
   */
  @Override
  public String getText()
  {
    return text;
  }


  public int getImageGap()
  {
    return imgap;
  }

  public void setImageGap(int gap)
  {
    if(gap < 0) gap = 0;
    imgap = gap;
  }


  /**
   * Metodo sobrescrito para pintura do botao.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int limit = 0;

    //seta a cor da parte de baixo
    g.setColor(style.getDownColor());

    //Pinta o estilo do label
    if(style.isGradientPaintEnabled()) {
      //Modo gradiente

      //Calcula a altura limite para divisão do componente
      limit = (int) (this.getHeight() * graddiv);

      //Modificador do limite para pintura apenas na cor
      //mais escura.
      int hl = (this.getHeight() - limit) / 2;

      //Pinta um retângulo começando no limite + o
      //modificador (+/- 75% da altura do componente),
      //com a cor mais escura.
      g.fillRect(0, (limit + hl), this.getWidth(), this.getHeight());
      Graphics2D g2 = (Graphics2D) g;

      //Define o estilo gradiente, começando com
      //uma tonalidade 5% mais clara que a cor
      //mais escura, e segue com um gradiente até
      //o início do componente até atingir a cor
      //mais clara.
      GradientPaint gp = new GradientPaint(
        new Point(0, (limit + hl)),
        PaintStyle.brighter(style.getDownColor(), 0.05),
        new Point(0, 0),
        style.getUpColor()
      );//GradientPaint

      //Define o retângulo a ser pintado com o gradiente,
      Rectangle r = new Rectangle(0, 0, this.getWidth(), (limit + hl));
      //Seta o estilo gradiente
      g2.setPaint(gp);
      //Pinta o retângulo com o gradiente.
      g2.fill(r);

    } else {
      //Modo Normal

      //Calcula a altura onde será dividido o componente
      //(+/- 55% da altura).
      limit = this.getHeight() - (int) (this.getHeight() * div);
      //Pinta a parte de baixo com a cor mais escura
      g.fillRect(0, limit, this.getWidth(), this.getHeight());
      //Seta a cor mais clara
      g.setColor(style.getUpColor());
      //Pinta a parte de cima com a cor mais clara
      g.fillRect(0, 0, this.getWidth(), limit);
    }//if-else

    //variáveis para o espeço vertival e horizontal
    //entre a borda e o componente a ser desenhado
    int horizSpace = 0;
    int vertSpace = 0;
    //distância entre objetos desenhados
    int gap = imgap;

    //Determina o tamanho do texto
    Font f = style.getFont();
    FontMetrics fm = g.getFontMetrics(f);

    //Divide o texto em linhas
    String[] texto = this.getText().split("\n");

    //Calcula a altura das linhas
    int stringHeight = fm.getHeight() * texto.length;

    //encontra a linha mais extensa
    int maior = 0;
    for(int j = 0; j < texto.length-1; j++)
    {
      if(texto[j].length() > texto[j+1].length())
        maior = j;
      else
        maior = j+1;
    }
    int stringWidth = fm.stringWidth(texto[maior]);

    DrawMetrics metrics = new DrawMetrics();
    metrics.g = g;
    metrics.gap = gap;
    metrics.horizSpace = horizSpace;
    metrics.vertSpace = vertSpace;
    metrics.stringWidth = stringWidth;
    metrics.stringHeight = stringHeight;



    //Desenha o ícone, se existir
    this.drawIcon(metrics);

    //Desenha o texto
    this.drawString(g, fm, f, texto, metrics);

  }//paintComponent()


  private void drawString(Graphics g, FontMetrics fm, Font f, String[] texto, DrawMetrics dm)
  {
    //Seta a cor da fonte
    g.setColor(style.getFontColor());
    //calcula a posição 'y' em que a string começará
    //a ser desenhada.
    int fpy = this.calculateVerticalSpace(dm.stringHeight, 0);

    //calcula a posição 'x' em que a string começará
    //a ser desenhada.
    int fpx = 0;
    if(this.getHorizontalTextPosition() == SwingConstants.RIGHT)
      if(icon != null) {
        fpx = dm.horizSpace + icon.getWidth() + dm.gap;
      } else {
        fpx = this.calculateHorizontalSpace(dm.stringWidth, 0);
      }
    else
      if(icon != null) {
        fpx = this.calculateHorizontalSpace(dm.stringWidth, icon.getWidth());
      } else {
        fpx = this.calculateHorizontalSpace(dm.stringWidth, 0);
      }

    if(fpx < 0) fpx = 0;
    if(fpy < 0) fpy = 0;

    //seta a fonte.
    g.setFont(f);

    //desenha as linhas de texto.
    for(int i = 0; i < texto.length; i++)
    {
      fpy += fm.getHeight();

      //este é o espaço existente entre o texto
      //e a linha acima, que deve ser
      //desconsiderado para o desenho
      fpy -= fm.getHeight()/4;
      g.drawString(texto[i], fpx, fpy);
    }
  }


  private void drawIcon(DrawMetrics dm)
  {
    //Desenha o ícone, se existir
    if(icon != null) {

      //calcula o espaço vertical necessário entre
      //o componente a ser desenhado e a borda, considerando
      //o alinhamento setado para o label.
      dm.vertSpace = this.calculateVerticalSpace(0, icon.getHeight());

      //calcula o tamanho necessário para o ícone, reduzindo-o
      //se necessário, para caber no label
      int imgsizeH = (int) (this.getHeight() * 0.85);
      if(icon.getHeight() < imgsizeH)
        imgsizeH = icon.getHeight();

      //calcula o tamanho necessário para o ícone, reduzindo-o
      //se necessário, para caber no label
      int imgsizeW = icon.getWidth();
      if(imgsizeW > this.getWidth() - dm.gap)
        imgsizeW = this.getWidth() - dm.gap;

      //calcula o espaço horizontal necessário entre
      //o componente a ser desenhado e a borda, considerando
      //o alinhamento setado para o label.
      dm.horizSpace = this.calculateHorizontalSpace(dm.stringWidth, imgsizeW);

      icon.scale(imgsizeW, imgsizeH);
      if(this.getHorizontalTextPosition() == SwingConstants.RIGHT)
        dm.g.drawImage(icon.getImage(), dm.horizSpace, dm.vertSpace, null);
      else
        dm.g.drawImage(icon.getImage(), dm.horizSpace + dm.stringWidth + dm.gap,
            dm.vertSpace, null);
    }
  }


  /**
   * Calcula o espaço horizontal necessário entre a borda do label
   * e o objeto a ser desenhado, considerando o alinhamento
   * interno setado.
   * @param stringWidth tamanho da String a ser desenhada, se houver.
   * @param imageWidth tamanho da imagem a ser desenhada, se houver.
   * @return posição horizontal onde o objeto será desenhado
   */
  public int calculateHorizontalSpace(int stringWidth, int imageWidth)
  {
    int space = 0;
    int gap = (imageWidth == 0 ? 0 : imgap);

    if(this.getHorizontalAlignment()
        == SwingConstants.LEFT) {

      space = 5;
    }
    else if(this.getHorizontalAlignment()
        == SwingConstants.CENTER) {

      space = (stringWidth + gap + imageWidth) / 2;
      space = this.getWidth() / 2 - space;
      if(space < 0) space = 5;
    }
    else {

      space = this.getWidth() - 5 -
          stringWidth - gap - imageWidth;
      if(space < 0) space = 5;
    }
    return space;
  }


  /**
   * Calcula o espaço vertical necessário entre a borda do label
   * e o objeto a ser desenhado, considerando o alinhamento
   * interno setado.
   * @param stringWidth tamanho da String a ser desenhada, se houver.
   * @param imageWidth tamanho da imagem a ser desenhada, se houver.
   * @return posição vertical onde o objeto será desenhado
   */
  public int calculateVerticalSpace(int stringHeight, int imageHeight)
  {
    if(imageHeight >= this.getHeight()
        || stringHeight >= this.getHeight())
      return 2;

    int height = (imageHeight > stringHeight ?
      imageHeight : stringHeight);
    int space = 0;

    if(this.getVerticalAlignment()
        == SwingConstants.TOP) {

      space = 5;
    }
    else if(this.getVerticalAlignment()
        == SwingConstants.CENTER) {

      //System.out.println("height: "+ height);
      space = (this.getHeight() - height) / 2;
    }
    else {

      space = this.getHeight() - 5 - height;
    }
    return space;
  }


  public static void main(String[] args) throws IOException
  {
    JFrame f = new JFrame("BlackLabel Test");
    f.setBounds(300, 300, 400, 400);
    BlackLabel b1 = new BlackLabel(
        PaintStyle.GRADIENT_RED, new PowerImage(
        "E:/java/power/nb/powercash/images/sort-down-arrow-[34x20].png"),
        "BlackLabel");
    b1.setBounds(10, 10, 200, 50);
    b1.setHorizontalTextPosition(SwingConstants.LEFT);
    b1.setHorizontalAlignment(SwingConstants.CENTER);
    b1.setImageGap(2);

    BlackLabel b2 = new BlackLabel(
        PaintStyle.RED_STYLE, new PowerImage(
        "E:/java/power/nb/powercash/images/sort-down-arrow-[34x20].png"),
        "   Black Label\nGradient Style");
    b2.setBounds(10, 70, 200, 50);
    //b2.setHorizontalTextPosition(SwingConstants.RIGHT);
    b2.setHorizontalAlignment(SwingConstants.CENTER);
    b2.setImageGap(15);

    f.setLayout(null);
    f.add(b1);
    f.add(b2);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
  }

}//BlackButton.class
