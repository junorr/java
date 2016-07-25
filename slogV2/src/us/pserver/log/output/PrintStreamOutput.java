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

package us.pserver.log.output;

import us.pserver.log.format.OutputFormatterFactory;
import us.pserver.log.format.OutputFormatter;
import java.io.PrintStream;
import us.pserver.log.LogLevel;

/**
 * An implementation of <code>LogOutput</code> which
 * redirect log messages to a <code>PrintStream</code>.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public class PrintStreamOutput extends AbstractLogOutput {
  
  /**
   * Synchronized object for thread safety.
   */
  private static final Object SYNC = new Object();
  
  private PrintStreamFactory factory;
  
  PrintStream print;
  
  
  /**
   * Constructor which receives the factory of
   * <code>PrintStream</code> objects for printing log messages
   * and the formatter for log outputs.
   * @param fact The factory which will create the 
   * <code>PrintStream</code> object when needed.
   * @param fmt The formatter for log outputs.
   */
  public PrintStreamOutput(PrintStreamFactory fact, OutputFormatter fmt) {
    super(fmt);
    if(fact == null)
      throw new IllegalArgumentException("Invalid PrintStreamFactory: "+ fact);
    print = null;
    factory = fact;
  }
  
  
  /**
   * Default constructor receives the factory of
   * <code>PrintStream</code> objects for printing log messages.
   * @param fact The factory which will create the 
   * <code>PrintStream</code> object when needed.
   */
  public PrintStreamOutput(PrintStreamFactory fact) {
    this(fact, OutputFormatterFactory.standardFormatter());
  }
  
  
  /**
   * Constructor which receives the <code>PrintStream</code> 
   * object for printing log messages.
   * @param fact The <code>PrintStream</code> object for printing log messages.
   */
  public PrintStreamOutput(final PrintStream ps) {
    this(new PrintStreamFactory() {
      @Override
      public PrintStream create() {
        return ps;
      }
    });
  }
  
  
  /**
   * Set the factory of <code>PrintStream</code> 
   * objects for printing log messages.
   * @param fact The factory of <code>PrintStream</code> 
   * objects for print log messages.
   */
  public void setPrintStreamFactory(PrintStreamFactory fact) {
    if(fact == null)
      throw new IllegalArgumentException("Invalid PrintStreamFactory: "+ fact);
    this.factory = fact;
  }
  
  
  /**
   * Get the factory of <code>PrintStream</code> 
   * objects for printing log messages.
   * @return The factory to create <code>PrintStream</code> 
   * objects for print log messages.
   */
  public PrintStreamFactory getPrintStreamFactory() {
    return factory;
  }
  
  
  /**
   * Return the <code>PrintStream</code> object
   * current in use by this <code>LogOutput</code>.
   * @return the <code>PrintStream</code> object
   * current in use by this <code>LogOutput</code>.
   */
  public PrintStream getPrinter() {
    synchronized(SYNC) {
      if(print == null)
        print = factory.create();
      return print;
    }
  }


  @Override
  public PrintStreamOutput log(LogLevel lvl, String msg) {
    synchronized(SYNC) {
      if(levels.isLevelEnabled(lvl)) {
        getPrinter().println(msg);
      }
      return this;
    }
  }


  @Override
  public void close() {
    if(print != null) {
      print.flush();
      print.close();
    }
  }

}
