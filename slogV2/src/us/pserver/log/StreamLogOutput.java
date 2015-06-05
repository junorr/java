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

import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;
import us.pserver.log.output.LogOutput;
import us.pserver.log.output.OutputFormatter;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 05/06/2015
 */
public class StreamLogOutput implements LogOutput {
  
  private OutputFormatter fmt;
  
  private OutputStream out;
  
  private Writer writer;
  
  
  public StreamLogOutput(OutputStream stream) {
    if(stream == null)
      throw new IllegalArgumentException("Invalid OutputStream: "+ stream);
    fmt = BasicOutputFormatter.fileFormatter();
    writer = null;
  }
  
  
  public StreamLogOutput(OutputStream stream, OutputFormatter fmt) {
    this(stream);
    this.setOutputFormatter(fmt);
  }


  @Override
  public LogOutput setLevelEnabled(LogLevel lvl, boolean enabled) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public LogOutput log(String msg, LogLevel lvl) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void close() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public boolean isLevelEnabled(LogLevel lvl) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public OutputFormatter getOutputFormatter() {
    return fmt;
  }


  @Override
  public void setOutputFormatter(OutputFormatter fmt) {
    if(fmt == null) 
      throw new IllegalArgumentException("Invalid OutputFormatter: "+ fmt);
    this.fmt = fmt;
  }

}
