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
package com.jpower.httpsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/06/2012
 */
public class ForwardRequestHandler extends PrinterHandler {
  
  private String fwaddress = "172.24.74.38";
  
  private int fwport = 6060;

  
  public String getFwaddress() {
    return fwaddress;
  }


  public ForwardRequestHandler setFwaddress(String fwaddress) {
    this.fwaddress = fwaddress;
    return this;
  }


  public int getFwport() {
    return fwport;
  }


  public ForwardRequestHandler setFwport(int fwport) {
    this.fwport = fwport;
    return this;
  }
  
  
  public void handle(Socket s) {
    this.setSocket(s).handle();
  }
  
  
  private void transfer(InputStream is, OutputStream os) throws IOException {
    int buf_size = 1024;
    byte[] buf = new byte[buf_size];
    int read = 0;
    while((read = is.read(buf)) >= 0) {
      os.write(buf, 0, read);
    }
    os.flush();
  }
  
  
  private void transfer(StringBuilder sb, OutputStream os) throws IOException {
    os.write(sb.toString().getBytes());
    os.flush();
  }
  
  
  public void handle() {
    this.setCloseSocketOnFinished(false);
    super.handle();
    
    if(fwaddress == null)
      fwaddress = this.getRequestedAddress();
    if(fwport == 0)
      fwport = this.getRequestedPort();
    
    try {
      
      System.out.println(" * Connecting to server...");
      Socket fw = new Socket(fwaddress, fwport);
      System.out.println(" * Forwarding Request...");
      this.transfer(this.getRequest(), fw.getOutputStream());
      System.out.println(" * Reading response...");
      this.transfer(fw.getInputStream(), clientSocket.getOutputStream());
      System.out.println(" * Done");
      fw.close();
      clientSocket.close();
      
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }
  
}
