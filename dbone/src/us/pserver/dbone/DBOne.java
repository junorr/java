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

package us.pserver.dbone;

import java.util.List;
import java.util.function.Predicate;
import us.pserver.dbone.internal.Index;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/09/2017
 */
public interface DBOne extends AutoCloseable {

  public Index<String> store(Object obj) throws DBOneException;
  
  public List<Index<String>> store(Object ... objs) throws DBOneException;
  
  public boolean remove(Index<String> idx) throws DBOneException;
  
  public Index<String> update(Index<String> idx, Object obj) throws DBOneException;
  
  public <T> T get(Index<String> idx) throws DBOneException;
  
  public <T> List<Selected<T>> select(Class<T> cls, Predicate<T> prd) throws DBOneException;
  
  public <T,V> List<Selected<T>> selectIndexed(Class<T> cls, String name, V value) throws DBOneException;
  
  @Override
  public void close() throws DBOneException;
  
}
