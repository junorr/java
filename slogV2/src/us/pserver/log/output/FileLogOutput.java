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
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implements a <code>LogOutput</code> which redirects logs to file.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.1 - 201506
 */
public class FileLogOutput extends PrintStreamOutput {

  private Path path;
  
  
  /**
   * A <code>PrintStream</code> factory for file.
   */
  static class FilePrintStreamFactory implements PrintStreamFactory {
    private final Path path;
    FilePrintStreamFactory(Path p) { path = p; }
    @Override
    public PrintStream create() {
      try {
        return new PrintStream(path.toFile(), "UTF-8");
      } catch(FileNotFoundException | UnsupportedEncodingException e) {
        throw new RuntimeException(e.toString(), e);
      }
    }
  }
  
  
  /**
   * Constructor which receives the log file path and the
   * <code>OutputFormatter</code> object.
   * @param logfile The log file path.
   * @param fmt The <code>OutputFormatter</code> object.
   */
  public FileLogOutput(Path logfile, OutputFormatter fmt) {
    super(new FilePrintStreamFactory(logfile), fmt);
  }
  

  /**
   * Default constructor 
   * @param logfile 
   */
  public FileLogOutput(Path logfile) {
    this(logfile, OutputFormatterFactory.standardFormatter());
  }
  
  
  public FileLogOutput(String logfile, OutputFormatter fmt) {
    this(Paths.get(logfile), fmt);
  }
  
  
  public FileLogOutput(String logfile) {
    this(Paths.get(logfile), OutputFormatterFactory.standardFormatter());
  }
  
}
