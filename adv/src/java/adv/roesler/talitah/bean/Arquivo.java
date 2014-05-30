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
package adv.roesler.talitah.bean;

import badraadv.User;
import java.io.File;
import java.io.Serializable;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/12/2011
 */
public class Arquivo implements Serializable {
  
  private User owner;
  
  private String name;
  
  private String description;
  
  private File path;
  
  private long processo;
  
  private int hash;
  
  
  public Arquivo() {
    owner = null;
    name = null;
    description = null;
    path = null;
    hash = 0;
  }
  
  
  public Arquivo(File path) {
    this();
    this.path = path;
  }


  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public User getOwner() {
    return owner;
  }


  public void setOwner(User owner) {
    this.owner = owner;
  }


  public File getPath() {
    return path;
  }


  public void setPath(File path) {
    this.path = path;
  }
  
  
  public void setNumeroProcesso(Processo p) {
    if(p == null) return;
    this.processo = p.getNumero();
  }
  
  
  public void setProcesso(long p) {
    this.processo = p;
  }
  
  
  public long getProcesso() {
    return processo;
  }
  
  
  public boolean isOwner(User u) {
    if(u == null || owner == null)
      return false;
    return owner.equals(u);
  }


  public void createHash() {
    hash = 3;
    hash = 53 * hash + (this.path != null ? this.path.hashCode() : 0);
  }
  
  
  public int getHash() {
    return hash;
  }
  
  
  @Override
  public int hashCode() {
    return hash;
  }
  
  
  @Override
  public boolean equals(Object o) {
    if(o != null && o instanceof Arquivo) {
      return this.hashCode()
          == o.hashCode();
    }
    return false;
  }
  
}
