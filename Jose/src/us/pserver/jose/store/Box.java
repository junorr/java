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

package us.pserver.jose.store;

import java.util.Objects;
import us.pserver.jose.Region;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/03/2017
 */
public interface Box<T> {

  public Class<T> cclass();
  
  public UID uid();
  
  public T content();
  
  public Region region();
  
  public Region nextBox();
  
  public Box<T> withRegion(Region r);
  
  public Box<T> withNextBox(Region r);
  
  
  public static <U> Box<U> box(U cont) {
    return new BoxImpl(cont, UID.of(cont));
  }
  
  public static <U> Box<U> box(U cont, Region reg) {
    return new BoxImpl(cont, UID.of(cont), reg, null);
  }
  
  public static <U> Box<U> box(U cont, Region reg, Region next) {
    return new BoxImpl(cont, UID.of(cont), reg, next);
  }
  
  
  
  
  
  public static class BoxImpl<T> implements Box<T> {
    
    private final Class cclass;
    
    private final UID uid;
    
    private final T content;
    
    private final Region region;
    
    private final Region next;
    
    
    private BoxImpl() {
      cclass = null;
      uid = null;
      content = null;
      region = null;
      next = null;
    }
    
    
    private BoxImpl(T cont, UID uid) {
      if(cont == null) {
        throw new IllegalArgumentException("Bad Null Content");
      }
      if(uid == null) {
        throw new IllegalArgumentException("Bad Null UID");
      }
      this.content = cont;
      this.cclass = this.content.getClass();
      this.uid = uid;
      this.region = null;
      this.next = null;
    }


    private BoxImpl(T cont, UID uid, Region reg, Region next) {
      if(cont == null) {
        throw new IllegalArgumentException("Bad Null Content");
      }
      if(uid == null) {
        throw new IllegalArgumentException("Bad Null UID");
      }
      if(reg == null) {
        throw new IllegalArgumentException("Bad Null Region");
      }
      this.content = cont;
      this.cclass = this.content.getClass();
      this.uid = uid;
      this.region = reg;
      this.next = next;
    }


    @Override
    public Class<T> cclass() {
      return this.cclass;
    }


    @Override
    public UID uid() {
      return this.uid;
    }


    @Override
    public T content() {
      return this.content;
    }


    @Override
    public Region region() {
      return this.region;
    }


    @Override
    public Region nextBox() {
      return this.next;
    }


    @Override
    public Box<T> withRegion(Region r) {
      return new BoxImpl(content, uid, r, next);
    }


    @Override
    public Box<T> withNextBox(Region r) {
      return new BoxImpl(content, uid, region, r);
    }


    @Override
    public int hashCode() {
      int hash = 3;
      hash = 43 * hash + Objects.hashCode(this.uid);
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
      final BoxImpl<?> other = (BoxImpl<?>) obj;
      return Objects.equals(this.uid, other.uid);
    }
    
  }
  
}
