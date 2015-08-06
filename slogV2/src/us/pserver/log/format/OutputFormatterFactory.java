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

/**
 * Factory for <code>OutputFormatter</code>
 * objects, allows configure a formatter in any
 * style for log strings.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public class OutputFormatterFactory {
  
  /**
   * <code>
   *  LEVEL_LENGTH = 5;
   * </code>
   * Max size of the string log level.
   */
  public static final int LEVEL_LENGTH = 5;
  
  
  private final List<String> args;
  
  private DateFormat dfm;
  
  
  /**
   * Default constructor without arguments.
   */
  public OutputFormatterFactory() {
    args = new LinkedList();
    dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  }
  
  
  /**
   * Set a <code>DateFormat</code> object for formatting dates.
   * @param df A <code>DateFormat</code> object for formatting dates.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   */
  public OutputFormatterFactory setDateFormat(DateFormat df) {
    if(df != null) dfm = df;
    return this;
  }
  
  
  /**
   * Set a <code>DateFormat</code> pattern for formatting dates.
   * @param df A <code>DateFormat</code> pattern for formatting dates.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   * @see java.text.DateFormat
   */
  public OutputFormatterFactory setDatePattern(String pattern) {
    if(pattern != null) 
      dfm = new SimpleDateFormat(pattern);
    return this;
  }
  
  
  /**
   * Append a static string in the formatted log output.
   * @param str The static string to be appended.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   */
  public OutputFormatterFactory append(String str) {
    if(str != null)
      args.add(str);
    return this;
  }
  
  
  /**
   * Append a static char in the formatted log output.
   * @param str The static char to be appended.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   */
  public OutputFormatterFactory append(char ch) {
    args.add(String.valueOf(ch));
    return this;
  }
  
  
  /**
   * Append a log mark that will be replaced in the 
   * final log string by his respective value.
   * @param tag The <code>LogMark</code> to append.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   * @see us.pserver.log.format.LogTag
   */
  public OutputFormatterFactory append(LogTag tag) {
    if(tag == null)
      throw new IllegalArgumentException("Invalid LogTag: null");
    args.add(tag.getTag());
    return this;
  }
  
  
  /**
   * Append a log LEVEL mark that will be replaced in the 
   * final log string by his respective value.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   * @see us.pserver.log.format.LogTag
   */
  public OutputFormatterFactory appendLevel() {
    return append(LogTag.LEVEL);
  }
  
  
  /**
   * Append a log DATE mark that will be replaced in the 
   * final log string by his respective value.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   * @see us.pserver.log.format.LogTag
   */
  public OutputFormatterFactory appendDate() {
    return append(LogTag.DATE);
  }
  
  
  /**
   * Append a log MESSAGE mark that will be replaced in the 
   * final log string by his respective value.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   * @see us.pserver.log.format.LogTag
   */
  public OutputFormatterFactory appendMessage() {
    return append(LogTag.MESSAGE);
  }
  
  
  /**
   * Append a log NAME mark that will be replaced in the 
   * final log string by his respective value.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   * @see us.pserver.log.format.LogTag
   */
  public OutputFormatterFactory appendName() {
    return append(LogTag.NAME);
  }
  
  
  /**
   * Append a log LINE mark that will be replaced in the 
   * final log string by his respective value.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   * @see us.pserver.log.format.LogTag
   */
  public OutputFormatterFactory appendLine() {
    return append(LogTag.LINE);
  }
  
  
  /**
   * Reset the configurations of this <code>OutputFormatterFactory</code> instance.
   * @return This modified <code>OutputFormatterFactory</code> instance.
   */
  public OutputFormatterFactory reset() {
    args.clear();
    return this;
  }
  
  
  /**
   * Create an <code>OutputFormatter</code> instance with the configured pattern.
   * @return An <code>OutputFormatter</code> instance with the configured pattern.
   */
  public OutputFormatter create() {
    return new OutputFormatter() {
      @Override
      public String format(LogLevel lvl, Date dte, String name, int line, String msg) {
        StringBuilder sb = new StringBuilder();
        for(String s : args) {
          if(LogTag.DATE.match(s))
            sb.append(dfm.format(dte));
          else if(LogTag.LEVEL.match(s))
            sb.append(justify(lvl.toString(), LEVEL_LENGTH));
          else if(LogTag.MESSAGE.match(s))
            sb.append(msg);
          else if(LogTag.NAME.match(s))
            sb.append(name);
          else if(LogTag.LINE.match(s))
            sb.append(line);
          else
            sb.append(s);
        }
        return sb.toString();
      }
    };
  }
  
  
  /**
   * Create an instance of <code>OutputFormatterFactory</code>.
   * @return An instance of <code>OutputFormatterFactory</code>.
   */
  public static OutputFormatterFactory factory() {
    return new OutputFormatterFactory();
  }
  
  
  /**
   * Get an <code>OutputFormatter</code> configured with default log pattern.
   * @return An <code>OutputFormatter</code> configured with default log pattern.
   */
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