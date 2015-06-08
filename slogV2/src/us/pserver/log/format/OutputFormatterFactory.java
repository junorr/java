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

package us.pserver.log.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import us.pserver.log.LogLevel;
import us.pserver.log.format.OutputFormatter;

/**
 * <b>OutputFormatter</b> formata a saída de mensagens 
 * de log de acordo com seu nível de importância 
 * e parâmetros configurados.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/04/2014
 */
public class OutputFormatterFactory {
  
  /**
   * Tamanho máximo do texto que representa 
   * o nível de log na mensagem <code>(5)</code>.
   */
  public static final int LEVEL_LENGTH = 5;
  
  
  private final List<String> args;
  
  private DateFormat dfm;
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public OutputFormatterFactory() {
    args = new LinkedList();
    dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  }
  
  
  /**
   * Define o formatador de datas utilizado nas 
   * mensagens de log.
   * @param df Formatador <code>DateFormat</code>.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public OutputFormatterFactory setDateFormat(DateFormat df) {
    if(df != null) dfm = df;
    return this;
  }
  
  
  /**
   * Define o formato de saída de datas utilizadas nas 
   * mensagens de log.
   * @param pattern Formato de saída de datas.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   * @see java.text.DateFormat
   */
  public OutputFormatterFactory setDatePattern(String pattern) {
    if(pattern != null) 
      dfm = new SimpleDateFormat(pattern);
    return this;
  }
  
  
  /**
   * Anexa o texto especificado na saída de log 
   * formatada.
   * @param str Texto a ser anexado na saída 
   * de log formatada.
   * @return Esta instância modificada de <code>OutputFormatter</code>.
   */
  public OutputFormatterFactory append(String str) {
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
  public OutputFormatterFactory append(char ch) {
    args.add(String.valueOf(ch));
    return this;
  }
  
  
  public OutputFormatterFactory append(LogMark mark) {
    if(mark == null)
      throw new IllegalArgumentException("Invalid null LogMark");
    args.add(mark.getMark());
    return this;
  }
  
  
  public OutputFormatterFactory appendLevel() {
    return append(LogMark.LEVEL);
  }
  
  
  public OutputFormatterFactory appendDate() {
    return append(LogMark.DATE);
  }
  
  
  public OutputFormatterFactory appendMessage() {
    return append(LogMark.MESSAGE);
  }
  
  
  public OutputFormatterFactory appendName() {
    return append(LogMark.NAME);
  }
  
  
  public OutputFormatterFactory reset() {
    args.clear();
    return this;
  }
  
  
  public OutputFormatter create() {
    return (level, date, name, msg)-> {
      StringBuilder sb = new StringBuilder();
      args.forEach(s-> {
        if(LogMark.DATE.match(s))
          sb.append(dfm.format(date));
        else if(LogMark.LEVEL.match(s))
          sb.append(justify(level.toString(), LEVEL_LENGTH));
        else if(LogMark.MESSAGE.match(s))
          sb.append(msg);
        else if(LogMark.NAME.match(s))
          sb.append(name);
        else
          sb.append(s);
      });
      return sb.toString();
    };
  }
  
  
  public static OutputFormatterFactory factory() {
    return new OutputFormatterFactory();
  }
  
  
  public static OutputFormatter standardFormatter() {
    return new OutputFormatterFactory()
        .appendDate()
        .append("  [")
        .appendLevel()
        .append("]  ")
        .appendName()
        .append(" - ")
        .appendMessage()
        .create();
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
  
}