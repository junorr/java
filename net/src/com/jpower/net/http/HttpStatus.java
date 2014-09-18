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

package com.jpower.net.http;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 19/07/2013
 */
public class HttpStatus {
  
  public static final String VERSION = "HTTP";
  
  private String status;
  
  private String version;
  
  private int code;
  
  
  public HttpStatus(String header) {
    if(header != null && header.contains(VERSION)) {
      String[] s = header.split(" ");
      version = s[0];
      status = s[2];
      try { 
        code = Integer.parseInt(s[1]);
      } catch(NumberFormatException ex) {
        code = -1;
      }
    }
  }


  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    this.status = status;
  }


  public int getCode() {
    return code;
  }


  public void setCode(int code) {
    this.code = code;
  }


  public String getVersion() {
    return version;
  }


  public void setVersion(String version) {
    this.version = version;
  }
  
  
  public String toString() {
    if(version == null
        || status == null)
      return null;
    return version.concat(" ")
        .concat(String.valueOf(code))
        .concat(" ").concat(status);
  }

  
  public static void main(String[] args) {
    HttpStatus st = new HttpStatus("HTTP/1.1 302 Found");
    System.out.println("* version   : "+ st.getVersion());
    System.out.println("* code      : "+ st.getCode());
    System.out.println("* status    : "+ st.getStatus());
    System.out.println("* HttpStatus: "+ st.toString());
  }
  
}
