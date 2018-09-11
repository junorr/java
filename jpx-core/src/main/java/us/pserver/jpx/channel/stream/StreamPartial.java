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

package us.pserver.jpx.channel.stream;

import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/09/2018
 */
public interface StreamPartial<T> {

  public boolean isActive();
  
  public Optional<T> get();
  
  
  public static <U> StreamPartial<U> activeStream(Optional<U> opt) {
    return new DefaultPartial<>(true, opt); 
  } 
  
  public static <U> StreamPartial<U> activeStream(U val) {
    return new DefaultPartial<>(true, Optional.ofNullable(val)); 
  } 
  
  public static StreamPartial activeStream() {
    return new DefaultPartial(true, Optional.empty()); 
  } 
  
  public static StreamPartial brokenStream() {
    return new DefaultPartial<>(false, Optional.empty()); 
  } 
  
  
  


  public static class DefaultPartial<T> implements StreamPartial<T> {
    
    private final Optional<T> opt;
    
    private final boolean isAlive;
    
    public DefaultPartial(boolean isAlive, Optional<T> opt) {
      this.isAlive = isAlive;
      this.opt = Objects.requireNonNull(opt);
    }
    
    @Override
    public boolean isActive() {
      return isAlive;
    }

    @Override
    public Optional<T> get() {
      return opt;
    }


    @Override
    public int hashCode() {
      int hash = 7;
      hash = 11 * hash + Objects.hashCode(this.opt);
      hash = 11 * hash + (this.isAlive ? 1 : 0);
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
      final DefaultPartial<?> other = (DefaultPartial<?>) obj;
      if (this.isAlive != other.isAlive) {
        return false;
      }
      if (!Objects.equals(this.opt, other.opt)) {
        return false;
      }
      return true;
    }


    @Override
    public String toString() {
      return "StreamPartial{" + "isAlive=" + isAlive + ", value=" + (opt.isPresent() ? opt.get() : null) + '}';
    }
    
  }
  
}
