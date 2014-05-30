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

package us.pserver.psf.func;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/02/2014
 */
public class SystemCommand implements Runnable {

  private Process proc;
  
  private String[] args;
  
  private ByteArrayOutputStream bos;
  
  private int exitCode;
  
  
  public SystemCommand(String cmd) {
    if(cmd == null || cmd.isEmpty())
      throw new IllegalArgumentException(
          "Invalid command ["+ cmd+ "]");
    args = cmd.split(" ");
    proc = null;
    bos = new ByteArrayOutputStream();
    exitCode = -1;
  }
  
  
  public SystemCommand(String ... strs) {
    this();
    args = strs;
  }
  
  
  public SystemCommand() {
    proc = null;
    bos = new ByteArrayOutputStream();
    exitCode = -1;
    args = null;
  }
  
  
  public SystemCommand setCommand(String cmd) {
    if(cmd != null && !cmd.isEmpty())
      args = cmd.split(" ");
    return this;
  }
  
  
  public SystemCommand setCommand(String ... strs) {
    args = strs;
    return this;
  }
  
  
  public int getExitCode() {
    return exitCode;
  }
  
  
  public String getOutput() {
    if(bos.size() > 0)
      return bos.toString();
    return null;
  }
  
  
  public String runCommand() throws IOException {
    if(args == null || args.length < 1)
      throw new IllegalArgumentException(
          "Invalid command ["+ args+ "]");
    
    if(bos.size() > 0) bos.reset();
    
    ProcessBuilder pb = new ProcessBuilder();
    pb.command(args);
    pb.redirectErrorStream(true);
    
    proc = pb.start();
    transfer(proc.getInputStream(), bos);
    
    try { exitCode = proc.waitFor(); }
    catch(InterruptedException e) {
      throw new IOException(e.toString(), e);
    }
    return getOutput();
  }
  
  
  @Override
  public void run() {
    try {
      this.runCommand();
    } catch(IOException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  private void transfer(InputStream in, OutputStream out) throws IOException {
    int read = 0;
    while((read = in.read()) != -1) {
      out.write(read);
    }
  }
  
}
