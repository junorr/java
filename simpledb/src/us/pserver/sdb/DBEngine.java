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

package us.pserver.sdb;

import java.util.List;
import us.pserver.sdb.engine.StorageEngine;
import us.pserver.sdb.query.Query;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 29/12/2014
 */
public interface DBEngine<T> {

  public StorageEngine getEngine();
  
  public void setRemoveOnCascade(boolean bool);
  
  public boolean isRemoveOnCascade();
  
  public void close();
  
  public Object put(T t);
  
  
  public Object get(long id);
  
  public List get(T t);
  
  public Object getOne(T t);
  
  public List get(Query q);
  
  public Object getOne(Query q);
  
  public List get(String label, int limit);
  
  
  public Object remove(long id);
  
  public boolean remove(T t);
  
  public List removeAll(Query q);
  
  public Object removeOne(Query q);
  
  
  public List join(Query q, List ls);
  
}
