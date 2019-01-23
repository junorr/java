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
import java.time.LocalDate;
import us.pserver.micron.security.User;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/01/2019
 */
public interface IUser {

  public String getName();
  
  public String getFullName();
  
  public String getEmail();
  
  public String getHash();
  
  public LocalDate getBirth();
  
  public Instant getCreated();
  
  
  public IBuilder edit();
  
  public boolean authenticate(String name, String hash);
  
  public boolean authenticate(String name, char[] password);
  
  public <U extends IUser> boolean authenticate(U user);
  
  
  
  public static IBuilder build() {
    return User.builder();
  }
  
  
  
  
  
  public static interface IBuilder {
    
    public String getName();
    
    public IBuilder setName(String name);
    
    
    public String getEmail();
    
    public IBuilder setEmail(String email);
    
    
    public String getFullName();
    
    public IBuilder setFullName(String fullName);
    
    
    public String getHash();
    
    public IBuilder setHash(String hash);
    
    public IBuilder setPassword(char[] password);
    
    
    public LocalDate getBirth();
    
    public IBuilder setBirth(LocalDate birth);
    
    
    public Instant getCreated();
    
    public IBuilder setCreated(Instant created);
    
    
    public IUser build();
    
  }
  
}
