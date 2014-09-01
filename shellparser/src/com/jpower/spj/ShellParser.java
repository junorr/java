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

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * <p style="font-size: 15px;">
 * Classe para análise e classificação de opções 
 * e argumentos para programas de linha comando.
 * Também possui métodos utilitários para construção 
 * do cabeçalho de texto e opções de ajuda em programas
 * de linha de comando.
 * Para utilização é necessário registrar
 * as opções como objetos <code>Option</code> através
 * do método <code>ShellParser.addOption(Option)</code>.
 * </p>
 * <span style='font-size: 16px; font-weight: bold;'>Exemplo de uso:</span>
 * <p style="font-size: 15px;">
 * Como exemplo de uso da biblioteca, vamos imaginar
 * um programa simples de linha de comando, similar ao
 * <i>echo</i>, que simplesmente repete o argumento informado.<br>
 * Nosso programa chama-se <b><code>sayit</code></b> e possui
 * 2 opções além do argumento padrão, <code>'-n'</code> e <code>'-h'</code>.<br>
 * O programa receberá como argumento obrigatório o texto a ser
 * repetido.<br>
 * <b><code>'-n'</code></b> É opcional e terá como argumento o 
 * número de vezes que o texto será repetido. Deverá 
 * ser informada no formato <i><code>'-n=x'</code></i>, onde
 * <code>'x'</code> é o número de repetiçoes.<br>
 * <b><code>'-h'</code></b> Irá mostrar a ajuda de uso do programa
 * e é uma opção exclusiva, ou seja, não pode haver outras opções 
 * no mesmo comando.<br>
 * Primeiro construiremos os objetos <code>ShellParser e Option</code>,
 * para então adicionar-mos as opções pelo método 
 * <code>ShellParser.addOption(Option)</code>:
 * </p>
 * <pre style="font-size: 16px;">
 *  ShellParser shell = new ShellParser();
 *  
 *  // opcionalmente podemos configurar os dados
 *  // da aplicação e mostrar uma boa apresentação
 *  // em texto com o método 
 *  // <i>ShellParser.createHeader(int):String</i>
 * 
 *  shell.setAuthor("Juno Roesler")
 *      .setContact("juno.rr@gmail.com")
 *      .setAppName("sayit")
 *      .setDescription("Echo Like Program");
 *  
 *  Option w = Option.EMPTY_OPTION
 *      .setMandatory(true).setLongName("text")
 *      .setDescription("The text to be repeated");
 *  
 *  Option n = new Option("-n")
 *      .setLongName("--number")
 *      .setMandatory(false)
 *      .setArgsSeparator("=")
 *      .setDefaultvalue(1)
 *      .setDescription("Number of times to repeat");
 *  
 *  Option h = new Option("-h")
 *      .setLongName("--help")
 *      .setMandatory(false)
 *      <b>.setExclusive(true)</b>
 *      .setDescription("Show this help text");
 *  
 *  shell.addOption(w)
 *      .addOption(n)
 *      .addOption(h);
 *
 * 
 * // Aqui terminamos de configurar <i>ShellParser</i>.
 * // Veremos como funciona o programa <b>sayit</b>.
 * 
 * 
 * // Options and arguments to parse
 * // from the command:
 * // <b>sayit "Shell Rock's" -n=3</b>
 * 
 *  args = new String[] {"Shell Rock's", "-n=3"};
 *  
 *  shell.parseArgs(args);
 *  boolean all_ok = shell.parseErrors();
 *  
 *  if(!all_ok || shell.isOptionPresent("-h")) {
 *    
 *    System.out.println(
 *        shell.createHeader(40));
 *    
 *    System.out.println( 
 *        shell.createUsage() );
 *    
 *    shell.printAllMessages(System.out);
 *    
 *  } else if(shell.isOptionPresent(Option.EMPTY)) {
 *    
 *    Option num = shell.getOption("-n");
 *    int times;
 * 
 *    // The option was informed?
 *    if(num.isPresent())
 *      // lets get the first argument
 *      // of the option as an integer
 *      times = num.getAsInteger(0);
 *    else
 *      // else get the default value
 *      times = (int) num.getDefaultvalue();
 *    
 *    for(int i = 0; i &lt; times; i++) {
 *      System.out.println(
 *          shell.getOption(Option.EMPTY).getFirstArg());
 *    }
 *  }
 * </pre>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 06/11/2012
 */
