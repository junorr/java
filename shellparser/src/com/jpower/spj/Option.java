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

import java.util.LinkedList;
import java.util.List;

/**
 * <span style="font-size: 15px;">
 * Opção de execução de um programa, utilizada
 * por <code>ShellParser</code>.</span>
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 06/11/2012
 */
public class Option {
  
  /**
   * Nome de opção vazia que representa apenas
   * argumentos de uma aplicação.
   */
  public static final String EMPTY = "<empty>";
  
  /**
   * Opção vazia que representa apenas argumentos
   * de uma aplicação.
   */
  public static final Option EMPTY_OPTION = 
      new Option(EMPTY).setArgsSeparator(" ")
      .setDescription("App Argument")
      .setLongName("argument").setAcceptArgs(true)
      .setExclusive(false).setMandatory(false);
  
  
  protected String name;
  
  private String longName;
  
  private boolean hasArgs;
  
  private boolean mandatory;
  
  private boolean exclusive;
  
  private boolean present;
  
  private List<String> arguments;
  
  private String argsSeparator;
  
  private String description;
  
  private Object defval;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public Option() {
    name = null;
    longName = null;
    hasArgs = true;
    mandatory = true;
    exclusive = false;
    present = false;
    argsSeparator = " ";
    description = null;
    arguments = new LinkedList<>();
    defval = null;
  }
  
  
  /**
   * Construtor que recebe o nome identificador
   * da opção (Ex: <code>'-h'</code>).
   * @param name Nome da opção.
   */
  public Option(String name) {
    this();
    this.name = name;
  }
  
  
  /**
   * Adiciona um argumento à opção.
   * @param arg Argumento.
   * @return Esta instância modificada de 
   * <code>Option</code>.
   */
  public Option addArg(String arg) {
    if(arg != null && !arg.trim().isEmpty())
      arguments.add(arg);
    return this;
  }
  
  
  /**
   * Retorna a lista de argumentos da opção.
   * @return List&lt;String&gt;
   */
  public List<String> arguments() {
    return arguments;
  }
  
  
  /**
   * Retorn o argumento identificado
   * pelo índice informado, na lista de 
   * argumentos existentes da opção.
   * @param argIndex Índice da lista
   * de argumentos.
   * @return O argumento identificado
   * pelo índice informado na lista
   * de argumentos.
   */
  public String getArg(int argIndex) {
    if(arguments.isEmpty()
        || argIndex < 0 
        || argIndex >= arguments.size()) return null;
    return arguments.get(argIndex);
  }
  
  
  /**
   * Retorn o primeiro argumento da lista de 
   * argumentos existentes da opção, caso exista.
   * @return O primeiro argumento da
   * lista de argumentos.
   */
  public String getFirstArg() {
    return this.getArg(0);
  }
  
  
  /**
   * Retorn como número inteiro o argumento 
   * identificado pelo índice informado, 
   * na lista de argumentos existentes da opção.
   * @param argIndex Índice da lista
   * de argumentos.
   * @return O argumento identificado
   * pelo índice informado na lista
   * de argumentos.
   */
  public int getAsInteger(int argIndex) {
    try {
      return Integer.parseInt(arguments.get(argIndex));
    } catch(Exception e) {
      return -1;
    }
  }


  /**
   * Retorna o nome identificador configurado 
   * para a opção
   * @return Nome da opção.
   */
  public String getName() {
    if(name.equals(EMPTY))
      return longName;
    return name;
  }


  /**
   * Define o nome identificador da opção
   * (Ex: <code>'-h'</code>).
   * @param name Nome da opção.
   * @return Esta instância modificada de
   * <code>Option</code>.
   */
  public Option setName(String name) {
    this.name = name;
    return this;
  }
  
  
  /**
   * Retorna o nome longo configurada
   * para a opção.
   * @return Nome longo da opção.
   */
  public String getLongName() {
    return longName;
  }


  /**
   * Define o nome longo de identificação da
   * opção (Ex: <code>'--help'</code>).
   * @param longName Nome longo da opção.
   * @return Esta instância modificada de
   * <code>Option</code>.
   */
  public Option setLongName(String longName) {
    this.longName = longName;
    return this;
  }


  /**
   * Verifica se a opção aceita argumentos.
   * @return <code>true</code> se a opção
   * aceita argumentos, <code>false</code>
   * caso contrário.
   */
  public boolean acceptArgs() {
    return hasArgs;
  }


  /**
   * Define se a opção aceita argumentos.
   * @param accept <code>true</code> para
   * a opção aceitar argumentos, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de 
   * <code>Option</code>.
   */
  public Option setAcceptArgs(boolean accept) {
    this.hasArgs = accept;
    return this;
  }
  
  
  /**
   * Verifica se a presença da opção é obrigatória
   * à execução do programa.
   * @return <code>true</code> se
   * a presença da opção seja obrigatória
   * à execução do programa, <code>false</code>
   * caso contrário.
   */
  public boolean isMandatory() {
    return mandatory;
  }


