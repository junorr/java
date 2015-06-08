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
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2015
 */
public class PatternOutputFormatter implements OutputFormatter {

  private OutputFormatter formatter;
  
  private DateFormat dtfm;
  
  private String pattern;
  
  
  public PatternOutputFormatter(String pattern) {
    this.init(pattern);
  }
  
  
  private void init(String pattern) {
    if(pattern == null || pattern.trim().isEmpty())
      throw new IllegalArgumentException("Invalid string pattern: '"+ pattern+ "'");
    
    this.pattern = pattern;
    OutputFormatterFactory fact = OutputFormatterFactory.factory();
    class IntTupple {
      private int i1, i2;
      IntTupple(int i1, int i2) { 
        this.i1 = i1; this.i2 = i2;
      }
      int getFirst() { return i1; }
      int getSecond() { return i2; }
    }
    
    List<IntTupple> index = new LinkedList();
    int id = 0;
    int i = -1;
    while((i = pattern.indexOf("{", id)) >= 0) {
      int i2 = pattern.indexOf("}", i);
      if(i2 < 0) 
        throw new IllegalArgumentException("Invalid Pattern. No closing token '}' for index ["+ i+ "]");
      id = i2;
      index.add(new IntTupple(i, i2));
    }
    
    int start = 0;
    for(int j = 0; j < index.size(); j++) {
      IntTupple t = index.get(j);
      fact.append(pattern.substring(start, t.getFirst()));
      fact.append(pattern.substring(t.getFirst(), t.getSecond()+1));
      start = t.getSecond()+ 1;
    }
    if(dtfm != null) fact.setDateFormat(dtfm);
    formatter = fact.create();
  }
  
  
  public String getPattern() {
    return pattern;
  }
  
  
  public PatternOutputFormatter setDateFormat(String format) {
    dtfm = new SimpleDateFormat(format);
    init(this.pattern);
    return this;
  }


  public PatternOutputFormatter setDateFormat(DateFormat df) {
    if(df == null) 
      throw new IllegalArgumentException("Invalid null DateFormat");
    dtfm = df;
    init(this.pattern);
    return this;
  }


  @Override
  public String format(LogLevel lvl, Date dte, String name, String msg) {
    return formatter.format(lvl, dte, name, msg);
  }
  
}
