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
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 05/06/2015
 */
public class PrintStreamOutput extends AbstractLogOutput {
  
  private PrintStreamFactory factory;
  
  PrintStream print;
  
  
  public PrintStreamOutput(PrintStreamFactory fact, OutputFormatter fmt) {
    super(fmt);
    if(fact == null)
      throw new IllegalArgumentException("Invalid PrintStreamFactory: "+ fact);
    print = null;
    factory = fact;
  }
  
  
  public PrintStreamOutput(PrintStreamFactory fact) {
    this(fact, OutputFormatterFactory.standardFormatter());
  }
  
  
  public void setPrintStreamFactory(PrintStreamFactory fact) {
    if(fact == null)
      throw new IllegalArgumentException("Invalid PrintStreamFactory: "+ fact);
    this.factory = fact;
  }
  
  
  public PrintStreamFactory getPrintStreamFactory() {
    return factory;
  }
  
  
  public PrintStream getPrinter() {
    if(print == null)
      print = factory.create();
    return print;
  }


  @Override
  public PrintStreamOutput log(LogLevel lvl, String msg) {
    if(levels.isLevelEnabled(lvl)) {
      getPrinter().println(msg);
    }
    return this;
  }


  @Override
  public void close() {
    if(print != null) {
      print.flush();
      print.close();
    }
  }

}
