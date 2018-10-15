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

package us.pserver.jpx.log;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;
import us.pserver.tools.StringPad;
import us.pserver.tools.date.DateTime;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/08/2018
 */
public class LogFormat {
  
  public static enum Var {
    CLASS("@CLS"), 
    METHOD("@MTH"), 
    LINE_NUMBER("@LNR"), 
    LEVEL("@LVL"), 
    INSTANT("@INS"), 
    MESSAGE("@MSG");
    
    private Var(String tag) {
      this.tag = tag;
    }
    
    private final String tag;
    
    @Override
    public String toString() {
      return tag;
    }
    
    public static Var parse(String tag) {
      Objects.requireNonNull(tag);
      switch(tag) {
        case "@CLS":
          return CLASS;
        case "@MTH":
          return METHOD;
        case "@LNR":
          return LINE_NUMBER;
        case "@LVL":
          return LEVEL;
        case "@INS":
          return INSTANT;
        case "@MSG":
          return MESSAGE;
        default:
          throw new IllegalArgumentException(String.format("Unknown Var tag (%s)", tag));
      }
    }
    
  }
  
  public static final Function<Instant,String> DEFAULT_INSTANT_FORMAT = i->DateTime.of(i).toLocalDT().toString();
  
  private final String fmt;
  
  private final Function<Instant,String> instf;
  
  public LogFormat(String msg, Function<Instant,String> format) {
    this.fmt = Objects.requireNonNull(msg);
    instf = Objects.requireNonNull(format);
  }
  
  public LogFormat(String msg) {
    this(msg, DEFAULT_INSTANT_FORMAT);
  }
  
  public String getMessage() {
    return fmt;
  }
  
  public LinkedList<Var> parse() {
    LinkedList<Var> vars = new LinkedList<>();
    int idx = 0;
    while((idx = fmt.indexOf("@", idx)) >= 0) {
      String tag = fmt.substring(idx, idx + 4);
      try {
        vars.add(Var.parse(tag));
      } catch(IllegalArgumentException e) {
        String err = e.getMessage() + "\n"
            + StringPad.of("").cpad("-", fmt.length() + 1) + "\n"
            + fmt + "\n"
            + StringPad.of("^^^^").lpad(" ", idx + 4) + "\n"
            + StringPad.of("").lpad("-", fmt.length() + 1) + "\n";
        throw new IllegalArgumentException(err);
      }
      idx++;
    }
    return vars;
  }
  
  public String format(Log.Level lvl, String className, String methodName, int line, String msg, Instant inst) {
    StringBuilder bld = new StringBuilder();
    LinkedList<Var> vars = parse();
    int skip = 0;
    for(char c : fmt.toCharArray()) {
      if(skip > 0) {
        skip--;
      }
      else if(c == '@') {
        skip = 3;
        Var v = vars.pollFirst();
        switch(v) {
          case CLASS:
            bld.append(className);
            break;
          case LEVEL:
            bld.append(lvl);
            break;
          case LINE_NUMBER:
            bld.append(line);
            break;
          case MESSAGE:
            bld.append(msg);
            break;
          case METHOD:
            bld.append(methodName);
            break;
          case INSTANT:
            bld.append(instf.apply(inst));
            break;
          default:
            throw new UnsupportedOperationException();
        }
      }
      else {
        bld.append(c);
      }
    }
    return bld.toString();
  }
  
  public String format(Log.Level lvl, String className, String methodName, int line, String msg) {
    return this.format(lvl, className, methodName, line, msg, Instant.now());
  }
  
}
