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

package us.pserver.sdb.filedriver;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import us.pserver.insane.Checkup;
import us.pserver.insane.Sane;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/12/2016
 */
public interface IndexUpdate<T extends Serializable> {

  public int update(String name, Predicate<T> prd, T newValue);
  
  public int update(Predicate<Index<T>> prd, Index<T> newIndex);
  
  public boolean updateOne(String name, Predicate<T> prd, T newValue);
  
  public boolean updateOne(Predicate<Index<T>> prd, Index<T> newIndex);
  
  
  public static <U extends Serializable> IndexUpdate<U> of(Map<String, List<Index<U>>> store, ReentrantLock lock) {
    return new DefIndexUpdate(store, lock);
  }
  
  
  
  
  
  public static class DefIndexUpdate<T extends Serializable> implements IndexUpdate<T> {
    
    private final Map<String, List<Index<T>>> store;
    
    private final ReentrantLock lock;
    
    
    public DefIndexUpdate(Map<String, List<Index<T>>> store, ReentrantLock lock) {
      this.store = Sane.of(store)
          .with("Bad Null Map")
          .get(Checkup.isNotNull());
      this.lock = Sane.of(lock)
          .with("Bad Null ReentrantLock")
          .get(Checkup.isNotNull());
    }


    @Override
    public int update(String name, Predicate<T> prd, T newValue) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public int update(Predicate<Index<T>> prd, Index<T> newIndex) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public boolean updateOne(String name, Predicate<T> prd, T newValue) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public boolean updateOne(Predicate<Index<T>> prd, Index<T> newIndex) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  }
  
}
