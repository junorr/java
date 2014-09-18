package com.power.visual.controls;

import com.jpower.utils.PowerImage;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * BlackButton eh um botao <code>JButton</code>
 * customizado em duas cores com gradiente vertical.
 * Apesar de ser chamado BlackButton, pode ser configurado
 * com quaisquer cores atraves da classe
 * <code>PaintStyle</code>. A classe possui alguns atributos
 * estaticos do tipo <code>PaintStyle</code>, que podem
 * ser passados para o construtor, criando um botao com
 * o estilo corespondente. <BR>
 * O uso de <code>BlackButton</code> eh muito similar
 * aa sua superclasse <code>JButton</code>, o exemplo
 * mais simples seria:<br><code>
 * BlackButton button = new BlackButton("My Button");</code>
 * <br>
 * O restante do uso eh identico ao
 * <code>javax.swing.JButton</code>.
 * Para usar um dos estilos estaticos existentes em
 * BlackButton, um exemplo seria:<br><code>
 * BlackButton winxpButton = new BlackButton(
 * PaintStyle.WINXP_STYLE, "My Button");</code>
 * @author Juno D. R. Roesler - juno.rr@gmail.com
 * @version BlackButton 0.1.0 - build#2  2009-03-26
 */
public class BlackButton
    extends JButton
    implements KeyListener, FocusListener, MouseListener
{

  /**
   * SerialVersionUID para fins de serializacao do objeto.
   */
  protected static final long serialVersionUID = 200903261L;

  /**
   * Estilo com o qual o botao serah pintado.
   */
  private PaintStyle style;

  /**
   * Percentual de divisao para pintura do botao.
   */
  private double div = 0.45;

  private double graddiv = 0.3;

  private PowerImage icon;

  private PowerImage normalicon;

  private PowerImage overicon;

  private boolean focused;

  /**
   * Construtor sem argumentos, instancia um objeto
   * <code>BlackButton</code> com o estilo
   * <code>GRADIENT_BLACK</code>.
   */
  public BlackButton() {
    super();
    this.setHorizontalAlignment(SwingConstants.CENTER);
    this.setVerticalAlignment(SwingConstants.CENTER);
    icon = null;
    overicon = null;
    normalicon = null;
    style = PaintStyle.GRADIENT_BLACK;
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent me) {
        if(overicon != null) {
          icon = overicon;
          BlackButton.this.repaint();
        }
      }
      @Override
      public void mouseExited(MouseEvent me) {
        if(overicon != null) {
          icon = normalicon;
          BlackButton.this.repaint();
        }
      }
    });
    this.addKeyListener(this);
    this.addFocusListener(this);
    this.addMouseListener(this);
    focused = false;
  }//blackButton()


  /**
   * Construtor que recebe o texto que
   * serah exibido no botao. Instancia
   * um botao com o estilo <code>GRADIENT_BLACK</code>
   * @param text Texto do botao.
   */
  public BlackButton(String text) {
    this();
    this.setText(text);
  }


  /**
   * Construtor com dois argumentos que recebe um objeto
   * <code>PaintStyle</code>, configurando o estilo de
   * pintura do botao, e uma String com o texto que
   * serah exibido no botao.
   * @param style Configura o estilo de pintura do botao.
   * @param text Texo que serah exibido no botao.
   */
  public BlackButton(PaintStyle style, String text) {
    this();
    if(style != null)
      this.style = style;
    this.setText(text);
  }//BlackButton()


  /**
   * Construtor com dois argumentos que recebe um objeto
   * <code>PaintStyle</code>, configurando o estilo de
   * pintura do botão, e uma String com o texto que
   * serah exibido.
   * @param style Configura o estilo de pintura do botão.
   * @param text Texo que serah exibido no botão.
   */
  public BlackButton(PaintStyle style, PowerImage icon, String text) {
    this();
    if(style != null)
      this.style = style;
    this.setText(text);
    this.icon = icon;
    this.normalicon = icon;
  }//BlackButton()


  /**
   * Retorna a imagem de ícone.
   * @return PowerImage icon.
   */
  public PowerImage getImageIcon()
  {
    return normalicon;
  }


  /**
   * Seta a imagem de ícone.
   * @param icon PowerImage.
   */
  public void setImageIcon(PowerImage icon)
  {
    this.icon = icon;
    this.normalicon = icon;
  }


  /**
   * Retorna a imagem de ícone quando o componente
   * estiver sob o mouse.
   * @return PowerImage icon.
   */
  public PowerImage getOverImageIcon()
  {
    return overicon;
  }


  /**
   * Seta a imagem de ícone quando o componente
   * estiver sob o mouse.
   * @param icon PowerImage.
   */
  public void setOverImageIcon(PowerImage icon)
  {
    this.overicon = icon;
  }


  /**
   * Metodo sobrescrito para pintura do botao.
   */
  @Override
  protected void paintComponent(Graphics g) {
    //super.paintComponent(g);
    int limit = 0;

    Graphics gb = g.create();
    //seta a cor da parte de baixo
    gb.setColor(style.getDownColor());

    //Pinta o estilo do botão
    if(style.isGradientPaintEnabled()) {//Modo gradiente
      //Modo gradiente

      //Calcula a altura limite para divisão do componente
      limit = (int) (this.getHeight() * graddiv);

      //Modificador do limite para pintura apenas na cor
      //mais escura.
      int hl = (this.getHeight() - limit) / 2;

      //Pinta um retângulo começando no limite + o
      //modificador (+/- 75% da altura do componente),
      //com a cor mais escura.
      gb.fillRect(0, (limit + hl), this.getWidth(), this.getHeight());
      Graphics2D g2 = (Graphics2D) gb;

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
      gb.fillRect(0, limit, this.getWidth(), this.getHeight());
      //Seta a cor mais clara
      gb.setColor(style.getUpColor());
      //Pinta a parte de cima com a cor mais clara
      gb.fillRect(0, 0, this.getWidth(), limit);
    }//if-else

    //variáveis para o espeço vertival e horizontal
    //entre a borda e o componente a ser desenhado
    int horizSpace = 0;
    int vertSpace = 0;
    //distância entre objetos desenhados
    int gap = 8;

    //Determina o tamanho do texto
    Font f = style.getFont();
    FontMetrics fm = gb.getFontMetrics(f);

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

    //Desenha o ícone se existir
    if(icon != null) {

      //calcula o espaço vertical necessário entre
      //o componente a ser desenhado e a borda, considerando
      //o alinhamento setado para o botão.
      vertSpace = this.calculateVerticalSpace(stringHeight, icon.getHeight());

      //calcula o tamanho necessário para o ícone, reduzindo-o
      //se necessário, para caber no botão
      int imgsizeH = (int) (this.getHeight() * 0.85);
      if(icon.getHeight() < imgsizeH)
        imgsizeH = icon.getHeight();

      //calcula o tamanho necessário para o ícone, reduzindo-o
      //se necessário, para caber no botão
      int imgsizeW = icon.getWidth();
      if(imgsizeW > this.getWidth() - gap)
        imgsizeW = this.getWidth() - gap;

      //calcula o espaço horizontal necessário entre
      //o componente a ser desenhado e a borda, considerando
      //o alinhamento setado para o botão.
      horizSpace = this.calculateHorizontalSpace(stringWidth, imgsizeW);

      icon.scale(imgsizeW, imgsizeH);
      gb.drawImage(icon.getImage(), horizSpace, vertSpace, null);
    }

    //Desenha a String

    //Seta a cor da fonte
    gb.setColor(style.getFontColor());
    //calcula a posição 'y' em que a string começará
    //a ser desenhada.
    int fpy = this.calculateVerticalSpace(stringHeight, 0);

    //calcula a posição 'x' em que a string começará
    //a ser desenhada.
    int fpx = 0;
    if(icon != null)
      fpx = horizSpace + icon.getWidth() + gap;
    else
      fpx = this.calculateHorizontalSpace(stringWidth, 0);

    if(fpx < 0) fpx = 0;
    if(fpy < 0) fpy = 0;

    //seta a fonte.
    gb.setFont(f);

    //desenha as linhas de texto.
    for(int i = 0; i < texto.length; i++)
    {
      fpy += fm.getHeight();

      //este é o espaço existente entre o texto
      //e a linha acima, que deve ser
      //desconsiderado para o desenho
      fpy -= fm.getHeight()/4;
      gb.drawString(texto[i], fpx, fpy);
    }

    //desenha bordas quando estiver focado
    if(this.isFocused()) {
      Rectangle r1 = new Rectangle(0, 0, this.getWidth() -2, this.getHeight() -2);
      Rectangle r2 = new Rectangle(1, 1, this.getWidth() -4, this.getHeight() -4);

      Graphics2D g2 = (Graphics2D) gb;

      g2.setColor(style.getFocusColor());
      g2.draw(r1);
      g2.setColor(PaintStyle.brighter(style.getFocusColor(), .25));
      g2.draw(r2);

      g2.dispose();
    }
    gb.dispose();
  }//paintComponent()


  /**
   * Calcula o espaço horizontal necessário entre a borda do botão
   * e o objeto a ser desenhado, considerando o alinhamento
   * interno setado.
   * @param stringWidth tamanho da String a ser desenhada, se houver.
   * @param imageWidth tamanho da imagem a ser desenhada, se houver.
   * @return posição horizontal onde o objeto será desenhado
   */
  public int calculateHorizontalSpace(int stringWidth, int imageWidth)
  {
    int space = 0;
    int gap = (imageWidth == 0 ? 0 : 5);

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
   * Calcula o espaço vertical necessário entre a borda do botão
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

  public void keyTyped(KeyEvent e) { }
  public void keyReleased(KeyEvent e) { }
  public void mouseEntered(MouseEvent e){}
  public void mouseClicked(MouseEvent e){}
  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}


  @Override
  public void keyPressed(KeyEvent e)
  {
    if(e.getKeyCode() == KeyEvent.VK_ENTER)
      this.fireActionPerformed(new ActionEvent(this, e.getID(), "EnterKeyAction"));
  }

  private void setFocused(boolean focused)
  {
    this.focused = focused;
    this.repaint();
  }

  private boolean isFocused()
  {
    return focused;
  }

  @Override
  public void focusGained(FocusEvent e)
  {
    this.setFocused(true);
  }

  @Override
  public void focusLost(FocusEvent e)
  {
    this.setFocused(false);
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    this.setFocused(false);
  }

}//BlackButton.class
