/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package com.jpower.spj;

/**
 * <span style="font-size: 15px;">
 * Representa uma mensagem de erro ou alerta
 * gerado por <code>ShellParser</code> na análise
 * da lista de opções e argumentos informados na
 * execução do programa.</span>
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 07/11/2012
 */
public class Error {
  
  /**
   * <span style="font-size: 15px;">
   * Representa o nível de severidade
   * da mensagem de erro <code>(WARN | ERROR)</code>.
   * </span>
   * @see com.jpower.spj.Error
   */
  public static enum LEVEL {
    WARN, ERROR;
  }
  
  
  private LEVEL level;
  
  private String text;
  
  
  /**
   * Construtor padrão que recebe o nível
   * de severidade e o texto da mensagem 
   * de erro.
   * @param lev Nível de severidade.
   * @param text Texto da mensagem.
   */
  public Error(LEVEL lev, String text) {
    this.level = lev;
    this.text = text;
  }
  
  
  /**
   * Verifica se o nível de severidade
   * da mensagem é <code>Level.ERROR</code>.
   * @return <code>true</code> se o nível de
   * severidade da mensagem de erro está
   * definido como <code>Level.ERROR</code>,
   * <code>false</code> caso contrário.
   */
  public boolean isError() {
    return level == LEVEL.ERROR;
  }
  
  
  /**
   * Verifica se o nível de severidade
   * da mensagem é <code>Level.WARN</code>.
   * @return <code>true</code> se o nível de
   * severidade da mensagem de erro está
   * definido como <code>Level.WARN</code>,
   * <code>false</code> caso contrário.
   */
  public boolean isWarning() {
    return level == LEVEL.WARN;
  }


  /**
   * Retorna o nível de severidade
   * configurado para a mensagem.
   * @return Level
   */
  public LEVEL getLevel() {
    return level;
  }


  /**
   * Define o nível de severidade
   * configurado para a mensagem.
   * @param level Level
   * @return Esta instância modificada
   * de <code>Error</code>
   */
  public Error setLevel(LEVEL level) {
    this.level = level;
    return this;
  }


  /**
   * Retorna o texto da mensagem
   * @return String.
   */
  public String getText() {
    return text;
  }


  /**
   * Define o texto da mensagem de erro.
   * @param text String
   * @return Esta instância modificada
   * de <code>Error</code>.
   */
  public Error setText(String text) {
    this.text = text;
    return this;
  }
  
  
  /**
   * <code>* [Level]: text;</code>
   * @return String <code>* [Level]: text;</code>
   */
  @Override
  public String toString() {
    return "* ["+ level.name()+ "]: "+ text;
  }
  
}
