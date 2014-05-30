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

package com.jpower.inet;

import java.io.File;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/08/2013
 */
public class Attachment {

  private String path;
  
  private String name;
  
  private long length;
  
  
  public Attachment() {
    path = name = null;
    length = 0;
  }


  public String getPath() {
    return path;
  }


  public Attachment setPath(String path) {
    this.path = path;
    return this;
  }


  public String getName() {
    return name;
  }


  public Attachment setName(String name) {
    this.name = name;
    return this;
  }
  
  
  public long getLength() {
    return length;
  }


  public Attachment setLength(long length) {
    this.length = length;
    return this;
  }
  
  
  public File getFile() {
    if(path == null) return null;
    return new File(path);
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 59 * hash + Objects.hashCode(this.name);
    hash = 59 * hash + (int) (this.length ^ (this.length >>> 32));
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Attachment other = (Attachment) obj;
    if (!Objects.equals(this.name, other.name))
      return false;
    if (this.length != other.length)
      return false;
    return true;
  }


  @Override
  public String toString() {
    return "Attachment{" + "path=" + path + ", name=" + name + ", length="+ length+ '}';
  }
  
}
