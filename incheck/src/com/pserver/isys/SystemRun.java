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

package com.pserver.isys;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 06/09/2012
 */
public class SystemRun implements Runnable, Serializable {
  
  private String[] args;
  
  private String command;
  
  private InputStream in;
  
  private transient ProcessBuilder pb;
  
  private String output;
  
  private int rcode;
  
  
  public SystemRun() {
    args = null;
    command = null;
    pb = new ProcessBuilder();
    in = null;
    output = null;
    rcode = -1;
  }
  
  
  public SystemRun(String cmd, String ... args) {
    command = cmd;
    this.args = args;
    pb = new ProcessBuilder();
    in = null;
    output = null;
  }


  public SystemRun setInputStream(InputStream is) {
    in = is;
    return this;
  }
  
  
  public SystemRun setArgs(String ... args) {
    this.args = args;
    return this;
  }
  
  
  public SystemRun setCommand(String cmd) {
    command = cmd;
    return this;
  }
  
  
  public InputStream getInputStream() {
    return in;
  }
  
  
  public String getCommand() {
    return command;
  }
  
  
  public String buildCommand() {
    StringBuilder sb = new StringBuilder();
    sb.append(command);
    for(String s : args)
      sb.append(" ").append(s);
    return sb.toString();
  }
  
  
  public String[] getArgs() {
    return args;
  }
  
  
  public int getReturnCode() {
    return rcode;
  }
  
  
  public String getOutput() {
    return output;
  }
  
  
  private void setProcessBuilder() {
    if(command == null || command.trim().isEmpty())
      throw new IllegalStateException("Invalid Command: "+ command);
    
    if(pb == null)
      pb = new ProcessBuilder();
    
    String[] cs = new String[args.length +1];
    cs[0] = command;
    for(int i = 1; i < cs.length; i++) {
      cs[i] = args[i-1];
    }
    pb.command(cs);
    pb.redirectErrorStream(true);
  }
  
  
  @Override
  public void run() {
    try {
      this.setProcessBuilder();
      Process p = pb.start();
      
      ByteArrayOutputStream bos = 
          new ByteArrayOutputStream();
      
      if(in != null) {
        OutputStream pout = p.getOutputStream();
        int r = in.read();
        while(r >= 0) {
          pout.write(r);
          r = in.read();
        }
        pout.flush();
        pout.close();
        in.close();
      }
      
      InputStream pin = p.getInputStream();
      int r = pin.read();
      while(r >= 0) {
        bos.write(r);
        r = pin.read();
      }
      pin.close();
      
      output = new String(bos.toByteArray());
      
      rcode = p.waitFor();
      
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }


  @Override
  public String toString() {
    return this.buildCommand();
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.buildCommand());
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    final SystemRun other = (SystemRun) obj;
    if(!Objects.equals(this.buildCommand(), other.buildCommand())) {
      return false;
    }
    return true;
  }
  
  
  public static void main(String[] args) {
    SystemRun sr = new SystemRun("cmd", "/c", "dir", "d:");
    sr.run();
    System.out.println(sr.getOutput());
    System.out.println("return code: "+ sr.getReturnCode());
  }

}