  /**
   * Define se a presença da opção deve ser obrigatória
   * à execução do programa.
   * @param mandatory <code>true</code> para
   * que a presença da opção seja obrigatória
   * à execução do programa, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de 
   * <code>Option</code>.
   */
  public Option setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
    return this;
  }


  /**
   * Verifica se a opção é exclusiva na lista
   * de opções, ou seja, se deve existir apenas
   * esta opção na lista informada para
   * execução do programa.
   * @return <code>true</code> se
   * a opção é exclusica e não aceita outras opções
   * na mesma lista informada para 
   * execução do programa, <code>false</code>
   * caso contrário.
   */
  public boolean isExclusive() {
    return exclusive;
  }


  /**
   * Define se a opção deve ser exclusiva na lista
   * de opções, ou seja, se deve existir apenas
   * esta opção na lista informada para
   * execução do programa.
   * @param exclusive <code>true</code> para definir
   * a opção como exclusica e não aceitará outras opções
   * na mesma lista informada para 
   * execução do programa, <code>false</code>
   * caso contrário.
   * @return Esta instância modificada de 
   * <code>Option</code>.
   */
  public Option setExclusive(boolean exclusive) {
    this.exclusive = exclusive;
    return this;
  }


  /**
   * Verifica se a opção foi informada
   * na lista de opções para execução do programa.
   * @return <code>true</code> caso a opção
   * tenha sido informada na lista de
   * opções, <code>false</code> caso contrário.
   */
  public boolean isPresent() {
    return present;
  }


  /**
   * Verifica se a opção foi informada
   * na lista de opções para execução do programa.
   * @param present <code>true</code> se a opção
   * foi informada na lista de opções, 
   * <code>false</code> caso contrário.
   * @return Esta instância modificada de 
   * <code>Option</code>.
   */
  public Option setPresent(boolean present) {
    this.present = present;
    return this;
  }
  
  
  /**
   * Verifica se esta opção é vazia e representa
   * apenas argumentos diretos informados na 
   * execução do programa.
   * @return <code>true</code> se a opção é
   * uma opção vazia, <code>façse</code>
   * caso contrário.
   */
  public boolean isEmptyOption() {
    return name != null && name.equals(EMPTY);
  }


  /**
   * Retorna o caracter de separação do argumento e
   * da opção em si (Ex: <code>-n' 'argument</code>
   * @return Caracter de separação do argumento
   * da opção.
   */
  public String getArgsSeparator() {
    return argsSeparator;
  }


  /**
   * Define o caracter de separação do argumento e
   * da opção em si (Ex: <code>-n' 'argument</code>
   * @param argsSeparator Caracter de separação do argumento
   * da opção.
   * @return Esta instância modificada de 
   * <code>Option</code>.
   */
  public Option setArgsSeparator(String argsSeparator) {
    this.argsSeparator = argsSeparator;
    return this;
  }


  /**
   * Retorna a descrição da funcionalidade
   * da opção.
   * @return String
   */
  public String getDescription() {
    return description;
  }


  /**
   * Define a descrição da funcionalidade da opção.
   * @param description Descrição da opção.
   * @return Esta instância modificada de 
   * <code>Option</code>.
   */
  public Option setDescription(String description) {
    this.description = description;
    return this;
  }


  /**
   * Retorna o valor padrão assumido pela
   * opção, caso não tenha sido informado na lista
   * de opções para execução do programa.
   * @return Valor padrão do argumento da opção.
   */
  public Object getDefaultvalue() {
    return defval;
  }


  /**
   * Define o valor padrão assumido pela
   * opção, caso não tenha sido informado na lista
   * de opções para execução do programa.
   * @param defval Valor padrão do argumento da opção.
   * @return Esta instância modificada de 
   * <code>Option</code>.
   */
  public Option setDefaultvalue(Object defval) {
    this.defval = defval;
    return this;
  }


  /**
   * Verifica se a opção possui nome de
   * identificação e separador de argumentos
   * configurados.
   * @return <code>true</code> se as propriedades
   * nome e separador de argumentos foram configurados,
   * <code>false</code> casp comtrário.
   */
  public boolean validate() {
    return name != null
        && !name.trim().isEmpty()
        && ((hasArgs && argsSeparator != null)
        || !hasArgs);
  }


  @Override
  public String toString() {
    return "Option{" + "name=" + name + ", longName=" + longName + ", mandatory=" + mandatory + ", arguments=" + arguments + '}';
  }

}
