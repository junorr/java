package com.power.visual.controls;

import java.awt.*;

/**
 * Classe que configura o estilo de pintura
 * de <code>BlackButton</code>.
 * O estilo de pintura gradiente para transicao
 * gradual entre as cores eh o padrao utilizado
 * por <code>PaintStyle</code>. Porem este estilo
 * pode ser configurado pelo metodo
 * <code>setGradientPaintEnabled(boolean)</code>.
 * A fonte padrao usada em eh a "SansSerif"
 * estilo <code>Font.BOLD</code> com tamanho 11.
 * Um exemplo simples de utilizacao de <code>PaintStyle</code>,
 * seria:<br><code>
 * PaintStyle paint = new PaintStyle(
 * Color.RED, Color.ORANGE, Color.BLACK);</code><BR>
 * Dessa forma, terÃ­amos uma instancia de <code>PaintStyle</code>
 * que pintaria um BlackButton, por exemplo, comecando em
 * vermelho na parte de baixo, mudando para laranja
 * de forma gradual (gradient) e com fonte preta.
 * Poderiamos modificar a fonte padrao, por exemplo:<br>
 * <code>
 * Font f = new Font("Curier", Font.ITALIC, 8);
 * paint.setFont(f);
 * </code><br>
 * Poderiamos ainda desabilitar o estilo de transicao
 * gradual das cores:<br><code>
 * paint.setGradientPaintEnabled(false);
 * </code><br>
 * Ou modificar a segunda cor (que pintaria a parte de
 * baixo do botao) para um vermelho
 * 30% mais claro:<br>
 * <code>
 * paint.setDownColor( paint.brighter(Color.RED, 0.3) );
 * </code>
 */
public class PaintStyle {

  /**
   * Estilo em dois tons de preto, mais claro
   * na parte de cima do botao e mais escuro
   * na parte de baixo. Nao possui efeito gradiente.
   */
  public static final PaintStyle BLACK_STYLE =
    new PaintStyle(
      new Color(0x3E3E3E), //UP   -  62,  62,  62
      new Color(0x070A0A), //DOWN -   7,  10,  10
      new Color(0xF0F0F0), //FONT - 240, 240, 240
      false                //Sem gradiente
    );//BLACK_STYLE

  /**
   * Estilo preto com efeito gradiente vertical,
   * comecando mais escuro na parte de baixo e
   * clareando gradualmente ateh a parte de cima.
   */
  public static final PaintStyle GRADIENT_BLACK =
    new PaintStyle(
      new Color(0x696969), //UP   - 105, 105, 105
      new Color(0x070A0A), //DOWN -   7,  10,  10
      new Color(0xF0F0F0)  //FONT - 240, 240, 240
    );//BLACK_STYLE

  /**
   * Estilo em dois tons de amarelo, mais claro
   * na parte de cima do botao e mais escuro
   * na parte de baixo. Nao possui efeito gradiente.
   */
  public static final PaintStyle YELLOW_STYLE =
    new PaintStyle(
      new Color(0xFFE272), //UP   - 255, 226, 114
      new Color(0xFEBA2B), //DOWN - 254, 186,  43
      new Color(0x434343), //FONT -  67,  67,  67
      false                //Sem gradiente
    );//YELLOW_STYLE

  /**
   * Estilo amarelo com efeito gradiente vertical,
   * comecando mais escuro na parte de baixo e
   * clareando gradualmente ateh a parte de cima.
   */
  public static final PaintStyle GRADIENT_YELLOW =
    new PaintStyle(
      new Color(0xFFE272), //UP   - 255, 226, 114
      new Color(0xFEBA2B), //DOWN - 254, 186,  43
      new Color(0x434343)  //FONT -  67,  67,  67
    );//YELLOW_STYLE

  /**
   * Estilo em dois tons de azul, mais claro
   * na parte de cima do botao e mais escuro
   * na parte de baixo. Nao possui efeito gradiente.
   */
  public static final PaintStyle BLUE_STYLE =
    new PaintStyle(
      new Color(0x6DAEF6), //UP   - 109, 174, 246
      new Color(0x3678BE), //DOWN -  54, 120, 190
      new Color(0xF0F0F0), //FONT - 240, 240, 240
      false                //Sem gradiente
    );//BLUE_STYLE

  /**
   * Estilo azul com efeito gradiente vertical,
   * comecando mais escuro na parte de baixo e
   * clareando gradualmente ateh a parte de cima.
   */
  public static final PaintStyle GRADIENT_BLUE =
    new PaintStyle(
      new Color(0x6DAEF6), //UP   - 109, 174, 246
      new Color(0x3678BE), //DOWN -  54, 120, 190
      new Color(0xF0F0F0)  //FONT - 240, 240, 240
    );//BLUE_STYLE