public class ShellParser {
  
  private List<Option> options;
  
  private String name;
  
  private String author;
  
  private String contact;
  
  private String year;
  
  private String description;
  
  private String license;
  
  private String headerComment;
  
  private List<Error> errors;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public ShellParser() {
    options = new LinkedList<>();
    errors = new LinkedList<>();
    name = null;
    author = null;
    contact = null;
    year = "2012";
    description = null;
    headerComment = null;
  }
  
  
  /**
   * Adiciona a opção informada, 
   * caso não exista na lista.
   * @param opt Opção a ser adicionada.
   * @return esta instância modificada de <code>ShellParser</code>
   */
  public ShellParser addOption(Option opt) {
    if(opt.isEmptyOption() && this.containsOption(Option.EMPTY))
      return this;
    if(opt != null && opt.validate() 
        && !this.containsOption(opt.name))
      options.add(opt);
    return this;
  }
  
  
  /**
   * Adiciona a opção informada, 
   * caso não exista na lista.
   * @param opt Opção a ser adicionada.
   * @return esta instância modificada de <code>ShellParser</code>
   */
  public ShellParser addOption(String opt) {
    return this.addOption(new Option(opt));
  }
  
  
  /**
   * Retorna a opção definida pelo nome informado,
   * caso exista.
   * @param name Nome da opção.
   * @return Objeto <code>Option</code> ou <code>null</code>,
   * caso não exista.
   */
  public Option getOption(String name) {
    if(name == null || name.trim().isEmpty()
        || options.isEmpty())
      return null;
    for(Option o : options) {
      if(o.name.equals(name)
          || name.equals(o.getLongName()))
        return o;
    }
    return null;
  }
  
  
  /**
   * Verifica se existe a opção informada.
   * @param name Nome da opção.
   * @return <code>true</code> se a opção
   * informada existir, <code>false</code>
   * caso contrário.
   */
  public boolean containsOption(String name) {
    return this.getOption(name) != null;
  }
  
  
  /**
   * Verifica se a opção existe e se foi encontrada 
   * na análise da lista de opções.
   * @param name Nome da opção.
   * @return <code>true</code> se a opção
   * foi informada na lista de argumentos, 
   * <code>false</code> caso contrário.
   */
  public boolean isOptionPresent(String name) {
    Option o = this.getOption(name);
    return o != null && o.isPresent();
  }
  
  
  /**
   * Retorna a lista de erros encontrados 
   * na análise das opções e argumentos.
   * @return List&lt;Error&gt;
   */
  public List<Error> errors() {
    return errors;
  }
  
  
  /**
   * Retorna a lista de opções adicionadas.
   * @return List&lt;Option&gt;
   */
  public List<Option> options() {
    return options;
  }
  
  
  /**
   * Cria uma String de cabeçalho contendo os 
   * dados da aplicação, como nome, descrição, autor
   * e e-mail de contato.
   * @param size Comprimento das linhas do cabeçalho.
   * @return String do cabeçalho criado.
   */
  public String createHeader(int size) {
    if(size <= 1) size = 36;
    String main = name;
    if(description != null)
      main += " - "+ description;
    return rept('-', size)+ "\n"
        + centralize(main, size) + "\n\n"
        + centralize("Copyright (c) "+ year+ " "+ author, size) + "\n"
        + (headerComment != null ? 
        centralize(headerComment, size) + "\n" : "")
        + (license != null ? 
        centralize("License: "+ license, size) + "\n" : "")
        + centralize("Contact: "+ contact, size) + "\n"
        + rept('-', size)+ "\n";
  }
  
  
  /**
   * Cria uma String de ajuda com as opções possíveis
   * para a aplicação, descrição de uso de cada uma
   * e exemplos de sintaxe.
   * @return String de ajuda com a descrição das
   * opções possíveis da aplicação.
   */
  public String createUsage() {
    String us = "* Usage: "+ name.toLowerCase();
    for(int i = 0; i < options.size(); i++) {
      Option o = options.get(i);
      us += " ";
      if(o.isMandatory())
        us += "<"+ o.getName()+ ">";
      else if(o.isExclusive())
        us += "{"+ o.getName()+ "}";
      else
        us += "["+ o.getName()+ "]";
    }
    
    us += "\n";
    int size = 0;
    for(Option o : options) {
      size = (o.getLongName() != null && o.getLongName().length() > size 
          ? o.getLongName().length() : size);
    }
    
    for(int i = 0; i < options.size(); i++) {
      us += "  ";
      Option o = options.get(i);
      String line = (o.isEmptyOption() ? 
          "<"+ o.getName()+ ">" : o.getName())
          + (o.getLongName() != null 
          && !o.isEmptyOption()
          ? " ("+o.getLongName()+")" : "");
      
      line = alignLeft(line, (size > 0 ? size+5 : size));
      us += line+ ": "+ o.getDescription()+ ";\n";
      
      if(!o.getArgsSeparator().equals(" ") 
          || o.getDefaultvalue() != null) {
        us += rept(' ', line.length() + 4)
            + "Ex: " + o.getName()+ o.getArgsSeparator()
            + (o.getDefaultvalue() != null 
            ? o.getDefaultvalue().toString() : "<arg>;");
        
        if(o.getDefaultvalue() != null)
          us += " (Default value: "
              + o.getDefaultvalue().toString()+ ");";
        us += "\n";
      }
    }
    return us;
  }
  
  
  private String centralize(String s, int size) {
    if(size <= 1) size = 36;
    if(s == null || s.isEmpty()
        || s.length() >= size)
      return s;
    
    int nleft = (size - s.length()) / 2;
    int nright = size - (nleft + s.length());
    return rept(' ', nleft) + s + rept(' ', nright);
  }
  
  
  private String alignLeft(String s, int size) {
    if(s == null || s.isEmpty() || size <= 0)
      return s;
    String sp = rept(' ', size - s.length());
    return s + (sp != null ? sp : "");
  }
  
  
  private String rept(char c, int times) { 
    if(c == 0 || times <= 0)
      return null;
    StringBuilder rept = new StringBuilder();
    for(int i = 0; i < times; i++) {
      rept.append(c);
    }
    return rept.toString();
  }
  
  
  /**
   * Retorna o nome configurado para a aplicação.
   * @return String
   */
  public String getAppName() {
    return name;
  }
  
  
  /**
   * Configura o nome da aplicação, a ser exibido no cabeçalho.
   * @param name Nome da aplicação.
   * @return Esta instância modificada de 
   * <code>ShellParser</code>.
   */
  public ShellParser setAppName(String name) {
    this.name = name;
    return this;
  }
  
  
  /**
   * Retorna o autor configurado para a aplicação.
   * @return String
   */
  public String getAuthor() {
    return author;
  }
  
  
  /**
   * Configura o autor do programa, a ser exibido no cabeçalho.
   * @param author Nome do autor da aplicação.
   * @return Esta instância modificada de
   * <code>ShellParser</code>.
   */
  public ShellParser setAuthor(String author) {
    this.author = author;
    return this;
  }
  
  
  /**
   * Retorna o e-mail de contato configurado 
   * para a aplicação.
   * @return String
   */
  public String getContact() {
    return contact;
  }
  
  
  /**
   * Configura um e-mail de contato com o autor
   * do programa, a ser exibido no cabeçalho.
   * @param contact e-mail de contato.
   * @return Esta instância modificada de 
   * <code>ShellParser</code>.
   */
  public ShellParser setContact(String contact) {
    this.contact = contact;
    return this;
  }
  
  
  /**
   * Retorna o ano de criação configurado para
   * a aplicação.
   * @return String
   */
  public String getYear() {
    return year;
  }
  
  
  /**
   * Configura o ano de criação do programa,
   * a ser exibido no cabeçalho.
   * @param year Ano.
   * @return Esta instância modificada de 
   * <code>ShellParser</code>.
   */
  public ShellParser setYear(String year) {
    this.year = year;
    return this;
  }
  
  
  /**
   * Retorna a descrição configurada para o
   * programa.
   * @return String
   */
  public String getDescription() {
    return description;
  }
  
  
  /**
   * Configura a descrição da aplicação, a ser
   * exibida no cabeçalho.
   * @param description Descrição da aplicação.
   * @return Esta instância modificada de 
   * <code>ShellParser</code>.
   */
  public ShellParser setDescription(String description) {
    this.description = description;
    return this;
  }


