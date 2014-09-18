/*
  Direitos Autorais Reservados (c) 2009 Juno Roesler
  Contato com o autor: powernet.de@gmail.com

  Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
  termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
  Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
  versão posterior.

  Esta biblioteca é distribuído na expectativa de que seja útil, porém, SEM
  NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
  OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
  Geral Menor do GNU para mais detalhes.

  Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
  com esta biblioteca; se não, escreva para a Free Software Foundation, Inc., no
  endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
*/

package com.jpower.utils;

/**
 * Justifyer é um justificador de texto.
 * @author Juno Roesler
 */
public class Justifyer {

  public static final String endl = "\n", spc = " ", codln = "&&&";


  /**
   * Construtor único e sem argumentos.
   */
  public Justifyer() {
  }//construtor


  /**
   * Justifica uma linha de texto com o número de
   * colunas menor do o número especificado por
   * <code>cols</code>.
   */
  private String justifyLesser(int cols, String text) {
    if(text == null || text.length() > cols)
      return null;

    int nspc, ispc;
    ispc = text.lastIndexOf(spc);

    if( (text.length() == cols || ispc < 0)
        || text.endsWith(".") )
      return text;

    StringBuffer buf = new StringBuffer();

    nspc = cols - text.length();
    buf.append(text);

    int inicounter = 0;
    for(int i = 0; i < nspc; i++) {
      buf.insert(ispc, spc);

      int ospc = buf.lastIndexOf(spc, ispc-1);
      if(ospc > 0)
        ispc = ospc;
      else {
        //reinicia o indice dos espaços
        ispc = buf.lastIndexOf(spc);
        inicounter++;
        //volta o indice até o inicio da
        //última sequencia de espaços.
        for(int j = 0; j < inicounter; j++)
          ispc = buf.lastIndexOf(spc, ispc);
      }//else
    }//for

    return buf.toString();
  }//method


  /**
   * Justifica um texto formatando-o de acordo com o
   * número de colunas especificado (inserindo quebras
   * de linha).
   */
  private String justifyGreater(int cols, String text, StringBuffer buf) {
    if( (cols <= 0 || text == null) ||
        text.length() <= cols)
      return null;

    if(buf == null)
      buf = new StringBuffer();

    int ispc = text.lastIndexOf(spc, cols);
    int iendl = text.lastIndexOf(endl, cols);

    if(iendl > 0)
      ispc = iendl;

    if(ispc > 0) {
      buf.append(
        justifyLesser(cols,
          text.substring(0, ispc)));

      buf.append(endl);

      text = text.substring(ispc+1);
    } else {
      buf.append(
        text.substring(0, cols));
      buf.append(endl);

      text = text.substring(cols+1);
    }//if-else

    if(text != null && text.length() > cols)
      justifyGreater(cols, text, buf);
    else if(text != null && text.length() > 0)
      buf.append(
        justifyLesser(cols, text));

    return buf.toString();
  }//method


  /**
   * Justifica um texto ao número de colunas indicado.
   * Caso o texto tenha tamanho menor que o número de
   * colunas indicado, serão inseridos espaços para
   * atingir o número de colunas. Caso seja maior,
   * serão inseridas quebras de linha para respeitar
   * o número de colunas informado.
   * @param cols Número de colunas que
   * o testo deve ter.
   * @param text Texto a ser justificado.
   */
  public String justify(int cols, String text) {
    if(text == null || cols <= 0)
      return null;

    text = text.replaceAll(codln, endl);

    if(text.length() > cols)
      return justifyGreater(cols, text, null);
    else
      return justifyLesser(cols, text);
  }//method


}//class
