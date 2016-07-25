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

package us.pserver.fastgear;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/06/2016
 */
public interface Holder<T> {

  public void set(T t);
  
  public T get();
  
  public boolean isSetted();
  
  public Holder<T> sync();
  
  public static <U> Holder<U> sync(U u) {
    return new SyncHolder<>(u);
  }
  
  public static <U> Holder<U> of(U u) {
    return new DefHolder(u);
  }
  
  
  
  public static class DefHolder<T> implements Holder<T> {

    private T obj;
    
    public DefHolder(T t) {
      this.obj = t;
    }
    
    @Override
    public void set(T t) {
      this.obj = t;
    }

    @Override
    public T get() {
      return this.obj;
    }

    @Override
    public boolean isSetted() {
      return this.obj != null;
    }
    
    @Override
    public Holder<T> sync() {
      return new SyncHolder(this);
    }
    
  }
  
  
  
  public static class SyncHolder<T> implements Holder<T> {

    private Holder<T> hold;
    
    private final ReadWriteLock lock;
    
    public SyncHolder(T t) {
      this(new DefHolder(t));
    }
    
    public SyncHolder(Holder<T> hold) {
      this.hold = hold;
      lock = new ReentrantReadWriteLock();
    }
    
    @Override
    public void set(T t) {
      lock.writeLock().lock();
      try {
        hold.set(t);
      } finally {
        lock.writeLock().unlock();
      }
    }

    @Override
    public T get() {
      lock.readLock().lock();
      try {
        return hold.get();
      } finally {
        lock.readLock().unlock();
      }
    }

    @Override
    public boolean isSetted() {
      lock.readLock().lock();
      try {
        return hold.isSetted();
      } finally {
        lock.readLock().unlock();
      }
    }
    
    @Override
    public Holder<T> sync() {
      return this;
    }
    
  }
  

}
