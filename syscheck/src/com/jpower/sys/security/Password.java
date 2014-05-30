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


package com.jpower.sys.security;

import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @version 0.0 - 04/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class Password implements Serializable {
  
  private String hash;
  
  
  public Password() {
    hash = null;
  }
  
  
  public Password(String passwd) {
    this.setPassword(passwd);
  }
  
  
  public static Password rawPassword(String hash) {
    if(hash == null || hash.isEmpty())
      throw new IllegalArgumentException(
          "Invalid raw hash: "+ hash);
    Password p = new Password();
    p.hash = hash;
    return p;
  }
  
  
  public byte[] getBytes() {
    if(isEmpty()) return null;
    return SHA256.fromHexString(hash);
  }
  
  
  public String getHash() {
    return hash;
  }
  
  
  public SecretKeySpec getAES256KeySpec() {
    if(isEmpty()) return null;
    return new SecretKeySpec(getBytes(), "AES");
  }
  
  
  public boolean isEmpty() {
    return hash == null || hash.isEmpty();
  }
  
  
  public boolean validate(Password p) {
    return hash != null && p != null
        && p.hash != null
        && hash.equals(p.hash);
  }
  
  
  private Password setPassword(String passwd) {
    if(passwd != null && !passwd.isEmpty()) {
      hash = SHA256.toHexString(SHA256.hash(passwd));
      passwd = null;
      System.gc();
    }
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 71 * hash + Objects.hashCode(this.hash);
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
    final Password other = (Password) obj;
    if(!Objects.equals(this.hash, other.hash)) {
      return false;
    }
    return true;
  }
  
  
  public static void main(String[] args) {
    Password p = new Password();
    Scanner sc = new Scanner(System.in);
    System.out.print("* password: ");
    p.setPassword(sc.nextLine());
    if(p.isEmpty()) {
      System.out.println();
      System.out.println("# Invalid password!");
    }
    else {
      System.out.println("* Good password!");
      System.out.println("* hash: "+ p.hash);
      System.out.print("* Validate: ");
      Password p2 = new Password(sc.nextLine());
      System.out.println();
      if(p.validate(p2)) {
        System.out.println("* Success! Passwords matches.");
      }
      else {
        System.out.println("# Bad password! Dont match.");
      }
    }
  }
  
}