  /**
   * Estilo em dois tons de vermelho, mais claro
   * na parte de cima e mais escuro
   * na parte de baixo. Nao possui efeito gradiente.
   */
  public static final PaintStyle RED_STYLE =
    new PaintStyle(
      new Color(0xE62E2E), //UP   - 230,  46,  46
      new Color(0x960000), //DOWN - 109,   0,   0
      new Color(0xF0F0F0), //FONT - 240, 240, 240
      false                //Sem gradiente
    );//RED_STYLE

  /**
   * Estilo vermelho com efeito gradiente vertical,
   * comecando mais escuro na parte de baixo e
   * clareando gradualmente ateh a parte de cima.
   */
  public static final PaintStyle GRADIENT_RED =
    new PaintStyle(
      new Color(0xE62E2E), //UP   - 230,  46,  46
      new Color(0x960000), //DOWN - 109,   0,   0
      new Color(0xF0F0F0)  //FONT - 240, 240, 240
    );//GRADIENT_RED

  /**
   * Estilo em dois tons de vermelho, mais claro
   * na parte de cima e mais escuro
   * na parte de baixo. Nao possui efeito gradiente.
   */
  public static final PaintStyle GREEN_STYLE =
    new PaintStyle(
      new Color(92, 215, 83), //UP   -  92, 215,  83
      new Color(80, 133, 76), //DOWN -  80, 133,  76
      Color.BLACK,            //FONT - 0, 0, 0
      false                   //Sem gradiente
    );//GREEN_STYLE

  /**
   * Estilo vermelho com efeito gradiente vertical,
   * comecando mais escuro na parte de baixo e
   * clareando gradualmente ateh a parte de cima.
   */
  public static final PaintStyle GRADIENT_GREEN =
    new PaintStyle(
      new Color(92, 215, 83), //UP   -  92, 215,  83
      new Color(80, 133, 76), //DOWN -  80, 133,  76
      Color.BLACK             //FONT - 0, 0, 0
    );//GRADIENT_GREEN

  /**
   * Estilo em dois tons de cinza padrao Windows XP,
   * mais claro na parte de cima do botao e mais escuro
   * na parte de baixo. Nao possui efeito gradiente.
   */
  public static final PaintStyle WINXP_STYLE =
    new PaintStyle(
      new Color(0xE8E7EF), //UP   - 232, 231, 239
      new Color(0xCBCCDA), //DOWN - 203, 204, 218
      new Color(0x434343), //FONT -  67,  67,  67
      false                //Sem gradiente
    );//WINXP_STYLE

  /**
   * Estilo cinza padrao Windows XP com efeito
   * gradiente vertical, comecando mais escuro
   * na parte de baixo e clareando gradualmente
   * ateh a parte de cima.
   */
  public static final PaintStyle GRADIENT_WINXP =
    new PaintStyle(
      new Color(0xF8F9FA), //UP   - 248, 249, 250
      new Color(0xCBCCDA), //DOWN - 203, 204, 218
      new Color(0x434343)  //FONT -  67,  67,  67
    );//WINXP_STYLE

  private Color upColor, downColor, fontColor, focusColor;

  private Font font;

  private boolean gradient;

  /**
   * Construtor padrao recebe tres objetos
   * <code>java.awt.Color</code> que configuram
   * as cores utilizadas na pintura do botao.
   * @param up Cor utilizada na parte de cima do botao
   * @param down Cor utilizada na parte de baixo do botao.
   * @param font Cor utilizada na fonte do texto do botao.
   */
  public PaintStyle(Color up, Color down, Color font) {
    upColor = up;
    downColor = down;
    fontColor = font;
    gradient = true;
    this.font = new Font("SansSerif", Font.BOLD, 11);
    focusColor = new Color(55, 188, 196);
  }//PaintStyle()

  public PaintStyle(Color up, Color down, Color font, Color focus)
  {
    this(up, down, font);
    this.setFocusColor(focus);
  }

  /**
   * Construtor similar ao padrao, porem recebe um booleano
   * que configura o estilo gradiente para transicao entre
   * as cores.
   * @param up Cor utilizada na parte de cima do botao
   * @param down Cor utilizada na parte de baixo do botao.
   * @param font Cor utilizada na fonte do texto do botao.
   * @param gradient <code>true</code> habilita o estilo
   * gradiente utilizado na transicao das cores,
   * <code>false</code> desabilita.
   */
  public PaintStyle(Color up, Color down, Color font, boolean gradient) {
    this(up, down, font);
    this.setGradientPaintEnabled(gradient);
  }//PaintStyle()