  /**
   * Retorna a descrição configurada para o
   * programa.
   * @return String
   */
  public String getLicense() {
    return license;
  }


  /**
   * Configura a licença de uso da aplicação,
   * exibida no cabeçalho.
   * @param license Nome da licença de uso. Ex: GNU LGPL v2.1
   * @return Esta instância modificada de 
   * <code>ShellParser</code>.
   */
  public ShellParser setLicense(String license) {
    this.license = license;
    return this;
  }


  /**
   * Retorna o comentário a ser exibido no cabeçalho.
   * @return String
   */
  public String getHeaderComment() {
    return headerComment;
  }


  /**
   * Configura um comentário a ser exibido no cabeçalho.
   * @param headerComment Comentário do cabeçalho
   * @return Esta instância modificada de 
   * <code>ShellParser</code>.
   */
  public ShellParser setHeaderComment(String headerComment) {
    this.headerComment = headerComment;
    return this;
  }
  
  
  /**
   * Analisa e interpreta a lista de opções
   * e argumentos informados para execução do programa,
   * de acordo com as opções existentes em 
   * <code>ShellParser</code>.
   * @see com.jpower.spj.ShellParser#addOption(com.jpower.spj.Option) 
   * @param args Lista de opções e argumentos a serem
   * analisados.
   * @return Esta instância modificada de 
   * <code>ShellParser</code>.
   */
  public ShellParser parseArgs(String[] args) {
    if(args == null || args.length == 0 
        || options.isEmpty()) return this;
    
    for(int i = 0; i < args.length; i++) {
      String arg = args[i];
      boolean present = false;
      
      for(Option o : options) {
        
        if((o.getLongName() != null 
            && arg.contains(o.getLongName()))
            || arg.startsWith(o.getName())) {
          
          present = true;
          o.setPresent(true);
          
          if(o.acceptArgs() 
              && o.getArgsSeparator().equals(" ") 
              && args.length > (i + 1)) {
            o.addArg(args[++i]);
          }
          
          else if(o.getArgsSeparator().isEmpty()) {
            if(o.getLongName() != null 
                && arg.startsWith(o.getLongName()))
              arg = arg.substring(o.getLongName().length());
            else
              arg = arg.substring(o.getName().length());
            o.addArg(arg);
            
          } else if(arg.contains(o.getArgsSeparator())) {
            o.addArg(arg.split(o.getArgsSeparator())[1]);
          }
        }
      }//for
      
      if(!present && !this.containsOption(Option.EMPTY))
        errors.add(new Error(Error.LEVEL.WARN, 
            "Unknown option: '"+ arg+ "'!"));
      else if(!present && this.containsOption(Option.EMPTY))
        this.getOption(Option.EMPTY)
            .addArg(arg).setPresent(true);
    }
    
    return this;
  }
  
  
  /**
   * Analisa a sintaxe e exigibilidade das opções
   * informadas em <code>parseArgs(String[])</code>,
   * gerando uma lista de mensagens de alertas e erros
   * para as opções, se for o caso.
   * @return <code>true</code> se a análise não
   * encontrar erros na lista de opções,
   * <code>false</code> caso contrário.
   */
  public boolean parseErrors() {
    if(options.isEmpty()) return true;
    
    int presents = 0;
    boolean success = true;
    boolean mandatory = false;
    
    for(Option o : options) {
      if(o.isMandatory() && !o.isPresent()) {
        if(o == Option.EMPTY_OPTION)
          errors.add(new Error(Error.LEVEL.ERROR, "Argument missing"));
        else
          errors.add(new Error(Error.LEVEL.ERROR, "Argument missing for '"+ o.getName()+ "' option"));
      }
    }//for
    
    if(!errors.isEmpty()) success = false;
    
    boolean exclusive = false;
    for(Option o : options) {
      if(o.isExclusive()) {
        if(o.isPresent() && presents > 1) {
          errors.add(new Error(Error.LEVEL.ERROR, 
              "Option '"+ o.getName()+ "' is exclusive!"));
          success = false;
        }
        exclusive = true;
      }
    }
    
    if(!exclusive) {
      for(Option o : options) {
        if(o.isMandatory() && !o.isPresent()) {
          errors.add(new Error(Error.LEVEL.ERROR, 
              "Missing mandatory option '"+ o.getName()+ "'!"));
          success = false;
        } else if(o.isPresent() && o.acceptArgs() && o.arguments().isEmpty()) {
          errors.add(new Error(Error.LEVEL.ERROR, 
              "Missing arguments for '"+ o.getName()+ "' option!"));
          success = false;
        } else if(!o.acceptArgs() && !o.arguments().isEmpty()) {
          errors.add(new Error(Error.LEVEL.ERROR, 
              "Option '"+ o.getName()+ "' should not have arguments!"));
          success = false;
        }
      }
    }
    
    return success;
  }
  
  
  /**
   * Imprime todas as mensagens de erro geradas
   * na análise da lista de opções.
   * @param p <code>PrintStream</code> onde serão 
   * implressas as mensagens.
   * @return Esta instância modificada de <code>ShellParser</code>.
   */
  public ShellParser printErrorMessages(PrintStream p) {
    if(p == null) p = System.out;
    
    for(Error e : errors)
      if(e.isError()) p.println(e);
    
    return this;
  }
  
  
  /**
   * Imprime todas as mensagens de alerta geradas
   * na análise da lista de opções.
   * @param p <code>PrintStream</code> onde serão 
   * implressas as mensagens.
   * @return Esta instância modificada de <code>ShellParser</code>.
   */
  public ShellParser printWarningMessages(PrintStream p) {
    if(p == null) p = System.out;
    
    for(Error e : errors)
      if(e.isWarning()) p.println(e);
    
    return this;
  }
  
  
  /**
   * Imprime todas as mensagens geradas
   * na análise da lista de opções.
   * @param p <code>PrintStream</code> onde serão 
   * implressas as mensagens.
   * @return Esta instância modificada de <code>ShellParser</code>.
   */
  public ShellParser printAllMessages(PrintStream p) {
    if(p == null) p = System.out;
    
    for(Error e : errors)
      p.println(e);
    
    return this;
  }
  
  
  /**
   * Código de exemplo de uso de <code>ShellParser</code>,
   * criando uma pequena aplicação "echo-like" chamada 
   * <code>sayit</code>.
   * @param args Argumentos informados para a execução
   * da aplicação.
   */
  public static void main(String[] args) {
    ShellParser shell = new ShellParser();
    
    shell.setAuthor("Juno Roesler")
        .setContact("juno.rr@gmail.com")
        .setAppName("sayit")
        .setDescription("Echo Like Program");
    
    Option w = Option.EMPTY_OPTION
        .setMandatory(true).setLongName("text")
        .setDescription("The text to be repeated");
    
    Option n = new Option("-n")
        .setLongName("--number")
        .setMandatory(false)
        .setAcceptArgs(true)
        //.setArgsSeparator("=")
        .setDefaultvalue(1)
        .setDescription("Number of times to repeat");
    
    Option h = new Option("-h")
        .setLongName("--help")
        .setAcceptArgs(false)
        .setMandatory(false)
        .setExclusive(true)
        .setDescription("Show this help text");
    
    shell.addOption(w)
        .addOption(n)
        .addOption(h);
    
    // Aqui terminamos de configurar ShellParser.
    // Veremos como funciona o programa sayit.
     
    // Options and arguments to parse
    // from the command:
    // sayit "Shell Rock's" -n=3
    
    args = new String[] {"Shell Rock's"};
    
    shell.parseArgs(args);
    boolean all_ok = shell.parseErrors();
    
    if(!all_ok || shell.isOptionPresent("-h")) {
      
      System.out.println(
          shell.createHeader(40));
      
      System.out.println( 
          shell.createUsage() );
      
      shell.printAllMessages(System.out);
      
    } else if(shell.isOptionPresent(Option.EMPTY)) {
      
      Option num = shell.getOption("-n");
      int times;
      
      // The option was informed?
      if(num.isPresent())
        // lets get the first argument (index=0)
        // of the option as an integer
        times = num.getAsInteger(0);
      else
        // else get the default value
        times = (int) num.getDefaultvalue();
      
      for(int i = 0; i < times; i++) {
        System.out.println(
            shell.getOption(Option.EMPTY).getFirstArg());
      }
    }
  }

}
