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

package us.pserver.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import us.pserver.log.output.OutputFormatter;

/**
 * <b>OutputFormatter</b> formata a saída de mensagens 
 * de log de acordo com seu nível de importância 
 * e parâmetros configurados.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/04/2014
 */
public class BasicOutputFormatter implements OutputFormatter {
  
  /**
   * Tamanho máximo do texto que representa 
   * o nível de log na mensagem <code>(5)</code>.
   */
  public static final int LEVEL_LENGTH = 5;
  
  /**
   * Texto que será substituído pelo nível 
   * de log da mensagem <code>("LEVEL")</code>.
   */
  public static final String LEVEL = "LEVEL";
  
  /**
   * Texto que será substituído pela data
   * da mensagem <code>("DATE")</code>.
   */
  public static final String DATE = "DATE";
  
  /**
   * Texto que será substituído pela mensagem
   * de log <code>("MESSAGE")</code>.
   */
  public static final String MESSAGE = "MESSAGE";
  

  private final List<String> args;
  
  private DateFormat dfm;
  
  private static BasicOutputFormatter instance;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public BasicOutputFormatter() {
    args = new LinkedList();
    dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    instance = this;
  }
  
  
  /**
   * Define o formatador de datas utilizado nas 
   * mensagens de log.
   * @param df Formatador <code>DateFormat</code>.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public BasicOutputFormatter dateFormat(DateFormat df) {
    if(df != null) dfm = df;
    return this;
  }
  
  
  /**
   * Define o formato de saída de datas utilizadas nas 
   * mensagens de log.
   * @param fmt Formato de saída de datas.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   * @see java.text.DateFormat
   */
  public BasicOutputFormatter dateFormat(String fmt) {
    if(fmt != null) 
      dfm = new SimpleDateFormat(fmt);
    return this;
  }
  
  
  /**
   * Anexa o texto especificado na saída de log 
   * formatada.
   * @param str Texto a ser anexado na saída 
   * de log formatada.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public BasicOutputFormatter append(String str) {
    if(str != null)
      args.add(str);
    return this;
  }
  
  
  /**
   * Anexa o caractere especificado na saída de log 
   * formatada.
   * @param ch Caractere a ser anexado na saída 
   * de log formatada.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public BasicOutputFormatter append(char ch) {
    args.add(String.valueOf(ch));
    return this;
  }
  
  
  /**
   * Anexa o nível de log na saída formatada.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public BasicOutputFormatter appendLevel() {
    args.add(LEVEL);
    return this;
  }
  
  
  /**
   * Anexa a data do log na saída formatada.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public BasicOutputFormatter appendDate() {
    args.add(DATE);
    return this;
  }
  
  
  /**
   * Anexa a mensagem de log na saída formatada.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public BasicOutputFormatter appendMessage() {
    args.add(MESSAGE);
    return this;
  }
  
  
  /**
   * Retorna uma instância de <code>OutputFormatter</code>.
   * @return instância de <code>OutputFormatter</code>.
   */
  public static BasicOutputFormatter instance() {
    if(instance == null)
      instance = new BasicOutputFormatter();
    return instance;
  }
  
  
  /**
   * Reseta <code>OutputFormatter</code>, zerando as especificações
   * de formatação.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public BasicOutputFormatter reset() {
    args.clear();
    return this;
  }
  
  
  /**
   * Retorna uma instância de <code>OutputFormatter</code>
   * configurado para formatar mensagens de log 
   * níveis erro e fatal.
   * @return Instância pré-configurada de <code>OutputFormatter</code>.
   */
  public static BasicOutputFormatter errorFormatter() {
    return new BasicOutputFormatter()
        .append("# ")
        .append("(")
        .appendDate()
        .append(") [")
        .appendLevel()
        .append("]: ")
        .appendMessage();
  }
  
  
  /**
   * Retorna uma instância de <code>OutputFormatter</code>
   * configurado para formatar mensagens padrão de log.
   * @return Instância pré-configurada de <code>OutputFormatter</code>.
   */
  public static BasicOutputFormatter stdFormatter() {
    return new BasicOutputFormatter()
        .append("* ")
        .append("(")
        .appendDate()
        .append(") [")
        .appendLevel()
        .append("]: ")
        .appendMessage();
  }
  
  
  /**
   * Retorna uma instância de <code>OutputFormatter</code>
   * configurado para formatar mensagens de log 
   * com saída para arquivo.
   * @return Instância pré-configurada de <code>OutputFormatter</code>.
   */
  public static BasicOutputFormatter fileFormatter() {
    return new BasicOutputFormatter()
        .append('[')
        .appendLevel()
        .append("] (")
        .appendDate()
        .append("): ")
        .appendMessage();
  }
  
  
  private String justify(String str, int size) {
    if(str == null || size < 1) return null;
    if(size == str.length()) return str;
    if(size < str.length())
      return str.substring(0, size);
    int max = size - str.length();
    for(int i = 0; i < max; i++) {
      str += " ";
    }
    return str;
  }
  
  
  /**
   * Formata a mensagem de log e nível especificados
   * de acordo com as configurações definidas em
   * <code>OutputFormatter</code>.
   * @param message Mensagem de log.
   * @param level Nível de log.
   * @return A mensagem de log formatada para saída.
   * @see us.pserver.log.LogLevel
   */
  @Override
  public String format(LogLevel level, String message) {
    return format(level, new Date(), message);
  }


  @Override
  public String format(LogLevel lvl, Date dte, String msg) {
    if(msg == null || lvl == null)
      return null;
    StringBuilder sb = new StringBuilder();
    args.forEach(s-> {
      switch(s) {
        case LEVEL:
          sb.append(justify(lvl.name(), LEVEL_LENGTH));
          break;
        case DATE:
          sb.append(dfm.format(dte));
          break;
        case MESSAGE:
          sb.append(msg);
          break;
        default:
          sb.append(s);
          break;
      }
    });
    return sb.toString();
  }
  
}