  /**
   * Seta o estilo gradiente para transicao gradual
   * entre as cores.
   * @param enabled <code>true</code> habilita o estilo,
   * <code>false</code> desabilita o estilo.
   */
  public void setGradientPaintEnabled(boolean enabled) {
    gradient = enabled;
  }//setGradientPaintEnabled()

  /**
   * Verifica se o estilo gradiente para transicao
   * gradual entre as cores esta habilitado.
   * @return <code>true</code> caso esteja habilitado,
   * <code>false</code> caso contrario.
   */
  public boolean isGradientPaintEnabled() {
    return gradient;
  }//isGradientPaintEnabled()

  /**
   * Metodo utilitario que transforma uma cor
   * <code>c</code> percentualmente mais
   * clara pelo valor especificado por <code>d</code>.
   * @param c Cor a ser clareada.
   * @param d Percentual do clareamento.
   * @return Nova cor mais clara que a original.
   */
  public static Color brighter(Color c, double d) {
    int r, g, b;
    r = c.getRed();
    g = c.getGreen();
    b = c.getBlue();
    int maior = 0;
    if(r > g) maior = r;
    else maior = g;
    if(maior < b) maior = b;
    int amount = (int) (maior * d);
    r += amount;
    g += amount;
    b += amount;
    if(r > 255) r = 255;
    if(g > 255) g = 255;
    if(b > 255) b = 255;
    return new Color(r, g, b);
  }//brighter()

  public void setFocusColor(Color c)
  {
    this.focusColor = c;
  }

  public Color getFocusColor()
  {
    return this.focusColor;
  }

  /**
   * Retorna a cor de baixo utilizada na pintura
   * do botao.
   * @return cor de baixo utilizada na pintura
   * do botao.
   */
  public Color getDownColor() {
    return downColor;
  }//getDownColor()

  /**
   * Seta a cor de baixo utilizada na pintura
   * do botao.
   * @param downColor cor de baixo utilizada na pintura
   * do botao.
   */
  public void setDownColor(Color downColor) {
    this.downColor = downColor;
  }//setdownColor()

  /**
   * Retorna a cor de cima utilizada na pintura
   * do botao.
   * @return cor de cima utilizada na pintura
   * do botao.
   */
  public Color getUpColor() {
    return upColor;
  }//getUpColor()

  /**
   * Seta a cor de cima utilizada na pintura
   * do botao.
   * @param upColor cor de cima utilizada na pintura
   * do botao.
   */
  public void setUpColor(Color upColor) {
    this.upColor = upColor;
  }//setupColor()

  /**
   * Retorna a cor da fonte utilizada na pintura
   * do botao.
   * @return cor da fonte utilizada na pintura
   * do botao.
   */
  public Color getFontColor() {
    return fontColor;
  }//getFontColor()

  /**
   * Seta a cor da fonte utilizada na pintura
   * do botao.
   * @param fontColor cor da fonte utilizada na pintura
   * do botao.
   */
  public void setFontColor(Color fontColor) {
    this.fontColor = fontColor;
  }//setfontColor()

  /**
   * Retorna a fonte utilizada para exibir
   * o texto do botao
   * @return A fonte utilizada para exibir
   * o texto do botao
   */
  public Font getFont() {
    return font;
  }//getFont()

  /**
   * Seta a fonte utilizada para exibir
   * o texto do botao.
   * @param font fonte utilizada para exibir
   * o texto do botao.
   */
  public void setFont(Font font) {
    this.font = font;
  }//setfont()

  /**
   * Copia os atributos da classe para outra
   * instancia de <code>PaintStyle</code>.
   * @return Nova instancia de <code>PaintStyle</code>
   * com os atributos iguais.
   */
  @Override
  public PaintStyle clone() {
    PaintStyle ps = new PaintStyle(
      this.getUpColor(),
      this.getDownColor(),
      this.getFontColor()
    );
    ps.setGradientPaintEnabled(
      this.isGradientPaintEnabled()
    );
    ps.setFont(this.getFont());
    return ps;
  }//clone()

  public static void main(String[] args)
  {
    Color c = new Color(55, 188, 196);
    System.out.println( c );
    Color cb = PaintStyle.brighter(c, .26);
    System.out.println( cb );
  }

}//PaintStyle.class
