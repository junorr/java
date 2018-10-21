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

package us.pserver.tools.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/08/2018
 */
public class FileLog extends StdLog implements Runnable {
  
  public FileLog(LogFormat lf, Path path, Level ... levels) {
    this(lf, printStream(path), Arrays.asList(levels));
  }
  
  public FileLog(LogFormat lf, Path path, Collection<Level> levels) {
    this(lf, printStream(path), levels);
  }
  
  public FileLog(LogFormat lf, File file, Level ... levels) {
    this(lf, printStream(file.toPath()), Arrays.asList(levels));
  }
  
  public FileLog(LogFormat lf, File file, Collection<Level> levels) {
    this(lf, printStream(file.toPath()), levels);
  }
  
  private static PrintStream printStream(Path path) {
    try {
      return new PrintStream(
          Files.newOutputStream(path, 
              StandardOpenOption.APPEND, 
              StandardOpenOption.CREATE, 
              StandardOpenOption.DSYNC), 
          true, StandardCharsets.UTF_8.name()
      );
    }
    catch(IOException e) {
      StdLog.STDERR.error(e);
      throw new RuntimeException(e.toString(), e);
    }
  }

  private FileLog(LogFormat lf, PrintStream ps, Collection<Level> levels) {
    super(lf, ps, levels);
    Thread closing = new Thread(this);
    closing.setPriority(Thread.MIN_PRIORITY);
    Runtime.getRuntime().addShutdownHook(closing);
  }
  
  @Override
  public void run() {
    printStream.close();
  }
  
}
