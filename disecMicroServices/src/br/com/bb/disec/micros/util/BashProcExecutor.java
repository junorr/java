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

package br.com.bb.disec.micros.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/03/2017
 */
public class BashProcExecutor {
  
  public static final String BASH = "/bin/bash";
  
  public static final String BASH_C = "-c";
  
  
  private final String cmd;
  
  private final ProcessBuilder builder;
  
  private final Buffer out;
  
  private Process proc;
  
  
  public BashProcExecutor(String cmd) {
    if(cmd == null || cmd.length() < 1) {
      throw new IllegalArgumentException("Bad null command String");
    }
    this.cmd = cmd;
    this.builder = new ProcessBuilder(BASH, BASH_C, cmd);
    builder.redirectError(builder.redirectOutput());
    this.out = Buffer.create();
  }
  
  
  public String getCommand() {
    return cmd;
  }
  
  
  public Process getProcess() {
    return proc;
  }
  
  
  public InputStream getInputStream() {
    return (proc != null ? proc.getInputStream() : null);
  }
  
  
  public OutputStream getOutputStream() {
    return (proc != null ? proc.getOutputStream() : null);
  }
  
  
  public BashProcExecutor start() throws IOException {
    proc = builder.start();
    return this;
  }
  
  
  public int waitExit() {
    if(proc == null) return -1;
    try { return proc.waitFor(); }
    catch(InterruptedException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
  
  
  public String waitOutput() throws IOException {
    if(proc == null) return null;
    InputStream in = proc.getInputStream();
    int r = 0;
    byte[] bs = new byte[512];
    while((r = in.read(bs)) != -1) {
      out.write(bs, 0, r);
    }
    try { proc.waitFor(); }
    catch(InterruptedException e) {
      throw new IOException(e.toString(), e);
    }
    return new String(out.toBytes(), StandardCharsets.UTF_8);
  }
  
  
  public BashProcExecutor transferOutput(OutputStream os) throws IOException {
    if(proc == null || os == null) return this;
    InputStream in = proc.getInputStream();
    int r = 0;
    byte[] bs = new byte[512];
    while((r = in.read(bs)) != -1) {
      os.write(bs, 0, r);
      os.flush();
    }
    try { proc.waitFor(); }
    catch(InterruptedException e) {
      throw new IOException(e.toString(), e);
    }
    return this;
  }
  
}
