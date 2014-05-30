/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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
package badraadv.bean;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Password encapsula um hash md5 de uma senha.
 * Utiliza o método <code>equals</code>, que aceita
 * outra instância de <code>Password</code> ou uma
 * <code>String</code> como parâmetro, para verificar
 * duas senhas.
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/12/2011
 */
public class Password implements Serializable {
  
  private byte[] pass;
  
  
  public Password() {
    pass = null;
  }
  
  
  public Password(String pass) {
    this();
    this.setPassword(pass);
  }
  
  
  public byte[] getBytes() {
    return pass;
  }
  
  
  public Password setPassword(String pass) {
    if(pass == null || pass.trim().equals(""))
      throw new IllegalArgumentException("Invalid Password: "+ pass);
    
    MessageDigest md5 = null;
    
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException ex) {}
    
    if(md5 == null) 
      throw new IllegalStateException("MD5 not initialized.");
    
    md5.update(pass.getBytes());
    this.pass = md5.digest();
    
    this.generateHash(this.pass);
    
    return this;
  }
  
  
  private int generateHash(byte[] bs) {
    if(bs == null || bs.length == 0)
      return 0;
    
    String sh = "";
    for(byte b : bs)
      sh += b;
    
    sh = sh.replaceAll("-", "");
    sh = sh.substring(0, 9) +
        sh.substring(sh.length() / 2, sh.length() / 2 + 1) +
        sh.substring(sh.length() - 9);
    
    return (int) Long.parseLong(sh);
  }
  
  
  @Override
  public int hashCode() {
    if(pass == null
        || pass.length == 0)
      return 0;
    return this.generateHash(pass);
  }
  
  
  @Override
  public boolean equals(Object o) {
    if(o == null) return false;
    if(this.hashCode() == 0) return false;
    
    if(o instanceof String) {
      return this.equals(new Password(o.toString()));
    } else if(o instanceof Password) {
      Password p = (Password) o;
      if(p.hashCode() == 0) return false;
      return Arrays.equals(this.pass, p.getBytes());
    } else return false;
  }


  @Override
  public String toString() {
    if(this.getBytes() == null) return "null";
    return String.valueOf(this.hashCode());
  }
  
}
