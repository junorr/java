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

package us.pserver.micron.security.api;

import java.time.Instant;
import java.util.Set;
import us.pserver.micron.security.Resource;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public interface IResource {

  public String getName();
  
  public Instant getCreated();
  
  public Set<String> getRoles();
  
  public boolean contains(IRole role);
  
  public IBuilder edit();
  
  
  
  public static IBuilder builder() {
    return Resource.builder();
  }
  
  
  
  
  
  public static interface IBuilder {
    
    public String getName();
    
    public IBuilder setName(String name);
    
    
    public Instant getCreated();
    
    public IBuilder setCreated(Instant created);
    
    
    public Set<String> getRoles();
    
    public IBuilder setRoles(Set<String> usernames);
    
    public IBuilder clearRoles();
    
    public IBuilder addRole(String role);
    
    public IBuilder addRole(IRole role);
    
    
    public IResource build();
    
  }
  
}
