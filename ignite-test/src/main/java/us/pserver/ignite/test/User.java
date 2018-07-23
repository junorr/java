package us.pserver.ignite.test;

import java.util.Arrays;
import java.util.Objects;
import us.pserver.tools.Hash;

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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/07/2018
 */
public class User {

  private String name;
  
  private byte[] hash;


  public User() {
  }
  
  public User(String name, byte[] hash) {
    this.name = name;
    this.hash = hash;
  }
  
  public User(String name, String secret) {
    this.name = name;
    this.hash = Hash.sha256().put(secret).getBytes();
  }
  
  public String getName() {
    return name;
  }
  
  public User setName(String name) {
    this.name = name;
    return this;
  }
  
  public byte[] getHash() {
    return hash;
  }
  
  public User setHash(byte[] hash) {
    this.hash = hash;
    return this;
  }
  
  public boolean authenticate(byte[] bs) {
    return Arrays.equals(bs, bs);
  } 
  
  public boolean authenticate(String secret) {
    return authenticate(Hash.sha256().put(secret).getBytes());
  } 
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 83 * hash + Objects.hashCode(this.name);
    hash = 83 * hash + Arrays.hashCode(this.hash);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Arrays.equals(this.hash, other.hash)) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return "User{" + "name=" + name + ", hash=" + Hash.bytesToHex(hash) + '}';
  }
  
}
