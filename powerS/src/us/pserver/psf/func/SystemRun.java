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
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 06/09/2012
 */
public class SystemRun implements Runnable, Serializable {
  
  private String[] args;
  
  private InputStream in;
  
  private transient ProcessBuilder pb;
  
  private String output;
  
  private int rcode;
  
  private int limitout;
  
  private boolean waitfor;
  
  private boolean pullout;
  
  
  public SystemRun() {
    args = null;
    pb = new ProcessBuilder();
    in = null;
    output = null;
    rcode = -1;
    waitfor = true;
    limitout = -1;
    pullout = true;
  }
  
  
  public SystemRun(String ... cmargs) {
    this.args = args;
    pb = new ProcessBuilder();
    in = null;
    output = null;
    rcode = -1;
    waitfor = true;
    limitout = -1;
    pullout = true;
  }
  
  
  public boolean isWaitFor() {
    return waitfor;
  }
  
  
  public SystemRun setWaitFor(boolean b) {
    waitfor = b;
    return this;
  }


  public int getOutputLimit() {
    return limitout;
  }
  
  
  public SystemRun setOutputLimit(int lmt) {
    limitout = lmt;
    return this;
  }


  public boolean isPullOutput() {
    return pullout;
  }
  
  
  public SystemRun setPullOutput(boolean b) {
    pullout = b;
    return this;
  }


  public SystemRun setInputStream(InputStream is) {
    in = is;
    return this;
  }
  
  
  public SystemRun setCommandArgs(String ... cargs) {
    args = cargs;
    return this;
  }
  
  
  public InputStream getInputStream() {
    return in;
  }
  
  
  public String[] getCommandArgs() {
    return args;
  }
  
  
  public String buildCommand() {
    StringBuilder sb = new StringBuilder();
    for(String s : args)
      sb.append(s).append(" ");
    sb.deleteCharAt(sb.length() -1);
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
    if(args == null || args.length == 0)
      throw new IllegalStateException("Invalid CommandArgs: "+ Arrays.toString(args));
    
    if(pb == null)
      pb = new ProcessBuilder();
    
    pb.command(args);
    pb.redirectErrorStream(true);
  }
  
  
  @Override
  public void run() {
    try {
      exec();
    } catch(IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  
  
  public void exec() throws IOException {
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
      
    if(pullout) {
      InputStream pin = p.getInputStream();
      int r = pin.read();
      int count = 0;
      while(r >= 0) {
        bos.write(r);
        r = pin.read();
        if(limitout > 0 
            && count++ >= limitout)
          break;
      }
      pin.close();
      output = new String(bos.toByteArray());
    }
    
    if(waitfor) 
      try {
        rcode = p.waitFor();
      } catch(InterruptedException e) {
        throw new IOException(e.getMessage(), e);
      }
  }

  
  public SystemRun parseCommand(String code) {
    if(code == null || code.isEmpty())
      return this;
    
    char[] cs = code.toCharArray();
    System.out.println(new String(cs));
    List<String> list = new LinkedList<>();
    StringBuffer sb = new StringBuffer();
    boolean quote = false;
    for(int i = 0; i < cs.length; i++) {
      if(quote) {
        if(cs[i] == '"' || cs[i] == '\'')
          quote = false;
        else 
          sb.append(cs[i]);
      }
      else if(cs[i] == ' ') {
        if(sb.length() > 0)
          list.add(sb.toString());
        sb = new StringBuffer();
      }
      else {
        if(cs[i] == '"' || cs[i] == '\'')
          quote = true;
        else 
          sb.append(cs[i]);
      }
    }
    if(sb.length() > 0)
      list.add(sb.toString());
    args = new String[list.size()];
    args = list.toArray(args);
    return this;
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
