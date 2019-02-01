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

package us.pserver.micron.config;

import io.helidon.config.Config;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import us.pserver.tools.Match;
import us.pserver.micron.security.User;
import us.pserver.micron.security.impl.UserImpl;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/01/2019
 */
public class UserConfigHandler implements InvocationHandler, User {
  
  private final Config cfg;
  
  public UserConfigHandler(Config cfg) {
    this.cfg = Match.notNull(cfg).getOrFail("Bad null Config");
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    switch(method.getName()) {
      case "getName":
        return getName();
      case "getFullName":
        return getFullName();
      case "getEmail":
        return getEmail();
      case "getHash":
        return getHash();
      case "getBirth":
        return getBirth();
      case "getCreated":
        return getCreated();
      case "edit":
        return edit();
      case "hashCode":
        return hashCode();
      case "equals":
        return equals(args[0]);
      case "toString":
        return toString();
      case "authenticate":
        if(User.class.isAssignableFrom(args[0].getClass())) {
          return authenticate((User)args[0]);
        }
        else if(String.class.isAssignableFrom(args[0].getClass()) 
            && String.class.isAssignableFrom(args[1].getClass())) {
          return authenticate((String)args[0], (String)args[1]);
        }
        else if(String.class.isAssignableFrom(args[0].getClass()) 
            && args[1].getClass().isArray()) {
          return authenticate((String)args[0], (char[])args[1]);
        }
        else {
          throw new UnsupportedOperationException("Unknown method: " + getMethodString(method, args));
        }
      default:
        throw new UnsupportedOperationException("Unknown method: " + getMethodString(method, args));
    }
  }
    
    
  private String getMethodString(Method meth, Object[] args) {
    StringBuilder msg = new StringBuilder(meth.getName())
        .append("( ");
    if(args == null || args.length < 1) {
      return msg.append(")").toString();
    }
    for(int i = 0; i < args.length; i++) {
      msg.append(args[i].getClass().getSimpleName()).append(", ");
    }
    return msg.delete(msg.length() - 2, msg.length()).append(" )").toString();
  }


  @Override
  public String getFullName() {
    return cfg.get("fullName").asString().get();
  }


  @Override
  public String getEmail() {
    return cfg.get("email").asString().get();
  }


  @Override
  public LocalDate getBirth() {
    return cfg.get("birth").as(LocalDate.class).get();
  }


  @Override
  public String getHash() {
    return User.builder()
        .setName(getName())
        .setEmail(getEmail())
        .setPassword(cfg.get("password").asString().get().toCharArray())
        .build()
        .getHash();
  }


  @Override
  public boolean authenticate(String name, String hash) {
    return authenticate(User.builder()
        .setName(getName())
        .setEmail(getEmail())
        .setHash(hash)
        .build()
    );
  }


  @Override
  public boolean authenticate(String name, char[] password) {
    return authenticate(User.builder()
        .setName(getName())
        .setEmail(getEmail())
        .setPassword(password)
        .build()
    );
  }


  @Override
  public boolean authenticate(User user) {
    return user != null
        && this.getName().equals(user.getName())
        && this.getHash().equals(user.getHash());
  }


  @Override
  public UserBuilder edit() {
    return User.builder()
        .setName(getName())
        .setEmail(getEmail())
        .setFullName(getFullName())
        .setPassword(cfg.get("password").asString().get().toCharArray())
        .setBirth(getBirth())
        .setCreated(getCreated());
  }


  @Override
  public String getName() {
    return cfg.get("name").asString().get();
  }


  @Override
  public Instant getCreated() {
    return cfg.get("created").as(Instant.class).get();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + Objects.hashCode(getName());
    hash = 41 * hash + Objects.hashCode(getEmail());
    hash = 41 * hash + Objects.hashCode(getHash());
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(!UserImpl.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final UserImpl other = (UserImpl) obj;
    if(!Objects.equals(getName(), other.getName())) {
      return false;
    }
    if(!Objects.equals(getEmail(), other.getEmail())) {
      return false;
    }
    return Objects.equals(getHash(), other.getHash());
  }
  
  @Override
  public String toString() {
    return "User{" + "name=" + getName() + ", fullName=" + getFullName() + ", email=" + getEmail() + ", birth=" + getBirth() + ", hash=" + getHash() + ", created=" + getCreated() + '}';
  }
  
}
